package pi2.example.back_end.db;

import pi2.example.back_end.Modelo.Alimento;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DALAlimento {
    private final Conexao bd;

    public DALAlimento(Conexao bd) {
        this.bd = bd;
    }

    @Override
    public boolean gravar(Alimento entidade) {
        String sql = "INSERT INTO alimento (id_alimento, nome, tipo, descricao) VALUES ("
                + entidade.getId() + ", '" + entidade.getNome() + "')";
        return bd.manipular(sql);
    }

    @Override
    public boolean alterar(Alimento entidade) {
        String sql = "UPDATE alimento SET nome = '" + entidade.getNome()
                + "' WHERE id_alimento = " + entidade.getId();
        return bd.manipular(sql);
    }

    @Override
    public boolean apagar(Alimento entidade) {
        String sql = "DELETE FROM alimento WHERE id_alimento = " + entidade.getId();
        return bd.manipular(sql);
    }

    @Override
    public Alimento get(int id) {
        Alimento ali = null;
        String sql = "SELECT * FROM alimento WHERE id_alimento = " + id;
        ResultSet rs = bd.consultar(sql);
        try {
            if (rs != null && rs.next()) {
                ali = new Alimento(rs.getInt("id_alimento"), rs.getString("nome"), rs.getString("tipo"), rs.getString("descricao"));
            }
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.out.println("Erro ao buscar alimento: " + e);
        }
        return ali;
    }

    @Override
    public List<Alimento> get(String filtro) {
        List<Alimento> lista = new ArrayList<>();
        String sql = "SELECT * FROM alimento";
        if (filtro != null && !filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }
        ResultSet rs = bd.consultar(sql);
        try {
            if (rs != null) {
                while (rs.next()) {
                    Alimento eve = new Alimento(rs.getInt("id_alimento"), rs.getString("nome"), rs.getString("tipo"),rs.getString("descricao"));
                    lista.add(eve);
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar lista de alimentos: " + e);
        }
        return lista;
    }
}
