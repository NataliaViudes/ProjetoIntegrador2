import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Menu from "./Components/Menu";
import Eventos from "./Pages/Eventos";
import { Toaster } from "react-hot-toast";
export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/menu" />} />
        <Route path="/menu" element={<Menu />} />
        <Route path="/eventos" element={<Eventos />} />
      </Routes>

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
