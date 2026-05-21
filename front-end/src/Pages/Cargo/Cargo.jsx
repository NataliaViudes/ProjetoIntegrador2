import { useEffect, useState } from "react";
import api from "../../Services/api";
import Menu from "../../Components/Menu/Menu.jsx";
import "./Cargo.css";

function Cargos() {

    const [cargos, setCargos] = useState([]);
    const [busca, setBusca] = useState("");

    const [nome, setNome] = useState("");
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
        setEditando(null);
        setErro("");
    }

    function editar(cargo) {
        setNome(cargo.nome);
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
                    nome
                });
            } else {
                await api.post("/cargos", { nome });
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
                        {c.nome}

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