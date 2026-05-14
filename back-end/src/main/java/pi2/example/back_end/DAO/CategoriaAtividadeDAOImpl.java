package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.CategoriaAtividade;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaAtividadeDAOImpl {
    private final Conexao bd;

    public CategoriaAtividadeDAOImpl(Conexao bd) {
        this.bd = bd;
    }

    public CategoriaAtividade gravar(CategoriaAtividade entidade) {
        String sql = "INSERT INTO categoria_atividade (nome) VALUES (?)";

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {
            stmt.setString(1, entidade.getNome());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entidade.setId(rs.getInt(1));
            }

            return entidade;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao incluir categoria de atividade", e);
        }
    }

    public CategoriaAtividade alterar(CategoriaAtividade entidade) {
        String sql = "UPDATE categoria_atividade SET nome = ? WHERE id_categoria = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setString(1, entidade.getNome());
            stmt.setInt(2, entidade.getId());

            stmt.executeUpdate();
            return entidade;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar categoria de atividade: " + e.getMessage());
            return null;
        }
    }

    public boolean apagar(CategoriaAtividade entidade) {
        String sql = "DELETE FROM categoria_atividade WHERE id_categoria = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, entidade.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir categoria de atividade: " + e.getMessage());
            return false;
        }
    }

    private CategoriaAtividade mapCategoria(ResultSet rs) throws SQLException {
        CategoriaAtividade categoria = new CategoriaAtividade();
        categoria.setId(rs.getInt("id_categoria"));
        categoria.setNome(rs.getString("nome"));
        return categoria;
    }

    public CategoriaAtividade get(int id) {
        CategoriaAtividade categoria = null;

        String sql = """
        SELECT id_categoria, nome
        FROM categoria_atividade
        WHERE id_categoria = ?
    """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                categoria = mapCategoria(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar categoria de atividade por id: " + e.getMessage());
        }

        return categoria;
    }

    public List<CategoriaAtividade> get(String filtro) {
        List<CategoriaAtividade> lista = new ArrayList<>();

        String sql = """
            SELECT id_categoria, nome
            FROM categoria_atividade
            WHERE nome ILIKE ?
            ORDER BY nome
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setString(1, "%" + filtro + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapCategoria(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar categoria com filtro: " + e.getMessage());
        }

        return lista;
    }

    public List<CategoriaAtividade> getAll() {
        List<CategoriaAtividade> lista = new ArrayList<>();

        String sql = "SELECT id_categoria, nome FROM categoria_atividade ORDER BY nome";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapCategoria(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar categorias de atividade: " + e.getMessage());
        }

        return lista;
    }

    public List<CategoriaAtividade> buscarPorNome(String nome) {
        List<CategoriaAtividade> lista = new ArrayList<>();
        String sql;

        if (nome == null || nome.isEmpty()) {
            sql = "SELECT id_categoria, nome FROM categoria_atividade ORDER BY nome";

            try (PreparedStatement stmt = bd.preparar(sql)) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(mapCategoria(rs));
                }
            } catch (SQLException e) {
                System.out.println("Erro ao buscar categorias por nome: " + e.getMessage());
            }

        } else {
            sql = """
                SELECT id_categoria, nome
                FROM categoria_atividade
                WHERE nome ILIKE '%' || ? || '%'
                ORDER BY nome
            """;

            try (PreparedStatement stmt = bd.preparar(sql)) {
                stmt.setString(1, nome);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(mapCategoria(rs));
                }
            } catch (SQLException e) {
                System.out.println("Erro ao buscar categorias por nome: " + e.getMessage());
            }
        }

        return lista;
    }
}