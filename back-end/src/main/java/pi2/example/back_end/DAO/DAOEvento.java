package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Cargo;
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

                    // =================================================
                    // EVENTO
                    // =================================================

                    "e.id_evento, " +
                    "e.nome, " +
                    "e.qtd, " +
                    "e.local, " +
                    "e.inicio, " +
                    "e.fim, " +

                    // =================================================
                    // CATEGORIA
                    // =================================================

                    "c.id_cat_evento, " +
                    "c.categoria, " +
                    "c.descricao, " +

                    // =================================================
                    // FUNCIONARIO
                    // =================================================

                    "f.id_funcionario, " +
                    "f.nome AS funcionario_nome, " +
                    "f.cpf, " +
                    "f.telefone, " +
                    "f.nis, " +
                    "f.nascimento, " +
                    "f.sexo, " +
                    "f.endereco, " +

                    // =================================================
                    // CARGO
                    // =================================================

                    "cg.id_cargo, " +
                    "cg.nome AS cargo_nome " +

                    "FROM evento e " +

                    // =================================================
                    // CATEGORIA
                    // =================================================

                    "INNER JOIN cat_evento c " +
                    "ON c.id_cat_evento = e.id_cat_evento " +

                    // =================================================
                    // FUNCIONARIO
                    // =================================================

                    "LEFT JOIN funcionario f " +
                    "ON f.id_funcionario = e.id_funcionario " +

                    // =================================================
                    // CARGO
                    // =================================================

                    "LEFT JOIN cargo cg " +
                    "ON cg.id_cargo = f.id_cargo ";

    // =====================================================
    // INSERT
    // =====================================================

    public Evento gravar(Evento entidade) {

        String sql =
                "INSERT INTO evento " +
                        "(nome, qtd, local, inicio, fim, id_cat_evento, id_funcionario) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt =
                     bd.prepararComRetorno(sql)) {

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

                stmt.setInt(
                        7,
                        entidade.getIdFuncionario()
                );

            } else {

                stmt.setNull(7, Types.INTEGER);
            }

            stmt.executeUpdate();

            try (ResultSet rs =
                         stmt.getGeneratedKeys()) {

                if (rs.next()) {
                    entidade.setIdEvento(
                            rs.getInt(1)
                    );
                }
            }

            return entidade;

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao gravar evento:"
            );

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // UPDATE
    // =====================================================

    public Evento alterar(Evento entidade) {

        String sql =
                "UPDATE evento SET " +
                        "nome=?, " +
                        "qtd=?, " +
                        "local=?, " +
                        "inicio=?, " +
                        "fim=?, " +
                        "id_cat_evento=?, " +
                        "id_funcionario=? " +
                        "WHERE id_evento=?";

        try (PreparedStatement stmt =
                     bd.preparar(sql)) {

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

            stmt.setInt(
                    6,
                    entidade.getIdCatEvento()
            );

            if (entidade.possuiFuncionario()) {

                stmt.setInt(
                        7,
                        entidade.getIdFuncionario()
                );

            } else {

                stmt.setNull(7, Types.INTEGER);
            }

            stmt.setInt(
                    8,
                    entidade.getIdEvento()
            );

            stmt.executeUpdate();

            return entidade;

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao alterar evento:"
            );

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // DELETE
    // =====================================================

    public boolean apagar(Evento entidade) {

        String sql =
                "DELETE FROM evento " +
                        "WHERE id_evento=?";

        try (PreparedStatement stmt =
                     bd.preparar(sql)) {

            stmt.setInt(
                    1,
                    entidade.getIdEvento()
            );

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao apagar evento:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // LISTAR
    // =====================================================

    public List<Evento> listar() {

        List<Evento> lista =
                new ArrayList<>();

        String sql =
                SQL_SELECT_BASE +
                        "ORDER BY e.id_evento";

        try (PreparedStatement stmt =
                     bd.preparar(sql);

             ResultSet rs =
                     stmt.executeQuery()) {

            while (rs.next()) {

                lista.add(
                        mapearEvento(rs)
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao listar eventos:"
            );

            e.printStackTrace();
        }

        return lista;
    }

    // =====================================================
    // BUSCAR POR ID
    // =====================================================

    public Evento buscarPorId(Integer id) {

        String sql =
                SQL_SELECT_BASE +
                        "WHERE e.id_evento=?";

        try (PreparedStatement stmt =
                     bd.preparar(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs =
                         stmt.executeQuery()) {

                if (rs.next()) {

                    return mapearEvento(rs);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao buscar evento por ID:"
            );

            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // BUSCAR POR NOME
    // =====================================================

    public List<Evento> buscarPorNome(String nome) {

        List<Evento> lista =
                new ArrayList<>();

        String sql =
                SQL_SELECT_BASE +
                        "WHERE UPPER(e.nome) LIKE UPPER(?) " +
                        "ORDER BY e.nome";

        try (PreparedStatement stmt =
                     bd.preparar(sql)) {

            stmt.setString(
                    1,
                    "%" + nome + "%"
            );

            try (ResultSet rs =
                         stmt.executeQuery()) {

                while (rs.next()) {

                    lista.add(
                            mapearEvento(rs)
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao buscar evento por nome:"
            );

            e.printStackTrace();
        }

        return lista;
    }

    // =====================================================
    // BUSCAR POR LOCAL
    // =====================================================

    public List<Evento> buscarPorLocal(String local) {

        List<Evento> lista =
                new ArrayList<>();

        String sql =
                SQL_SELECT_BASE +
                        "WHERE UPPER(e.local) LIKE UPPER(?) " +
                        "ORDER BY e.local";

        try (PreparedStatement stmt =
                     bd.preparar(sql)) {

            stmt.setString(
                    1,
                    "%" + local + "%"
            );

            try (ResultSet rs =
                         stmt.executeQuery()) {

                while (rs.next()) {

                    lista.add(
                            mapearEvento(rs)
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao buscar evento por local:"
            );

            e.printStackTrace();
        }

        return lista;
    }

    // =====================================================
    // BUSCAR POR PERIODO
    // =====================================================

    public List<Evento> buscarPorPeriodo(
            LocalDateTime inicioBusca,
            LocalDateTime fimBusca
    ) {

        List<Evento> lista =
                new ArrayList<>();

        String sql =
                SQL_SELECT_BASE +

                        "WHERE e.inicio <= ? " +
                        "AND e.fim >= ? " +

                        "ORDER BY e.inicio";

        try (PreparedStatement stmt =
                     bd.preparar(sql)) {

            stmt.setTimestamp(
                    1,
                    Timestamp.valueOf(fimBusca)
            );

            stmt.setTimestamp(
                    2,
                    Timestamp.valueOf(inicioBusca)
            );

            try (ResultSet rs =
                         stmt.executeQuery()) {

                while (rs.next()) {

                    lista.add(
                            mapearEvento(rs)
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao buscar eventos por período:"
            );

            e.printStackTrace();
        }

        return lista;
    }

    // =====================================================
    // MAPEAMENTO
    // =====================================================

    private Evento mapearEvento(ResultSet rs)
            throws SQLException {

        // =====================================================
        // CATEGORIA
        // =====================================================

        Cat_Evento categoria =
                new Cat_Evento(

                        rs.getInt(
                                "id_cat_evento"
                        ),

                        rs.getString(
                                "categoria"
                        ),

                        rs.getString(
                                "descricao"
                        )
                );

        // =====================================================
        // CARGO
        // =====================================================

        Cargo cargo = null;

        if (rs.getObject("id_cargo")
                != null) {

            cargo = new Cargo();

            cargo.setId(
                    rs.getInt("id_cargo")
            );

            cargo.setNome(
                    rs.getString("cargo_nome")
            );
        }

        // =====================================================
        // FUNCIONARIO
        // =====================================================

        Funcionario funcionario = null;

        if (rs.getObject("id_funcionario")
                != null) {

            funcionario =
                    new Funcionario();

            funcionario.setId(
                    rs.getInt(
                            "id_funcionario"
                    )
            );

            funcionario.setNome(
                    rs.getString(
                            "funcionario_nome"
                    )
            );

            funcionario.setCpf(
                    rs.getString("cpf")
            );

            funcionario.setTelefone(
                    rs.getString("telefone")
            );

            funcionario.setNis(
                    rs.getString("nis")
            );

            funcionario.setNascimento(
                    rs.getDate("nascimento")
            );

            funcionario.setSexo(
                    rs.getString("sexo")
            );

            funcionario.setEndereco(
                    rs.getString("endereco")
            );

            funcionario.setCargo(cargo);
        }

        // =====================================================
        // EVENTO
        // =====================================================

        return new Evento(

                rs.getInt("id_evento"),

                rs.getTimestamp("inicio") != null
                        ? rs.getTimestamp("inicio")
                        .toLocalDateTime()
                        : null,

                rs.getTimestamp("fim") != null
                        ? rs.getTimestamp("fim")
                        .toLocalDateTime()
                        : null,

                rs.getString("nome"),

                rs.getString("local"),

                rs.getInt("qtd"),

                categoria,

                funcionario
        );
    }
}