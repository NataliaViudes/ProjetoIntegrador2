package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Alimento;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.List;

public class AlimentoControl {

    public ResponseEntity<?> incluir(Alimento alimento) {

        if (alimento.getNome() != null && !alimento.getNome().isEmpty()) {

            if (alimento.getTipo() != null && !alimento.getTipo().isEmpty()) {

                if (alimento.getDescricao() != null && !alimento.getDescricao().isEmpty()) {

                    Conexao db = Banco.getConexao();

                    try {
                        if (!db.conectar())
                            throw new Exception("Erro ao conectar");

                        Alimento resultado = alimento.incluir(db);
                        return ResponseEntity.ok(resultado);

                    } catch (Exception e) {
                        return ResponseEntity.badRequest().body(new Erro("Erro geral"));
                    } finally {
                        db.desconectar();
                    }

                } else return ResponseEntity.badRequest().body(new Erro("Descricao invalida"));

            } else return ResponseEntity.badRequest().body(new Erro("Tipo invalido"));

        } else return ResponseEntity.badRequest().body(new Erro("Nome invalido"));
    }

    public ResponseEntity<?> getById(int id) {

        if (id <= 0)
            return ResponseEntity.badRequest().body(new Erro("Id invalido"));

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar())
                throw new Exception("Erro");

            Alimento a = new Alimento().buscarPorId(db, id);

            if (a != null)
                return ResponseEntity.ok(a);
            else
                return ResponseEntity.badRequest().body(new Erro("Nao encontrado"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro"));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> buscaPorTipo(String tipo) {

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar())
                throw new Exception();

            List<Alimento> lista = new Alimento().buscarPorTipo(db, tipo);

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro"));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> buscaPorDescricao(String descricao) {

        if(descricao == null)
            descricao = "";
        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar())
                throw new Exception();

            List<Alimento> lista = new Alimento().buscarPorDescricao(db, descricao);

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro"));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> alterar(Alimento alimento) {

        if (alimento.getId() == null || alimento.getId() <= 0)
            return ResponseEntity.badRequest().body(new Erro("Id invalido"));

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar())
                throw new Exception();

            return ResponseEntity.ok(alimento.alterar(db));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro"));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> delete(Integer id) {

        if (id == null || id <= 0)
            return ResponseEntity.badRequest().body(new Erro("Id invalido"));

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar())
                throw new Exception();

            Alimento a = new Alimento(id);

            if (a.apagar(db))
                return ResponseEntity.ok(true);
            else
                return ResponseEntity.badRequest().body(new Erro("Erro ao deletar"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro"));
        } finally {
            db.desconectar();
        }
    }
}