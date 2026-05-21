import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../../Services/api";
import Menu from "../../Components/Menu/Menu.jsx";
import "./Familiares.css";

function Familiares() {

    const { idBeneficiario } = useParams();

    const [familiares, setFamiliares] = useState([]);
    const [busca, setBusca] = useState("");

    const [tela, setTela] = useState("tabela");
    const [editando, setEditando] = useState(false);

    const [form, setForm] = useState({
        id: null,
        nome: "",
        parentesco: "",
        profissao: "",
        renda: "",
        telefone: ""
    });

    useEffect(() => {
        carregar();
    }, [idBeneficiario]);

    // ================= UTIL =================

    function limparNumero(valor) {
        return String(valor || "").replace(/\D/g, "");
    }

    function mascaraTelefone(valor) {
        valor = limparNumero(valor).slice(0, 11);

        if (valor.length <= 2) return valor;
        if (valor.length <= 7) {
            return `(${valor.slice(0, 2)}) ${valor.slice(2)}`;
        }

        return `(${valor.slice(0, 2)}) ${valor.slice(2, 7)}-${valor.slice(7)}`;
    }

    function mascaraRenda(valor) {
        valor = limparNumero(valor);

        if (!valor) return "";

        valor = (parseInt(valor) / 100).toFixed(2) + "";
        valor = valor.replace(".", ",");
        valor = valor.replace(/\B(?=(\d{3})+(?!\d))/g, ".");

        return "R$ " + valor;
    }

    // ================= API =================

    async function carregar() {
        try {

            // futuramente trocar para:
            // /familiares/beneficiario/${idBeneficiario}

            const resp = await api.get(
                `/familiares/beneficiario/${idBeneficiario}`
            );

            setFamiliares(Array.isArray(resp.data)
                ? resp.data
                : []);

        } catch (e) {
            console.error(e);
        }
    }

    // ================= HANDLE =================

    function handleChange(e) {

        const { name, value } = e.target;

        let novoValor = value;

        if (name === "telefone") {
            novoValor = mascaraTelefone(value);
        }

        if (name === "renda") {
            novoValor = mascaraRenda(value);
        }

        setForm({
            ...form,
            [name]: novoValor
        });
    }

    function verDetalhes(f) {

        setForm({
            ...f,
            telefone: mascaraTelefone(f.telefone),
            renda: mascaraRenda(f.renda)
        });

        setEditando(false);
        setTela("detalhes");
    }

    function novo() {

        setForm({
            id: null,
            nome: "",
            parentesco: "",
            profissao: "",
            renda: "",
            telefone: ""
        });

        setEditando(true);
        setTela("cadastro");
    }

    // ================= SALVAR =================

    async function salvar() {

        const payload = {
            ...form,
            idBeneficiario: idBeneficiario,
            telefone: limparNumero(form.telefone),
            renda: limparNumero(form.renda)
        };

        try {

            if (tela === "cadastro") {

                await api.post("/familiares", payload);

            } else {

                await api.put(`/familiares/${form.id}`, payload);
            }

            setTela("tabela");

            carregar();

        } catch (e) {

            console.error(e);

            alert("Erro ao salvar familiar");
        }
    }

    // ================= DELETE =================

    async function deletar(id) {

        if (!window.confirm("Deseja excluir o familiar?")) {
            return;
        }

        try {

            await api.delete(`/familiares/${id}`);

            carregar();

        } catch (e) {

            console.error(e);

            alert("Erro ao excluir");
        }
    }

    // ================= FILTRO =================

    const filtrados = familiares.filter(f =>
        (f.nome || "")
            .toLowerCase()
            .includes(busca.toLowerCase())
    );

    // ================= TABELA =================

    if (tela === "tabela") {

        return (
            <div className="pagina-familiar">

                <Menu />

                <div className="topo">

                    <input
                        placeholder="Buscar familiar..."
                        value={busca}
                        onChange={(e) => setBusca(e.target.value)}
                    />

                </div>

                <div className="tabela">

                    <table>

                        <thead>
                            <tr>
                                <th>Nome</th>
                                <th>Parentesco</th>
                                <th>Telefone</th>
                                <th>Ações</th>
                            </tr>
                        </thead>

                        <tbody>

                            {filtrados.map(f => (

                                <tr key={f.id}>

                                    <td>{f.nome}</td>

                                    <td>{f.parentesco}</td>

                                    <td>{mascaraTelefone(f.telefone)}</td>

                                    <td>

                                        <button
                                            onClick={() => verDetalhes(f)}
                                        >
                                            Ver
                                        </button>

                                        <button
                                            onClick={() => deletar(f.id)}
                                        >
                                            Excluir
                                        </button>

                                    </td>

                                </tr>

                            ))}

                        </tbody>

                    </table>

                </div>

                <div className="rodape-familiar">
                    <button onClick={() => window.history.back()}>
                        Voltar para Beneficiários
                    </button>

                    <button onClick={novo}>
                        Cadastrar Familiar
                    </button>
                </div>

            </div>
        );
    }

    // ================= FORM =================

    return (

        <div className="form-familiar">

            <Menu />

            <h2>
                {tela === "cadastro"
                    ? "Cadastro de Familiar"
                    : "Detalhes do Familiar"}
            </h2>

            <div className="form-grid">

                <div className="campo">
                    <label>Nome</label>

                    <input
                        name="nome"
                        value={form.nome}
                        disabled={!editando}
                        onChange={handleChange}
                    />
                </div>

                <div className="campo">

                    <label>Parentesco</label>

                    <select
                        name="parentesco"
                        value={form.parentesco}
                        disabled={!editando}
                        onChange={handleChange}
                    >

                        <option value="">
                            Selecione
                        </option>

                        <option value="Irmão/Irmã">
                            Irmão/Irmã
                        </option>

                        <option value="Pai/Mãe">
                            Pai/Mãe
                        </option>

                        <option value="Tio/Tia">
                            Tio/Tia
                        </option>

                        <option value="Neto/Neta">
                            Neto/Neta
                        </option>

                        <option value="Filho/Filha">
                            Filho/Filha
                        </option>

                        <option value="Primo/Prima">
                            Primo/Prima
                        </option>

                        <option value="Outros">
                            Outros
                        </option>

                    </select>

                </div>

                <div className="campo">
                    <label>Profissão</label>

                    <input
                        name="profissao"
                        value={form.profissao}
                        disabled={!editando}
                        onChange={handleChange}
                    />
                </div>

                <div className="campo">
                    <label>Telefone</label>

                    <input
                        name="telefone"
                        value={form.telefone}
                        disabled={!editando}
                        onChange={handleChange}
                    />
                </div>

                <div className="campo">
                    <label>Renda</label>

                    <input
                        name="renda"
                        value={form.renda}
                        disabled={!editando}
                        onChange={handleChange}
                    />
                </div>

            </div>

            <div className="botoes">

                {!editando && (

                    <button
                        onClick={() => setEditando(true)}
                    >
                        Alterar
                    </button>
                )}

                {editando && (

                    <button onClick={salvar}>
                        Salvar
                    </button>
                )}

                <button
                    onClick={() => setTela("tabela")}
                >
                    Voltar
                </button>

            </div>

        </div>
    );
}

export default Familiares;