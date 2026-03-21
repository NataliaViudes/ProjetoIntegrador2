package pi2.example.back_end.db;

import pi2.example.back_end.Modelo.Funcionario;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DALFuncionarios implements IDAL<Funcionario> {
    private final Conexao bd;

    public DALFuncionarios(Conexao bd) {
        this.bd = bd;
    }

    @Override
    public boolean gravar(Funcionario F) {
        String sql = "INSERT INTO funcionario (nome, cpf, telefone, nis, nascimento, sexo, endereco, cargo) VALUES ('"
                + F.getNome() + "', '"
                + F.getCpf() + "', '"
                + F.getTelefone() + "', '"
                + F.getNis() + "', '"
                + F.getNascimento() + "', '"
                + F.getSexo() + "', '"
                + F.getEndereco() + "', '"
                + F.getCargo() + "')";

        return bd.manipular(sql);
    }

    @Override
    public boolean alterar(Funcionario F) {
        String sql = "UPDATE funcionario SET "
                + "nome = '" + F.getNome() + "', "
                + "cpf = '" + F.getCpf() + "', "
                + "telefone = '" + F.getTelefone() + "', "
                + "nis = '" + F.getNis() + "', "
                + "nascimento = '" + F.getNascimento() + "', "
                + "sexo = '" + F.getSexo() + "', "
                + "endereco = '" + F.getEndereco() + "', "
                + "cargo = '" + F.getCargo() + "' "
                + "WHERE id_funcionario = " + F.getId();
        return bd.manipular(sql);
    }

    @Override
    public boolean apagar(Funcionario F) {
        String sql = "DELETE FROM funcionario WHERE id_funcionario = " + F.getId();
        return bd.manipular(sql);
    }

    @Override
    public Funcionario get(int id) {
        Funcionario f = null;
        String sql = "SELECT * FROM funcionario WHERE id_funcionario = " + id;

        try {
            ResultSet rs = bd.consultar(sql);
            if (rs.next()) {
                f = new Funcionario();

                f.setId(rs.getInt("id_funcionario"));
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf"));
                f.setTelefone(rs.getString("telefone"));
                f.setNis(rs.getString("nis"));
                f.setNascimento(rs.getDate("nascimento"));
                f.setSexo(rs.getString("sexo"));
                f.setEndereco(rs.getString("endereco"));
                f.setCargo(rs.getString("cargo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    @Override
    public List<Funcionario> get(String filtro) {
        List<Funcionario> lista = new ArrayList<>();

        String sql = "SELECT * FROM funcionario WHERE nome ILIKE '%" + filtro + "%'";

        try {
            ResultSet rs = bd.consultar(sql);

            while (rs.next()) {
                Funcionario f = new Funcionario();

                f.setId(rs.getInt("id_funcionario"));
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf"));
                f.setTelefone(rs.getString("telefone"));
                f.setNis(rs.getString("nis"));
                f.setNascimento(rs.getDate("nascimento"));
                f.setSexo(rs.getString("sexo"));
                f.setEndereco(rs.getString("endereco"));
                f.setCargo(rs.getString("cargo"));

                lista.add(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Funcionario> getAll(){
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario";

        try {
            ResultSet rs = bd.consultar(sql);
            while (rs.next()) {
                Funcionario f = new Funcionario();

                f.setId(rs.getInt("id_funcionario"));
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf"));
                f.setTelefone(rs.getString("telefone"));
                f.setNis(rs.getString("nis"));
                f.setNascimento(rs.getDate("nascimento"));
                f.setSexo(rs.getString("sexo"));
                f.setEndereco(rs.getString("endereco"));
                f.setCargo(rs.getString("cargo"));

                lista.add(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
