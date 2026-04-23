package pi2.example.back_end.Modelo;

import java.util.List;

public class ListaBeneficiario {
    private List<VincularBeneficiario> listaBeneficiario;

    public ListaBeneficiario() {
    }

    public ListaBeneficiario(List<VincularBeneficiario> listaBeneficiario) {
        this.listaBeneficiario = listaBeneficiario;
    }

    public List<VincularBeneficiario> getListaBeneficiario() {
        return listaBeneficiario;
    }

    public void setListaBeneficiario(List<VincularBeneficiario> listaBeneficiario) {
        this.listaBeneficiario = listaBeneficiario;
    }

    public VincularBeneficiario getElemento(int i){
        return listaBeneficiario.get(i);
    }
}
