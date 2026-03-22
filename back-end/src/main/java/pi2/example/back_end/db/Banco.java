package pi2.example.back_end.db;

public class Banco  // classe Singleton
{
    static private Conexao con=new Conexao();
    static public boolean conectar() {
        boolean ok = con.conectar("jdbc:postgresql://localhost:5432/pi",  "postgres", "felipe123"); //mudar rota conforme seu banco
        if(!ok) {
            System.out.println("ERRO ao conectar com o banco: " + con.getMensagemErro());
            return false;
        }
        return true;
    }
    static public Conexao getCon()
    {
        return con;
    }

    private Banco() {
    }
    
}
