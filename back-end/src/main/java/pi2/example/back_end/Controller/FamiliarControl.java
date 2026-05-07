package pi2.example.back_end.Controller;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.Familiar;
import pi2.example.back_end.Modelo.Funcionario;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.List;

public class FamiliarControl {
    public FamiliarControl() {
    }

    private boolean campoVazio(Familiar f){
        if(f.getNome()!=null && !f.getNome().isEmpty()
                && f.getParentesco()!=null && !f.getParentesco().isEmpty()
                && f.getTelefone()!=null && !f.getTelefone().isEmpty()
                && f.getProfissao()!=null && !f.getProfissao().isEmpty()
                && f.getRenda()>=0)
            return false;
        return true;
    }

    public ResponseEntity<?> incluir(Familiar familiar)
    {
        if (familiar.getNome() != null && !familiar.getNome().isEmpty()) {
            Familiar resultado=null;
            Conexao db = Banco.getConexao(); //Abre a conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }
                resultado = familiar.incluir(db);
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
            return ResponseEntity.badRequest().body(new Erro("Todos os campos são obrigatorios"));
        }
    }

    public ResponseEntity<?> update(Familiar familiar)
    {
        //id invalido
        if (familiar.getId() != null && familiar.getId()>0) {
            // nome obrigatória
            if (!campoVazio(familiar)) {
                Conexao db = Banco.getConexao(); // Abre conexao
                try {
                    if (!db.conectar()) {
                        throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                    }
                    Familiar existente = familiar.buscarporId(familiar.getId(),db); // verificar se existe no banco
                    if (existente != null) {
                        Familiar f = familiar.alterar(db);
                        if(f!=null)
                            return ResponseEntity.ok(familiar);
                        else
                            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar Familiar"));
                    }
                    else
                        return ResponseEntity.badRequest().body(new Erro("Familiar não encontrado"));
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
            return ResponseEntity.badRequest().body(new Erro("Todos os campos são obrigatórios para alteração"));
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
                Familiar familiar = new Familiar(id);
                Familiar existente = familiar.buscarporId(familiar.getId(),db); // verificar se existe no banco
                if (existente != null) {
                    if(familiar.apagar(db))
                        return ResponseEntity.ok(true);
                    else
                        return ResponseEntity.badRequest().body(new Erro("Erro ao excluir Familiar"));
                }
                else
                    return ResponseEntity.badRequest().body(new Erro("Familiar não encontrado"));
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


    public ResponseEntity<?> getById(int id)
    {
        if (id < 0) {
            return ResponseEntity.badRequest().body(new Erro("Id(id_familiar) invalido"));
        }
        else
        {
            Familiar familiar= new Familiar();
            Conexao db = Banco.getConexao(); // Abre conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                Familiar resultado = familiar.buscarporId(id,db);
                if(resultado!=null) { // se encontrar algum Cat_evento
                    return ResponseEntity.ok(resultado);
                }
                else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar Familiar id: "+id));
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

    public ResponseEntity<?> buscaPorNome(String nome)
    {
        List<Familiar> familiares;
        Familiar familiar = new Familiar();

        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            familiares = familiar.buscarPorNome(nome, db);
            if (familiares != null && !familiares.isEmpty()) {
                return ResponseEntity.ok(familiares);
            } else {
                return ResponseEntity.badRequest().body(new Erro("Erro ao buscar Familiar: "+nome));
            }
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

    public ResponseEntity<?> getAllOrFilter(String filtro) {
        Conexao db = Banco.getConexao();
        Familiar familiar = new Familiar();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            List<Familiar> lista;

            if (filtro == null || filtro.isEmpty()) {
                lista = familiar.buscarTodos(db);
            } else {
                lista = familiar.buscarComFiltro(filtro, db);
            }

            if (lista == null || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar funcionários: "+e.getMessage());
        } finally {
            db.desconectar();
        }
    }
}
