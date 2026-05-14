import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Menu from "./components/Menu";
import Auxilios from "./pages/Auxilios/Auxilios";
import Agendamentos from "./pages/Agendamentos/Agendamentos";
import Atividades from "./pages/Atividades/Atividades";
import Funcionario from "./pages/Funcionarios/Funcionario";
import Cargo from "./pages/Cargo/Cargo";
import PlanejarEtapa from "./pages/PlanejarEtapa/PlanejarEtapa";
import CategoriaAuxilio from "./pages/CategoriaAuxilio/CategoriaAuxilio";
import CategoriaAtividade from "./pages/CategoriaAtividade/CategoriaAtividade";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/menu" />} />
        <Route path="/menu" element={<Menu />} />
        <Route path="/atividades" element={<Atividades />} />
        <Route path="/auxilios" element={<Auxilios />} />
        <Route path="/agendamentos" element={<Agendamentos />} />
        <Route path="/funcionario" element={<Funcionario />} />
        <Route path="/cargos" element={<Cargo />} />
        <Route path="/planejar-etapa/:id" element={<PlanejarEtapa />} />
        <Route path="/categoriaAuxilio" element={<CategoriaAuxilio />} />
        <Route path="/categoriaAtividade" element={<CategoriaAtividade />} />

      </Routes>
    </BrowserRouter>
  );
}
export default App;