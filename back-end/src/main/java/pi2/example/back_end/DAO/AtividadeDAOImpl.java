package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Atividade;
import pi2.example.back_end.Modelo.CategoriaAtividade;
import pi2.example.back_end.Modelo.Funcionario;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AtividadeDAOImpl {
    private final Conexao bd;

    public AtividadeDAOImpl(Conexao bd) {
        this.bd = bd;
    }

    public Atividade gravar(Atividade entidade) {
        String sql = "INSERT INTO atividade (descricao, id_categoria, id_funcionario) VALUES (?,?,?)";

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {
            stmt.setString(1, entidade.getDescricao());
            stmt.setInt(2, entidade.getCategoria().getId());
            stmt.setInt(3, entidade.getFuncionario().getId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entidade.setId(rs.getInt(1));
            }

            return entidade;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao incluir atividade", e);
        }
    }

    public Atividade alterar(Atividade entidade) {
        String sql = "UPDATE atividade SET descricao = ?, id_categoria = ?, id_funcionario = ? WHERE id_atividade = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setString(1, entidade.getDescricao());
            stmt.setInt(2, entidade.getCategoria().getId());
            stmt.setInt(3, entidade.getFuncionario().getId());
            stmt.setInt(4, entidade.getId());

            stmt.executeUpdate();
            return entidade;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar atividade: " + e.getMessage());
            return null;
        }
    }

    public boolean apagar(Atividade entidade) {
        String sql = "DELETE FROM atividade WHERE id_atividade = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, entidade.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir atividade: " + e.getMessage());
            return false;
        }
    }

    private Atividade mapAtividade(ResultSet rs) throws SQLException {
        CategoriaAtividade categoria = new CategoriaAtividade();
        categoria.setId(rs.getInt("id_categoria"));
        categoria.setNome(rs.getString("nome_categoria"));

        Funcionario funcionario = new Funcionario();
        funcionario.setId(rs.getInt("id_funcionario"));
        funcionario.setNome(rs.getString("nome_funcionario"));

        Atividade atividade = new Atividade();
        atividade.setId(rs.getInt("id_atividade"));
        atividade.setDescricao(rs.getString("descricao"));
        atividade.setCategoria(categoria);
        atividade.setFuncionario(funcionario);

        return atividade;
    }

    public Atividade get(int id) {
        Atividade atividade = null;

        String sql = """
            SELECT a.id_atividade, a.descricao,
                   c.id_categoria, c.nome AS nome_categoria,
                   f.id_funcionario, f.nome AS nome_funcionario
            FROM atividade a
            JOIN categoria_atividade c ON a.id_categoria = c.id_categoria
            JOIN funcionario f ON a.id_funcionario = f.id_funcionario
            WHERE a.id_atividade = ?
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                atividade = mapAtividade(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar atividade por id: " + e.getMessage());
        }

        return atividade;
    }

    public List<Atividade> get(String filtro) {
        List<Atividade> lista = new ArrayList<>();

        String sql = """
            SELECT a.id_atividade, a.descricao,
                   c.id_categoria, c.nome AS nome_categoria,
                   f.id_funcionario, f.nome AS nome_funcionario
            FROM atividade a
            JOIN categoria_atividade c ON a.id_categoria = c.id_categoria
            JOIN funcionario f ON a.id_funcionario = f.id_funcionario
            WHERE a.descricao ILIKE ?
            ORDER BY a.descricao
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setString(1, "%" + filtro + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapAtividade(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar atividade com filtro: " + e.getMessage());
        }

        return lista;
    }

    public List<Atividade> getAll() {
        List<Atividade> lista = new ArrayList<>();

        String sql = """
            SELECT a.id_atividade, a.descricao,
                   c.id_categoria, c.nome AS nome_categoria,
                   f.id_funcionario, f.nome AS nome_funcionario
            FROM atividade a
            JOIN categoria_atividade c ON a.id_categoria = c.id_categoria
            JOIN funcionario f ON a.id_funcionario = f.id_funcionario
            ORDER BY a.descricao
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapAtividade(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar atividades: " + e.getMessage());
        }

        return lista;
    }

    public List<Atividade> buscarPorNome(String nome) {
        List<Atividade> lista = new ArrayList<>();
        String sql;

        if (nome == null || nome.isEmpty()) {
            sql = """
                SELECT a.id_atividade, a.descricao,
                       c.id_categoria, c.nome AS nome_categoria,
                       f.id_funcionario, f.nome AS nome_funcionario
                FROM atividade a
                JOIN categoria_atividade c ON a.id_categoria = c.id_categoria
                JOIN funcionario f ON a.id_funcionario = f.id_funcionario
                ORDER BY a.descricao
            """;

            try (PreparedStatement stmt = bd.preparar(sql)) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(mapAtividade(rs));
                }
            } catch (SQLException e) {
                System.out.println("Erro ao buscar atividades por nome: " + e.getMessage());
            }
        } else {
            sql = """
                SELECT a.id_atividade, a.descricao,
                       c.id_categoria, c.nome AS nome_categoria,
                       f.id_funcionario, f.nome AS nome_funcionario
                FROM atividade a
                JOIN categoria_atividade c ON a.id_categoria = c.id_categoria
                JOIN funcionario f ON a.id_funcionario = f.id_funcionario
                WHERE a.descricao ILIKE '%' || ? || '%'
                ORDER BY a.descricao
            """;

            try (PreparedStatement stmt = bd.preparar(sql)) {
                stmt.setString(1, nome);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(mapAtividade(rs));
                }
            } catch (SQLException e) {
                System.out.println("Erro ao buscar atividades por nome: " + e.getMessage());
            }
        }

        return lista;
    }
}
