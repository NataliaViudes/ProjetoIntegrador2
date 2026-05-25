package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.OrdemJudicial;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOOrdemJudicial {

    private final Conexao bd;

    public DAOOrdemJudicial(Conexao bd) {
        this.bd = bd;
    }

    public OrdemJudicial gravar(OrdemJudicial o) {

        String sql = "INSERT INTO ordem_judicial (beneficiario_id, possui_ordem, descricao) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {

            stmt.setInt(1, o.getBeneficiarioId());
            stmt.setBoolean(2, o.getPossuiOrdem());
            stmt.setString(3, o.getDescricao());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) o.setId(rs.getInt(1));

            return o;

        } catch (SQLException e) {
            System.out.println("Erro insert ordem: " + e);
            return null;
        }
    }

    public OrdemJudicial alterar(OrdemJudicial o) {

        String sql = "UPDATE ordem_judicial SET possui_ordem=?, descricao=? WHERE beneficiario_id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setBoolean(1, o.getPossuiOrdem());
            stmt.setString(2, o.getDescricao());
            stmt.setInt(3, o.getBeneficiarioId());

            stmt.executeUpdate();

            return o;

        } catch (SQLException e) {
            System.out.println("Erro update ordem: " + e);
            return null;
        }
    }

    public boolean apagarPorBeneficiario(Integer beneficiarioId) {

        String sql = "DELETE FROM ordem_judicial WHERE beneficiario_id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, beneficiarioId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro delete ordem: " + e);
            return false;
        }
    }

    public OrdemJudicial getByBeneficiario(Integer beneficiarioId) {

        String sql = "SELECT * FROM ordem_judicial WHERE beneficiario_id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, beneficiarioId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new OrdemJudicial(
                        rs.getInt("id"),
                        rs.getInt("beneficiario_id"),
                        rs.getBoolean("possui_ordem"),
                        rs.getString("descricao")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro get ordem: " + e);
        }

        return null;
    }


    public List<OrdemJudicial> getAll() {

        List<OrdemJudicial> lista = new ArrayList<>();

        String sql = "SELECT * FROM ordem_judicial";

        try (PreparedStatement stmt = bd.preparar(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                OrdemJudicial o = new OrdemJudicial();

                o.setId(rs.getInt("id"));
                o.setBeneficiarioId(rs.getInt("beneficiario_id"));
                o.setPossuiOrdem(rs.getBoolean("possui_ordem"));
                o.setDescricao(rs.getString("descricao"));

                lista.add(o);
            }

        } catch (Exception e) {
            System.out.println("Erro getAll ordem: " + e);
        }

        return lista;
    }
}