package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Controller.FamiliarControl;
import pi2.example.back_end.Modelo.Familiar;

@CrossOrigin
@RestController
@RequestMapping("/familiares")
public class FamiliarRestController {
    private final FamiliarControl control = new FamiliarControl();

    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable int id) {
        return  control.getById(id);
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam (required = false) String filtro){
        return control.getAllOrFilter(filtro);
    }

    @GetMapping("nome")
    public ResponseEntity<?> getNome(@RequestParam (required = false) String nome) {
        return control.buscaPorNome(nome);
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Familiar familiar) {
        return control.incluir(familiar);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Familiar familiar) {
        familiar.setId(id);
        return control.update(familiar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return control.delete(id);
    }
}
