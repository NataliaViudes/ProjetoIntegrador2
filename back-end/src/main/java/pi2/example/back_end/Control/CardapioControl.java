package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Cardapio;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.util.List;


public class CardapioControl {

    private boolean invalido(Cardapio c) {
        return c.getDescricao() == null || c.getDescricao().isEmpty()
                || c.getHora() == null || c.getHora().isEmpty()
                || c.getAgendamento() == null
                || c.getAgendamento().getId() == null
                || c.getAgendamento().getId() <= 0;
    }

    public ResponseEntity<?> incluir(Cardapio cardapio) {

        if (invalido(cardapio)) {
            return ResponseEntity.badRequest().body(new Erro("Campos inválidos"));
        }

        Conexao db = Banco.getConexao();

        try {
            db.conectar();
            return ResponseEntity.ok(cardapio.incluir(db));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao inserir"));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> update(Cardapio cardapio) {

        if (cardapio.getId() == null || cardapio.getId() <= 0) {
            return ResponseEntity.badRequest().body(new Erro("ID inválido"));
        }

        if (invalido(cardapio)) {
            return ResponseEntity.badRequest().body(new Erro("Campos inválidos"));
        }

        Conexao db = Banco.getConexao();

        try {
            db.conectar();
            return ResponseEntity.ok(cardapio.alterar(db));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao atualizar"));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> getAll() {

        Conexao db = Banco.getConexao();

        try {
            db.conectar();

            List<Cardapio> lista = new Cardapio().buscarTodos(db);

            if (lista.isEmpty())
                return ResponseEntity.noContent().build();

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao buscar"));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> getById(int id) {

        if (id <= 0)
            return ResponseEntity.badRequest().body(new Erro("ID inválido"));

        Conexao db = Banco.getConexao();

        try {
            db.conectar();

            Cardapio c = new Cardapio().buscarPorId(id, db);

            if (c == null)
                return ResponseEntity.badRequest().body(new Erro("Não encontrado"));

            return ResponseEntity.ok(c);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro"));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> delete(Integer id) {

        if (id == null || id <= 0)
            return ResponseEntity.badRequest().body(new Erro("ID inválido"));

        Conexao db = Banco.getConexao();

        try {
            db.conectar();

            Cardapio c = new Cardapio(id);

            if (c.apagar(db))
                return ResponseEntity.ok(true);

            return ResponseEntity.badRequest().body(new Erro("Erro ao deletar"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro"));
        } finally {
            db.desconectar();
        }
    }
}