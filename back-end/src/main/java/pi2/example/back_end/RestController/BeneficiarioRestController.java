package pi2.example.back_end.RestController;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.BeneficiarioControl;
import pi2.example.back_end.Modelo.Beneficiario;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/beneficiarios")
public class BeneficiarioRestController {

    private final BeneficiarioControl controll = new BeneficiarioControl();

    // -------------------- GET ALL --------------------
    @GetMapping
    public ResponseEntity<?> getAll() {
        return controll.getAll();
    }

    // -------------------- GET POR ID --------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable Integer id) {
        return controll.getById(id);
    }

    // -------------------- BUSCAS --------------------

    @GetMapping("/nome")
    public ResponseEntity<?> getNome(@RequestParam(required = false) String nome) {
        return controll.buscaPorNome(nome);
    }

    @GetMapping("/cpf")
    public ResponseEntity<?> getCpf(@RequestParam(required = false) String cpf) {
        return controll.buscaPorCpf(cpf);
    }

    @GetMapping("/nis")
    public ResponseEntity<?> getNis(@RequestParam(required = false) String nis) {
        return controll.buscaPorNis(nis);
    }


    // -------------------- CRUD --------------------

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Beneficiario b) {
        return controll.incluir(b);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Beneficiario b) {
        return controll.update(b);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return controll.delete(id);
    }

    // ================= PDF INDIVIDUAL =================

    @GetMapping("/{id}/pdf")
    public ResponseEntity<?> gerarPdf(
            @PathVariable Integer id
    ) {

        byte[] pdf = controll.gerarPdf(id);

        if (pdf == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Erro ao gerar PDF");
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=beneficiario_" + id + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // ================= RELATÓRIO =================

    @GetMapping("/relatorio")
    public ResponseEntity<byte[]> gerarRelatorioBeneficiarios(

            @RequestParam(required = false)
            Boolean ativo,

            @RequestParam(required = false)
            String faixaEtaria,

            @RequestParam(required = false)
            Boolean ordemJudicial,

            @RequestParam(required = false)
            String atividade
    ) {

        byte[] pdf = controll.gerarRelatorioBeneficiarios(
                ativo,
                faixaEtaria,
                ordemJudicial,
                atividade
        );

        if (pdf == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=Relatorio_Beneficiarios.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}