package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.DAO.DAOOrdemJudicial;
import pi2.example.back_end.Modelo.OrdemJudicial;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

public class OrdemJudicialControl {

    public ResponseEntity<?> salvar(OrdemJudicial o) {

        if (o.getBeneficiarioId() == null)
            return ResponseEntity.badRequest().body("Beneficiário obrigatório");

        Conexao db = Banco.getConexao();

        try {
            db.conectar();

            DAOOrdemJudicial dao = new DAOOrdemJudicial(db);

            OrdemJudicial existente = dao.getByBeneficiario(o.getBeneficiarioId());

            if (existente == null) {
                return ResponseEntity.ok(dao.gravar(o));
            } else {
                o.setBeneficiarioId(o.getBeneficiarioId());
                return ResponseEntity.ok(dao.alterar(o));
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro");
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> getByBeneficiario(Integer id) {

        Conexao db = Banco.getConexao();

        try {
            db.conectar();

            DAOOrdemJudicial dao = new DAOOrdemJudicial(db);

            return ResponseEntity.ok(dao.getByBeneficiario(id));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro");
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> delete(Integer id) {

        Conexao db = Banco.getConexao();

        try {
            db.conectar();

            DAOOrdemJudicial dao = new DAOOrdemJudicial(db);

            return ResponseEntity.ok(dao.apagarPorBeneficiario(id));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro");
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> listarTodos() {

        Conexao db = Banco.getConexao();

        try {
            db.conectar();

            DAOOrdemJudicial dao = new DAOOrdemJudicial(db);

            return ResponseEntity.ok(dao.getAll());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro");
        } finally {
            db.desconectar();
        }
    }
}