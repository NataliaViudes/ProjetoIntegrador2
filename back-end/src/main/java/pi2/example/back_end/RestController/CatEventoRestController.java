package pi2.example.back_end.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Controller.Cat_EventoControl;
import pi2.example.back_end.Modelo.Cat_Evento;


@CrossOrigin
@RestController
@RequestMapping("/cat-evento")
public class CatEventoRestController {

    private final Cat_EventoControl controll = new Cat_EventoControl();


    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable int id) {
       return  controll.getById(id);
    }

    @GetMapping("categoria")
    public ResponseEntity<?> getCategoria(@RequestParam (required = false) String categoria) {
       return controll.buscaPorCategoria(categoria);
    }

    @GetMapping("descricao")
    public ResponseEntity<?> getDescricao(@RequestParam (required = false) String descricao) {
        return controll.buscaPorDescricao(descricao);
    }


    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Cat_Evento cat_evento) {
        return controll.incluir(cat_evento);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Cat_Evento cat_evento) {
        return controll.update(cat_evento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return controll.delete(id);
    }

}