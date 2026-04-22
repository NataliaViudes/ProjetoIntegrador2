package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.AgendamentoAtividadeControl;
import pi2.example.back_end.Modelo.AgendamentoAtividade;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoAtividadeRestController {

    private final AgendamentoAtividadeControl control = new AgendamentoAtividadeControl();

    @GetMapping("{id}")
    public ResponseEntity<?> getId(@PathVariable int id) {
        return control.getById(id);
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(required = false) String filtro) {
        return control.getAllOrFilter(filtro);
    }

    @PostMapping
    public ResponseEntity<?> gravar(@RequestBody AgendamentoAtividade agendamento) {
        return control.incluir(agendamento);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> apagar(@PathVariable int id) {
        return control.delete(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> alterar(@PathVariable int id, @RequestBody AgendamentoAtividade agendamento) {
        agendamento.setId(id);
        return control.update(agendamento);
    }
}
