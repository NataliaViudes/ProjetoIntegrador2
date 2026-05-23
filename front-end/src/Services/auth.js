export function getUsuario() {
  return JSON.parse(localStorage.getItem("usuario"));
}

export function getToken() {
  return localStorage.getItem("token");
}

export function logout() {
  localStorage.removeItem("token");
  localStorage.removeItem("usuario");
}

export function isAuthenticated() {
  return !!getToken();
}

export function getNivel() {
  const usuario = getUsuario();

  return usuario?.funcionario?.cargo?.nivelAcesso || 0;
}