package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.DAOEvento;
import pi2.example.back_end.db.Conexao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Evento {

    private Integer idEvento;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private String nome;
    private String local;
    private Integer qtd;
    private Cat_Evento categoria;
    private Funcionario funcionario;

    public Evento() {
    }

    public Evento(Integer id, LocalDateTime inicio, LocalDateTime fim, String nome, String local, Integer qtd, Cat_Evento categoria, Funcionario funcionario) {
        this.idEvento = id;
        this.inicio = inicio;
        this.fim = fim;
        this.nome = nome;
        this.local = local;
        this.qtd = qtd;
        this.categoria = categoria;
        this.funcionario = funcionario;
    }

    public Evento(LocalDateTime inicio, LocalDateTime fim, String nome, String local, Integer qtd, Cat_Evento categoria, Funcionario funcionario) {
        this.inicio = inicio;
        this.fim = fim;
        this.nome = nome;
        this.local = local;
        this.qtd = qtd;
        this.categoria = categoria;
        this.funcionario = funcionario;
    }

    // =====================================================
    // GETTERS E SETTERS
    // =====================================================


    public Integer getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    public Cat_Evento getCategoria() {
        return categoria;
    }

    public void setCategoria(Cat_Evento categoria) {
        this.categoria = categoria;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    // =====================================================
    // Validaçao
    // =====================================================

    public String validar() {

        //List<String> erros = new ArrayList<>();
        String erros="";
        if (categoria == null || categoria.getId() == null || categoria.getId() <= 0)
            erros += "Categoria inválida\n";

        if (qtd == null || qtd <= 0)
            erros += "Quantidade inválida";

        if (nome == null || nome.trim().isEmpty())
            erros += "Nome inválido";

        if (local == null || local.trim().isEmpty())
            erros += "Local inválido";

        if (inicio == null)
            erros += "Data/hora inicial obrigatória";

        if (fim == null)
            erros += "Data/hora final obrigatória";

        if (inicio != null && fim != null) {

            if (!fim.isAfter(inicio))
                erros += "Data final deve ser após a inicial";

            if (inicio.isBefore(LocalDateTime.now()))
                erros += "Não é permitido criar eventos no passado";
        }

        return erros;
    }



    // =====================================================
    // DAO
    // =====================================================

    public List<Evento> listar(Conexao con) {
        DAOEvento dao = new DAOEvento(con);
        return dao.listar();
    }

    public Evento buscarPorId(Conexao con, int id) {
        DAOEvento dao = new DAOEvento(con);
        return dao.buscarPorId(id);
    }

    public List<Evento> buscarPorNome(Conexao con, String nome) {
        DAOEvento dao = new DAOEvento(con);
        return dao.buscarPorNome(nome);
    }

    public List<Evento> buscarPorLocal(Conexao con, String local) {
        DAOEvento dao = new DAOEvento(con);
        return dao.buscarPorLocal(local);
    }

    public Evento incluir(Conexao con) {
        DAOEvento dao = new DAOEvento(con);
        return dao.gravar(this);
    }

    public Evento alterar(Conexao con) {
        DAOEvento dao = new DAOEvento(con);
        return dao.alterar(this);
    }

    public Boolean apagar(Conexao con) {
        DAOEvento dao = new DAOEvento(con);
        return dao.apagar(this);
    }

    public List<Evento> buscarPorPeriodo(Conexao con, LocalDateTime inicio, LocalDateTime fim) {
        DAOEvento dao = new DAOEvento(con);
        return dao.buscarPorPeriodo(inicio, fim);
    }

    // =====================================================
    // AUXILIARES
    // =====================================================



    public Integer getIdCatEvento() {

        if (categoria == null)
            return null;

        return categoria.getId();
    }

    public boolean possuiFuncionario() {
        return getIdFuncionario() != null;
    }

    public Integer getIdFuncionario() {

        if (funcionario == null)
            return null;

        return funcionario.getId();
    }
}