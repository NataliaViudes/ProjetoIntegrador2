package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Atividade;
import pi2.example.back_end.Modelo.CategoriaAtividade;
import pi2.example.back_end.Modelo.Funcionario;
import pi2.example.back_end.db.Banco;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AtividadeDAOImpl implements AtividadeDAO {
    @Override
    public boolean salvar(Atividade a) {

        String sql = "INSERT INTO atividade (descricao, id_categoria, id_funcionario) VALUES (" +
                "'" + a.getDescricao() + "', " +
                a.getCategoria().getId() + ", " +
                a.getFuncionario().getId() + ")";

        return Banco.getCon().manipular(sql);
    }

    @Override
    public List<Atividade> listar() {

        List<Atividade> lista = new ArrayList<>();

        String sql = """
        SELECT a.id_atividade, a.descricao,
               c.id_categoria as cat_id, c.nome as cat_nome,
               f.id_funcionario as func_id, f.nome as func_nome
        FROM atividade a
        JOIN categoria c ON a.id_categoria = c.id_categoria
        JOIN funcionario f ON a.id_funcionario = f.id_funcionario
    """;

        ResultSet rs = Banco.getCon().consultar(sql);

        if (rs == null) {
            System.out.println("Erro ao listar atividades: " + Banco.getCon().getMensagemErro());
            return lista;
        }

        try {
            while (rs.next()) {

                CategoriaAtividade c = new CategoriaAtividade();
                c.setId(rs.getInt("cat_id"));
                c.setNome(rs.getString("cat_nome"));

                Funcionario f = new Funcionario();
                f.setId(rs.getInt("func_id"));
                f.setNome(rs.getString("func_nome"));

                Atividade a = new Atividade();
                a.setId(rs.getInt("id_atividade"));
                a.setDescricao(rs.getString("descricao"));
                a.setCategoria(c);
                a.setFuncionario(f);

                lista.add(a);
            }
        } catch (Exception e) {
            System.out.println("Erro ao percorrer atividades: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public List<Atividade> buscarPorNome(String nome) {

        List<Atividade> lista = new ArrayList<>();

        String sql = """
        SELECT a.id_atividade, a.descricao,
               c.id_categoria as cat_id, c.nome as cat_nome,
               f.id_funcionario as func_id, f.nome as func_nome
        FROM atividade a
        JOIN categoria c ON a.id_categoria = c.id_categoria
        JOIN funcionario f ON a.id_funcionario = f.id_funcionario
        WHERE a.descricao LIKE '%""" + nome + "%'";

        ResultSet rs = Banco.getCon().consultar(sql);

        if (rs == null) {
            System.out.println("Erro ao buscar atividades: " + Banco.getCon().getMensagemErro());
            return lista;
        }

        try {
            while (rs.next()) {

                CategoriaAtividade c = new CategoriaAtividade();
                c.setId(rs.getInt("cat_id"));
                c.setNome(rs.getString("cat_nome"));

                Funcionario f = new Funcionario();
                f.setId(rs.getInt("func_id"));
                f.setNome(rs.getString("func_nome"));

                Atividade a = new Atividade();
                a.setId(rs.getInt("id_atividade"));
                a.setDescricao(rs.getString("descricao"));
                a.setCategoria(c);
                a.setFuncionario(f);

                lista.add(a);
            }
        } catch (Exception e) {
            System.out.println("Erro ao percorrer busca de atividades: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public boolean excluir(int id) {

        String sql = "DELETE FROM atividade WHERE id_atividade=" + id;

        return Banco.getCon().manipular(sql);
    }

    @Override
    public boolean atualizar(Atividade a) {

        String sql = "UPDATE atividade SET " +
                "descricao = '" + a.getDescricao() + "', " +
                "id_categoria = " + a.getCategoria().getId() + ", " +
                "id_funcionario = " + a.getFuncionario().getId() +
                " WHERE id_atividade = " + a.getId();

        return Banco.getCon().manipular(sql);
    }
}
