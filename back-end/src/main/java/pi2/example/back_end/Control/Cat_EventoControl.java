package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Cat_Evento;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.List;

public class Cat_EventoControl {
    public Cat_EventoControl() {
    }

    public ResponseEntity<?> incluir(Cat_Evento cat_evento)
    {
        if (cat_evento.getCategoria() != null && !cat_evento.getCategoria().isEmpty()) {

            if (cat_evento.getDescricao() != null && !cat_evento.getDescricao().isEmpty()) {

                Cat_Evento resultado=null;
                Conexao db = Banco.getConexao(); //Abre a conexao
                try {
                    if (!db.conectar()) {
                        throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                    }

                    resultado = cat_evento.incluir(db);
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
                return ResponseEntity.badRequest().body(new Erro("Descricao(cat_descricao) é obrigatoria"));
            }
        }
        else
        {
            return ResponseEntity.badRequest().body(new Erro("Nome da categoria(cat_nome) é obrigatoria"));
        }
    }

    public ResponseEntity<?> getById(int id)
    {
        if (id < 0) {
            return ResponseEntity.badRequest().body(new Erro("Id(cat_evento_id) invalido"));
        }
        else
        {
            Cat_Evento eve= new Cat_Evento();
            Conexao db = Banco.getConexao(); // Abre conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                Cat_Evento resultado = eve.buscarporId(id,db);
                if(resultado!=null) { // se encontrar algum Cat_evento
                    return ResponseEntity.ok(resultado);
                }
                else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar Evento id: "+id));
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

    public ResponseEntity<?> buscaPorCategoria(String categoria)
    {
        List<Cat_Evento> eventos;
        Cat_Evento eve= new Cat_Evento();

        if(categoria != null && !categoria.isEmpty())
        {
            Conexao db = Banco.getConexao(); // Abre conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }
                eventos = eve.buscarPorCategoria(categoria,db);
                if(eventos!=null && !eventos.isEmpty()) { // se encontrar algum Cat_evento
                    return ResponseEntity.ok(eventos);
                }
                else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar Evento: "+categoria));
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
                eventos = eve.buscarPorCategoria("",db);
                if(eventos!=null && !eventos.isEmpty())
                {
                    return ResponseEntity.ok(eventos);
                }
                else
                {
                    return ResponseEntity.badRequest().body(new Erro("Nenhum evento nessa categoria: ")+categoria);
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
        List<Cat_Evento> eventos;
        Cat_Evento eve= new Cat_Evento();

        if(descricao != null && !descricao.isEmpty())
        {
            Conexao db = Banco.getConexao(); // Abre conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }
                eventos = eve.buscarPorDescricao(descricao,db);
                if(eventos!=null && !eventos.isEmpty()) { // se encontrar algum Cat_evento
                    return ResponseEntity.ok(eventos);
                }
                else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar Evento: "+descricao));
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
            eventos = eve.buscarPorDescricao("",db);
            if(eventos!=null && !eventos.isEmpty())
            {
                return ResponseEntity.ok(eventos);
            }
            else
            {
                return ResponseEntity.badRequest().body(new Erro("Nenhum evento com essa descricao:: ")+descricao);
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


    public ResponseEntity<?> update(Cat_Evento cat_evento)
    {
        //id invalido
        if (cat_evento.getId() != null && cat_evento.getId()>0) {
            // categoria obrigatória
            if (cat_evento.getCategoria() != null && !cat_evento.getCategoria().isEmpty()) {

                if(cat_evento.getDescricao() != null && !cat_evento.getDescricao().isEmpty())
                {
                    Conexao db = Banco.getConexao(); // Abre conexao
                    try {
                        if (!db.conectar()) {
                            throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                        }
                        Cat_Evento existente = cat_evento.buscarporId(cat_evento.getId(),db); // verificar se existe no banco
                        if (existente != null) {
                            Cat_Evento eve = cat_evento.alterar(db);
                            if(eve!=null)
                                return ResponseEntity.ok(cat_evento);
                            else
                                return ResponseEntity.badRequest().body(new Erro("Erro ao alterar Cat_Evento"));
                        }
                        else
                            return ResponseEntity.badRequest().body(new Erro("Cat_Evento não encontrado"));
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
                return ResponseEntity.badRequest().body(new Erro("Descricao é obrigatória para alteração"));
            }
            else
                return ResponseEntity.badRequest().body(new Erro("Categoria é obrigatória para alteração"));
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
                Cat_Evento eve = new Cat_Evento(id);
                Cat_Evento existente = eve.buscarporId(eve.getId(),db); // verificar se existe no banco
                if (existente != null) {
                    if(eve.apagar(db))
                        return ResponseEntity.ok(true);
                    else
                        return ResponseEntity.badRequest().body(new Erro("Erro ao excluir Cat_Evento"));
                }
                else
                    return ResponseEntity.badRequest().body(new Erro("Cat_Evento não encontrado"));
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


