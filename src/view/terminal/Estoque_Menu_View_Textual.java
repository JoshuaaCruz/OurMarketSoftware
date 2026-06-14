package view.terminal;

import java.util.List;
import java.util.Scanner;
import model.OurMarket;
import model.categoria_produto.Categoria;
import model.categoria_produto.Categoria_if;
import model.categoria_produto.ColecaoProdutos;
import model.categoria_produto.ItemProduto;
import model.categoria_produto.Produto;
import model.cliente.Cliente;

public class Estoque_Menu_View_Textual {

    private final OurMarket model;
    private final Scanner scanner;

    public Estoque_Menu_View_Textual(OurMarket model, Scanner scanner) {
        this.model = model;
        this.scanner = scanner;
    }

    public void iniciar() {
        Cliente cliente = model.getClienteLogado();
        if (cliente == null) return;
        
        // Facade: using getEstoqueDoClienteLogado
        ColecaoProdutos estoque = model.getEstoqueDoClienteLogado();
        if (estoque == null) return;

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n===========================");
            System.out.println("  GERENCIAR ESTOQUE");
            System.out.println("===========================");
            System.out.println("1. Adicionar Produto");
            System.out.println("2. Ver Produtos");
            System.out.println("3. Atualizar Produto");
            System.out.println("4. Remover Produto");
            System.out.println("5. Ver Nota do Produto e Vendedor");
            System.out.println("6. Ver Fotos de um Produto");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma operação: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    adicionarProdutoEstoque(estoque, cliente);
                    break;
                case 2:
                    verProdutos(estoque);
                    break;
                case 3:
                    atualizarProduto(estoque);
                    break;
                case 4:
                    removerProduto(estoque);
                    break;
                case 5:
                    verNotaProdutoVendedor(estoque);
                    break;
                case 6:
                    abrirFotosProduto(estoque);
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

    private void adicionarProdutoEstoque(ColecaoProdutos colecao, Cliente cliente) {
        System.out.println("\n--- ADICIONAR PRODUTO ---");

        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine();

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        System.out.print("Preço base: R$ ");
        double preco = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Quantidade em estoque: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();

        if (quantidade <= 0) {
            System.out.println(" Quantidade inválida.");
            return;
        }

        System.out.println("Escolha a categoria do produto (0 para deixar sem categoria):");
        Categoria_Menu_View_Textual catMenu = new Categoria_Menu_View_Textual(model, scanner);
        Categoria_if categoriaEscolhida = catMenu.escolherCategoriaNavegacao(model.getCategoriaRaiz(), "Deixar sem categoria");

        Produto novoProduto = new Produto(nome, descricao, preco);
        novoProduto.setVendedor(cliente);
        Produtos_Menu_View_Textual.coletarFotos(scanner, novoProduto);

        if (categoriaEscolhida != null) {
            novoProduto.setCategoria((Categoria) categoriaEscolhida);
            ((Categoria) categoriaEscolhida).addProduto(novoProduto);
        }

        colecao.adicionarProduto(novoProduto, quantidade);
        model.salvar();

        System.out.println("\n " + quantidade + "x '" + nome + "' adicionado ao seu Estoque com sucesso!");
    }

    private void verProdutos(ColecaoProdutos colecao) {
        System.out.println("\n--- PRODUTOS NO ESTOQUE ---");

        List<ItemProduto> itens = colecao.getItens();
        if (itens.isEmpty()) {
            System.out.println(" Nenhum produto encontrado no estoque.");
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
            System.out.println("    Nota: " + String.format("%.2f", p.getNota()) + " (" + p.getTotalVotos() + " voto(s))");
            Produtos_Menu_View_Textual.imprimirFotos(p);
        }
        System.out.println("-------------------------------------------------");
    }

    private void atualizarProduto(ColecaoProdutos colecao) {
        System.out.println("\n--- ATUALIZAR PRODUTO ---");

        List<Produto> produtos = colecao.getProdutos();
        if (produtos.isEmpty()) {
            System.out.println(" Nenhum produto disponível para atualizar.");
            return;
        }

        System.out.println("\nProdutos disponíveis:");
        for (int i = 0; i < produtos.size(); i++) {
            System.out.println((i + 1) + ". " + produtos.get(i).getNome());
        }

        System.out.print("\nEscolha o número do produto a atualizar (0 para cancelar): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha == 0) {
            System.out.println("Operação cancelada.");
            return;
        }
        if (escolha < 1 || escolha > produtos.size()) {
            System.out.println(" Erro: Número inválido.");
            return;
        }

        Produto produto = produtos.get(escolha - 1);

        System.out.println("\nO que deseja atualizar?");
        System.out.println("1. Nome");
        System.out.println("2. Descrição");
        System.out.println("3. Preço");
        System.out.println("4. Categoria");
        System.out.println("5. Adicionar Quantidade ao Estoque");
        System.out.println("6. Gerenciar Fotos");
        System.out.println("0. Cancelar");
        System.out.print("Escolha: ");

        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1:
                System.out.print("Novo nome: ");
                produto.setNome(scanner.nextLine());
                break;
            case 2:
                System.out.print("Nova descrição: ");
                produto.setDescricao(scanner.nextLine());
                break;
            case 3:
                System.out.print("Novo preço: R$ ");
                produto.setPrecoBase(scanner.nextDouble());
                scanner.nextLine();
                break;
            case 4:
                System.out.println("Escolha a nova categoria (0 para deixar sem categoria):");
                Categoria_Menu_View_Textual catMenu = new Categoria_Menu_View_Textual(model, scanner);
                Categoria_if novaCategoria = catMenu.escolherCategoriaNavegacao(model.getCategoriaRaiz(), "Deixar sem categoria");

                if (produto.getCategoria() != null) {
                    produto.getCategoria().removerProduto(produto);
                }
                if (novaCategoria != null) {
                    produto.setCategoria((Categoria) novaCategoria);
                    ((Categoria) novaCategoria).addProduto(produto);
                } else {
                    produto.setCategoria(null);
                }
                break;
            case 5:
                System.out.print("Quantidade a adicionar: ");
                int qtd = scanner.nextInt();
                scanner.nextLine();
                if (qtd > 0) {
                    colecao.adicionarProduto(produto, qtd);
                } else {
                    System.out.println(" Quantidade inválida.");
                    return;
                }
                break;
            case 6:
                Produtos_Menu_View_Textual.gerenciarFotosProduto(scanner, model, produto);
                return;
            case 0:
                System.out.println("Operação cancelada.");
                return;
            default:
                System.out.println(" Opção inválida.");
                return;
        }

        model.salvar();
        System.out.println(" Produto atualizado com sucesso!");
    }

    private void removerProduto(ColecaoProdutos colecao) {
        System.out.println("\n--- REMOVER PRODUTO ---");

        List<ItemProduto> itens = colecao.getItens();
        if (itens.isEmpty()) {
            System.out.println(" Nenhum produto disponível para remover.");
            return;
        }

        System.out.println("\nProdutos disponíveis:");
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

        System.out.print("Quantidade a remover (máximo " + maxQtd + ", 0 para cancelar): ");
        int qtdRemover = scanner.nextInt();
        scanner.nextLine();

        if (qtdRemover <= 0) {
            System.out.println("Operação cancelada.");
            return;
        }
        if (qtdRemover > maxQtd) {
            System.out.println(" Erro: Você só possui " + maxQtd + " unidades.");
            return;
        }

        colecao.removerProduto(produto, qtdRemover);
        model.salvar();
        System.out.println("\n " + qtdRemover + "x '" + nomeProduto + "' removido do estoque com sucesso!");
    }

    private void verNotaProdutoVendedor(ColecaoProdutos colecao) {
        Produtos_Menu_View_Textual.verNotaProdutoVendedor(scanner, colecao);
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
