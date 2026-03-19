package pi2.example.back_end;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pi2.example.back_end.db.Banco;

@SpringBootApplication
public class BackEndApplication {

	public static void main(String[] args) {
		if (!Banco.conectar()) {
			System.out.println("Erro ao conectar com o banco!");
			return;
		}

		System.out.println("Banco conectado com sucesso!");
		SpringApplication.run(BackEndApplication.class, args);
	}

}
