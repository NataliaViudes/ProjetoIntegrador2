package pi2.example.back_end.DAO;


import pi2.example.back_end.Modelo.Estoque;
import pi2.example.back_end.Modelo.MaterialAtividade;
import pi2.example.back_end.Modelo.TipoEstoque;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOEstoque {

    private final Conexao bd;

    public DAOEstoque(Conexao bd) {
        this.bd = bd;
    }


    public Estoque gravar(Estoque entidade) {
        String sql = "INSERT INTO ESTOQUE(descricao,qtd,id_tipo_estoque) VALUES(?,?,?)";

        try (PreparedStatement stmt = bd.prepararComRetorno(sql)) {

            stmt.setString(1, entidade.getDescricao());
            stmt.setInt(2, entidade.getQtd());
            stmt.setInt(3, entidade.getTipoId());
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

    public Estoque alterar(Estoque entidade) {
        String sql = "UPDATE ESTOQUE SET descricao=?,qtd=?,id_tipo_estoque=?  WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, entidade.getDescricao());
            stmt.setInt(2, entidade.getQtd());
            stmt.setInt(3, entidade.getTipoId());
            stmt.setInt(4,entidade.getId());
            stmt.executeUpdate();
            return entidade;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }


    public boolean apagar(Estoque entidade) {
        String sql = "DELETE FROM ESTOQUE WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, entidade.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            return false;
        }
    }


    public Estoque getPorId(Integer id) {
        Estoque est = null;
        String sql = "SELECT * FROM ESTOQUE WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                TipoEstoque tipoEstoque = new TipoEstoque();
                tipoEstoque = tipoEstoque.buscarPorId(bd,rs.getInt("id_tipo_estoque"));
                if(tipoEstoque!=null)
                {
                    est = new Estoque(
                            rs.getInt("id"),
                            rs.getString("descricao"),
                            rs.getInt("qtd"),
                            tipoEstoque
                    );

                }
                else
                {
                    System.out.println("Erro: ao buscar o Tipo Estoque idTipoEStoque: "+rs.getInt("id_tipo_estoque"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return est;
    }


    public List<Estoque> buscarPorTipoEstoque(String tipo) {
        List<Estoque> lista = new ArrayList<>();
        String sql;

        // regra: se vazio ou null → traz tudo
        if (tipo == null) {
            tipo="";
        }
        sql = "select e.id,e.descricao,e.qtd,e.id_tipo_estoque,te.tipo from estoque e join (select * from tipo_estoque te where te.tipo ILIKE  ? ) as te on e.id_tipo_estoque = te.id order by te.tipo asc";
        try (PreparedStatement stmt = bd.preparar(sql)) {

            stmt.setString(1, "%" + tipo + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Estoque(
                        rs.getInt("id"),
                        rs.getString("descricao"),
                        rs.getInt("qtd"),
                        new TipoEstoque(rs.getInt("id_tipo_estoque"), rs.getString("tipo"))
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }
        return lista;
    }


    public List<Estoque> buscarPorDescricao(String descricao) {
        List<Estoque> lista = new ArrayList<Estoque>();
        String sql;

        // regra: se vazio ou null → traz tudo
        if (descricao == null) {
            descricao="";
        }
            sql = "SELECT * FROM ESTOQUE WHERE descricao ILIKE ? ORDER BY descricao ASC";
            try (PreparedStatement stmt = bd.preparar(sql)) {
                stmt.setString(1,"%"+descricao+"%");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {

                    TipoEstoque tipoEstoque = new TipoEstoque();
                    tipoEstoque = tipoEstoque.buscarPorId(bd,rs.getInt("id_tipo_estoque"));
                    if(tipoEstoque!=null)
                    {
                        lista.add( new Estoque(
                                rs.getInt("id"),
                                rs.getString("descricao"),
                                rs.getInt("qtd"),
                                tipoEstoque
                        ));

                    }
                    else
                    {
                        System.out.println("Erro: ao buscar o Tipo Estoque idTipoEStoque: "+rs.getInt("id_tipo_estoque"));
                    }
                }

            } catch (SQLException e) {
                System.out.println("Erro: " + e);
            }
        return lista;
    }

    // FUNÇÕES ABAIXO SÃO USADAS NA ETAPA

    public boolean baixarEstoque(int idItem, int quantidade) {
        String sqlSelect = "SELECT qtd FROM estoque WHERE id = ?";
        String sqlUpdate = "UPDATE estoque SET qtd = qtd - ? WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sqlSelect)) {
            stmt.setInt(1, idItem);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int atual = rs.getInt("qtd");

                if (atual < quantidade) {
                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao verificar estoque: " + e.getMessage());
            return false;
        }

        try (PreparedStatement stmt = bd.preparar(sqlUpdate)) {
            stmt.setInt(1, quantidade);
            stmt.setInt(2, idItem);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar estoque: " + e.getMessage());
            return false;
        }
    }

    public boolean salvarAgendamentoMaterial(int idAgendamento, int idItem, int quantidade) {
        String sql = """
            INSERT INTO agendamento_material (id_agendamento, id_item, quantidade)
            VALUES (?, ?, ?)
            ON CONFLICT (id_agendamento, id_item)
            DO UPDATE SET quantidade = EXCLUDED.quantidade
        """;

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, idAgendamento);
            stmt.setInt(2, idItem);
            stmt.setInt(3, quantidade);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar material: " + e.getMessage());
            return false;
        }
    }

    public List<MaterialAtividade> buscarPorAgendamento(int idAgendamento) {
        List<MaterialAtividade> lista = new ArrayList<>();

        String sql = "SELECT * FROM agendamento_material WHERE id_agendamento = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, idAgendamento);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MaterialAtividade m = new MaterialAtividade();
                m.setIdAgendamento(rs.getInt("id_agendamento"));
                m.setIdItem(rs.getInt("id_item"));
                m.setQuantidade(rs.getInt("quantidade"));

                lista.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return lista;
    }
    public boolean existeMaterial(int idAgendamento, int idItem) {
        String sql = "SELECT 1 FROM agendamento_material WHERE id_agendamento = ? AND id_item = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, idAgendamento);
            stmt.setInt(2, idItem);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // se achou, já existe

        } catch (SQLException e) {
            System.out.println("Erro ao verificar material: " + e.getMessage());
            return false;
        }
    }

    public Integer getQuantidadeAtual(int idAgendamento, int idItem) {
        String sql = "SELECT quantidade FROM agendamento_material WHERE id_agendamento = ? AND id_item = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, idAgendamento);
            stmt.setInt(2, idItem);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("quantidade");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar quantidade: " + e.getMessage());
        }

        return null;
    }
    public boolean devolverEstoque(int idItem, int quantidade) {
        String sql = "UPDATE estoque SET qtd = qtd + ? WHERE id = ?";

        try (PreparedStatement stmt = bd.preparar(sql)) {
            stmt.setInt(1, quantidade);
            stmt.setInt(2, idItem);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao devolver estoque: " + e.getMessage());
            return false;
        }
    }
    public boolean deletarMaterial(int idAgendamento, int idItem) {
        String sqlSelect = """
            SELECT quantidade FROM agendamento_material
            WHERE id_agendamento = ? AND id_item = ?
        """;

        String sqlDevolver = """
            UPDATE estoque SET qtd = qtd + ?
            WHERE id = ?
        """;

        String sqlDelete = """
            DELETE FROM agendamento_material
            WHERE id_agendamento = ? AND id_item = ?
        """;

        try {
            int quantidade = 0;

            // 1. Buscar quantidade usada
            try (PreparedStatement stmt = bd.preparar(sqlSelect)) {
                stmt.setInt(1, idAgendamento);
                stmt.setInt(2, idItem);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    quantidade = rs.getInt("quantidade");
                } else {
                    return false; // não encontrou
                }
            }

            // 2. Devolver ao estoque
            try (PreparedStatement stmt = bd.preparar(sqlDevolver)) {
                stmt.setInt(1, quantidade);
                stmt.setInt(2, idItem);
                stmt.executeUpdate();
            }

            // 3. Deletar vínculo
            try (PreparedStatement stmt = bd.preparar(sqlDelete)) {
                stmt.setInt(1, idAgendamento);
                stmt.setInt(2, idItem);
                stmt.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao deletar material: " + e.getMessage());
            return false;
        }
    }

}
