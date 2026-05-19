package pi2.example.back_end.DAO;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.*;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoAtividadeDAO {
    private final Conexao bd;

    public AgendamentoAtividadeDAO(Conexao bd) {
        this.bd = bd;
    }

    private Timestamp converterTimestamp(String dataHora) {
        if (dataHora == null || dataHora.isEmpty()) {
            return null;
        }

        String valor = dataHora.replace("T", " ");

        if (valor.length() == 16) {
            valor += ":00";
        }

        return Timestamp.valueOf(valor);
    }

    public AgendamentoAtividade gravar(AgendamentoAtividade entidade) {
        String sql = """
            INSERT INTO agendamento_atividade
            (id_atividade, data_inicio, data_fim, observacao)
            VALUES (?,?,?,?)
        """;

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {
            stmt.setInt(1, entidade.getAtividade().getId());
            stmt.setTimestamp(2, converterTimestamp(entidade.getDataInicio()));
            stmt.setTimestamp(3, converterTimestamp(entidade.getDataFim()));
            stmt.setString(4, entidade.getObservacao());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entidade.setId(rs.getInt(1));
            }

            return entidade;
        } catch (SQLException e) {
            System.out.println("Erro SQL ao incluir agendamento: " + e.getMessage());
            return null;
        }
    }

    public AgendamentoAtividade alterar(AgendamentoAtividade entidade) {
        String sql = """
            UPDATE agendamento_atividade SET
                id_atividade = ?,
                data_inicio = ?,
                data_fim = ?,
                observacao = ?
            WHERE id = ?
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, entidade.getAtividade().getId());
            stmt.setTimestamp(2, converterTimestamp(entidade.getDataInicio()));
            stmt.setTimestamp(3, converterTimestamp(entidade.getDataFim()));
            stmt.setString(4, entidade.getObservacao());
            stmt.setInt(5, entidade.getId());

            stmt.executeUpdate();
            return entidade;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar agendamento: " + e.getMessage());
            return null;
        }
    }

    public boolean apagar(AgendamentoAtividade entidade) {
        String sql = "DELETE FROM agendamento_atividade WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, entidade.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir agendamento: " + e.getMessage());
            return false;
        }
    }

    private AgendamentoAtividade mapAgendamento(ResultSet rs) throws SQLException {
        CategoriaAtividade categoria = new CategoriaAtividade();
        categoria.setId(rs.getInt("cat_id"));
        categoria.setNome(rs.getString("cat_nome"));

        Funcionario funcionario = new Funcionario();
        funcionario.setId(rs.getInt("func_id"));
        funcionario.setNome(rs.getString("func_nome"));

        Atividade atividade = new Atividade();
        atividade.setId(rs.getInt("id_atividade"));
        atividade.setDescricao(rs.getString("descricao"));
        atividade.setCategoria(categoria);
        atividade.setFuncionario(funcionario);

        AgendamentoAtividade agendamento = new AgendamentoAtividade();
        agendamento.setId(rs.getInt("id"));
        agendamento.setAtividade(atividade);
        Timestamp inicio = rs.getTimestamp("data_inicio");
        Timestamp fim = rs.getTimestamp("data_fim");

        agendamento.setDataInicio(inicio != null ? inicio.toLocalDateTime().toString() : null);
        agendamento.setDataFim(fim != null ? fim.toLocalDateTime().toString() : null);
        agendamento.setObservacao(rs.getString("observacao"));

        return agendamento;
    }

    public AgendamentoAtividade get(int id) {
        AgendamentoAtividade agendamento = null;

        String sql = """
            SELECT ag.id,
                   ag.data_inicio,
                   ag.data_fim,
                   ag.observacao,
                   at.id_atividade,
                   at.descricao,
                   c.id_categoria AS cat_id,
                   c.nome AS cat_nome,
                   f.id_funcionario AS func_id,
                   f.nome AS func_nome
            FROM agendamento_atividade ag
            JOIN atividade at ON ag.id_atividade = at.id_atividade
            JOIN categoria_atividade c ON at.id_categoria = c.id_categoria
            JOIN funcionario f ON at.id_funcionario = f.id_funcionario
            WHERE ag.id = ?
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                agendamento = mapAgendamento(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar agendamento por id: " + e.getMessage());
        }

        return agendamento;
    }

    public List<AgendamentoAtividade> get(String filtro) {
        List<AgendamentoAtividade> lista = new ArrayList<>();

        String sql = """
            SELECT ag.id,
                   ag.data_inicio,
                   ag.data_fim,
                   ag.observacao,
                   at.id_atividade,
                   at.descricao,
                   c.id_categoria AS cat_id,
                   c.nome AS cat_nome,
                   f.id_funcionario AS func_id,
                   f.nome AS func_nome
            FROM agendamento_atividade ag
            JOIN atividade at ON ag.id_atividade = at.id_atividade
            JOIN categoria_atividade c ON at.id_categoria = c.id_categoria
            JOIN funcionario f ON at.id_funcionario = f.id_funcionario
            WHERE at.descricao ILIKE ?
            ORDER BY ag.data_inicio
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setString(1, "%" + filtro + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapAgendamento(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar agendamento com filtro: " + e.getMessage());
        }

        return lista;
    }

    public List<AgendamentoAtividade> getAll() {
        List<AgendamentoAtividade> lista = new ArrayList<>();

        String sql = """
            SELECT ag.id,
                   ag.data_inicio,
                   ag.data_fim,
                   ag.observacao,
                   at.id_atividade,
                   at.descricao,
                   c.id_categoria AS cat_id,
                   c.nome AS cat_nome,
                   f.id_funcionario AS func_id,
                   f.nome AS func_nome
            FROM agendamento_atividade ag
            JOIN atividade at ON ag.id_atividade = at.id_atividade
            JOIN categoria_atividade c ON at.id_categoria = c.id_categoria
            JOIN funcionario f ON at.id_funcionario = f.id_funcionario
            ORDER BY ag.data_inicio
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapAgendamento(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar agendamentos: " + e.getMessage());
        }

        return lista;
    }

    public AgendamentoAtividade getPorAtividade(int idAtividade) {
        String sql = """
        SELECT ag.id,
               ag.data_inicio,
               ag.data_fim,
               ag.observacao,
               at.id_atividade,
               at.descricao,
               c.id_categoria AS cat_id,
               c.nome AS cat_nome,
               f.id_funcionario AS func_id,
               f.nome AS func_nome
        FROM agendamento_atividade ag
        JOIN atividade at ON ag.id_atividade = at.id_atividade
        JOIN categoria_atividade c ON at.id_categoria = c.id_categoria
        JOIN funcionario f ON at.id_funcionario = f.id_funcionario
        WHERE ag.id_atividade = ?
    """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, idAtividade);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapAgendamento(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar agendamento por atividade: " + e.getMessage());
        }

        return null;
    }
}
