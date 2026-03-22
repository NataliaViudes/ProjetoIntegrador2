package pi2.example.back_end;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pi2.example.back_end.db.Banco;

@SpringBootApplication
public class BackEndApplication {

	public static void main(String[] args) {

		//incializa o singletom de forma melhor pq tem varios restController ent pode dar ruim cada restIniciar uma nova conexao
		Banco.conectar();
		SpringApplication.run(BackEndApplication.class, args);
	}

}
