package pi2.example.back_end.DAO;


import pi2.example.back_end.Modelo.VincularBeneficiario;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOVincularBeneficiario {
    private final Conexao bd;

    public DAOVincularBeneficiario(Conexao bd) {
        this.bd = bd;
    }

    // -------------------- INSERT --------------------
    public VincularBeneficiario gravar(VincularBeneficiario vb) {

        String sql = "INSERT INTO vincularbeneficiario (idBeneficiario, idAgendamento) VALUES (?, ?)";

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {
            stmt.setInt(1, vb.getIdBeneficiario());
            stmt.setInt(2, vb.getIdAgendamento());

            stmt.executeUpdate();

            return vb;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }

    // -------------------- DELETE --------------------
    public boolean apagar(VincularBeneficiario vb) {

        String sql = "DELETE FROM VINCULARBENEFICIARIO WHERE idAgendamento=? AND idBeneficiario=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, vb.getIdAgendamento());
            stmt.setInt(2, vb.getIdBeneficiario());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }

    // -------------------- GET ALL --------------------
    public List<VincularBeneficiario> getAll() {

        List<VincularBeneficiario> lista = new ArrayList<>();

        String sql = "SELECT * FROM vincularbeneficiario ORDER BY nome ASC";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                lista.add(new VincularBeneficiario(
                        rs.getInt("idAgendamento"),
                        rs.getInt("idBeneficiario")));
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return lista;
    }

    // -------------------- GET BY ID AGENDAMENTO --------------------
    public List<VincularBeneficiario> getByIdAgendamento(Integer id) {
        List<VincularBeneficiario> lvb = new ArrayList<>();
        VincularBeneficiario vb = null;
        String sql = "SELECT * FROM vincularbeneficiario WHERE idAgendamento=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lvb.add(vb = new VincularBeneficiario(
                        rs.getInt("idAgendamento"),
                        rs.getInt("idBeneficiario")));
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return lvb;
    }

    // -------------------- GET ELEMENTO --------------------
    public VincularBeneficiario BuscaElemento(VincularBeneficiario vb){

        String sql = "SELECT FROM vincularbeneficiario WHERE idAgendamento=? AND idBeneficiario=?";
        VincularBeneficiario result = null;

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, vb.getIdAgendamento());
            stmt.setInt(2, vb.getIdBeneficiario());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                result = new VincularBeneficiario(
                        rs.getInt("idAgendamento"),
                        rs.getInt("idBeneficiario"));
            }

            return result;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return result;
        }
    }

    public boolean apagarPorAgendamento(Integer idAgendamento) {

        String sql = "DELETE FROM vincularbeneficiario WHERE idAgendamento=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, idAgendamento);
            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }

}
