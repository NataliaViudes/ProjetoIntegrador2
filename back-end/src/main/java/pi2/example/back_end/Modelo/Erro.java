package pi2.example.back_end.Modelo;

public class Erro {
    private  String  mensage;

    public Erro(String mensage) {
        this.mensage = mensage;
    }

    public String getMensage() {
        return mensage;
    }

    public void setMensage(String mensage) {
        this.mensage = mensage;
    }
}
