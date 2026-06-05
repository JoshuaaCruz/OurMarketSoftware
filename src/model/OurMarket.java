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
import model.categoria_produto.ItemProduto;
import model.categoria_produto.Produto;
import model.cliente.Cliente;
import model.contaBancaria.ContaBancaria;
import model.contaBancaria.FormaDePagamento;

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

    /**
     * Finds a client by login and senha and sets them as the logged-in user.
     * @return the matched Cliente, or null if credentials are invalid
     */
    public Cliente login(String login, String senha) {
        if (login == null || senha == null) return null;
        for (Cliente c : clientes) {
            if (login.equals(c.getLogin()) && senha.equals(c.getSenha())) {
                clienteLogado = c;
                return c;
            }
        }
        return null;
    }

    
    public void logoff() {
        clienteLogado = null;
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

        ContaBancaria contaLoja = new ContaBancaria();
        contaLoja.setNumero("12345-6");
        lojaPadrao.setContaCliente(contaLoja);

        Produto p1 = new Produto("Notebook Gamer", "Intel i9, 32GB RAM, RTX 4080", 8999.99);
        Produto p2 = new Produto("Fone Bluetooth Pro", "Cancelamento de ruído ativo, 40h de bateria", 599.90);
        Produto p3 = new Produto("SSD 1TB NVMe", "Leitura 7000MB/s, compatível PCIe 4.0", 349.90);

        p1.setNota(5); p1.setVendedor(lojaPadrao);
        p2.setNota(3); p2.setVendedor(lojaPadrao);
        p3.setNota(4); p3.setVendedor(lojaPadrao);

        lojaPadrao.getEstoque().adicionarProduto(p1, 50);
        lojaPadrao.getEstoque().adicionarProduto(p2, 30);
        lojaPadrao.getEstoque().adicionarProduto(p3, 20);

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

    //compra em si, utiliza metodo pagamento selecionado do comprador, atualiza listas, e retorna status
    public String processarCompra(Cliente comprador, FormaDePagamento forma) {
        if (comprador == null || forma == null) return "Erro: Dados inválidos.";

        List<ItemProduto> carrinho = comprador.getCarrinho().getItens();
        if (carrinho.isEmpty()) {
            return "Erro: Carrinho vazio.";
        }

        ContaBancaria origem = comprador.getContaCliente();
        if (origem == null){
            return "Erro: Comprador não possui conta bancária.";
    }

        if (!origem.selecionarFormaPagamento(forma)){
            return "Erro: Forma de pagamento inválida na conta.";
        }

        // Pass 1: Validate Stock and Calculate Total
        double totalGasto = 0;
        for (ItemProduto item : carrinho) {
            Produto p = item.getProduto();
            int qtd = item.getQuantidadeProduto();
            
            Cliente vendedor = p.getVendedor();
            if (vendedor == null) return "Erro: Produto '" + p.getNome() + "' não tem vendedor associado.";
            if (vendedor.getContaCliente() == null) return "Erro: Vendedor '" + vendedor.getName() + "' não possui conta para receber.";
            
            int estoqueVendedor = vendedor.getEstoque().getQuantidade(p);
            if (qtd > estoqueVendedor) {
                return "Erro de Estoque: O vendedor '" + vendedor.getName() + "' possui apenas " + estoqueVendedor + " unidades de '" + p.getNome() + "'.";
            }
            
            totalGasto += p.getPrecoBase() * qtd;
        }

        // Pass 2: Check balance
        if (!origem.podePagar(totalGasto)) {
            return "Erro: Saldo/Limite insuficiente para a compra no valor de R$ " + String.format("%.2f", totalGasto) + ".";
        }

        // Pass 3: Execute transactions

        for (ItemProduto item : carrinho) {
            Produto p = item.getProduto();
            int qtd = item.getQuantidadeProduto();
            double subtotal = p.getPrecoBase() * qtd;
            
            Cliente vendedor = p.getVendedor();
            
            // Transfer money
            origem.pagar(vendedor.getContaCliente(), subtotal);

            // Remove from seller stock
            vendedor.getEstoque().removerProduto(p, qtd);
            
            // Add to buyer history
            comprador.addPedidoComprado(new ItemProduto(p, qtd));

            //Add to seller history
            vendedor.addProdutoVendido(new ItemProduto(p, qtd));
        }

        // Pass 4: Empty the cart
        comprador.getCarrinho().limparColecao();
        
        return "Sucesso";
    }

    {
        categoriaRaiz = new Categoria("Geral");
        autenticador = Fabrica.new_Autenticador(); //autenticacao foi simplificada, avaliar para deletar model/autenticador
        clientes = new ArrayList<>();
    }
}
