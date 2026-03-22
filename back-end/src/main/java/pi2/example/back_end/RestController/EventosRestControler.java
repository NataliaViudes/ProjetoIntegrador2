package pi2.example.back_end.RestController;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Service.EventoService;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;
import pi2.example.back_end.db.DALEvento;
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

    @GetMapping("/nome")
    public ResponseEntity<List<Evento>> getNome(@RequestParam (required = false) String nome) {
        List<Evento> eventos = service.buscarPorNome(nome,Banco.getCon());
        if (!eventos.isEmpty())
            return ResponseEntity.ok(eventos);
        else
            return ResponseEntity.notFound().build();
    }


    @GetMapping
    public ResponseEntity<List<Evento>> getAll(@RequestParam(required = false) String filtro) {
        List<Evento> eventos =service.buscarPorNome("",Banco.getCon());
        return ResponseEntity.ok(eventos);
    }

    @PostMapping
    public ResponseEntity<Evento> salvar(@RequestBody Evento evento) {
        return service.salvar(evento,Banco.getCon()) ?
                ResponseEntity.ok(evento)
                : ResponseEntity.badRequest().build();
    }

    @PutMapping
    public ResponseEntity<Evento> update(@RequestBody Evento evento) {
        return service.alterar(evento,Banco.getCon()) ?
                ResponseEntity.ok(evento)
                : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return service.deletar(id,Banco.getCon()) ?
                ResponseEntity.ok().body("Evento "+id+" Deletado!")
                : ResponseEntity.badRequest().build();
    }

}