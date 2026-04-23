package pi2.example.back_end.db;

public class Banco  // classe Singleton
{
    private static Banco singleton;


    private static final String URL = "jdbc:postgresql://localhost:5432/SCFV";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres123";

    public static Conexao getConexao() {
        return new Conexao(URL, USER, PASSWORD);
    }

    public static Banco getSingleton() {
        if (singleton == null) {
            singleton = new Banco();
        }
        return singleton;
    }

    private Banco() {}

}
