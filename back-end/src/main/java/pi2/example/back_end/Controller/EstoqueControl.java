package pi2.example.back_end.Controller;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Estoque;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstoqueControl {
    public EstoqueControl() {
    }

    public ResponseEntity<?> incluir(Estoque estoque)
    {
        if(estoque.getTipo()!=null && estoque.getTipoId()>0)
        {
            if(estoque.getQtd() !=null &&  estoque.getQtd()>0)
            {
                if (estoque.getDescricao() != null && !estoque.getDescricao().isEmpty()) {

                    Estoque resultado=null;
                    Conexao db = Banco.getConexao(); //Abre a conexao
                    try {
                        if (!db.conectar()) {
                            throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                        }

                        resultado = estoque.incluir(db);
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
                    return ResponseEntity.badRequest().body(new Erro("Descricao invalida"));
                }
            }
            else
            {
                return ResponseEntity.badRequest().body(new Erro("Quantidade invalida!"));
            }
        }else
        {
            return ResponseEntity.badRequest().body(new Erro("Tipo invalido"));
        }
    }

    public ResponseEntity<?> getById(int id)
    {
        if (id < 0) {
            return ResponseEntity.badRequest().body(new Erro("Id invalido"));
        }
        else
        {
            Estoque est= new Estoque();
            Conexao db = Banco.getConexao(); // Abre conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                Estoque resultado = est.buscarPorId(db,id);
                if(resultado!=null) { // se encontrar algum estoque
                    return ResponseEntity.ok(resultado);
                }
                else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar estoque id: "+id));
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
        List<Estoque> estoques = new ArrayList<>();
        Estoque est= new Estoque();
        if(tipo==null) tipo="";
        Conexao db = Banco.getConexao(); // Abre conexao
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }
            estoques = est.buscarPorTipo(db,tipo);
            if(estoques!=null && !estoques.isEmpty()) { // se encontrar algum
                return ResponseEntity.ok(estoques);
            }
            else {
                return ResponseEntity.badRequest().body(new Erro("Erro ao buscar Estoque do tipo: "+ tipo));
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

    public ResponseEntity<?> buscaPorDescricao(String descricao)
    {
        List<Estoque> estoques;
        Estoque este= new Estoque();
        if(descricao==null) descricao="";

        Conexao db = Banco.getConexao(); // Abre conexao
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }
            estoques = este.buscarPorDescricao(db,descricao);
            if(estoques!=null && !estoques.isEmpty()) { // se encontrar
                return ResponseEntity.ok(estoques);
            }
            else {
                return ResponseEntity.badRequest().body(new Erro("Erro ao buscar estoque: "+descricao));
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

    public ResponseEntity<?> alterar(Estoque estoque)
    {
        if(estoque.getId()!=null && estoque.getId()>0)
        {
            if(estoque.getTipo()!=null && estoque.getTipoId()>0)
            {
                if(estoque.getQtd() !=null &&  estoque.getQtd()>0)
                {
                    if (estoque.getDescricao() != null && !estoque.getDescricao().isEmpty()) {

                        Estoque resultado=null;
                        Conexao db = Banco.getConexao(); //Abre a conexao
                        try {
                            if (!db.conectar()) {
                                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                            }
                            resultado = estoque.alterar(db);
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
                        return ResponseEntity.badRequest().body(new Erro("Descricao invalida"));
                    }
                }
                else
                {
                    return ResponseEntity.badRequest().body(new Erro("Quantidade invalida!"));
                }
            }else
            {
                return ResponseEntity.badRequest().body(new Erro("Tipo invalido"));
            }
        }
        else
        {
            return ResponseEntity.badRequest().body(new Erro("Id invalida!"));
        }
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
                Estoque est = new Estoque(id);
                Estoque existente = est.buscarPorId(db, est.getId()); // verificar se existe no banco
                if (existente != null) {
                    if(est.apagar(db))
                        return ResponseEntity.ok(true);
                    else
                        return ResponseEntity.badRequest().body(new Erro("Erro ao excluir Estoque"));
                }
                else
                    return ResponseEntity.badRequest().body(new Erro("Estoque não encontrado"));
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


}
