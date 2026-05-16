import contaBancaria.ContaBancaria;
import contaBancaria.CartaoCredito;
import contaBancaria.Pix;
import contaBancaria.FormaDePagamento;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuCompra {

    private Scanner scanner;
    private Cliente consumidor;
    private List<Cliente> listaVendedores;

    public MenuCompra(Cliente consumidor, List<Cliente> listaVendedores, Scanner scanner) {
        this.consumidor = consumidor;
        this.listaVendedores = listaVendedores;
        this.scanner = scanner;
    }

    public void iniciar() {
        if (consumidor.getContaCliente() == null) {
            System.out.println("\n Erro: Você não possui conta cadastrada. Cadastre uma conta primeiro.");
            return;
        }

        List<Cliente> vendedoresComEstoque = new ArrayList<>();
        for (Cliente v : listaVendedores) {
            if (v != consumidor && !v.getEstoque().getProdutos().isEmpty()) {
                vendedoresComEstoque.add(v);
            }
        }

        if (vendedoresComEstoque.isEmpty()) {
            System.out.println("\n Nenhum vendedor com produtos disponíveis no momento.");
            return;
        }

        System.out.println("\n--- COMPRAR PRODUTOS ---");
        System.out.println("Vendedores disponíveis:");
        for (int i = 0; i < vendedoresComEstoque.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + vendedoresComEstoque.get(i).getName());
        }
        System.out.println("  0. Cancelar");
        System.out.print("Escolha o vendedor: ");
        int idxVendedor = scanner.nextInt() - 1;
        scanner.nextLine();

        if (idxVendedor < 0) { System.out.println("Cancelado."); return; }
        if (idxVendedor >= vendedoresComEstoque.size()) { System.out.println("Opção inválida."); return; }

        Cliente vendedor = vendedoresComEstoque.get(idxVendedor);
        List<Produto> estoque = vendedor.getEstoque().getProdutos();

        List<Produto> carrinho = new ArrayList<>();
        boolean continuarComprando = true;

        while (continuarComprando) {
            System.out.println("\n--- PRODUTOS DE " + vendedor.getName().toUpperCase() + " ---");
            for (int i = 0; i < estoque.size(); i++) {
                Produto p = estoque.get(i);
                System.out.printf("  %d. %-20s R$ %.2f%n", (i + 1), p.getNome(), p.getPrecoBase());
            }

            if (!carrinho.isEmpty()) {
                System.out.println("\n  Carrinho atual:");
                double subtotal = 0;
                for (Produto p : carrinho) {
                    System.out.printf("    - %-20s R$ %.2f%n", p.getNome(), p.getPrecoBase());
                    subtotal += p.getPrecoBase();
                }
                System.out.printf("  Subtotal: R$ %.2f%n", subtotal);
            }

            System.out.println("\n  0. Finalizar compra");
            System.out.print("Adicione um produto ao carrinho (número): ");
            int idxProduto = scanner.nextInt() - 1;
            scanner.nextLine();

            if (idxProduto < 0) {
                continuarComprando = false;
            } else if (idxProduto < estoque.size()) {
                carrinho.add(estoque.get(idxProduto));
                System.out.println("'" + estoque.get(idxProduto).getNome() + "' adicionado ao carrinho!");
            } else {
                System.out.println("Opção inválida.");
            }
        }

        if (carrinho.isEmpty()) {
            System.out.println("Carrinho vazio. Compra cancelada.");
            return;
        }

        double total = 0;
        System.out.println("\n--- RESUMO DA COMPRA ---");
        for (Produto p : carrinho) {
            System.out.printf("  - %-20s R$ %.2f%n", p.getNome(), p.getPrecoBase());
            total += p.getPrecoBase();
        }
        System.out.printf("  TOTAL: R$ %.2f%n", total);

        ContaBancaria contaConsumidor = consumidor.getContaCliente();
        List<String> formasDisponiveis = contaConsumidor.getFormasPagamento();

        if (formasDisponiveis.isEmpty()) {
            System.out.println("\n Você não possui formas de pagamento cadastradas.");
            return;
        }

        System.out.println("\nFormas de pagamento disponíveis:");
        for (int i = 0; i < formasDisponiveis.size(); i++) {
            System.out.print("  " + (i + 1) + ". " + formasDisponiveis.get(i));
            if (formasDisponiveis.get(i).equals("Pix")) {
                System.out.printf(" (Saldo: R$ %.2f)", contaConsumidor.getSaldoConta());
            } else if (formasDisponiveis.get(i).equals("Cartão de Crédito")) {
                System.out.printf(" (Disponível: R$ %.2f)", contaConsumidor.getLimiteFatura() - contaConsumidor.getFatura());
            }
            System.out.println();
        }
        System.out.println("  0. Cancelar compra");
        System.out.print("Escolha a forma de pagamento: ");
        int idxForma = scanner.nextInt() - 1;
        scanner.nextLine();

        if (idxForma < 0) { System.out.println("Compra cancelada."); return; }
        if (idxForma >= formasDisponiveis.size()) { System.out.println("Opção inválida."); return; }

        String formaSelecionada = formasDisponiveis.get(idxForma);

        ContaBancaria contaVendedor = vendedor.getContaCliente();
        if (contaVendedor == null) {
            System.out.println("\n Erro: vendedor não possui conta cadastrada. Compra cancelada.");
            return;
        }

        FormaDePagamento formaPagamento;
        if (formaSelecionada.equals("Pix")) {
            formaPagamento = new Pix();
        } else {
            formaPagamento = new CartaoCredito();
        }

        boolean sucesso = formaPagamento.pagar(contaConsumidor, contaVendedor, total);

        if (sucesso) {
            System.out.println("\n Compra realizada com sucesso via " + formaSelecionada + "!");
            System.out.printf("  Total pago: R$ %.2f para %s%n", total, vendedor.getName());
            if (formaSelecionada.equals("Pix")) {
                System.out.printf("  Saldo restante: R$ %.2f%n", contaConsumidor.getSaldoConta());
            } else {
                System.out.printf("  Fatura atual: R$ %.2f / Limite: R$ %.2f%n",
                    contaConsumidor.getFatura(), contaConsumidor.getLimiteFatura());
            }
        } else {
            System.out.println("\n Pagamento recusado!");
            if (formaSelecionada.equals("Pix")) {
                System.out.printf("  Saldo insuficiente (R$ %.2f). Total necessário: R$ %.2f%n",
                    contaConsumidor.getSaldoConta(), total);
            } else {
                System.out.printf("  Limite insuficiente (disponível R$ %.2f). Total necessário: R$ %.2f%n",
                    contaConsumidor.getLimiteFatura() - contaConsumidor.getFatura(), total);
            }
        }
    }
}
