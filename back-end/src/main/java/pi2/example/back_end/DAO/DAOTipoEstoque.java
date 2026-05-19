package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.TipoEstoque;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOTipoEstoque {

    private final Conexao bd;

    public DAOTipoEstoque(Conexao bd) {
        this.bd = bd;
    }


    public TipoEstoque gravar(TipoEstoque entidade) {
        String sql = "INSERT INTO TIPO_ESTOQUE ( tipo) VALUES (?)";

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {

            stmt.setString(1, entidade.getTipo());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                entidade.setId(id);
            }
            return entidade;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }



    public TipoEstoque alterar(TipoEstoque entidade) {
        String sql = "UPDATE TIPO_ESTOQUE SET tipo = ?  WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, entidade.getTipo());
            stmt.setInt(2, entidade.getId());
            stmt.executeUpdate();

            return entidade;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }


    public boolean apagar(TipoEstoque entidade) {
        String sql = "DELETE FROM TIPO_ESTOQUE WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, entidade.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }


    public TipoEstoque getPorId(Integer id) {
        TipoEstoque eve = null;
        String sql = "SELECT * FROM Tipo_Estoque WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                eve = new TipoEstoque(
                        rs.getInt("id"),
                        rs.getString("tipo")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return eve;
    }



    public List<TipoEstoque> buscarPorTipo(String tipo) {
        List<TipoEstoque> lista = new ArrayList<>();
        String sql;

        // regra: se vazio ou null → traz tudo
        if (tipo == null || tipo.isEmpty()) {
            sql = "SELECT * FROM TIPO_ESTOQUE ORDER BY tipo ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new TipoEstoque(
                            rs.getInt("id"),
                            rs.getString("tipo")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }

        } else {
            sql = "SELECT * FROM TIPO_ESTOQUE WHERE tipo ILIKE '%' || ? || '%' ORDER BY tipo ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                stmt.setString(1, tipo);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new TipoEstoque(
                            rs.getInt("id"),
                            rs.getString("tipo")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }
        }
        return lista;
    }


}