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

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Cardapio c) {
        return control.incluir(c);
    }
}