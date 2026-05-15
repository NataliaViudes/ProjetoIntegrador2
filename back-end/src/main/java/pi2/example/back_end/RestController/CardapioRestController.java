package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.CardapioControl;
import pi2.example.back_end.Modelo.Cardapio;

@RestController
@RequestMapping("/cardapio")
@CrossOrigin
public class CardapioRestController {

    private final CardapioControl control = new CardapioControl();

    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable(value = "id") int id) {
        return control.getById(id);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return control.getAll();
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Cardapio cardapio) {
        return control.incluir(cardapio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") int id, @RequestBody Cardapio cardapio) {
        cardapio.setId(id);
        return control.update(cardapio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Integer id) {
        return control.delete(id);
    }

}