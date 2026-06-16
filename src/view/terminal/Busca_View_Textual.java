package view.terminal;

import java.util.List;
import java.util.Scanner;
import model.OurMarket;
import model.categoria_produto.Categoria_if;
import model.categoria_produto.Produto;

public class Busca_View_Textual {

    private final OurMarket model;
    private final Scanner scanner;

    public Busca_View_Textual(OurMarket model, Scanner scanner) {
        this.model = model;
        this.scanner = scanner;
    }

    public void iniciar() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n===========================");
            System.out.println("          BUSCAR");
            System.out.println("===========================");
            System.out.println("1. Buscar Produto por Nome");
            System.out.println("2. Buscar Categoria por Nome");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    buscarProduto();
                    break;
                case 2:
                    buscarCategoria();
                    break;
                case 0:
                    System.out.println("\nVoltando...");
                    break;
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha 1, 2 ou 0.");
                    break;
            }
        }
    }

    private void buscarProduto() {
        System.out.print("\nDigite o nome do produto que procura: ");
        String termo = scanner.nextLine().trim();
        if (termo.isEmpty()) {
            System.out.println(" Termo de busca não pode ser vazio.");
            return;
        }

        List<Produto> resultados = model.buscarProdutosPorNome(termo);
        if (resultados.isEmpty()) {
            System.out.println(" Nenhum produto encontrado com o nome '" + termo + "'.");
            return;
        }

        System.out.println("\n--- RESULTADOS DA BUSCA ---");
        for (int i = 0; i < resultados.size(); i++) {
            Produto p = resultados.get(i);
            System.out.printf("%d. %s (R$ %.2f)\n", i + 1, p.getNome(), p.getPrecoBase());
        }

        System.out.print("\nEscolha um produto para ver detalhes (0 para cancelar): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha == 0) return;
        if (escolha < 1 || escolha > resultados.size()) {
            System.out.println(" Erro: Número inválido.");
            return;
        }

        Produto selecionado = resultados.get(escolha - 1);
        exibirDetalhesProduto(selecionado);
    }

    private void exibirDetalhesProduto(Produto p) {
        System.out.println("\n--- DETALHES DO PRODUTO ---");
        System.out.println("Nome: " + p.getNome());
        System.out.println("Descrição: " + p.getDescricao());
        System.out.println("Preço: R$ " + String.format("%.2f", p.getPrecoBase()));
        if (p.getCategoria() != null) {
            System.out.println("Categoria: " + p.getCategoria().getNome());
        }
        System.out.println("Vendedor: " + p.getVendedor().getName());
        System.out.println("Nota do Vendedor: " + String.format("%.1f", p.getVendedor().getNota()) + " (" + p.getVendedor().getTotalProdutosAvaliados() + " produtos avaliados)");
        System.out.println("Nota do Produto: " + String.format("%.1f", p.getNota()) + " (" + p.getTotalVotos() + " avaliações)");
        System.out.println("Fotos: " + p.getFotos().size());

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\nO que deseja fazer?");
            System.out.println("1. Adicionar ao Carrinho");
            System.out.println("2. Adicionar à Lista de Desejos");
            System.out.println("3. Ver Fotos");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    adicionarAoCarrinho(p);
                    break;
                case 2:
                    adicionarAListaDesejos(p);
                    break;
                case 3:
                    Produtos_Menu_View_Textual.abrirFotos(p);
                    break;
                case 0:
                    return;
                default:
                    System.out.println(" Opção inválida.");
            }
        }
    }

    private void adicionarAoCarrinho(Produto p) {
        System.out.print("Quantidade: ");
        int qtd = scanner.nextInt();
        scanner.nextLine();

        try {
            model.adicionarProdutoAoCarrinho(p, qtd);
            System.out.println(" " + qtd + "x '" + p.getNome() + "' adicionado ao Carrinho com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(" Erro: " + e.getMessage());
        }
    }

    private void adicionarAListaDesejos(Produto p) {
        try {
            model.adicionarProdutoAListaDesejos(p);
            System.out.println(" '" + p.getNome() + "' adicionado à Lista de Desejos com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(" Erro: " + e.getMessage());
        }
    }

    private void buscarCategoria() {
        System.out.print("\nDigite o nome da categoria que procura: ");
        String termo = scanner.nextLine().trim();
        if (termo.isEmpty()) {
            System.out.println(" Termo de busca não pode ser vazio.");
            return;
        }

        List<Categoria_if> resultados = model.buscarCategoriasPorNome(termo);
        if (resultados.isEmpty()) {
            System.out.println(" Nenhuma categoria encontrada com o nome '" + termo + "'.");
            return;
        }

        System.out.println("\n--- RESULTADOS DA BUSCA ---");
        for (int i = 0; i < resultados.size(); i++) {
            Categoria_if c = resultados.get(i);
            System.out.printf("%d. %s\n", i + 1, c.getNome());
        }

        System.out.print("\nEscolha uma categoria para ver detalhes (0 para cancelar): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha == 0) return;
        if (escolha < 1 || escolha > resultados.size()) {
            System.out.println(" Erro: Número inválido.");
            return;
        }

        Categoria_if selecionada = resultados.get(escolha - 1);
        exibirDetalhesCategoria(selecionada);
    }

    private void exibirDetalhesCategoria(Categoria_if catEscolh) {
        int escolha = -1;
        while (escolha != 0) {
            System.out.println("\n--- DETALHES DA CATEGORIA ---");
            System.out.println("Nome: " + catEscolh.getNome());
            if (catEscolh.getDescricao() != null && !catEscolh.getDescricao().isEmpty()) {
                System.out.println("Descrição: " + catEscolh.getDescricao());
            }

            List<Categoria_if> subCat = catEscolh.getSubcategorias();
            List<Produto> prods = catEscolh.getProdutos();
            
            int contador = 1;

            if (!subCat.isEmpty()) {
                System.out.println("\nSubcategorias:");
                for (Categoria_if s : subCat) {
                    System.out.printf("%d. [Categoria] %s\n", contador++, s.getNome());
                }
            }

            if (prods != null && !prods.isEmpty()) {
                System.out.println("\nProdutos nesta categoria:");
                for (Produto p : prods) {
                    System.out.printf("%d. [Produto] %s (R$ %.2f)\n", contador++, p.getNome(), p.getPrecoBase());
                }
            } else {
                System.out.println("\nNenhum produto nesta categoria.");
            }
            
            if (subCat.isEmpty() && (prods == null || prods.isEmpty())) {
                System.out.println("\nPressione Enter para voltar.");
                scanner.nextLine();
                escolha = 0;
            } else {
                System.out.print("\nEscolha um item para explorar (0 para voltar): ");
                escolha = scanner.nextInt();
                scanner.nextLine();

                if (escolha > 0 && escolha < contador) {
                    if (escolha <= subCat.size()) {
                        exibirDetalhesCategoria(subCat.get(escolha - 1));
                    } else {
                        exibirDetalhesProduto(prods.get(escolha - 1 - subCat.size()));
                    }
                } else if (escolha != 0) {
                    System.out.println(" Erro: Número inválido.");
                }
            }
        }
    }
}
