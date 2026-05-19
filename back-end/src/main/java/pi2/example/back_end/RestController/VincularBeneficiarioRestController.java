package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.VincularBeneficiarioControl;
import pi2.example.back_end.Modelo.VincularBeneficiario;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/vincularBeneficiario")
public class VincularBeneficiarioRestController {

    private final VincularBeneficiarioControl control = new VincularBeneficiarioControl();

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody List<VincularBeneficiario> lb) {
        return control.incluir(lb);
    }

    @GetMapping("/{idAgendamento}")
    public ResponseEntity<?> getPorAgenda(@PathVariable Integer idAgendamento) {
        return control.buscaPorIdAgendamento(idAgendamento);
    }


    @DeleteMapping("/{idAgendamento}")
    public ResponseEntity<?> delete(@PathVariable Integer idAgendamento) {
        return control.apagarPorAgendamento(idAgendamento);
    }

    @GetMapping("/agendamento/{idAgendamento}")
    public ResponseEntity<?> buscarBeneficiariosPorAgendamento(
            @PathVariable int idAgendamento
    ) {
        return control.buscarBeneficiariosPorAgendamento(idAgendamento);
    }

}