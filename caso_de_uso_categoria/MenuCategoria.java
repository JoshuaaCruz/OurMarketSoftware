import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MenuCategoria {
    private Scanner scanner;
    private Cliente cliente;
    private Set<Categoria> categorias;
    private String nomeColecao;

    public MenuCategoria(Cliente cliente, Scanner scanner) {
        this.cliente = cliente;
        this.scanner = scanner;
        this.categorias = new HashSet<>();
    }

    public void iniciar() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n===========================");
            System.out.println("  ESCOLHA UMA OPÇÃO");
            System.out.println("===========================");
            System.out.println("1. Inserir");
            System.out.println("2. Consultar");
            System.out.println("3. Deletar");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            crud(opcao);
        }
    }

    private void crud(int opcao) {
        String nomeCategoria;
        switch (opcao) {
            case 1:
                System.out.print("Digite o nome da nova categoria: ");
                nomeCategoria = scanner.next();
                Categoria categoria = new Categoria(nomeCategoria);
                categorias.add(categoria);
                System.out.println("Categoria adicionada com sucesso!");
                break;
            case 2:
                System.out.println("Consultando Categorias...\n");
                categorias.forEach(cat -> System.out.printf("- %s%n", cat.getNome()));
                break;
            case 3:
                System.out.print("Insira o nome da categoria: ");
                nomeCategoria = scanner.next();
                boolean isRemovido = categorias.removeIf(x -> x.getNome().equals(nomeCategoria));
                if (isRemovido) {
                    System.out.println("Categoria(s) removidas com sucesso!");
                    break;
                }
                System.out.println("Esta categoria não existe");
            case 0:
                System.out.println("\nVoltando...");
                break;
            default:
                System.out.println("\nOpção inválida! Por favor, escolha 1, 2, 3 ou 0.");
                break;
        }
    }
}
