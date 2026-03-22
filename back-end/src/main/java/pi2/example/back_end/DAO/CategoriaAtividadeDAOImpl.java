package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.CategoriaAtividade;
import pi2.example.back_end.db.Banco;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoriaAtividadeDAOImpl implements CategoriaAtividadeDAO {
    @Override
    public boolean salvar(CategoriaAtividade c) {
        String sql = "INSERT INTO categoria (nome) VALUES ('" + c.getNome() + "')";
        return Banco.getCon().manipular(sql);
    }

    @Override
    public List<CategoriaAtividade> listar() {
        List<CategoriaAtividade> lista = new ArrayList<>();

        String sql = "SELECT id_categoria, nome FROM categoria ORDER BY nome";

        ResultSet rs = Banco.getCon().consultar(sql);

        try {
            while (rs.next()) {
                CategoriaAtividade c = new CategoriaAtividade();
                c.setId(rs.getInt("id_categoria"));
                c.setNome(rs.getString("nome"));

                lista.add(c);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar categorias de atividade: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public CategoriaAtividade buscarPorId(int id) {
        CategoriaAtividade c = null;

        String sql = "SELECT id_categoria, nome FROM categoria WHERE id_categoria = " + id;

        ResultSet rs = Banco.getCon().consultar(sql);

        try {
            if (rs.next()) {
                c = new CategoriaAtividade();
                c.setId(rs.getInt("id_categoria"));
                c.setNome(rs.getString("nome"));
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar categoria de atividade por id: " + e.getMessage());
        }

        return c;
    }

    @Override
    public boolean atualizar(CategoriaAtividade c) {
        String sql = "UPDATE categoria SET nome = '" + c.getNome() + "' WHERE id_categoria = " + c.getId();
        return Banco.getCon().manipular(sql);
    }

    @Override
    public boolean excluir(int id) {
        String sql = "DELETE FROM categoria WHERE id_categoria = " + id;
        return Banco.getCon().manipular(sql);
    }

}
