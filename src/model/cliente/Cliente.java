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
    private Set<String> produtosAvaliados;
    private int totalProdutosAvaliados = 0;

    public Cliente() {
        this.estoque = new ColecaoProdutos();
        this.carrinho = new ColecaoProdutos();
        this.listaDesejos = new ArrayList<>();
        this.produtosVendidos = new ArrayList<>();
        this.enderecos = new ArrayList<>();
        this.pedidosComprados = new ArrayList<>();
        this.cuponsUsados = new HashSet<>();
        this.produtosAvaliados = new HashSet<>();
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

    public Endereco getEndereco() {
        return enderecos.isEmpty() ? null : enderecos.get(0);
    }

    public void setEndereco(Endereco endereco) {
        enderecos.clear();
        if (endereco != null) {
            enderecos.add(endereco);
        }
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
        Set<Produto> todosProdutos = new HashSet<>();
        
        //como nota do vendedor depende da nota de todos os produtos dele, somamos produtos ainda no estoque + totalmente vendidos
        if (this.estoque != null) {
            todosProdutos.addAll(this.estoque.getProdutos());
        }
        
        if (this.produtosVendidos != null) {
            for (ItemProduto item : this.produtosVendidos) {
                if (item.getProduto() != null) {
                    todosProdutos.add(item.getProduto());
                }
            }
        }

        double somaPonderada = 0;
        int totalVotosVendedor = 0;
        int qtdProdAvaliados = 0;

        for (Produto produto : todosProdutos) {
            int votos = produto.getTotalVotos();
            if (votos > 0) {
                somaPonderada += (produto.getNota() * votos);
                totalVotosVendedor += votos;
                qtdProdAvaliados++;
            }
        }

        if (totalVotosVendedor == 0) {
            this.nota = 0;
            this.totalProdutosAvaliados = 0;
        } else {
            this.nota = somaPonderada / totalVotosVendedor;
            this.totalProdutosAvaliados = qtdProdAvaliados;
        }
    }

    public int getTotalProdutosAvaliados() {
        return totalProdutosAvaliados;
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

    public List<ItemProduto> getProdutosVendidos() {
        return produtosVendidos;
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

    public Set<String> getCuponsUsados() {
        return cuponsUsados;
    }



    private String chaveAvaliacao(Produto produto) {
        if (produto.getId() > 0) {
            return String.valueOf(produto.getId());
        }
        // caso o id seja 0, não deve acontecer mas por garantia
        String cpfVendedor = (produto.getVendedor() != null) ? produto.getVendedor().getCPF() : "sem_vendedor";
        return produto.getNome() + "::" + cpfVendedor;
    }

    public boolean jaAvaliou(Produto produto) {
        return produtosAvaliados.contains(chaveAvaliacao(produto));
    }

    public void marcarComoAvaliado(Produto produto) {
        produtosAvaliados.add(chaveAvaliacao(produto));
    }

    public Set<String> getProdutosAvaliados() {
        return produtosAvaliados;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                ", CPF='" + CPF + '\'' +
                ", login='" + login + '\'' +
                ", senha='" + senha + '\'' +
                ", dataNascimento=" + dataNascimento +
                '}';
    }

    public static ClienteBuilder builder() {
        return new ClienteBuilder();
    }

    public static final class ClienteBuilder {
        private String nome, CPF, login, senha;
        private LocalDate dataNascimento;
        private List<Endereco> enderecos;
        private ContaBancaria contaCliente;

        public ClienteBuilder() {
            this.enderecos = new ArrayList<>();
        }

        public ClienteBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public ClienteBuilder cpf(String CPF) {
            this.CPF = CPF;
            return this;
        }

        public ClienteBuilder login(String login) {
            this.login = login;
            return this;
        }

        public ClienteBuilder senha(String senha) {
            this.senha = senha;
            return this;
        }

        public ClienteBuilder dataNascimento(LocalDate dataNascimento) {
            this.dataNascimento = dataNascimento;
            return this;
        }

        public ClienteBuilder contaCliente(ContaBancaria conta) {
            this.contaCliente = conta;
            return this;
        }

        public ClienteBuilder endereco(Endereco endereco) {
            this.enderecos.add(endereco);
            return this;
        }

        public ClienteBuilder enderecos(List<Endereco> enderecos) {
            this.enderecos.addAll(enderecos);
            return this;
        }
        public Cliente build() {
            Cliente cliente = new Cliente();
            cliente.setNome(this.nome);
            cliente.setCPF(this.CPF);
            cliente.setLogin(this.login);
            cliente.setSenha(this.senha);
            cliente.setDataNascimento(this.dataNascimento);
            cliente.setContaCliente(contaCliente);            return cliente;
        }
    }
}
