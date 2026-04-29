package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Cardapio;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardapioDAO {

    private final Conexao bd;

    public CardapioDAO(Conexao bd) {
        this.bd = bd;
    }

    public Cardapio gravar(Cardapio c) {

        String sql = """
            INSERT INTO cardapio
            (descricao, data, hora, quantidade, id_alimento, id_agendamento)
            VALUES (?,?,?,?,?,?)
        """;

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {

            stmt.setString(1, c.getDescricao());
            stmt.setString(2, c.getData());
            stmt.setString(3, c.getHora());
            stmt.setInt(4, c.getQuantidade());
            stmt.setInt(5, c.getAlimento().getId());
            stmt.setInt(6, c.getAgendamento().getId());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                c.setId(rs.getInt(1));
            }

            return c;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir cardápio", e);
        }
    }

    public Cardapio alterar(Cardapio c) {
        String sql = """
            UPDATE cardapio SET
            descricao=?, data=?, hora=?, quantidade=?,
            id_alimento=?, id_agendamento=?
            WHERE id=?
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, c.getDescricao());
            stmt.setString(2, c.getData());
            stmt.setString(3, c.getHora());
            stmt.setInt(4, c.getQuantidade());
            stmt.setInt(5, c.getAlimento().getId());
            stmt.setInt(6, c.getAgendamento().getId());
            stmt.setInt(7, c.getId());

            stmt.executeUpdate();
            return c;

        } catch (SQLException e) {
            return null;
        }
    }

    public boolean apagar(Cardapio c) {
        String sql = "DELETE FROM cardapio WHERE id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, c.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public Cardapio get(int id) {
        String sql = "SELECT * FROM cardapio WHERE id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cardapio c = new Cardapio();

                c.setId(rs.getInt("id"));
                c.setDescricao(rs.getString("descricao"));
                c.setData(rs.getString("data"));
                c.setHora(rs.getString("hora"));
                c.setQuantidade(rs.getInt("quantidade"));

                return c;
            }

        } catch (SQLException e) {}

        return null;
    }

    public List<Cardapio> getAll() {
        List<Cardapio> lista = new ArrayList<>();

        String sql = "SELECT * FROM cardapio";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cardapio c = new Cardapio();

                c.setId(rs.getInt("id"));
                c.setDescricao(rs.getString("descricao"));
                c.setData(rs.getString("data"));
                c.setHora(rs.getString("hora"));
                c.setQuantidade(rs.getInt("quantidade"));

                lista.add(c);
            }

        } catch (SQLException e) {}

        return lista;
    }
}