
package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.Evento;
import pi2.example.back_end.Modelo.ItensEvento;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoControl {

    // =====================================================
    // REGRAS DE NEGÓCIO
    // =====================================================

    private boolean eventoFinalizado(Evento evento) {

        return evento.getFim().isBefore(LocalDateTime.now());
    }

    private boolean eventoEmAndamento(Evento evento) {

        LocalDateTime agora = LocalDateTime.now();

        return
                (evento.getInicio().isBefore(agora)
                        || evento.getInicio().isEqual(agora))

                        &&

                        (evento.getFim().isAfter(agora)
                                || evento.getFim().isEqual(agora));
    }

    private boolean possuiConflitoHorario(
            Evento evento,
            List<Evento> eventos
    ) {

        for (Evento ev : eventos) {

            boolean mesmoEvento = false;

            if (evento.getIdEvento() != null &&
                    ev.getIdEvento() != null) {

                mesmoEvento =
                        evento.getIdEvento()
                                .equals(ev.getIdEvento());
            }

            boolean mesmoLocal =
                    evento.getLocal().trim()
                            .equalsIgnoreCase(
                                    ev.getLocal().trim()
                            );

            boolean conflitoHorario =
                    evento.getInicio().isBefore(ev.getFim())
                            &&
                            evento.getFim().isAfter(ev.getInicio());

            if (!mesmoEvento &&
                    mesmoLocal &&
                    conflitoHorario) {

                return true;
            }
        }

        return false;
    }

    // =====================================================
    // INSERT
    // =====================================================

    public ResponseEntity<?> incluir(Evento evento) {

        if (evento == null) {

            return ResponseEntity.badRequest()
                    .body(new Erro("Evento nulo"));
        }

        String erros = evento.validar();

        if (!erros.isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(new Erro(erros));
        }

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {

                throw new Exception(
                        "Erro ao conectar: "
                                + db.getMensagemErro()
                );
            }

            Evento ev = new Evento();

            List<Evento> eventosPeriodo =
                    ev.buscarPorPeriodo(
                            db,
                            evento.getInicio(),
                            evento.getFim()
                    );

            if (possuiConflitoHorario(
                    evento,
                    eventosPeriodo
            )) {

                return ResponseEntity.badRequest()
                        .body(
                                new Erro(
                                        "Já existe um evento nesse local nesse horário"
                                )
                        );
            }

            Evento resultado = evento.incluir(db);

            if (resultado != null) {

                return ResponseEntity.ok(resultado);
            }

            return ResponseEntity.badRequest()
                    .body(
                            new Erro(
                                    "Erro ao inserir evento"
                            )
                    );

        } catch (SQLException e) {

            System.out.println("Erro SQL:");
            e.printStackTrace();

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro no banco"));

        } catch (Exception e) {

            System.out.println("Erro geral:");
            e.printStackTrace();

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

        if (id <= 0) {

            return ResponseEntity.badRequest()
                    .body(new Erro("Id inválido"));
        }

        Evento ev = new Evento();

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {

                throw new Exception(
                        "Erro ao conectar: "
                                + db.getMensagemErro()
                );
            }

            Evento resultado =
                    ev.buscarPorId(db, id);

            if (resultado != null) {

                return ResponseEntity.ok(resultado);
            }

            return ResponseEntity.badRequest()
                    .body(
                            new Erro(
                                    "Evento não encontrado"
                            )
                    );

        } catch (SQLException e) {

            System.out.println("Erro SQL:");
            e.printStackTrace();

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro no banco"));

        } catch (Exception e) {

            System.out.println("Erro geral:");
            e.printStackTrace();

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

        if (nome == null) {
            nome = "";
        }

        Evento ev = new Evento();

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {

                throw new Exception(
                        "Erro ao conectar: "
                                + db.getMensagemErro()
                );
            }

            List<Evento> eventos =
                    ev.buscarPorNome(db, nome);

            return ResponseEntity.ok(eventos);

        } catch (SQLException e) {

            System.out.println("Erro SQL:");
            e.printStackTrace();

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro no banco"));

        } catch (Exception e) {

            System.out.println("Erro geral:");
            e.printStackTrace();

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

        if (local == null) {
            local = "";
        }

        Evento ev = new Evento();

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {

                throw new Exception(
                        "Erro ao conectar: "
                                + db.getMensagemErro()
                );
            }

            List<Evento> eventos =
                    ev.buscarPorLocal(db, local);

            return ResponseEntity.ok(eventos);

        } catch (SQLException e) {

            System.out.println("Erro SQL:");
            e.printStackTrace();

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro no banco"));

        } catch (Exception e) {

            System.out.println("Erro geral:");
            e.printStackTrace();

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

        if (evento == null) {

            return ResponseEntity.badRequest()
                    .body(new Erro("Evento nulo"));
        }

        if (evento.getIdEvento() == null ||
                evento.getIdEvento() <= 0) {

            return ResponseEntity.badRequest()
                    .body(new Erro("Id inválido"));
        }

        String erros = evento.validar();

        if (!erros.isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(new Erro (erros));
        }

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {

                throw new Exception(
                        "Erro ao conectar: "
                                + db.getMensagemErro()
                );
            }

            Evento ev = new Evento();

            Evento existente =
                    ev.buscarPorId(
                            db,
                            evento.getIdEvento()
                    );

            if (existente == null) {

                return ResponseEntity.badRequest()
                        .body(
                                new Erro(
                                        "Evento não encontrado"
                                )
                        );
            }

            if (eventoFinalizado(existente)) {

                return ResponseEntity.badRequest()
                        .body(
                                new Erro(
                                        "Eventos finalizados não podem ser alterados"
                                )
                        );
            }

            List<Evento> eventosPeriodo =
                    ev.buscarPorPeriodo(
                            db,
                            evento.getInicio(),
                            evento.getFim()
                    );

            if (possuiConflitoHorario(
                    evento,
                    eventosPeriodo
            )) {

                return ResponseEntity.badRequest()
                        .body(
                                new Erro(
                                        "Já existe um evento nesse local nesse horário"
                                )
                        );
            }

            Evento resultado =
                    evento.alterar(db);

            if (resultado != null) {

                return ResponseEntity.ok(resultado);
            }

            return ResponseEntity.badRequest()
                    .body(
                            new Erro(
                                    "Erro ao alterar evento"
                            )
                    );

        } catch (SQLException e) {

            System.out.println("Erro SQL:");
            e.printStackTrace();

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro no banco"));

        } catch (Exception e) {

            System.out.println("Erro geral:");
            e.printStackTrace();

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

        if (id == null || id <= 0) {

            return ResponseEntity.badRequest()
                    .body(new Erro("Id inválido"));
        }

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            Evento ev = new Evento();

            Evento existente = ev.buscarPorId(db, id);

            if (existente == null) {
                return ResponseEntity.badRequest().body(new Erro("Evento não encontrado"));
            }

            if (eventoEmAndamento(existente)) {
                return ResponseEntity.badRequest().body(new Erro("Eventos em andamento não podem ser removidos"));
            }

            if (eventoFinalizado(existente)) {
                return ResponseEntity.badRequest().body(new Erro("Eventos finalizados não podem ser removidos"));
            }

            existente.setIdEvento(id);

            ItensEvento it = new ItensEvento();
            List<ItensEvento> listaItens = it.buscarItensDoEvento(db,id);



            if(listaItens!=null && !listaItens.isEmpty())
                return ResponseEntity.badRequest().body(new Erro("Esse evento possui itens! Exclua eles antes de excluir o evento."));

            if (existente.apagar(db)) {

                return ResponseEntity.ok(true);
            }

            return ResponseEntity.badRequest()
                    .body(
                            new Erro(
                                    "Erro ao excluir evento"
                            )
                    );

        } catch (SQLException e) {

            System.out.println("Erro SQL:");
            e.printStackTrace();

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro no banco"));

        } catch (Exception e) {

            System.out.println("Erro geral:");
            e.printStackTrace();

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro geral"));

        } finally {

            db.desconectar();
        }
    }

    public ResponseEntity<?> buscarPorPeriodo(
            LocalDateTime inicio,
            LocalDateTime fim
    ) {

        if (inicio == null || fim == null) {
            return ResponseEntity.badRequest()
                    .body(new Erro("Datas inválidas"));
        }

        if (!fim.isAfter(inicio)) {
            return ResponseEntity.badRequest()
                    .body(new Erro("Fim deve ser após início"));
        }

        Evento ev = new Evento();
        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {
                throw new Exception(db.getMensagemErro());
            }

            List<Evento> eventos =
                    ev.buscarPorPeriodo(db, inicio, fim);

            return ResponseEntity.ok(eventos);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro ao buscar período"));

        } finally {
            db.desconectar();
        }
    }



    public ResponseEntity<?> listarTodos() {

        Evento ev = new Evento();
        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {
                throw new Exception(db.getMensagemErro());
            }

            List<Evento> eventos = ev.listar(db);

            return ResponseEntity.ok(eventos);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro ao listar eventos"));

        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> verificarConflito(Evento evento) {

        if (evento == null) {
            return ResponseEntity.badRequest()
                    .body(new Erro("Evento nulo"));
        }

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {
                throw new Exception(db.getMensagemErro());
            }

            Evento ev = new Evento();

            List<Evento> eventosPeriodo =
                    ev.buscarPorPeriodo(
                            db,
                            evento.getInicio(),
                            evento.getFim()
                    );

            boolean conflito =
                    possuiConflitoHorario(evento, eventosPeriodo);

            return ResponseEntity.ok(conflito);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.badRequest()
                    .body(new Erro("Erro ao verificar conflito"));

        } finally {
            db.desconectar();
        }
    }
}

