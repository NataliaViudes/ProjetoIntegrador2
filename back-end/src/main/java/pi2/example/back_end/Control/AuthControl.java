package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pi2.example.back_end.DTO.LoginRequest;
import pi2.example.back_end.DTO.LoginResponse;
import pi2.example.back_end.Modelo.Usuario;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;
import pi2.example.back_end.utils.JWTUtil;

@Service
public class AuthControl {

    public ResponseEntity<?> login(LoginRequest req) {
        Conexao con = Banco.getConexao();
        try {
            con.conectar();
            Usuario usuario = new Usuario() .buscarPorLogin(req.getLogin(), con);

            if (usuario == null) {
                System.out.println("USUARIO NÃO ENCONTRADO");
                return ResponseEntity.status(401).body("Usuário não encontrado");
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean senhaCorreta = encoder.matches(req.getSenha(),usuario.getSenha());

            if (!senhaCorreta) {
                return ResponseEntity.status(401).body("Senha inválida");
            }

            if (!usuario.getAtivo()) {
                return ResponseEntity.status(403).body("Usuário desativado");
            }

            String cargo = usuario.getFuncionario().getCargo().getNome();
            Integer nivel = usuario.getFuncionario()
                    .getCargo()
                    .getNivelAcesso();
            String token = JWTUtil.gerarToken(usuario.getLogin(),cargo,nivel);

            usuario.setSenha(null);
            LoginResponse response =
                    new LoginResponse(token, usuario);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(500)
                    .body(e.getMessage());
        } finally {
            con.desconectar();
        }
    }
}
