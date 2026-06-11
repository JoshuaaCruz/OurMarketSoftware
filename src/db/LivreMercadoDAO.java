package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import model.Cupom;
import model.OurMarket;
import model.categoria_produto.Categoria;
import model.categoria_produto.Categoria_if;
import model.categoria_produto.ItemProduto;
import model.categoria_produto.Produto;
import model.cliente.Cliente;

public class LivreMercadoDAO {
    private final Base base;
    private final ClienteDAO clienteDAO;
    private final CategoriaDAO categoriaDAO;
    private final ProdutoDAO produtoDAO;

    public LivreMercadoDAO(Base base) {
        this.base = base;
        this.clienteDAO = new ClienteDAO(base);
        this.categoriaDAO = new CategoriaDAO(base);
        this.produtoDAO = new ProdutoDAO(base);
    }

    public void salvar(OurMarket mercado) throws SQLException {
        if (mercado == null) {
            throw new IllegalArgumentException("OurMarket nao pode ser nulo.");
        }

        base.inicializar();
        salvarCupons(mercado);
        if (mercado.getCategoriaRaiz() instanceof Categoria) {
            categoriaDAO.substituirPor((Categoria) mercado.getCategoriaRaiz());
        }
        for (Cliente cliente : mercado.getClientes()) {
            clienteDAO.salvar(cliente);
        }
        produtoDAO.removerOrfaos();
    }

    public void carregarEm(OurMarket mercado) throws SQLException {
        if (mercado == null) {
            throw new IllegalArgumentException("OurMarket nao pode ser nulo.");
        }

        base.inicializar();
        salvarCupons(mercado);
        carregarCuponsEm(mercado);
        List<Cliente> clientes = clienteDAO.listarTodos();
        if (clientes.isEmpty()) {
            mercado.inicializarDadosPadrao();
            salvar(mercado);
            return;
        }

        for (Cliente cliente : clientes) {
            mercado.adicionarCliente(cliente);
        }
        carregarCategoriasEm(mercado.getCategoriaRaiz(), "Geral");
        reconectarProdutos(mercado);
    }

    public void carregarCategoriasEm(Categoria_if destino, String nomeCategoriaRaiz) throws SQLException {
        if (destino == null) {
            throw new IllegalArgumentException("Categoria de destino nao pode ser nula.");
        }

        categoriaDAO.buscarRaizPorNome(nomeCategoriaRaiz).ifPresent(categoria -> {
            for (Categoria_if subcategoria : categoria.getSubcategorias()) {
                if (subcategoria instanceof Categoria) {
                    destino.addSubCategoria((Categoria) subcategoria);
                }
            }
        });
    }

    private void reconectarProdutos(OurMarket mercado) {
        for (Cliente cliente : mercado.getClientes()) {
            for (ItemProduto item : cliente.getEstoque().getItens()) {
                Produto produto = item.getProduto();
                produto.setVendedor(cliente);
                Categoria categoria = encontrarCategoria(mercado.getCategoriaRaiz(), produto.getCategoria());
                produto.setCategoria(categoria);
                if (categoria != null) {
                    categoria.addProduto(produto);
                }
            }
        }

        for (Cliente cliente : mercado.getClientes()) {
            for (ItemProduto item : cliente.getCarrinho().getItens()) {
                reconectarItem(mercado, item);
            }
            for (int i = 0; i < cliente.getListaDesejos().size(); i++) {
                Produto produto = reconectarProduto(mercado, cliente.getListaDesejos().get(i));
                if (produto != null) {
                    cliente.getListaDesejos().set(i, produto);
                }
            }
            for (ItemProduto item : cliente.getPedidosComprados()) {
                reconectarItem(mercado, item);
            }
            for (ItemProduto item : cliente.getProdutosVendidos()) {
                reconectarItem(mercado, item);
            }
        }
    }

    private void reconectarItem(OurMarket mercado, ItemProduto item) {
        Produto produto = reconectarProduto(mercado, item.getProduto());
        if (produto != null) {
            item.setProduto(produto);
            item.setVendedor(produto.getVendedor());
            adicionarProdutoNaCategoria(produto);
        }
    }

    private Produto reconectarProduto(OurMarket mercado, Produto produto) {
        if (produto == null) {
            return null;
        }

        Cliente vendedor = encontrarClientePorCpf(mercado, produto.getVendedor());
        Produto produtoDoEstoque = encontrarProdutoNoEstoque(vendedor, produto);
        if (produtoDoEstoque != null) {
            return produtoDoEstoque;
        }
        if (vendedor != null) {
            produto.setVendedor(vendedor);
        }
        Categoria categoria = encontrarCategoria(mercado.getCategoriaRaiz(), produto.getCategoria());
        produto.setCategoria(categoria);
        return produto;
    }

    private void adicionarProdutoNaCategoria(Produto produto) {
        if (produto != null && produto.getCategoria() != null) {
            produto.getCategoria().addProduto(produto);
        }
    }

    private void salvarCupons(OurMarket mercado) throws SQLException {
        String sql = "INSERT INTO cupom (codigo, desconto, descricao) VALUES (?, ?, ?) "
                + "ON CONFLICT(codigo) DO UPDATE SET desconto = excluded.desconto, descricao = excluded.descricao";
        try (Connection connection = base.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Cupom cupom : mercado.getCupons()) {
                statement.setString(1, cupom.getCodigo());
                statement.setDouble(2, cupom.getDesconto());
                statement.setString(3, cupom.getDescricao());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void carregarCuponsEm(OurMarket mercado) throws SQLException {
        try (Connection connection = base.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT codigo, desconto, descricao FROM cupom ORDER BY codigo");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                mercado.adicionarCupom(new Cupom(
                        resultSet.getString("codigo"),
                        resultSet.getDouble("desconto"),
                        resultSet.getString("descricao")));
            }
        }
    }

    private Cliente encontrarClientePorCpf(OurMarket mercado, Cliente referencia) {
        if (referencia == null || referencia.getCPF() == null) {
            return null;
        }
        for (Cliente cliente : mercado.getClientes()) {
            if (referencia.getCPF().equals(cliente.getCPF())) {
                return cliente;
            }
        }
        return null;
    }

    private Produto encontrarProdutoNoEstoque(Cliente vendedor, Produto referencia) {
        if (vendedor == null || referencia == null) {
            return null;
        }
        for (ItemProduto item : vendedor.getEstoque().getItens()) {
            Produto produto = item.getProduto();
            if (Objects.equals(produto.getNome(), referencia.getNome())
                    && Objects.equals(produto.getDescricao(), referencia.getDescricao())
                    && Double.compare(produto.getPrecoBase(), referencia.getPrecoBase()) == 0) {
                return produto;
            }
        }
        return null;
    }

    private Categoria encontrarCategoria(Categoria_if atual, Categoria referencia) {
        if (referencia == null || atual == null) {
            return null;
        }
        if (atual instanceof Categoria && atual.getNome().equals(referencia.getNome())) {
            return (Categoria) atual;
        }
        for (Categoria_if subcategoria : atual.getSubcategorias()) {
            Categoria encontrada = encontrarCategoria(subcategoria, referencia);
            if (encontrada != null) {
                return encontrada;
            }
        }
        return referencia;
    }
}
