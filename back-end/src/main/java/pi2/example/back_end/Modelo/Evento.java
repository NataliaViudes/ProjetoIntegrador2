package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.DAOEvento;
import pi2.example.back_end.db.Conexao;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Evento {
    private Integer id;
    private LocalDate data;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private String nome;
    private String local;
    private Integer qtd;
    private Integer idCatEvento;
    private Integer idFuncionario;

    public Evento() {
    }

    public Evento(LocalDate data, LocalTime horaInicio, LocalTime horaFim, String nome, String local, Integer qtd, Integer idCatEvento, Integer idFuncionario) {
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.nome = nome;
        this.local = local;
        this.qtd = qtd;
        this.idCatEvento = idCatEvento;
        this.idFuncionario = idFuncionario;
    }

    public Evento(Integer id, LocalDate data, LocalTime horaInicio, LocalTime horaFim, String nome, String local, Integer qtd, Integer idCatEvento, Integer idFuncionario) {
        this.id = id;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.nome = nome;
        this.local = local;
        this.qtd = qtd;
        this.idCatEvento = idCatEvento;
        this.idFuncionario = idFuncionario;
    }

    public Integer getIdCatEvento() {
        return idCatEvento;
    }

    public void setIdCatEvento(Integer idCatEvento) {
        this.idCatEvento = idCatEvento;
    }

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
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





}
