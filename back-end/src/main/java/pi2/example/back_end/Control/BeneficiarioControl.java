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

    // -------------------- INCLUIR --------------------
    public ResponseEntity<?> incluir(Beneficiario b)
    {
        if (b.getNome() != null && !b.getNome().isEmpty()) {

            if (b.getNascimento() != null && !b.getNascimento().isEmpty()) {

                if (b.getIdade() != null && b.getIdade() > 0) {

                    if (b.getRg() != null && !b.getRg().isEmpty()) {

                        if (b.getCpf() != null && !b.getCpf().isEmpty()) {

                            if (b.getNis() != null && !b.getNis().isEmpty()) {

                                if (b.getRenda() != null && b.getRenda() >= 0) {

                                    if (b.getEndereco() != null && !b.getEndereco().isEmpty()) {

                                        if (b.getBairro() != null && !b.getBairro().isEmpty()) {

                                            if (b.getTipoResidencia() != null && !b.getTipoResidencia().isEmpty()) {

                                                if (b.getTelefone() != null && !b.getTelefone().isEmpty()) {

                                                    if (b.getCelular() != null && !b.getCelular().isEmpty()) {

                                                        if (b.getCelularRecado() != null && !b.getCelularRecado().isEmpty()) {

                                                            if (b.getAlergias() != null && !b.getAlergias().isEmpty()) {

                                                                if (b.getTratamentos() != null && !b.getTratamentos().isEmpty()) {

                                                                    if (b.getMedicamentos() != null && !b.getMedicamentos().isEmpty()) {

                                                                        if (b.getParticipacao() != null && !b.getParticipacao().isEmpty()) {

                                                                            if (b.getSituacao() != null && !b.getSituacao().isEmpty()) {

                                                                                Conexao db = Banco.getConexao();
                                                                                try {
                                                                                    if (!db.conectar()) {
                                                                                        throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                                                                                    }

                                                                                    Beneficiario resultado = b.incluir(db);
                                                                                    return ResponseEntity.ok(resultado);

                                                                                } catch (SQLException e) {
                                                                                    return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

                                                                                } catch (Exception e) {
                                                                                    return ResponseEntity.badRequest().body(new Erro("Erro geral"));

                                                                                } finally {
                                                                                    db.desconectar();
                                                                                }

                                                                            } else return ResponseEntity.badRequest().body(new Erro("Situação é obrigatória"));
                                                                        } else return ResponseEntity.badRequest().body(new Erro("Participação é obrigatória"));
                                                                    } else return ResponseEntity.badRequest().body(new Erro("Medicamentos são obrigatórios"));
                                                                } else return ResponseEntity.badRequest().body(new Erro("Tratamentos são obrigatórios"));
                                                            } else return ResponseEntity.badRequest().body(new Erro("Alergias são obrigatórias"));
                                                        } else return ResponseEntity.badRequest().body(new Erro("Celular de recado é obrigatório"));
                                                    } else return ResponseEntity.badRequest().body(new Erro("Celular é obrigatório"));
                                                } else return ResponseEntity.badRequest().body(new Erro("Telefone é obrigatório"));
                                            } else return ResponseEntity.badRequest().body(new Erro("Tipo de residência é obrigatório"));
                                        } else return ResponseEntity.badRequest().body(new Erro("Bairro é obrigatório"));
                                    } else return ResponseEntity.badRequest().body(new Erro("Endereço é obrigatório"));
                                } else return ResponseEntity.badRequest().body(new Erro("Renda é obrigatória"));
                            } else return ResponseEntity.badRequest().body(new Erro("NIS é obrigatório"));
                        } else return ResponseEntity.badRequest().body(new Erro("CPF é obrigatório"));
                    } else return ResponseEntity.badRequest().body(new Erro("RG é obrigatório"));
                } else return ResponseEntity.badRequest().body(new Erro("Idade é obrigatória"));
            } else return ResponseEntity.badRequest().body(new Erro("Nascimento é obrigatório"));
        } else return ResponseEntity.badRequest().body(new Erro("Nome é obrigatório"));
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

            Beneficiario b = new Beneficiario();
            Beneficiario resultado = b.buscarPorId(id, db);

            if (resultado != null)
                return ResponseEntity.ok(resultado);
            else
                return ResponseEntity.badRequest().body(new Erro("Beneficiário não encontrado"));

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
        List<Beneficiario> lista;
        Beneficiario b = new Beneficiario();

        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            lista = b.buscarPorNome(nome != null ? nome : "", db);

            if (lista != null && !lista.isEmpty())
                return ResponseEntity.ok(lista);
            else
                return ResponseEntity.badRequest().body(new Erro("Nenhum beneficiário encontrado"));

        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {
            db.desconectar();
        }
    }

    // -------------------- BUSCAR POR CPF --------------------
    public ResponseEntity<?> buscaPorCpf(String cpf)
    {
        List<Beneficiario> lista;
        Beneficiario b = new Beneficiario();

        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            lista = b.buscarPorCpf(cpf != null ? cpf : "", db);

            if (lista != null && !lista.isEmpty())
                return ResponseEntity.ok(lista);
            else
                return ResponseEntity.badRequest().body(new Erro("Nenhum beneficiário encontrado"));

        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {
            db.desconectar();
        }
    }

    // -------------------- BUSCAR POR NIS --------------------
    public ResponseEntity<?> buscaPorNis(String nis)
    {
        List<Beneficiario> lista;
        Beneficiario b = new Beneficiario();

        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            lista = b.buscarPorNis(nis != null ? nis : "", db);

            if (lista != null && !lista.isEmpty())
                return ResponseEntity.ok(lista);
            else
                return ResponseEntity.badRequest().body(new Erro("Nenhum beneficiário encontrado"));

        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {
            db.desconectar();
        }
    }

    // -------------------- UPDATE --------------------
    public ResponseEntity<?> update(Beneficiario b)
    {
        if (b.getId() == null || b.getId() <= 0)
            return ResponseEntity.badRequest().body(new Erro("ID obrigatório"));

        if (b.getNome() == null || b.getNome().isEmpty())
            return ResponseEntity.badRequest().body(new Erro("Nome obrigatório"));

        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            Beneficiario existente = b.buscarPorId(b.getId(), db);

            if (existente != null) {
                Beneficiario atualizado = b.alterar(db);

                if (atualizado != null)
                    return ResponseEntity.ok(atualizado);
                else
                    return ResponseEntity.badRequest().body(new Erro("Erro ao atualizar"));
            }
            else {
                return ResponseEntity.badRequest().body(new Erro("Beneficiário não encontrado"));
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
        if (id == null || id <= 0)
            return ResponseEntity.badRequest().body(new Erro("ID inválido"));

        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            Beneficiario b = new Beneficiario(id);
            Beneficiario existente = b.buscarPorId(id, db);

            if (existente != null) {
                if (b.apagar(db))
                    return ResponseEntity.ok(true);
                else
                    return ResponseEntity.badRequest().body(new Erro("Erro ao excluir"));
            }
            else {
                return ResponseEntity.badRequest().body(new Erro("Beneficiário não encontrado"));
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