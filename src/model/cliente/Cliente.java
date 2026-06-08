package model.cliente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.categoria_produto.ColecaoProdutos;
import model.categoria_produto.ItemProduto;
import model.categoria_produto.Produto;
import model.contaBancaria.ContaBancaria;


public class Cliente implements Cliente_if{
    private String nome, CPF, login, senha;
    private LocalDate dataNascimento;
    private List<Endereco> enderecos;
    private ContaBancaria contaCliente;
    private ColecaoProdutos estoque, carrinho;
    private double nota;
    private List<Produto> listaDesejos;
    private List<ItemProduto> produtosVendidos;
    private List<ItemProduto> pedidosComprados;
    private Set<String> cuponsUsados;

    public Cliente() {
        this.estoque = new ColecaoProdutos();
        this.carrinho = new ColecaoProdutos();
        this.listaDesejos = new ArrayList<>();
        this.produtosVendidos = new ArrayList<>();
        this.enderecos = new ArrayList<>();
        this.pedidosComprados = new ArrayList<>();
        this.cuponsUsados = new HashSet<>();
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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
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

    public void addProdutoVendido(ItemProduto produto){
        for(ItemProduto item : produtosVendidos){
            if(item.getProduto().equals(produto.getProduto())){
                item.setQuantidadeProduto(item.getQuantidadeProduto() + produto.getQuantidadeProduto());
                return;
            }
        }
        produtosVendidos.add(produto);
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

    public List<ItemProduto> getPedidosComprados() {
        return pedidosComprados;
    }

    public void addPedidoComprado(ItemProduto item) {
        if (item != null) {
            pedidosComprados.add(item);
        }
    }


    public boolean jaFoiUsado(String codigo) {
        if (codigo == null) return false;
        return cuponsUsados.contains(codigo.toUpperCase());
    }

    public void markCupomAsUsado(String codigo) {
        if (codigo != null) {
            cuponsUsados.add(codigo.toUpperCase());
        }
    }
}