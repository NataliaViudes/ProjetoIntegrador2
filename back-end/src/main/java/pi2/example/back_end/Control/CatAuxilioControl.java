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
}
