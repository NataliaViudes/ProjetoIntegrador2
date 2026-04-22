import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Atividades from "./pages/Atividades";
import Auxilios from "./pages/Auxilios";
import Funcionario from "./pages/Funcionario";
import Agendamentos from "./pages/Agendamentos";
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/atividades" />} />
        <Route path="/atividades" element={<Atividades />} />
        <Route path="/auxilios" element={<Auxilios />} />
        <Route path="/funcionario" element={<Funcionario />} />
        <Route path="/agendamentos" element={<Agendamentos />} />

      </Routes>
    </BrowserRouter>
  );
}

export default App;