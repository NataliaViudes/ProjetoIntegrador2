package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.EtapaControl;
import pi2.example.back_end.Modelo.Etapa;


@RestController
@RequestMapping("/etapas")
@CrossOrigin(origins = "http://localhost:3000")
public class EtapaRestController {
    private final EtapaControl control = new EtapaControl();

    @PostMapping
    public ResponseEntity<?> inserir(@RequestBody Etapa etapa) {
        return control.incluir(etapa);
    }

    @GetMapping("/{idAgendamento}")
    public ResponseEntity<?> listar(@PathVariable(value = "idAgendamento") int idAgendamento) {
        return control.listarPorAgendamento(idAgendamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable(value = "id") int id) {
        return control.excluir(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable(value = "id") int id,@RequestBody Etapa etapa) {
        etapa.setId(id);
        return control.atualizar(etapa);
    }
}