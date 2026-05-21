package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Cargo;
import pi2.example.back_end.Modelo.Funcionario;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOFuncionario {
    private final Conexao bd;

    public DAOFuncionario(Conexao bd) {
        this.bd = bd;
    }

    public Funcionario gravar(Funcionario entidade) {
        String sql = "INSERT INTO funcionario (nome, cpf, telefone, nis, nascimento, sexo, endereco, id_cargo) VALUES (?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)){
            stmt.setString(1, entidade.getNome());
            stmt.setString(2, entidade.getCpf());
            stmt.setString(3, entidade.getTelefone());
            stmt.setString(4, entidade.getNis());
            stmt.setDate(5, new java.sql.Date(entidade.getNascimento().getTime()));
            stmt.setString(6, entidade.getSexo());
            stmt.setString(7, entidade.getEndereco());
            stmt.setInt(8, entidade.getCargo().getId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                entidade.setId(id);
            }
            return entidade;
        } catch (SQLException e){
            throw new RuntimeException("Erro ao incluir funcionário", e);
        }
    }

    public Funcionario alterar(Funcionario entidade) {
        String sql = "UPDATE funcionario SET nome = ?, cpf = ?, telefone = ?, nis = ?, nascimento = ?, sexo = ?, endereco = ?, id_cargo = ? WHERE id_funcionario = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setString(1, entidade.getNome());
            stmt.setString(2, entidade.getCpf());
            stmt.setString(3, entidade.getTelefone());
            stmt.setString(4, entidade.getNis());
            stmt.setDate(5, new java.sql.Date(entidade.getNascimento().getTime()));
            stmt.setString(6, entidade.getSexo());
            stmt.setString(7, entidade.getEndereco());
            stmt.setInt(8, entidade.getCargo().getId());
            stmt.setInt(9, entidade.getId());

            stmt.executeUpdate();

            return entidade;
        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }

    public boolean apagar(Funcionario entidade) {
        String sql = "DELETE FROM funcionario WHERE id_funcionario = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, entidade.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }

    private Funcionario mapFuncionario(ResultSet rs) throws SQLException {
        Funcionario f = new Funcionario();

        f.setId(rs.getInt("id_funcionario"));
        f.setNome(rs.getString("nome"));
        f.setCpf(rs.getString("cpf"));
        f.setTelefone(rs.getString("telefone"));
        f.setNis(rs.getString("nis"));
        f.setNascimento(rs.getDate("nascimento"));
        f.setSexo(rs.getString("sexo"));
        f.setEndereco(rs.getString("endereco"));

        Cargo c = new Cargo();
        c.setId(rs.getInt("id_cargo"));
        c.setNome(rs.getString("nome_cargo"));

        f.setCargo(c);

        return f;
    }

    public Funcionario get(int id) {
        Funcionario funcionario = null;

        String sql = "SELECT f.*, c.nome AS nome_cargo " +
                "FROM funcionario f " +
                "JOIN cargo c ON f.id_cargo = c.id_cargo " +
                "WHERE f.id_funcionario = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                funcionario = new Funcionario();

                funcionario.setId(rs.getInt("id_funcionario"));
                funcionario.setNome(rs.getString("nome"));
                funcionario.setCpf(rs.getString("cpf"));
                funcionario.setTelefone(rs.getString("telefone"));
                funcionario.setNis(rs.getString("nis"));
                funcionario.setNascimento(rs.getDate("nascimento"));
                funcionario.setSexo(rs.getString("sexo"));
                funcionario.setEndereco(rs.getString("endereco"));

                Cargo c = new Cargo();
                c.setId(rs.getInt("id_cargo"));
                c.setNome(rs.getString("nome_cargo"));

                funcionario.setCargo(c);
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return funcionario;
    }

    public List<Funcionario> get(String tipo, String filtro) {

        List<Funcionario> lista = new ArrayList<>();

        String sql = "SELECT f.*, c.nome AS nome_cargo " +
                "FROM funcionario f " +
                "JOIN cargo c ON f.id_cargo = c.id_cargo ";

        switch (tipo) {

            case "cpf":
                sql += "WHERE f.cpf LIKE ?";
                break;

            case "cargo":
                sql += "WHERE c.nome ILIKE ?";
                break;

            default:
                sql += "WHERE f.nome ILIKE ?";
                break;
        }

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, "%" + filtro + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapFuncionario(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Funcionario> get(String filtro) {
        List<Funcionario> lista = new ArrayList<>();

        String sql = "SELECT f.*, c.nome AS nome_cargo " +
                "FROM funcionario f " +
                "JOIN cargo c ON f.id_cargo = c.id_cargo " +
                "WHERE f.nome ILIKE ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, "%" + filtro + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Funcionario f = new Funcionario();

                f.setId(rs.getInt("id_funcionario"));
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf"));
                f.setTelefone(rs.getString("telefone"));
                f.setNis(rs.getString("nis"));
                f.setNascimento(rs.getDate("nascimento"));
                f.setSexo(rs.getString("sexo"));
                f.setEndereco(rs.getString("endereco"));

                Cargo c = new Cargo();
                c.setId(rs.getInt("id_cargo"));
                c.setNome(rs.getString("nome_cargo"));

                f.setCargo(c);

                lista.add(f);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Funcionario> getAll() {
        List<Funcionario> lista = new ArrayList<>();

        String sql = "SELECT f.*, c.nome AS nome_cargo " +
                "FROM funcionario f " +
                "JOIN cargo c ON f.id_cargo = c.id_cargo";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapFuncionario(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Funcionario> buscarPorNome(String nome) {
        List<Funcionario> lista = new ArrayList<>();
        String sql;

        if (nome == null || nome.isEmpty()) {
            sql = "SELECT f.*, c.nome AS nome_cargo " +
                    "FROM funcionario f " +
                    "JOIN cargo c ON f.id_cargo = c.id_cargo " +
                    "ORDER BY f.nome ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(mapFuncionario(rs));
                }
            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }

        } else {
            sql = "SELECT f.*, c.nome AS nome_cargo " +
                    "FROM funcionario f " +
                    "JOIN cargo c ON f.id_cargo = c.id_cargo " +
                    "WHERE f.nome ILIKE '%' || ? || '%' " +
                    "ORDER BY f.nome ASC";

            try (PreparedStatement stmt = bd.preparar(sql)) {
                stmt.setString(1, nome);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    lista.add(mapFuncionario(rs));
                }
            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }
        }
        return lista;
    }

    public List<Funcionario> buscarPorCargo(Integer cargoId) {
        List<Funcionario> lista = new ArrayList<>();
        String sql =
                "SELECT f.*, c.nome AS nome_cargo " +
                        "FROM funcionario f " +
                        "JOIN cargo c ON f.id_cargo = c.id_cargo " +
                        "WHERE c.id_cargo = ? " +
                        "ORDER BY f.nome ASC";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, cargoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapFuncionario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}