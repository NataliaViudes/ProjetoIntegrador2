package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.DAO.AgendamentoAtividadeDAO;
import pi2.example.back_end.DAO.DAOEtapa;
import pi2.example.back_end.Modelo.AgendamentoAtividade;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.Etapa;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class EtapaControl {
    public EtapaControl() {}

    private Timestamp converterTimestamp(String dataHora) {
        if (dataHora == null || dataHora.isEmpty()) return null;

        String valor = dataHora.replace("T", " ");

        if (valor.length() == 16) {
            valor += ":00";
        }

        return Timestamp.valueOf(valor);
    }

    public ResponseEntity<?> incluir(Etapa etapa) {

        if (etapa.getIdAgendamento() > 0) {

            if (etapa.getDescricao() != null && !etapa.getDescricao().isEmpty()) {

                if (etapa.getDataHoraInicio() != null && !etapa.getDataHoraInicio().isEmpty()) {

                    Conexao db = Banco.getConexao();

                    try {
                        if (!db.conectar()) {
                            throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                        }

                        AgendamentoAtividadeDAO agDAO = new AgendamentoAtividadeDAO(db);

                        AgendamentoAtividade agendamento = agDAO.get(etapa.getIdAgendamento());

                        if (agendamento == null) {
                            return ResponseEntity.badRequest().body(new Erro("Agendamento não encontrado"));
                        }

                        Timestamp inicioAg = converterTimestamp(agendamento.getDataInicio());
                        Timestamp fimAg = converterTimestamp(agendamento.getDataFim());
                        Timestamp horaEtapa = converterTimestamp(etapa.getDataHoraInicio());

                        if (horaEtapa == null) {
                            return ResponseEntity
                                    .badRequest()
                                    .body(new Erro("Data/Hora inválida"));
                        }

                        Timestamp agora = new Timestamp(System.currentTimeMillis());

                        if (horaEtapa.before(agora)) {
                            return ResponseEntity
                                    .badRequest()
                                    .body(new Erro("Atividade ja foi encerrada"));
                        }

                        if (horaEtapa.before(inicioAg) || horaEtapa.after(fimAg)) {
                            return ResponseEntity
                                    .badRequest()
                                    .body(new Erro("A etapa está fora do horário do agendamento"));
                        }

                        DAOEtapa dao = new DAOEtapa(db);
                        Etapa resultado = dao.inserir(etapa);
                        if (resultado == null) {
                            return ResponseEntity
                                    .badRequest()
                                    .body(new Erro("Erro DAO ao inserir etapa"));
                        }
                        return ResponseEntity.ok(resultado);

                    } catch (SQLException e) {
                        System.out.println("Erro SQL: " + e.getMessage());
                        return ResponseEntity
                                .badRequest()
                                .body(new Erro("Erro no banco de dados"));

                    } catch (Exception e) {
                        System.out.println("Erro geral: " + e.getMessage());
                        return ResponseEntity
                                .badRequest()
                                .body(new Erro(e.getMessage()));

                    } finally {
                        db.desconectar();
                    }

                } else {
                    return ResponseEntity.badRequest().body(new Erro("Data/Hora inválida"));
                }

            } else {
                return ResponseEntity.badRequest().body(new Erro("Descrição inválida"));
            }

        } else {
            return ResponseEntity.badRequest() .body(new Erro("Agendamento inválido"));
        }
    }

    public ResponseEntity<?> listarPorAgendamento(int idAgendamento) {
        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar");
            }

            DAOEtapa dao = new DAOEtapa(db);
            List<Etapa> lista = dao.listarPorAgendamento(idAgendamento);

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new Erro(e.getMessage()));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> excluir(int id) {
        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar");
            }

            DAOEtapa dao = new DAOEtapa(db);
            boolean ok = dao.excluir(id);

            return ResponseEntity.ok(ok);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro(e.getMessage()));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> atualizar(Etapa etapa) {
        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar");
            }

            DAOEtapa dao = new DAOEtapa(db);

            Etapa existente = dao.buscarPorId(etapa.getId());

            if (existente == null) {
                return ResponseEntity.badRequest()
                        .body(new Erro("Etapa não encontrada"));
            }

            Timestamp agora = new Timestamp(System.currentTimeMillis());

            Timestamp dataExistente = converterTimestamp(existente.getDataHoraInicio());

            if (dataExistente.before(agora)) {
                return ResponseEntity.badRequest()
                        .body(new Erro("Não é permitido alterar atividades que já aconteceram"));
            }

            Timestamp dataHora = converterTimestamp(etapa.getDataHoraInicio());

            if (dataHora == null) {
                return ResponseEntity.badRequest()
                        .body(new Erro("Data/Hora inválida"));
            }

            if (dataHora.before(agora)) {
                return ResponseEntity.badRequest()
                        .body(new Erro("Não é permitido mover etapa para o passado"));
            }

            boolean ok = dao.atualizar(etapa);

            return ResponseEntity.ok(ok);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new Erro(e.getMessage()));
        } finally {
            db.desconectar();
        }
    }
}