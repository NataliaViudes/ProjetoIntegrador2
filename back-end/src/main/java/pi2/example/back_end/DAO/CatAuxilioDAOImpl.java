package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.CategoriaAuxilio;
import pi2.example.back_end.db.Banco;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CatAuxilioDAOImpl implements CatAuxilioDAO {
    @Override
    public List<CategoriaAuxilio> listar() {

        List<CategoriaAuxilio> lista = new ArrayList<>();

        String sql = "SELECT id, nome FROM categoria_auxilio";

        ResultSet rs = Banco.getCon().consultar(sql);

        try {
            while(rs.next()) {

                CategoriaAuxilio c = new CategoriaAuxilio();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));

                lista.add(c);
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }

        return lista;
    }
}
