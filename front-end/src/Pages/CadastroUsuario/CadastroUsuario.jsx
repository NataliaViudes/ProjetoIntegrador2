import { useEffect, useState } from "react";
import api from "../../Services/api";
import "./CadastroUsuario.css";

function CadastroUsuario() {
    const [funcionarios, setFuncionarios] = useState([]);
    const [form, setForm] = useState({
        login: "",
        senha: "",
        idFuncionario: ""
    });

    useEffect(() => {
        carregarFuncionarios();
    }, []);

    async function carregarFuncionarios() {
        try {
            const response = await api.get("/funcionarios");
            setFuncionarios(response.data);
        } catch (e) {
            console.error(e);
        }
    }

    function handleChange(e) {
        setForm({
            ...form,
            [e.target.name]: e.target.value
        });
    }

    async function salvar(e) {
        e.preventDefault();
        try {
            await api.post(
                "/usuarios",
                form
            );
            alert("Usuário cadastrado");
        } catch (e) {
            console.error(e);
            alert("Erro");
        }
    }

    return (
        <div className="pagina-login">

            <form
                className="card-login"
                onSubmit={salvar}
            >

                <h1>Cadastrar Usuário</h1>

                <select
                    name="idFuncionario"
                    value={form.idFuncionario}
                    onChange={handleChange}
                >

                    <option value="">
                        Selecione
                    </option>

                    {funcionarios.map(f => (
                        <option
                            key={f.id}
                            value={f.id}
                        >
                            {f.nome}
                        </option>
                    ))}

                </select>

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
                    Cadastrar
                </button>

            </form>

        </div>
    );
}

export default CadastroUsuario;