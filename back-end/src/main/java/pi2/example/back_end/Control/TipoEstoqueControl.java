package pi2.example.back_end.Controler;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.TipoEstoque;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.List;

public class TipoEstoqueControl {

    public TipoEstoqueControl() {
    }

    public ResponseEntity<?> incluir(TipoEstoque tipoEstoque)
    {
        if (tipoEstoque.getTipo() != null && !tipoEstoque.getTipo().isEmpty()) {

            TipoEstoque resultado=null;
            Conexao db = Banco.getConexao(); //Abre a conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                resultado = tipoEstoque.incluir(db);
                return ResponseEntity.ok(resultado);

            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro ao conectar com o banco de dados"));

            } catch (Exception e) {
                System.out.println("Erro geral: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

            } finally {
                db.desconectar(); // não esquece de fechar a conexao com o banco!!
            }
        }
        else
        {
            return ResponseEntity.badRequest().body(new Erro("Tipo é obrigatorio"));
        }
    }

    public ResponseEntity<?> getById(int id)
    {
        if (id < 0) {
            return ResponseEntity.badRequest().body(new Erro("Id invalido"));
        }
        else
        {
            TipoEstoque eve= new TipoEstoque(id);
            Conexao db = Banco.getConexao(); // Abre conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                TipoEstoque resultado = eve.buscarPorId(db,id);
                if(resultado!=null) { // se encontrar algum
                    return ResponseEntity.ok(resultado);
                }
                else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar TipoEstoque id: "+id));
                }
            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro ao conectar com o banco de dados"));

            } catch (Exception e) {
                System.out.println("Erro geral: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

            } finally {
                db.desconectar(); // não esquece de fechar a conexao com o banco!!
            }

        }
    }

    public ResponseEntity<?> buscaPorTipo(String tipo)
    {
        List<TipoEstoque> resultado;
        TipoEstoque eve= new TipoEstoque();

        if(tipo != null && !tipo.isEmpty())
        {
            Conexao db = Banco.getConexao(); // Abre conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }
                resultado = eve.buscarPorTipo(db,tipo);
                if(resultado!=null && !resultado.isEmpty()) { // se encontrar
                    return ResponseEntity.ok(resultado);
                }
                else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar TipoEstoque: "+ tipo));
                }
            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro com o banco"));

            } catch (Exception e) {
                System.out.println("Erro geral: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

            } finally {
                db.desconectar(); // não esquece de fechar a conexao com o banco!!
            }

        }
        else
        {
            Conexao db = Banco.getConexao(); // Abre conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }
                resultado = eve.buscarPorTipo(db,"");
                if(resultado!=null && !resultado.isEmpty())
                {
                    return ResponseEntity.ok(resultado);
                }
                else
                {
                    return ResponseEntity.badRequest().body(new Erro("Nenhum evento nessa categoria: ")+ tipo);
                }
            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro com o banco"));

            } catch (Exception e) {
                System.out.println("Erro geral: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

            } finally {
                db.desconectar(); // não esquece de fechar a conexao com o banco!!
            }
        }
    }



    public ResponseEntity<?> update(TipoEstoque tipoEstoque)
    {
        //id invalido
        if (tipoEstoque.getId() != null && tipoEstoque.getId()>0) {
            // categoria obrigatória
            if (tipoEstoque.getTipo() != null && !tipoEstoque.getTipo().isEmpty()) {
                Conexao db = Banco.getConexao(); // Abre conexao
                try {
                    if (!db.conectar()) {
                        throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                    }
                    TipoEstoque existente = tipoEstoque.buscarPorId(db,tipoEstoque.getId()); // verificar se existe no banco
                    if (existente != null) {
                        TipoEstoque eve = tipoEstoque.alterar(db);
                        if(eve!=null)
                            return ResponseEntity.ok(tipoEstoque);
                        else
                            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar TipoEstoque"));
                    }
                    else
                        return ResponseEntity.badRequest().body(new Erro("TipoEstoque não encontrado"));
                } catch (SQLException e) {
                    System.out.println("Erro SQL: " + e.getMessage());
                    return ResponseEntity.badRequest().body(new Erro("Erro com o banco"));

                } catch (Exception e) {
                    System.out.println("Erro geral: " + e.getMessage());
                    return ResponseEntity.badRequest().body(new Erro("Erro geral"));

                } finally {
                    db.desconectar(); // não esquece de fechar a conexao com o banco!!
                }

            }
            else
                return ResponseEntity.badRequest().body(new Erro("Tipo é obrigatória para alteração"));
        }
        else
            return ResponseEntity.badRequest().body(new Erro("ID é obrigatório para alteração"));
    }


    public ResponseEntity<?> delete(Integer id)
    {
        if(id!=null && id>0)
        {
            Conexao db = Banco.getConexao(); // Abre conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }
                TipoEstoque eve = new TipoEstoque(id);
                TipoEstoque existente = eve.buscarPorId(db,id); // verificar se existe no banco
                if (existente != null) {
                    if(eve.apagar(db))
                        return ResponseEntity.ok(true);
                    else
                        return ResponseEntity.badRequest().body(new Erro("Erro ao excluir TipoEstoqueAA"));
                }
                else
                    return ResponseEntity.badRequest().body(new Erro("TipoEstoque não encontrado"));
            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro com o banco"));

            } catch (Exception e) {
                System.out.println("Erro geral: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

            } finally {
                db.desconectar(); // não esquece de fechar a conexao com o banco!!
            }
        }
        else
            return ResponseEntity.badRequest().body(new Erro("id invalido"));
    }

    public ResponseEntity<?> getAllOrFilter(String filtro) {
        Conexao db = Banco.getConexao();
        TipoEstoque tipoEstoque = new TipoEstoque();
        List<TipoEstoque> resultado;

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            if (filtro == null || filtro.isEmpty()) {
                resultado = tipoEstoque.buscarPorTipo(db, "");
            } else {
                resultado = tipoEstoque.buscarPorTipo(db, filtro);
            }

            return ResponseEntity.ok(resultado);
        } catch (SQLException e) {
            System.out.println("Erro SQL: " + e.getMessage());
            return ResponseEntity.badRequest().body(new Erro("Erro com o banco"));

        } catch (Exception e) {
            System.out.println("Erro geral: " + e.getMessage());
            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {
            db.desconectar();
        }
    }
}

