package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Beneficiario;
import pi2.example.back_end.db.Banco;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BeneficiarioDAOImpl implements BeneficiarioDAO {

    @Override
    public List<Beneficiario> listar() {

        List<Beneficiario> lista = new ArrayList<>();

        String sql = "SELECT id, nome, cpf FROM beneficiario";

        ResultSet rs = Banco.getCon().consultar(sql);

        if (rs == null) {
            System.out.println("Erro ao listar: " + Banco.getCon().getMensagemErro());
            return lista;
        }

        try {
            while (rs.next()) {

                Beneficiario b = new Beneficiario();
                b.setId(rs.getInt("id"));
                b.setNome(rs.getString("nome"));
                b.setCpf(rs.getString("cpf"));

                lista.add(b);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public Beneficiario buscarPorCpf(String cpf) {

        String sql = "SELECT id, nome, cpf FROM beneficiario WHERE cpf = '" + cpf + "'";

        ResultSet rs = Banco.getCon().consultar(sql);

        if (rs == null) {
            System.out.println("Erro ao buscar: " + Banco.getCon().getMensagemErro());
            return null;
        }

        try {
            if (rs.next()) {

                Beneficiario b = new Beneficiario();
                b.setId(rs.getInt("id"));
                b.setNome(rs.getString("nome"));
                b.setCpf(rs.getString("cpf"));

                rs.close();
                return b;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean salvar(Beneficiario b) {

        String sql = "INSERT INTO beneficiario (" +
                "nome, nascimento, idade, rg, cpf, nis, renda, endereco, bairro, tipo_residencia, " +
                "telefone, celular, celular_recado, alergias, tratamentos, participacao, situacao) VALUES (" +
                "'" + b.getNome() + "', '" +
                b.getNascimento() + "', " +
                b.getIdade() + ", '" +
                b.getRg() + "', '" +
                b.getCpf() + "', '" +
                b.getNis() + "', " +
                b.getRenda() + ", '" +
                b.getEndereco() + "', '" +
                b.getBairro() + "', '" +
                b.getTipoResidencia() + "', '" +
                b.getTelefone() + "', '" +
                b.getCelular() + "', '" +
                b.getCelularRecado() + "', '" +
                b.getAlergias() + "', '" +
                b.getTratamentos() + "', '" +
                b.getParticipacao() + "', '" +
                b.getSituacao() + "')";

        return Banco.getCon().manipular(sql);
    }

    @Override
    public boolean excluir(int id) {

        String sql = "DELETE FROM beneficiario WHERE id = " + id;

        return Banco.getCon().manipular(sql);
    }

    @Override
    public boolean atualizar(Beneficiario b) {

        String sql = "UPDATE beneficiario SET " +
                "nome = '" + b.getNome() + "', " +
                "nascimento = '" + b.getNascimento() + "', " +
                "idade = " + b.getIdade() + ", " +
                "rg = '" + b.getRg() + "', " +
                "cpf = '" + b.getCpf() + "', " +
                "nis = '" + b.getNis() + "', " +
                "renda = " + b.getRenda() + ", " +
                "endereco = '" + b.getEndereco() + "', " +
                "bairro = '" + b.getBairro() + "', " +
                "tipo_residencia = '" + b.getTipoResidencia() + "', " +
                "telefone = '" + b.getTelefone() + "', " +
                "celular = '" + b.getCelular() + "', " +
                "celular_recado = '" + b.getCelularRecado() + "', " +
                "alergias = '" + b.getAlergias() + "', " +
                "tratamentos = '" + b.getTratamentos() + "', " +
                "participacao = '" + b.getParticipacao() + "', " +
                "situacao = '" + b.getSituacao() + "' " +
                "WHERE id = " + b.getId();

        return Banco.getCon().manipular(sql);
    }
}