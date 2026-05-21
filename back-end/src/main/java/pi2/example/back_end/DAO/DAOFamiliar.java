package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Familiar;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOFamiliar {
    private final Conexao bd;
    public DAOFamiliar(Conexao bd){
        this.bd=bd;
    }

    public Familiar gravar(Familiar entidade){
        String sql = """
            INSERT INTO familiar
            (id_beneficiario, nome, parentesco, profissao, renda, telefone)
            VALUES (?,?,?,?,?,?)
            RETURNING id_familiar
            """;

        try (PreparedStatement stmt = bd.preparar(sql)){
            stmt.setInt(1, entidade.getIdBeneficiario());
            stmt.setString(2, entidade.getNome());
            stmt.setString(3, entidade.getParentesco());
            stmt.setString(4, entidade.getProfissao());
            stmt.setDouble(5, entidade.getRenda());
            stmt.setString(6, entidade.getTelefone());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                entidade.setId(rs.getInt("id_familiar"));
            }

            return entidade;
        } catch (SQLException e){
            System.out.println("Erro: " + e);
            return null;
        }
    }

    public Familiar alterar(Familiar entidade){
        String sql = "UPDATE familiar SET nome=?, parentesco=?, profissao=?, renda=?, telefone=? WHERE id_familiar=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setString(1, entidade.getNome());
            stmt.setString(2, entidade.getParentesco());
            stmt.setString(3, entidade.getProfissao());
            stmt.setDouble(4, entidade.getRenda());
            stmt.setString(5, entidade.getTelefone());
            stmt.setInt(6, entidade.getId());
            stmt.executeUpdate();

            return entidade;
        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }

    public boolean apagar(Familiar entidade){
        String sql = "DELETE FROM familiar WHERE id_familiar = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, entidade.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }

    public Familiar mapFamiliar(ResultSet rs) throws SQLException{
        Familiar f = new Familiar();

        f.setId(rs.getInt("id_familiar"));
        f.setNome(rs.getString("nome"));
        f.setParentesco(rs.getString("parentesco"));
        f.setProfissao(rs.getString("profissao"));
        f.setRenda(rs.getDouble("renda"));
        f.setTelefone(rs.getString("telefone"));
        f.setIdBeneficiario(rs.getInt("id_beneficiario"));

        return f;
    }

    public Familiar get(int id){
        Familiar familiar = null;

        String sql = "SELECT * FROM familiar WHERE id_familiar = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                familiar=mapFamiliar(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return familiar;
    }

    public List<Familiar> getAll(){
        List<Familiar> lista = new ArrayList<>();

        String sql = "SELECT * FROM familiar";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapFamiliar(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Familiar> buscarPorNome(String nome){
        List<Familiar> lista = new ArrayList<>();
        String sql;

        if (nome == null || nome.isEmpty()) {
            sql = "SELECT * FROM familiar";
        } else {
            sql = "SELECT * FROM familiar WHERE nome ILIKE '%' || ? || '%'";
        }

        try (PreparedStatement stmt = bd.preparar(sql)) {

            if (nome != null && !nome.isEmpty()) {
                stmt.setString(1, nome);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapFamiliar(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return lista;
    }

    public List<Familiar> getByBeneficiario(Integer idBeneficiario){

        List<Familiar> lista = new ArrayList<>();

        String sql = """
            SELECT * FROM familiar
            WHERE id_beneficiario = ?
            """;

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, idBeneficiario);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapFamiliar(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return lista;
    }
}
