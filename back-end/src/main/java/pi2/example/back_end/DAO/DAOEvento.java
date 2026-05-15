package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Cat_Evento;
import pi2.example.back_end.Modelo.Evento;
import pi2.example.back_end.Modelo.Funcionario;
import pi2.example.back_end.db.Conexao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DAOEvento {

    private final Conexao bd;

    public DAOEvento(Conexao bd) {
        this.bd = bd;
    }

    // =====================================================
    // SELECT BASE
    // =====================================================

    private static final String SQL_SELECT_BASE =
            "SELECT " +
                    "e.id_evento, " +
                    "e.nome, " +
                    "e.qtd, " +
                    "e.local, " +
                    "e.inicio, " +
                    "e.fim, " +
                    "e.id_funcionario, " +

                    "c.id_cat_evento, " +
                    "c.categoria, " +
                    "c.descricao " +

                    "FROM EVENTO e " +

                    "JOIN CAT_EVENTO c " +
                    "ON c.id_cat_evento = e.id_cat_evento ";

    // =====================================================
    // INSERT
    // =====================================================

    public Evento gravar(Evento entidade) {

        String sql =
                "INSERT INTO EVENTO " +
                        "(nome, qtd, local, inicio, fim, id_cat_evento, id_funcionario) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {

            stmt.setString(1, entidade.getNome());
            stmt.setInt(2, entidade.getQtd());
            stmt.setString(3, entidade.getLocal());

            stmt.setTimestamp(
                    4,
                    entidade.getInicio() != null
                            ? Timestamp.valueOf(entidade.getInicio())
                            : null
            );

            stmt.setTimestamp(
                    5,
                    entidade.getFim() != null
                            ? Timestamp.valueOf(entidade.getFim())
                            : null
            );

            stmt.setInt(6, entidade.getIdCatEvento());

            if (entidade.possuiFuncionario()) {

                stmt.setInt(7, entidade.getIdFuncionario());

            } else {

                stmt.setNull(7, Types.INTEGER);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {
                    entidade.setIdEvento(rs.getInt(1));
                }
            }

            return entidade;

        } catch (SQLException e) {

            System.out.println("Erro ao gravar evento:");
            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // UPDATE
    // =====================================================

    public Evento alterar(Evento entidade) {

        String sql =
                "UPDATE EVENTO SET " +
                        "nome=?, " +
                        "qtd=?, " +
                        "local=?, " +
                        "inicio=?, " +
                        "fim=?, " +
                        "id_cat_evento=?, " +
                        "id_funcionario=? " +
                        "WHERE id_evento=?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, entidade.getNome());
            stmt.setInt(2, entidade.getQtd());
            stmt.setString(3, entidade.getLocal());

            stmt.setTimestamp(
                    4,
                    entidade.getInicio() != null
                            ? Timestamp.valueOf(entidade.getInicio())
                            : null
            );

            stmt.setTimestamp(
                    5,
                    entidade.getFim() != null
                            ? Timestamp.valueOf(entidade.getFim())
                            : null
            );

            stmt.setInt(6, entidade.getIdCatEvento());

            if (entidade.possuiFuncionario()) {

                stmt.setInt(7, entidade.getIdFuncionario());

            } else {

                stmt.setNull(7, Types.INTEGER);
            }

            stmt.setInt(8, entidade.getIdEvento());

            stmt.executeUpdate();

            return entidade;

        } catch (SQLException e) {

            System.out.println("Erro ao alterar evento:");
            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // DELETE
    // =====================================================

    public boolean apagar(Evento entidade) {

        String sql =
                "DELETE FROM EVENTO " +
                        "WHERE id_evento = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, entidade.getIdEvento());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println("Erro ao apagar evento:");
            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // LISTAR
    // =====================================================

    public List<Evento> listar() {

        List<Evento> lista = new ArrayList<>();

        String sql =
                SQL_SELECT_BASE +
                        "ORDER BY e.id_evento";

        try (PreparedStatement stmt = bd.preparar(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {

            System.out.println("Erro ao listar eventos:");
            e.printStackTrace();
        }

        return lista;
    }

    // =====================================================
    // BUSCAR POR ID
    // =====================================================

    public Evento buscarPorId(int id) {

        String sql =
                SQL_SELECT_BASE +
                        "WHERE e.id_evento = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapear(rs);
                }
            }

        } catch (SQLException e) {

            System.out.println("Erro ao buscar evento por ID:");
            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // BUSCAR POR NOME
    // =====================================================

    public List<Evento> buscarPorNome(String nome) {

        List<Evento> lista = new ArrayList<>();

        String sql =
                SQL_SELECT_BASE +
                        "WHERE e.nome ILIKE ? " +
                        "ORDER BY e.nome";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }

        } catch (SQLException e) {

            System.out.println("Erro ao buscar evento por nome:");
            e.printStackTrace();
        }

        return lista;
    }

    // =====================================================
    // BUSCAR POR LOCAL
    // =====================================================

    public List<Evento> buscarPorLocal(String local) {

        List<Evento> lista = new ArrayList<>();

        String sql =
                SQL_SELECT_BASE +
                        "WHERE e.local ILIKE ? " +
                        "ORDER BY e.local";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, "%" + local + "%");

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }

        } catch (SQLException e) {

            System.out.println("Erro ao buscar evento por local:");
            e.printStackTrace();
        }

        return lista;
    }

    // =====================================================
    // MAPEAMENTO
    // =====================================================

    private Evento mapear(ResultSet rs) throws SQLException {

        Cat_Evento categoria = new Cat_Evento(
                rs.getInt("id_cat_evento"),
                rs.getString("categoria"),
                rs.getString("descricao")
        );

        Funcionario funcionario = null;

        if (rs.getObject("id_funcionario") != null) {

            funcionario = new Funcionario();
            funcionario.setId(
                    rs.getInt("id_funcionario")
            );
        }

        return new Evento(
                rs.getInt("id_evento"),

                rs.getTimestamp("inicio") != null
                        ? rs.getTimestamp("inicio").toLocalDateTime()
                        : null,

                rs.getTimestamp("fim") != null
                        ? rs.getTimestamp("fim").toLocalDateTime()
                        : null,

                rs.getString("nome"),
                rs.getString("local"),
                rs.getInt("qtd"),
                categoria,
                funcionario
        );
    }



// =====================================================
// BUSCAR POR PERÍODO
// =====================================================

    public List<Evento> buscarPorPeriodo(
            LocalDateTime inicioBusca,
            LocalDateTime fimBusca
    ) {

        List<Evento> lista = new ArrayList<>();

        String sql =
                SQL_SELECT_BASE +

                        "WHERE e.inicio <= ? " +
                        "AND e.fim >= ? " +

                        "ORDER BY e.inicio";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(fimBusca));
            stmt.setTimestamp(2, Timestamp.valueOf(inicioBusca));

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }

        } catch (SQLException e) {

            System.out.println("Erro ao buscar eventos por período:");
            e.printStackTrace();
        }

        return lista;
    }


}
