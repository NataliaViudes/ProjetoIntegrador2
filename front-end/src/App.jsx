import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Menu from "./components/Menu";
import Atividades from "./pages/Alimentos/Alimentos";
import Auxilios from "./pages/Auxilios/Auxilios";
import Alimentos from "./pages/Alimentos/Alimentos";
import Beneficiarios from "./pages/Beneficiarios/Beneficiarios";
import Agendamentos from "./pages/Agendamentos/Agendamentos";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/menu" />} />
        <Route path="/menu" element={<Menu />} />
        <Route path="/atividades" element={<Atividades />} />
        <Route path="/auxilios" element={<Auxilios />} />
        <Route path="/alimentos" element={<Alimentos />} />
        <Route path="/beneficiarios" element={<Beneficiarios />} />
        <Route path="/agendamentos" element={<Agendamentos />} />

      </Routes>
    </BrowserRouter>
  );
}
export default App;