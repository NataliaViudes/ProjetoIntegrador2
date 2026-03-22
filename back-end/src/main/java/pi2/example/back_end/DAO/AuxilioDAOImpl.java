package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Auxilio;
import pi2.example.back_end.Modelo.Beneficiario;
import pi2.example.back_end.Modelo.CategoriaAuxilio;
import pi2.example.back_end.db.Banco;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AuxilioDAOImpl implements AuxilioDAO {
    @Override
    public boolean salvar(Auxilio a) {

        String sql = "INSERT INTO auxilio (descricao, id_beneficiario, id_categoria) VALUES (" +
                "'" + a.getDescricao() + "', " +
                a.getBeneficiario().getId() + ", " +
                a.getCategoria().getId() + ")";

        return Banco.getCon().manipular(sql);
    }

    @Override
    public List<Auxilio> listar() {

        List<Auxilio> lista = new ArrayList<>();

        String sql = """
            SELECT a.id, a.descricao,
                   b.id as ben_id, b.nome as ben_nome,
                   c.id as cat_id, c.nome as cat_nome
            FROM auxilio a
            JOIN beneficiario b ON a.id_beneficiario = b.id
            JOIN categoria c ON a.id_categoria = c.id
        """;

        ResultSet rs = Banco.getCon().consultar(sql);

        try {
            while(rs.next()) {

                Beneficiario b = new Beneficiario();
                b.setId(rs.getInt("ben_id"));
                b.setNome(rs.getString("ben_nome"));

                CategoriaAuxilio c = new CategoriaAuxilio();
                c.setId(rs.getInt("cat_id"));
                c.setNome(rs.getString("cat_nome"));

                Auxilio a = new Auxilio();
                a.setId(rs.getInt("id"));
                a.setDescricao(rs.getString("descricao"));
                a.setBeneficiario(b);
                a.setCategoria(c);

                lista.add(a);
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public boolean excluir(int id) {

        String sql = "DELETE FROM auxilio WHERE id=" + id;

        return Banco.getCon().manipular(sql);
    }

    @Override
    public boolean atualizar(Auxilio a) {

        String sql = "UPDATE auxilio SET " +
                "descricao = '" + a.getDescricao() + "', " +
                "id_beneficiario = " + a.getBeneficiario().getId() + ", " +
                "id_categoria = " + a.getCategoria().getId() +
                " WHERE id = " + a.getId();

        return Banco.getCon().manipular(sql);
    }
}
