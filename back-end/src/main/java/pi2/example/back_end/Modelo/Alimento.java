package pi2.example.back_end.Modelo;

public class Alimento {
    private String nome;
    private String tipo;
    private int codigo;


    public Alimento() {
        this("", "", 0);
    }

    public Alimento(String nome, String tipo, int codigo) {
        this.nome = nome;
        this.tipo = tipo;
        this.codigo = codigo;
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

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
