import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Atividades from "./pages/Atividades";
import Auxilios from "./pages/Auxilios";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/atividades" />} />
        <Route path="/atividades" element={<Atividades />} />
        <Route path="/auxilios" element={<Auxilios />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;