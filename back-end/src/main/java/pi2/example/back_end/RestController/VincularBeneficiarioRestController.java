package pi2.example.back_end.RestController;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.VincularBeneficiarioControl;
import pi2.example.back_end.Modelo.ListaBeneficiario;
import pi2.example.back_end.Modelo.VincularBeneficiario;


@CrossOrigin
@RestController
@RequestMapping("/vincularBeneficiario")
public class VincularBeneficiarioRestController {

    private final VincularBeneficiarioControl control = new VincularBeneficiarioControl();

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody ListaBeneficiario lb) {
        return control.incluir(lb);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable VincularBeneficiario vb) {
        return control.apagar(vb);
    }


}
