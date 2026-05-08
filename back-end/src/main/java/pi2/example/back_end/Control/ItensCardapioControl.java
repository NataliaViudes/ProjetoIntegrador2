package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.ItensCardapio;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class ItensCardapioControl {

    private boolean camposInvalidos(ItensCardapio i) {
        return i.getAlimento() == null || i.getAlimento().getId() == null
                || i.getCardapio() == null || i.getCardapio().getId() == null
                || i.getQuantidade() == null;
    }

    public ResponseEntity<?> salvarOuAtualizar(ItensCardapio item) {
        if (item.getAlimento() == null || item.getCardapio() == null || item.getQuantidade() == null) {
            return ResponseEntity.badRequest().body(new Erro("Dados incompletos"));
        }

        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar()) throw new Exception();

            // Buscar item existente usando o modelo
            ItensCardapio existente = item.buscarPorIds(db, item.getAlimento().getId(), item.getCardapio().getId());

            if (item.getQuantidade() <= 0) {
                // quantidade 0 -> apagar se existir
                if (existente != null) {
                    existente.apagar(db);
                }
                return ResponseEntity.ok("Item removido");
            } else {
                if (existente == null) {
                    // não existe -> incluir
                    item.incluir(db);
                } else {
                    // existe -> alterar
                    existente.setQuantidade(item.getQuantidade());
                    existente.alterar(db);
                }
                return ResponseEntity.ok(item);
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao processar item: " + e.getMessage()));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> incluir(ItensCardapio item) {
        if (camposInvalidos(item)) {
            return ResponseEntity.badRequest().body(new Erro("Campos obrigatórios"));
        }

        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar())
                throw new Exception();

            ItensCardapio resultado = item.incluir(db);
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao inserir item"));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> alterar(ItensCardapio item) {
        if (camposInvalidos(item)) {
            return ResponseEntity.badRequest().body(new Erro("Campos obrigatórios"));
        }

        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar())
                throw new Exception();

            ItensCardapio resultado = item.alterar(db);
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao atualizar item"));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> delete(ItensCardapio item) {
        if (item.getAlimento() == null || item.getCardapio() == null)
            return ResponseEntity.badRequest().body(new Erro("Dados inválidos"));

        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar())
                throw new Exception();

            if (item.apagar(db))
                return ResponseEntity.ok(true);
            else
                return ResponseEntity.badRequest().body(new Erro("Erro ao deletar item"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao deletar"));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> getByCardapio(int id) {
        if (id <= 0)
            return ResponseEntity.badRequest().body(new Erro("Id inválido"));

        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar())
                throw new Exception();

            List<ItensCardapio> lista = new ItensCardapio().buscarPorCardapio(db, id);
            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao buscar itens"));
        } finally {
            db.desconectar();
        }
    }
}