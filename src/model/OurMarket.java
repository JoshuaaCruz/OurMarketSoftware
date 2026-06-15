package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import db.LivreMercadoDAO;
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
    private final List<Cupom> cupons;
    private final Categoria_if categoriaRaiz;
    
    // Cliente atualmente autenticado na sessão
    private Cliente clienteLogado;
    private LivreMercadoDAO persistencia;

    public OurMarket() {
        this(true);
    }

    public OurMarket(boolean inicializarPadrao) {
        if (inicializarPadrao) {
            inicializarDadosPadrao();
        }
    }

    public void habilitarPersistencia(LivreMercadoDAO persistencia) {
        this.persistencia = persistencia;
    }

    public void salvar() {
        if (persistencia == null) {
            return;
        }
        try {
            persistencia.salvar(this);
        } catch (java.sql.SQLException exception) {
            System.err.println("Erro ao salvar banco: " + exception.getMessage());
        }
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

    public boolean loginExiste(String login) {
        if (login == null) return false;
        for (Cliente c : clientes) {
            if (login.equals(c.getLogin())) 
                return true;
        }
        return false;
    }

    public boolean cpfExiste(String cpf) {
        if (cpf == null) return false;
        for (Cliente c : clientes) {
            if (cpf.equals(c.getCPF())) return true;
        }
        return false;
    }

    public void inicializarDadosPadrao() {
        if (!clientes.isEmpty()) {
            return;
        }
        // inicializa vendedor padrão do sistema para ja haver produtos cadastrados
        Cliente lojaPadrao = new Cliente();
        lojaPadrao.setNome("Loja Padrão");
        lojaPadrao.setCPF("00000000000");

        ContaBancaria contaLoja = new ContaBancaria();
        contaLoja.setNumero("12345-6");
        lojaPadrao.setContaCliente(contaLoja);
        clientes.add(lojaPadrao);

        // inicializa categorias

        Categoria catTech = new Categoria("Tecnologia");
        categoriaRaiz.addSubCategoria(catTech);
        
        Categoria catInformatica = new Categoria("Informatica");
        catTech.addSubCategoria(catInformatica);
        
        Categoria catGames = new Categoria("Games");
        catTech.addSubCategoria(catGames);
        
        Categoria catCelulares = new Categoria("Celulares e Telefones");
        catTech.addSubCategoria(catCelulares);

        Categoria catPecasCelular = new Categoria("Peças para Celular");
        catCelulares.addSubCategoria(catPecasCelular);

        // Instancia produtos
        Produto pGta = new Produto("GTA", "SO MUCH FUN", 100);
        Produto pRdr2 = new Produto("RDR2", "SO MUCH COWBOYS", 1000);
        Produto pCarregador = new Produto("Carregador", "Carrega Iphone", 50);
        Produto pCapinha = new Produto("capinha", "Protege Iphone", 50);
        Produto pNotebook = new Produto("Notebook Gamer", "Intel i9, 32GB RAM, RTX 4080", 8000);
        Produto pFone = new Produto("Fone Bluetooth Pro", "Cancelamento de ruído ativo, 40h de bateria", 600);
        Produto pSsd = new Produto("SSD 1TB NVMe", "Leitura 7000MB/s, compatível PCIe 4.0", 300);

        // Configura vendor, nota, categoria
        adicionarProdutoAoSistema(pGta, lojaPadrao, 0, catGames, 10);
        adicionarProdutoAoSistema(pRdr2, lojaPadrao, 0, catGames, 10);
        adicionarProdutoAoSistema(pCarregador, lojaPadrao, 0, catPecasCelular, 50);
        adicionarProdutoAoSistema(pCapinha, lojaPadrao, 0, catPecasCelular, 20);
        adicionarProdutoAoSistema(pNotebook, lojaPadrao, 0, catInformatica, 1);
        adicionarProdutoAoSistema(pFone, lojaPadrao, 0, catInformatica, 1);
        adicionarProdutoAoSistema(pSsd, lojaPadrao, 0, catInformatica, 5);
    }

    private void adicionarProdutoAoSistema(Produto p, Cliente vendedor, double nota, Categoria cat, int qtd) {
        p.setVendedor(vendedor);
        p.setNota(nota);
        p.setCategoria(cat);
        cat.addProduto(p);
        vendedor.getEstoque().adicionarProduto(p, qtd);
    }
    

    public Categoria_if getCategoriaRaiz() {
        return categoriaRaiz;
    }
    

    public ColecaoProdutos getCarrinhoDoClienteLogado() {
        if (clienteLogado == null) {
            return null;
        }
        return clienteLogado.getCarrinho();
    }


    public ColecaoProdutos getEstoqueDoClienteLogado() {
        if (clienteLogado == null) {
            return null;
        }
        return clienteLogado.getEstoque();
    }

    public ContaBancaria getContaDoClienteLogado() {
        if (clienteLogado == null) return null;
        return clienteLogado.getContaCliente();
    }

    public List<FormaDePagamento> getFormasPagamentoDoClienteLogado() {
        ContaBancaria conta = getContaDoClienteLogado();
        if (conta == null) return Collections.emptyList();
        return conta.getFormasPagamento();
    }

    public int getEstoqueDisponivel(Produto produto) {
        if (produto == null || produto.getVendedor() == null) return 0;
        return produto.getVendedor().getEstoque().getQuantidade(produto);
    }


    public List<Cliente> getClientes() {
        return Collections.unmodifiableList(clientes);
    }

    public List<Cupom> getCupons() {
        return Collections.unmodifiableList(cupons);
    }

    public void adicionarCupom(Cupom cupom) {
        if (cupom == null) {
            return;
        }
        for (int i = 0; i < cupons.size(); i++) {
            if (cupons.get(i).getCodigo().equals(cupom.getCodigo())) {
                cupons.set(i, cupom);
                return;
            }
        }
        cupons.add(cupom);
    }


    public void adicionarCliente(Cliente cliente) {
        if (cliente != null) {
            for (int i = 0; i < clientes.size(); i++) {
                Cliente existente = clientes.get(i);
                if (existente.getCPF() != null && existente.getCPF().equals(cliente.getCPF())) {
                    clientes.set(i, cliente);
                    return;
                }
            }
            clientes.add(cliente);
        }
    }

    /**
     * Procura cupom pelo codigo (case-insensitive)
     * @return o Cupom, ou null se nao encontrou
     */
    public Cupom buscarCupom(String codigo) {
        if (codigo == null) return null;
        String upper = codigo.trim().toUpperCase();
        for (Cupom c : cupons) {
            if (c.getCodigo().equals(upper)) return c;
        }
        return null;
    }


    public boolean cupomDisponivel(Cliente cliente, Cupom cupom) {
        if (cliente == null || cupom == null) return false;
        return !cliente.jaFoiUsado(cupom.getCodigo());
    }

    public String avaliarProduto(Cliente comprador, Produto produto, double nota) {
        if (comprador == null || produto == null) return "Erro: Dados inválidos.";
        if (nota < 0 || nota > 5) return "Erro: Nota deve ser entre 0 e 5.";

        // verificando se cliente já comprou o produto
        boolean comprou = false;
        for (ItemProduto item : comprador.getPedidosComprados()) {
            if (item.getProduto() != null && item.getProduto().getNome().equals(produto.getNome())) {
                comprou = true;
                break;
            }
        }
        if (!comprou) return "Erro: Você só pode avaliar produtos que comprou.";

        // não deixa avaliar produtos que ja o fez
        if (comprador.jaAvaliou(produto)) return "Erro: Você já avaliou este produto.";

        produto.adicionarAvaliacao(nota);
        comprador.marcarComoAvaliado(produto);

        // atualizando nota do vendedor
        Cliente vendedor = produto.getVendedor();
        if (vendedor != null) {
            vendedor.calculaNotaColecao(vendedor.getEstoque());
        }

        salvar();
        return "Sucesso";
    }

    //compra em si, utiliza metodo pagamento selecionado do comprador, atualiza listas, e retorna status
    public String processarCompra(Cliente comprador, FormaDePagamento forma, Cupom cupom) {
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

        double totalFinal = cupom == null ? totalGasto : cupom.aplicar(totalGasto);
        double fatorDesconto = totalGasto <= 0 ? 1 : totalFinal / totalGasto;

        // Pass 2: Check balance
        if (!origem.podePagar(totalFinal)) {
            return "Erro: Saldo/Limite insuficiente para a compra no valor de R$ " + String.format("%.2f", totalFinal) + ".";
        }

        // Pass 3: Execute transactions

        for (ItemProduto item : carrinho) {
            Produto p = item.getProduto();
            int qtd = item.getQuantidadeProduto();
            double subtotal = p.getPrecoBase() * qtd * fatorDesconto;
            
            Cliente vendedor = p.getVendedor();
            
            // Transfer money
            origem.pagar(vendedor.getContaCliente(), subtotal);

            // Remove from seller stock
            vendedor.getEstoque().removerProduto(p, qtd);
            
            // Add to buyer history
            comprador.addPedidoComprado(new ItemProduto(p, qtd));

            //Add to seller history
            vendedor.addProdutoVendido(new ItemProduto(p, qtd));

            // Increment product sales counter
            p.setVendas(p.getVendas() + qtd);
        }

        // Pass 4: Empty the cart and mark cupom as used
        comprador.getCarrinho().limparColecao();
        if (cupom != null) {
            comprador.markCupomAsUsado(cupom.getCodigo());
        }

        salvar();
        return "Sucesso";
    }

    {
        categoriaRaiz = new Categoria("Geral");

        clientes = new ArrayList<>();
        cupons = new ArrayList<>();
        cupons.add(Cupom.ANIVERSARIO);
    }
}
