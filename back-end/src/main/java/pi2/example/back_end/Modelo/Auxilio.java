package pi2.example.back_end.Modelo;

public class Auxilio {

    private Integer id;
    private String descricao;

    private Beneficiario beneficiario;
    private CategoriaAuxilio categoria;

    public Auxilio() {
    }

    public Auxilio(Integer id, String descricao, Beneficiario beneficiario, CategoriaAuxilio categoria) {
        this.id = id;
        this.descricao = descricao;
        this.beneficiario = beneficiario;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Beneficiario getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Beneficiario beneficiario) {
        this.beneficiario = beneficiario;
    }

    public CategoriaAuxilio getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaAuxilio categoria) {
        this.categoria = categoria;
    }
}
