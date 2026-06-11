package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import model.categoria_produto.Categoria;
import model.categoria_produto.ColecaoProdutos;
import model.categoria_produto.Produto;
import model.cliente.Cliente;

public class ProdutoDAO {
    public enum TipoColecao {
        ESTOQUE,
        CARRINHO
    }

    private final Base base;
    private final CategoriaDAO categoriaDAO;

    public ProdutoDAO(Base base) {
        this.base = base;
        this.categoriaDAO = new CategoriaDAO(base);
    }

    public Optional<Produto> buscarItemPorId(int idItemProduto) throws SQLException {
        try (Connection connection = base.getConnection()) {
            return buscarItemPorId(connection, idItemProduto);
        }
    }

    int salvarReferenciaProduto(Connection connection, Produto produto, int quantidade) throws SQLException {
        validarProduto(produto);
        return salvarItem(connection, produto, quantidade);
    }

    public void removerOrfaos() throws SQLException {
        try (Connection connection = base.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "DELETE FROM item_produto WHERE id_item_produto NOT IN "
                            + "(SELECT id_item_produto FROM cliente_produto "
                            + "UNION SELECT id_item_produto FROM cliente_lista_desejos "
                            + "UNION SELECT id_item_produto FROM cliente_historico_produto)"
            );
            statement.executeUpdate(
                    "DELETE FROM produto WHERE id_produto NOT IN "
                            + "(SELECT id_produto FROM item_produto)"
            );
        }
    }

    public void salvarColecao(String cpfCliente, ColecaoProdutos colecao, TipoColecao tipo) throws SQLException {
        if (cpfCliente == null || cpfCliente.isBlank()) {
            throw new IllegalArgumentException("CPF do cliente precisa ser informado.");
        }
        if (colecao == null || tipo == null) {
            return;
        }

        try (Connection connection = base.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement delete = connection.prepareStatement(
                        "DELETE FROM cliente_produto WHERE cpf_cliente = ? AND tipo_colecao = ?")) {
                    delete.setString(1, cpfCliente);
                    delete.setString(2, tipo.name());
                    delete.executeUpdate();
                }

                for (Map.Entry<Produto, Integer> entry : contarProdutos(colecao).entrySet()) {
                    int idItemProduto = salvarItem(connection, entry.getKey(), entry.getValue());
                    inserirItemColecao(connection, cpfCliente, idItemProduto, tipo, entry.getValue());
                }
                connection.commit();
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public ColecaoProdutos carregarColecao(String cpfCliente, TipoColecao tipo) throws SQLException {
        ColecaoProdutos colecao = new ColecaoProdutos();
        String sql = "SELECT id_item_produto, quantidade FROM cliente_produto "
                + "WHERE cpf_cliente = ? AND tipo_colecao = ? ORDER BY id_item_produto";

        try (Connection connection = base.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cpfCliente);
            statement.setString(2, tipo.name());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Optional<Produto> produto = buscarItemPorId(connection, resultSet.getInt("id_item_produto"));
                    if (produto.isPresent()) {
                        int quantidade = Math.max(1, resultSet.getInt("quantidade"));
                        colecao.adicionarProduto(produto.get(), quantidade);
                    }
                }
            }
        }
        return colecao;
    }

    private int salvarItem(Connection connection, Produto produto, int quantidade) throws SQLException {
        int idProduto = salvarProdutoCatalogo(connection, produto);
        String cpfVendedor = salvarVendedorSeExistir(connection, produto.getVendedor());

        int idItemProduto = buscarIdItem(connection, idProduto, cpfVendedor, produto.getPrecoBase());
        if (idItemProduto > 0) {
            atualizarItem(connection, idItemProduto, quantidade, produto.getNota());
            return idItemProduto;
        }
        return inserirItem(connection, idProduto, cpfVendedor, produto, quantidade);
    }

    private int salvarProdutoCatalogo(Connection connection, Produto produto) throws SQLException {
        Integer idCategoria = salvarCategoriaSeExistir(connection, produto);
        int idProduto = buscarIdProdutoCatalogo(connection, produto, idCategoria);
        if (idProduto > 0) {
            atualizarProdutoCatalogo(connection, idProduto, produto, idCategoria);
            salvarFotos(connection, idProduto, produto);
            produto.setId(idProduto);
            return idProduto;
        }
        return inserirProdutoCatalogo(connection, produto, idCategoria);
    }

    private int inserirProdutoCatalogo(Connection connection, Produto produto, Integer idCategoria)
            throws SQLException {
        String sql = "INSERT INTO produto (nome, descricao, id_categoria, vendas, total_votos) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherProdutoCatalogo(statement, produto, idCategoria);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    int idProduto = keys.getInt(1);
                    produto.setId(idProduto);
                    salvarFotos(connection, idProduto, produto);
                    return idProduto;
                }
            }
        }
        throw new SQLException("Nao foi possivel obter o id do produto salvo.");
    }

    private void atualizarProdutoCatalogo(Connection connection, int idProduto, Produto produto,
            Integer idCategoria) throws SQLException {
        String sql = "UPDATE produto SET nome = ?, descricao = ?, id_categoria = ?, vendas = ?, total_votos = ? WHERE id_produto = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            preencherProdutoCatalogo(statement, produto, idCategoria);
            statement.setInt(6, idProduto);
            statement.executeUpdate();
        }
    }

    private int inserirItem(Connection connection, int idProduto, String cpfVendedor, Produto produto,
            int quantidade) throws SQLException {
        String sql = "INSERT INTO item_produto "
                + "(id_produto, cpf_vendedor, preco_base, quantidade, nota) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, idProduto);
            statement.setString(2, cpfVendedor);
            statement.setDouble(3, produto.getPrecoBase());
            statement.setInt(4, Math.max(1, quantidade));
            statement.setDouble(5, produto.getNota());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Nao foi possivel obter o id do item de produto salvo.");
    }

    private void atualizarItem(Connection connection, int idItemProduto, int quantidade, double nota)
            throws SQLException {
        String sql = "UPDATE item_produto SET quantidade = ?, nota = ? WHERE id_item_produto = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Math.max(1, quantidade));
            statement.setDouble(2, nota);
            statement.setInt(3, idItemProduto);
            statement.executeUpdate();
        }
    }

    private Optional<Produto> buscarItemPorId(Connection connection, int idItemProduto) throws SQLException {
        String sql = "SELECT p.id_produto, p.nome, p.descricao, p.id_categoria, p.vendas, p.total_votos, i.preco_base, i.nota, "
                + "c.cpf, c.nome AS nome_vendedor "
                + "FROM item_produto i "
                + "JOIN produto p ON p.id_produto = i.id_produto "
                + "LEFT JOIN cliente c ON c.cpf = i.cpf_vendedor "
                + "WHERE i.id_item_produto = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idItemProduto);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                Produto produto = new Produto(
                        resultSet.getString("nome"),
                        resultSet.getString("descricao"),
                        resultSet.getDouble("preco_base"));
                produto.setId(resultSet.getInt("id_produto"));
                produto.setNota(resultSet.getDouble("nota"));
                produto.setVendas(resultSet.getInt("vendas"));
                produto.setTotalVotos(resultSet.getInt("total_votos"));
                carregarCategoria(resultSet, produto);
                carregarFotos(connection, resultSet.getInt("id_produto"), produto);

                String cpfVendedor = resultSet.getString("cpf");
                if (cpfVendedor != null) {
                    Cliente vendedor = new Cliente();
                    vendedor.setCPF(cpfVendedor);
                    vendedor.setNome(resultSet.getString("nome_vendedor"));
                    produto.setVendedor(vendedor);
                }
                return Optional.of(produto);
            }
        }
    }

    private void preencherProdutoCatalogo(PreparedStatement statement, Produto produto, Integer idCategoria)
            throws SQLException {
        statement.setString(1, produto.getNome());
        statement.setString(2, produto.getDescricao());
        if (idCategoria == null) {
            statement.setNull(3, java.sql.Types.INTEGER);
        } else {
            statement.setInt(3, idCategoria);
        }
        statement.setInt(4, produto.getVendas());
        statement.setInt(5, produto.getTotalVotos());
    }

    private int buscarIdProdutoCatalogo(Connection connection, Produto produto, Integer idCategoria)
            throws SQLException {
        String sql = idCategoria == null
                ? "SELECT id_produto FROM produto WHERE nome = ? AND descricao IS ? AND id_categoria IS NULL"
                : "SELECT id_produto FROM produto WHERE nome = ? AND descricao IS ? AND id_categoria = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, produto.getNome());
            statement.setString(2, produto.getDescricao());
            if (idCategoria != null) {
                statement.setInt(3, idCategoria);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt("id_produto") : 0;
            }
        }
    }

    private int buscarIdItem(Connection connection, int idProduto, String cpfVendedor, double precoBase)
            throws SQLException {
        String sql = "SELECT id_item_produto FROM item_produto "
                + "WHERE id_produto = ? AND cpf_vendedor IS ? AND preco_base = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idProduto);
            statement.setString(2, cpfVendedor);
            statement.setDouble(3, precoBase);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt("id_item_produto") : 0;
            }
        }
    }

    private void inserirItemColecao(Connection connection, String cpfCliente, int idItemProduto,
            TipoColecao tipo, int quantidade) throws SQLException {
        String sql = "INSERT INTO cliente_produto (cpf_cliente, id_item_produto, tipo_colecao, quantidade) "
                + "VALUES (?, ?, ?, ?) "
                + "ON CONFLICT(cpf_cliente, id_item_produto, tipo_colecao) "
                + "DO UPDATE SET quantidade = excluded.quantidade";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cpfCliente);
            statement.setInt(2, idItemProduto);
            statement.setString(3, tipo.name());
            statement.setInt(4, Math.max(1, quantidade));
            statement.executeUpdate();
        }
    }

    private Integer salvarCategoriaSeExistir(Connection connection, Produto produto) throws SQLException {
        if (produto.getCategoria() instanceof Categoria) {
            Categoria categoria = (Categoria) produto.getCategoria();
            int idCategoria = categoriaDAO.buscarIdPorNome(connection, categoria.getNome());
            if (idCategoria > 0) {
                return idCategoria;
            }
            return categoriaDAO.salvarSomenteCategoria(connection, categoria, null);
        }
        return null;
    }

    private void carregarCategoria(ResultSet resultSet, Produto produto) throws SQLException {
        int idCategoria = resultSet.getInt("id_categoria");
        if (!resultSet.wasNull()) {
            categoriaDAO.buscarPorId(idCategoria).ifPresent(produto::setCategoria);
        }
    }

    private void salvarFotos(Connection connection, int idProduto, Produto produto) throws SQLException {
        try (PreparedStatement delete = connection.prepareStatement(
                "DELETE FROM produto_foto WHERE id_produto = ?")) {
            delete.setInt(1, idProduto);
            delete.executeUpdate();
        }

        String sql = "INSERT INTO produto_foto (id_produto, caminho, legenda, principal, ordem) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement insert = connection.prepareStatement(sql)) {
            int ordem = 0;
            for (String caminho : produto.getFotos()) {
                insert.setInt(1, idProduto);
                insert.setString(2, caminho);
                insert.setString(3, null);
                insert.setInt(4, ordem == 0 ? 1 : 0);
                insert.setInt(5, ordem++);
                insert.addBatch();
            }
            insert.executeBatch();
        }
    }

    private void carregarFotos(Connection connection, int idProduto, Produto produto) throws SQLException {
        produto.limparFotos();
        String sql = "SELECT caminho FROM produto_foto WHERE id_produto = ? ORDER BY ordem, id_foto";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idProduto);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    produto.addFoto(resultSet.getString("caminho"));
                }
            }
        }
    }

    private String salvarVendedorSeExistir(Connection connection, Cliente vendedor) throws SQLException {
        if (vendedor == null || vendedor.getCPF() == null || vendedor.getCPF().isBlank()) {
            return null;
        }

        String nome = vendedor.getName();
        if (nome == null || nome.isBlank()) {
            nome = "Vendedor";
        }

        String sql = "INSERT INTO cliente (cpf, nome, nota) VALUES (?, ?, ?) "
                + "ON CONFLICT(cpf) DO UPDATE SET nome = excluded.nome, nota = excluded.nota";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, vendedor.getCPF());
            statement.setString(2, nome);
            statement.setDouble(3, vendedor.getNota());
            statement.executeUpdate();
        }
        return vendedor.getCPF();
    }

    private Map<Produto, Integer> contarProdutos(ColecaoProdutos colecao) {
        Map<Produto, Integer> contagem = new LinkedHashMap<>();
        for (model.categoria_produto.ItemProduto item : colecao.getItens()) {
            Produto produto = item.getProduto();
            contagem.put(produto, contagem.getOrDefault(produto, 0) + 1);
            contagem.put(produto, contagem.get(produto) + Math.max(1, item.getQuantidadeProduto()) - 1);
        }
        return contagem;
    }

    private void validarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto nao pode ser nulo.");
        }
        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do produto precisa ser informado.");
        }
    }
}
