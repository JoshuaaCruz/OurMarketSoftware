package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.categoria_produto.Categoria;
import model.categoria_produto.Categoria_if;

public class CategoriaDAO {
    private final Base base;

    public CategoriaDAO(Base base) {
        this.base = base;
    }

    public void substituirPor(Categoria categoriaRaiz) throws SQLException {
        if (categoriaRaiz == null) {
            throw new IllegalArgumentException("Categoria raiz nao pode ser nula.");
        }
        try (Connection connection = base.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Integer> idsAtuais = new ArrayList<>();
                salvarColetandoIds(connection, categoriaRaiz, null, idsAtuais);
                removerCategoriasForaDaArvore(connection, idsAtuais);
                connection.commit();
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private int salvarColetandoIds(Connection connection, Categoria categoria, Integer idCategoriaPai,
            List<Integer> idsAtuais) throws SQLException {
        int idCategoria = salvarSomenteCategoria(connection, categoria, idCategoriaPai);
        idsAtuais.add(idCategoria);
        for (Categoria_if subcategoria : categoria.getSubcategorias()) {
            if (subcategoria instanceof Categoria) {
                salvarColetandoIds(connection, (Categoria) subcategoria, idCategoria, idsAtuais);
            }
        }
        return idCategoria;
    }

    private void removerCategoriasForaDaArvore(Connection connection, List<Integer> idsAtuais) throws SQLException {
        if (idsAtuais.isEmpty()) {
            return;
        }
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < idsAtuais.size(); i++) {
            if (i > 0) {
                placeholders.append(",");
            }
            placeholders.append("?");
        }

        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM categoria WHERE id_categoria NOT IN (" + placeholders + ")")) {
            for (int i = 0; i < idsAtuais.size(); i++) {
                statement.setInt(i + 1, idsAtuais.get(i));
            }
            statement.executeUpdate();
        }
    }

    public Optional<Categoria> buscarPorId(int idCategoria) throws SQLException {
        try (Connection connection = base.getConnection()) {
            return buscarPorId(connection, idCategoria);
        }
    }

    public Optional<Categoria> buscarRaizPorNome(String nome) throws SQLException {
        if (nome == null || nome.isBlank()) {
            return Optional.empty();
        }
        try (Connection connection = base.getConnection()) {
            int idCategoria = buscarId(connection, nome, null);
            return idCategoria > 0 ? buscarPorId(connection, idCategoria) : Optional.empty();
        }
    }

    int salvarSomenteCategoria(Connection connection, Categoria categoria, Integer idCategoriaPai)
            throws SQLException {
        int idCategoria = buscarId(connection, categoria.getNome(), idCategoriaPai);
        if (idCategoria > 0) {
            atualizarSomenteCategoria(connection, idCategoria, categoria, idCategoriaPai);
            return idCategoria;
        }

        String sql = "INSERT INTO categoria (nome, descricao, id_categoria_pai, destaque_admin) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, categoria.getNome());
            statement.setString(2, categoria.getDescricao());
            if (idCategoriaPai == null) {
                statement.setNull(3, java.sql.Types.INTEGER);
            } else {
                statement.setInt(3, idCategoriaPai);
            }
            statement.setInt(4, categoria.isDestaqueAdmin() ? 1 : 0);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Nao foi possivel obter o id da categoria salva.");
    }

    int buscarIdPorNome(Connection connection, String nome) throws SQLException {
        if (nome == null || nome.isBlank()) {
            return 0;
        }
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id_categoria FROM categoria WHERE nome = ? ORDER BY id_categoria LIMIT 1")) {
            statement.setString(1, nome);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt("id_categoria") : 0;
            }
        }
    }

    private void atualizarSomenteCategoria(Connection connection, int idCategoria, Categoria categoria,
            Integer idCategoriaPai) throws SQLException {
        String sql = "UPDATE categoria SET nome = ?, descricao = ?, id_categoria_pai = ?, destaque_admin = ? "
                + "WHERE id_categoria = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, categoria.getNome());
            statement.setString(2, categoria.getDescricao());
            if (idCategoriaPai == null) {
                statement.setNull(3, java.sql.Types.INTEGER);
            } else {
                statement.setInt(3, idCategoriaPai);
            }
            statement.setInt(4, categoria.isDestaqueAdmin() ? 1 : 0);
            statement.setInt(5, idCategoria);
            statement.executeUpdate();
        }
    }

    private Optional<Categoria> buscarPorId(Connection connection, int idCategoria) throws SQLException {
        String sql = "SELECT nome, descricao, destaque_admin FROM categoria WHERE id_categoria = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idCategoria);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                Categoria categoria = new Categoria(resultSet.getString("nome"));
                categoria.setDescricao(resultSet.getString("descricao"));
                categoria.setDestaqueAdmin(resultSet.getInt("destaque_admin") != 0);
                carregarSubcategorias(connection, categoria, idCategoria);
                return Optional.of(categoria);
            }
        }
    }

    private void carregarSubcategorias(Connection connection, Categoria categoria, int idCategoriaPai)
            throws SQLException {
        String sql = "SELECT id_categoria FROM categoria WHERE id_categoria_pai = ? ORDER BY nome";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idCategoriaPai);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Optional<Categoria> subcategoria = buscarPorId(connection, resultSet.getInt("id_categoria"));
                    subcategoria.ifPresent(categoria::addSubCategoria);
                }
            }
        }
    }

    private int buscarId(Connection connection, String nome, Integer idCategoriaPai) throws SQLException {
        String sql = idCategoriaPai == null
                ? "SELECT id_categoria FROM categoria WHERE nome = ? AND id_categoria_pai IS NULL"
                : "SELECT id_categoria FROM categoria WHERE nome = ? AND id_categoria_pai = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nome);
            if (idCategoriaPai != null) {
                statement.setInt(2, idCategoriaPai);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt("id_categoria") : 0;
            }
        }
    }
}
