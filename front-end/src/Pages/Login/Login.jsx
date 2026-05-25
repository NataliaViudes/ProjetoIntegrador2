import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../services/api";
import "./Login.css";

function Login() {
    const navigate = useNavigate();

    const [form, setForm] = useState({
        login: "",
        senha: ""
    });

    async function entrar(e) {
        e.preventDefault();
        try {
            const response = await api.post("/auth/login", form);
            localStorage.setItem("token", response.data.token);
            localStorage.setItem("usuario", JSON.stringify(response.data.usuario));
            navigate("/pagina-inicial");
        } catch (e) {
            console.error(e);
            alert("Login inválido");
        }
    }

    function handleChange(e) {
        setForm({
            ...form,
            [e.target.name]: e.target.value
        });
    }

    return (
        <div className="pagina-login">
            <form className="card-login" onSubmit={entrar}>
                <h1>Login</h1>
                <input
                    type="text"
                    name="login"
                    placeholder="Login"
                    value={form.login}
                    onChange={handleChange}
                />

                <input
                    type="password"
                    name="senha"
                    placeholder="Senha"
                    value={form.senha}
                    onChange={handleChange}
                />

                <button type="submit">
                    Entrar
                </button>
            </form>
        </div>
    );
}

export default Login;