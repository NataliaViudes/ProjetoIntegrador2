package pi2.example.back_end.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pi2.example.back_end.Modelo.Beneficiario;

public class DALBeneficiario implements IDAL<Beneficiario> {

    private final Conexao bd;

    public DALBeneficiario(Conexao bd) {
        this.bd = bd;
    }

    @Override
    public boolean gravar(Beneficiario entidade) {
        String sql = "INSERT INTO beneficiario (" +
                "id, nome, nascimento, idade, rg, cpf, nis, renda, endereco, bairro, tipo_residencia, " +
                "telefone, celular, celular_recado, alergias, tratamentos, participacao, situacao) VALUES (" +
                entidade.getId() + ", '" +
                entidade.getNome() + "', '" +
                entidade.getNascimento() + "', " +
                entidade.getIdade() + ", '" +
                entidade.getRg() + "', '" +
                entidade.getCpf() + "', '" +
                entidade.getNis() + "', " +
                entidade.getRenda() + ", '" +
                entidade.getEndereco() + "', '" +
                entidade.getBairro() + "', '" +
                entidade.getTipoResidencia() + "', '" +
                entidade.getTelefone() + "', '" +
                entidade.getCelular() + "', '" +
                entidade.getCelularRecado() + "', '" +
                entidade.getAlergias() + "', '" +
                entidade.getTratamentos() + "', '" +
                entidade.getParticipacao() + "', '" +
                entidade.getSituacao() + "')";
        
        return bd.manipular(sql);
    }

    @Override
    public boolean alterar(Beneficiario entidade) {
        String sql = "UPDATE beneficiario SET " +
                "nome = '" + entidade.getNome() + "', " +
                "nascimento = '" + entidade.getNascimento() + "', " +
                "idade = " + entidade.getIdade() + ", " +
                "rg = '" + entidade.getRg() + "', " +
                "cpf = '" + entidade.getCpf() + "', " +
                "nis = '" + entidade.getNis() + "', " +
                "renda = " + entidade.getRenda() + ", " +
                "endereco = '" + entidade.getEndereco() + "', " +
                "bairro = '" + entidade.getBairro() + "', " +
                "tipo_residencia = '" + entidade.getTipoResidencia() + "', " +
                "telefone = '" + entidade.getTelefone() + "', " +
                "celular = '" + entidade.getCelular() + "', " +
                "celular_recado = '" + entidade.getCelularRecado() + "', " +
                "alergias = '" + entidade.getAlergias() + "', " +
                "tratamentos = '" + entidade.getTratamentos() + "', " +
                "participacao = '" + entidade.getParticipacao() + "', " +
                "situacao = '" + entidade.getSituacao() + "' " +
                "WHERE id = " + entidade.getId();

        return bd.manipular(sql);
    }

    @Override
    public boolean apagar(Beneficiario entidade) {
        String sql = "DELETE FROM beneficiario WHERE id = " + entidade.getId();
        return bd.manipular(sql);
    }

    @Override
    public Beneficiario get(int id) {
        Beneficiario b = null;
        String sql = "SELECT * FROM beneficiario WHERE id = " + id;
        ResultSet rs = bd.consultar(sql);

        try {
            if (rs != null && rs.next()) {
                b = new Beneficiario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("nascimento"),
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
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.out.println("Erro ao buscar beneficiário: " + e);
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
                            rs.getString("nascimento"),
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
            System.out.println("Erro ao buscar lista de beneficiários: " + e);
        }

        return lista;
    }
}