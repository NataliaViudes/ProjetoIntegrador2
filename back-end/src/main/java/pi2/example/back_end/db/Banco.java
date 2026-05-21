package pi2.example.back_end.db;

public class Banco  // classe Singleton
{
    private static Banco singletom;
    private static final String URL = "jdbc:postgresql://localhost:5432/SCFV2";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres123";
    public static Conexao getConexao() {
        return new Conexao(URL, USER, PASSWORD);
    }

    public static Banco getSingletom() {
        if (singletom == null) {
            singletom = new Banco();
        }
        return singletom;
    }

    private Banco() {}


    
}
