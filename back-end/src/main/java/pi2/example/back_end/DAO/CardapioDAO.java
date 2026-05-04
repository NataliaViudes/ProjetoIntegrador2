package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.AgendamentoAtividade;
import pi2.example.back_end.Modelo.Cardapio;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;

public class CardapioDAO {

    private final Conexao bd;

    public CardapioDAO(Conexao bd) {
        this.bd = bd;
    }

    public Cardapio gravar(Cardapio c) {

        if (c.getAgendamento() == null || c.getAgendamento().getId() == null) {
            throw new RuntimeException("Agendamento inválido");
        }

        String sql = """
            INSERT INTO cardapio
            (descricao, hora, Agendamento_idAtividade)
            VALUES (?,?,?)
        """;

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {

            stmt.setString(1, c.getDescricao());
            stmt.setString(2, c.getHora());
            stmt.setInt(3, c.getAgendamento().getId());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                c.setId(rs.getInt(1));
            }

            return c;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir cardápio: " + e.getMessage());
        }
    }

    public Cardapio alterar(Cardapio c) {

        String sql = """
            UPDATE cardapio SET
            descricao = ?, hora = ?, Agendamento_idAtividade = ?
            WHERE idCardapio = ?
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, c.getDescricao());
            stmt.setString(2, c.getHora());
            stmt.setInt(3, c.getAgendamento().getId());
            stmt.setInt(4, c.getId());

            stmt.executeUpdate();
            return c;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar: " + e.getMessage());
        }
    }

    public boolean apagar(Cardapio c) {

        String sql = "DELETE FROM cardapio WHERE idCardapio = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, c.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir: " + e.getMessage());
        }
    }

    public Cardapio getPorId(int id) {

        String sql = "SELECT * FROM cardapio WHERE idCardapio = ?";
        Cardapio c = null;

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                c = new Cardapio();
                c.setId(rs.getInt("idCardapio"));
                c.setDescricao(rs.getString("descricao"));
                c.setHora(rs.getString("hora"));

                AgendamentoAtividade a = new AgendamentoAtividade();
                a.setId(rs.getInt("Agendamento_idAtividade"));

                c.setAgendamento(a);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar: " + e.getMessage());
        }

        return c;
    }

    public List<Cardapio> getAll() {

        List<Cardapio> lista = new ArrayList<>();

        String sql = "SELECT * FROM cardapio";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Cardapio c = new Cardapio();
                c.setId(rs.getInt("idCardapio"));
                c.setDescricao(rs.getString("descricao"));
                c.setHora(rs.getString("hora"));

                AgendamentoAtividade a = new AgendamentoAtividade();
                a.setId(rs.getInt("Agendamento_idAtividade"));

                c.setAgendamento(a);

                lista.add(c);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar: " + e.getMessage());
        }

        return lista;
    }
}