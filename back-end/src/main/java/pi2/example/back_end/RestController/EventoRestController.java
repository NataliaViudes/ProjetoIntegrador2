package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Controller.EventoControl;
import pi2.example.back_end.Modelo.Evento;

@CrossOrigin
@RestController
@RequestMapping("/evento")
public class EventoRestController {

    private final EventoControl control = new EventoControl();


    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable int id) {
        return control.getById(id);
    }

    @GetMapping("/nome")
    public ResponseEntity<?> getNome(@RequestParam(required = false) String nome) {
        return control.buscarPorNome(nome);
    }

    @GetMapping("/local")
    public ResponseEntity<?> getLocal(@RequestParam(required = false) String local) {
        return control.buscarPorLocal(local);
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Evento evento) {
        return control.incluir(evento);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Evento evento) {
        return control.alterar(evento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return control.delete(id);
    }


}
