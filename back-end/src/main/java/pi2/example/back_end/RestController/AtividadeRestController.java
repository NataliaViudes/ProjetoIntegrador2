package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.AtividadeControl;
import pi2.example.back_end.Modelo.Atividade;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/atividades")
public class AtividadeRestController {

    private final AtividadeControl control = new AtividadeControl();

    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable("id") int id) {
        return control.getById(id);
    }

    @GetMapping("/nome")
    public ResponseEntity<?> getNome(@RequestParam(name = "nome", required = false) String nome) {
        return control.buscaPorNome(nome);
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(name = "filtro", required = false) String filtro) {
        return control.getAllOrFilter(filtro);
    }

    @PostMapping
    public ResponseEntity<?> gravar(@RequestBody Atividade atividade) {
        return control.incluir(atividade);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagar(@PathVariable("id") int id) {
        return control.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> alterar(@PathVariable("id") int id, @RequestBody Atividade atividade) {
        atividade.setId(id);
        return control.update(atividade);
    }

    @GetMapping("/relatorio")
    public ResponseEntity<?> relatorio(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer idCategoria
    ) {
        return control.relatorio(status, idCategoria);
    }
}