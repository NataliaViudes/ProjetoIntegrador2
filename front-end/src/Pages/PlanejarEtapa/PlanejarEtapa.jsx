import { useEffect, useState, useCallback } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../../Services/api";
import "./PlanejarEtapa.css";
import Menu from "../../Components/Menu/Menu.jsx";
function PlanejarEtapa() {
    const { id } = useParams();

    const [agendamento, setAgendamento] = useState(null);
    const [etapas, setEtapas] = useState([]);
    const [materiais, setMateriais] = useState([]);
    const [estoque, setEstoque] = useState([]);

    const navigate = useNavigate();

    const carregarDados = useCallback(async () => {
        try {
            const [respAg, respEtapas, respEstoque, respMateriais] = await Promise.all([
                api.get(`/agendamentos/${id}`),
                api.get(`/etapas/${id}`),
                api.get("/estoque"),
                api.get(`/estoque/agendamento-material/${id}`)
            ]);

            setAgendamento(respAg.data);

            setEtapas(
                (respEtapas.data || []).map(e => {
                    let data = "";
                    let hora = "";

                    if (e.dataHoraInicio) {
                        const [d, t] = e.dataHoraInicio.split("T");
                        data = d;
                        hora = t?.substring(0, 5);
                    }

                    return { ...e, data, hora };
                })
            );

            setEstoque(respEstoque.data || []);

            setMateriais(
                (respMateriais.data || []).map(m => ({
                    id_item: m.idItem,
                    quantidade: m.quantidade,
                    salvo: true
                }))
            );

        } catch (e) {
            console.error("Erro ao carregar dados:", e);
        }
    }, [id]);

    useEffect(() => {
        carregarDados();
    }, [carregarDados]);

    if (!agendamento) return <div>Carregando...</div>;

    const agora = new Date();
    const agendamentoEncerrado = new Date(agendamento.dataFim) < agora;

    // ================= ETAPAS =================

    function adicionarEtapa() {
        setEtapas([...etapas, { data: "", hora: "", descricao: "" }]);
    }

    function atualizarEtapa(index, campo, valor) {
        const novas = [...etapas];
        novas[index][campo] = valor;
        setEtapas(novas);
    }

    async function removerEtapa(index) {
        const etapa = etapas[index];

        if (!window.confirm("Tem certeza que deseja excluir esta etapa?")) return;

        try {
            if (etapa.id) {
                await api.delete(`/etapas/${etapa.id}`);
            }

            setEtapas(etapas.filter((_, i) => i !== index));

        } catch (e) {
            console.error(e);
            alert("Erro ao excluir etapa");
        }
    }

    // ================= MATERIAIS =================

    function adicionarMaterial() {
        if (agendamentoEncerrado) {
            alert("Não é possível alterar materiais após o fim da atividade!");
            return;
        }

        setMateriais([...materiais, { id_item: "", quantidade: 1, salvo: false }]);
    }

    function atualizarMaterial(index, campo, valor) {
        if (agendamentoEncerrado) return;

        const novos = [...materiais];
        novos[index][campo] = valor;
        setMateriais(novos);
    }

    async function removerMaterial(index) {
        if (agendamentoEncerrado) {
            alert("Não é possível remover materiais após o fim da atividade!");
            return;
        }

        const mat = materiais[index];

        if (!window.confirm("Tem certeza que deseja remover este material?")) return;

        try {
            if (mat.salvo) {
                await api.delete(`/estoque/agendamento-material`, {
                    data: {
                        idAgendamento: Number(id),
                        idItem: Number(mat.id_item)
                    }
                });
            }

            setMateriais(materiais.filter((_, i) => i !== index));

        } catch (e) {
            console.error(e);
            alert("Erro ao remover material");
        }
    }

    // ================= SALVAR =================

    async function salvarPlanejamento() {
        try {

            if (agendamentoEncerrado) {
                alert("Não é possível alterar planejamento após o fim da atividade!");
                return;
            }

            const inicioAg = new Date(agendamento.dataInicio);
            const fimAg = new Date(agendamento.dataFim);

            for (let etapa of etapas) {

                if (!etapa.descricao || !etapa.data || !etapa.hora) {
                    alert("Preencha todas as etapas!");
                    return;
                }

                const dataEtapa = new Date(`${etapa.data}T${etapa.hora}`);

                if (dataEtapa < agora) {
                    alert("Não é permitido salvar etapas no passado!");
                    return;
                }

                if (dataEtapa < inicioAg || dataEtapa > fimAg) {
                    alert("A etapa está fora do período do agendamento!");
                    return;
                }

                const payload = {
                    idAgendamento: Number(id),
                    descricao: etapa.descricao,
                    dataHoraInicio: `${etapa.data}T${etapa.hora}`
                };

                if (etapa.id) {
                    await api.put(`/etapas/${etapa.id}`, payload);
                } else {
                    const resp = await api.post("/etapas", payload);
                    etapa.id = resp.data.id;
                }
            }

            if (materiais.length > 0) {
                await api.post("/estoque/agendamento-material",
                    materiais.map(m => ({
                        idAgendamento: Number(id),
                        idItem: Number(m.id_item),
                        quantidade: Number(m.quantidade)
                    }))
                );
            }

            alert("Planejamento salvo com sucesso!");
            navigate("/agendamentos");

        } catch (e) {
            console.error(e.response?.data || e);
            alert(e.response?.data?.mensagem || "Erro ao salvar planejamento");
        }
    }

    // ================= UI =================

    return (
        <div className="pagina-planejamento">
            <div className="container-planejamento">

                {/* INFO */}
                <div className="painel-info">
                    <div className="box">
                        {(() => {
                            const inicio = new Date(agendamento.dataInicio).toLocaleDateString();
                            const fim = new Date(agendamento.dataFim).toLocaleDateString();
                            return inicio === fim ? inicio : `${inicio} até ${fim}`;
                        })()}
                    </div>
                    <div className="box">{agendamento.atividade?.funcionario?.nome}</div>
                    <div className="box">{agendamento.atividade?.descricao}</div>
                    <div className="box">{agendamento.atividade?.categoria?.nome}</div>
                </div>

                <div className="painel-direito">

                    {/* MATERIAIS */}
                    <div className="painel-material">
                        <div className="titulo">
                            <span>Requisitar Material</span>
                            <button onClick={adicionarMaterial} disabled={agendamentoEncerrado}>+</button>
                        </div>

                        <div className="lista-material">
                            {materiais.map((mat, index) => (
                                <div key={index} className="linha-material">

                                    <select
                                        value={mat.id_item}
                                        disabled={agendamentoEncerrado}
                                        onChange={(e) =>
                                            atualizarMaterial(index, "id_item", e.target.value)
                                        }
                                    >
                                        <option value="">Selecione</option>
                                        {estoque
                                            .filter(item =>
                                                !materiais.some((m, i) =>
                                                    Number(m.id_item) === item.id && i !== index
                                                )
                                            )
                                            .map(item => (
                                                <option key={item.id} value={item.id}>
                                                    {item.descricao} (Qtd: {item.qtd})
                                                </option>
                                            ))}
                                    </select>

                                    <input
                                        type="number"
                                        value={mat.quantidade}
                                        disabled={agendamentoEncerrado}
                                        onChange={(e) =>
                                            atualizarMaterial(index, "quantidade", e.target.value)
                                        }
                                    />

                                    <button
                                        onClick={() => removerMaterial(index)}
                                        disabled={agendamentoEncerrado}
                                    >
                                        ❌
                                    </button>
                                </div>
                            ))}
                        </div>
                    </div>

                    {/* CRONOGRAMA */}
                    <div className="painel-cronograma">
                        <div className="titulo">
                            Cronograma
                            <button onClick={adicionarEtapa} disabled={agendamentoEncerrado}>+</button>
                        </div>

                        <div className="lista-cronograma">
                            {etapas.map((etapa, index) => (
                                <div key={index} className="linha-cronograma">

                                    <input
                                        type="date"
                                        value={etapa.data}
                                        disabled={agendamentoEncerrado}
                                        onChange={(e) =>
                                            atualizarEtapa(index, "data", e.target.value)
                                        }
                                    />

                                    <input
                                        type="time"
                                        value={etapa.hora}
                                        disabled={agendamentoEncerrado}
                                        onChange={(e) =>
                                            atualizarEtapa(index, "hora", e.target.value)
                                        }
                                    />

                                    <input
                                        type="text"
                                        value={etapa.descricao}
                                        disabled={agendamentoEncerrado}
                                        onChange={(e) =>
                                            atualizarEtapa(index, "descricao", e.target.value)
                                        }
                                    />

                                    <button
                                        onClick={() => removerEtapa(index)}
                                        disabled={agendamentoEncerrado}
                                    >
                                        ❌
                                    </button>

                                </div>
                            ))}
                        </div>
                    </div>

                    {/* AÇÕES */}
                    <div className="acoes">
                        <button className="btn-confirmar" onClick={salvarPlanejamento}>
                            Confirmar
                        </button>

                        <button
                            className="btn-confirmar"
                            onClick={() => navigate("/agendamentos")}
                        >
                            Voltar
                        </button>
                    </div>

                </div>
            </div>
        </div>
    );
}

export default PlanejarEtapa;
