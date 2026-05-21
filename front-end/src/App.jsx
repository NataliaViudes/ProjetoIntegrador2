import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Menu from "./Components/Menu/Menu";
import Eventos from "./Pages/Eventos";
import Agendar from "./Pages/AgendarEventos/agendarEventos";
import Cargos from "./Pages/Cargos";

import Auxilios from "./Pages/Auxilios/Auxilios";
import Agendamentos from "./Pages/Agendamentos/Agendamentos";
import Atividades from "./Pages/Atividades/Atividades";
import Cargo from "./Pages/Cargo/Cargo";
import PlanejarEtapa from "./Pages/PlanejarEtapa/PlanejarEtapa";
import CategoriaAuxilio from "./Pages/CategoriaAuxilio/CategoriaAuxilio";
import CategoriaAtividade from "./Pages/CategoriaAtividade/CategoriaAtividade";
import Vincular from "./Pages/VincularBeneficiario/VincularBeneficiario";
import Beneficiario from "./Pages/Beneficiarios/Beneficiarios";
import Ocorrencias from "./Pages/Ocorrencias/Ocorrencias";
import RelatorioOcorrencias from "./Pages/RelatorioOcorrencias/RelatorioOcorrencias";
import Funcionario from "./Pages/Funcionarios/Funcionario";
import Familiares from "./Pages/Familiares/Familiares";


import { Toaster } from "react-hot-toast";

function App() {
  return (
    <BrowserRouter>
      <Toaster />

      <Routes>
        <Route path="/" element={<Navigate to="/menu" />} />

        <Route path="/menu" element={<Menu />} />

        <Route path="/eventos" element={<Eventos />} />
        <Route path="/agendarEventos" element={<Agendar />} />

        <Route path="/funcionarios" element={<Funcionario />} />
        <Route path="/cargos" element={<Cargos />} />

        <Route path="/atividades" element={<Atividades />} />
        <Route path="/auxilios" element={<Auxilios />} />
        <Route path="/agendamentos" element={<Agendamentos />} />
        <Route path="/funcionarios" element={<Funcionario />} />
        <Route path="/cargo" element={<Cargo />} />

        <Route path="/planejar-etapa/:id" element={<PlanejarEtapa />} />

        <Route path="/categoriaAuxilio" element={<CategoriaAuxilio />} />
        <Route path="/categoriaAtividade" element={<CategoriaAtividade />} />

        <Route path="/vincular" element={<Vincular />} />

        <Route path="/beneficiarios" element={<Beneficiario />} />
        <Route path="/familiares/:idBeneficiario" element={<Familiares />} />

        <Route path="/ocorrencias" element={<Ocorrencias />} />

        <Route
          path="/relatorioOcorrencias"
          element={<RelatorioOcorrencias />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;