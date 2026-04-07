package pi2.example.back_end.db;

import pi2.example.back_end.Modelo.Cat_Evento;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAOEvento {

    private final Conexao bd;

    public DAOEvento(Conexao bd) {
        this.bd = bd;
    }


    public Cat_Evento gravar(Cat_Evento entidade) {
        String sql = "INSERT INTO CAT_EVENTO ( cat_nome, cat_descricao) VALUES ( ?, ?)";

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



    public Cat_Evento alterar(Cat_Evento entidade) {
        String sql = "UPDATE CAT_EVENTO SET cat_nome = ?, cat_descricao = ? WHERE id_evento = ?";

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


    public boolean apagar(Cat_Evento entidade) {
        String sql = "DELETE FROM CAT_EVENTO WHERE cat_evento_id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, entidade.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }


    public Cat_Evento get(Integer id) {
        Cat_Evento eve = null;
        String sql = "SELECT * FROM CAT_EVENTO WHERE cat_evento_id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                eve = new Cat_Evento(
                        rs.getInt("cat_evento_id"),
                        rs.getString("cat_nome"),
                        rs.getString("cat_descricao")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return eve;
    }



    public List<Cat_Evento> buscarPorCategoria(String categoria) {
        List<Cat_Evento> lista = new ArrayList<>();
        String sql;

        // regra: se vazio ou null → traz tudo
        if (categoria == null || categoria.isEmpty()) {
            sql = "SELECT * FROM CAT_EVENTO ORDER BY cat_nome ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Cat_Evento(
                            rs.getInt("cat_evento_id"),
                            rs.getString("cat_nome"),
                            rs.getString("cat_descricao")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }

        } else {
            sql = "SELECT * FROM CAT_EVENTO WHERE cat_nome ILIKE '%' || ? || '%' ORDER BY cat_nome ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                stmt.setString(1, categoria);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Cat_Evento(
                            rs.getInt("cat_evento_id"),
                            rs.getString("cat_nome"),
                            rs.getString("cat_descricao")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }
        }
        return lista;
    }


    public List<Cat_Evento> buscarPorDescricao(String descricao) {
        List<Cat_Evento> lista = new ArrayList<>();
        String sql;

        // regra: se vazio ou null → traz tudo
        if (descricao == null || descricao.isEmpty()) {
            sql = "SELECT * FROM CAT_EVENTO ORDER BY cat_descricao ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Cat_Evento(
                            rs.getInt("cat_evento_id"),
                            rs.getString("cat_nome"),
                            rs.getString("cat_descricao")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }

        } else {
            sql = "SELECT * FROM CAT_EVENTO WHERE cat_descricao ILIKE '%' || ? || '%' ORDER BY cat_descricao ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                stmt.setString(1, descricao);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Cat_Evento(
                            rs.getInt("cat_evento_id"),
                            rs.getString("cat_nome"),
                            rs.getString("cat_descricao")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }
        }
        return lista;
    }


}