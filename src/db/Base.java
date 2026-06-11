package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Base {
    public static final String DEFAULT_DB_FILE = "base.db";
    private final String url;

    public Base() {
        this(DEFAULT_DB_FILE);
    }

    public Base(String dbFile) {
        this.url = "jdbc:sqlite:" + dbFile;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }

    public void inicializar() throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS cliente ("
                            + "cpf TEXT PRIMARY KEY,"
                            + "nome TEXT NOT NULL,"
                            + "nota REAL NOT NULL DEFAULT 0,"
                            + "data_nascimento TEXT"
                            + ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS endereco ("
                            + "cpf_cliente TEXT NOT NULL,"
                            + "indice INTEGER NOT NULL,"
                            + "estado TEXT,"
                            + "cidade TEXT,"
                            + "logradouro TEXT,"
                            + "numero INTEGER,"
                            + "complemento TEXT,"
                            + "apelido TEXT,"
                            + "PRIMARY KEY (cpf_cliente, indice),"
                            + "FOREIGN KEY (cpf_cliente) REFERENCES cliente(cpf) ON DELETE CASCADE"
                            + ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS conta_bancaria ("
                            + "cpf_cliente TEXT PRIMARY KEY,"
                            + "numero_conta TEXT,"
                            + "saldo_conta REAL NOT NULL DEFAULT 0,"
                            + "fatura REAL NOT NULL DEFAULT 0,"
                            + "limite_fatura REAL NOT NULL DEFAULT 0,"
                            + "FOREIGN KEY (cpf_cliente) REFERENCES cliente(cpf) ON DELETE CASCADE"
                            + ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS forma_pagamento ("
                            + "cpf_cliente TEXT NOT NULL,"
                            + "forma TEXT NOT NULL,"
                            + "PRIMARY KEY (cpf_cliente, forma),"
                            + "FOREIGN KEY (cpf_cliente) REFERENCES conta_bancaria(cpf_cliente) ON DELETE CASCADE"
                            + ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS credencial_login_senha ("
                            + "usuario TEXT PRIMARY KEY,"
                            + "senha TEXT NOT NULL,"
                            + "cpf_cliente TEXT UNIQUE,"
                            + "FOREIGN KEY (cpf_cliente) REFERENCES cliente(cpf) ON DELETE SET NULL"
                            + ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS categoria ("
                            + "id_categoria INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "nome TEXT NOT NULL,"
                            + "descricao TEXT,"
                            + "id_categoria_pai INTEGER,"
                            + "destaque_admin INTEGER NOT NULL DEFAULT 0,"
                            + "UNIQUE (nome, id_categoria_pai),"
                            + "FOREIGN KEY (id_categoria_pai) REFERENCES categoria(id_categoria) ON DELETE CASCADE"
                            + ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS produto ("
                            + "id_produto INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "nome TEXT NOT NULL,"
                            + "descricao TEXT,"
                            + "id_categoria INTEGER,"
                            + "vendas INTEGER NOT NULL DEFAULT 0,"
                            + "UNIQUE (nome, descricao, id_categoria),"
                            + "FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria) ON DELETE SET NULL"
                            + ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS produto_foto ("
                            + "id_foto INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "id_produto INTEGER NOT NULL,"
                            + "caminho TEXT NOT NULL,"
                            + "legenda TEXT,"
                            + "principal INTEGER NOT NULL DEFAULT 0,"
                            + "ordem INTEGER NOT NULL DEFAULT 0,"
                            + "UNIQUE (id_produto, caminho),"
                            + "FOREIGN KEY (id_produto) REFERENCES produto(id_produto) ON DELETE CASCADE"
                            + ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS item_produto ("
                            + "id_item_produto INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "id_produto INTEGER NOT NULL,"
                            + "cpf_vendedor TEXT,"
                            + "preco_base REAL NOT NULL,"
                            + "quantidade INTEGER NOT NULL DEFAULT 1,"
                            + "nota REAL NOT NULL DEFAULT 0,"
                            + "UNIQUE (id_produto, cpf_vendedor, preco_base),"
                            + "FOREIGN KEY (id_produto) REFERENCES produto(id_produto) ON DELETE CASCADE,"
                            + "FOREIGN KEY (cpf_vendedor) REFERENCES cliente(cpf) ON DELETE SET NULL"
                            + ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS cliente_produto ("
                            + "cpf_cliente TEXT NOT NULL,"
                            + "id_item_produto INTEGER NOT NULL,"
                            + "tipo_colecao TEXT NOT NULL CHECK (tipo_colecao IN ('ESTOQUE', 'CARRINHO')),"
                            + "quantidade INTEGER NOT NULL DEFAULT 1,"
                            + "PRIMARY KEY (cpf_cliente, id_item_produto, tipo_colecao),"
                            + "FOREIGN KEY (cpf_cliente) REFERENCES cliente(cpf) ON DELETE CASCADE,"
                            + "FOREIGN KEY (id_item_produto) REFERENCES item_produto(id_item_produto) ON DELETE CASCADE"
                            + ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS cupom ("
                            + "codigo TEXT PRIMARY KEY,"
                            + "desconto REAL NOT NULL,"
                            + "descricao TEXT"
                            + ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS cliente_cupom_usado ("
                            + "cpf_cliente TEXT NOT NULL,"
                            + "codigo_cupom TEXT NOT NULL,"
                            + "PRIMARY KEY (cpf_cliente, codigo_cupom),"
                            + "FOREIGN KEY (cpf_cliente) REFERENCES cliente(cpf) ON DELETE CASCADE,"
                            + "FOREIGN KEY (codigo_cupom) REFERENCES cupom(codigo) ON DELETE CASCADE"
                            + ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS cliente_lista_desejos ("
                            + "cpf_cliente TEXT NOT NULL,"
                            + "id_item_produto INTEGER NOT NULL,"
                            + "ordem INTEGER NOT NULL DEFAULT 0,"
                            + "PRIMARY KEY (cpf_cliente, id_item_produto),"
                            + "FOREIGN KEY (cpf_cliente) REFERENCES cliente(cpf) ON DELETE CASCADE,"
                            + "FOREIGN KEY (id_item_produto) REFERENCES item_produto(id_item_produto) ON DELETE CASCADE"
                            + ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS cliente_historico_produto ("
                            + "id_historico INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "cpf_cliente TEXT NOT NULL,"
                            + "id_item_produto INTEGER NOT NULL,"
                            + "tipo_historico TEXT NOT NULL CHECK (tipo_historico IN ('COMPRA', 'VENDA')),"
                            + "quantidade INTEGER NOT NULL DEFAULT 1,"
                            + "FOREIGN KEY (cpf_cliente) REFERENCES cliente(cpf) ON DELETE CASCADE,"
                            + "FOREIGN KEY (id_item_produto) REFERENCES item_produto(id_item_produto) ON DELETE CASCADE"
                            + ")"
            );

            migrarSchema(statement);
        }
    }

    private void migrarSchema(Statement statement) throws SQLException {
        adicionarColunaSeNaoExistir(statement, "cliente", "data_nascimento", "TEXT");
        adicionarColunaSeNaoExistir(statement, "categoria", "destaque_admin", "INTEGER NOT NULL DEFAULT 0");
        adicionarColunaSeNaoExistir(statement, "produto", "vendas", "INTEGER NOT NULL DEFAULT 0");
        recriarEnderecoAntigoSeNecessario(statement);
    }

    private void adicionarColunaSeNaoExistir(Statement statement, String tabela, String coluna, String definicao)
            throws SQLException {
        try (java.sql.ResultSet resultSet = statement.executeQuery("PRAGMA table_info(" + tabela + ")")) {
            while (resultSet.next()) {
                if (coluna.equalsIgnoreCase(resultSet.getString("name"))) {
                    return;
                }
            }
        }
        statement.executeUpdate("ALTER TABLE " + tabela + " ADD COLUMN " + coluna + " " + definicao);
    }

    private void recriarEnderecoAntigoSeNecessario(Statement statement) throws SQLException {
        boolean temIndice = false;
        try (java.sql.ResultSet resultSet = statement.executeQuery("PRAGMA table_info(endereco)")) {
            while (resultSet.next()) {
                if ("indice".equalsIgnoreCase(resultSet.getString("name"))) {
                    temIndice = true;
                    break;
                }
            }
        }
        if (temIndice) {
            return;
        }

        statement.executeUpdate("ALTER TABLE endereco RENAME TO endereco_antigo");
        statement.executeUpdate(
                "CREATE TABLE endereco ("
                        + "cpf_cliente TEXT NOT NULL,"
                        + "indice INTEGER NOT NULL,"
                        + "estado TEXT,"
                        + "cidade TEXT,"
                        + "logradouro TEXT,"
                        + "numero INTEGER,"
                        + "complemento TEXT,"
                        + "apelido TEXT,"
                        + "PRIMARY KEY (cpf_cliente, indice),"
                        + "FOREIGN KEY (cpf_cliente) REFERENCES cliente(cpf) ON DELETE CASCADE"
                        + ")"
        );
        statement.executeUpdate(
                "INSERT INTO endereco (cpf_cliente, indice, estado, cidade, logradouro, numero, complemento) "
                        + "SELECT cpf_cliente, 0, estado, cidade, logradouro, numero, complemento FROM endereco_antigo"
        );
        statement.executeUpdate("DROP TABLE endereco_antigo");
    }

    public void recriar() throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS cliente_produto");
            statement.executeUpdate("DROP TABLE IF EXISTS cliente_historico_produto");
            statement.executeUpdate("DROP TABLE IF EXISTS cliente_lista_desejos");
            statement.executeUpdate("DROP TABLE IF EXISTS cliente_cupom_usado");
            statement.executeUpdate("DROP TABLE IF EXISTS cupom");
            statement.executeUpdate("DROP TABLE IF EXISTS item_produto");
            statement.executeUpdate("DROP TABLE IF EXISTS produto_foto");
            statement.executeUpdate("DROP TABLE IF EXISTS produto");
            statement.executeUpdate("DROP TABLE IF EXISTS categoria");
            statement.executeUpdate("DROP TABLE IF EXISTS credencial_login_senha");
            statement.executeUpdate("DROP TABLE IF EXISTS forma_pagamento");
            statement.executeUpdate("DROP TABLE IF EXISTS conta_bancaria");
            statement.executeUpdate("DROP TABLE IF EXISTS endereco");
            statement.executeUpdate("DROP TABLE IF EXISTS cliente");
        }
        inicializar();
    }

    public static void main(String[] args) {
        try {
            Base base = new Base(args.length > 0 ? args[0] : DEFAULT_DB_FILE);
            base.inicializar();
            System.out.println("Banco de dados inicializado com sucesso.");
        } catch (SQLException exception) {
            System.err.println("Erro ao inicializar banco: " + exception.getMessage());
        }
    }
}
