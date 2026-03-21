package pi2.example.back_end.DAO;

import pi2.example.back_end.Conexao;
import pi2.example.back_end.Modelo.Evento;

import java.sql.ResultSet;
import java.util.List;

public  class DALEvento  {



    public boolean gravar(Object entidade) {


        return false;
    }


    public boolean alterar(Object entidade) {
        return false;
    }


    public boolean apagar(Object entidade) {
        return false;
    }


/*
    public Evento get(int id, Conexao bd) {
        Evento eve = null;
        DALEvento dale = new DALEvento();
        ResultSet rs= bd.consultar("select * from evento where id_evento=" + id);
        try
        {
            if(rs.next())
            {
                eve = new Evento( id,"Teste");
            }
        }
        catch(Exception e ){
            System.out.println(e);
        }
        return eve;
    }
*/
    /*public Cidade get(int cod)
    {   Cidade cid=null;
        DALEstado dale = new DALEstado();
        Estado estado=null;
        ResultSet rs=BD.con.consultar("select * from cidade where cid_cod="+cod);
        try
        {  if(rs.next())
        {  estado = dale.getUF(rs.getInt("est_cod"));
            cid=new Cidade(cod,rs.getString("cid_nome"),estado);
        }
        }catch(Exception e ){ System.out.println(e);}
        return cid;
    }*/




    public List get(String filtro) {
        return List.of();
    }
}
