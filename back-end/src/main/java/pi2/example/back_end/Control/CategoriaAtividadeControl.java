package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.CategoriaAtividade;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.List;

public class CategoriaAtividadeControl {
    public CategoriaAtividadeControl() {}

    private boolean campoVazio(CategoriaAtividade c) {
        return c.getNome() == null || c.getNome().isEmpty();
    }

    public ResponseEntity<?> incluir(CategoriaAtividade categoria) {
        if (!campoVazio(categoria)) {
            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                CategoriaAtividade resultado = categoria.incluir(db);
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
            return ResponseEntity.badRequest().body(new Erro("Nome é obrigatório!"));
        }
    }

    public ResponseEntity<?> getById(int id) {
        if (id < 0) {
            return ResponseEntity.badRequest().body(new Erro("Id da categoria inválido"));
        } else {
            CategoriaAtividade categoria = new CategoriaAtividade();
            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                CategoriaAtividade resultado = categoria.buscarPorId(id, db);
                if (resultado != null) {
                    return ResponseEntity.ok(resultado);
                } else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar categoria id: " + id));
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
        List<CategoriaAtividade> categorias;
        CategoriaAtividade categoria = new CategoriaAtividade();

        if (nome != null && !nome.isEmpty()) {
            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                categorias = categoria.buscarPorNome(nome, db);
                if (categorias != null && !categorias.isEmpty()) {
                    return ResponseEntity.ok(categorias);
                } else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar categoria: " + nome));
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

            categorias = categoria.buscarPorNome("", db);
            if (categorias != null && !categorias.isEmpty()) {
                return ResponseEntity.ok(categorias);
            } else {
                return ResponseEntity.badRequest().body(new Erro("Nenhuma categoria encontrada"));
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

    public ResponseEntity<?> update(CategoriaAtividade categoria) {
        Integer id = categoria.getId();

        if (id != null && id > 0) {
            if (!campoVazio(categoria)) {
                Conexao db = Banco.getConexao();

                try {
                    if (!db.conectar()) {
                        throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                    }

                    CategoriaAtividade existente = categoria.buscarPorId(categoria.getId(), db);
                    if (existente != null) {
                        CategoriaAtividade c = categoria.alterar(db);
                        if (c != null)
                            return ResponseEntity.ok(categoria);
                        else
                            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar categoria"));
                    } else {
                        return ResponseEntity.badRequest().body(new Erro("Categoria não encontrada"));
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

            return ResponseEntity.badRequest().body(new Erro("Nome é obrigatório para alteração"));
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

                CategoriaAtividade categoria = new CategoriaAtividade(id);
                CategoriaAtividade existente = categoria.buscarPorId(categoria.getId(), db);

                if (existente != null) {
                    if (categoria.apagar(db))
                        return ResponseEntity.ok(true);
                    else
                        return ResponseEntity.badRequest().body(new Erro("Erro ao excluir categoria"));
                } else {
                    return ResponseEntity.badRequest().body(new Erro("Categoria não encontrada"));
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
        CategoriaAtividade categoria = new CategoriaAtividade();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            List<CategoriaAtividade> lista;

            if (filtro == null || filtro.isEmpty()) {
                lista = categoria.buscarTodos(db);
            } else {
                lista = categoria.buscarComFiltro(filtro, db);
            }

            if (lista == null || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar categorias: " + e.getMessage());
        } finally {
            db.desconectar();
        }
    }
}

