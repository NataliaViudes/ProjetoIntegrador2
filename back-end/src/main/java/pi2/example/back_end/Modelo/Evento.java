package pi2.example.back_end.Modelo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.ResponseEntity;
import pi2.example.back_end.db.Conexao;
import pi2.example.back_end.db.DALEvento;

import java.util.List;

@JsonPropertyOrder({ "id", "nome", "descricao" })
public class Evento {
    private int id;
    private String nome;
    private String descricao;

    public Evento() {
    }


    public Evento(int id) {
        this.id = id;
        this.nome = "";
        this.descricao = "";
    }

    public Evento(String nome, String descricao) {
        this.id=0;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Evento(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public List<Evento> consultarTodos(Conexao con) {
        DALEvento dao = new DALEvento(con);
        return dao.get("");
    }

    public List<Evento> consultarNome(String termo,Conexao con) {
        DALEvento dao = new DALEvento(con);
        return dao.buscarPorNome(termo);
    }

    public Evento consultarId(int termo,Conexao con) {
        DALEvento dao = new DALEvento(con);
        return dao.get(termo);
    }

    public boolean incluir(Conexao con)
    {
        DALEvento dao = new DALEvento(con);
        return dao.gravar(this);
    }

}
