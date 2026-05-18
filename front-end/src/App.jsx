import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Menu from "./components/Menu";
import Atividades from "./pages/Alimentos/Alimentos";
import Auxilios from "./pages/Auxilios/Auxilios";
import Alimentos from "./pages/Alimentos/Alimentos";
import Beneficiarios from "./pages/Beneficiarios/Beneficiarios";
import PageAtiv from "./pages/PaginaAtividade/PageAtiv";

function App() {
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Menu from "./Components/Menu/Menu";
import Eventos from "./Pages/Eventos";
import Agendar from "./Pages/AgendarEventos/agendarEventos"
import Cargos from "./Pages/Cargos";
import Funcionarios from "./Pages/Funcionarios";
import Familiares from "./Pages/Familiares";
import { Toaster } from "react-hot-toast";
export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/menu" />} />
        <Route path="/menu" element={<Menu />} />
        <Route path="/atividades" element={<Atividades />} />
        <Route path="/auxilios" element={<Auxilios />} />
        <Route path="/alimentos" element={<Alimentos />} />
        <Route path="/beneficiarios" element={<Beneficiarios />} />
        <Route path="/AgendarAtividade" element={<PageAtiv />} />
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/menu" />} />
        <Route path="/menu" element={<Menu />} />
        <Route path="/eventos" element={<Eventos />} />
        <Route path="/agendarEventos" element={<Agendar />} />
        <Route path="/funcionarios" element={<Funcionarios />} />
        <Route path="/cargos" element={<Cargos />} />
        <Route path="/familiares" element={<Familiares />} />
      </Routes>

      </Routes>
    </BrowserRouter>
  );
}
export default App;
      <Toaster
        position="top-center"
        toastOptions={{
          duration: 1500,
          style: {
            background: "#2f2f2f",
            color: "#fff",
            borderRadius: "10px",
            padding: "14px 18px",
            fontSize: "14px",
          },
        }}
      />
    </BrowserRouter>
  );
}
