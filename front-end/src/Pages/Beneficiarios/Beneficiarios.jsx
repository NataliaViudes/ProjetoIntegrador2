import { useEffect, useState } from "react";
import api from "../../services/api.js";
import Menu from "../../components/Menu/Menu.jsx";
import "./Beneficiarios.css";
import { Link } from "react-router-dom";

function Beneficiario() {
    const usuario = JSON.parse(localStorage.getItem("usuario"));
    const nivel = usuario?.funcionario?.cargo?.nivelAcesso || 1;

    const [filtroSituacao, setFiltroSituacao] = useState("");
    const [filtroFaixaEtaria, setFiltroFaixaEtaria] = useState("");
    const [filtroOrdemJudicial, setFiltroOrdemJudicial] = useState("");
    const [filtroAtividade, setFiltroAtividade] = useState("");

    const [lista, setLista] = useState([]);
    const [busca, setBusca] = useState("");

    const [tela, setTela] = useState("tabela");
    const [editando, setEditando] = useState(false);
    const [erros, setErros] = useState({});

    const [form, setForm] = useState({
        id: null,
        nome: "",
        cpf: "",
        rg: "",
        telefone: "",
        nascimento: "",
        endereco: "",
        bairro: "",
        situacao: "",
        idade: "",
        celular: "",
        celularRecado: "",
        tipoResidencia: "",
        nis: "",
        renda: "",
        participacao: "",
        alergias: "",
        tratamentos: "",
        medicamentos: null
    });

    useEffect(() => {
        carregar();
    }, []);

    // ================= UTIL =================
    function limparNumero(valor) {
        return String(valor || "").replace(/\D/g, "");
    }

    function calcularIdade(dataNascimento) {

        if (!dataNascimento) return "";

        const hoje = new Date();
        const nascimento = new Date(dataNascimento);

        let idade = hoje.getFullYear() - nascimento.getFullYear();

        const mes = hoje.getMonth() - nascimento.getMonth();

        if (
            mes < 0 ||
            (mes === 0 && hoje.getDate() < nascimento.getDate())
        ) {
            idade--;
        }

        return idade;
    }


    // ================= MÁSCARAS =================
    function mascaraTelefone(valor) {
        valor = limparNumero(valor).slice(0, 11);

        if (valor.length <= 2) return valor;
        if (valor.length <= 7) return `(${valor.slice(0, 2)}) ${valor.slice(2)}`;
        return `(${valor.slice(0, 2)}) ${valor.slice(2, 7)}-${valor.slice(7)}`;
    }

    function mascaraCPF(valor) {
        valor = limparNumero(valor).slice(0, 11);

        return valor
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d{1,2})$/, "$1-$2");
    }

    function mascaraRG(valor) {
        valor = limparNumero(valor).slice(0, 9);

        return valor
            .replace(/(\d{2})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d{1})$/, "$1-$2");
    }

    function mascaraNIS(valor) {
        valor = limparNumero(valor).slice(0, 11);

        return valor
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d{1,2})$/, "$1-$2");
    }

    function mascaraRenda(valor) {
        valor = limparNumero(valor);

        if (!valor) return "";

        valor = (parseInt(valor) / 100).toFixed(2) + "";
        valor = valor.replace(".", ",");
        valor = valor.replace(/\B(?=(\d{3})+(?!\d))/g, ".");

        return "R$ " + valor;
    }

    // ================= FORMATAR =================
    const formatarCPF = (v) => mascaraCPF(v);
    const formatarRG = (v) => mascaraRG(v);
    const formatarTelefone = (v) => mascaraTelefone(v);
    const formatarNIS = (v) => mascaraNIS(v);
    const formatarRenda = (v) => mascaraRenda(v);

    // ================= API =================
    async function carregar() {
        try {
            const resp = await api.get("/beneficiarios");
            setLista(Array.isArray(resp.data) ? resp.data : []);
        } catch (e) {
            console.error(e);
        }
    }

    // ================= HANDLE =================
    function handleChange(e) {

        const { name, value } = e.target;

        let novoValor = value;

        if (
            name === "telefone" ||
            name === "celular" ||
            name === "celularRecado"
        ) {
            novoValor = mascaraTelefone(value);
        }

        if (name === "cpf") {
            novoValor = mascaraCPF(value);
        }

        if (name === "rg") {
            novoValor = mascaraRG(value);
        }

        if (name === "nis") {
            novoValor = mascaraNIS(value);
        }

        if (name === "renda") {
            novoValor = mascaraRenda(value);
        }

        let novoForm = {
            ...form,
            [name]: novoValor
        };

        // CALCULAR IDADE AUTOMATICAMENTE
        if (name === "nascimento") {
            novoForm.idade = calcularIdade(value);
        }

        setForm(novoForm);

        // REMOVE ERRO AO DIGITAR
        if (erros[name]) {
            setErros({
                ...erros,
                [name]: ""
            });
        }
    }

    async function verDetalhes(b) {
        try {
            const resp = await api.get(`/beneficiarios/${b.id}`);
            const dados = resp.data;

            setForm({
                id: dados.id || null,
                nome: dados.nome || "",
                cpf: formatarCPF(dados.cpf || ""),
                rg: formatarRG(dados.rg || ""),
                telefone: formatarTelefone(dados.telefone || ""),
                nascimento: dados.nascimento || "",
                endereco: dados.endereco || "",
                bairro: dados.bairro || "",
                situacao: dados.situacao || "",
                idade: dados.idade || calcularIdade(dados.nascimento),
                celular: formatarTelefone(dados.celular || ""),
                celularRecado: formatarTelefone(dados.celularRecado || ""),
                tipoResidencia: dados.tipoResidencia || "",
                nis: formatarNIS(dados.nis || ""),
                renda: formatarRenda(dados.renda || ""),
                participacao: dados.participacao || "",
                alergias: dados.alergias || "",
                tratamentos: dados.tratamentos || "",
                medicamentos: dados.medicamentos || null
            });

            setErros({});
            setEditando(false);
            setTela("detalhes");

        } catch (e) {
            console.error(e);
            alert("Erro ao carregar os dados do beneficiário");
        }
    }

    function novo() {

        setForm({
            id: null,
            nome: "",
            cpf: "",
            rg: "",
            telefone: "",
            nascimento: "",
            endereco: "",
            bairro: "",
            situacao: "",
            idade: "",
            celular: "",
            celularRecado: "",
            tipoResidencia: "",
            nis: "",
            renda: "",
            participacao: "",
            alergias: "",
            tratamentos: "",
            medicamentos: null
        });

        setErros({});

        setEditando(true);

        setTela("cadastro");
    }

    // ================= VALIDAÇÃO =================
    function validarCampos() {

        let novosErros = {};

        const camposObrigatorios = [
            "nome",
            "cpf",
            "rg",
            "telefone",
            "nascimento",
            "endereco",
            "bairro",
            "situacao",
            "idade",
            "celular",
            "tipoResidencia",
            "nis",
            "renda",
            "participacao"
        ];

        camposObrigatorios.forEach(campo => {

            if (
                !form[campo] ||
                form[campo].toString().trim() === ""
            ) {
                novosErros[campo] = "Campo obrigatório";
            }

        });

        setErros(novosErros);

        return Object.keys(novosErros).length === 0;
    }

    // ================= SALVAR =================
    async function salvar() {

        if (!validarCampos()) return;

        const payload = {
            ...form,
            cpf: limparNumero(form.cpf),
            rg: limparNumero(form.rg),
            telefone: limparNumero(form.telefone),
            celular: limparNumero(form.celular),
            celularRecado: limparNumero(form.celularRecado),
            nis: limparNumero(form.nis),
            renda: limparNumero(form.renda)
        };

        try {
            if (!form.id) {
                await api.post("/beneficiarios", payload);
            } else {
                await api.put(`/beneficiarios/${form.id}`, payload);
            }

            setTela("tabela");
            carregar();

        } catch (e) {
            console.error(e);
            alert("Erro ao salvar");
        }
    }

    async function deletar(id) {
        if (!window.confirm("Deseja excluir o beneficiário?")) return;

        await api.delete(`/beneficiarios/${id}`);
        carregar();
    }

    async function baixarPdf(beneficiario) {
        try {
            const response = await api.get(
                `/beneficiarios/${beneficiario.id}/pdf`,
                {
                    responseType: "blob"
                }
            );

            const url = window.URL.createObjectURL(
                new Blob([response.data], {
                    type: "application/pdf"
                })
            );

            const link = document.createElement("a");
            link.href = url;

            const primeiroNome = beneficiario.nome.split(" ")[0];

            link.download = `Relatorio_${primeiroNome}.pdf`;

            document.body.appendChild(link);
            link.click();
            link.remove();

            window.URL.revokeObjectURL(url);

        } catch (e) {
            console.error(e);
            alert("Erro ao gerar PDF");
        }
    }

    async function verDetalhesFuncionario(funcionario) {
        try {
            const resp = await api.get(`/funcionarios/${funcionario.id}`);
            const dados = resp.data;

            setForm({
                id: dados.id || null,
                nome: dados.nome || "",
                cpf: formatarCPF(dados.cpf || ""),
                rg: formatarRG(dados.rg || ""),
                telefone: formatarTelefone(dados.telefone || ""),
                email: dados.email || "",
                endereco: dados.endereco || "",
                cargo: dados.cargo || "",
                nascimento: dados.nascimento || ""
            });

            setErros({});
            setEditando(false);
            setTela("detalhes");

        } catch (e) {
            console.error(e);
            alert("Erro ao carregar os dados do funcionário");
        }
    }

    function normalizarTexto(valor) {
        return String(valor ?? "")
            .trim()
            .toLowerCase()
            .normalize("NFD")
            .replace(/[\u0300-\u036f]/g, "");
    }

    function obterSituacao(valor) {
        const texto = normalizarTexto(valor);

        if (texto.includes("inativo")) return "inativo";
        if (texto.includes("ativo")) return "ativo";

        if (texto === "false" || texto === "0") return "inativo";
        if (texto === "true" || texto === "1") return "ativo";

        return texto;
    }

    function obterIdade(beneficiario) {
        const idade = Number(beneficiario.idade);

        if (!Number.isNaN(idade) && idade > 0) {
            return idade;
        }

        return Number(calcularIdade(beneficiario.nascimento));
    }

    function estaNaFaixaEtaria(beneficiario, faixa) {
        if (!faixa) return true;

        const idade = obterIdade(beneficiario);

        if (Number.isNaN(idade)) return false;

        if (faixa === "80+") {
            return idade >= 80;
        }

        const [min, max] = faixa.split("-").map(Number);

        return idade >= min && idade <= max;
    }

    async function gerarRelatorioBeneficiario() {
        try {
            console.log("LISTA ORIGINAL:", lista);
            console.log("TOTAL ORIGINAL:", lista.length);
            console.log("FILTRO SITUAÇÃO:", filtroSituacao);
            console.log("FILTRO FAIXA:", filtroFaixaEtaria);

            const beneficiariosFiltrados = lista.filter((beneficiario) => {
                const situacaoOk =
                    !filtroSituacao ||
                    obterSituacao(beneficiario.situacao) === obterSituacao(filtroSituacao);

                const faixaEtariaOk = estaNaFaixaEtaria(
                    beneficiario,
                    filtroFaixaEtaria
                );

                console.log({
                    nome: beneficiario.nome,
                    situacao: beneficiario.situacao,
                    situacaoNormalizada: obterSituacao(beneficiario.situacao),
                    idade: beneficiario.idade,
                    idadeCalculada: obterIdade(beneficiario),
                    situacaoOk,
                    faixaEtariaOk
                });

                return situacaoOk && faixaEtariaOk;
            });

            console.log("FILTRADOS:", beneficiariosFiltrados);
            console.log("TOTAL FILTRADO:", beneficiariosFiltrados.length);

            if (beneficiariosFiltrados.length === 0) {
                alert("Nenhum beneficiário encontrado para os filtros selecionados.");
                return;
            }

            const response = await api.post(
                "/beneficiarios/relatorio",
                beneficiariosFiltrados,
                {
                    responseType: "blob"
                }
            );

            const url = window.URL.createObjectURL(
                new Blob([response.data], { type: "application/pdf" })
            );

            const link = document.createElement("a");
            link.href = url;
            link.setAttribute("download", "Relatorio_Beneficiarios.pdf");

            document.body.appendChild(link);
            link.click();
            link.remove();

            window.URL.revokeObjectURL(url);

        } catch (e) {
            console.error(e);
            alert("Erro ao gerar relatório");
        }
    }

    const filtrados = lista.filter(b =>
        (b.nome || "").toLowerCase().includes(busca.toLowerCase())
    );

    if (nivel < 3) {
        return (
            <div>
                <Menu />
                <h2 style={{ padding: "20px" }}>
                    Você não possui acesso a esta página.
                </h2>
            </div>
        );
    }
    // ================= TABELA =================
    if (tela === "tabela") {
        return (
            <div className="beneficiario-page">

                <Menu />

                <div className="topbar">

                    <input
                        placeholder="Buscar beneficiário..."
                        onChange={(e) => setBusca(e.target.value)}
                    />

                    <div className="relatorio-beneficiario">

                        <select
                            value={filtroSituacao}
                            onChange={(e) => setFiltroSituacao(e.target.value)}
                        >
                            <option value="">Todas as situações</option>
                            <option value="Ativo">Ativos</option>
                            <option value="Inativo">Inativos</option>
                        </select>

                        <select
                            value={filtroFaixaEtaria}
                            onChange={(e) => setFiltroFaixaEtaria(e.target.value)}
                        >
                            <option value="">Todas as idades</option>
                            <option value="60-65">60 a 65 anos</option>
                            <option value="65-70">65 a 70 anos</option>
                            <option value="70-75">70 a 75 anos</option>
                            <option value="75-80">75 a 80 anos</option>
                            <option value="80+">Mais de 80 anos</option>
                        </select>

                        <button onClick={gerarRelatorioBeneficiario}>
                            Gerar Relatório
                        </button>

                    </div>

                </div>

                <div className="table-card">
                    <table>
                        <thead>
                            <tr>
                                <th>Nome</th>
                                <th>CPF</th>
                                <th>Situação</th>
                                <th>Ações</th>
                            </tr>
                        </thead>

                        <tbody>
                            {filtrados.map(b => (
                                <tr key={b.id}>
                                    <td>{b.nome}</td>
                                    <td>{formatarCPF(b.cpf)}</td>
                                    <td>{b.situacao}</td>
                                    <td>

                                        <button onClick={() => verDetalhes(b)}>
                                            Ver
                                        </button>

                                        <button onClick={() => deletar(b.id)}>
                                            Excluir
                                        </button>

                                        <button onClick={() => baixarPdf(b)}>
                                            Relatório
                                        </button>

                                        <Link to={`/familiares/${b.id}`}>
                                            <button>Familiares</button>
                                        </Link>

                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                <div className="rodape">
                    <button onClick={novo}>Cadastrar Beneficiário</button>
                </div>

            </div>
        );
    }

    // ================= FORM =================
    return (
        <div className="beneficiario-page">

            <Menu />

            <div className="form-header">

                <div>
                    <h1>
                        {tela === "cadastro"
                            ? "Cadastro de Beneficiário"
                            : "Detalhes do Beneficiário"}
                    </h1>

                    <p>
                        Gerencie informações dos beneficiários do sistema
                    </p>
                </div>

            </div>

            <div className="form-card">

                <div className="form-grid">

                    <div className="campo grande">
                        <label>Nome</label>
                        <input name="nome" value={form.nome} disabled={!editando} onChange={handleChange}/>

                        {erros.nome && (
                            <span className="erro">
                                {erros.nome}
                            </span>
                        )}                
                    </div>

                    <div className="campo medio">
                        <label>CPF</label>
                        <input name="cpf" value={form.cpf} disabled={!editando} onChange={handleChange} />

                        {erros.cpf && (
                            <span className="erro">
                                {erros.cpf}
                            </span>
                        )} 
                    </div>

                    <div className="campo medio">
                        <label>RG</label>
                        <input name="rg" value={form.rg} disabled={!editando} onChange={handleChange} />
                        {erros.rg && (
                            <span className="erro">
                                {erros.rg}
                            </span>
                        )} 
                    </div>

                    <div className="campo pequeno">
                        <label>Nascimento</label>
                        <input type="date" name="nascimento" value={form.nascimento} disabled={!editando} onChange={handleChange} />
                        {erros.nascimento && (
                            <span className="erro">
                                {erros.nascimento}
                            </span>
                        )} 
                    </div>

                    <div className="campo mini">
                        <label>Idade</label>
                        <input name="idade" value={form.idade} disabled={!editando} onChange={handleChange} />
                        {erros.idade && (
                            <span className="erro">
                                {erros.idade}
                            </span>
                        )} 
                    </div>

                    <div className="campo medio">
                        <label>Telefone</label>
                        <input name="telefone" value={form.telefone} disabled={!editando} onChange={handleChange} />
                        {erros.telefone && (
                            <span className="erro">
                                {erros.telefone}
                            </span>
                        )} 
                    </div>

                    <div className="campo medio">
                        <label>Celular</label>
                        <input name="celular" value={form.celular} disabled={!editando} onChange={handleChange} />
                        {erros.celular && (
                            <span className="erro">
                                {erros.celular}
                            </span>
                        )} 
                    </div>

                    <div className="campo medio">
                        <label>Celular Recado</label>
                        <input name="celularRecado" value={form.celularRecado} disabled={!editando} onChange={handleChange} />
                        {erros.celularRecado && (
                            <span className="erro">
                                {erros.celularRecado}
                            </span>
                        )} 
                    </div>

                    <div className="campo grande">
                        <label>Endereço</label>
                        <input name="endereco" value={form.endereco} disabled={!editando} onChange={handleChange} />
                        {erros.endereco && (
                            <span className="erro">
                                {erros.endereco}
                            </span>
                        )} 
                    </div>

                    <div className="campo medio">
                        <label>Bairro</label>
                        <input name="bairro" value={form.bairro} disabled={!editando} onChange={handleChange} />
                        {erros.bairro && (
                            <span className="erro">
                                {erros.bairro}
                            </span>
                        )} 
                    </div>

                    <div className="campo medio">
                        <label>Tipo de Residência</label>
                        <input name="tipoResidencia" value={form.tipoResidencia} disabled={!editando} onChange={handleChange} />
                        {erros.tipoResidencia && (
                            <span className="erro">
                                {erros.tipoResidencia}
                            </span>
                        )} 
                    </div>

                    <div className="campo medio">
                        <label>NIS</label>
                        <input name="nis" value={form.nis} disabled={!editando} onChange={handleChange} />
                        {erros.nis && (
                            <span className="erro">
                                {erros.nis}
                            </span>
                        )} 
                    </div>

                    <div className="campo medio">
                        <label>Renda</label>
                        <input name="renda" value={form.renda} disabled={!editando} onChange={handleChange} />
                        {erros.renda && (
                            <span className="erro">
                                {erros.renda}
                            </span>
                        )} 
                    </div>

                    <div className="campo medio">
                        <label>Participação</label>
                        <input name="participacao" value={form.participacao} disabled={!editando} onChange={handleChange} />
                        {erros.participacao && (
                            <span className="erro">
                                {erros.participacao}
                            </span>
                        )} 
                    </div>

                    <div className="campo medio">
                        <label>Situação</label>
                        <input name="situacao" value={form.situacao} disabled={!editando} onChange={handleChange} />
                        {erros.situacao && (
                            <span className="erro">
                                {erros.situacao}
                            </span>
                        )} 
                    </div>

                    <div className="campo grande">
                        <label>Alergias</label>
                        <input name="alergias" value={form.alergias} disabled={!editando} onChange={handleChange} />
                        {erros.alergias && (
                            <span className="erro">
                                {erros.alergias}
                            </span>
                        )} 
                    </div>

                    <div className="campo grande">
                        <label>Tratamentos</label>
                        <input name="tratamentos" value={form.tratamentos} disabled={!editando} onChange={handleChange} />
                        {erros.tratamentos && (
                            <span className="erro">
                                {erros.tratamentos}
                            </span>
                        )} 
                    </div>
                </div>            
            </div>

            <div className="acoes-form">
                {!editando && (
                    <button
                        className="btn btn-primary"
                        onClick={() => setEditando(true)}
                    >
                        Alterar
                    </button>
                )}

                {editando && (
                    <button
                        className="btn btn-success"
                        onClick={salvar}
                    >
                        Salvar
                    </button>
                )}

                <button
                    className="btn btn-secondary"
                    onClick={() => setTela("tabela")}
                >
                    Voltar
                </button>
            </div>

        </div>
    );
}

export default Beneficiario;