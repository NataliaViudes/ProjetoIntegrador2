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

        const idsComOrdem = new Set(
            ordens.data.map(o => o.beneficiarioId)
        );

        const hoje = new Date();
        const limite = new Date();
        limite.setDate(hoje.getDate() - 30);

        const merged = await Promise.all(
            benef.data.map(async (b) => {

                const registros = await carregarFaltas(b.id);

                const faltasFiltradas = registros
                    .filter(r => !r.presente && r.dataInicio)
                    .filter(r => {
                        const d = new Date(r.dataInicio);
                        return d >= limite && d <= hoje;
                    })
                    .sort((a, b) => new Date(a.dataInicio) - new Date(b.dataInicio));

                const faltasConsecutivas = faltasFiltradas.length;

                const ultimasFaltas = faltasFiltradas.map(f => ({
                    data: f.dataInicio
                }));

                return {
                    ...b,
                    ordemJudicial: idsComOrdem.has(b.id),
                    faltasConsecutivas,
                    ultimasFaltas
                };
            })
        );

        setLista(merged);
    }

    async function carregarFaltas(idBeneficiario) {
        try {
            const resp = await api.get(
                `/presencas/relatorio/beneficiario/${idBeneficiario}`
            );

            return Array.isArray(resp.data) ? resp.data : [];
        } catch (err) {
            return [];
        }
    }

    async function toggleOrdem(b) {
        const temOrdem = !!b.ordemJudicial;

        try {
            setLoadingId(b.id);

            if (temOrdem) {
                await api.delete(`/ordem-judicial/${b.id}`);
            } else {
                await api.post("/ordem-judicial", {
                    beneficiarioId: b.id,
                    possuiOrdem: true,
                    descricao: null
                });
            }

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
                {filtrados.map(b => {

                    const temAlerta =
                        b.ordemJudicial &&
                        b.faltasConsecutivas >= 2;

                    return (
                        <div
                            key={b.id}
                            className={`card ${temAlerta ? "card-alerta" : ""}`}
                        >
                            <div className="info">
                                <strong>{b.nome}</strong>
                                <span>CPF: {b.cpf}</span>

                                {b.ordemJudicial && (
                                    <>
                                        <span>
                                            Faltas nos últimos 30 dias: {b.faltasConsecutivas}
                                        </span>

                                        {b.ultimasFaltas?.length > 0 && (
                                            <div className="faltas-lista">
                                                {b.ultimasFaltas.map((f, i) => (
                                                    <span key={i}>
                                                        ❌ {new Date(f.data).toLocaleDateString()}
                                                    </span>
                                                ))}
                                            </div>
                                        )}
                                    </>
                                )}
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
                    );
                })}
            </div>
        </div>
    );
}

export default OrdemJudicial;