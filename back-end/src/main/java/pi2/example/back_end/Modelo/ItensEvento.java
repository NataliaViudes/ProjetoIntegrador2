package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.DAOItensEvento;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.List;

public class ItensEvento {

    private Estoque estoque;
    private Evento evento;
    private int qtd;

    public ItensEvento() {
    }

    public ItensEvento(Estoque estoque, Evento evento, int qtd) {
        this.estoque = estoque;
        this.evento = evento;
        this.qtd = qtd;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public Integer getEventoId() {

        if (evento == null)
            return null;

        return evento.getIdEvento();
    }

    public Integer getEstoqueId() {

        if (estoque == null)
            return null;

        return estoque.getId();
    }

    // =====================================================
    // BUSCAR
    // =====================================================

    public List<ItensEvento> buscarItensDoEvento(
            Conexao con,
            Integer idEvento
    ) {

        DAOItensEvento dao =
                new DAOItensEvento(con);

        return dao.getPorIdEvento(idEvento);
    }

    // =====================================================
    // INSERT
    // =====================================================

    public ItensEvento incluir(Conexao con) {

        DAOItensEvento dao =
                new DAOItensEvento(con);

        return dao.gravar(this);
    }

    // =====================================================
    // UPDATE
    // =====================================================

    public ItensEvento alterar(Conexao con){

        DAOItensEvento dao =
                new DAOItensEvento(con);

        return dao.alterar(this);
    }

    // =====================================================
    // DELETE
    // =====================================================

    public boolean apagar(Conexao con) {

        DAOItensEvento dao =
                new DAOItensEvento(con);

        return dao.apagar(this);
    }

    // =====================================================
    // LIMPAR
    // =====================================================

    public boolean limparItens(
            Conexao con,
            Integer idEvento
    ) {

        DAOItensEvento dao =
                new DAOItensEvento(con);

        return dao.limparItens(idEvento);
    }

    // =====================================================
    // SYNC
    // =====================================================

    public boolean syncItensEvento(
            Conexao con,
            Integer eventoId,
            List<ItensEvento> itens
    ) {

        DAOItensEvento dao =
                new DAOItensEvento(con);

        return dao.syncItensEvento(
                eventoId,
                itens
        );
    }


    public ItensEvento buscarItem(
            Conexao con,
            Integer idEvento,
            Integer idEstoque
    ) {

        DAOItensEvento dao =
                new DAOItensEvento(con);

        return dao.buscarItem(
                idEvento,
                idEstoque
        );
    }


    public ItensEvento buscarPorChave(
            Conexao con,
            Integer idEvento,
            Integer idEstoque
    ) {
        DAOItensEvento dao =
                new DAOItensEvento(con);

        return dao.buscarPorChave(
                idEvento,
                idEstoque
        );
    }
}