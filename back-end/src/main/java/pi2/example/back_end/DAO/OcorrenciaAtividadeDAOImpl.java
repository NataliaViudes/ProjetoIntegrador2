package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.AgendamentoAtividade;
import pi2.example.back_end.Modelo.Beneficiario;
import pi2.example.back_end.Modelo.OcorrenciaAtividade;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OcorrenciaAtividadeDAOImpl {
    private final Conexao bd;

    public OcorrenciaAtividadeDAOImpl(Conexao bd) {
        this.bd = bd;
    }

    public boolean agendamentoEstaAcontecendoAgora(Integer idAgendamento) {
        String sql = """
            SELECT id
            FROM agendamento_atividade
            WHERE id = ?
            AND CURRENT_TIMESTAMP BETWEEN data_inicio AND data_fim
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, idAgendamento);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Erro ao validar horário do agendamento: " + e.getMessage());
            return false;
        }
    }

    public OcorrenciaAtividade gravar(OcorrenciaAtividade entidade) {
        String sql = """
            INSERT INTO ocorrencia_atividade
            (id_agendamento, id_beneficiario, tipo, observacao)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {
            stmt.setInt(1, entidade.getAgendamento().getId());

            if (entidade.getBeneficiario() != null && entidade.getBeneficiario().getId() != null) {
                stmt.setInt(2, entidade.getBeneficiario().getId());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }

            stmt.setString(3, entidade.getTipo());
            stmt.setString(4, entidade.getObservacao());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entidade.setId(rs.getInt(1));
            }

            return entidade;

        } catch (SQLException e) {
            System.out.println("Erro ao registrar ocorrência: " + e.getMessage());
            return null;
        }
    }

    public List<OcorrenciaAtividade> relatorio(String dataInicio, String dataFim, Integer idBeneficiario) {
        List<OcorrenciaAtividade> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT 
            o.id,
            o.tipo,
            o.observacao,
            o.data_registro,
            ag.id AS id_agendamento,
            b.id AS id_beneficiario,
            b.nome AS nome_beneficiario
        FROM ocorrencia_atividade o
        JOIN agendamento_atividade ag ON o.id_agendamento = ag.id
        LEFT JOIN beneficiario b ON o.id_beneficiario = b.id
        WHERE 1 = 1
    """);

        if (dataInicio != null && !dataInicio.isEmpty()) {
            sql.append(" AND DATE(o.data_registro) >= ? ");
        }

        if (dataFim != null && !dataFim.isEmpty()) {
            sql.append(" AND DATE(o.data_registro) <= ? ");
        }

        if (idBeneficiario != null && idBeneficiario > 0) {
            sql.append(" AND b.id = ? ");
        }

        sql.append(" ORDER BY o.data_registro DESC ");

        try (PreparedStatement stmt = bd.preparar(sql.toString())) {
            int index = 1;

            if (dataInicio != null && !dataInicio.isEmpty()) {
                stmt.setDate(index++, java.sql.Date.valueOf(dataInicio));
            }

            if (dataFim != null && !dataFim.isEmpty()) {
                stmt.setDate(index++, java.sql.Date.valueOf(dataFim));
            }

            if (idBeneficiario != null && idBeneficiario > 0) {
                stmt.setInt(index++, idBeneficiario);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OcorrenciaAtividade o = new OcorrenciaAtividade();

                o.setId(rs.getInt("id"));
                o.setTipo(rs.getString("tipo"));
                o.setObservacao(rs.getString("observacao"));
                o.setDataRegistro(rs.getTimestamp("data_registro").toLocalDateTime().toString());

                AgendamentoAtividade ag = new AgendamentoAtividade();
                ag.setId(rs.getInt("id_agendamento"));
                o.setAgendamento(ag);

                int idB = rs.getInt("id_beneficiario");
                if (!rs.wasNull()) {
                    Beneficiario b = new Beneficiario();
                    b.setId(idB);
                    b.setNome(rs.getString("nome_beneficiario"));
                    o.setBeneficiario(b);
                }

                lista.add(o);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao gerar relatório de ocorrências: " + e.getMessage());
        }

        return lista;
    }
    public OcorrenciaAtividade get(int id) {
        String sql = """
        SELECT id, id_agendamento, id_beneficiario, tipo, observacao, data_registro
        FROM ocorrencia_atividade
        WHERE id = ?
    """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                OcorrenciaAtividade o = new OcorrenciaAtividade();
                o.setId(rs.getInt("id"));
                o.setTipo(rs.getString("tipo"));
                o.setObservacao(rs.getString("observacao"));
                o.setDataRegistro(rs.getTimestamp("data_registro").toLocalDateTime().toString());

                AgendamentoAtividade ag = new AgendamentoAtividade();
                ag.setId(rs.getInt("id_agendamento"));
                o.setAgendamento(ag);

                int idBeneficiario = rs.getInt("id_beneficiario");
                if (!rs.wasNull()) {
                    Beneficiario b = new Beneficiario();
                    b.setId(idBeneficiario);
                    o.setBeneficiario(b);
                }

                return o;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar ocorrência: " + e.getMessage());
        }

        return null;
    }

    public boolean dentroDoPrazoDeEdicao(int id) {
        String sql = """
        SELECT id
        FROM ocorrencia_atividade
        WHERE id = ?
        AND data_registro >= CURRENT_TIMESTAMP - INTERVAL '30 minutes'
    """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Erro ao validar prazo da ocorrência: " + e.getMessage());
            return false;
        }
    }

    public OcorrenciaAtividade alterar(OcorrenciaAtividade entidade) {
        String sql = """
        UPDATE ocorrencia_atividade
        SET tipo = ?,
            id_beneficiario = ?,
            observacao = ?
        WHERE id = ?
    """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setString(1, entidade.getTipo());

            if (entidade.getBeneficiario() != null && entidade.getBeneficiario().getId() != null) {
                stmt.setInt(2, entidade.getBeneficiario().getId());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }

            stmt.setString(3, entidade.getObservacao());
            stmt.setInt(4, entidade.getId());

            stmt.executeUpdate();
            return entidade;

        } catch (SQLException e) {
            System.out.println("Erro ao alterar ocorrência: " + e.getMessage());
            return null;
        }
    }

    public boolean apagar(Integer id) {
        String sql = "DELETE FROM ocorrencia_atividade WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir ocorrência: " + e.getMessage());
            return false;
        }
    }
}
