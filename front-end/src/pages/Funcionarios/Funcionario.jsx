import { useEffect, useState } from "react";
import api from "../../services/api";
import "./Funcionario.css";

function Funcionario() {

    const [funcionarios, setFuncionarios] = useState([]);
    const [busca, setBusca] = useState("");

    const [tela, setTela] = useState("tabela"); // tabela | detalhes | cadastro
    const [editando, setEditando] = useState(false);

    const [erros, setErros] = useState({});

    const [form, setForm] = useState({
        id: null,
        nome: "",
        cpf: "",
        telefone: "",
        nis: "",
        nascimento: "",
        sexo: "",
        endereco: "",
        cargo: ""
    });

    useEffect(() => {
        carregar();
    }, []);

    async function carregar() {
        try {
            const resp = await api.get("/funcionario");
            setFuncionarios(Array.isArray(resp.data) ? resp.data : []);
        } catch (e) {
            console.error(e);
        }
    }

    function formatCPF(value) {
        return value
            .replace(/\D/g, "")
            .slice(0, 11)
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d{1,2})$/, "$1-$2");
    }

    function formatTelefone(value) {
        return value
            .replace(/\D/g, "")
            .slice(0, 11)
            .replace(/(\d{2})(\d)/, "($1) $2")
            .replace(/(\d{5})(\d{1,4})$/, "$1-$2");
    }

    function handleChange(e) {
        let { name, value } = e.target;

        if (name === "cpf") value = formatCPF(value);
        if (name === "telefone") value = formatTelefone(value);

        setForm({ ...form, [name]: value });
    }

    function formatarDataParaInput(data) {
        if (!data) return "";

        // se vier tipo "2000-01-01T00:00:00"
        if (data.includes("T")) {
            return data.split("T")[0];
        }

        // se vier tipo "dd/mm/yyyy"
        if (data.includes("/")) {
            const [dia, mes, ano] = data.split("/");
            return `${ano}-${mes}-${dia}`;
        }

        return data;
    }

    function formatCPFView(value) {
        if (!value) return "";

        return value
            .replace(/\D/g, "")
            .slice(0, 11)
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d{1,2})$/, "$1-$2");
    }

    function formatTelefoneView(value) {
        if (!value) return "";

        return value
            .replace(/\D/g, "")
            .slice(0, 11)
            .replace(/(\d{2})(\d)/, "($1) $2")
            .replace(/(\d{5})(\d{1,4})$/, "$1-$2");
    }

    function verDetalhes(f) {
        setForm({
            ...f,
            nascimento: formatarDataParaInput(f.nascimento),
            cpf: formatCPFView(f.cpf),
            telefone: formatTelefoneView(f.telefone),
            nis: f.nis ? f.nis.replace(/\D/g, "") : ""
        });

        setEditando(false);
        setTela("detalhes");
    }

    function novo() {
        setForm({
            nome: "", cpf: "", telefone: "", nis: "",
            nascimento: "", sexo: "", endereco: "", cargo: ""
        });
        setEditando(true);
        setTela("cadastro");
    }

    function limparNumero(valor) {
        return valor.replace(/\D/g, "");
    }

    async function salvar() {
        let novosErros = {};

        if (!form.nome) novosErros.nome = "Nome é obrigatório";
        if (!form.cargo) novosErros.cargo = "Cargo é obrigatório";

        if (!form.cpf) {
            novosErros.cpf = "CPF é obrigatório";
        } else if (form.cpf.length < 14) {
            novosErros.cpf = "CPF inválido";
        }

        if (!form.telefone && limparNumero(form.telefone).length < 10) {
            novosErros.telefone = "Telefone inválido";
        }

        if (!form.sexo) novosErros.sexo = "Sexo obrigatório";
        if (!form.nascimento) novosErros.nascimento = "Data obrigatória";

        if (!form.endereco) novosErros.endereco = "Endereço obrigatório";

        if (Object.keys(novosErros).length > 0) {
            setErros(novosErros);
            return;
        }

        if (Object.keys(novosErros).length > 0) {
            setErros(novosErros);
            return;
        }

        setErros({});

        const payload = {
            ...form,
            cpf: limparNumero(form.cpf),
            telefone: limparNumero(form.telefone),
            nis: form.nis ? limparNumero(form.nis) : null
        };

        try {
            if (tela === "cadastro") {
                await api.post("/funcionario", payload);
            } else {
                await api.put(`/funcionario/${form.id}`, payload);
            }

            setTela("tabela");
            carregar();

        } catch (e) {
            console.error(e);
            alert("Erro ao salvar");
        }
    }

    async function deletar(id) {
        if (!window.confirm("Deseja desligar o funcionário?")) return;

        await api.delete(`/funcionario/${id}`);
        carregar();
    }

    const filtrados = funcionarios.filter(f =>
        f.nome.toLowerCase().includes(busca.toLowerCase())
    );

    // tela === "tabela" -> tabela de funcionários
    if (tela === "tabela") {
        return (
            <div className="pagina-funcionario">
                <header className="topo-menu">
                    <div className="grupo-botoes">
                        <button>Cadastros</button>
                        <button>Vincular</button>
                        <button>Agendador</button>
                        <button>Relatórios</button>
                    </div>

                    <div className="area-pesquisa-topo">
                        <input type="text" placeholder="Pesquisar..." />
                    </div>
                </header>

                <div className="topo">
                    <input
                        placeholder="Buscar funcionário..."
                        onChange={(e) => setBusca(e.target.value)}
                    />
                </div>

                <div className="tabela">
                    <table>
                        <thead>
                            <tr>
                                <th>Nome</th>
                                <th>Cargo</th>
                                <th>Ações</th>
                            </tr>
                        </thead>

                        <tbody>
                            {filtrados.map(f => (
                                <tr key={f.id}>
                                    <td>{f.nome}</td>
                                    <td>{f.cargo}</td>
                                    <td>
                                        <button onClick={() => verDetalhes(f)}>Ver</button>
                                        <button onClick={() => deletar(f.id)}>Desligar</button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                <div className="rodape">
                    <button onClick={novo}>Cadastrar Funcionário</button>
                </div>

            </div>
        );
    }

    // tela === "detalhes" | "cadastro" -> formulário de detalhes ou cadastro
    return (
        <div className="form-funcionario">

            <h2 className="titulo-form">
                {tela === "cadastro" ? "Cadastro de Funcionário" : "Detalhes do Funcionário"}
            </h2>

            <div className="form-grid">

                <div className="campo">
                    <label>Nome</label>
                    <input
                        name="nome"
                        value={form.nome || ""}
                        disabled={!editando}
                        onChange={handleChange}
                        className={erros.nome ? "input-erro" : ""}
                    />
                    {erros.nome && <span className="erro-texto">{erros.nome}</span>}
                </div>

                <div className="campo">
                    <label>Cargo</label>
                    <input
                        name="cargo"
                        value={form.cargo || ""}
                        disabled={!editando}
                        onChange={handleChange}
                        className={erros.cargo ? "input-erro" : ""}
                    />
                    {erros.cargo && <span className="erro-texto">{erros.cargo}</span>}
                </div>

                <div className="campo">
                    <label>CPF</label>
                    <input
                        name="cpf"
                        value={form.cpf || ""}
                        disabled={!editando}
                        onChange={handleChange}
                        className={erros.cpf ? "input-erro" : ""}
                    />
                    {erros.cpf && <span className="erro-texto">{erros.cpf}</span>}
                </div>

                <div className="campo">
                    <label>Telefone</label>
                    <input
                        name="telefone"
                        value={form.telefone || ""}
                        disabled={!editando}
                        onChange={handleChange}
                        className={erros.telefone ? "input-erro" : ""}
                    />
                    {erros.telefone && <span className="erro-texto">{erros.telefone}</span>}
                </div>

                <div className="campo">
                    <label>Sexo</label>
                    <select
                        name="sexo"
                        value={form.sexo || ""}
                        disabled={!editando}
                        onChange={handleChange}
                        className={erros.sexo ? "input-erro" : ""}
                    >
                        <option value="">Selecione</option>
                        <option value="M">Masculino</option>
                        <option value="F">Feminino</option>
                        <option value="O">Outro</option>
                    </select>
                    {erros.sexo && <span className="erro-texto">{erros.sexo}</span>}
                </div>

                <div className="campo">
                    <label>Data de Nascimento</label>
                    <input
                        type="date"
                        name="nascimento"
                        value={form.nascimento || ""}
                        disabled={!editando}
                        onChange={handleChange}
                        className={erros.nascimento ? "input-erro" : ""}
                    />
                    {erros.nascimento && <span className="erro-texto">{erros.nascimento}</span>}
                </div>

                <div className="campo full">
                    <label>Endereço</label>
                    <input
                        name="endereco"
                        value={form.endereco || ""}
                        disabled={!editando}
                        onChange={handleChange}
                        className={erros.endereco ? "input-erro" : ""}
                    />
                    {erros.endereco && <span className="erro-texto">{erros.endereco}</span>}
                </div>

                <div className="campo">
                    <label>NIS</label>
                    <input
                        name="nis"
                        value={form.nis || ""}
                        disabled={!editando}
                        onChange={handleChange}
                        className={erros.nis ? "input-erro" : ""}
                    />
                    {erros.nis && <span className="erro-texto">{erros.nis}</span>}
                </div>

            </div>

            <div className="botoes">

                {!editando && (
                    <button onClick={() => setEditando(true)}>
                        Alterar
                    </button>
                )}

                {editando && (
                    <button onClick={salvar}>
                        Salvar
                    </button>
                )}

                <button onClick={() => {
                    if (window.confirm("Deseja voltar?"))
                        setTela("tabela");
                }}>
                    Voltar
                </button>

            </div>
        </div>
    );
}

export default Funcionario;