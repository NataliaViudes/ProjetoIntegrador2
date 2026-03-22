package pi2.example.back_end.db;

import pi2.example.back_end.Modelo.Evento;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;
public class DALEvento implements IDAL<Evento> {

    private final Conexao bd;

    public DALEvento(Conexao bd) {
        this.bd = bd;
    }

    @Override
    public boolean gravar(Evento entidade) {
        String sql = "INSERT INTO evento ( nome, descricao) VALUES ( ?, ?)";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, entidade.getNome());
            stmt.setString(2, entidade.getDescricao());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }


    @Override
    public boolean alterar(Evento entidade) {
        String sql = "UPDATE evento SET nome = ?, descricao = ? WHERE id_evento = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, entidade.getNome());
            stmt.setString(2, entidade.getDescricao());
            stmt.setInt(3, entidade.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }

    @Override
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

    @Override
    public Evento get(int id) {
        Evento eve = null;
        String sql = "SELECT * FROM evento WHERE id_evento = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                eve = new Evento(
                        rs.getInt("id_evento"),
                        rs.getString("nome"),
                        rs.getString("descricao")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return eve;
    }

    @Override
    public List<Evento> get(String filtro) {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT * FROM evento";
        if (filtro != null && !filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }
        ResultSet rs = bd.consultar(sql);
        try {
            if (rs != null) {
                while (rs.next()) {
                    Evento eve = new Evento(rs.getInt("id_evento"), rs.getString("nome"),rs.getString("descricao"));
                    lista.add(eve);
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar lista de eventos: " + e);
        }
        return lista;
    }


    public List<Evento> buscarPorNome(String nome) {
        List<Evento> lista = new ArrayList<>();
        String sql;

        // 🔥 regra: se vazio ou null → traz tudo
        if (nome == null || nome.isEmpty()) {
            sql = "SELECT * FROM evento ORDER BY id_evento ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Evento(
                            rs.getInt("id_evento"),
                            rs.getString("nome"),
                            rs.getString("descricao")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }

        } else {
            sql = "SELECT * FROM evento WHERE nome ILIKE '%' || ? || '%' ORDER BY id_evento ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                stmt.setString(1, nome);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Evento(
                            rs.getInt("id_evento"),
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