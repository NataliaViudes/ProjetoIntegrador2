package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Remedio;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAORemedio {

    private final Conexao bd;

    public DAORemedio(Conexao bd) {
        this.bd = bd;
    }


    public Remedio gravar(Remedio entidade) {
        String sql = "INSERT INTO REMEDIO ( nome, descricao) VALUES ( ?, ?)";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, entidade.getNome());
            stmt.setString(2, entidade.getDescricao());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                entidade.setId(id);
            }
            return entidade;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }



    public Remedio alterar(Remedio entidade) {
        String sql = "UPDATE REMEDIO SET nome = ?, descricao = ? WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, entidade.getNome());
            stmt.setString(2, entidade.getDescricao());
            stmt.setInt(3, entidade.getId());
            stmt.executeUpdate();


            return entidade;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }


    public boolean apagar(Remedio entidade) {
        String sql = "DELETE FROM REMEDIO WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, entidade.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }


    public Remedio get(Integer id) {
        Remedio eve = null;
        String sql = "SELECT * FROM REMEDIO WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                eve = new Remedio(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return eve;
    }



    public List<Remedio> buscarPorCategoria(String categoria) {
        List<Remedio> lista = new ArrayList<>();
        String sql;

        // regra: se vazio ou null → traz tudo
        if (categoria == null || categoria.isEmpty()) {
            sql = "SELECT * FROM REMEDIO ORDER BY cat_nome ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Remedio(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("descricao")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }

        } else {
            sql = "SELECT * FROM REMEDIO WHERE nome ILIKE '%' || ? || '%' ORDER BY nome ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                stmt.setString(1, categoria);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Remedio(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("descricao")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }
        }
        return lista;
    }


    public List<Remedio> buscarPorDescricao(String descricao) {
        List<Remedio> lista = new ArrayList<>();
        String sql;

        // regra: se vazio ou null → traz tudo
        if (descricao == null || descricao.isEmpty()) {
            sql = "SELECT * FROM REMEDIO ORDER BY descricao ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Remedio(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("descricao")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }

        } else {
            sql = "SELECT * FROM REMEDIO WHERE descricao ILIKE '%' || ? || '%' ORDER BY descricao ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                stmt.setString(1, descricao);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Remedio(
                            rs.getInt("id"),
                            rs.getString("nome"),
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