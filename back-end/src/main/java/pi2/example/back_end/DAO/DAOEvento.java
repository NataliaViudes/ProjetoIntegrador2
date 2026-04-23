package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Evento;
import pi2.example.back_end.db.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOEvento {

    private final Conexao bd;

    public DAOEvento(Conexao bd) {
        this.bd = bd;
    }

    // 🔹 INSERT
    public Evento gravar(Evento entidade) {
        String sql = "INSERT INTO EVENTO (nome, qtd, local, id_cat_evento, dia, hora_inicio, hora_fim, id_funcionario) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {

            stmt.setString(1, entidade.getNome());
            stmt.setInt(2, entidade.getQtd());
            stmt.setString(3, entidade.getLocal());
            stmt.setInt(4, entidade.getIdCatEvento());
            stmt.setDate(5, Date.valueOf(entidade.getData()));
            stmt.setTime(6, Time.valueOf(entidade.getHoraInicio()));
            stmt.setTime(7, Time.valueOf(entidade.getHoraFim()));

            if (entidade.getIdFuncionario() != null) {
                stmt.setInt(8, entidade.getIdFuncionario());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entidade.setId(rs.getInt(1));
            }

            return entidade;

        } catch (SQLException e) {
            System.out.println("Erro ao gravar evento: " + e.getMessage());
            return null;
        }
    }

    // 🔹 UPDATE
    public Evento alterar(Evento entidade) {
        String sql = "UPDATE EVENTO SET nome=?, qtd=?, local=?, id_cat_evento=?, dia=?, hora_inicio=?, hora_fim=?, id_funcionario=? WHERE id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, entidade.getNome());
            stmt.setInt(2, entidade.getQtd());
            stmt.setString(3, entidade.getLocal());
            stmt.setInt(4, entidade.getIdCatEvento());
            stmt.setDate(5, Date.valueOf(entidade.getData()));
            stmt.setTime(6, Time.valueOf(entidade.getHoraInicio()));
            stmt.setTime(7, Time.valueOf(entidade.getHoraFim()));

            if (entidade.getIdFuncionario() != null) {
                stmt.setInt(8, entidade.getIdFuncionario());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }

            stmt.setInt(9, entidade.getId());

            stmt.executeUpdate();
            return entidade;

        } catch (SQLException e) {
            System.out.println("Erro ao alterar evento: " + e.getMessage());
            return null;
        }
    }

    // 🔹 DELETE
    public boolean apagar(Evento entidade) {
        String sql = "DELETE FROM EVENTO WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, entidade.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao apagar evento: " + e.getMessage());
            return false;
        }
    }

    // 🔹 LISTAR TODOS
    public List<Evento> listar() {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT * FROM EVENTO";

        try (PreparedStatement stmt = bd.preparar(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar eventos: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 BUSCAR POR ID
    public Evento buscarPorId(int id) {
        String sql = "SELECT * FROM EVENTO WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapear(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar por ID: " + e.getMessage());
        }

        return null;
    }

    // 🔹 BUSCAR POR NOME
    public List<Evento> buscarPorNome(String nome) {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT * FROM EVENTO WHERE nome ILIKE ? ORDER BY nome ASC";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar por nome: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 BUSCAR POR LOCAL
    public List<Evento> buscarPorLocal(String local) {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT * FROM EVENTO WHERE local ILIKE ?  ORDER BY local ASC";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, "%" + local + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar por local: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 MAPEAMENTO (ResultSet → Objeto)
    private Evento mapear(ResultSet rs) throws SQLException {
        Evento e = new Evento();

        e.setId(rs.getInt("id"));
        e.setNome(rs.getString("nome"));
        e.setQtd(rs.getInt("qtd"));
        e.setLocal(rs.getString("local"));
        e.setIdCatEvento(rs.getInt("id_cat_evento"));

        Date data = rs.getDate("dia");
        if (data != null) {
            e.setData(data.toLocalDate());
        }

        Time hi = rs.getTime("hora_inicio");
        if (hi != null) {
            e.setHoraInicio(hi.toLocalTime());
        }

        Time hf = rs.getTime("hora_fim");
        if (hf != null) {
            e.setHoraFim(hf.toLocalTime());
        }

        int funcId = rs.getInt("id_funcionario");
        if (!rs.wasNull()) {
            e.setIdFuncionario(funcId);
        }
        return e;
    }
}
