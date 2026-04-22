package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.AgendamentoAtividade;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.List;

public class AgendamentoAtividadeControl {

    public AgendamentoAtividadeControl() {}

    private boolean campoVazio(AgendamentoAtividade a) {
        return a.getAtividade() == null || a.getAtividade().getId() == null
                || a.getDataInicio() == null || a.getDataInicio().isEmpty()
                || a.getDataFim() == null || a.getDataFim().isEmpty();
    }

    public ResponseEntity<?> incluir(AgendamentoAtividade agendamento) {
        if (!campoVazio(agendamento)) {
            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                AgendamentoAtividade resultado = agendamento.incluir(db);
                return ResponseEntity.ok(resultado);

            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro ao conectar com o banco de dados"));

            } catch (Exception e) {
                System.out.println("Erro geral: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

            } finally {
                db.desconectar();
            }
        } else {
            return ResponseEntity.badRequest().body(new Erro("Atividade, data início e data fim são obrigatórios!"));
        }
    }

    public ResponseEntity<?> getById(int id) {
        if (id < 0) {
            return ResponseEntity.badRequest().body(new Erro("Id do agendamento inválido"));
        } else {
            AgendamentoAtividade agendamento = new AgendamentoAtividade();
            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                AgendamentoAtividade resultado = agendamento.buscarPorId(id, db);
                if (resultado != null) {
                    return ResponseEntity.ok(resultado);
                } else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar agendamento id: " + id));
                }

            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro ao conectar com o banco de dados"));

            } catch (Exception e) {
                System.out.println("Erro geral: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

            } finally {
                db.desconectar();
            }
        }
    }

    public ResponseEntity<?> update(AgendamentoAtividade agendamento) {
        Integer id = agendamento.getId();

        if (id != null && id > 0) {
            if (!campoVazio(agendamento)) {
                Conexao db = Banco.getConexao();

                try {
                    if (!db.conectar()) {
                        throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                    }

                    AgendamentoAtividade existente = agendamento.buscarPorId(agendamento.getId(), db);

                    if (existente != null) {
                        AgendamentoAtividade a = agendamento.alterar(db);
                        if (a != null)
                            return ResponseEntity.ok(agendamento);
                        else
                            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar agendamento"));
                    } else {
                        return ResponseEntity.badRequest().body(new Erro("Agendamento não encontrado"));
                    }

                } catch (SQLException e) {
                    System.out.println("Erro SQL: " + e.getMessage());
                    return ResponseEntity.badRequest().body(new Erro("Erro com o banco"));

                } catch (Exception e) {
                    System.out.println("Erro geral: " + e.getMessage());
                    return ResponseEntity.badRequest().body(new Erro("Erro geral"));

                } finally {
                    db.desconectar();
                }
            }

            return ResponseEntity.badRequest().body(new Erro("Atividade, data início e data fim são obrigatórios para alteração"));
        } else {
            return ResponseEntity.badRequest().body(new Erro("ID é obrigatório para alteração"));
        }
    }

    public ResponseEntity<?> delete(Integer id) {
        if (id != null && id > 0) {
            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                AgendamentoAtividade agendamento = new AgendamentoAtividade(id);
                AgendamentoAtividade existente = agendamento.buscarPorId(agendamento.getId(), db);

                if (existente != null) {
                    if (agendamento.apagar(db))
                        return ResponseEntity.ok(true);
                    else
                        return ResponseEntity.badRequest().body(new Erro("Erro ao excluir agendamento"));
                } else {
                    return ResponseEntity.badRequest().body(new Erro("Agendamento não encontrado"));
                }

            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro com o banco"));

            } catch (Exception e) {
                System.out.println("Erro geral: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

            } finally {
                db.desconectar();
            }
        } else {
            return ResponseEntity.badRequest().body(new Erro("Id inválido"));
        }
    }

    public ResponseEntity<?> getAllOrFilter(String filtro) {
        Conexao db = Banco.getConexao();
        AgendamentoAtividade agendamento = new AgendamentoAtividade();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            List<AgendamentoAtividade> lista;

            if (filtro == null || filtro.isEmpty()) {
                lista = agendamento.buscarTodos(db);
            } else {
                lista = agendamento.buscarComFiltro(filtro, db);
            }

            if (lista == null || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar agendamentos: " + e.getMessage());
        } finally {
            db.desconectar();
        }
    }

}
