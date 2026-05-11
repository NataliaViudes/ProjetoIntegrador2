package pi2.example.back_end.Controller;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.Evento;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventoControl {

    public EventoControl() {
    }

    // 🔹 INSERT
    public ResponseEntity<?> incluir(Evento evento) {

        if (evento == null)
            return ResponseEntity.badRequest().body(new Erro("Evento nulo"));

        if (evento.getIdCatEvento() == null || evento.getIdCatEvento() <= 0)
            return ResponseEntity.badRequest().body(new Erro("Categoria inválida"));

        if (evento.getQtd() == null || evento.getQtd() <= 0)
            return ResponseEntity.badRequest().body(new Erro("Quantidade inválida"));

        if (evento.getNome() == null || evento.getNome().isEmpty())
            return ResponseEntity.badRequest().body(new Erro("Nome inválido"));

        if (evento.getData() == null)
            return ResponseEntity.badRequest().body(new Erro("Data obrigatória"));

        if (evento.getHoraInicio() == null || evento.getHoraFim() == null)
            return ResponseEntity.badRequest().body(new Erro("Horário inválido"));

        if (!evento.getHoraFim().isAfter(evento.getHoraInicio()))
            return ResponseEntity.badRequest().body(new Erro("Hora fim deve ser após início"));

        if (evento.getData().isBefore(LocalDate.now()))
            return ResponseEntity.badRequest()
                    .body(new Erro("Não é permitido cadastrar eventos em datas passadas"));

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar())
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());

            Evento resultado = evento.incluir(db);

            if (resultado != null)
                return ResponseEntity.ok(resultado);
            else
                return ResponseEntity.badRequest().body(new Erro("Erro ao inserir evento"));

        } catch (SQLException e) {
            System.out.println("Erro SQL: " + e.getMessage());
            return ResponseEntity.badRequest().body(new Erro("Erro no banco"));

        } catch (Exception e) {
            System.out.println("Erro geral: " + e.getMessage());
            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {
            db.desconectar();
        }
    }

    // GET BY ID
    public ResponseEntity<?> getById(int id) {
        if (id < 0) {
            return ResponseEntity.badRequest().body(new Erro("Id invalido"));
        } else {
            Evento ev = new Evento();
            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                Evento resultado = ev.buscarPorId(db, id);
                if (resultado != null) {
                    return ResponseEntity.ok(resultado);
                } else {
                    return ResponseEntity.badRequest().body(new Erro("Evento não encontrado id: " + id));
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

    // 🔹 BUSCAR POR NOME
    public ResponseEntity<?> buscarPorNome(String nome) {
        List<Evento> eventos = new ArrayList<>();
        Evento ev = new Evento();

        if (nome == null) nome = "";

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            eventos = ev.buscarPorNome(db, nome);

            if (eventos != null && !eventos.isEmpty()) {
                return ResponseEntity.ok(eventos);
            } else {
                return ResponseEntity.badRequest().body(new Erro("Erro ao buscar evento: " + nome));
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

    // 🔹 BUSCAR POR LOCAL
    public ResponseEntity<?> buscarPorLocal(String local) {
        List<Evento> eventos = new ArrayList<>();
        Evento ev = new Evento();

        if (local == null) local = "";

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            eventos = ev.buscarPorLocal(db, local);

            if (eventos != null && !eventos.isEmpty()) {
                return ResponseEntity.ok(eventos);
            } else {
                return ResponseEntity.badRequest().body(new Erro("Erro ao buscar evento no local: " + local));
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

    //  UPDATE
    public ResponseEntity<?> alterar(Evento evento) {

        if (evento == null)
            return ResponseEntity.badRequest().body(new Erro("Evento nulo"));

        if (evento.getId() == null || evento.getId() <= 0)
            return ResponseEntity.badRequest().body(new Erro("Id inválido"));

        if (evento.getIdCatEvento() == null || evento.getIdCatEvento() <= 0)
            return ResponseEntity.badRequest().body(new Erro("Categoria inválida"));

        if (evento.getQtd() == null || evento.getQtd() <= 0)
            return ResponseEntity.badRequest().body(new Erro("Quantidade inválida"));

        if (evento.getNome() == null || evento.getNome().isEmpty())
            return ResponseEntity.badRequest().body(new Erro("Nome inválido"));

        if (evento.getData() == null)
            return ResponseEntity.badRequest().body(new Erro("Data obrigatória"));

        if (evento.getHoraInicio() == null || evento.getHoraFim() == null)
            return ResponseEntity.badRequest().body(new Erro("Horário inválido"));

        if (!evento.getHoraFim().isAfter(evento.getHoraInicio()))
            return ResponseEntity.badRequest().body(new Erro("Hora fim deve ser após início"));

        if (evento.getData().isBefore(LocalDate.now()))
            return ResponseEntity.badRequest()
                    .body(new Erro("Não é permitido atualizar eventos em datas passadas"));

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar())
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());

            Evento existente = new Evento().buscarPorId(db, evento.getId());

            if (existente == null)
                return ResponseEntity.badRequest()
                        .body(new Erro("Evento não encontrado"));

            if (existente.getData().isBefore(LocalDate.now()))
                return ResponseEntity.badRequest()
                        .body(new Erro("Eventos passados não podem ser alterados"));

            Evento resultado = evento.alterar(db);

            if (resultado != null)
                return ResponseEntity.ok(resultado);
            else
                return ResponseEntity.badRequest().body(new Erro("Erro ao alterar evento"));

        } catch (SQLException e) {
            System.out.println("Erro SQL: " + e.getMessage());
            return ResponseEntity.badRequest().body(new Erro("Erro no banco"));

        } catch (Exception e) {
            System.out.println("Erro geral: " + e.getMessage());
            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {
            db.desconectar();
        }
    }

    //  DELETE
    public ResponseEntity<?> delete(Integer id) {
        if (id != null && id > 0) {

            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                Evento ev = new Evento();
                Evento existente = ev.buscarPorId(db, id);

                if (existente != null) {

                    existente.setId(id);

                    if (existente.apagar(db))
                        return ResponseEntity.ok(true);
                    else
                        return ResponseEntity.badRequest().body(new Erro("Erro ao excluir evento"));

                } else {
                    return ResponseEntity.badRequest().body(new Erro("Evento não encontrado"));
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
            return ResponseEntity.badRequest().body(new Erro("id invalido"));
        }
    }
}
