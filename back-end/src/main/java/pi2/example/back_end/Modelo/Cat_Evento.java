package pi2.example.back_end.Modelo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import pi2.example.back_end.DAO.DAOCat_Evento;
import pi2.example.back_end.db.Conexao;

import java.util.List;

@JsonPropertyOrder({ "id", "categoria", "descricao" })
public class Cat_Evento {
    private Integer id;
    private String categoria;
    private String descricao;

    public Cat_Evento() {
    }

    public Cat_Evento(int id) {
        this.id = id;
        this.categoria = "";
        this.descricao = "";
    }

    public Cat_Evento(String categoria, String descricao) {
        this.id =0;
        this.categoria = categoria;
        this.descricao = descricao;
    }

    public Cat_Evento(int id, String categoria, String descricao) {
        this.id = id;
        this.categoria = categoria;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Cat_Evento incluir(Conexao con)
    {
        DAOCat_Evento dao = new DAOCat_Evento(con);
        return dao.gravar(this);
    }

    public Cat_Evento alterar(Conexao con)
    {
        DAOCat_Evento dao = new DAOCat_Evento(con);
        return dao.alterar(this);
    }

    public boolean apagar(Conexao con)
    {
        DAOCat_Evento dao = new DAOCat_Evento(con);
        return dao.apagar(this);
    }

    public Cat_Evento buscarporId(Integer id,Conexao con)
    {
        DAOCat_Evento dao = new DAOCat_Evento(con);
        return dao.get(id);
    }

    public List<Cat_Evento> buscarPorCategoria(String categoria,Conexao con)
    {
        DAOCat_Evento dao = new DAOCat_Evento(con);
        return dao.buscarPorNome(categoria);
    }

    public List<Cat_Evento> buscarPorDescricao(String descricao,Conexao con)
    {
        DAOCat_Evento dao = new DAOCat_Evento(con);
        return dao.buscarPorNome(descricao);
    }




}
