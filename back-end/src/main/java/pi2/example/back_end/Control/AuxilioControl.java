package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Auxilio;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.List;


public class AuxilioControl {

    public AuxilioControl() {}

    private boolean campoVazio(Auxilio a) {
        return a.getDescricao() == null || a.getDescricao().isEmpty()
                || a.getBeneficiario() == null
                || a.getBeneficiario().getId() == null
                || a.getCategoria() == null
                || a.getCategoria().getId() == null;
    }

    public ResponseEntity<?> incluir(Auxilio auxilio) {
        if (!campoVazio(auxilio)) {
            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                Auxilio resultado = auxilio.incluir(db);
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
            return ResponseEntity.badRequest().body(new Erro("Todos os campos são obrigatórios!"));
        }
    }

    public ResponseEntity<?> getById(int id) {
        if (id < 0) {
            return ResponseEntity.badRequest().body(new Erro("Id do auxílio inválido"));
        } else {
            Auxilio auxilio = new Auxilio();
            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                Auxilio resultado = auxilio.buscarPorId(id, db);
                if (resultado != null) {
                    return ResponseEntity.ok(resultado);
                } else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar auxílio id: " + id));
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

    public ResponseEntity<?> update(Auxilio auxilio) {
        Integer id = auxilio.getId();

        if (id != null && id > 0) {
            if (!campoVazio(auxilio)) {
                Conexao db = Banco.getConexao();

                try {
                    if (!db.conectar()) {
                        throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                    }

                    Auxilio existente = auxilio.buscarPorId(auxilio.getId(), db);
                    if (existente != null) {
                        Auxilio a = auxilio.alterar(db);
                        if (a != null)
                            return ResponseEntity.ok(auxilio);
                        else
                            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar auxílio"));
                    } else {
                        return ResponseEntity.badRequest().body(new Erro("Auxílio não encontrado"));
                    }

                } catch (SQLException e) {
                    System.out.println("Erro SQL: " + e.getMessage());
                    return ResponseEntity.badRequest().body(new Erro("Erro com o banco"));

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Erro geral: " + e.getMessage());
                    return ResponseEntity.badRequest().body(new Erro("Erro geral"));

                } finally {
                    db.desconectar();
                }
            }

            return ResponseEntity.badRequest().body(new Erro("Todos os campos são obrigatórios para alteração"));
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

                Auxilio auxilio = new Auxilio(id);
                Auxilio existente = auxilio.buscarPorId(auxilio.getId(), db);

                if (existente != null) {
                    if (auxilio.apagar(db))
                        return ResponseEntity.ok(true);
                    else
                        return ResponseEntity.badRequest().body(new Erro("Erro ao excluir auxílio"));
                } else {
                    return ResponseEntity.badRequest().body(new Erro("Auxílio não encontrado"));
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
        Auxilio auxilio = new Auxilio();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            List<Auxilio> lista;

            if (filtro == null || filtro.isEmpty()) {
                lista = auxilio.buscarTodos(db);
            } else {
                lista = auxilio.buscarComFiltro(filtro, db);
            }

            if (lista == null || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar auxílios: " + e.getMessage());
        } finally {
            db.desconectar();
        }
    }
}

