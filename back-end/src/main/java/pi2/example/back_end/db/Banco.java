package pi2.example.back_end.db;

public class Banco  // classe Singleton
{
    private static Banco singletom;
<<<<<<< HEAD
    private static final String URL = "jdbc:postgresql://localhost:5432/SCFV2";
=======
    private static final String URL = "jdbc:postgresql://localhost:5432/SCFV";
>>>>>>> fdbf19478333d54b581d0a89c6a69bbf3aaecfb7
    private static final String USER = "postgres";
    private static final String PASSWORD = "14042005";
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
