package pi2.example.back_end.Modelo;

public class Erro {
    private  String  message;

    public Erro(String mensage) {
        this.message = mensage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
