package pi2.example.back_end.db;

import pi2.example.back_end.Modelo.Beneficiario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;

public class DALBeneficiario implements IDAL<Beneficiario> {

    private final Conexao bd;

    public DALBeneficiario(Conexao bd) {
        this.bd = bd;
    }

    @Override
    public boolean gravar(Beneficiario b) {
        String sql = "INSERT INTO beneficiario (" +
                "nome, nascimento, idade, rg, cpf, nis, renda, endereco, bairro, tipo_residencia, " +
                "telefone, celular, celular_recado, alergias, tratamentos, participacao, situacao" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }

    @Override
    public boolean alterar(Beneficiario b) {
        String sql = "UPDATE beneficiario SET " +
                "nome=?, nascimento=?, idade=?, rg=?, cpf=?, nis=?, renda=?, endereco=?, bairro=?, tipo_residencia=?, " +
                "telefone=?, celular=?, celular_recado=?, alergias=?, tratamentos=?, participacao=?, situacao=? " +
                "WHERE id = ?";

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

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }

    @Override
    public boolean apagar(Beneficiario b) {
        String sql = "DELETE FROM beneficiario WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, b.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }

    @Override
    public Beneficiario get(int id) {
        Beneficiario b = null;
        String sql = "SELECT * FROM beneficiario WHERE id = ?";

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
                        rs.getString("participacao"),
                        rs.getString("situacao")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return b;
    }

    @Override
    public List<Beneficiario> get(String filtro) {
        List<Beneficiario> lista = new ArrayList<>();
        String sql = "SELECT * FROM beneficiario";

        if (filtro != null && !filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }

        ResultSet rs = bd.consultar(sql);

        try {
            if (rs != null) {
                while (rs.next()) {
                    Beneficiario b = new Beneficiario(
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
                            rs.getString("participacao"),
                            rs.getString("situacao")
                    );
                    lista.add(b);
                }
                rs.close();
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar beneficiários: " + e);
        }

        return lista;
    }

    public List<Beneficiario> buscarPorNome(String nome) {
        List<Beneficiario> lista = new ArrayList<>();
        String sql;

        if (nome == null || nome.isEmpty()) {
            sql = "SELECT * FROM beneficiario ORDER BY id ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(get(rs.getInt("id")));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }

        } else {
            sql = "SELECT * FROM beneficiario WHERE nome ILIKE '%' || ? || '%' ORDER BY id ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                stmt.setString(1, nome);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(get(rs.getInt("id")));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }
        }

        return lista;
    }
}