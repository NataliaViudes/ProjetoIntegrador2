package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Remedio;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAORemedio {

    private final Conexao bd;

    public DAORemedio(Conexao bd) {
        this.bd = bd;
    }

    // -------------------- INSERT --------------------
    public Remedio gravar(Remedio r) {

        String sql = "INSERT INTO REMEDIO (nome, descricao) VALUES (?, ?)";

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {

            stmt.setString(1, r.getNome());
            stmt.setString(2, r.getDescricao());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                r.setId(rs.getInt(1));
            }

            return r;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }

    // -------------------- UPDATE --------------------
    public Remedio alterar(Remedio r) {

        String sql = "UPDATE REMEDIO SET nome=?, descricao=? WHERE id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, r.getNome());
            stmt.setString(2, r.getDescricao());
            stmt.setInt(3, r.getId());

            stmt.executeUpdate();

            return r;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }

    // -------------------- DELETE --------------------
    public boolean apagar(Remedio r) {

        String sql = "DELETE FROM REMEDIO WHERE id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, r.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }

    // -------------------- GET BY ID --------------------
    public Remedio get(Integer id) {

        Remedio r = null;

        String sql = "SELECT * FROM REMEDIO WHERE id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                r = new Remedio(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return r;
    }

    // -------------------- BUSCAR POR NOME --------------------
    public List<Remedio> buscarPorNome(String nome) {

        List<Remedio> lista = new ArrayList<>();

        String sql;

        if (nome == null || nome.isEmpty()) {
            sql = "SELECT * FROM REMEDIO ORDER BY nome ASC";
        } else {
            sql = "SELECT * FROM REMEDIO WHERE nome ILIKE '%' || ? || '%' ORDER BY nome ASC";
        }

        try (PreparedStatement stmt = bd.preparar(sql)) {

            if (nome != null && !nome.isEmpty()) {
                stmt.setString(1, nome);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(get(rs.getInt("id")));
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return lista;
    }

    // -------------------- BUSCAR POR DESCRICAO --------------------
    public List<Remedio> buscarPorDescricao(String descricao) {

        List<Remedio> lista = new ArrayList<>();

        String sql;

        if (descricao == null || descricao.isEmpty()) {
            sql = "SELECT * FROM REMEDIO ORDER BY descricao ASC";
        } else {
            sql = "SELECT * FROM REMEDIO WHERE descricao ILIKE '%' || ? || '%' ORDER BY descricao ASC";
        }

        try (PreparedStatement stmt = bd.preparar(sql)) {

            if (descricao != null && !descricao.isEmpty()) {
                stmt.setString(1, descricao);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(get(rs.getInt("id")));
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return lista;
    }

    // -------------------- GET ALL --------------------
    public List<Remedio> getAll() {

        List<Remedio> lista = new ArrayList<>();

        String sql = "SELECT * FROM REMEDIO ORDER BY nome";

        try {

            ResultSet rs = bd.consultar(sql);

            while (rs.next()) {

                Remedio r = new Remedio();

                r.setId(rs.getInt("id"));
                r.setNome(rs.getString("nome"));
                r.setDescricao(rs.getString("descricao"));

                lista.add(r);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar remédios: " + e.getMessage());
        }

        return lista;
    }

    public List<Remedio> listar() {
        return getAll();
    }

    public Remedio buscarPorId(Integer id) {
        return get(id);
    }
}