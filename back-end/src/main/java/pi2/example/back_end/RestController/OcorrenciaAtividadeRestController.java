package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.OcorrenciaAtividadeControl;
import pi2.example.back_end.Modelo.OcorrenciaAtividade;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/ocorrencias")
public class OcorrenciaAtividadeRestController {
    private final OcorrenciaAtividadeControl control = new OcorrenciaAtividadeControl();

    @PostMapping
    public ResponseEntity<?> gravar(@RequestBody OcorrenciaAtividade ocorrencia) {
        return control.incluir(ocorrencia);
    }

    @GetMapping("/relatorio")
    public ResponseEntity<?> relatorio(
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            @RequestParam(required = false) Integer idBeneficiario
    ) {
        return control.relatorio(dataInicio, dataFim, idBeneficiario);
    }
}
