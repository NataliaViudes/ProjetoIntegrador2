package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.*;
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


    public ItensEvento gravar(ItensEvento entidade) {
        String sql = "INSERT INTO Itens_Evento(id_estoque,id_evento,qtd) VALUES(?,?,?)";

        try (PreparedStatement stmt = db.prepararComRetorno(sql)) {

            stmt.setInt(1, entidade.getEstoqueId());
            stmt.setInt(2, entidade.getEventoId());
            stmt.setInt(3, entidade.getQtd());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
            }
            return entidade;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }

    public ItensEvento alterar(ItensEvento entidade) {
        String sql = "UPDATE itens_Evento SET qtd=?  WHERE id_estoque = ? AND id_evento = ?";

        try (PreparedStatement stmt = db.preparar(sql)) {

           stmt.setInt(1,entidade.getQtd());
           stmt.setInt(2,entidade.getEstoqueId());
           stmt.setInt(3,entidade.getEventoId());

            stmt.executeUpdate();
            return entidade;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }


    public boolean apagar(ItensEvento entidade) {
        String sql = "DELETE FROM Itens_evento WHERE id_estoque = ? AND id_evento = ?";

        try (PreparedStatement stmt = db.preparar(sql)) {

            stmt.setInt(1, entidade.getEstoqueId());
            stmt.setInt(2, entidade.getEventoId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }


    public List<ItensEvento> getPorIdEvento(Integer idEvento) {
        List<ItensEvento> result = new ArrayList<>();

        String sql = "SELECT " +
                "e.id AS evento_id, " +
                "e.nome, " +
                "e.local, " +
                "e.dia, " +
                "e.qtd AS qtd_evento, " +
                "e.hora_inicio, " +
                "e.hora_fim, " +
                "e.id_funcionario, " +

                "cat.id AS cat_id, " +
                "cat.categoria, " +
                "cat.descricao AS cat_descricao, " +

                "item.qtd AS item_qtd, " +

                "est.id AS estoque_id, " +
                "est.qtd as estoque_qtd, " +
                "est.descricao AS estoque_descricao, " +
                "te.id as tipo_estoque_id," +
                "te.tipo " +

                "FROM evento e " +
                "JOIN cat_evento cat ON cat.id = e.id_cat_evento " +
                "JOIN itens_evento item ON item.id_evento = e.id " +
                "JOIN estoque est ON est.id = item.id_estoque " +
                "JOIN tipo_estoque te ON te.id = est.id_tipo_estoque " +
                "WHERE e.id = ?";

        try (PreparedStatement stmt = db.preparar(sql)) {

            stmt.setInt(1, idEvento);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {


                Cat_Evento cat = new Cat_Evento(
                        rs.getInt("cat_id"),
                        rs.getString("categoria"),
                        rs.getString("cat_descricao")
                );


                Evento eve = new Evento(
                        rs.getInt("evento_id"),
                        rs.getDate("dia").toLocalDate(),
                        rs.getTime("hora_inicio").toLocalTime(),
                        rs.getTime("hora_fim").toLocalTime(),
                        rs.getString("nome"),
                        rs.getString("local"),
                        rs.getInt("qtd_evento"),
                        cat,
                        rs.getInt("id_funcionario")
                );

                TipoEstoque tipo = new TipoEstoque();
                tipo.setTipo(rs.getString("tipo"));
                tipo.setId(rs.getInt("tipo_estoque_id"));


                Estoque est = new Estoque();
                est.setId(rs.getInt("estoque_id"));
                est.setQtd(rs.getInt("estoque_qtd"));
                est.setDescricao(rs.getString("estoque_descricao"));

                est.setTipo(tipo);


                ItensEvento item = new ItensEvento(
                        est,
                        eve,
                        rs.getInt("item_qtd")
                );

                result.add(item);
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return result;
    }

}
