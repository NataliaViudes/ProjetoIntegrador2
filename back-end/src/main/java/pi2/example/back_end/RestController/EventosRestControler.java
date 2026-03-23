package pi2.example.back_end.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Service.EventoService;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.Modelo.Evento;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/eventos")
public class EventosRestControler {

    private final EventoService service = new EventoService();

    @GetMapping("/{id}")
    public ResponseEntity<Evento> getId(@PathVariable int id) {
        Evento model= service.buscarPorId(id,Banco.getCon());
        if (model != null)
            return ResponseEntity.ok(model);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Evento>> getNome(@RequestParam (required = false) String categoria) {
        List<Evento> eventos = service.buscaPorCategoria(categoria,Banco.getCon());
        if (!eventos.isEmpty())
            return ResponseEntity.ok(eventos);
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Evento> salvar(@RequestBody Evento evento) {
         Evento eve = service.salvar(evento,Banco.getCon());
         if(eve!=null)
            return ResponseEntity.ok(evento);
         else
            return ResponseEntity.badRequest().build();
    }

    @PutMapping
    public ResponseEntity<Evento> update(@RequestBody Evento evento) {
        Evento eve =service.alterar(evento,Banco.getCon());
        if(eve!=null)
            return ResponseEntity.ok(evento);
        else
            return  ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return service.deletar(id,Banco.getCon()) ?
                ResponseEntity.ok().body("Evento "+id+" Deletado!")
                : ResponseEntity.badRequest().build();
    }

}