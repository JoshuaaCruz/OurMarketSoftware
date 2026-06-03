/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.autenticador.Autenticador_if;
import model.categoria_produto.Categoria;
import model.categoria_produto.Categoria_if;
import model.categoria_produto.ColecaoProdutos;
import model.categoria_produto.Produto;
import model.cliente.Cliente;
import model.contaBancaria.CartaoCredito;
import model.contaBancaria.ContaBancaria;
import model.contaBancaria.FormaDePagamento;
import model.contaBancaria.Pix;

public class OurMarket {
    
    private final List<Cliente> clientes;
    private ContaBancaria contaMercado;
    
    private final Autenticador_if autenticador;
    private final Categoria_if categoriaRaiz;
    
    // Cliente atualmente autenticado na sessão
    private Cliente clienteLogado;

    public OurMarket() {
        initOrLoad();
    }

    public Autenticador_if getAutenticador() {
        return autenticador;
    }

    /**
     * @return o cliente atualmente autenticado, ou null se nenhum
     */
    public Cliente getClienteLogado() {
        return clienteLogado;
    }

    /**
     * @param cliente o cliente que acabou de se autenticar
     */
    public void setClienteLogado(Cliente cliente) {
        this.clienteLogado = cliente;
    }

    private void initOrLoad() {
        // inicializa categorias
        Categoria g1, g2;
        categoriaRaiz.addCategoria(new Categoria("Veiculos"));
        g1 = new Categoria("Tecnologia");
        categoriaRaiz.addCategoria(g1);
        categoriaRaiz.addCategoria(new Categoria("Casa e Mooveis"));
        g1.addCategoria(new Categoria("Informatica"));
        
        Categoria games = new Categoria("Games");
        g1.addCategoria(games);
        
        games.addProduto(new Produto("GTA", "SO MUCH FUN", 100));
        games.addProduto(new Produto("RDR2", "SO MUCH COWBOYS", 1000));

        g2 = new Categoria("Celulares e Telefones");
        g1.addCategoria(g2);

        Categoria pecasCelular = new Categoria("Peças para Celular");
        Produto randP = new Produto("Carregador", "Carrega Iphone", 50);
        Produto randP2 = new Produto("capinha", "Carrega Iphone", 5000);
        
        pecasCelular.addProduto(randP);
        pecasCelular.addProduto(randP2);
        g2.addCategoria(pecasCelular);

        // inicializa vendedor padrão do sistema para ja haver produtos cadastrados
        Cliente lojaPadrao = new Cliente();
        lojaPadrao.setNome("Loja Padrão");
        lojaPadrao.setCPF("00000000000");

        Produto p1 = new Produto("Notebook Gamer", "Intel i9, 32GB RAM, RTX 4080", 8999.99);
        Produto p2 = new Produto("Fone Bluetooth Pro", "Cancelamento de ruído ativo, 40h de bateria", 599.90);
        Produto p3 = new Produto("SSD 1TB NVMe", "Leitura 7000MB/s, compatível PCIe 4.0", 349.90);

        p1.setNota(5); p1.setVendedor(lojaPadrao);
        p2.setNota(3); p2.setVendedor(lojaPadrao);
        p3.setNota(4); p3.setVendedor(lojaPadrao);

        lojaPadrao.getEstoque().adicionarProduto(p1);
        lojaPadrao.getEstoque().adicionarProduto(p2);
        lojaPadrao.getEstoque().adicionarProduto(p3);

        clientes.add(lojaPadrao);
    }
    
    /**
     * @return the categoriaRaiz
     */
    public Categoria_if getCategoriaRaiz() {
        return categoriaRaiz;
    }

    // ===== Service Methods for Product Management =====
    
    /**
     * Gets the shopping cart (carrinho) of the currently logged-in client
     * @return ColecaoProdutos representing the client's cart, or null if no client is logged in
     */
    public ColecaoProdutos getCarrinhoDoClienteLogado() {
        if (clienteLogado == null) {
            return null;
        }
        return clienteLogado.getCarrinho();
    }

    /**
     * Gets the inventory (estoque) of the currently logged-in client
     * @return ColecaoProdutos representing the client's inventory, or null if no client is logged in
     */
    public ColecaoProdutos getEstoqueDoClienteLogado() {
        if (clienteLogado == null) {
            return null;
        }
        return clienteLogado.getEstoque();
    }

    /**
     * Adds a product to the cart of the currently logged-in client
     * @param produto The product to add
     * @throws IllegalStateException if no client is logged in
     */
    public void adicionarProdutoAoCarrinho(Produto produto) {
        if (clienteLogado == null) {
            throw new IllegalStateException("Nenhum cliente autenticado. Não é possível adicionar produto ao carrinho.");
        }
        clienteLogado.getCarrinho().adicionarProduto(produto);
    }

    /**
     * Removes a product from the cart of the currently logged-in client
     * @param produto The product to remove
     * @throws IllegalStateException if no client is logged in
     */
    public void removerProdutoDoCarrinho(Produto produto) {
        if (clienteLogado == null) {
            throw new IllegalStateException("Nenhum cliente autenticado. Não é possível remover produto do carrinho.");
        }
        clienteLogado.getCarrinho().removerProduto(produto);
    }

    /**
     * Adds a product to the inventory (estoque) of the currently logged-in client
     * @param produto The product to add
     * @throws IllegalStateException if no client is logged in
     */
    public void adicionarProdutoAoEstoque(Produto produto) {
        if (clienteLogado == null) {
            throw new IllegalStateException("Nenhum cliente autenticado. Não é possível adicionar produto ao estoque.");
        }
        clienteLogado.getEstoque().adicionarProduto(produto);
    }

    /**
     * Removes a product from the inventory (estoque) of the currently logged-in client
     * @param produto The product to remove
     * @throws IllegalStateException if no client is logged in
     */
    public void removerProdutoDoEstoque(Produto produto) {
        if (clienteLogado == null) {
            throw new IllegalStateException("Nenhum cliente autenticado. Não é possível remover produto do estoque.");
        }
        clienteLogado.getEstoque().removerProduto(produto);
    }

    /**
     * Returns all registered clients.
     */
    public List<Cliente> getClientes() {
        return Collections.unmodifiableList(clientes);
    }

    /**
     * Registers a new client in the marketplace.
     */
    public void adicionarCliente(Cliente cliente) {
        if (cliente != null) {
            clientes.add(cliente);
        }
    }

    /**
     * Processes a purchase from comprador to vendedor using the specified payment method.
     */
    public boolean processarCompra(Cliente comprador, Cliente vendedor, double valor, String formaPagamento) {
        if (comprador == null || vendedor == null || valor <= 0 || formaPagamento == null) {
            return false;
        }
        ContaBancaria origem = comprador.getContaCliente();
        ContaBancaria destino = vendedor.getContaCliente();
        if (origem == null || destino == null) {
            return false;
        }

        FormaDePagamento forma;
        switch (formaPagamento.toLowerCase()) {
            case "pix":
                forma = new Pix();
                break;
            case "cartão de crédito":
            case "cartao de crédito":
            case "cartao de credito":
            case "cartão de credito":
                forma = new CartaoCredito();
                break;
            default:
                return false;
        }

        return forma.pagar(origem, destino, valor);
    }

    {
        categoriaRaiz = new Categoria("Geral");
        autenticador = Fabrica.new_Autenticador();
        clientes = new ArrayList<>();
    }
}
