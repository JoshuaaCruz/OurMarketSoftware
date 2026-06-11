package view.terminal;

import java.util.List;
import java.util.Scanner;
import model.Cupom;
import model.OurMarket;
import model.categoria_produto.ColecaoProdutos;
import model.categoria_produto.ItemProduto;
import model.categoria_produto.Produto;
import model.cliente.Cliente;
import model.cliente.Endereco;
import model.contaBancaria.ContaBancaria;
import model.contaBancaria.FormaDePagamento;

public class Carrinho_Checkout_View_Textual {

    private final OurMarket model;
    private final Scanner scanner;

    public Carrinho_Checkout_View_Textual(OurMarket model, Scanner scanner) {
        this.model = model;
        this.scanner = scanner;
    }

    public void iniciar() {
        Cliente cliente = model.getClienteLogado();
        if (cliente == null) return;

        // Facade method for the shopping cart
        ColecaoProdutos carrinho = model.getCarrinhoDoClienteLogado();
        if (carrinho == null) return;

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n===========================");
            System.out.println("  GERENCIAR CARRINHO");
            System.out.println("===========================");
            System.out.println("1. Adicionar Produto ao Carrinho");
            System.out.println("2. Ver Carrinho");
            System.out.println("3. Remover Produto do Carrinho");
            System.out.println("4. Ver Nota do Produto e Vendedor");
            System.out.println("5. Ver Fotos de um Produto");
            System.out.println("6. Finalizar Compra");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma operação: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    adicionarProdutoAoCarrinho(carrinho, cliente);
                    break;
                case 2:
                    verProdutos(carrinho);
                    break;
                case 3:
                    removerProduto(carrinho);
                    break;
                case 4:
                    Produtos_Menu_View_Textual.verNotaProdutoVendedor(scanner, carrinho);
                    break;
                case 5:
                    abrirFotosProduto(carrinho);
                    break;
                case 6:
                    finalizarCompra(cliente, carrinho);
                    break;
                case 0:
                    System.out.println("\nVoltando ao menu anterior...");
                    break;
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha um número do menu.");
                    break;
            }
        }
    }

    private void adicionarProdutoAoCarrinho(ColecaoProdutos carrinho, Cliente cliente) {
        System.out.println("\n--- ADICIONAR AO CARRINHO ---");

        Produto produto = Produtos_Menu_View_Textual.escolherProdutoDeVendedor(model, scanner, cliente);
        if (produto == null) return;

        System.out.print("Quantidade: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();

        if (quantidade <= 0) {
            System.out.println(" Quantidade inválida.");
            return;
        }

        // Facade: Delegate checking the available stock to the model.
        int estoqueDisponivel = model.getEstoqueDisponivel(produto);
        if (quantidade > estoqueDisponivel) {
            System.out.println(" Erro: O vendedor só possui " + estoqueDisponivel + " unidades em estoque.");
            return;
        }

        carrinho.adicionarProduto(produto, quantidade);
        model.salvar();
        System.out.println("\n " + quantidade + "x '" + produto.getNome() + "' adicionado ao Carrinho com sucesso!");
    }

    private void verProdutos(ColecaoProdutos colecao) {
        System.out.println("\n--- PRODUTOS NO CARRINHO ---");

        List<ItemProduto> itens = colecao.getItens();
        if (itens.isEmpty()) {
            System.out.println(" Nenhum produto encontrado no carrinho.");
            return;
        }

        System.out.println("\nTotal de itens diferentes: " + itens.size());
        System.out.println("-------------------------------------------------");

        for (int i = 0; i < itens.size(); i++) {
            ItemProduto item = itens.get(i);
            Produto p = item.getProduto();
            System.out.println("\n[" + (i + 1) + "] " + item.getQuantidadeProduto() + "x " + p.getNome());
            System.out.println("    Descrição: " + p.getDescricao());
            System.out.println("    Preço unitário: R$ " + String.format("%.2f", p.getPrecoBase()));
            System.out.println("    Subtotal: R$ " + String.format("%.2f", p.getPrecoBase() * item.getQuantidadeProduto()));
            if (p.getCategoria() != null) {
                System.out.println("    Categoria: " + p.getCategoria().getNome());
            }
            if (p.getVendedor() != null) {
                System.out.println("    Vendedor: " + p.getVendedor().getName());
            }
            Produtos_Menu_View_Textual.imprimirFotos(p);
        }
        System.out.println("-------------------------------------------------");
    }

    private void removerProduto(ColecaoProdutos colecao) {
        System.out.println("\n--- REMOVER PRODUTO ---");

        List<ItemProduto> itens = colecao.getItens();
        if (itens.isEmpty()) {
            System.out.println(" Nenhum produto disponível para remover.");
            return;
        }

        System.out.println("\nProdutos no carrinho:");
        for (int i = 0; i < itens.size(); i++) {
            System.out.println((i + 1) + ". " + itens.get(i).getProduto().getNome() + " (Qtd: " + itens.get(i).getQuantidadeProduto() + ")");
        }

        System.out.print("\nEscolha o número do produto a remover (0 para cancelar): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha == 0) {
            System.out.println("Operação cancelada.");
            return;
        }

        if (escolha < 1 || escolha > itens.size()) {
            System.out.println(" Erro: Número inválido.");
            return;
        }

        ItemProduto item = itens.get(escolha - 1);
        Produto produto = item.getProduto();
        String nomeProduto = produto.getNome();
        int maxQtd = item.getQuantidadeProduto();

        System.out.print("Quantidade a remover (máximo " + maxQtd + ", 0 para remover todos): ");
        int qtdRemover = scanner.nextInt();
        scanner.nextLine();

        if (qtdRemover <= 0) {
            qtdRemover = maxQtd;
        }
        if (qtdRemover > maxQtd) {
            System.out.println(" Erro: Você só possui " + maxQtd + " unidades.");
            return;
        }

        colecao.removerProduto(produto, qtdRemover);
        model.salvar();
        System.out.println("\n " + qtdRemover + "x '" + nomeProduto + "' removido do carrinho com sucesso!");
    }

    private void finalizarCompra(Cliente cliente, ColecaoProdutos carrinho) {
        System.out.println("\n--- FINALIZAR COMPRA ---");

        List<ItemProduto> itensCarrinho = carrinho.getItens();
        if (itensCarrinho.isEmpty()) {
            System.out.println(" O seu carrinho está vazio.");
            return;
        }

        double totalGasto = 0;
        System.out.println("\nResumo do Pedido:");
        for (ItemProduto item : itensCarrinho) {
            Produto p = item.getProduto();
            int qtd = item.getQuantidadeProduto();
            double sub = p.getPrecoBase() * qtd;
            totalGasto += sub;
            System.out.printf("- %dx %s (R$ %.2f) = R$ %.2f\n", qtd, p.getNome(), p.getPrecoBase(), sub);
        }
        System.out.printf("Total a pagar: R$ %.2f\n", totalGasto);

        Cupom cupom = null;
        boolean tentarCupom = true;
        while (tentarCupom) {
            System.out.print("\nPossui um cupom de desconto? Digite o código (Enter para pular): ");
            String codigoCupom = scanner.nextLine().trim();
            
            if (codigoCupom.isEmpty()) {
                break;
            }
            
            Cupom c = model.buscarCupom(codigoCupom);
            if (c == null) {
                System.out.println(" Cupom não encontrado.");
            } else if (!model.cupomDisponivel(cliente, c)) {
                System.out.println(" Você já utilizou o cupom '" + c.getCodigo() + "'.");
            } else {
                double totalComDesconto = c.aplicar(totalGasto);
                System.out.println(" Cupom válido! " + c.getDescricao());
                System.out.printf(" Total original:      R$ %.2f%n", totalGasto);
                System.out.printf(" Desconto aplicado:  -R$ %.2f%n", totalGasto - totalComDesconto);
                System.out.printf(" Total com desconto:  R$ %.2f%n", totalComDesconto);
                
                System.out.print(" Deseja aplicar este cupom? (S/N): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
                    cupom = c;
                    break;
                }
            }
            
            System.out.print(" Deseja tentar outro cupom? (S/N): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("S")) {
                tentarCupom = false;
            }
        }

        List<Endereco> enderecos = cliente.getEnderecos();
        if(enderecos.isEmpty()){
            System.out.println("\n Erro: Você não possui nenhum endereço cadastrado.");
            return;
        }
        System.out.println("\nSelecione o Endereço de Entrega:");
        for (int i = 0; i < enderecos.size(); i++) {
            System.out.println((i + 1) + ". " + enderecos.get(i).toString());
        }
        System.out.print("Escolha o número do endereço (0 para cancelar): ");
        int endEscolha = scanner.nextInt();
        scanner.nextLine();
        if (endEscolha <= 0 || endEscolha > enderecos.size()) {
            System.out.println(" Operação cancelada.");
            return;
        }
        Endereco enderecoEscolhido = enderecos.get(endEscolha - 1);

        // Facade
        ContaBancaria conta = model.getContaDoClienteLogado();
        if (conta == null) {
            System.out.println("\n Erro: Você não possui uma conta bancária vinculada.");
            return;
        }
        
        List<FormaDePagamento> formas = model.getFormasPagamentoDoClienteLogado();
        if (formas.isEmpty()) {
            System.out.println("\n Você não possui nenhuma forma de pagamento cadastrada.");
            return;
        }

        System.out.println("\nSelecione a Forma de Pagamento:");
        for (int i = 0; i < formas.size(); i++) {
            System.out.println((i + 1) + ". " + formas.get(i).getNome() + " (" + formas.get(i).getDescricao(conta) + ")");
        }
        System.out.print("Escolha o número (0 para cancelar): ");
        int pagEscolha = scanner.nextInt();
        scanner.nextLine();
        if (pagEscolha <= 0 || pagEscolha > formas.size()) {
            System.out.println(" Operação cancelada.");
            return;
        }
        FormaDePagamento formaEscolhida = formas.get(pagEscolha - 1);

        System.out.println("\n--- CONFIRMAÇÃO DE COMPRA ---");
        System.out.printf("Endereço de Entrega: %s\n", enderecoEscolhido.toString());
        System.out.printf("Forma de Pagamento:  %s\n", formaEscolhida.getNome());
        if (cupom != null) {
            System.out.printf("Total a pagar:       R$ %.2f (com desconto do cupom)\n", cupom.aplicar(totalGasto));
        } else {
            System.out.printf("Total a pagar:       R$ %.2f\n", totalGasto);
        }
        
        System.out.print("\nConfirmar compra? (S/N): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("S")) {
            System.out.println(" Compra cancelada pelo usuário.");
            return;
        }

        System.out.println("\nProcessando compra...");
        String resultado = model.processarCompra(cliente, formaEscolhida, cupom);
        
        if (resultado.equals("Sucesso")) {
            System.out.println("\n Compra realizada com sucesso!");
            System.out.println(" O pedido será entregue em: " + enderecoEscolhido.toString());
        } else {
            System.out.println("\n A compra falhou.");
            System.out.println(resultado);
        }
    }

    private void abrirFotosProduto(ColecaoProdutos colecao) {
        System.out.println("\n--- VER FOTOS ---");

        List<Produto> produtos = colecao.getProdutos();
        if (produtos.isEmpty()) {
            System.out.println(" A coleção está vazia.");
            return;
        }

        System.out.println("\nEscolha o produto:");
        for (int i = 0; i < produtos.size(); i++) {
            System.out.println((i + 1) + ". " + produtos.get(i).getNome());
        }

        System.out.print("\nOpção (0 para cancelar): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha >= 1 && escolha <= produtos.size()) {
            Produtos_Menu_View_Textual.abrirFotos(produtos.get(escolha - 1));
        } else if (escolha != 0) {
            System.out.println(" Erro: Número inválido.");
        }
    }
}
