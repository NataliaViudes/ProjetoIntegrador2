package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.CategoriaAuxilio;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CatAuxilioDAOImpl {
    private final Conexao bd;

    public CatAuxilioDAOImpl(Conexao bd) {
        this.bd = bd;
    }

    private CategoriaAuxilio mapCategoria(ResultSet rs) throws SQLException {
        CategoriaAuxilio categoria = new CategoriaAuxilio();
        categoria.setId(rs.getInt("id"));
        categoria.setNome(rs.getString("nome"));
        return categoria;
    }

    public CategoriaAuxilio get(int id) {
        CategoriaAuxilio categoria = null;

        String sql = "SELECT id, nome FROM categoria_auxilio WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                categoria = mapCategoria(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar categoria de auxílio por id: " + e.getMessage());
        }

        return categoria;
    }

    public List<CategoriaAuxilio> get(String filtro) {
        List<CategoriaAuxilio> lista = new ArrayList<>();

        String sql = """
            SELECT id, nome
            FROM categoria_auxilio
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
            System.out.println("Erro ao buscar categoria de auxílio com filtro: " + e.getMessage());
        }

        return lista;
    }

    public List<CategoriaAuxilio> getAll() {
        List<CategoriaAuxilio> lista = new ArrayList<>();

        String sql = "SELECT id, nome FROM categoria_auxilio ORDER BY nome";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapCategoria(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar categorias de auxílio: " + e.getMessage());
        }

        return lista;
    }

    public List<CategoriaAuxilio> buscarPorNome(String nome) {
        List<CategoriaAuxilio> lista = new ArrayList<>();
        String sql;

        if (nome == null || nome.isEmpty()) {
            sql = "SELECT id, nome FROM categoria_auxilio ORDER BY nome";

            try (PreparedStatement stmt = bd.preparar(sql)) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(mapCategoria(rs));
                }
            } catch (SQLException e) {
                System.out.println("Erro ao buscar categorias de auxílio por nome: " + e.getMessage());
            }

        } else {
            sql = """
                SELECT id, nome
                FROM categoria_auxilio
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
                System.out.println("Erro ao buscar categorias de auxílio por nome: " + e.getMessage());
            }
        }

        return lista;
    }
}
