import { Navigate } from "react-router-dom";
import { isAuthenticated, getNivel } from "../services/auth";

function PrivateRoute({
  children,
  nivelMinimo = 1
}) {

  if (!isAuthenticated()) {
    return <Navigate to="/login" />;
  }

  if (getNivel() < nivelMinimo) {
    return <Navigate to="/pagina-inicial" />;
  }

  return children;
}

export default PrivateRoute;