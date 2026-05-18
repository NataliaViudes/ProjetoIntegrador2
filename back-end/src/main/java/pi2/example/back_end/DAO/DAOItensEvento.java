package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Cat_Evento;
import pi2.example.back_end.Modelo.Estoque;
import pi2.example.back_end.Modelo.Evento;
import pi2.example.back_end.Modelo.Funcionario;
import pi2.example.back_end.Modelo.ItensEvento;
import pi2.example.back_end.Modelo.TipoEstoque;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOItensEvento {

    private final Conexao db;

    public DAOItensEvento(Conexao bd) {
        this.db = bd;
    }

    // =====================================================
    // INSERT
    // =====================================================

    public ItensEvento gravar(ItensEvento entidade) {

        String sql =
                "INSERT INTO itens_evento " +
                        "(id_estoque, id_evento, qtd) " +
                        "VALUES (?, ?, ?)";

        try (PreparedStatement stmt =
                     db.preparar(sql)) {

            stmt.setInt(1, entidade.getEstoqueId());
            stmt.setInt(2, entidade.getEventoId());
            stmt.setInt(3, entidade.getQtd());

            stmt.executeUpdate();

            return entidade;

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao gravar item:"
            );

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // UPDATE
    // =====================================================

    public ItensEvento alterar(ItensEvento entidade) {

        String sql =
                "UPDATE itens_evento SET qtd=? " +
                        "WHERE id_estoque=? " +
                        "AND id_evento=?";

        try (PreparedStatement stmt =
                     db.preparar(sql)) {

            stmt.setInt(1, entidade.getQtd());
            stmt.setInt(2, entidade.getEstoqueId());
            stmt.setInt(3, entidade.getEventoId());

            stmt.executeUpdate();

            return entidade;

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao alterar item:"
            );

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // DELETE ITEM
    // =====================================================

    public boolean apagar(ItensEvento entidade) {

        String sql =
                "DELETE FROM itens_evento " +
                        "WHERE id_estoque=? " +
                        "AND id_evento=?";

        try (PreparedStatement stmt =
                     db.preparar(sql)) {

            stmt.setInt(1, entidade.getEstoqueId());
            stmt.setInt(2, entidade.getEventoId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao apagar item:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // LIMPAR TODOS ITENS
    // =====================================================

    public boolean limparItens(Integer idEvento) {

        String sql =
                "DELETE FROM itens_evento " +
                        "WHERE id_evento=?";

        try (PreparedStatement stmt =
                     db.preparar(sql)) {

            stmt.setInt(1, idEvento);

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao limpar itens:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // SINCRONIZAR ITENS
    // =====================================================

    public boolean syncItensEvento(
            Integer eventoId,
            List<ItensEvento> novosItens
    ) {

        try {

            // =========================================
            // ITENS ATUAIS
            // =========================================

            List<ItensEvento> itensAtuais =
                    getPorIdEvento(eventoId);

            // =========================================
            // REMOVER ITENS EXCLUÍDOS
            // =========================================

            for (ItensEvento atual : itensAtuais) {

                boolean existe =
                        novosItens.stream()
                                .anyMatch(n ->
                                        n.getEstoqueId()
                                                .equals(
                                                        atual.getEstoqueId()
                                                )
                                );

                if (!existe) {

                    apagar(atual);
                }
            }

            // =========================================
            // INSERT / UPDATE
            // =========================================

            for (ItensEvento novo : novosItens) {

                ItensEvento atualBanco =
                        buscarPorChave(
                                eventoId,
                                novo.getEstoqueId()
                        );

                // =====================================
                // INSERT
                // =====================================

                if (atualBanco == null) {

                    gravar(novo);
                }

                // =====================================
                // UPDATE
                // =====================================

                else {

                    atualBanco.setQtd(
                            novo.getQtd()
                    );

                    alterar(atualBanco);
                }
            }

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Erro syncItensEvento:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // GET POR EVENTO
    // =====================================================

    public List<ItensEvento> getPorIdEvento(
            Integer idEvento
    ) {

        List<ItensEvento> result =
                new ArrayList<>();

        String sql =
                "SELECT " +

                        // EVENTO
                        "e.id_evento AS evento_id, " +
                        "e.nome, " +
                        "e.local, " +
                        "e.inicio, " +
                        "e.fim, " +
                        "e.qtd AS qtd_evento, " +
                        "e.id_funcionario, " +

                        // CATEGORIA
                        "cat.id_cat_evento AS cat_id, " +
                        "cat.categoria, " +
                        "cat.descricao AS cat_descricao, " +

                        // ITEM EVENTO
                        "item.qtd AS item_qtd, " +

                        // ESTOQUE
                        "est.id AS estoque_id, " +
                        "est.qtd AS estoque_qtd, " +
                        "est.descricao AS estoque_descricao, " +

                        // TIPO ESTOQUE
                        "te.id AS tipo_estoque_id, " +
                        "te.tipo " +

                        "FROM evento e " +

                        "JOIN cat_evento cat " +
                        "ON cat.id_cat_evento = e.id_cat_evento " +

                        "JOIN itens_evento item " +
                        "ON item.id_evento = e.id_evento " +

                        "JOIN estoque est " +
                        "ON est.id = item.id_estoque " +

                        "JOIN tipo_estoque te " +
                        "ON te.id = est.id_tipo_estoque " +

                        "WHERE e.id_evento = ?";

        try (PreparedStatement stmt =
                     db.preparar(sql)) {

            stmt.setInt(1, idEvento);

            ResultSet rs =
                    stmt.executeQuery();

            while (rs.next()) {

                // =====================================
                // CATEGORIA
                // =====================================

                Cat_Evento cat =
                        new Cat_Evento(
                                rs.getInt("cat_id"),
                                rs.getString("categoria"),
                                rs.getString("cat_descricao")
                        );

                // =====================================
                // FUNCIONARIO
                // =====================================

                Funcionario funcionario = null;

                if (rs.getObject(
                        "id_funcionario"
                ) != null) {

                    funcionario =
                            new Funcionario();

                    funcionario.setId(
                            rs.getInt(
                                    "id_funcionario"
                            )
                    );
                }

                // =====================================
                // EVENTO
                // =====================================

                Evento eve =
                        new Evento(

                                rs.getInt("evento_id"),

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
                                rs.getInt("qtd_evento"),
                                cat,
                                funcionario
                        );

                // =====================================
                // TIPO ESTOQUE
                // =====================================

                TipoEstoque tipo =
                        new TipoEstoque();

                tipo.setId(
                        rs.getInt(
                                "tipo_estoque_id"
                        )
                );

                tipo.setTipo(
                        rs.getString("tipo")
                );

                // =====================================
                // ESTOQUE
                // =====================================

                Estoque est =
                        new Estoque();

                est.setId(
                        rs.getInt("estoque_id")
                );

                est.setQtd(
                        rs.getInt("estoque_qtd")
                );

                est.setDescricao(
                        rs.getString(
                                "estoque_descricao"
                        )
                );

                est.setTipo(tipo);

                // =====================================
                // ITEM EVENTO
                // =====================================

                ItensEvento item =
                        new ItensEvento(
                                est,
                                eve,
                                rs.getInt("item_qtd")
                        );

                result.add(item);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao buscar itens do evento:"
            );

            e.printStackTrace();
        }

        return result;
    }

    // =====================================================
    // BUSCAR ITEM
    // =====================================================

    public ItensEvento buscarItem(
            Integer idEvento,
            Integer idEstoque
    ) {

        String sql =
                "SELECT " +
                        "item.qtd AS item_qtd, " +

                        "est.id AS estoque_id, " +
                        "est.qtd AS estoque_qtd, " +
                        "est.descricao, " +

                        "te.id AS tipo_id, " +
                        "te.tipo " +

                        "FROM itens_evento item " +

                        "JOIN estoque est " +
                        "ON est.id = item.id_estoque " +

                        "JOIN tipo_estoque te " +
                        "ON te.id = est.id_tipo_estoque " +

                        "WHERE item.id_evento=? " +
                        "AND item.id_estoque=?";

        try (PreparedStatement stmt =
                     db.preparar(sql)) {

            stmt.setInt(1, idEvento);
            stmt.setInt(2, idEstoque);

            ResultSet rs =
                    stmt.executeQuery();

            if (rs.next()) {

                TipoEstoque tipo =
                        new TipoEstoque();

                tipo.setId(
                        rs.getInt("tipo_id")
                );

                tipo.setTipo(
                        rs.getString("tipo")
                );

                Estoque estoque =
                        new Estoque();

                estoque.setId(
                        rs.getInt("estoque_id")
                );

                estoque.setQtd(
                        rs.getInt("estoque_qtd")
                );

                estoque.setDescricao(
                        rs.getString("descricao")
                );

                estoque.setTipo(tipo);

                Evento evento =
                        new Evento();

                evento.setIdEvento(idEvento);

                return new ItensEvento(
                        estoque,
                        evento,
                        rs.getInt("item_qtd")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // BUSCAR POR CHAVE
    // =====================================================

    public ItensEvento buscarPorChave(
            Integer idEvento,
            Integer idEstoque
    ) {

        String sql =
                "SELECT qtd " +
                        "FROM itens_evento " +
                        "WHERE id_evento=? " +
                        "AND id_estoque=?";

        try (PreparedStatement stmt =
                     db.preparar(sql)) {

            stmt.setInt(1, idEvento);
            stmt.setInt(2, idEstoque);

            ResultSet rs =
                    stmt.executeQuery();

            if (rs.next()) {

                Evento evento =
                        new Evento();

                evento.setIdEvento(idEvento);

                Estoque estoque =
                        new Estoque();

                estoque.setId(idEstoque);

                return new ItensEvento(
                        estoque,
                        evento,
                        rs.getInt("qtd")
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erro buscarPorChave"
            );

            e.printStackTrace();
        }

        return null;
    }
}