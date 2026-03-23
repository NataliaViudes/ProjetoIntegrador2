package pi2.example.back_end.Modelo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.ResponseEntity;
import pi2.example.back_end.db.Conexao;
import pi2.example.back_end.db.DALEvento;

import java.util.List;

@JsonPropertyOrder({ "id", "categoria", "descricao" })
public class Evento {
    private Integer id;
    private String categoria;
    private String descricao;

    public Evento() {
    }


    public Evento(int id) {
        this.id = id;
        this.categoria = "";
        this.descricao = "";
    }

    public Evento(String categoria, String descricao) {
        this.id=0;
        this.categoria = categoria;
        this.descricao = descricao;
    }

    public Evento(int id, String categoria, String descricao) {
        this.id = id;
        this.categoria = categoria;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Evento> consultarCategoria(String termo,Conexao con) {
        DALEvento dao = new DALEvento(con);
        return dao.buscarPorCategoria(termo);
    }

    public Evento consultarId(Integer termo,Conexao con) {
        DALEvento dao = new DALEvento(con);
        return dao.get(termo);
    }

    public Evento incluir(Conexao con)
    {
        DALEvento dao = new DALEvento(con);
        return dao.gravar(this);
    }

    public Evento alterar(Conexao con)
    {
        DALEvento dao = new DALEvento(con);
        return dao.alterar(this);
    }

    public boolean deletar(Conexao con) {
        DALEvento dao = new DALEvento(con);
       return dao.apagar(this);
    }
}
