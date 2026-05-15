package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.Funcionario;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.List;

public class FuncionarioControl {
    public FuncionarioControl() {}

    private boolean campoVazio(Funcionario f){
        if(f.getNome()!=null && !f.getNome().isEmpty()
                && f.getCpf()!=null && !f.getCpf().isEmpty()
                && f.getTelefone()!=null && !f.getTelefone().isEmpty()
                && f.getSexo()!=null && !f.getSexo().isEmpty()
                && f.getEndereco()!=null && !f.getEndereco().isEmpty()
                && f.getCargo()!=null && f.getCargo().getId()!=null && f.getNascimento()!=null)
            return false;
        return true;
    }

    public ResponseEntity<?> incluir(Funcionario funcionario)
    {
        if (!campoVazio(funcionario)) {
            Funcionario resultado=null;
            Conexao db = Banco.getConexao(); //Abre a conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }
                resultado = funcionario.incluir(db);
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
            return ResponseEntity.badRequest().body(new Erro("Todos os campos são obrigatórios!"));
        }
    }

    public ResponseEntity<?> getById(int id)
    {
        if (id < 0) {
            return ResponseEntity.badRequest().body(new Erro("Id(id_funcionario) invalido"));
        }
        else
        {
            Funcionario funcionario= new Funcionario();
            Conexao db = Banco.getConexao(); // Abre conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                Funcionario resultado = funcionario.buscarporId(id,db);
                if(resultado!=null) { // se encontrar algum Cat_evento
                    return ResponseEntity.ok(resultado);
                }
                else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar Funcionario id: "+id));
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
        List<Funcionario> funcionarios;
        Funcionario funcionario= new Funcionario();

        if(nome != null && !nome.isEmpty())
        {
            Conexao db = Banco.getConexao(); // Abre conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }
                funcionarios = funcionario.buscarPorNome(nome,db);
                if(funcionarios!=null && !funcionarios.isEmpty()) { // se encontrar algum Cat_evento
                    return ResponseEntity.ok(funcionarios);
                }
                else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar Funcionario: "+nome));
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

        Conexao db = Banco.getConexao(); // Abre conexao
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }
            funcionarios = funcionario.buscarPorNome("",db);
            if(funcionarios!=null && !funcionarios.isEmpty())
            {
                return ResponseEntity.ok(funcionarios);
            }
            else
            {
                return ResponseEntity.badRequest().body(new Erro("Nenhum funcionario com esse nome:: ")+nome);
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


    public ResponseEntity<?> update(Funcionario funcionario)
    {
        Integer id = funcionario.getId();
        //id invalido
        if (id != null && id > 0) {
            // nome obrigatória
            if (!campoVazio(funcionario)) {
                Conexao db = Banco.getConexao(); // Abre conexao
                try {
                    if (!db.conectar()) {
                        throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                    }
                    Funcionario existente = funcionario.buscarporId(funcionario.getId(),db); // verificar se existe no banco
                    if (existente != null) {
                        Funcionario f = funcionario.alterar(db);
                        if(f!=null)
                            return ResponseEntity.ok(funcionario);
                        else
                            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar Cargo"));
                    }
                    else
                        return ResponseEntity.badRequest().body(new Erro("Cargo não encontrado"));
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
                Funcionario funcionario = new Funcionario(id);
                Funcionario existente = funcionario.buscarporId(funcionario.getId(),db); // verificar se existe no banco
                if (existente != null) {
                    if(funcionario.apagar(db))
                        return ResponseEntity.ok(true);
                    else
                        return ResponseEntity.badRequest().body(new Erro("Erro ao excluir funionario"));
                }
                else
                    return ResponseEntity.badRequest().body(new Erro("Funcionario não encontrado"));
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
        Funcionario funcionario = new Funcionario();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            List<Funcionario> lista;

            if (filtro == null || filtro.isEmpty()) {
                lista = funcionario.buscarTodos(db);
            } else {
                lista = funcionario.buscarComFiltro(filtro, db);
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
