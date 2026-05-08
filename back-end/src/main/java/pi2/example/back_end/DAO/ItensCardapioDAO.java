package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Alimento;
import pi2.example.back_end.Modelo.Cardapio;
import pi2.example.back_end.Modelo.ItensCardapio;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItensCardapioDAO {

    private final Conexao bd;

    public ItensCardapioDAO(Conexao bd) {
        this.bd = bd;
    }

    public ItensCardapio gravar(ItensCardapio item) {
        String sql = """
            INSERT INTO itens_cardapio
            (id_cardapio, id_alimento, quantidade)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, item.getCardapio().getId());
            stmt.setInt(2, item.getAlimento().getId());
            stmt.setInt(3, item.getQuantidade());
            stmt.executeUpdate();
            return item;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir item: " + e.getMessage());
            return null;
        }
    }

    public ItensCardapio alterar(ItensCardapio item) {
        String sql = """
            UPDATE itens_cardapio
            SET quantidade = ?
            WHERE id_cardapio = ? AND id_alimento = ?
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, item.getQuantidade());
            stmt.setInt(2, item.getCardapio().getId());
            stmt.setInt(3, item.getAlimento().getId());
            stmt.executeUpdate();
            return item;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar item: " + e.getMessage());
            return null;
        }
    }

    public boolean apagar(ItensCardapio item) {
        String sql = """
            DELETE FROM itens_cardapio
            WHERE id_cardapio = ? AND id_alimento = ?
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, item.getCardapio().getId());
            stmt.setInt(2, item.getAlimento().getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir item: " + e.getMessage());
            return false;
        }
    }

    public List<ItensCardapio> buscarPorCardapio(int idCardapio) {
        List<ItensCardapio> lista = new ArrayList<>();
        String sql = "SELECT * FROM itens_cardapio WHERE id_cardapio = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, idCardapio);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ItensCardapio item = new ItensCardapio();

                Alimento a = new Alimento();
                a.setId(rs.getInt("id_alimento"));

                Cardapio c = new Cardapio();
                c.setId(rs.getInt("id_cardapio"));

                item.setAlimento(a);
                item.setCardapio(c);
                item.setQuantidade(rs.getInt("quantidade"));

                lista.add(item);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar itens: " + e.getMessage());
        }

        return lista;
    }

    public ItensCardapio getByIds(int idCardapio, int idAlimento) {
        String sql = """
            SELECT * FROM itens_cardapio
            WHERE id_cardapio = ? AND id_alimento = ?
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, idCardapio);
            stmt.setInt(2, idAlimento);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ItensCardapio item = new ItensCardapio();

                Alimento a = new Alimento();
                a.setId(rs.getInt("id_alimento"));

                Cardapio c = new Cardapio();
                c.setId(rs.getInt("id_cardapio"));

                item.setAlimento(a);
                item.setCardapio(c);
                item.setQuantidade(rs.getInt("quantidade"));

                return item;
            }
            
        } catch (SQLException e) {
            System.out.println("Erro ao buscar item: " + e.getMessage());
        }
        return null;
    }
}