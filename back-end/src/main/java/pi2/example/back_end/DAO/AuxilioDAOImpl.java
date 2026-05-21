package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Auxilio;
import pi2.example.back_end.Modelo.Beneficiario;
import pi2.example.back_end.Modelo.CategoriaAuxilio;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuxilioDAOImpl {
    private final Conexao bd;

    public AuxilioDAOImpl(Conexao bd) {
        this.bd = bd;
    }

    private Timestamp converterTimestamp(String dataHora) {
        if (dataHora == null || dataHora.isEmpty()) return null;

        String valor = dataHora.replace("T", " ");
        if (valor.length() == 16) valor += ":00";

        return Timestamp.valueOf(valor);
    }

    public Auxilio gravar(Auxilio entidade) {
        String sql = "INSERT INTO auxilio (descricao, data, status, id_beneficiario, id_categoria) VALUES (?,?,?,?,?)";

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {

            stmt.setString(1, entidade.getDescricao());
            stmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            stmt.setString(3, "Processando");
            stmt.setInt(4, entidade.getBeneficiario().getId());
            stmt.setInt(5, entidade.getCategoria().getId());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entidade.setId(rs.getInt(1));
            }
            entidade.setStatus("Processando");
            entidade.setData(LocalDate.now().toString());
            return entidade;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir auxílio", e);
        }
    }

    public Auxilio alterar(Auxilio entidade) {
        String sql = "UPDATE auxilio SET descricao = ?, status = ?, id_beneficiario = ?, id_categoria = ? WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setString(1, entidade.getDescricao());
            stmt.setString(2, entidade.getStatus());
            stmt.setInt(3, entidade.getBeneficiario().getId());
            stmt.setInt(4, entidade.getCategoria().getId());
            stmt.setInt(5, entidade.getId());

            stmt.executeUpdate();
            return entidade;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar auxílio: " + e.getMessage());
            return null;
        }
    }

    public boolean apagar(Auxilio entidade) {
        String sql = "DELETE FROM auxilio WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, entidade.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir auxílio: " + e.getMessage());
            return false;
        }
    }

    private Auxilio mapAuxilio(ResultSet rs) throws SQLException {
        Beneficiario beneficiario = new Beneficiario();
        beneficiario.setId(rs.getInt("id_beneficiario"));
        beneficiario.setNome(rs.getString("nome_beneficiario"));
        beneficiario.setCpf(rs.getString("cpf"));

        CategoriaAuxilio categoria = new CategoriaAuxilio();
        categoria.setId(rs.getInt("id_categoria"));
        categoria.setNome(rs.getString("nome_categoria"));

        Auxilio auxilio = new Auxilio();
        auxilio.setId(rs.getInt("id"));
        auxilio.setDescricao(rs.getString("descricao"));
        auxilio.setData(rs.getString("data"));
        auxilio.setStatus(rs.getString("status"));
        auxilio.setBeneficiario(beneficiario);
        auxilio.setCategoria(categoria);

        return auxilio;
    }

    public Auxilio get(int id) {
        Auxilio auxilio = null;

        String sql = """
            SELECT a.id, a.descricao, a.data, a.status,
                   b.id AS id_beneficiario, b.nome AS nome_beneficiario, b.cpf,
                   c.id AS id_categoria, c.nome AS nome_categoria
            FROM auxilio a
            JOIN beneficiario b ON a.id_beneficiario = b.id
            JOIN categoria_auxilio c ON a.id_categoria = c.id
            WHERE a.id = ?
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                auxilio = mapAuxilio(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar auxílio por id: " + e.getMessage());
        }

        return auxilio;
    }

    public List<Auxilio> get(String filtro) {
        List<Auxilio> lista = new ArrayList<>();

        String sql = """
            SELECT a.id, a.descricao, a.data, a.status,
                   b.id AS id_beneficiario, b.nome AS nome_beneficiario, b.cpf,
                   c.id AS id_categoria, c.nome AS nome_categoria
            FROM auxilio a
            JOIN beneficiario b ON a.id_beneficiario = b.id
            JOIN categoria_auxilio c ON a.id_categoria = c.id
            WHERE b.cpf ILIKE ?
            ORDER BY b.nome
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setString(1, "%" + filtro + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapAuxilio(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar auxílio com filtro: " + e.getMessage());
        }

        return lista;
    }

    public List<Auxilio> getAll() {
        List<Auxilio> lista = new ArrayList<>();

        String sql = """
            SELECT a.id, a.descricao, a.data, a.status,
                   b.id AS id_beneficiario, b.nome AS nome_beneficiario, b.cpf,
                   c.id AS id_categoria, c.nome AS nome_categoria
            FROM auxilio a
            JOIN beneficiario b ON a.id_beneficiario = b.id
            JOIN categoria_auxilio c ON a.id_categoria = c.id
            ORDER BY b.nome
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapAuxilio(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar auxílios: " + e.getMessage());
        }

        return lista;
    }
}