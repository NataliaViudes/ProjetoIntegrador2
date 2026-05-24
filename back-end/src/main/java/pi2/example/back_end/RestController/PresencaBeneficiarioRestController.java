package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.PresencaBeneficiarioControl;
import pi2.example.back_end.Modelo.PresencaBeneficiario;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/presencas")
public class PresencaBeneficiarioRestController {
    private final PresencaBeneficiarioControl control = new PresencaBeneficiarioControl();

    @GetMapping("/{idAgendamento}")
    public ResponseEntity<?> getPorAgendamento(@PathVariable Integer idAgendamento) {
        return control.buscaPorIdAgendamento(idAgendamento);
    }

    @GetMapping("/relatorio/beneficiario/{idBeneficiario}")
    public ResponseEntity<?> relatorioFaltas(@PathVariable Integer idBeneficiario) {
        return control.buscarFaltasPorBeneficiario(idBeneficiario);
    }

    @GetMapping("/relatorio/beneficiario/{idBeneficiario}/pdf")
    public ResponseEntity<?> relatorioFaltasPdf(@PathVariable Integer idBeneficiario) {
        byte[] pdf = control.gerarPdfFaltas(idBeneficiario);

        if (pdf == null) {
            return ResponseEntity.badRequest().body("Erro ao gerar PDF");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Relatorio_Faltas.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody List<PresencaBeneficiario> presencas) {
        return control.salvar(presencas);
    }
}
