/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.cliente;

import java.util.ArrayList;
import java.util.List;
import model.categoria_produto.ColecaoProdutos;
import model.categoria_produto.Produto;
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
    private double nota;
    private List<Produto> listaDesejos;
    private List<Produto> produtosVendidos;

    public Cliente() {
        this.estoque = new ColecaoProdutos();
        this.carrinho = new ColecaoProdutos();
        this.listaDesejos = new ArrayList<>();
        this.produtosVendidos = new ArrayList<>();
    }

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

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    /**
     * @return the ContaBancaria
     */
    public ContaBancaria getContaCliente() {
        return contaCliente;
    }

    public void setContaCliente(ContaBancaria contaCliente) {
        this.contaCliente = contaCliente;
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

    public double getNota() {
        calculaNotaColecao(this.estoque);
        return nota;
    }

    public void calculaNotaColecao(ColecaoProdutos colecao) {
        if (colecao == null || colecao.getProdutos().isEmpty()) {
            this.nota = 0;
            return;
        }
        double somaNotas = 0;
        for (model.categoria_produto.Produto produto : colecao.getProdutos()) {
            somaNotas += produto.getNota();
        }
        this.nota = somaNotas / colecao.getProdutos().size();
    }

    public int getQuantidadeProdutosVendidos() {
        return produtosVendidos.size();
    }

    public List<Produto> getListaDesejos() {
        return listaDesejos;
    }

    public void addProdutoListaDesejo(Produto produto){
        if (!this.listaDesejos.contains(produto)){
            this.listaDesejos.add(produto);
        }
    }

    public void deleteListaDesejos(){
        this.listaDesejos.clear();
    }

    public void removeProdutoListaDesejo(Produto produto){
            this.listaDesejos.remove(produto);
    }
}