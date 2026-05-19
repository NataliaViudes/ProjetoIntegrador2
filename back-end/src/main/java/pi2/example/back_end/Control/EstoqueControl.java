package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.DAO.DAOEstoque;
import pi2.example.back_end.Modelo.Estoque;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.MaterialAtividade;
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
        List<Estoque> eventos = new ArrayList<>();
        Estoque est= new Estoque();
        if(tipo==null) tipo="";
        Conexao db = Banco.getConexao(); // Abre conexao
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }
            eventos = est.buscarPorTipo(db,tipo);
            if(eventos!=null && !eventos.isEmpty()) { // se encontrar algum
                return ResponseEntity.ok(eventos);
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
        List<Estoque> eventos;
        Estoque este= new Estoque();
        if(descricao==null) descricao="";

        Conexao db = Banco.getConexao(); // Abre conexao
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }
            eventos = este.buscarPorDescricao(db,descricao);
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

    public ResponseEntity<?> getAllOrFilter(String filtro) {

        List<Estoque> resultado;
        Estoque estoque = new Estoque();

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            if (filtro != null && !filtro.isEmpty()) {
                resultado = estoque.buscarPorDescricao(db, filtro);
            } else {
                resultado = estoque.buscarPorDescricao(db, "");
            }

            if (resultado != null && !resultado.isEmpty()) {
                return ResponseEntity.ok(resultado);
            } else {
                return ResponseEntity.noContent().build(); // melhor que 400 aqui
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

    public ResponseEntity<?> salvarMateriaisEtapa(List<MaterialAtividade> materiais) {
        System.out.println("CHEGOU NO BACK: " + materiais.size());
        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar");
            }

            DAOEstoque dao = new DAOEstoque(db);

            for (MaterialAtividade mat : materiais) {

                Integer qtdAtual = dao.getQuantidadeAtual(mat.getIdAgendamento(), mat.getIdItem());

                if (qtdAtual == null) {
                    // NOVO MATERIAL
                    boolean ok = dao.baixarEstoque(mat.getIdItem(), mat.getQuantidade());

                    if (!ok) {
                        return ResponseEntity
                                .badRequest()
                                .body(new Erro("Estoque insuficiente"));
                    }

                } else {
                    int diferenca = mat.getQuantidade() - qtdAtual;

                    if (diferenca > 0) {
                        boolean ok = dao.baixarEstoque(mat.getIdItem(), diferenca);

                        if (!ok) {
                            return ResponseEntity
                                    .badRequest()
                                    .body(new Erro("Estoque insuficiente"));
                        }

                    } else if (diferenca < 0) {
                        dao.devolverEstoque(mat.getIdItem(), Math.abs(diferenca));
                    }
                }

                dao.salvarAgendamentoMaterial(
                        mat.getIdAgendamento(),
                        mat.getIdItem(),
                        mat.getQuantidade()
                );
            }
            return ResponseEntity.ok("Materiais vinculados à etapa com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro(e.getMessage()));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> buscarMateriaisPorAgendamento(int idAgendamento) {
        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar");
            }

            DAOEstoque dao = new DAOEstoque(db);
            List<MaterialAtividade> lista = dao.buscarPorAgendamento(idAgendamento);

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro(e.getMessage()));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> removerMaterial(MaterialAtividade mat) {
        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar");
            }

            DAOEstoque dao = new DAOEstoque(db);

            boolean ok = dao.deletarMaterial(
                    mat.getIdAgendamento(),
                    mat.getIdItem()
            );

            if (!ok) {
                return ResponseEntity.badRequest()
                        .body(new Erro("Erro ao deletar material"));
            }

            return ResponseEntity.ok(true);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro(e.getMessage()));
        } finally {
            db.desconectar();
        }
    }
}