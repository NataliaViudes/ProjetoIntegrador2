package pi2.example.back_end.db;

import pi2.example.back_end.Modelo.Evento;

import java.sql.ResultSet;
import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;
public class DALEvento implements IDAL<Evento> {

    private final Conexao bd;

    public DALEvento(Conexao bd) {
        this.bd = bd;
    }

    @Override
    public boolean gravar(Evento entidade) {
        String sql = "INSERT INTO evento (id_evento, nome,descricao) VALUES ("
                + entidade.getId() + ", '" + entidade.getNome() + "')";
        return bd.manipular(sql);
    }

    @Override
    public boolean alterar(Evento entidade) {
        String sql = "UPDATE evento SET nome = '" + entidade.getNome()
                + "' WHERE id_evento = " + entidade.getId();
        return bd.manipular(sql);
    }

    @Override
    public boolean apagar(Evento entidade) {
        String sql = "DELETE FROM evento WHERE id_evento = " + entidade.getId();
        return bd.manipular(sql);
    }

    @Override
    public Evento get(int id) {
        Evento eve = null;
        String sql = "SELECT * FROM evento WHERE id_evento = " + id;
        ResultSet rs = bd.consultar(sql);
        try {
            if (rs != null && rs.next()) {
                eve = new Evento(rs.getInt("id_evento"), rs.getString("nome"),rs.getString("descricao"));
            }
            // Fechar ResultSet depois de usar
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.out.println("Erro ao buscar evento: " + e);
        }
        return eve;
    }

    @Override
    public List<Evento> get(String filtro) {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT * FROM evento";
        if (filtro != null && !filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }
        ResultSet rs = bd.consultar(sql);
        try {
            if (rs != null) {
                while (rs.next()) {
                    Evento eve = new Evento(rs.getInt("id_evento"), rs.getString("nome"),rs.getString("descricao"));
                    lista.add(eve);
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar lista de eventos: " + e);
        }
        return lista;
    }
}