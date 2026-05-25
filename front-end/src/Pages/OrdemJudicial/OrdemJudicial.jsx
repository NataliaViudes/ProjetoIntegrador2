import { useEffect, useState } from "react";
import api from "../../services/api";
import Menu from "../../components/Menu/Menu";
import "./OrdemJudicial.css";

function OrdemJudicial() {
    const [lista, setLista] = useState([]);
    const [busca, setBusca] = useState("");
    const [loadingId, setLoadingId] = useState(null);

    useEffect(() => {
        carregar();
    }, []);

    async function carregar() {
        const [benef, ordens] = await Promise.all([
            api.get("/beneficiarios"),
            api.get("/ordem-judicial")
        ]);

        // 🔥 CORRIGIDO: agora usa beneficiarioId
        const idsComOrdem = new Set(
            ordens.data.map(o => o.beneficiarioId)
        );

        const merged = benef.data.map(b => ({
            ...b,
            ordemJudicial: idsComOrdem.has(b.id)
        }));

        setLista(merged);
    }

    async function toggleOrdem(b) {
        const temOrdem = !!b.ordemJudicial;

        try {
            setLoadingId(b.id);

            if (temOrdem) {
                // REMOVE ordem judicial
                await api.delete(`/ordem-judicial/${b.id}`);
            } else {
                // CRIA ordem judicial
                await api.post("/ordem-judicial", {
                    beneficiarioId: b.id,
                    possuiOrdem: true,
                    descricao: null
                });
            }

            // atualiza UI local
            setLista(prev =>
                prev.map(item =>
                    item.id === b.id
                        ? { ...item, ordemJudicial: !temOrdem }
                        : item
                )
            );

        } catch (err) {
            console.error(err);
            alert("Erro ao atualizar ordem judicial");
        } finally {
            setLoadingId(null);
        }
    }

    const filtrados = lista.filter(b =>
        (b.nome || "").toLowerCase().includes(busca.toLowerCase())
    );

    return (
        <div className="pagina-ordem-judicial">
            <Menu />

            <div className="topo">
                <h2>Ordem Judicial</h2>

                <input
                    placeholder="Buscar beneficiário..."
                    value={busca}
                    onChange={(e) => setBusca(e.target.value)}
                />
            </div>

            <div className="lista">
                {filtrados.map(b => (
                    <div key={b.id} className="card">
                        <div className="info">
                            <strong>{b.nome}</strong>
                            <span>CPF: {b.cpf}</span>
                        </div>

                        <div className="toggle-area">
                            <span>Ordem Judicial</span>

                            <label className="switch">
                                <input
                                    type="checkbox"
                                    checked={!!b.ordemJudicial}
                                    disabled={loadingId === b.id}
                                    onChange={() => toggleOrdem(b)}
                                />
                                <span className="slider" />
                            </label>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default OrdemJudicial;