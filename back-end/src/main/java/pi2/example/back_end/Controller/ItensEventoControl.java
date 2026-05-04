package pi2.example.back_end.Controller;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.DAO.DAOItensEvento;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.Estoque;
import pi2.example.back_end.Modelo.Evento;
import pi2.example.back_end.Modelo.ItensEvento;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItensEventoControl {

    public ItensEventoControl() {}

    // 🔹 INSERT
    public ResponseEntity<?> incluir(ItensEvento item) {

        if (item == null)
            return ResponseEntity.badRequest().body(new Erro("Item nulo"));

        if (item.getEventoId() == null || item.getEventoId() <= 0)
            return ResponseEntity.badRequest().body(new Erro("Evento inválido"));

        if (item.getEstoqueId() == null || item.getEstoqueId() <= 0)
            return ResponseEntity.badRequest().body(new Erro("Estoque inválido"));

        if (item.getQtd() <= 0)
            return ResponseEntity.badRequest().body(new Erro("Quantidade inválida"));

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar())
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());

            DAOItensEvento dao = new DAOItensEvento(db);
            ItensEvento resultado = dao.gravar(item);

            return ResponseEntity.ok(resultado);

        } catch (SQLException e) {
            System.out.println("Erro SQL: " + e.getMessage());
            return ResponseEntity.badRequest().body(new Erro("Erro no banco"));

        } catch (Exception e) {
            System.out.println("Erro geral: " + e.getMessage());
            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {
            db.desconectar();
        }
    }

    // 🔹 UPDATE
    public ResponseEntity<?> alterar(List<ItensEvento> itens) {

        if (itens == null || itens.isEmpty())
            return ResponseEntity.badRequest().body(new Erro("Lista vazia"));

        Integer eventoId = itens.get(0).getEventoId();

        if (eventoId == null || eventoId <= 0)
            return ResponseEntity.badRequest().body(new Erro("Evento inválido"));

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar())
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());

            DAOItensEvento dao = new DAOItensEvento(db);

            dao.syncItensEvento(eventoId, itens);

            return ResponseEntity.ok("Itens atualizados com sucesso");

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return ResponseEntity.badRequest().body(new Erro("Erro ao atualizar itens"));

        } finally {
            db.desconectar();
        }
    }

    // 🔹 DELETE (chave composta)
    public ResponseEntity<?> delete(Integer idEvento, Integer idEstoque) {

        if (idEvento == null || idEvento <= 0)
            return ResponseEntity.badRequest().body(new Erro("Evento inválido"));

        if (idEstoque == null || idEstoque <= 0)
            return ResponseEntity.badRequest().body(new Erro("Estoque inválido"));

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar())
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());

            DAOItensEvento dao = new DAOItensEvento(db);

            ItensEvento item = new ItensEvento();
            Evento e = new Evento();
            e.setId(idEvento);

            Estoque est = new Estoque();
            est.setId(idEstoque);

            item.setEvento(e);
            item.setEstoque(est);

            boolean ok = dao.apagar(item);

            if (ok)
                return ResponseEntity.ok(true);
            else
                return ResponseEntity.badRequest().body(new Erro("Não foi possível excluir"));

        } catch (SQLException e) {
            System.out.println("Erro SQL: " + e.getMessage());
            return ResponseEntity.badRequest().body(new Erro("Erro no banco"));

        } catch (Exception e) {
            System.out.println("Erro geral: " + e.getMessage());
            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {
            db.desconectar();
        }
    }

    // 🔹 GET POR EVENTO (principal)
    public ResponseEntity<?> getPorEvento(Integer idEvento) {

        if (idEvento == null || idEvento <= 0)
            return ResponseEntity.badRequest().body(new Erro("Evento inválido"));

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar())
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());

            ItensEvento it = new ItensEvento();
            List<ItensEvento> lista = new ArrayList<ItensEvento>();
            lista = it.buscarItensDoEvento(db,idEvento);
            if (lista != null && !lista.isEmpty())
                return ResponseEntity.ok(lista);
            else
                return ResponseEntity.status(404).body(new Erro("Nenhum item encontrado"));

        } catch (SQLException e) {
            System.out.println("Erro SQL: " + e.getMessage());
            return ResponseEntity.badRequest().body(new Erro("Erro no banco"));

        } catch (Exception e) {
            System.out.println("Erro geral: " + e.getMessage());
            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {
            db.desconectar();
        }
    }
}
