package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.CategoriaAuxilio;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.util.List;



public class CatAuxilioControl {
    public CatAuxilioControl() {}

    public ResponseEntity<?> getById(int id) {
        if (id < 0) {
            return ResponseEntity.badRequest().body("Id da categoria de auxílio inválido");
        } else {
            CategoriaAuxilio categoria = new CategoriaAuxilio();
            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                CategoriaAuxilio resultado = categoria.buscarPorId(id, db);

                if (resultado != null) {
                    return ResponseEntity.ok(resultado);
                } else {
                    return ResponseEntity.badRequest().body("Categoria de auxílio não encontrada");
                }

            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Erro ao buscar categoria de auxílio: " + e.getMessage());
            } finally {
                db.desconectar();
            }
        }
    }

    public ResponseEntity<?> buscaPorNome(String nome) {
        CategoriaAuxilio categoria = new CategoriaAuxilio();
        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            List<CategoriaAuxilio> lista = categoria.buscarPorNome(nome, db);

            if (lista == null || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar categoria de auxílio: " + e.getMessage());
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> getAllOrFilter(String filtro) {
        Conexao db = Banco.getConexao();
        CategoriaAuxilio categoria = new CategoriaAuxilio();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            List<CategoriaAuxilio> lista;

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
            return ResponseEntity.internalServerError().body("Erro ao buscar categorias de auxílio: " + e.getMessage());
        } finally {
            db.desconectar();
        }
    }
    private boolean campoVazio(CategoriaAuxilio c) {
        return c.getNome() == null || c.getNome().trim().isEmpty();
    }

    public ResponseEntity<?> incluir(CategoriaAuxilio categoria) {
        if (campoVazio(categoria)) {
            return ResponseEntity.badRequest().body("Nome é obrigatório!");
        }

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            CategoriaAuxilio resultado = categoria.incluir(db);
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao incluir categoria de auxílio: " + e.getMessage());
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> update(CategoriaAuxilio categoria) {
        if (categoria.getId() == null || categoria.getId() <= 0) {
            return ResponseEntity.badRequest().body("ID é obrigatório para alteração");
        }

        if (campoVazio(categoria)) {
            return ResponseEntity.badRequest().body("Nome é obrigatório para alteração");
        }

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            CategoriaAuxilio existente = categoria.buscarPorId(categoria.getId(), db);

            if (existente == null) {
                return ResponseEntity.badRequest().body("Categoria de auxílio não encontrada");
            }

            CategoriaAuxilio resultado = categoria.alterar(db);

            if (resultado != null) {
                return ResponseEntity.ok(resultado);
            }

            return ResponseEntity.badRequest().body("Erro ao alterar categoria de auxílio");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao alterar categoria de auxílio: " + e.getMessage());
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> delete(Integer id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("Id inválido");
        }

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            CategoriaAuxilio categoria = new CategoriaAuxilio(id);
            CategoriaAuxilio existente = categoria.buscarPorId(id, db);

            if (existente == null) {
                return ResponseEntity.badRequest().body("Categoria de auxílio não encontrada");
            }

            if (categoria.apagar(db)) {
                return ResponseEntity.ok(true);
            }

            return ResponseEntity.badRequest()
                    .body("Não é possível excluir esta categoria, pois ela está vinculada a um auxílio.");

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body("Não é possível excluir esta categoria, pois ela está vinculada a um auxílio.");

        } finally {
            db.desconectar();
        }
    }
}
