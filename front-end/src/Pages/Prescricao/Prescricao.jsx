import { useEffect, useMemo, useState } from "react";
import { Calendar, momentLocalizer } from "react-big-calendar";
import moment from "moment";
import "moment/locale/pt-br";
import "react-big-calendar/lib/css/react-big-calendar.css";

import api from "../../services/api";
import Menu from "../../components/Menu/Menu.jsx";

import "./Prescricao.css";

moment.locale("pt-br");

const localizer = momentLocalizer(moment);

function Prescricao() {

  const [prescricoes, setPrescricoes] = useState([]);
  const [beneficiarios, setBeneficiarios] = useState([]);
  const [remedios, setRemedios] = useState([]);

  const [buscaBeneficiario, setBuscaBeneficiario] = useState("");

  const [beneficiarioId, setBeneficiarioId] = useState("");
  const [remedioId, setRemedioId] = useState("");

  const [dosagem, setDosagem] = useState("");
  const [quantidade, setQuantidade] = useState("");

  const [dataHora, setDataHora] = useState("");

  const [intervalo, setIntervalo] = useState("");

  const [prescricaoEditando, setPrescricaoEditando] = useState(null);

  const [dataAtual, setDataAtual] = useState(new Date());
  const [viewAtual, setViewAtual] = useState("week");

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {

    try {

      const [
        respPrescricoes,
        respBeneficiarios,
        respRemedios
      ] = await Promise.all([
        api.get("/prescricoes"),
        api.get("/beneficiarios"),
        api.get("/remedios")
      ]);

      setPrescricoes(
        Array.isArray(respPrescricoes.data)
          ? respPrescricoes.data
          : []
      );

      setBeneficiarios(
        Array.isArray(respBeneficiarios.data)
          ? respBeneficiarios.data
          : []
      );

      setRemedios(
        Array.isArray(respRemedios.data)
          ? respRemedios.data
          : []
      );

    } catch (e) {

      console.error("Erro ao carregar dados:", e);
    }
  }

  function formatarDatetimeLocal(valor) {

    const d = new Date(valor);

    const ano = d.getFullYear();
    const mes = String(d.getMonth() + 1).padStart(2, "0");
    const dia = String(d.getDate()).padStart(2, "0");

    const hora = String(d.getHours()).padStart(2, "0");
    const minuto = String(d.getMinutes()).padStart(2, "0");

    return `${ano}-${mes}-${dia}T${hora}:${minuto}`;
  }

  

  function limparFormulario() {

    setPrescricaoEditando(null);

    setBeneficiarioId("");
    setRemedioId("");

    setDosagem("");
    setQuantidade("");

    setDataHora("");

    setIntervalo("");

    setBuscaBeneficiario("");
  }

  function gerarProximasDatas(dataInicial, tipoIntervalo) {

    const eventos = [];

    let horas = 0;

    if (tipoIntervalo === "12h") horas = 12;
    if (tipoIntervalo === "8h") horas = 8;
    if (tipoIntervalo === "6h") horas = 6;

    const dataBase = new Date(dataInicial);

    if (tipoIntervalo === "1dia") {

      for (let i = 0; i < 30; i++) {

        const nova = new Date(dataBase);

        nova.setDate(nova.getDate() + i);

        eventos.push(nova);
      }
    }

    else if (tipoIntervalo === "1semana") {

      for (let i = 0; i < 12; i++) {

        const nova = new Date(dataBase);

        nova.setDate(nova.getDate() + (i * 7));

        eventos.push(nova);
      }
    }

    else {

      for (let i = 0; i < 30; i++) {

        const nova = new Date(dataBase);

        nova.setHours(nova.getHours() + (horas * i));

        eventos.push(nova);
      }
    }

    return eventos;
  }

  async function salvar() {

    if (
      !beneficiarioId ||
      !remedioId ||
      !dosagem ||
      !quantidade ||
      !dataHora ||
      !intervalo
    ) {

      alert("Preencha todos os campos.");
      return;
    }

    const datas = gerarProximasDatas(dataHora, intervalo);

    try {

      for (const data of datas) {

        const payload = {

          dosagem: dosagem,

          quantidade: Number(quantidade),

          horario: formatarDatetimeLocal(data) + ":00",

          beneficiario: {
            id: Number(beneficiarioId)
          },

          remedio: {
            id: Number(remedioId)
          },

          intervalo:
            intervalo === "12h" ? 12 :
            intervalo === "8h" ? 8 :
            intervalo === "6h" ? 6 :
            intervalo === "1dia" ? 24 :
            168
        };

        await api.post("/prescricoes", payload);
      }

      alert("Prescrição cadastrada com sucesso!");

      limparFormulario();

      carregarTudo();

    } catch (e) {

      console.error("Erro ao salvar prescrição:", e);

      alert("Erro ao salvar prescrição.");
    }
  }

  async function excluir(id) {

    const confirmou = window.confirm(
      "Deseja excluir esta prescrição?"
    );

    if (!confirmou) return;

    try {

      await api.delete(`/prescricoes/${id}`);

      carregarTudo();

    } catch (e) {

      console.error("Erro ao excluir:", e);

      alert("Erro ao excluir prescrição.");
    }
  }

  const eventos = useMemo(() => {

    return prescricoes
      .filter((p) =>
        p &&
        p.horario
      )
      .map((p) => {

        const dataInicio = new Date(p.horario);

        return {

          id: p.id,

          title:
            `${p.remedio?.nome || "Remédio"} - ${p.beneficiario?.nome || ""}`,

          start: dataInicio,

          end: new Date(
            dataInicio.getTime() + (30 * 60000)
          ),

          resource: p
        };
      });

  }, [prescricoes]);

  const beneficiariosFiltrados = beneficiarios.filter((b) =>
    b.nome?.toLowerCase().includes(
      buscaBeneficiario.toLowerCase()
    )
  );

  return (
    <div className="pagina-prescricao">

      <Menu />

      <div className="conteudo-prescricao">
        <section className="painel-calendario">

          <Calendar
            localizer={localizer}
            events={eventos}
            startAccessor="start"
            endAccessor="end"
            popup
            selectable

            eventPropGetter={(event) => ({

              style: {

                backgroundColor: "#c1121f",

                border: "none",

                color: "white",

                borderRadius: "10px",

                fontWeight: "600",

                padding: "3px",

                fontSize: "13px"
              }
            })}

            onSelectSlot={(slotInfo) => {

              setDataHora(
                formatarDatetimeLocal(slotInfo.start)
              );
            }}

            date={dataAtual}

            view={viewAtual}

            onNavigate={(novaData) =>
              setDataAtual(novaData)
            }

            onView={(novaView) =>
              setViewAtual(novaView)
            }

            views={[
              "month",
              "week",
              "day",
              "agenda"
            ]}

            defaultView="week"

            messages={{
              next: "Próximo",
              previous: "Anterior",
              today: "Hoje",
              month: "Mês",
              week: "Semana",
              day: "Dia",
              agenda: "Agenda",
              date: "Data",
              time: "Hora",
              event: "Evento",
              noEventsInRange:
                "Nenhuma prescrição neste período",
              allDay: "Dia inteiro"
            }}

            onSelectEvent={(evento) => {

              const p = evento.resource;

              const confirmou = window.confirm(
                `Excluir prescrição de ${p.remedio?.nome}?`
              );

              if (confirmou) {

                excluir(p.id);
              }
            }}

            style={{ height: "80vh" }}
          />

        </section>

        <section className="painel-formulario glass">

          <h2>Prescrição</h2>

          <label>Buscar beneficiário</label>

          <input
            type="text"
            value={buscaBeneficiario}
            onChange={(e) =>
              setBuscaBeneficiario(e.target.value)
            }
            placeholder="Digite o nome"
          />

          <label>Beneficiário</label>

          <select
            value={beneficiarioId}
            onChange={(e) =>
              setBeneficiarioId(e.target.value)
            }
          >

            <option value="">
              Selecione
            </option>

            {beneficiariosFiltrados.map((b) => (

              <option
                key={b.id}
                value={b.id}
              >
                {b.nome}
              </option>

            ))}

          </select>

          <label>Remédio</label>

          <select
            value={remedioId}
            onChange={(e) =>
              setRemedioId(e.target.value)
            }
          >

            <option value="">
              Selecione
            </option>

            {remedios.map((r) => (

              <option
                key={r.id}
                value={r.id}
              >
                {r.nome}
              </option>

            ))}

          </select>

          <label>Dosagem</label>

          <input
            type="text"
            value={dosagem}
            onChange={(e) =>
              setDosagem(e.target.value)
            }
            placeholder="Ex: 1 comprimido"
          />

          <div className="linha-dupla">

          <div>
            <label>Quantidade</label>

            <input
              type="number"
              value={quantidade}
              onChange={(e) =>
                setQuantidade(e.target.value)
              }
            />
          </div>

          <div>
            <label>Data e Hora</label>

            <input
              type="datetime-local"
              value={dataHora}
              onChange={(e) =>
                setDataHora(e.target.value)
              }
            />
          </div>

        </div>

          <label>Intervalo</label>

          <select
            value={intervalo}
            onChange={(e) =>
              setIntervalo(e.target.value)
            }
          >

            <option value="">
              Selecione
            </option>

            <option value="6h">
              De 6 em 6h
            </option>

            <option value="8h">
              De 8 em 8h
            </option>

            <option value="12h">
              De 12 em 12h
            </option>

            <option value="1dia">
              1x ao dia
            </option>

            <option value="1semana">
              1x na semana
            </option>

          </select>

          <div className="acoes-formulario">

            <button onClick={salvar}>
              Salvar
            </button>

            <button onClick={limparFormulario}>
              Limpar
            </button>

          </div>

        </section>
        <section>
        <div className="lista-scroll">

          {prescricoes
            .filter((p) =>
              p &&
              p.remedio &&
              p.beneficiario
            )
            .map((p) => (

              <div
                key={p.id}
                className="card-prescricao"
              >

                <div>

                  <strong>
                    {p.remedio?.nome}
                  </strong>

                  <p>
                    Beneficiário:
                    {" "}
                    {p.beneficiario?.nome}
                  </p>

                  <p>
                    Dosagem:
                    {" "}
                    {p.dosagem}
                  </p>

                  <p>
                    Quantidade:
                    {" "}
                    {p.quantidade}
                  </p>

                  <p>
                    Horário:
                    {" "}
                    {moment(p.horario).format(
                      "DD/MM/YYYY HH:mm"
                    )}
                  </p>

                </div>

                <button
                  className="btn-excluir"
                  onClick={() => excluir(p.id)}
                >
                  Excluir
                </button>

              </div>

          ))}

        </div>
        </section>
        

      </div>

    </div>
  );
}

export default Prescricao;