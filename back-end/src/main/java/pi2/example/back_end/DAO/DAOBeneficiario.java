package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Beneficiario;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOBeneficiario {

    private final Conexao bd;

    public DAOBeneficiario(Conexao bd) {
        this.bd = bd;
    }

    // -------------------- INSERT --------------------
    public Beneficiario gravar(Beneficiario b) {

        String sql = "INSERT INTO BENEFICIARIO (" +
                "nome, nascimento, idade, rg, cpf, nis, renda, endereco, bairro, tipo_residencia, " +
                "telefone, celular, celular_recado, alergias, tratamentos, participacao, situacao" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {

            stmt.setString(1, b.getNome());
            stmt.setDate(2, b.getNascimento());
            stmt.setInt(3, b.getIdade());
            stmt.setString(4, b.getRg());
            stmt.setString(5, b.getCpf());
            stmt.setString(6, b.getNis());
            stmt.setDouble(7, b.getRenda());
            stmt.setString(8, b.getEndereco());
            stmt.setString(9, b.getBairro());
            stmt.setString(10, b.getTipoResidencia());
            stmt.setString(11, b.getTelefone());
            stmt.setString(12, b.getCelular());
            stmt.setString(13, b.getCelularRecado());
            stmt.setString(14, b.getAlergias());
            stmt.setString(15, b.getTratamentos());
            stmt.setString(16, b.getParticipacao());
            stmt.setString(17, b.getSituacao());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                b.setId(rs.getInt(1));
            }

            return b;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }

    // -------------------- UPDATE --------------------
    public Beneficiario alterar(Beneficiario b) {

        String sql = "UPDATE BENEFICIARIO SET " +
                "nome=?, nascimento=?, idade=?, rg=?, cpf=?, nis=?, renda=?, endereco=?, bairro=?, tipo_residencia=?, " +
                "telefone=?, celular=?, celular_recado=?, alergias=?, tratamentos=?, participacao=?, situacao=? " +
                "WHERE id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, b.getNome());
            stmt.setDate(2, b.getNascimento());
            stmt.setInt(3, b.getIdade());
            stmt.setString(4, b.getRg());
            stmt.setString(5, b.getCpf());
            stmt.setString(6, b.getNis());
            stmt.setDouble(7, b.getRenda());
            stmt.setString(8, b.getEndereco());
            stmt.setString(9, b.getBairro());
            stmt.setString(10, b.getTipoResidencia());
            stmt.setString(11, b.getTelefone());
            stmt.setString(12, b.getCelular());
            stmt.setString(13, b.getCelularRecado());
            stmt.setString(14, b.getAlergias());
            stmt.setString(15, b.getTratamentos());
            stmt.setString(16, b.getParticipacao());
            stmt.setString(17, b.getSituacao());
            stmt.setInt(18, b.getId());

            stmt.executeUpdate();

            return b;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }

    // -------------------- DELETE --------------------
    public boolean apagar(Beneficiario b) {

        String sql = "DELETE FROM BENEFICIARIO WHERE id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, b.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }

    // -------------------- GET ALL --------------------
    public List<Beneficiario> getAll() {

        List<Beneficiario> lista = new ArrayList<>();

        String sql = "SELECT * FROM BENEFICIARIO ORDER BY nome ASC";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                lista.add(new Beneficiario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDate("nascimento"),
                        rs.getInt("idade"),
                        rs.getString("rg"),
                        rs.getString("cpf"),
                        rs.getString("nis"),
                        rs.getDouble("renda"),
                        rs.getString("endereco"),
                        rs.getString("bairro"),
                        rs.getString("tipo_residencia"),
                        rs.getString("telefone"),
                        rs.getString("celular"),
                        rs.getString("celular_recado"),
                        rs.getString("alergias"),
                        rs.getString("tratamentos"),
                        null,
                        rs.getString("participacao"),
                        rs.getString("situacao")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return lista;
    }

    // -------------------- GET BY ID --------------------
    public Beneficiario get(Integer id) {

        Beneficiario b = null;
        String sql = "SELECT * FROM BENEFICIARIO WHERE id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                b = new Beneficiario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDate("nascimento"),
                        rs.getInt("idade"),
                        rs.getString("rg"),
                        rs.getString("cpf"),
                        rs.getString("nis"),
                        rs.getDouble("renda"),
                        rs.getString("endereco"),
                        rs.getString("bairro"),
                        rs.getString("tipo_residencia"),
                        rs.getString("telefone"),
                        rs.getString("celular"),
                        rs.getString("celular_recado"),
                        rs.getString("alergias"),
                        rs.getString("tratamentos"),
                        null,
                        rs.getString("participacao"),
                        rs.getString("situacao")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return b;
    }

    // -------------------- BUSCAR POR NOME --------------------
    public List<Beneficiario> buscarPorNome(String nome) {

        List<Beneficiario> lista = new ArrayList<>();
        String sql;

        if (nome == null || nome.isEmpty()) {
            sql = "SELECT * FROM BENEFICIARIO ORDER BY nome ASC";
        } else {
            sql = "SELECT * FROM BENEFICIARIO WHERE nome ILIKE '%' || ? || '%' ORDER BY nome ASC";
        }

        try (PreparedStatement stmt = bd.preparar(sql)) {

            if (nome != null && !nome.isEmpty()) {
                stmt.setString(1, nome);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(get(rs.getInt("id")));
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return lista;
    }

    // -------------------- BUSCAR POR CPF --------------------
    public List<Beneficiario> buscarPorCpf(String cpf) {

        List<Beneficiario> lista = new ArrayList<>();
        String sql;

        if (cpf == null || cpf.isEmpty()) {
            sql = "SELECT * FROM BENEFICIARIO ORDER BY cpf ASC";
        } else {
            sql = "SELECT * FROM BENEFICIARIO WHERE cpf ILIKE '%' || ? || '%' ORDER BY cpf ASC";
        }

        try (PreparedStatement stmt = bd.preparar(sql)) {

            if (cpf != null && !cpf.isEmpty()) {
                stmt.setString(1, cpf);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(get(rs.getInt("id")));
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return lista;
    }

    public List<Beneficiario> buscarPorNis(String nis)
    {
        List<Beneficiario> lista = new ArrayList<>();
        String sql;

        if (nis == null || nis.isEmpty()) {
            sql = "SELECT * FROM BENEFICIARIO ORDER BY nis ASC";
        } else {
            sql = "SELECT * FROM BENEFICIARIO WHERE nis ILIKE '%' || ? || '%' ORDER BY nis ASC";
        }

        try (PreparedStatement stmt = bd.preparar(sql)) {

            if (nis != null && !nis.isEmpty()) {
                stmt.setString(1, nis);
            }
            System.out.println(nis);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Beneficiario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDate("nascimento"),
                        rs.getInt("idade"),
                        rs.getString("rg"),
                        rs.getString("cpf"),
                        rs.getString("nis"),
                        rs.getDouble("renda"),
                        rs.getString("endereco"),
                        rs.getString("bairro"),
                        rs.getString("tipo_residencia"),
                        rs.getString("telefone"),
                        rs.getString("celular"),
                        rs.getString("celular_recado"),
                        rs.getString("alergias"),
                        rs.getString("tratamentos"),
                        null,
                        rs.getString("participacao"),
                        rs.getString("situacao")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return lista;
    }
}