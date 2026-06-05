package model.cliente;

public class Endereco {

    private String estado;
    private String cidade;
    private String rua;
    private int numero;
    private String complemento;
    private String apelido; // optional label, como "Casa", "Trabalho"

    public Endereco(String estado, String cidade, String rua, int numero, String complemento) {
        this.estado = estado;
        this.cidade = cidade;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
    }

    public String getEstado() { 
        return estado; 
    }

    public void setEstado(String estado) { 
        this.estado = estado; 
    }

    public String getCidade() { 
        return cidade; 
    }

    public void setCidade(String cidade) { 
        this.cidade = cidade; 
    }

    public String getRua() { 
        return rua; 
    }

    public void setRua(String rua) { 
        this.rua = rua; 
    }

    public int getNumero() { 
        return numero; 
    }

    public void setNumero(int numero) { 
        this.numero = numero; 
    }

    public String getComplemento() { 
        return complemento; 
    }
    public void setComplemento(String complemento) { 
        this.complemento = complemento; 
    }

    public String getApelido() { 
        return apelido; 
    }
    public void setApelido(String apelido) { 
        this.apelido = apelido; 
    }

    //Ex: "[Casa] Rua das Flores, 42 Apto 3 - São Paulo/SP"
    @Override
    public String toString() {
        String oApelido = (apelido != null && !apelido.isBlank()) ? "[" + apelido + "] " : "";
        return oApelido + rua + ", " + "N° " + numero
                + (complemento != null && !complemento.isBlank() ? " " + complemento : "")
                + " - " + cidade + "/" + estado;
    }
}
