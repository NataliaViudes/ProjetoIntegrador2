package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.ItensCardapioControl;
import pi2.example.back_end.Modelo.Alimento;
import pi2.example.back_end.Modelo.Cardapio;
import pi2.example.back_end.Modelo.ItensCardapio;

@CrossOrigin
@RestController
@RequestMapping("/itens-cardapio")
public class ItensCardapioRestController {

    private final ItensCardapioControl control = new ItensCardapioControl();

    @PutMapping
    public ResponseEntity<?> atualizar(@RequestBody ItensCardapio item) {
        return control.alterar(item);
    }

    @DeleteMapping("/{idCardapio}/{idAlimento}")
    public ResponseEntity<?> delete(
            @PathVariable(value = "idCardapio") int idCardapio,
            @PathVariable(value = "idAlimento") int idAlimento) {
        ItensCardapio item = new ItensCardapio();
        item.setCardapio(new Cardapio());
        item.getCardapio().setId(idCardapio);

        item.setAlimento(new Alimento());
        item.getAlimento().setId(idAlimento);

        return control.delete(item);
    }

    @PostMapping
    public ResponseEntity<?> salvarOuAtualizar(@RequestBody ItensCardapio item) {
        return control.salvarOuAtualizar(item);
    }

    @GetMapping("/{idCardapio}")
    public ResponseEntity<?> getByCardapio(@PathVariable(value = "idCardapio") int idCardapio) {
        return control.getByCardapio(idCardapio);
    }
}