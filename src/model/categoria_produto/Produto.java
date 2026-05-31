package model.categoria_produto;

import model.cliente.Cliente;

/**
 *
 * @author joshu
 */
public class Produto {
    private String nome;
    private String descricao;
    private double precoBase;
    private Cliente vendedor;
    private Categoria categoria;
    private double nota;

    public Produto(String nome, String descricao, double precoBase){
        this.nome=nome;
        this.descricao=descricao;
        this.precoBase=precoBase;
        this.nota = 0;
    }
    
    
    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @return the precoBase
     */
    public double getPrecoBase() {
        return precoBase;
    }

    /**
     * @return the vendedor
     */
    public Cliente getVendedor() {
        return vendedor;
    }

    public void setVendedor(Cliente vendedor) {
        this.vendedor = vendedor;
    }

    /**
     * @return the categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }


    public void setNome(String novoNome) {
        this.nome = novoNome;
    }


    public void setDescricao(String novaDescricao) {
        this.descricao = novaDescricao;  
    }


    public void setPrecoBase(double novoPrecoBase) {
        this.precoBase = novoPrecoBase;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public double getNota() {
        return nota;
    }
}
