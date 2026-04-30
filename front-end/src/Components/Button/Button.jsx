import { useState } from "react";
import "./Button.css";

const Button = () => {
  const [open, setOpen] = useState(false);

  return (
    <div className="container">
      <div className={`menu ${open ? "open" : ""}`}>
        
        <div className="items">
          <div className="item">
            <div className="circle">🍔</div>
            <span>Planejar Cardapio</span>
          </div>

          <div className="item">
            <div className="circle">🔍</div>
            <span>Agendar atividade</span>
          </div>
        </div>

        <button className="main-btn" onClick={() => setOpen(!open)}>
          {open ? "✕" : "+"}
        </button>

      </div>
    </div>
  );
};

export default Button;