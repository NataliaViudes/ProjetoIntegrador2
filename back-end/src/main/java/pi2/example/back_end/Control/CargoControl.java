package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Cargo;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.Funcionario;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.List;

public class CargoControl {
    public CargoControl() {}

    public ResponseEntity<?> incluir(Cargo cargo)
    {
        if (cargo.getNome() != null && !cargo.getNome().isEmpty()) {
            Cargo resultado=null;
            Conexao db = Banco.getConexao(); //Abre a conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }
                resultado = cargo.incluir(db);
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
            return ResponseEntity.badRequest().body(new Erro("Nome(nome) é obrigatorio"));
        }
    }

    public ResponseEntity<?> getById(int id)
    {
        if (id < 0) {
            return ResponseEntity.badRequest().body(new Erro("Id(id_cargo) invalido"));
        }
        else
        {
            Cargo cargo= new Cargo();
            Conexao db = Banco.getConexao(); // Abre conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                Cargo resultado = cargo.buscarporId(id,db);
                if(resultado!=null) { // se encontrar algum Cat_evento
                    return ResponseEntity.ok(resultado);
                }
                else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar Cargo id: "+id));
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
        List<Cargo> cargos;
        Cargo cargo = new Cargo();

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            cargos = cargo.buscarPorNome(nome, db);

            if (cargos != null && !cargos.isEmpty()) {
                return ResponseEntity.ok(cargos);
            } else {
                return ResponseEntity.badRequest().body(new Erro("Nenhum cargo com esse nome:: "+nome));
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


    public ResponseEntity<?> update(Cargo cargo)
    {
        //id invalido
        if (cargo.getId() != null && cargo.getId()>0) {
            // nome obrigatória
            if (cargo.getNome() != null && !cargo.getNome().isEmpty()) {
                Conexao db = Banco.getConexao(); // Abre conexao
                try {
                    if (!db.conectar()) {
                        throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                    }
                    Cargo existente = cargo.buscarporId(cargo.getId(),db); // verificar se existe no banco
                    if (existente != null) {
                        Cargo car = cargo.alterar(db);
                        if(car!=null)
                            return ResponseEntity.ok(cargo);
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
            return ResponseEntity.badRequest().body(new Erro("Nome é obrigatória para alteração"));
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
                Cargo cargo = new Cargo(id);
                Cargo existente = cargo.buscarporId(cargo.getId(),db); // verificar se existe no banco
                if (existente != null) {
                    if(cargo.apagar(db))
                        return ResponseEntity.ok(true);
                    else
                        return ResponseEntity.badRequest().body(new Erro("Erro ao excluir Cargo"));
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
        else
            return ResponseEntity.badRequest().body(new Erro("id invalido"));
    }

    public ResponseEntity<?> getAllOrFilter(String filtro) {
        Conexao db = Banco.getConexao();
        Cargo cargo = new Cargo();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            List<Cargo> lista;

            if (filtro == null || filtro.isEmpty()) {
                lista = cargo.buscarTodos(db);
            } else {
                lista = cargo.buscarComFiltro(filtro, db);
            }

            if (lista == null || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar cargos: "+e.getMessage());
        } finally {
            db.desconectar();
        }
    }
}

