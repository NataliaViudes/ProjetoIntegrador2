package pi2.example.back_end.RestController;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.DALEvento;
import pi2.example.back_end.Modelo.Evento;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/evento")
public class RestControler {

    private final DALEvento dalevento;

    public RestControler() {
        // Inicializa DAO com a conexão singleton do Banco
        Banco.conectar();  // garante que conecta
        this.dalevento = new DALEvento(Banco.getCon());
    }

    @GetMapping("{id}")
    public ResponseEntity<Evento> getId(@PathVariable int id) {
        Evento eve = dalevento.get(id);
        if (eve != null)
            return ResponseEntity.ok(eve);
        else
            return ResponseEntity.notFound().build();
    }


    @GetMapping
    public ResponseEntity<List<Evento>> getAll(@RequestParam(required = false) String filtro) {
        List<Evento> eventos = dalevento.get(filtro);
        return ResponseEntity.ok(eventos);
    }
}