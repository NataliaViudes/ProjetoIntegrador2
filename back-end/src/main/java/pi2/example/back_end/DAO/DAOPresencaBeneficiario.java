package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.PresencaBeneficiario;
import pi2.example.back_end.Modelo.RelatorioFaltaBeneficiario;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOPresencaBeneficiario {
    private final Conexao bd;

    public DAOPresencaBeneficiario(Conexao bd) {
        this.bd = bd;
    }

    public boolean criarTabelaSeNaoExistir() {
        String sql = """
            CREATE TABLE IF NOT EXISTS presencabeneficiario (
                idAgendamento INTEGER NOT NULL,
                idBeneficiario INTEGER NOT NULL,
                presente BOOLEAN NOT NULL,
                CONSTRAINT pk_presenca_beneficiario
                    PRIMARY KEY (idAgendamento, idBeneficiario),
                CONSTRAINT fk_presenca_agendamento
                    FOREIGN KEY (idAgendamento)
                    REFERENCES agendamento_atividade(id)
                    ON DELETE CASCADE,
                CONSTRAINT fk_presenca_beneficiario
                    FOREIGN KEY (idBeneficiario)
                    REFERENCES beneficiario(id)
                    ON DELETE CASCADE
            )
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela de presenca: " + e.getMessage());
            return false;
        }
    }

    public PresencaBeneficiario gravar(PresencaBeneficiario presenca) {
        String sql = """
            INSERT INTO presencabeneficiario (idAgendamento, idBeneficiario, presente)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, presenca.getIdAgendamento());
            stmt.setInt(2, presenca.getIdBeneficiario());
            stmt.setBoolean(3, Boolean.TRUE.equals(presenca.getPresente()));
            stmt.executeUpdate();

            return presenca;
        } catch (SQLException e) {
            System.out.println("Erro ao gravar presenca: " + e.getMessage());
            return null;
        }
    }

    public boolean apagarPorAgendamento(Integer idAgendamento) {
        String sql = "DELETE FROM presencabeneficiario WHERE idAgendamento=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, idAgendamento);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir presencas: " + e.getMessage());
            return false;
        }
    }

    public List<PresencaBeneficiario> getByIdAgendamento(Integer idAgendamento) {
        List<PresencaBeneficiario> lista = new ArrayList<>();
        String sql = "SELECT * FROM presencabeneficiario WHERE idAgendamento=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, idAgendamento);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new PresencaBeneficiario(
                        rs.getInt("idAgendamento"),
                        rs.getInt("idBeneficiario"),
                        rs.getBoolean("presente")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar presencas: " + e.getMessage());
        }

        return lista;
    }

    public List<RelatorioFaltaBeneficiario> buscarRelatorioPorBeneficiario(Integer idBeneficiario) {
        List<RelatorioFaltaBeneficiario> lista = new ArrayList<>();

        String sql = """
            SELECT p.idAgendamento,
                   p.idBeneficiario,
                   b.nome AS beneficiario,
                   b.cpf,
                   at.descricao AS atividade,
                   f.nome AS funcionario,
                   ag.data_inicio,
                   ag.data_fim,
                   p.presente
            FROM presencabeneficiario p
            LEFT JOIN beneficiario b ON b.id = p.idBeneficiario
            LEFT JOIN agendamento_atividade ag ON ag.id = p.idAgendamento
            LEFT JOIN atividade at ON at.id_atividade = ag.id_atividade
            LEFT JOIN funcionario f ON f.id_funcionario = at.id_funcionario
            WHERE p.idBeneficiario = ?
            ORDER BY ag.data_inicio DESC
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, idBeneficiario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new RelatorioFaltaBeneficiario(
                        rs.getInt("idAgendamento"),
                        rs.getInt("idBeneficiario"),
                        rs.getString("beneficiario"),
                        rs.getString("cpf"),
                        rs.getString("atividade"),
                        rs.getString("funcionario"),
                        rs.getTimestamp("data_inicio") != null ? rs.getTimestamp("data_inicio").toLocalDateTime().toString() : null,
                        rs.getTimestamp("data_fim") != null ? rs.getTimestamp("data_fim").toLocalDateTime().toString() : null,
                        rs.getBoolean("presente")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar relatorio de faltas: " + e.getMessage());
        }

        return lista;
    }
}
