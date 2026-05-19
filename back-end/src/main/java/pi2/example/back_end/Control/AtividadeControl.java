package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Atividade;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.List;

public class AtividadeControl {
    public AtividadeControl() {}

    private boolean campoVazio(Atividade a) {
        if (a.getDescricao() != null && !a.getDescricao().isEmpty()
                && a.getCategoria() != null && a.getCategoria().getId() > 0
                && a.getFuncionario() != null && a.getFuncionario().getId() > 0) {
            return false;
        }
        return true;
    }

    public ResponseEntity<?> incluir(Atividade atividade) {
        if (!campoVazio(atividade)) {
            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                Atividade resultado = atividade.incluir(db);
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
            return ResponseEntity.badRequest().body(new Erro("Id da atividade inválido"));
        } else {
            Atividade atividade = new Atividade();
            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                Atividade resultado = atividade.buscarPorId(id, db);
                if (resultado != null) {
                    return ResponseEntity.ok(resultado);
                } else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar atividade id: " + id));
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

    public ResponseEntity<?> buscaPorNome(String nome) {
        List<Atividade> atividades;
        Atividade atividade = new Atividade();

        if (nome != null && !nome.isEmpty()) {
            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                atividades = atividade.buscarPorNome(nome, db);
                if (atividades != null && !atividades.isEmpty()) {
                    return ResponseEntity.ok(atividades);
                } else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar atividade: " + nome));
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

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            atividades = atividade.buscarPorNome("", db);
            if (atividades != null && !atividades.isEmpty()) {
                return ResponseEntity.ok(atividades);
            } else {
                return ResponseEntity.badRequest().body(new Erro("Nenhuma atividade encontrada"));
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

    public ResponseEntity<?> update(Atividade atividade) {
        Integer id = atividade.getId();

        if (id != null && id > 0) {
            if (!campoVazio(atividade)) {
                Conexao db = Banco.getConexao();

                try {
                    if (!db.conectar()) {
                        throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                    }

                    Atividade existente = atividade.buscarPorId(atividade.getId(), db);
                    if (existente != null) {
                        Atividade a = atividade.alterar(db);
                        if (a != null)
                            return ResponseEntity.ok(atividade);
                        else
                            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar atividade"));
                    } else {
                        return ResponseEntity.badRequest().body(new Erro("Atividade não encontrada"));
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

                Atividade atividade = new Atividade(id);
                Atividade existente = atividade.buscarPorId(atividade.getId(), db);

                if (existente != null) {
                    if (atividade.apagar(db))
                        return ResponseEntity.ok(true);
                    else
                        return ResponseEntity.badRequest().body(new Erro("Erro ao excluir atividade"));
                } else {
                    return ResponseEntity.badRequest().body(new Erro("Atividade não encontrada"));
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
        Atividade atividade = new Atividade();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            List<Atividade> lista;

            if (filtro == null || filtro.isEmpty()) {
                lista = atividade.buscarTodos(db);
            } else {
                lista = atividade.buscarComFiltro(filtro, db);
            }

            if (lista == null || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar atividades: " + e.getMessage());
        } finally {
            db.desconectar();
        }
    }

}
