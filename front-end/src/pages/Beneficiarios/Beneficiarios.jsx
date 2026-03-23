import { useEffect, useState } from "react";
import api from "../../services/api";
import Menu from "../../components/Menu";
import "./Beneficiarios.css";

function Beneficiario() {

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
        tratamentos: ""
    });

    useEffect(() => {
        carregar();
    }, []);

    // ================= UTIL =================
    function limparNumero(valor) {
        return String(valor || "").replace(/\D/g, "");
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
            const resp = await api.get("/beneficiario");
            setLista(Array.isArray(resp.data) ? resp.data : []);
        } catch (e) {
            console.error(e);
        }
    }

    // ================= HANDLE =================
    function handleChange(e) {
        const { name, value } = e.target;
        let novoValor = value;

        if (name === "telefone" || name === "celular" || name === "celularRecado") {
            novoValor = mascaraTelefone(value);
        }

        if (name === "cpf") novoValor = mascaraCPF(value);
        if (name === "rg") novoValor = mascaraRG(value);
        if (name === "nis") novoValor = mascaraNIS(value);
        if (name === "renda") novoValor = mascaraRenda(value);

        setForm({ ...form, [name]: novoValor });
    }

    function verDetalhes(b) {
        setForm({
            ...b,
            cpf: formatarCPF(b.cpf),
            rg: formatarRG(b.rg),
            telefone: formatarTelefone(b.telefone),
            celular: formatarTelefone(b.celular),
            celularRecado: formatarTelefone(b.celularRecado),
            nis: formatarNIS(b.nis),
            renda: formatarRenda(b.renda)
        });

        setEditando(false);
        setTela("detalhes");
    }

    function novo() {
        setForm({
            nome: "", cpf: "", rg: "", telefone: "",
            nascimento: "", endereco: "", bairro: "",
            situacao: "", idade: "", celular: "",
            celularRecado: "", tipoResidencia: "",
            nis: "", renda: "", participacao: "",
            alergias: "", tratamentos: ""
        });
        setEditando(true);
        setTela("cadastro");
    }

    // ================= VALIDAÇÃO =================
    function validarCampos() {
        let novosErros = {};

        Object.keys(form).forEach(campo => {
            if (campo !== "id") {
                if (!form[campo] || form[campo].toString().trim() === "") {
                    novosErros[campo] = "Campo obrigatório";
                }
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
            if (tela === "cadastro") {
                await api.post("/beneficiario", payload);
            } else {
                await api.put(`/beneficiario/${form.id}`, payload);
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

        await api.delete(`/beneficiario/${id}`);
        carregar();
    }

    const filtrados = lista.filter(b =>
        (b.nome || "").toLowerCase().includes(busca.toLowerCase())
    );

    // ================= TABELA =================
    if (tela === "tabela") {
        return (
            <div className="pagina-funcionario">

                <Menu />

                <div className="topo">
                    <input
                        placeholder="Buscar beneficiário..."
                        onChange={(e) => setBusca(e.target.value)}
                    />
                </div>

                <div className="tabela">
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
                                        <button onClick={() => verDetalhes(b)}>Ver</button>
                                        <button onClick={() => deletar(b.id)}>Excluir</button>
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
        <div className="form-funcionario">

            <Menu />

            <h2>
                {tela === "cadastro"
                    ? "Cadastro de Beneficiário"
                    : "Detalhes do Beneficiário"}
            </h2>

            <div className="form-grid">

                <div className="campo grande">
                    <label>Nome</label>
                    <input name="nome" value={form.nome} disabled={!editando} onChange={handleChange} />
                </div>

                <div className="campo medio">
                    <label>CPF</label>
                    <input name="cpf" value={form.cpf} disabled={!editando} onChange={handleChange} />
                </div>

                <div className="campo medio">
                    <label>RG</label>
                    <input name="rg" value={form.rg} disabled={!editando} onChange={handleChange} />
                </div>

                <div className="campo pequeno">
                    <label>Nascimento</label>
                    <input type="date" name="nascimento" value={form.nascimento} disabled={!editando} onChange={handleChange} />
                </div>

                <div className="campo mini">
                    <label>Idade</label>
                    <input name="idade" value={form.idade} disabled={!editando} onChange={handleChange} />
                </div>

                <div className="campo medio">
                    <label>Telefone</label>
                    <input name="telefone" value={form.telefone} disabled={!editando} onChange={handleChange} />
                </div>

                <div className="campo medio">
                    <label>Celular</label>
                    <input name="celular" value={form.celular} disabled={!editando} onChange={handleChange} />
                </div>

                <div className="campo medio">
                    <label>Celular Recado</label>
                    <input name="celularRecado" value={form.celularRecado} disabled={!editando} onChange={handleChange} />
                </div>

                <div className="campo grande">
                    <label>Endereço</label>
                    <input name="endereco" value={form.endereco} disabled={!editando} onChange={handleChange} />
                </div>

                <div className="campo medio">
                    <label>Bairro</label>
                    <input name="bairro" value={form.bairro} disabled={!editando} onChange={handleChange} />
                </div>

                <div className="campo medio">
                    <label>Tipo de Residência</label>
                    <input name="tipoResidencia" value={form.tipoResidencia} disabled={!editando} onChange={handleChange} />
                </div>

                <div className="campo medio">
                    <label>NIS</label>
                    <input name="nis" value={form.nis} disabled={!editando} onChange={handleChange} />
                </div>

                <div className="campo medio">
                    <label>Renda</label>
                    <input name="renda" value={form.renda} disabled={!editando} onChange={handleChange} />
                </div>

                <div className="campo medio">
                    <label>Participação</label>
                    <input name="participacao" value={form.participacao} disabled={!editando} onChange={handleChange} />
                </div>

                <div className="campo medio">
                    <label>Situação</label>
                    <input name="situacao" value={form.situacao} disabled={!editando} onChange={handleChange} />
                </div>

                <div className="campo grande">
                    <label>Alergias</label>
                    <input name="alergias" value={form.alergias} disabled={!editando} onChange={handleChange} />
                </div>

                <div className="campo grande">
                    <label>Tratamentos</label>
                    <input name="tratamentos" value={form.tratamentos} disabled={!editando} onChange={handleChange} />
                </div>

            </div>

            <div className="botoes">
                {!editando && <button onClick={() => setEditando(true)}>Alterar</button>}
                {editando && <button onClick={salvar}>Salvar</button>}
                <button onClick={() => setTela("tabela")}>Voltar</button>
            </div>

        </div>
    );
}

export default Beneficiario;