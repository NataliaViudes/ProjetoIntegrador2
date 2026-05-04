package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Cardapio;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class CardapioControl {

    private boolean campoVazio(Cardapio c) {
        return c.getDescricao() == null || c.getDescricao().isEmpty()
                || c.getHora() == null || c.getHora().isEmpty()
                || c.getAgendamento() == null || c.getAgendamento().getId() == null;
    }

    public ResponseEntity<?> incluir(Cardapio cardapio) {
        if (!campoVazio(cardapio)) {
            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar())
                    throw new Exception("Erro ao conectar");

                Cardapio resultado = cardapio.incluir(db);
                return ResponseEntity.ok(resultado);

            } catch (Exception e) {
                return ResponseEntity.badRequest().body(new Erro("Erro ao inserir cardápio"));
            } finally {
                db.desconectar();
            }
        } else {
            return ResponseEntity.badRequest().body(new Erro("Campos obrigatórios não preenchidos"));
        }
    }

    public ResponseEntity<?> getById(int id) {
        if (id <= 0)
            return ResponseEntity.badRequest().body(new Erro("Id inválido"));

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar())
                throw new Exception();

            Cardapio c = new Cardapio().buscarPorId(id, db);

            if (c != null)
                return ResponseEntity.ok(c);
            else
                return ResponseEntity.badRequest().body(new Erro("Não encontrado"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro"));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> update(Cardapio cardapio) {

        if (cardapio.getId() == null || cardapio.getId() <= 0)
            return ResponseEntity.badRequest().body(new Erro("Id inválido"));

        if (campoVazio(cardapio))
            return ResponseEntity.badRequest().body(new Erro("Campos obrigatórios"));

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar())
                throw new Exception();

            return ResponseEntity.ok(cardapio.alterar(db));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro"));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> delete(Integer id) {
        if (id == null || id <= 0)
            return ResponseEntity.badRequest().body(new Erro("Id inválido"));

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar())
                throw new Exception();

            Cardapio c = new Cardapio(id);

            if (c.apagar(db))
                return ResponseEntity.ok(true);
            else
                return ResponseEntity.badRequest().body(new Erro("Erro ao deletar"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro"));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> getAll() {
        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar())
                throw new Exception();

            List<Cardapio> lista = new Cardapio().buscarTodos(db);

            if (lista.isEmpty())
                return ResponseEntity.noContent().build();

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro"));
        } finally {
            db.desconectar();
        }
    }
}