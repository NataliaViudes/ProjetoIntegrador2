package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.DAO.OcorrenciaAtividadeDAOImpl;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.OcorrenciaAtividade;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class OcorrenciaAtividadeControl {
    private boolean campoVazio(OcorrenciaAtividade o) {
        return o.getAgendamento() == null
                || o.getAgendamento().getId() == null
                || o.getTipo() == null
                || o.getTipo().trim().isEmpty()
                || o.getObservacao() == null
                || o.getObservacao().trim().isEmpty();
    }

    public ResponseEntity<?> incluir(OcorrenciaAtividade ocorrencia) {
        if (campoVazio(ocorrencia)) {
            return ResponseEntity.badRequest()
                    .body(new Erro("Agendamento, tipo e observação são obrigatórios."));
        }

        if (!ocorrencia.getTipo().equals("GERAL") && !ocorrencia.getTipo().equals("INDIVIDUAL")) {
            return ResponseEntity.badRequest()
                    .body(new Erro("Tipo de ocorrência inválido."));
        }

        if (ocorrencia.getTipo().equals("GERAL") && ocorrencia.getBeneficiario() != null) {
            return ResponseEntity.badRequest()
                    .body(new Erro("Ocorrência geral não deve possuir beneficiário."));
        }

        if (ocorrencia.getTipo().equals("INDIVIDUAL")) {
            if (ocorrencia.getBeneficiario() == null || ocorrencia.getBeneficiario().getId() == null) {
                return ResponseEntity.badRequest()
                        .body(new Erro("Selecione um beneficiário para ocorrência individual."));
            }
        }

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            OcorrenciaAtividadeDAOImpl dao = new OcorrenciaAtividadeDAOImpl(db);

            if (!dao.agendamentoEstaAcontecendoAgora(ocorrencia.getAgendamento().getId())) {
                return ResponseEntity.badRequest()
                        .body(new Erro("Só é possível registrar ocorrência durante o horário da atividade."));
            }

            OcorrenciaAtividade resultado = ocorrencia.incluir(db);

            if (resultado != null) {
                return ResponseEntity.ok(resultado);
            }

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro ao registrar ocorrência."));

        } catch (Exception e) {
            System.out.println("Erro geral ao registrar ocorrência: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new Erro("Erro ao registrar ocorrência."));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> relatorio(String dataInicio, String dataFim, Integer idBeneficiario) {
        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            OcorrenciaAtividadeDAOImpl dao = new OcorrenciaAtividadeDAOImpl(db);
            List<OcorrenciaAtividade> lista = dao.relatorio(dataInicio, dataFim, idBeneficiario);

            if (lista == null || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new Erro("Erro ao gerar relatório de ocorrências."));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> update(OcorrenciaAtividade ocorrencia) {
        if (ocorrencia.getId() == null || ocorrencia.getId() <= 0) {
            return ResponseEntity.badRequest().body(new Erro("ID da ocorrência é obrigatório."));
        }

        if (campoVazio(ocorrencia)) {
            return ResponseEntity.badRequest()
                    .body(new Erro("Agendamento, tipo e observação são obrigatórios."));
        }

        if (!ocorrencia.getTipo().equals("GERAL") && !ocorrencia.getTipo().equals("INDIVIDUAL")) {
            return ResponseEntity.badRequest()
                    .body(new Erro("Tipo de ocorrência inválido."));
        }

        if (ocorrencia.getTipo().equals("GERAL") && ocorrencia.getBeneficiario() != null) {
            return ResponseEntity.badRequest()
                    .body(new Erro("Ocorrência geral não deve possuir beneficiário."));
        }

        if (ocorrencia.getTipo().equals("INDIVIDUAL")) {
            if (ocorrencia.getBeneficiario() == null || ocorrencia.getBeneficiario().getId() == null) {
                return ResponseEntity.badRequest()
                        .body(new Erro("Selecione um beneficiário para ocorrência individual."));
            }
        }

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            OcorrenciaAtividadeDAOImpl dao = new OcorrenciaAtividadeDAOImpl(db);

            OcorrenciaAtividade existente = dao.get(ocorrencia.getId());

            if (existente == null) {
                return ResponseEntity.badRequest().body(new Erro("Ocorrência não encontrada."));
            }

            if (!dao.dentroDoPrazoDeEdicao(ocorrencia.getId())) {
                return ResponseEntity.badRequest()
                        .body(new Erro("Só é possível editar ocorrência até 30 minutos após o registro."));
            }

            OcorrenciaAtividade resultado = ocorrencia.alterar(db);

            if (resultado != null) {
                return ResponseEntity.ok(resultado);
            }

            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar ocorrência."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar ocorrência."));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> delete(Integer id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body(new Erro("ID da ocorrência inválido."));
        }

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            OcorrenciaAtividadeDAOImpl dao = new OcorrenciaAtividadeDAOImpl(db);

            OcorrenciaAtividade existente = dao.get(id);

            if (existente == null) {
                return ResponseEntity.badRequest().body(new Erro("Ocorrência não encontrada."));
            }

            if (!dao.dentroDoPrazoDeEdicao(id)) {
                return ResponseEntity.badRequest()
                        .body(new Erro("Só é possível excluir ocorrência até 30 minutos após o registro."));
            }

            OcorrenciaAtividade ocorrencia = new OcorrenciaAtividade();
            ocorrencia.setId(id);

            if (ocorrencia.apagar(db)) {
                return ResponseEntity.ok(true);
            }

            return ResponseEntity.badRequest().body(new Erro("Erro ao excluir ocorrência."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao excluir ocorrência."));
        } finally {
            db.desconectar();
        }
    }
}
