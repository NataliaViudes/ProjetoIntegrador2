package pi2.example.back_end;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TesteSenha {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senha = encoder.encode("admin123");
        System.out.println(senha);
    }
}