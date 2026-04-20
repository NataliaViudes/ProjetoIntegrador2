package pi2.example.back_end.Modelo;

public class Tipo_Estoque {
    private Integer id;
    private String tipo;


    public Tipo_Estoque(Integer id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public Tipo_Estoque() {
    }

    public Tipo_Estoque(String tipo) {
        this.tipo = tipo;
    }
}
