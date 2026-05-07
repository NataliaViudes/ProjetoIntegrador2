package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.EtapaControl;
import pi2.example.back_end.DAO.DAOEtapa;
import pi2.example.back_end.Modelo.Etapa;
import pi2.example.back_end.db.Banco;


@RestController
@RequestMapping("/etapas")
@CrossOrigin("*")
public class RestControllerEtapa {

    private final EtapaControl control = new EtapaControl(null);

    @PostMapping
    public ResponseEntity<?> inserir(@RequestBody Etapa etapa) {
        return control.incluir(etapa);
    }

    @GetMapping("/{idAgendamento}")
    public ResponseEntity<?> listar(@PathVariable("idAgendamento") int idAgendamento) {
        return control.listarPorAgendamento(idAgendamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") int id) {
        return control.excluir(id);
    }
}