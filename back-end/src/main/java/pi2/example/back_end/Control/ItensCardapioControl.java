package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.ItensCardapio;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class ItensCardapioControl {

    private boolean campoVazio(ItensCardapio i) {
        return i.getAlimento() == null || i.getAlimento().getId() == null
                || i.getCardapio() == null || i.getCardapio().getId() == null
                || i.getQuantidade() == null || i.getQuantidade() <= 0;
    }

    public ResponseEntity<?> incluir(ItensCardapio item) {

        if (!campoVazio(item)) {

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

        } else {
            return ResponseEntity.badRequest().body(new Erro("Campos obrigatórios"));
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
                return ResponseEntity.badRequest().body(new Erro("Erro ao deletar"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro"));
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
            return ResponseEntity.badRequest().body(new Erro("Erro"));
        } finally {
            db.desconectar();
        }
    }
}