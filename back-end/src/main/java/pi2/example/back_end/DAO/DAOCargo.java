package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Cargo;
import pi2.example.back_end.Modelo.Funcionario;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAOCargo {
    private final Conexao bd;

    public DAOCargo(Conexao bd) {
        this.bd = bd;
    }


    public Cargo gravar(Cargo entidade) {
        String sql = "INSERT INTO CARGO (nome) VALUES (?)";

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {

            stmt.setString(1, entidade.getNome());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                entidade.setId(id);
            }
            return entidade;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }

    public Cargo alterar(Cargo entidade) {
        String sql = "UPDATE CARGO SET nome = ? WHERE id_cargo = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, entidade.getNome());
            stmt.setInt(2, entidade.getId());
            stmt.executeUpdate();


            return entidade;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }

    public boolean apagar(Cargo entidade) {
        String sql = "DELETE FROM CARGO WHERE id_cargo = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, entidade.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }

    public Cargo get(Integer id) {
        Cargo cargo = null;
        String sql = "SELECT * FROM CARGO WHERE id_cargo = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cargo = new Cargo(
                        rs.getInt("id_cargo"),
                        rs.getString("nome")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return cargo;
    }

    public List<Cargo> getAll(){
        List<Cargo> lista = new ArrayList<>();

        String sql = "SELECT * FROM cargo ORDER BY nome";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Cargo(
                        rs.getInt("id_cargo"),
                        rs.getString("nome")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Cargo> buscarPorNome(String nome) {
        List<Cargo> lista = new ArrayList<>();
        String sql;

        // regra: se vazio ou null = traz tudo
        if (nome == null || nome.isEmpty()) {
            sql = "SELECT * FROM CARGO ORDER BY nome ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Cargo(
                            rs.getInt("id_cargo"),
                            rs.getString("nome")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }

        } else {
            sql = "SELECT * FROM CARGO WHERE nome ILIKE '%' || ? || '%' ORDER BY nome ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {

                stmt.setString(1, nome);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(new Cargo(
                            rs.getInt("id_cargo"),
                            rs.getString("nome")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }
        }
        return lista;
    }
}