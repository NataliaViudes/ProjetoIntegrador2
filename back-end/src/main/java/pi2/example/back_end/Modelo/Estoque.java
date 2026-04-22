package pi2.example.back_end.Modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import pi2.example.back_end.DAO.DAOEstoque;
import pi2.example.back_end.db.Conexao;

import java.util.List;

@JsonPropertyOrder({ "id", "qtd", "descricao","tipo" })
public class Estoque {
    private Integer id;
    private String descricao;
    private Integer qtd;
    TipoEstoque tipo;

    public Estoque() {
    }

    public Estoque(String descricao, Integer qtd, TipoEstoque tipo) {
        this.id=0;
        this.descricao = descricao;
        this.qtd = qtd;
        this.tipo = tipo;
    }

    public Estoque(Integer id) {
        this.id = id;
        this.descricao = "";
        this.qtd = 0;
        this.tipo = null;
    }

    public Estoque(Integer id, String descricao, Integer qtd, TipoEstoque tipo) {
        this.id = id;
        this.descricao = descricao;
        this.qtd = qtd;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    public TipoEstoque getTipo() {
        return tipo;
    }

    public void setTipo(TipoEstoque tipo) {
        this.tipo = tipo;
    }

    @JsonIgnore
    public Integer getTipoId()
    {
        return this.tipo.getId();
    }

    @JsonIgnore
    public String getTipoTipo()
    {
        return this.tipo.getTipo();
    }



    public List<Estoque> buscarPorDescricao(Conexao con,String descricao)
    {
        DAOEstoque dao = new DAOEstoque(con);
        return dao.buscarPorDescricao(descricao);
    }

    public List<Estoque> buscarPorTipo(Conexao con,String tipoEstoque)
    {
        DAOEstoque dao = new DAOEstoque(con);
        return dao.buscarPorTipoEstoque(tipoEstoque);
    }

    public Estoque buscarPorId(Conexao con,int id)
    {
        DAOEstoque dao = new DAOEstoque(con);
        return dao.getPorId(id);
    }

    public Estoque incluir(Conexao con)
    {
        DAOEstoque dao = new DAOEstoque(con);
        return dao.gravar(this);
    }

    public Estoque alterar(Conexao con)
    {
        DAOEstoque dao = new DAOEstoque(con);
        return dao.alterar(this);
    }

    public Boolean apagar(Conexao con)
    {
        DAOEstoque dao = new DAOEstoque(con);
        return dao.apagar(this);
    }




}
