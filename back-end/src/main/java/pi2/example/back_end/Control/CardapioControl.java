package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Cardapio;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class CardapioControl {

    private boolean invalido(Cardapio c) {
        return c.getNome() == null || c.getNome().isEmpty()
                || c.getHora() == null || c.getHora().isEmpty()
                || c.getData() == null || c.getData().isEmpty()
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
            Cardapio inserido = cardapio.incluir(db);
            return ResponseEntity.ok(inserido);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Erro("Erro ao inserir: " + e.getMessage()));
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
            Cardapio atualizado = cardapio.alterar(db);
            return ResponseEntity.ok(atualizado);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Erro("Erro ao atualizar: " + e.getMessage()));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> getAll() {

        Conexao db = Banco.getConexao();
        try {
            db.conectar();
            List<Cardapio> lista = new Cardapio().buscarTodos(db);
            if (lista.isEmpty()) return ResponseEntity.noContent().build();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Erro("Erro ao buscar: " + e.getMessage()));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> getById(int id) {

        if (id <= 0) return ResponseEntity.badRequest().body(new Erro("ID inválido"));

        Conexao db = Banco.getConexao();
        try {
            db.conectar();
            Cardapio c = new Cardapio().buscarPorId(id, db);
            if (c == null) return ResponseEntity.status(404).body(new Erro("Não encontrado"));
            return ResponseEntity.ok(c);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Erro("Erro ao buscar: " + e.getMessage()));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> delete(Integer id) {

        if (id == null || id <= 0) return ResponseEntity.badRequest().body(new Erro("ID inválido"));

        Conexao db = Banco.getConexao();
        try {
            db.conectar();
            Cardapio c = new Cardapio(id);
            boolean apagou = c.apagar(db);
            if (apagou) return ResponseEntity.ok().build();
            return ResponseEntity.status(404).body(new Erro("Não encontrado"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Erro("Erro ao deletar: " + e.getMessage()));
        } finally {
            db.desconectar();
        }
    }
}