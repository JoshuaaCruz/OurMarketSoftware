package view.terminal;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import model.OurMarket;
import model.categoria_produto.Categoria;
import model.categoria_produto.Categoria_if;
import view.Menu_if;

public class Categoria_Menu_View_Textual implements Menu_if {

    private final OurMarket model;
    private final Scanner scanner;

    public Categoria_Menu_View_Textual(OurMarket model) {
        this(model, new Scanner(System.in));
    }

    public Categoria_Menu_View_Textual(OurMarket model, Scanner scanner) {
        this.model = model;
        this.scanner = scanner;
    }

    @Override
    public void mostre() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n===========================");
            System.out.println("  GERENCIAR CATEGORIAS");
            System.out.println("===========================");
            System.out.println("1. Listar categorias");
            System.out.println("2. Adicionar subcategoria");
            System.out.println("3. Remover subcategoria");
            System.out.println("4. Alternar Destaque de Categoria");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    listarCategorias();
                    break;
                case 2:
                    adicionarSubcategoria();
                    break;
                case 3:
                    removerSubcategoria();
                    break;
                case 4:
                    alternarDestaque();
                    break;
                case 0:
                    System.out.println("\nVoltando...");
                    break;
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha 1, 2, 3, 4 ou 0.");
                    break;
            }
        }
    }

    private void listarCategorias() {
        System.out.println("\n--- CATEGORIAS ---");
        Categoria_if raiz = model.getCategoriaRaiz();
        listarRecursivo(raiz, 0);
    }

    private void listarRecursivo(Categoria_if categoria, int nivel) {
        String indent = "  ".repeat(nivel);
        String destaqueStr = categoria.isDestaqueAdmin() ? " [DESTAQUE]" : "";
        System.out.println(indent + "- " + categoria.getNome() + destaqueStr);
        for (Categoria_if sub : categoria.getSubcategorias()) {
            listarRecursivo(sub, nivel + 1);
        }
    }

    private void adicionarSubcategoria() {
        System.out.println("\n--- ADICIONAR SUBCATEGORIA ---");
        System.out.println("Escolha a categoria PAI onde adicionar:");
        Categoria_if pai = escolherCategoriaNavegacao(model.getCategoriaRaiz());

        if (pai == null) {
            System.out.println(" Operação cancelada.");
            return;
        }

        System.out.print("Nome da nova subcategoria: ");
        String novoNome = scanner.nextLine();
        
        System.out.print("Descrição da subcategoria: ");
        String novaDescricao = scanner.nextLine();

        Categoria novaCat = new Categoria(novoNome);
        novaCat.setDescricao(novaDescricao);
        
        ((Categoria) pai).addSubCategoria(novaCat);
        model.salvar();
        System.out.println(" Subcategoria '" + novoNome + "' adicionada em '" + pai.getNome() + "' com sucesso!");
    }

    private void removerSubcategoria() {
        System.out.println("\n--- REMOVER SUBCATEGORIA ---");
        System.out.println("Escolha a categoria PAI da qual deseja remover uma subcategoria:");
        Categoria_if pai = escolherCategoriaNavegacao(model.getCategoriaRaiz());

        if (pai == null) {
            System.out.println(" Operação cancelada.");
            return;
        }

        if (pai.getSubcategorias().isEmpty()) {
            System.out.println(" A categoria '" + pai.getNome() + "' não possui subcategorias.");
            return;
        }

        System.out.println("\nEscolha a subcategoria a remover:");
        Categoria_if alvo = escolherCategoriaNavegacao(pai);

        if (alvo == null) {
            System.out.println(" Operação cancelada.");
            return;
        }
        

        //Making sure alvo é filho de pai
        if (!pai.getSubcategorias().contains(alvo)) {
             System.out.println(" A categoria selecionada não é uma subcategoria direta de '" + pai.getNome() + "'.");
             return;
        }

        ((Categoria) pai).removerSubcategoria((Categoria) alvo);
        model.salvar();
        System.out.println(" Subcategoria '" + alvo.getNome() + "' removida com sucesso!");
    }

    private void alternarDestaque() {
        System.out.println("\n--- ALTERNAR DESTAQUE ---");
        System.out.println("Escolha a categoria para alternar o status de destaque:");
        Categoria_if cat = escolherCategoriaNavegacao(model.getCategoriaRaiz());

        if (cat == null) {
            System.out.println(" Operação cancelada.");
            return;
        }

        boolean novoStatus = !cat.isDestaqueAdmin();
        cat.setDestaqueAdmin(novoStatus);
        model.salvar();
        
        if (novoStatus) {
            System.out.println(" Categoria '" + cat.getNome() + "' agora está em DESTAQUE!");
        } else {
            System.out.println(" Categoria '" + cat.getNome() + "' não está mais em destaque.");
        }
    }

    /**
     * Helper universal para exibir categorias com números e retornar a escolhida.
     */
    public Categoria_if escolherCategoriaNavegacao(Categoria_if raiz) {
        List<Categoria_if> listaCategorias = new ArrayList<>();
        coletarTodasCategoriaseSubs(raiz, listaCategorias);

        for (int i = 0; i < listaCategorias.size(); i++) {
            Categoria_if c = listaCategorias.get(i);
            String destaque = c.isDestaqueAdmin() ? " [DESTAQUE]" : "";
            System.out.println("[" + (i + 1) + "] " + c.getNome() + destaque);
        }
        System.out.println("[0] Cancelar / Voltar");

        while (true) {
            System.out.print("Escolha o número da categoria: ");
            int escolha = scanner.nextInt();
            scanner.nextLine();

            if (escolha == 0) return null;
            if (escolha >= 1 && escolha <= listaCategorias.size()) {
                return listaCategorias.get(escolha - 1);
            }
            System.out.println("Número inválido. Tente novamente.");
        }
    }

    private void coletarTodasCategoriaseSubs(Categoria_if atual, List<Categoria_if> lista) {
        lista.add(atual);
        for (Categoria_if sub : atual.getSubcategorias()) {
            coletarTodasCategoriaseSubs(sub, lista);
        }
    }
}
