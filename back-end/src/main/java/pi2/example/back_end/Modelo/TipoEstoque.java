package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.DAOTipoEstoque;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class TipoEstoque {
    private Integer id;
    private String tipo;

    public TipoEstoque(Integer id) {
        this.id = id;
        this.tipo="";
    }

    public TipoEstoque(Integer id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public TipoEstoque() {
        this.id=0;
        this.tipo="";
    }

    public TipoEstoque(String tipo) {
        this.id=0;
        this.tipo = tipo;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public TipoEstoque incluir(Conexao con)
    {
        DAOTipoEstoque dao = new DAOTipoEstoque(con);
        return dao.gravar(this);
    }

    public TipoEstoque alterar(Conexao con)
    {
        DAOTipoEstoque dao = new DAOTipoEstoque(con);
        return dao.alterar(this);
    }

    public boolean apagar(Conexao con){
        DAOTipoEstoque dao = new DAOTipoEstoque(con);
        return dao.apagar(this);
    }

    public TipoEstoque buscarPorId(Conexao con,Integer id)
    {
        DAOTipoEstoque dao = new DAOTipoEstoque(con);
        return dao.getPorId(id);
    }

    public List<TipoEstoque> buscarPorTipo(Conexao con, String tipo)
    {
        DAOTipoEstoque dao = new DAOTipoEstoque(con);
        return dao.buscarPorTipo(tipo);
    }


}
