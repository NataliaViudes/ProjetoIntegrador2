package pi2.example.back_end.db;

import pi2.example.back_end.Modelo.Alimento;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DALAlimento implements IDAL<Alimento> {
    private final Conexao bd;

    public DALAlimento(Conexao bd) {
        this.bd = bd;
    }

    @Override
    public boolean gravar(Alimento entidade) {
        String sql = "INSERT INTO alimento ( nome, tipo, descricao) VALUES ( ?, ?, ?)";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, entidade.getNome());
            stmt.setString(2, entidade.getTipo());
            stmt.setString(3, entidade.getDescricao());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }

    @Override
    public boolean alterar(Alimento entidade) {
        String sql = "UPDATE alimento SET nome = ?, tipo = ?, descricao = ? WHERE id_alimento = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, entidade.getNome());
            stmt.setString(2, entidade.getTipo());
            stmt.setString(3, entidade.getDescricao());
            stmt.setInt(4, entidade.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }

    @Override
    public boolean apagar(Alimento entidade) {
        String sql = "DELETE FROM evento WHERE id_alimento = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, entidade.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }

    public boolean apagarPorID(int id) {
        String sql = "DELETE FROM alimento WHERE id_alimento = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }

    @Override
    public Alimento get(int id) {
        Alimento eve = null;
        String sql = "SELECT * FROM alimento WHERE id_alimento = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                eve = new Alimento(
                        rs.getInt("id_alimento"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getString("descricao")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return eve;
    }

    @Override
    public List<Alimento> get(String filtro) {
        List<Alimento> lista = new ArrayList<>();
        String sql = "SELECT * FROM alimento";
        if (filtro != null && !filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }
        ResultSet rs = bd.consultar(sql);
        try {
            if (rs != null) {
                while (rs.next()) {
                    Alimento eve = new Alimento(rs.getInt("id_alimento"), rs.getString("nome"),rs.getString("tipo"), rs.getString("descricao"));
                    lista.add(eve);
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar lista de alimentos: " + e);
        }
        return lista;
    }


    public List<Alimento> buscarPorNome(String nome) {
        List<Alimento> lista = new ArrayList<>();
        String sql;

        // 🔥 regra: se vazio ou null → traz tudo
        if (nome == null || nome.isEmpty()) {
            sql = "SELECT * FROM alimento ORDER BY id_alimento ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Alimento(
                            rs.getInt("id_alimento"),
                            rs.getString("nome"),
                            rs.getString("tipo"),
                            rs.getString("descricao")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }

        } else {
            sql = "SELECT * FROM alimento WHERE nome ILIKE '%' || ? || '%' ORDER BY id_alimento ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                stmt.setString(1, nome);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Alimento(
                            rs.getInt("id_alimento"),
                            rs.getString("nome"),
                            rs.getString("tipo"),
                            rs.getString("descricao")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }
        }

        return lista;
    }


}
