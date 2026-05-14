package pi2.example.back_end.Controller;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.Evento;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoControl {

    public EventoControl() {
    }

    // =====================================================
    // REGRAS DE NEGÓCIO
    // =====================================================

    private boolean eventoFinalizado(Evento evento) {

        return evento.getFim().isBefore(LocalDateTime.now());
    }

    private boolean eventoEmAndamento(Evento evento) {
        LocalDateTime agora = LocalDateTime.now();
        return (evento.getInicio().isBefore(agora) || evento.getInicio().isEqual(agora)) && (evento.getFim().isAfter(agora) || evento.getFim().isEqual(agora));
    }

    private boolean possuiConflitoHorario(Evento evento, List<Evento> eventos) {

        boolean conflito = false;

        for (Evento ev : eventos) {

            boolean mesmoEvento = false;
            if (evento.getId() != null) {
                mesmoEvento = ev.getId().equals(evento.getId());
            }

            boolean mesmoLocal =
                    evento.getLocal().trim()
                            .equalsIgnoreCase(ev.getLocal().trim());

            boolean conflitoHorario =
                    evento.getInicio().isBefore(ev.getFim())
                            &&
                            evento.getFim().isAfter(ev.getInicio());

            if (!mesmoEvento && mesmoLocal && conflitoHorario) {

                conflito = true;
            }
        }

        return conflito;
    }

    // =====================================================
    // INSERT
    // =====================================================

    public ResponseEntity<?> incluir(Evento evento) {

        if (evento == null)
            return ResponseEntity.badRequest()
                    .body(new Erro("Evento nulo"));

        String erroValidacao = evento.validar();

        if (erroValidacao != null)
            return ResponseEntity.badRequest()
                    .body(new Erro(erroValidacao));

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar())
                throw new Exception(
                        "Erro ao conectar: "
                                + db.getMensagemErro()
                );

            Evento ev = new Evento();

            List<Evento> eventosMesmoLocal =
                    ev.buscarPorLocal(db, evento.getLocal());

            if (possuiConflitoHorario(evento, eventosMesmoLocal)) {

                return ResponseEntity.badRequest()
                        .body(new Erro(
                                "Já existe um evento nesse local nesse horário"
                        ));
            }

            Evento resultado = evento.incluir(db);

            if (resultado != null)
                return ResponseEntity.ok(resultado);

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro ao inserir evento"));

        } catch (SQLException e) {

            System.out.println("Erro SQL: " + e.getMessage());

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro no banco"));

        } catch (Exception e) {

            System.out.println("Erro geral: " + e.getMessage());

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro geral"));

        } finally {

            db.desconectar();
        }
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    public ResponseEntity<?> getById(int id) {

        if (id <= 0)
            return ResponseEntity.badRequest()
                    .body(new Erro("Id inválido"));

        Evento ev = new Evento();

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar())
                throw new Exception(
                        "Erro ao conectar: "
                                + db.getMensagemErro()
                );

            Evento resultado = ev.buscarPorId(db, id);

            if (resultado != null)
                return ResponseEntity.ok(resultado);

            return ResponseEntity.badRequest()
                    .body(new Erro("Evento não encontrado"));

        } catch (SQLException e) {

            System.out.println("Erro SQL: " + e.getMessage());

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro ao conectar com o banco"));

        } catch (Exception e) {

            System.out.println("Erro geral: " + e.getMessage());

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro geral"));

        } finally {

            db.desconectar();
        }
    }

    // =====================================================
    // BUSCAR POR NOME
    // =====================================================

    public ResponseEntity<?> buscarPorNome(String nome) {

        List<Evento> eventos = new ArrayList<>();

        Evento ev = new Evento();

        if (nome == null)
            nome = "";

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar())
                throw new Exception(
                        "Erro ao conectar: "
                                + db.getMensagemErro()
                );

            eventos = ev.buscarPorNome(db, nome);

            if (eventos != null && !eventos.isEmpty())
                return ResponseEntity.ok(eventos);

            return ResponseEntity.badRequest()
                    .body(new Erro("Nenhum evento encontrado"));

        } catch (SQLException e) {

            System.out.println("Erro SQL: " + e.getMessage());

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro com o banco"));

        } catch (Exception e) {

            System.out.println("Erro geral: " + e.getMessage());

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro geral"));

        } finally {

            db.desconectar();
        }
    }

    // =====================================================
    // BUSCAR POR LOCAL
    // =====================================================

    public ResponseEntity<?> buscarPorLocal(String local) {

        List<Evento> eventos = new ArrayList<>();

        Evento ev = new Evento();

        if (local == null)
            local = "";

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar())
                throw new Exception(
                        "Erro ao conectar: "
                                + db.getMensagemErro()
                );

            eventos = ev.buscarPorLocal(db, local);

            if (eventos != null && !eventos.isEmpty())
                return ResponseEntity.ok(eventos);

            return ResponseEntity.badRequest()
                    .body(new Erro("Nenhum evento encontrado"));

        } catch (SQLException e) {

            System.out.println("Erro SQL: " + e.getMessage());

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro com o banco"));

        } catch (Exception e) {

            System.out.println("Erro geral: " + e.getMessage());

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro geral"));

        } finally {

            db.desconectar();
        }
    }

    // =====================================================
    // UPDATE
    // =====================================================

    public ResponseEntity<?> alterar(Evento evento) {

        if (evento == null)
            return ResponseEntity.badRequest()
                    .body(new Erro("Evento nulo"));

        if (evento.getId() == null || evento.getId() <= 0)
            return ResponseEntity.badRequest()
                    .body(new Erro("Id inválido"));

        String erroValidacao = evento.validar();

        if (erroValidacao != null)
            return ResponseEntity.badRequest()
                    .body(new Erro(erroValidacao));

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar())
                throw new Exception(
                        "Erro ao conectar: "
                                + db.getMensagemErro()
                );

            Evento ev = new Evento();

            Evento existente =
                    ev.buscarPorId(db, evento.getId());

            if (existente == null)
                return ResponseEntity.badRequest()
                        .body(new Erro("Evento não encontrado"));

            if (eventoFinalizado(existente))
                return ResponseEntity.badRequest()
                        .body(new Erro(
                                "Eventos finalizados não podem ser alterados"
                        ));

            List<Evento> eventosMesmoLocal =
                    ev.buscarPorLocal(db, evento.getLocal());

            if (possuiConflitoHorario(evento, eventosMesmoLocal)) {

                return ResponseEntity.badRequest()
                        .body(new Erro(
                                "Já existe um evento nesse local nesse horário"
                        ));
            }

            Evento resultado = evento.alterar(db);

            if (resultado != null)
                return ResponseEntity.ok(resultado);

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro ao alterar evento"));

        } catch (SQLException e) {

            System.out.println("Erro SQL: " + e.getMessage());

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro no banco"));

        } catch (Exception e) {

            System.out.println("Erro geral: " + e.getMessage());

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro geral"));

        } finally {

            db.desconectar();
        }
    }

    // =====================================================
    // DELETE
    // =====================================================

    public ResponseEntity<?> delete(Integer id) {

        if (id == null || id <= 0)
            return ResponseEntity.badRequest()
                    .body(new Erro("Id inválido"));

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar())
                throw new Exception(
                        "Erro ao conectar: "
                                + db.getMensagemErro()
                );

            Evento ev = new Evento();

            Evento existente =
                    ev.buscarPorId(db, id);

            if (existente == null)
                return ResponseEntity.badRequest()
                        .body(new Erro("Evento não encontrado"));

            if (eventoEmAndamento(existente))
                return ResponseEntity.badRequest()
                        .body(new Erro(
                                "Eventos em andamento não podem ser removidos"
                        ));

            if (eventoFinalizado(existente))
                return ResponseEntity.badRequest()
                        .body(new Erro(
                                "Eventos finalizados não podem ser removidos"
                        ));

            existente.setId(id);

            if (existente.apagar(db))
                return ResponseEntity.ok(true);

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro ao excluir evento"));

        } catch (SQLException e) {

            System.out.println("Erro SQL: " + e.getMessage());

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro com o banco"));

        } catch (Exception e) {

            System.out.println("Erro geral: " + e.getMessage());

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro geral"));

        } finally {

            db.desconectar();
        }
    }
}