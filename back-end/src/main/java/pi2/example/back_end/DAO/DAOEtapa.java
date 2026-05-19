package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Etapa;
import pi2.example.back_end.db.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOEtapa {

    private final Conexao bd;

    public DAOEtapa(Conexao bd) {
        this.bd = bd;
    }

    private Timestamp converterTimestamp(String dataHora) {
        if (dataHora == null || dataHora.isEmpty()) return null;

        String valor = dataHora.replace("T", " ");
        if (valor.length() == 16) valor += ":00";

        return Timestamp.valueOf(valor);
    }

    public Etapa inserir(Etapa etapa) {
        String sql = """
            INSERT INTO etapa (id_agendamento, descricao, data_hora_inicio)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {
            stmt.setInt(1, etapa.getIdAgendamento());
            stmt.setString(2, etapa.getDescricao());
            stmt.setTimestamp(3, converterTimestamp(etapa.getDataHoraInicio()));

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                etapa.setId(rs.getInt(1));
            }

            return etapa;
        } catch (SQLException e) {
            System.out.println("Erro ao incluir etapa: " + e.getMessage());
            return null;
        }
    }

    public List<Etapa> listarPorAgendamento(int idAgendamento) {
        List<Etapa> lista = new ArrayList<>();

        String sql = """
            SELECT * FROM etapa
            WHERE id_agendamento = ?
            ORDER BY data_hora_inicio
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, idAgendamento);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(map(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar etapas: " + e.getMessage());
        }

        return lista;
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM etapa WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir etapa: " + e.getMessage());
            return false;
        }
    }

    private Etapa map(ResultSet rs) throws SQLException {
        Etapa etapa = new Etapa();
        etapa.setId(rs.getInt("id"));
        etapa.setIdAgendamento(rs.getInt("id_agendamento"));
        etapa.setDescricao(rs.getString("descricao"));

        Timestamp ts = rs.getTimestamp("data_hora_inicio");
        etapa.setDataHoraInicio(ts != null ? ts.toLocalDateTime().toString().substring(0, 16) : null);

        return etapa;
    }

    public boolean atualizar(Etapa etapa) throws SQLException {

        String sql = """
            UPDATE etapa
            SET descricao = ?, data_hora_inicio = ?
            WHERE id = ?
        """;

        try (PreparedStatement ps = bd.preparar(sql)) {

            ps.setString(1, etapa.getDescricao());
            ps.setTimestamp(2, converterTimestamp(etapa.getDataHoraInicio()));
            ps.setInt(3, etapa.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public Etapa buscarPorId(int id) {
        String sql = "SELECT * FROM etapa WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return map(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar etapa: " + e.getMessage());
        }

        return null;
    }
}