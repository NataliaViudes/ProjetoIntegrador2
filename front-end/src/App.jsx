import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import Menu from "./components/Menu/Menu";

import Eventos from "./Pages/Eventos";
import Agendar from "./Pages/AgendarEventos/agendarEventos";

import Auxilios from "./Pages/Auxilios/Auxilios";
import Agendamentos from "./Pages/Agendamentos/Agendamentos";
import Atividades from "./Pages/Atividades/Atividades";

import Cargos from "./Pages/Cargo/Cargo";

import PlanejarEtapa from "./Pages/PlanejarEtapa/PlanejarEtapa";

import CategoriaAuxilio from "./Pages/CategoriaAuxilio/CategoriaAuxilio";
import CategoriaAtividade from "./Pages/CategoriaAtividade/CategoriaAtividade";

import Vincular from "./Pages/VincularBeneficiario/VincularBeneficiario";
import PresencaBeneficiario from "./Pages/PresencaBeneficiario/PresencaBeneficiario";

import Beneficiario from "./Pages/Beneficiarios/Beneficiarios";
import Familiares from "./Pages/Familiares/Familiares";

import Ocorrencias from "./Pages/Ocorrencias/Ocorrencias";
import RelatorioOcorrencias from "./Pages/RelatorioOcorrencias/RelatorioOcorrencias";
import RelatorioFaltas from "./Pages/RelatorioFaltas/RelatorioFaltas";
import RelatorioAtividades from "./Pages/RelatorioAtividades/RelatorioAtividades";
import RelatorioEstoque from "./Pages/RelatorioEstoque/RelatorioEstoque";

import Funcionario from "./Pages/Funcionarios/Funcionario";

import Alimentos from "./Pages/Alimentos/Alimentos";

import TipoEstoque from "./Pages/Estoque/TipoEstoque";
import Estoque from "./Pages/Estoque/Estoque";

import Cardapio from "./Pages/CardapioMensal/Cardapio";
import ItensCardapio from "./Pages/CardapioMensal/ItensCardapio";

import PaginaInicial from "./Pages/PaginaInicial/PaginaInicial";

import Login from "./Pages/Login/Login";
import CadastroUsuario from "./Pages/CadastroUsuario/CadastroUsuario";

import { Toaster } from "react-hot-toast";

function App() {
  return (
    <BrowserRouter>

      <Toaster />

      <Routes>

        {/* REDIRECIONAMENTO INICIAL */}
        <Route path="/" element={<Login />} />

        <Route path="/cadastro-usuario" element={<CadastroUsuario />} />

        {/* MENU */}
        <Route path="/menu" element={<Menu />} />

        {/* PÁGINA INICIAL */}
        <Route
          path="/pagina-inicial"
          element={<PaginaInicial />}
        />

        {/* EVENTOS */}
        <Route path="/eventos" element={<Eventos />} />

        <Route
          path="/agendarEventos"
          element={<Agendar />}
        />

        {/* FUNCIONÁRIOS */}
        <Route
          path="/funcionarios"
          element={<Funcionario />}
        />

        {/* CARGOS */}
        <Route
          path="/cargos"
          element={<Cargos />}
        />

        {/* ATIVIDADES */}
        <Route
          path="/atividades"
          element={<Atividades />}
        />

        {/* AUXÍLIOS */}
        <Route
          path="/auxilios"
          element={<Auxilios />}
        />

        {/* AGENDAMENTOS */}
        <Route
          path="/agendamentos"
          element={<Agendamentos />}
        />

        {/* PLANEJAR ETAPA */}
        <Route
          path="/planejar-etapa/:id"
          element={<PlanejarEtapa />}
        />

        {/* CATEGORIAS */}
        <Route
          path="/categoriaAuxilio"
          element={<CategoriaAuxilio />}
        />

        <Route
          path="/categoriaAtividade"
          element={<CategoriaAtividade />}
        />

        {/* VINCULAR */}
        <Route
          path="/vincular"
          element={<Vincular />}
        />

        <Route
          path="/presenca"
          element={<PresencaBeneficiario />}
        />

        {/* BENEFICIÁRIOS */}
        <Route
          path="/beneficiarios"
          element={<Beneficiario />}
        />

        <Route
          path="/familiares/:idBeneficiario"
          element={<Familiares />}
        />

        {/* OCORRÊNCIAS */}
        <Route
          path="/ocorrencias"
          element={<Ocorrencias />}
        />

        <Route
          path="/relatorioOcorrencias"
          element={<RelatorioOcorrencias />}
        />

        <Route
          path="/relatorioFaltas"
          element={<RelatorioFaltas />}
        />
        
        <Route
          path="/relatorioAtividades"
          element={<RelatorioAtividades />}
        />

        <Route
          path="/relatorioEstoque"
          element={<RelatorioEstoque />}
        />

        {/* ALIMENTOS */}

        <Route
          path="/alimentos"
          element={<Alimentos />}
        />


        {/* ESTOQUE */}

        <Route
          path="/tipo-estoque"
          element={<TipoEstoque />}
        />

        <Route
          path="/estoque"
          element={<Estoque />}
        />

        {/* CARDÁPIO */}

        <Route
          path="/cardapio"
          element={<Cardapio />}
        />


      </Routes>
    </BrowserRouter>
  );
}

export default App;
