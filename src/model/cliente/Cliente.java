package model.cliente;

import java.util.ArrayList;
import java.util.List;
import model.categoria_produto.ColecaoProdutos;
import model.categoria_produto.Produto;
import model.contaBancaria.ContaBancaria;


public class Cliente implements Cliente_if{
    private String nome, CPF, login, senha;
    private List<Endereco> enderecos;
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
        this.enderecos = new ArrayList<>();
    }

    @Override
    public String getName() {
        return nome;
    }
    
     public String getCPF() {
        return CPF;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }


    public List<Endereco> getEnderecos() {
        return enderecos;
    }


    public void addEndereco(Endereco endereco) {
        if (endereco != null) {
            enderecos.add(endereco);
        }
    }

    public boolean removeEndereco(Endereco endereco) {
        if (enderecos.size() <= 1) return false;
        return enderecos.remove(endereco);
    }

    public ContaBancaria getContaCliente() {
        return contaCliente;
    }

    public void setContaCliente(ContaBancaria contaCliente) {
        this.contaCliente = contaCliente;
    }


    public ColecaoProdutos getEstoque() {
        return estoque;
    }


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