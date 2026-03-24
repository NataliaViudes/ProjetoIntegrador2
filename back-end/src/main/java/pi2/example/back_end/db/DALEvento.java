package pi2.example.back_end.db;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Evento;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class DALEvento {

    private final Conexao bd;

    public DALEvento(Conexao bd) {
        this.bd = bd;
    }


    public Evento gravar(Evento entidade) {
        String sql = "INSERT INTO evento ( categoria, descricao) VALUES ( ?, ?)";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, entidade.getCategoria());
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



    public Evento alterar(Evento entidade) {
        String sql = "UPDATE evento SET categoria = ?, descricao = ? WHERE id_evento = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, entidade.getCategoria());
            stmt.setString(2, entidade.getDescricao());
            stmt.setInt(3, entidade.getId());
            stmt.executeUpdate();


            return entidade;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }


    public boolean apagar(Evento entidade) {
        String sql = "DELETE FROM evento WHERE id_evento = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, entidade.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }


    public Evento get(Integer id) {
        Evento eve = null;
        String sql = "SELECT * FROM evento WHERE id_evento = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                eve = new Evento(
                        rs.getInt("id_evento"),
                        rs.getString("categoria"),
                        rs.getString("descricao")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return eve;
    }



    public List<Evento> buscarPorCategoria(String categoria) {
        List<Evento> lista = new ArrayList<>();
        String sql;

        // regra: se vazio ou null → traz tudo
        if (categoria == null || categoria.isEmpty()) {
            sql = "SELECT * FROM evento ORDER BY categoria ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Evento(
                            rs.getInt("id_evento"),
                            rs.getString("categoria"),
                            rs.getString("descricao")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }

        } else {
            sql = "SELECT * FROM evento WHERE categoria ILIKE '%' || ? || '%' ORDER BY categoria ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                stmt.setString(1, categoria);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Evento(
                            rs.getInt("id_evento"),
                            rs.getString("categoria"),
                            rs.getString("descricao")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }
        }
        return lista;
    }


    public List<Evento> buscarPorDescricao(String descricao) {
        List<Evento> lista = new ArrayList<>();
        String sql;

        // regra: se vazio ou null → traz tudo
        if (descricao == null || descricao.isEmpty()) {
            sql = "SELECT * FROM evento ORDER BY descricao ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Evento(
                            rs.getInt("id_evento"),
                            rs.getString("categoria"),
                            rs.getString("descricao")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }

        } else {
            sql = "SELECT * FROM evento WHERE descricao ILIKE '%' || ? || '%' ORDER BY descricao ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                stmt.setString(1, descricao);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Evento(
                            rs.getInt("id_evento"),
                            rs.getString("categoria"),
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