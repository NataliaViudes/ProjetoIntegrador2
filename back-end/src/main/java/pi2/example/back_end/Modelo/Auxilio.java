package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.AuxilioDAOImpl;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class Auxilio {
    private Integer id;
    private String descricao;
    private String data;
    private String status;
    private Beneficiario beneficiario;
    private CategoriaAuxilio categoria;

    public Auxilio() {
    }

    public Auxilio(Integer id, String descricao, String data, String status, Beneficiario beneficiario, CategoriaAuxilio categoria) {
        this.id = id;
        this.descricao = descricao;
        this.beneficiario = beneficiario;
        this.categoria = categoria;
        this.data = data;
        this.status = status;
    }

    public Auxilio(Integer id) {
        this.id = id;
        this.descricao = "";
        this.beneficiario = null;
        this.categoria = null;
        this.data = "";
        this.status = "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Auxilio incluir(Conexao con) {
        AuxilioDAOImpl dao = new AuxilioDAOImpl(con);
        return dao.gravar(this);
    }

    public Auxilio alterar(Conexao con) {
        AuxilioDAOImpl dao = new AuxilioDAOImpl(con);
        return dao.alterar(this);
    }

    public boolean apagar(Conexao con) {
        AuxilioDAOImpl dao = new AuxilioDAOImpl(con);
        return dao.apagar(this);
    }

    public Auxilio buscarPorId(Integer id, Conexao con) {
        AuxilioDAOImpl dao = new AuxilioDAOImpl(con);
        return dao.get(id);
    }

    public List<Auxilio> buscarTodos(Conexao con) {
        AuxilioDAOImpl dao = new AuxilioDAOImpl(con);
        return dao.getAll();
    }

    public List<Auxilio> buscarComFiltro(String filtro, Conexao con) {
        AuxilioDAOImpl dao = new AuxilioDAOImpl(con);
        return dao.get(filtro);
    }
}