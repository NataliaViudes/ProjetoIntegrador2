package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.Remedio;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.List;

public class RemedioControl {

    public RemedioControl() {
    }

    // -------------------- INCLUIR --------------------
    public ResponseEntity<?> incluir(Remedio r)
    {
        if (r.getNome() != null && !r.getNome().isEmpty()) {

            if (r.getDescricao() != null && !r.getDescricao().isEmpty()) {

                Conexao db = Banco.getConexao();

                try {

                    if (!db.conectar()) {
                        throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                    }

                    Remedio resultado = r.incluir(db);

                    return ResponseEntity.ok(resultado);

                } catch (SQLException e) {

                    return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

                } catch (Exception e) {

                    return ResponseEntity.badRequest().body(new Erro("Erro geral"));

                } finally {

                    db.desconectar();
                }

            } else {
                return ResponseEntity.badRequest().body(new Erro("Descrição é obrigatória"));
            }

        } else {
            return ResponseEntity.badRequest().body(new Erro("Nome é obrigatório"));
        }
    }

    // -------------------- BUSCAR TODOS --------------------
    public ResponseEntity<?> getAll()
    {
        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            Remedio r = new Remedio();

            List<Remedio> lista = r.listar(db);

            return ResponseEntity.ok(lista);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.badRequest().body(new Erro("Erro ao buscar remédios"));

        } finally {

            db.desconectar();
        }
    }

    // -------------------- BUSCAR POR ID --------------------
    public ResponseEntity<?> getById(Integer id)
    {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body(new Erro("ID inválido"));
        }

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            Remedio r = new Remedio();

            Remedio resultado = r.buscarPorId(db, id);

            if (resultado != null) {
                return ResponseEntity.ok(resultado);
            } else {
                return ResponseEntity.badRequest().body(new Erro("Remédio não encontrado"));
            }

        } catch (SQLException e) {

            return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {

            db.desconectar();
        }
    }

    // -------------------- BUSCAR POR NOME --------------------
    public ResponseEntity<?> buscaPorNome(String nome)
    {
        List<Remedio> lista;

        Remedio r = new Remedio();

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            lista = r.buscarPorNome(db, nome != null ? nome : "");

            if (lista != null && !lista.isEmpty()) {
                return ResponseEntity.ok(lista);
            } else {
                return ResponseEntity.badRequest().body(new Erro("Nenhum remédio encontrado"));
            }

        } catch (SQLException e) {

            return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {

            db.desconectar();
        }
    }

    // -------------------- BUSCAR POR DESCRICAO --------------------
    public ResponseEntity<?> buscaPorDescricao(String descricao)
    {
        List<Remedio> lista;

        Remedio r = new Remedio();

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            lista = r.buscarPorDescricao(db, descricao != null ? descricao : "");

            if (lista != null && !lista.isEmpty()) {
                return ResponseEntity.ok(lista);
            } else {
                return ResponseEntity.badRequest().body(new Erro("Nenhum remédio encontrado"));
            }

        } catch (SQLException e) {

            return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {

            db.desconectar();
        }
    }

    // -------------------- UPDATE --------------------
    public ResponseEntity<?> update(Remedio r)
    {
        if (r.getId() == null || r.getId() <= 0) {
            return ResponseEntity.badRequest().body(new Erro("ID obrigatório"));
        }

        if (r.getNome() == null || r.getNome().isEmpty()) {
            return ResponseEntity.badRequest().body(new Erro("Nome obrigatório"));
        }

        if (r.getDescricao() == null || r.getDescricao().isEmpty()) {
            return ResponseEntity.badRequest().body(new Erro("Descrição obrigatória"));
        }

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            Remedio existente = r.buscarPorId(db, r.getId());

            if (existente != null) {

                Remedio atualizado = r.alterar(db);

                if (atualizado != null) {
                    return ResponseEntity.ok(atualizado);
                } else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao atualizar"));
                }

            } else {

                return ResponseEntity.badRequest().body(new Erro("Remédio não encontrado"));
            }

        } catch (SQLException e) {

            return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {

            db.desconectar();
        }
    }

    // -------------------- DELETE --------------------
    public ResponseEntity<?> delete(Integer id)
    {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body(new Erro("ID inválido"));
        }

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            Remedio r = new Remedio();

            Remedio existente = r.buscarPorId(db, id);

            if (existente != null) {

                r.setId(id);

                if (r.apagar(db)) {
                    return ResponseEntity.ok(true);
                } else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao excluir"));
                }

            } else {

                return ResponseEntity.badRequest().body(new Erro("Remédio não encontrado"));
            }

        } catch (SQLException e) {

            return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {

            db.desconectar();
        }
    }
}