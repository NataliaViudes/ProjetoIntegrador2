package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.VincularBeneficiario;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VincularBeneficiarioControl {
    public VincularBeneficiarioControl(){

    }



    public ResponseEntity<?> buscaPorIdAgendamento(Integer idAgendamento)
    {
        if(idAgendamento!= null && idAgendamento>0)
        {
            List<VincularBeneficiario> resultado= new ArrayList<>();
            Conexao db = Banco.getConexao(); //Abre a conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }
                VincularBeneficiario vincularBeneficiario = new VincularBeneficiario();
                int i=0;

                    resultado = vincularBeneficiario.BuscarPorIdAgendamento(db,idAgendamento);
                if(resultado.isEmpty())
                    return ResponseEntity.badRequest().body(new Erro("Erro geral"));
                else
                    return ResponseEntity.ok(resultado);

            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro ao conectar com o banco de dados"));

            } catch (Exception e) {
                System.out.println("Erro geral: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

            } finally {
                db.desconectar(); // não esquece de fechar a conexao com o banco!!
            }
        }
        else
        {
            return ResponseEntity.badRequest().body(new Erro("Inválido!"));
        }
    }

    public ResponseEntity<?> incluir(List<VincularBeneficiario> lb){
        if(lb != null){
            List<VincularBeneficiario> resultado= new ArrayList<>();
            Conexao db = Banco.getConexao(); //Abre a conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                VincularBeneficiario vincularBeneficiario;
                int i=0;
                while(i<lb.size()){
                    vincularBeneficiario = lb.get(i);
                    resultado.add(vincularBeneficiario.incluir(db));
                    i++;
                }
                return ResponseEntity.ok(resultado);

            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro ao conectar com o banco de dados"));

            } catch (Exception e) {
                System.out.println("Erro geral: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

            } finally {
                db.desconectar(); // não esquece de fechar a conexao com o banco!!
            }

        }
        else
        {
            return ResponseEntity.badRequest().body(new Erro("Inválido!"));
        }
    }

    public ResponseEntity<?> apagar(VincularBeneficiario vb){
        if(vb.getIdAgendamento()!=null && vb.getIdBeneficiario() != null && vb.getIdAgendamento()>0 && vb.getIdBeneficiario()>0)
        {
            Conexao db = Banco.getConexao(); // Abre conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }
                VincularBeneficiario existente = vb.BuscarElem(db); // verificar se existe no banco
                if (existente != null) {
                    if(vb.apagar(db))
                        return ResponseEntity.ok(true);
                    else
                        return ResponseEntity.badRequest().body(new Erro("Erro ao excluir"));
                }
                else
                    return ResponseEntity.badRequest().body(new Erro("Não encontrado"));
            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro com o banco"));

            } catch (Exception e) {
                System.out.println("Erro geral: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

            } finally {
                db.desconectar(); // não esquece de fechar a conexao com o banco!!
            }
        }
        else
            return ResponseEntity.badRequest().body(new Erro("Dado inválido"));
    }

}
