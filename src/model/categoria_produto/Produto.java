/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    
    public Produto(String nome, String descricao, double precoBase){
        this.nome=nome;
        this.descricao=descricao;
        this.precoBase=precoBase;
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
}
