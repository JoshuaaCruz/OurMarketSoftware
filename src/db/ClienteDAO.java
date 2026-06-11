package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.categoria_produto.ItemProduto;
import model.categoria_produto.Produto;
import model.categoria_produto.ColecaoProdutos;
import model.cliente.Cliente;
import model.cliente.Endereco;
import model.contaBancaria.Boleto;
import model.contaBancaria.CartaoCredito;
import model.contaBancaria.ContaBancaria;
import model.contaBancaria.FormaDePagamento;
import model.contaBancaria.Pix;

public class ClienteDAO {
    private final Base base;
    private final ProdutoDAO produtoDAO;

    public ClienteDAO(Base base) {
        this.base = base;
        this.produtoDAO = new ProdutoDAO(base);
    }

    public void salvar(Cliente cliente) throws SQLException {
        validarCliente(cliente);
        try (Connection connection = base.getConnection()) {
            connection.setAutoCommit(false);
            try {
                salvarDadosBasicos(connection, cliente);
                salvarCredencial(connection, cliente);
                salvarEndereco(connection, cliente);
                salvarConta(connection, cliente);
                connection.commit();
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }

        produtoDAO.salvarColecao(cliente.getCPF(), cliente.getEstoque(), ProdutoDAO.TipoColecao.ESTOQUE);
        produtoDAO.salvarColecao(cliente.getCPF(), cliente.getCarrinho(), ProdutoDAO.TipoColecao.CARRINHO);
        salvarListaDesejos(cliente);
        salvarHistorico(cliente, "COMPRA", cliente.getPedidosComprados());
        salvarHistorico(cliente, "VENDA", cliente.getProdutosVendidos());
        salvarCuponsUsados(cliente);
        salvarProdutosAvaliados(cliente);
    }

    public Optional<Cliente> buscarPorCpf(String cpf) throws SQLException {
        if (cpf == null || cpf.isBlank()) {
            return Optional.empty();
        }

        String sql = "SELECT cpf, nome, data_nascimento FROM cliente WHERE cpf = ?";
        try (Connection connection = base.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cpf);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                Cliente cliente = new Cliente();
                cliente.setCPF(resultSet.getString("cpf"));
                cliente.setNome(resultSet.getString("nome"));
                String dataNascimento = resultSet.getString("data_nascimento");
                if (dataNascimento != null && !dataNascimento.isBlank()) {
                    cliente.setDataNascimento(LocalDate.parse(dataNascimento));
                }
                carregarCredencial(connection, cliente);
                carregarEndereco(connection, cliente);
                carregarConta(connection, cliente);
                carregarColecoes(cliente);
                carregarListaDesejos(cliente);
                carregarHistorico(cliente, "COMPRA", cliente.getPedidosComprados());
                carregarHistorico(cliente, "VENDA", cliente.getProdutosVendidos());
                carregarCuponsUsados(connection, cliente);
                carregarProdutosAvaliados(connection, cliente);
                return Optional.of(cliente);
            }
        }
    }

    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection connection = base.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT cpf FROM cliente ORDER BY nome");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                buscarPorCpf(resultSet.getString("cpf")).ifPresent(clientes::add);
            }
        }
        return clientes;
    }

    private void salvarDadosBasicos(Connection connection, Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (cpf, nome, nota, data_nascimento) VALUES (?, ?, ?, ?) "
                + "ON CONFLICT(cpf) DO UPDATE SET nome = excluded.nome, nota = excluded.nota, "
                + "data_nascimento = excluded.data_nascimento";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getCPF());
            statement.setString(2, cliente.getName());
            statement.setDouble(3, cliente.getNota());
            statement.setString(4, cliente.getDataNascimento() == null ? null : cliente.getDataNascimento().toString());
            statement.executeUpdate();
        }
    }

    private void salvarCredencial(Connection connection, Cliente cliente) throws SQLException {
        if (cliente.getLogin() == null || cliente.getLogin().isBlank()
                || cliente.getSenha() == null || cliente.getSenha().isBlank()) {
            return;
        }

        String sql = "INSERT INTO credencial_login_senha (usuario, senha, cpf_cliente) VALUES (?, ?, ?) "
                + "ON CONFLICT(usuario) DO UPDATE SET senha = excluded.senha, cpf_cliente = excluded.cpf_cliente";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getLogin());
            statement.setString(2, cliente.getSenha());
            statement.setString(3, cliente.getCPF());
            statement.executeUpdate();
        }
    }

    private void salvarEndereco(Connection connection, Cliente cliente) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM endereco WHERE cpf_cliente = ?")) {
            statement.setString(1, cliente.getCPF());
            statement.executeUpdate();
        }

        String sql = "INSERT INTO endereco "
                + "(cpf_cliente, indice, estado, cidade, logradouro, numero, complemento, apelido) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int indice = 0;
            for (Endereco endereco : cliente.getEnderecos()) {
                statement.setString(1, cliente.getCPF());
                statement.setInt(2, indice++);
                statement.setString(3, endereco.getEstado());
                statement.setString(4, endereco.getCidade());
                statement.setString(5, endereco.getRua());
                statement.setInt(6, endereco.getNumero());
                statement.setString(7, endereco.getComplemento());
                statement.setString(8, endereco.getApelido());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void salvarConta(Connection connection, Cliente cliente) throws SQLException {
        ContaBancaria conta = cliente.getContaCliente();
        if (conta == null) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM conta_bancaria WHERE cpf_cliente = ?")) {
                statement.setString(1, cliente.getCPF());
                statement.executeUpdate();
            }
            return;
        }

        String sql = "INSERT INTO conta_bancaria "
                + "(cpf_cliente, numero_conta, saldo_conta, fatura, limite_fatura) VALUES (?, ?, ?, ?, ?) "
                + "ON CONFLICT(cpf_cliente) DO UPDATE SET "
                + "numero_conta = excluded.numero_conta, saldo_conta = excluded.saldo_conta, "
                + "fatura = excluded.fatura, limite_fatura = excluded.limite_fatura";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getCPF());
            statement.setString(2, conta.getNumero());
            statement.setDouble(3, conta.getSaldoConta());
            statement.setDouble(4, conta.getFatura());
            statement.setDouble(5, conta.getLimiteFatura());
            statement.executeUpdate();
        }

        try (PreparedStatement delete = connection.prepareStatement(
                "DELETE FROM forma_pagamento WHERE cpf_cliente = ?")) {
            delete.setString(1, cliente.getCPF());
            delete.executeUpdate();
        }
        for (FormaDePagamento forma : conta.getFormasPagamento()) {
            try (PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO forma_pagamento (cpf_cliente, forma) VALUES (?, ?)")) {
                insert.setString(1, cliente.getCPF());
                insert.setString(2, forma.getNome());
                insert.executeUpdate();
            }
        }
    }

    private void carregarCredencial(Connection connection, Cliente cliente) throws SQLException {
        String sql = "SELECT usuario, senha FROM credencial_login_senha WHERE cpf_cliente = ? LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getCPF());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    cliente.setLogin(resultSet.getString("usuario"));
                    cliente.setSenha(resultSet.getString("senha"));
                }
            }
        }
    }

    private void carregarEndereco(Connection connection, Cliente cliente) throws SQLException {
        String sql = "SELECT estado, cidade, logradouro, numero, complemento, apelido "
                + "FROM endereco WHERE cpf_cliente = ? ORDER BY indice";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getCPF());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Endereco endereco = new Endereco(
                            resultSet.getString("estado"),
                            resultSet.getString("cidade"),
                            resultSet.getString("logradouro"),
                            resultSet.getInt("numero"),
                            resultSet.getString("complemento"));
                    endereco.setApelido(resultSet.getString("apelido"));
                    cliente.addEndereco(endereco);
                }
            }
        }
    }

    private void carregarConta(Connection connection, Cliente cliente) throws SQLException {
        String sql = "SELECT numero_conta, saldo_conta, fatura, limite_fatura "
                + "FROM conta_bancaria WHERE cpf_cliente = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getCPF());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    ContaBancaria conta = new ContaBancaria();
                    conta.setNumero(resultSet.getString("numero_conta"));
                    conta.depositar(resultSet.getDouble("saldo_conta"));
                    conta.incrementaFatura(resultSet.getDouble("fatura"));
                    conta.setLimiteFatura(resultSet.getDouble("limite_fatura"));
                    carregarFormasPagamento(connection, cliente.getCPF(), conta);
                    cliente.setContaCliente(conta);
                }
            }
        }
    }

    private void carregarFormasPagamento(Connection connection, String cpf, ContaBancaria conta) throws SQLException {
        conta.getFormasPagamento().clear();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT forma FROM forma_pagamento WHERE cpf_cliente = ? ORDER BY forma")) {
            statement.setString(1, cpf);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    FormaDePagamento forma = criarFormaPagamento(resultSet.getString("forma"));
                    if (forma != null && !conta.temFormaPagamento(forma.getNome())) {
                        conta.adicionarFormaPagamento(forma);
                    }
                }
            }
        }
    }

    private void carregarColecoes(Cliente cliente) throws SQLException {
        ColecaoProdutos estoque = produtoDAO.carregarColecao(cliente.getCPF(), ProdutoDAO.TipoColecao.ESTOQUE);
        ColecaoProdutos carrinho = produtoDAO.carregarColecao(cliente.getCPF(), ProdutoDAO.TipoColecao.CARRINHO);
        cliente.getEstoque().limparColecao();
        cliente.getCarrinho().limparColecao();
        for (model.categoria_produto.ItemProduto item : estoque.getItens()) {
            cliente.getEstoque().adicionarProduto(item.getProduto(), item.getQuantidadeProduto());
        }
        for (model.categoria_produto.ItemProduto item : carrinho.getItens()) {
            cliente.getCarrinho().adicionarProduto(item.getProduto(), item.getQuantidadeProduto());
        }
    }

    private void salvarListaDesejos(Cliente cliente) throws SQLException {
        try (Connection connection = base.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement delete = connection.prepareStatement(
                        "DELETE FROM cliente_lista_desejos WHERE cpf_cliente = ?")) {
                    delete.setString(1, cliente.getCPF());
                    delete.executeUpdate();
                }

                String sql = "INSERT INTO cliente_lista_desejos (cpf_cliente, id_item_produto, ordem) "
                        + "VALUES (?, ?, ?)";
                try (PreparedStatement insert = connection.prepareStatement(sql)) {
                    int ordem = 0;
                    for (Produto produto : cliente.getListaDesejos()) {
                        int idItemProduto = produtoDAO.salvarReferenciaProduto(connection, produto, 1);
                        insert.setString(1, cliente.getCPF());
                        insert.setInt(2, idItemProduto);
                        insert.setInt(3, ordem++);
                        insert.addBatch();
                    }
                    insert.executeBatch();
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

    private void carregarListaDesejos(Cliente cliente) throws SQLException {
        cliente.getListaDesejos().clear();
        String sql = "SELECT id_item_produto FROM cliente_lista_desejos "
                + "WHERE cpf_cliente = ? ORDER BY ordem, id_item_produto";
        try (Connection connection = base.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getCPF());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    produtoDAO.buscarItemPorId(resultSet.getInt("id_item_produto"))
                            .ifPresent(cliente::addProdutoListaDesejo);
                }
            }
        }
    }

    private void salvarHistorico(Cliente cliente, String tipo, List<ItemProduto> historico) throws SQLException {
        try (Connection connection = base.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement delete = connection.prepareStatement(
                        "DELETE FROM cliente_historico_produto WHERE cpf_cliente = ? AND tipo_historico = ?")) {
                    delete.setString(1, cliente.getCPF());
                    delete.setString(2, tipo);
                    delete.executeUpdate();
                }

                String sql = "INSERT INTO cliente_historico_produto "
                        + "(cpf_cliente, id_item_produto, tipo_historico, quantidade) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insert = connection.prepareStatement(sql)) {
                    for (ItemProduto item : historico) {
                        int quantidade = Math.max(1, item.getQuantidadeProduto());
                        int idItemProduto = produtoDAO.salvarReferenciaProduto(connection, item.getProduto(), quantidade);
                        insert.setString(1, cliente.getCPF());
                        insert.setInt(2, idItemProduto);
                        insert.setString(3, tipo);
                        insert.setInt(4, quantidade);
                        insert.addBatch();
                    }
                    insert.executeBatch();
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

    private void carregarHistorico(Cliente cliente, String tipo, List<ItemProduto> destino) throws SQLException {
        destino.clear();
        String sql = "SELECT id_item_produto, quantidade FROM cliente_historico_produto "
                + "WHERE cpf_cliente = ? AND tipo_historico = ? ORDER BY id_historico";
        try (Connection connection = base.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getCPF());
            statement.setString(2, tipo);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Optional<Produto> produto = produtoDAO.buscarItemPorId(resultSet.getInt("id_item_produto"));
                    if (produto.isPresent()) {
                        destino.add(new ItemProduto(produto.get(), Math.max(1, resultSet.getInt("quantidade"))));
                    }
                }
            }
        }
    }

    private void salvarCuponsUsados(Cliente cliente) throws SQLException {
        try (Connection connection = base.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement delete = connection.prepareStatement(
                        "DELETE FROM cliente_cupom_usado WHERE cpf_cliente = ?")) {
                    delete.setString(1, cliente.getCPF());
                    delete.executeUpdate();
                }

                try (PreparedStatement insert = connection.prepareStatement(
                        "INSERT INTO cliente_cupom_usado (cpf_cliente, codigo_cupom) VALUES (?, ?)")) {
                    for (String codigo : cliente.getCuponsUsados()) {
                        insert.setString(1, cliente.getCPF());
                        insert.setString(2, codigo);
                        insert.addBatch();
                    }
                    insert.executeBatch();
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

    private void carregarCuponsUsados(Connection connection, Cliente cliente) throws SQLException {
        cliente.getCuponsUsados().clear();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT codigo_cupom FROM cliente_cupom_usado WHERE cpf_cliente = ?")) {
            statement.setString(1, cliente.getCPF());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cliente.markCupomAsUsado(resultSet.getString("codigo_cupom"));
                }
            }
        }
    }

    private void salvarProdutosAvaliados(Cliente cliente) throws SQLException {
        try (Connection connection = base.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement delete = connection.prepareStatement(
                        "DELETE FROM cliente_produto_avaliado WHERE cpf_cliente = ?")) {
                    delete.setString(1, cliente.getCPF());
                    delete.executeUpdate();
                }
                try (PreparedStatement insert = connection.prepareStatement(
                        "INSERT INTO cliente_produto_avaliado (cpf_cliente, chave_avaliacao) VALUES (?, ?)")) {
                    for (String chave : cliente.getProdutosAvaliados()) {
                        insert.setString(1, cliente.getCPF());
                        insert.setString(2, chave);
                        insert.addBatch();
                    }
                    insert.executeBatch();
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

    private void carregarProdutosAvaliados(Connection connection, Cliente cliente) throws SQLException {
        cliente.getProdutosAvaliados().clear();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT chave_avaliacao FROM cliente_produto_avaliado WHERE cpf_cliente = ?")) {
            statement.setString(1, cliente.getCPF());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cliente.getProdutosAvaliados().add(resultSet.getString("chave_avaliacao"));
                }
            }
        }
    }

    private FormaDePagamento criarFormaPagamento(String nome) {
        if ("Pix".equals(nome)) {
            return new Pix();
        }
        if ("Cartão de Crédito".equals(nome) || "Cartao de Credito".equals(nome)) {
            return new CartaoCredito();
        }
        if ("Boleto".equals(nome)) {
            return new Boleto();
        }
        return null;
    }

    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente nao pode ser nulo.");
        }
        if (cliente.getCPF() == null || cliente.getCPF().isBlank()) {
            throw new IllegalArgumentException("CPF do cliente precisa ser informado.");
        }
        if (cliente.getName() == null || cliente.getName().isBlank()) {
            throw new IllegalArgumentException("Nome do cliente precisa ser informado.");
        }
    }
}
