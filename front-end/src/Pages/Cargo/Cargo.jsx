import { useEffect, useState } from "react";
import api from "../../services/api.js";
import Menu from "../../components/Menu/Menu.jsx";
import "./Cargo.css";

function Cargos() {
    const usuario = JSON.parse(localStorage.getItem("usuario"));
    const nivelUsuario = usuario?.funcionario?.cargo?.nivelAcesso || 1;

    const [cargos, setCargos] = useState([]);
    const [busca, setBusca] = useState("");

    const [nome, setNome] = useState("");
    const [nivel, setNivel] = useState(1);
    const [editando, setEditando] = useState(null);

    const [erro, setErro] = useState("");

    useEffect(() => {
        carregar();
    }, []);

    async function carregar() {
        try {
            const resp = await api.get("/cargos");
            setCargos(Array.isArray(resp.data) ? resp.data : []);
        } catch (e) {
            console.error(e);
        }
    }

    function limpar() {
        setNome("");
        setNivel(1);
        setEditando(null);
        setErro("");
    }

    function editar(cargo) {
        setNome(cargo.nome);
        setNivel(cargo.nivelAcesso || 1);
        setEditando(cargo);
    }

    async function salvar() {
        if (!nome) {
            setErro("Nome é obrigatório");
            return;
        }

        setErro("");

        try {
            if (editando) {
                await api.put("/cargos", {
                    id: editando.id,
                    nome,
                    nivelAcesso: nivel
                });
            } else {
                await api.post("/cargos", {
                    nome,
                    nivelAcesso: nivel
                });
            }

            await carregar();
            limpar();

        } catch (e) {
            console.error(e);
            alert("Erro ao salvar");
        }
    }

    async function excluir(id) {
        if (!window.confirm("Deseja excluir o cargo?")) return;

        try {
            await api.delete(`/cargos/${id}`);
            carregar();
        } catch (e) {
            console.error(e);
            alert("Erro ao excluir");
        }
    }

    const filtrados = cargos.filter(c =>
        c.nome.toLowerCase().includes(busca.toLowerCase())
    );

    if (nivelUsuario < 3) {
        return (
            <div>
                <Menu />
                <h2 style={{ padding: "20px" }}>
                    Você não possui acesso a esta página.
                </h2>
            </div>
        );
    }
    return (
        <div className="pagina-cargos">
            <Menu />

            <div className="topo">
                <input
                    placeholder="Buscar cargo..."
                    onChange={(e) => setBusca(e.target.value)}
                />
            </div>

            <div className="form">
                <input
                    placeholder="Nome do cargo"
                    value={nome}
                    onChange={(e) => {
                        setNome(e.target.value);
                        setErro("");
                    }}
                    className={erro ? "input-erro" : ""}
                />

                <select
                    value={nivel}
                    onChange={(e) => setNivel(Number(e.target.value))}
                >
                    <option value={1}>Nível 1</option>
                    <option value={2}>Nível 2</option>
                    <option value={3}>Nível 3</option>
                </select>

                {erro && <span className="erro-texto">{erro}</span>}

                <button onClick={salvar}>
                    {editando ? "Atualizar" : "Cadastrar"}
                </button>

                {editando && (
                    <button onClick={limpar}>
                        Cancelar
                    </button>
                )}
            </div>

            <div className="lista">
                {filtrados.map(c => (
                    <div key={c.id} className="item">
                        <div>
                            <strong>{c.nome}</strong>
                            <div>Nível: {c.nivelAcesso}</div>
                        </div>

                        <div className="acoes">
                            <button onClick={() => editar(c)}>
                                Editar
                            </button>

                            <button onClick={() => excluir(c.id)}>
                                Excluir
                            </button>
                        </div>
                    </div>
                ))}
            </div>

        </div>
    );
}

export default Cargos;