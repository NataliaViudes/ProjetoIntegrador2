package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Beneficiario;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BeneficiarioDAOImpl {
    private final Conexao bd;

    public BeneficiarioDAOImpl(Conexao bd) {
        this.bd = bd;
    }

    public Beneficiario gravar(Beneficiario entidade) {
        String sql = """
            INSERT INTO beneficiario (
                nome, nascimento, idade, rg, cpf, nis, renda, endereco, bairro, tipo_residencia,
                telefone, celular, celular_recado, alergias, tratamentos, participacao, situacao
            ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
        """;

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {
            stmt.setString(1, entidade.getNome());
            stmt.setDate(2, entidade.getNascimento() == null || entidade.getNascimento().isEmpty()
                    ? null
                    : Date.valueOf(entidade.getNascimento()));
            stmt.setObject(3, entidade.getIdade());
            stmt.setString(4, entidade.getRg());
            stmt.setString(5, entidade.getCpf());
            stmt.setString(6, entidade.getNis());
            stmt.setObject(7, entidade.getRenda());
            stmt.setString(8, entidade.getEndereco());
            stmt.setString(9, entidade.getBairro());
            stmt.setString(10, entidade.getTipoResidencia());
            stmt.setString(11, entidade.getTelefone());
            stmt.setString(12, entidade.getCelular());
            stmt.setString(13, entidade.getCelularRecado());
            stmt.setString(14, entidade.getAlergias());
            stmt.setString(15, entidade.getTratamentos());
            stmt.setString(16, entidade.getParticipacao());
            stmt.setString(17, entidade.getSituacao());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entidade.setId(rs.getInt(1));
            }

            return entidade;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao incluir beneficiário", e);
        }
    }

    public Beneficiario alterar(Beneficiario entidade) {
        String sql = """
            UPDATE beneficiario SET
                nome = ?, nascimento = ?, idade = ?, rg = ?, cpf = ?, nis = ?, renda = ?,
                endereco = ?, bairro = ?, tipo_residencia = ?, telefone = ?, celular = ?,
                celular_recado = ?, alergias = ?, tratamentos = ?, participacao = ?, situacao = ?
            WHERE id = ?
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setString(1, entidade.getNome());
            stmt.setDate(2, entidade.getNascimento() == null || entidade.getNascimento().isEmpty()
                    ? null
                    : Date.valueOf(entidade.getNascimento()));
            stmt.setObject(3, entidade.getIdade());
            stmt.setString(4, entidade.getRg());
            stmt.setString(5, entidade.getCpf());
            stmt.setString(6, entidade.getNis());
            stmt.setObject(7, entidade.getRenda());
            stmt.setString(8, entidade.getEndereco());
            stmt.setString(9, entidade.getBairro());
            stmt.setString(10, entidade.getTipoResidencia());
            stmt.setString(11, entidade.getTelefone());
            stmt.setString(12, entidade.getCelular());
            stmt.setString(13, entidade.getCelularRecado());
            stmt.setString(14, entidade.getAlergias());
            stmt.setString(15, entidade.getTratamentos());
            stmt.setString(16, entidade.getParticipacao());
            stmt.setString(17, entidade.getSituacao());
            stmt.setInt(18, entidade.getId());

            stmt.executeUpdate();
            return entidade;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar beneficiário: " + e.getMessage());
            return null;
        }
    }

    public boolean apagar(Beneficiario entidade) {
        String sql = "DELETE FROM beneficiario WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, entidade.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir beneficiário: " + e.getMessage());
            return false;
        }
    }

    private Beneficiario mapBeneficiario(ResultSet rs) throws SQLException {
        Beneficiario b = new Beneficiario();

        b.setId(rs.getInt("id"));
        b.setNome(rs.getString("nome"));

        Date data = rs.getDate("nascimento");
        b.setNascimento(data != null ? data.toString() : null);

        b.setIdade((Integer) rs.getObject("idade"));
        b.setRg(rs.getString("rg"));
        b.setCpf(rs.getString("cpf"));
        b.setNis(rs.getString("nis"));
        b.setRenda((Double) rs.getObject("renda"));
        b.setEndereco(rs.getString("endereco"));
        b.setBairro(rs.getString("bairro"));
        b.setTipoResidencia(rs.getString("tipo_residencia"));
        b.setTelefone(rs.getString("telefone"));
        b.setCelular(rs.getString("celular"));
        b.setCelularRecado(rs.getString("celular_recado"));
        b.setAlergias(rs.getString("alergias"));
        b.setTratamentos(rs.getString("tratamentos"));
        b.setParticipacao(rs.getString("participacao"));
        b.setSituacao(rs.getString("situacao"));

        return b;
    }

    public Beneficiario get(int id) {
        Beneficiario beneficiario = null;

        String sql = "SELECT * FROM beneficiario WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                beneficiario = mapBeneficiario(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar beneficiário por id: " + e.getMessage());
        }

        return beneficiario;
    }

    public Beneficiario getByCpf(String cpf) {
        Beneficiario beneficiario = null;

        String sql = "SELECT * FROM beneficiario WHERE cpf = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                beneficiario = mapBeneficiario(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar beneficiário por CPF: " + e.getMessage());
        }

        return beneficiario;
    }

    public List<Beneficiario> get(String filtro) {
        List<Beneficiario> lista = new ArrayList<>();

        String sql = """
            SELECT * FROM beneficiario
            WHERE nome ILIKE ? OR cpf ILIKE ?
            ORDER BY nome
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setString(1, "%" + filtro + "%");
            stmt.setString(2, "%" + filtro + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapBeneficiario(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar beneficiário com filtro: " + e.getMessage());
        }

        return lista;
    }

    public List<Beneficiario> getAll() {
        List<Beneficiario> lista = new ArrayList<>();

        String sql = "SELECT * FROM beneficiario ORDER BY nome";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapBeneficiario(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar beneficiários: " + e.getMessage());
        }

        return lista;
    }
}