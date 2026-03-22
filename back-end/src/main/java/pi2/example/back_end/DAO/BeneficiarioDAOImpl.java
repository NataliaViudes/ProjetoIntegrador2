package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Beneficiario;
import pi2.example.back_end.db.Banco;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BeneficiarioDAOImpl implements BeneficiarioDAO{
    @Override
    public List<Beneficiario> listar() {

        List<Beneficiario> lista = new ArrayList<>();

        String sql = "SELECT id, nome, cpf FROM beneficiario";

        ResultSet rs = Banco.getCon().consultar(sql);

        try {
            while(rs.next()) {

                Beneficiario b = new Beneficiario();
                b.setId(rs.getInt("id"));
                b.setNome(rs.getString("nome"));
                b.setCpf(rs.getString("cpf"));

                lista.add(b);
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public Beneficiario buscarPorCpf(String cpf) {

        String sql = "SELECT id, nome, cpf FROM beneficiario WHERE cpf = '" + cpf + "'";

        ResultSet rs = Banco.getCon().consultar(sql);

        try {
            if(rs.next()) {

                Beneficiario b = new Beneficiario();
                b.setId(rs.getInt("id"));
                b.setNome(rs.getString("nome"));
                b.setCpf(rs.getString("cpf"));

                return b;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }

        return null;
    }
}
