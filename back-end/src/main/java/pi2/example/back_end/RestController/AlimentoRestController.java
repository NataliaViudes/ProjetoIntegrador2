package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Controller.AlimentoControl;
import pi2.example.back_end.Modelo.Alimento;

@CrossOrigin
@RestController
@RequestMapping("/alimento")
public class AlimentoRestController {

    private final AlimentoControl control = new AlimentoControl();

    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable int id) {
        return control.getById(id);
    }

    @GetMapping("tipo")
    public ResponseEntity<?> getTipo(@RequestParam(required = false) String tipo) {
        return control.buscaPorTipo(tipo);
    }

    @GetMapping("descricao")
    public ResponseEntity<?> getDescricao(@RequestParam(required = false) String descricao) {
        return control.buscaPorDescricao(descricao);
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Alimento alimento) {
        return control.incluir(alimento);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Alimento alimento) {
        return control.alterar(alimento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return control.delete(id);
    }
}