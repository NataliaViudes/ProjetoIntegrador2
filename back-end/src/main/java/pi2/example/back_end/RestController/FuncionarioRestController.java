package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import pi2.example.back_end.Control.FuncionarioControl;
import pi2.example.back_end.Modelo.Funcionario;

@CrossOrigin()
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioRestController {
    private final FuncionarioControl control = new FuncionarioControl();

    @GetMapping("{id}")
    public ResponseEntity<?> getId(@PathVariable int id) {
        return  control.getById(id);
    }

    @GetMapping("nome")
    public ResponseEntity<?> getNome(@RequestParam (required = false) String nome) {
        return control.buscaPorNome(nome);
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(value = "tipo", required = false) String tipo,@RequestParam(value = "filtro", required = false) String filtro) {
        return control.getAllOrFilter(tipo, filtro);
    }

    @PostMapping
    public ResponseEntity<?> gravar(@RequestBody Funcionario funcionario){
        return control.incluir(funcionario);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> apagar(@PathVariable int id) {
        return control.delete(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> alterar(@PathVariable int id, @RequestBody Funcionario funcionario) {
        funcionario.setId(id);
        return control.update(funcionario);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<?> gerarPdf(@PathVariable Integer id) {
        byte[] pdf = control.gerarPdf(id);
        if (pdf == null) {
            return ResponseEntity.badRequest().body("Erro ao gerar PDF");
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=funcionario_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/relatorio/cargo/{idCargo}")
    public ResponseEntity<byte[]> gerarRelatorioCargo(@PathVariable Integer idCargo) {
        byte[] pdf = control.gerarRelatorioCargo(idCargo);
        if (pdf == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=Relatorio_Cargo.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
