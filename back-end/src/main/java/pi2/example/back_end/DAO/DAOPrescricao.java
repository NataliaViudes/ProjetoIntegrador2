package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Beneficiario;
import pi2.example.back_end.Modelo.Prescricao;
import pi2.example.back_end.Modelo.Remedio;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOPrescricao {

    private final Conexao bd;

    public DAOPrescricao(Conexao bd) {
        this.bd = bd;
    }

    // -------------------- INSERT --------------------
    public Prescricao gravar(Prescricao p) {

        String sql = "INSERT INTO PRESCRICAO " +
                "(dosagem, quantidade, horario, idbeneficiario, idremedio, intervalo) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {

            stmt.setString(1, p.getDosagem());
            stmt.setInt(2, p.getQuantidade());
            stmt.setDate(3, p.getHorario());
            stmt.setInt(4, p.getIdBeneficiario());
            stmt.setInt(5, p.getIdRemedio());
            stmt.setInt(6, p.getIntervalo());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                p.setId(rs.getInt(1));
            }

            return p;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }

    // -------------------- UPDATE --------------------
    public Prescricao alterar(Prescricao p) {

        String sql = "UPDATE PRESCRICAO SET " +
                "dosagem=?, quantidade=?, horario=?, idbeneficiario=?, idremedio=?, intervalo=? " +
                "WHERE id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, p.getDosagem());
            stmt.setInt(2, p.getQuantidade());
            stmt.setDate(3, p.getHorario());
            stmt.setInt(4, p.getIdBeneficiario());
            stmt.setInt(5, p.getIdRemedio());
            stmt.setInt(6, p.getIntervalo());
            stmt.setInt(7, p.getId());

            stmt.executeUpdate();

            return p;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }

    // -------------------- DELETE --------------------
    public boolean apagar(Prescricao p) {

        String sql = "DELETE FROM PRESCRICAO WHERE id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, p.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }

    // -------------------- GET BY ID --------------------
    public Prescricao get(Integer id) {

        Prescricao p = null;

        String sql = "SELECT * FROM PRESCRICAO WHERE id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Beneficiario beneficiario = new Beneficiario();
                beneficiario.setId(rs.getInt("idbeneficiario"));

                Remedio remedio = new Remedio();
                remedio.setId(rs.getInt("idremedio"));

                p = new Prescricao(
                        rs.getInt("id"),
                        rs.getString("dosagem"),
                        rs.getInt("quantidade"),
                        rs.getDate("horario"),
                        beneficiario,
                        remedio,
                        rs.getInt("intervalo")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return p;
    }

    // -------------------- BUSCAR POR BENEFICIARIO --------------------
    public List<Prescricao> buscarPorBeneficiario(Integer idBeneficiario) {

        List<Prescricao> lista = new ArrayList<>();

        String sql;

        if (idBeneficiario == null || idBeneficiario <= 0) {
            sql = "SELECT * FROM PRESCRICAO ORDER BY id ASC";
        } else {
            sql = "SELECT * FROM PRESCRICAO WHERE idbeneficiario=? ORDER BY id ASC";
        }

        try (PreparedStatement stmt = bd.preparar(sql)) {

            if (idBeneficiario != null && idBeneficiario > 0) {
                stmt.setInt(1, idBeneficiario);
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

    // -------------------- BUSCAR POR REMEDIO --------------------
    public List<Prescricao> buscarPorRemedio(Integer idRemedio) {

        List<Prescricao> lista = new ArrayList<>();

        String sql;

        if (idRemedio == null || idRemedio <= 0) {
            sql = "SELECT * FROM PRESCRICAO ORDER BY id ASC";
        } else {
            sql = "SELECT * FROM PRESCRICAO WHERE idremedio=? ORDER BY id ASC";
        }

        try (PreparedStatement stmt = bd.preparar(sql)) {

            if (idRemedio != null && idRemedio > 0) {
                stmt.setInt(1, idRemedio);
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
    public List<Prescricao> getAll() {

        List<Prescricao> lista = new ArrayList<>();

        String sql = "SELECT * FROM PRESCRICAO ORDER BY id";

        try {

            ResultSet rs = bd.consultar(sql);

            while (rs.next()) {

                lista.add(get(rs.getInt("id")));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar prescrições: " + e.getMessage());
        }

        return lista;
    }

    // -------------------- LISTAR --------------------
    public List<Prescricao> listar() {
        return getAll();
    }

    // -------------------- BUSCAR POR ID --------------------
    public Prescricao buscarPorId(Integer id) {
        return get(id);
    }
}