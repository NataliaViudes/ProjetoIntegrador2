package pi2.example.back_end.Modelo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "categoria", "descricao" })
public class Cat_Evento {
    private Integer id;
    private String nome;
    private String descricao;

    public Cat_Evento() {
    }


    public Cat_Evento(int id) {
        this.id = id;
        this.nome = "";
        this.descricao = "";
    }

    public Cat_Evento(String categoria, String descricao) {
        this.id=0;
        this.nome = categoria;
        this.descricao = descricao;
    }

    public Cat_Evento(int id, String categoria, String descricao) {
        this.id = id;
        this.nome = categoria;
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

}
