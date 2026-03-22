package pi2.example.back_end.Modelo;

public class Alimento {
    private int id;
    private String nome;
    private String tipo;
    private String descricao;


    //os tipos vão ser: Salgado, Doce, Bebida

    public Alimento() {
        this(0, "", "", "");
    }

    public Alimento( int id, String nome, String tipo, String descricao) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
