package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Beneficiario;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.List;

public class BeneficiarioControl {
    public BeneficiarioControl() {}

    private boolean campoVazio(Beneficiario b) {
        return b.getNome() == null || b.getNome().isEmpty()
                || b.getCpf() == null || b.getCpf().isEmpty()
                || b.getNis() == null || b.getNis().isEmpty();
    }

    public ResponseEntity<?> incluir(Beneficiario beneficiario) {
        if (!campoVazio(beneficiario)) {
            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                Beneficiario resultado = beneficiario.incluir(db);
                return ResponseEntity.ok(resultado);

            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro ao conectar com o banco de dados"));

            } catch (Exception e) {
                System.out.println("Erro geral: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

            } finally {
                db.desconectar();
            }
        } else {
            return ResponseEntity.badRequest().body(new Erro("Nome, CPF e NIS são obrigatórios!"));
        }
    }

    public ResponseEntity<?> getById(int id) {
        if (id < 0) {
            return ResponseEntity.badRequest().body(new Erro("Id do beneficiário inválido"));
        } else {
            Beneficiario beneficiario = new Beneficiario();
            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                Beneficiario resultado = beneficiario.buscarPorId(id, db);
                if (resultado != null) {
                    return ResponseEntity.ok(resultado);
                } else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar beneficiário id: " + id));
                }

            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro ao conectar com o banco de dados"));

            } catch (Exception e) {
                System.out.println("Erro geral: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

            } finally {
                db.desconectar();
            }
        }
    }

    public ResponseEntity<?> getByCpf(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return ResponseEntity.badRequest().body(new Erro("CPF é obrigatório"));
        }

        Beneficiario beneficiario = new Beneficiario();
        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            Beneficiario resultado = beneficiario.buscarPorCpf(cpf, db);

            if (resultado != null) {
                return ResponseEntity.ok(resultado);
            } else {
                return ResponseEntity.badRequest().body(new Erro("Beneficiário não encontrado"));
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Erro("Erro ao buscar beneficiário por CPF"));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> update(Beneficiario beneficiario) {
        Integer id = beneficiario.getId();

        if (id != null && id > 0) {
            if (!campoVazio(beneficiario)) {
                Conexao db = Banco.getConexao();

                try {
                    if (!db.conectar()) {
                        throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                    }

                    Beneficiario existente = beneficiario.buscarPorId(beneficiario.getId(), db);

                    if (existente != null) {
                        Beneficiario b = beneficiario.alterar(db);
                        if (b != null)
                            return ResponseEntity.ok(beneficiario);
                        else
                            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar beneficiário"));
                    } else {
                        return ResponseEntity.badRequest().body(new Erro("Beneficiário não encontrado"));
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

            return ResponseEntity.badRequest().body(new Erro("Nome, CPF e NIS são obrigatórios para alteração"));
        } else {
            return ResponseEntity.badRequest().body(new Erro("ID é obrigatório para alteração"));
        }
    }

    public ResponseEntity<?> delete(Integer id) {
        if (id != null && id > 0) {
            Conexao db = Banco.getConexao();

            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                Beneficiario beneficiario = new Beneficiario(id);
                Beneficiario existente = beneficiario.buscarPorId(beneficiario.getId(), db);

                if (existente != null) {
                    if (beneficiario.apagar(db))
                        return ResponseEntity.ok(true);
                    else
                        return ResponseEntity.badRequest().body(new Erro("Erro ao excluir beneficiário"));
                } else {
                    return ResponseEntity.badRequest().body(new Erro("Beneficiário não encontrado"));
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
        } else {
            return ResponseEntity.badRequest().body(new Erro("Id inválido"));
        }
    }

    public ResponseEntity<?> getAllOrFilter(String filtro) {
        Conexao db = Banco.getConexao();
        Beneficiario beneficiario = new Beneficiario();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            List<Beneficiario> lista;

            if (filtro == null || filtro.isEmpty()) {
                lista = beneficiario.buscarTodos(db);
            } else {
                lista = beneficiario.buscarComFiltro(filtro, db);
            }

            if (lista == null || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar beneficiários: " + e.getMessage());
        } finally {
            db.desconectar();
        }
    }

}
