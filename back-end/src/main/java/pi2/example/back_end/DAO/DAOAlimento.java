package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Alimento;
import pi2.example.back_end.db.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOAlimento {

    private final Conexao bd;

    public DAOAlimento(Conexao bd) {
        this.bd = bd;
    }

    public Alimento gravar(Alimento entidade) {
        String sql = "INSERT INTO ALIMENTO(nome,tipo,descricao) VALUES(?,?,?)";

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {

            stmt.setString(1, entidade.getNome());
            stmt.setString(2, entidade.getTipo());
            stmt.setString(3, entidade.getDescricao());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entidade.setId(rs.getInt(1));
            }
            return entidade;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }

    public Alimento alterar(Alimento entidade) {
        String sql = "UPDATE ALIMENTO SET nome=?, tipo=?, descricao=? WHERE id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, entidade.getNome());
            stmt.setString(2, entidade.getTipo());
            stmt.setString(3, entidade.getDescricao());
            stmt.setInt(4, entidade.getId());
            stmt.executeUpdate();

            return entidade;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }

    public boolean apagar(Alimento entidade) {
        String sql = "DELETE FROM ALIMENTO WHERE id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, entidade.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }

    public Alimento getPorId(Integer id) {
        String sql = "SELECT * FROM ALIMENTO WHERE id=?";
        Alimento a = null;

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                a = new Alimento(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getString("descricao")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return a;
    }

    public List<Alimento> buscarPorTipo(String tipo) {
        List<Alimento> lista = new ArrayList<>();

        if (tipo == null) tipo = "";

        String sql = "SELECT * FROM ALIMENTO WHERE tipo ILIKE ? ORDER BY tipo ASC";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, "%" + tipo + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Alimento(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getString("descricao")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return lista;
    }

    public List<Alimento> buscarPorDescricao(String descricao) {
        List<Alimento> lista = new ArrayList<>();

        if (descricao == null) descricao = "";

        String sql = "SELECT * FROM ALIMENTO WHERE descricao ILIKE ? ORDER BY descricao ASC";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, "%" + descricao + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Alimento(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getString("descricao")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return lista;
    }
}