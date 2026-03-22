package pi2.example.back_end.Modelo;

public class Atividade {
    private Integer id;
    private String descricao;
    private CategoriaAtividade categoria;
    private Funcionario funcionario;

    public Atividade() {}

    public Atividade(Integer id, String descricao, CategoriaAtividade categoria, Funcionario funcionario) {
        this.id = id;
        this.descricao = descricao;
        this.categoria = categoria;
        this.funcionario = funcionario;
    }

    public Integer getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public CategoriaAtividade getCategoria() { return categoria; }
    public void setCategoria(CategoriaAtividade categoria) { this.categoria = categoria; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }
}
