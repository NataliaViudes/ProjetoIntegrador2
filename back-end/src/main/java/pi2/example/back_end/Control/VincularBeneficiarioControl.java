package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.ListaBeneficiario;
import pi2.example.back_end.Modelo.VincularBeneficiario;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;

public class VincularBeneficiarioControl {
    public VincularBeneficiarioControl(){

    }

    public ResponseEntity<?> incluir(ListaBeneficiario listaBeneficiario){
        if(listaBeneficiario.getListaBeneficiario() != null){
            ListaBeneficiario resultado=null;
            Conexao db = Banco.getConexao(); //Abre a conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                VincularBeneficiario vincularBeneficiario;
                int i=0;
                while(i<listaBeneficiario.getListaBeneficiario().size()){
                    vincularBeneficiario = listaBeneficiario.getElemento(i);
                    resultado.getListaBeneficiario().add(vincularBeneficiario.incluir(db));
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
