package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.Prescricao;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.List;

public class PrescricaoControl {

    public PrescricaoControl() {
    }

    // -------------------- INCLUIR --------------------
    public ResponseEntity<?> incluir(Prescricao p)
    {
        if (p.getDosagem() != null && !p.getDosagem().isEmpty()) {

            if (p.getQuantidade() != null && p.getQuantidade() > 0) {

                if (p.getHorario() != null) {

                    if (p.getIntervalo() != null && !p.getIntervalo().isEmpty()) {

                        if (p.getBeneficiario() != null &&
                                p.getBeneficiario().getId() != null &&
                                p.getBeneficiario().getId() > 0) {

                            if (p.getRemedio() != null &&
                                    p.getRemedio().getId() != null &&
                                    p.getRemedio().getId() > 0) {

                                Conexao db = Banco.getConexao();

                                try {

                                    if (!db.conectar()) {
                                        throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                                    }

                                    Prescricao resultado = p.incluir(db);

                                    if (resultado != null) {
                                        return ResponseEntity.ok(resultado);
                                    } else {
                                        return ResponseEntity.badRequest().body(new Erro("Erro ao salvar prescriÃ§Ã£o"));
                                    }

                                } catch (SQLException e) {

                                    return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

                                } catch (Exception e) {

                                    return ResponseEntity.badRequest().body(new Erro("Erro geral"));

                                } finally {

                                    db.desconectar();
                                }

                            } else {
                                return ResponseEntity.badRequest().body(new Erro("Remédio é obrigatório"));
                            }

                        } else {
                            return ResponseEntity.badRequest().body(new Erro("Beneficiário é obrigatório"));
                        }

                    } else {
                        return ResponseEntity.badRequest().body(new Erro("Intervalo é obrigatório"));
                    }

                } else {
                    return ResponseEntity.badRequest().body(new Erro("Horário é obrigatório"));
                }

            } else {
                return ResponseEntity.badRequest().body(new Erro("Quantidade é obrigatória"));
            }

        } else {
            return ResponseEntity.badRequest().body(new Erro("Dosagem é obrigatória"));
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

            Prescricao p = new Prescricao();

            List<Prescricao> lista = p.listar(db);

            return ResponseEntity.ok(lista);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.badRequest().body(new Erro("Erro ao buscar prescrições"));

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

            Prescricao p = new Prescricao();

            Prescricao resultado = p.buscarPorId(db, id);

            if (resultado != null) {
                return ResponseEntity.ok(resultado);
            } else {
                return ResponseEntity.badRequest().body(new Erro("Prescrição não encontrada"));
            }

        } catch (SQLException e) {

            return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {

            db.desconectar();
        }
    }

    // -------------------- BUSCAR POR BENEFICIARIO --------------------
    public ResponseEntity<?> buscaPorBeneficiario(Integer idBeneficiario)
    {
        List<Prescricao> lista;

        Prescricao p = new Prescricao();

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            lista = p.buscarPorBeneficiario(db, idBeneficiario);

            if (lista != null && !lista.isEmpty()) {
                return ResponseEntity.ok(lista);
            } else {
                return ResponseEntity.badRequest().body(new Erro("Nenhuma prescrição encontrada"));
            }

        } catch (SQLException e) {

            return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {

            db.desconectar();
        }
    }

    // -------------------- BUSCAR POR REMEDIO --------------------
    public ResponseEntity<?> buscaPorRemedio(Integer idRemedio)
    {
        List<Prescricao> lista;

        Prescricao p = new Prescricao();

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            lista = p.buscarPorRemedio(db, idRemedio);

            if (lista != null && !lista.isEmpty()) {
                return ResponseEntity.ok(lista);
            } else {
                return ResponseEntity.badRequest().body(new Erro("Nenhuma prescrição encontrada"));
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
    public ResponseEntity<?> update(Prescricao p)
    {
        if (p.getId() == null || p.getId() <= 0) {
            return ResponseEntity.badRequest().body(new Erro("ID obrigatório"));
        }

        if (p.getDosagem() == null || p.getDosagem().isEmpty()) {
            return ResponseEntity.badRequest().body(new Erro("Dosagem obrigatória"));
        }

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            Prescricao existente = p.buscarPorId(db, p.getId());

            if (existente != null) {

                Prescricao atualizado = p.alterar(db);

                if (atualizado != null) {
                    return ResponseEntity.ok(atualizado);
                } else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao atualizar"));
                }

            } else {

                return ResponseEntity.badRequest().body(new Erro("Prescrição não encontrada"));
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

            Prescricao p = new Prescricao();

            Prescricao existente = p.buscarPorId(db, id);

            if (existente != null) {

                p.setId(id);

                if (p.apagar(db)) {
                    return ResponseEntity.ok(true);
                } else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao excluir"));
                }

            } else {

                return ResponseEntity.badRequest().body(new Erro("Prescrição não encontrada"));
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
