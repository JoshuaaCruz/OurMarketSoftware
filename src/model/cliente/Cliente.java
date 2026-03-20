/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.cliente;

import model.categoria_produto.ColecaoProdutos;
import model.contaBancaria.ContaBancaria;

/**
 *
 * @author joshua.cruz
 */
public class Cliente implements Cliente_if{
    private String nome, CPF;
    private Endereco endereco;
    private ContaBancaria contaCliente;
    private ColecaoProdutos estoque, carrinho;

    @Override
    public String getName() {
        return nome;
    }
    
     public String getCPF() {
        return CPF;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @param CPF the CPF to set
     */
    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    /**
     * @return the endereco
     */
    public Endereco getEndereco() {
        return endereco;
    }

    /**
     * @return the ContaBancaria
     */
    public ContaBancaria getContaCliente() {
        return contaCliente;
    }

    /**
     * @return the estoque
     */
    public ColecaoProdutos getEstoque() {
        return estoque;
    }

    /**
     * @return the carrinho
     */
    public ColecaoProdutos getCarrinho() {
        return carrinho;
    }
    
}
