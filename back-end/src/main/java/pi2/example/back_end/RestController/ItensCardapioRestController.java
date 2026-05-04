package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.ItensCardapioControl;
import pi2.example.back_end.Modelo.ItensCardapio;

@RestController
@RequestMapping("/itens-cardapio")
public class ItensCardapioRestController {

    private final ItensCardapioControl control = new ItensCardapioControl();

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody ItensCardapio item) {
        return control.incluir(item);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody ItensCardapio item) {
        return control.delete(item);
    }

    @GetMapping("/{idCardapio}")
    public ResponseEntity<?> getByCardapio(@PathVariable int idCardapio) {
        return control.getByCardapio(idCardapio);
    }
}