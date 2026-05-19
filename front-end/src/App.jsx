import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Menu from "./Components/Menu/Menu";
import Eventos from "./Pages/Eventos";
import Agendar from "./Pages/AgendarEventos/agendarEventos"
import Cargos from "./Pages/Cargos";

import Auxilios from "./pages/Auxilios/Auxilios";
import Agendamentos from "./pages/Agendamentos/Agendamentos";
import Atividades from "./pages/Atividades/Atividades";
import Funcionario from "./pages/Funcionarios/Funcionario";
import Cargo from "./pages/Cargo/Cargo";
import PlanejarEtapa from "./pages/PlanejarEtapa/PlanejarEtapa";
import CategoriaAuxilio from "./pages/CategoriaAuxilio/CategoriaAuxilio";
import CategoriaAtividade from "./pages/CategoriaAtividade/CategoriaAtividade";
import Vincular from "./pages/VincularBeneficiario/VincularBeneficiario";
import Beneficiario from "./pages/Beneficiarios/Beneficiarios";
import Ocorrencias from "./pages/Ocorrencias/Ocorrencias";
import RelatorioOcorrencias from "./pages/RelatorioOcorrencias/RelatorioOcorrencias";



import { Toaster } from "react-hot-toast";
export default function App() {
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/menu" />} />
        <Route path="/menu" element={<Menu />} />
        <Route path="/eventos" element={<Eventos />} />
        <Route path="/agendarEventos" element={<Agendar />} />
        <Route path="/funcionarios" element={<Funcionarios />} />
        <Route path="/cargos" element={<Cargos />} />

        <Route path="/atividades" element={<Atividades />} />
        <Route path="/auxilios" element={<Auxilios />} />
        <Route path="/agendamentos" element={<Agendamentos />} />
        <Route path="/funcionario" element={<Funcionario />} />
        <Route path="/cargos" element={<Cargo />} />
        <Route path="/planejar-etapa/:id" element={<PlanejarEtapa />} />
        <Route path="/categoriaAuxilio" element={<CategoriaAuxilio />} />
        <Route path="/categoriaAtividade" element={<CategoriaAtividade />} />
        <Route path="/vincular" element={<Vincular />} />
        <Route path="/beneficiarios" element={<Beneficiario />} />
        <Route path="/ocorrencias" element={<Ocorrencias />} />
        <Route path="/relatorioOcorrencias" element={<RelatorioOcorrencias />} />

      </Routes>
    </BrowserRouter>
  );
}
export default App;
