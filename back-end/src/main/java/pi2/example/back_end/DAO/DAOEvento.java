package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Cat_Evento;
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

            if (entidade.getQtd() != null)
                stmt.setInt(2, entidade.getQtd());
            else
                stmt.setNull(2, Types.INTEGER);

            stmt.setString(3, entidade.getLocal());

            // 🔥 evita NullPointer
            if (entidade.getCategoria() != null && entidade.getCategoria().getId() != null)
                stmt.setInt(4, entidade.getCategoria().getId());
            else
                stmt.setNull(4, Types.INTEGER);

            if (entidade.getData() != null)
                stmt.setDate(5, Date.valueOf(entidade.getData()));
            else
                stmt.setNull(5, Types.DATE);

            if (entidade.getHoraInicio() != null)
                stmt.setTime(6, Time.valueOf(entidade.getHoraInicio()));
            else
                stmt.setNull(6, Types.TIME);

            if (entidade.getHoraFim() != null)
                stmt.setTime(7, Time.valueOf(entidade.getHoraFim()));
            else
                stmt.setNull(7, Types.TIME);

            if (entidade.getIdFuncionario() != null)
                stmt.setInt(8, entidade.getIdFuncionario());
            else
                stmt.setNull(8, Types.INTEGER);

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
    public Evento alterar(Evento entidade) {
        String sql = "UPDATE EVENTO SET nome=?, qtd=?, local=?, id_cat_evento=?, dia=?, hora_inicio=?, hora_fim=?, id_funcionario=? WHERE id=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, entidade.getNome());

            if (entidade.getQtd() != null)
                stmt.setInt(2, entidade.getQtd());
            else
                stmt.setNull(2, Types.INTEGER);

            stmt.setString(3, entidade.getLocal());

            if (entidade.getCategoria() != null && entidade.getCategoria().getId() != null)
                stmt.setInt(4, entidade.getCategoria().getId());
            else
                stmt.setNull(4, Types.INTEGER);

            if (entidade.getData() != null)
                stmt.setDate(5, Date.valueOf(entidade.getData()));
            else
                stmt.setNull(5, Types.DATE);

            if (entidade.getHoraInicio() != null)
                stmt.setTime(6, Time.valueOf(entidade.getHoraInicio()));
            else
                stmt.setNull(6, Types.TIME);

            if (entidade.getHoraFim() != null)
                stmt.setTime(7, Time.valueOf(entidade.getHoraFim()));
            else
                stmt.setNull(7, Types.TIME);

            if (entidade.getIdFuncionario() != null)
                stmt.setInt(8, entidade.getIdFuncionario());
            else
                stmt.setNull(8, Types.INTEGER);

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

    public List<Evento> listar() {
        List<Evento> lista = new ArrayList<>();

        String sql = "SELECT " +
                "e.id, e.nome, e.qtd, e.local, e.dia, e.hora_inicio, e.hora_fim, e.id_funcionario, " +
                "cat.id AS cat_id, cat.categoria, cat.descricao AS cat_descricao " +
                "FROM evento e " +
                "JOIN cat_evento cat ON cat.id = e.id_cat_evento " +
                "ORDER BY e.id";

        try (PreparedStatement stmt = bd.preparar(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Cat_Evento cat = new Cat_Evento(
                        rs.getInt("cat_id"),
                        rs.getString("categoria"),
                        rs.getString("cat_descricao")
                );

                Evento e = new Evento(
                        rs.getInt("id"),
                        rs.getDate("dia") != null ? rs.getDate("dia").toLocalDate() : null,
                        rs.getTime("hora_inicio") != null ? rs.getTime("hora_inicio").toLocalTime() : null,
                        rs.getTime("hora_fim") != null ? rs.getTime("hora_fim").toLocalTime() : null,
                        rs.getString("nome"),
                        rs.getString("local"),
                        rs.getInt("qtd"),
                        cat,
                        rs.getObject("id_funcionario") != null ? rs.getInt("id_funcionario") : null
                );

                lista.add(e);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar eventos: " + e.getMessage());
        }

        return lista;
    }

    public Evento buscarPorId(int id) {
        String sql = "SELECT " +
                "e.id, e.nome, e.qtd, e.local, e.dia, e.hora_inicio, e.hora_fim, e.id_funcionario, " +
                "cat.id AS cat_id, cat.categoria, cat.descricao AS cat_descricao " +
                "FROM evento e " +
                "JOIN cat_evento cat ON cat.id = e.id_cat_evento " +
                "WHERE e.id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Cat_Evento cat = new Cat_Evento(
                        rs.getInt("cat_id"),
                        rs.getString("categoria"),
                        rs.getString("cat_descricao")
                );

                Evento e = new Evento(
                        rs.getInt("id"),
                        rs.getDate("dia") != null ? rs.getDate("dia").toLocalDate() : null,
                        rs.getTime("hora_inicio") != null ? rs.getTime("hora_inicio").toLocalTime() : null,
                        rs.getTime("hora_fim") != null ? rs.getTime("hora_fim").toLocalTime() : null,
                        rs.getString("nome"),
                        rs.getString("local"),
                        rs.getInt("qtd"),
                        cat,
                        rs.getObject("id_funcionario") != null ? rs.getInt("id_funcionario") : null
                );

                return e;
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

        int qtd = rs.getInt("qtd");
        if (!rs.wasNull()) e.setQtd(qtd);

        e.setLocal(rs.getString("local"));

        int catId = rs.getInt("id_cat_evento");
        if (!rs.wasNull())
            e.setCategoria(new Cat_Evento(catId));

        Date data = rs.getDate("dia");
        if (data != null)
            e.setData(data.toLocalDate());

        Time hi = rs.getTime("hora_inicio");
        if (hi != null)
            e.setHoraInicio(hi.toLocalTime());

        Time hf = rs.getTime("hora_fim");
        if (hf != null)
            e.setHoraFim(hf.toLocalTime());

        int funcId = rs.getInt("id_funcionario");
        if (!rs.wasNull())
            e.setIdFuncionario(funcId);

        return e;
    }
}
