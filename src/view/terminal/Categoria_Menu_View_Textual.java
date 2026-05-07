package view.terminal;

import java.util.Scanner;
import model.LivreMercado;
import model.categoria_produto.Categoria;
import model.categoria_produto.Categoria_if;
import view.Categoria_Menu_View_if;

/**
 *
 */
public class Categoria_Menu_View_Textual implements Categoria_Menu_View_if {

    private final LivreMercado model;
    private final Scanner scanner;

    public Categoria_Menu_View_Textual(LivreMercado model) {
        this.model = model;
        this.scanner = new Scanner(System.in);
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
                case 0:
                    System.out.println("\nVoltando...");
                    break;
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha 1, 2, 3 ou 0.");
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
        System.out.println(indent + "- " + categoria.getNome());
        for (Categoria_if sub : categoria.getSubcategorias()) {
            listarRecursivo(sub, nivel + 1);
        }
    }

    private void adicionarSubcategoria() {
        System.out.println("\n--- ADICIONAR SUBCATEGORIA ---");
        listarCategorias();

        System.out.print("\nNome da categoria PAI onde adicionar: ");
        String nomePai = scanner.nextLine();

        Categoria_if pai = buscarCategoria(model.getCategoriaRaiz(), nomePai);
        if (pai == null) {
            System.out.println(" Categoria pai não encontrada.");
            return;
        }

        System.out.print("Nome da nova subcategoria: ");
        String novoNome = scanner.nextLine();

        ((Categoria) pai).addCategoria(new Categoria(novoNome));
        System.out.println(" Subcategoria '" + novoNome + "' adicionada em '" + nomePai + "' com sucesso!");
    }

    private void removerSubcategoria() {
        System.out.println("\n--- REMOVER SUBCATEGORIA ---");
        listarCategorias();

        System.out.print("\nNome da categoria PAI: ");
        String nomePai = scanner.nextLine();

        Categoria_if pai = buscarCategoria(model.getCategoriaRaiz(), nomePai);
        if (pai == null) {
            System.out.println(" Categoria pai não encontrada.");
            return;
        }

        System.out.print("Nome da subcategoria a remover: ");
        String nomeRemover = scanner.nextLine();

        Categoria_if alvo = null;
        for (Categoria_if sub : pai.getSubcategorias()) {
            if (sub.getNome().equals(nomeRemover)) {
                alvo = sub;
                break;
            }
        }

        if (alvo == null) {
            System.out.println(" Subcategoria não encontrada em '" + nomePai + "'.");
            return;
        }

        ((Categoria) pai).removerSubcategoria((Categoria) alvo);
        System.out.println(" Subcategoria '" + nomeRemover + "' removida com sucesso!");
    }

    private Categoria_if buscarCategoria(Categoria_if atual, String nome) {
        if (atual.getNome().equals(nome)) return atual;
        for (Categoria_if sub : atual.getSubcategorias()) {
            Categoria_if resultado = buscarCategoria(sub, nome);
            if (resultado != null) return resultado;
        }
        return null;
    }
}
