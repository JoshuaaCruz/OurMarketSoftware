import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import contaBancaria.ContaBancaria;
import contaBancaria.CartaoCredito;
import contaBancaria.Pix;
import contaBancaria.FormaDePagamento;

public class main {

    static final int ROLE_CONSUMIDOR = 1;
    static final int ROLE_VENDEDOR   = 2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Cliente> listaClientes = new ArrayList<>();

        Cliente cli   = null;
        ContaBancaria conta = null;
        int role      = 0;

        int opcao = -1;

        while (opcao != 0) {

            System.out.println("\n===========================");
            System.out.println("       MENU PRINCIPAL      ");
            if (cli != null) {
                String papel = (role == ROLE_CONSUMIDOR) ? "Consumidor" : "Vendedor";
                System.out.println("   " + papel + ": " + cli.getName());
            }
            System.out.println("===========================");

            System.out.println("1.  Cadastrar Cliente e Conta");
            System.out.println("2.  Trocar usuário ativo");
            System.out.println("3.  Ver dados do Cliente");
            System.out.println("4.  Consultar Saldo");
            System.out.println("5.  Depositar");
            System.out.println("6.  Sacar");
            System.out.println("7.  Gerenciar Forma de Pagamento"); 
            System.out.println("8.  Ver / Pagar Fatura do Cartão");

            if (role == ROLE_CONSUMIDOR) {
                System.out.println("9. Comprar Produto(s)");        
            }
            if (role == ROLE_VENDEDOR) {
                System.out.println("9. Gerenciar Estoque (Produtos)");
            }

            System.out.println("0.  Sair");
            System.out.print("Escolha uma operação: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {

                case 1:
                    System.out.println("\n--- PAPEL ---");
                    System.out.println("1. Consumidor");
                    System.out.println("2. Vendedor");
                    System.out.print("Escolha seu papel: ");
                    role = scanner.nextInt();
                    scanner.nextLine();
                    if (role != ROLE_CONSUMIDOR && role != ROLE_VENDEDOR) {
                        System.out.println("Papel inválido. Usando Consumidor.");
                        role = ROLE_CONSUMIDOR;
                    }

                    System.out.println("\n--- DADOS DO USUÁRIO ---");
                    cli = new Cliente();
                    System.out.print("Nome completo: ");
                    cli.setNome(scanner.nextLine());
                    System.out.print("CPF: ");
                    cli.setCPF(scanner.nextLine());

                    System.out.println("\n--- ENDEREÇO ---");
                    System.out.print("Estado (UF): ");
                    String uf = scanner.nextLine();
                    System.out.print("Cidade: ");
                    String cidade = scanner.nextLine();
                    System.out.print("Rua: ");
                    String rua = scanner.nextLine();
                    System.out.print("Número: ");
                    int numero = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Complemento: ");
                    String comp = scanner.nextLine();
                    Endereco end = new Endereco(uf, cidade, rua, numero, comp);
                    cli.setEndereco(end);

                    System.out.println("\n--- DADOS DA CONTA ---");
                    conta = new ContaBancaria();
                    System.out.print("Número da conta: ");
                    conta.setNumero(scanner.nextLine());
                    System.out.print("Limite inicial do Cartão de Crédito: R$ ");
                    conta.setLimiteFatura(scanner.nextDouble());
                    scanner.nextLine();
                    cli.setContaCliente(conta);
                    listaClientes.add(cli);

                    System.out.println("\n Cadastro realizado com sucesso!");
                    System.out.println(" Formas de pagamento ativas: Pix e Cartão de Crédito.");
                    break;

                case 2:
                    if (listaClientes.isEmpty()) {
                        System.out.println("\n Nenhum cliente cadastrado.");
                        break;
                    }
                    System.out.println("\n--- Selecionar Usuário Ativo ---");
                    for (int i = 0; i < listaClientes.size(); i++) {
                        System.out.println("  " + (i + 1) + ". " + listaClientes.get(i).getName()
                            + " (Conta: " + listaClientes.get(i).getContaCliente().getNumero() + ")");
                    }
                    System.out.print("Escolha: ");
                    int idx = scanner.nextInt() - 1;
                    scanner.nextLine();
                    if (idx >= 0 && idx < listaClientes.size()) {
                        cli   = listaClientes.get(idx);
                        conta = cli.getContaCliente();
                        System.out.println("\n--- PAPEL ---");
                        System.out.println("1. Consumidor");
                        System.out.println("2. Vendedor");
                        System.out.print("Escolha seu papel: ");
                        role = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Usuário ativo alterado para " + cli.getName() + ".");
                    } else {
                        System.out.println("Opção inválida.");
                    }
                    break;

                case 3:
                    if (cli == null) { System.out.println("\n Cadastre um cliente primeiro (opção 1)."); break; }
                    System.out.println("\n--- Dados do Cliente ---");
                    System.out.println("Nome: " + cli.getName());
                    System.out.println("CPF:  " + cli.getCPF());
                    if (cli.getEndereco() != null) {
                        Endereco e = cli.getEndereco();
                        System.out.println("End.: " + e.getLogradouro() + ", " + e.getNumero()
                            + " " + e.getComplemento() + " - " + e.getCidade() + "/" + e.getEstado());
                    }
                    if (conta != null) System.out.println("Conta nº: " + conta.getNumero());
                    break;

                case 4:
                    if (conta == null) { System.out.println("\n Cadastre uma conta primeiro (opção 1)."); break; }
                    System.out.printf("%nSaldo atual: R$ %.2f%n", conta.getSaldoConta());
                    break;

                case 5:
                    if (conta == null) { System.out.println("\n Cadastre uma conta primeiro (opção 1)."); break; }
                    System.out.print("\nValor para depósito: R$ ");
                    double dep = scanner.nextDouble();
                    scanner.nextLine();
                    conta.depositar(dep);
                    System.out.printf("Depósito de R$ %.2f realizado! Saldo: R$ %.2f%n", dep, conta.getSaldoConta());
                    break;

                case 6:
                    if (conta == null) { System.out.println("\n Cadastre uma conta primeiro (opção 1)."); break; }
                    System.out.print("\nValor para saque: R$ ");
                    double saq = scanner.nextDouble();
                    scanner.nextLine();
                    if (conta.sacar(saq)) {
                        System.out.printf("Saque de R$ %.2f realizado! Saldo: R$ %.2f%n", saq, conta.getSaldoConta());
                    } else {
                        System.out.printf("Saldo insuficiente (R$ %.2f).%n", conta.getSaldoConta());
                    }
                    break;

                case 7:
                    if (conta == null) { System.out.println("\n Cadastre uma conta primeiro (opção 1)."); break; }
                    new MenuFormaPagamento(conta, scanner).iniciar();
                    break;

                case 8:
                    if (conta == null) { System.out.println("\n Cadastre uma conta primeiro (opção 1)."); break; }
                    if (!conta.temFormaPagamento("Cartão de Crédito")) {
                        System.out.println("\n Cartão de Crédito não está cadastrado. Acesse Gerenciar Forma de Pagamento.");
                        break;
                    }
                    System.out.println("\n--- Fatura do Cartão de Crédito ---");
                    System.out.printf("Fatura atual:      R$ %.2f%n", conta.getFatura());
                    System.out.printf("Limite total:      R$ %.2f%n", conta.getLimiteFatura());
                    System.out.printf("Limite disponível: R$ %.2f%n", conta.getLimiteFatura() - conta.getFatura());
                    if (conta.getFatura() > 0) {
                        System.out.print("\nDeseja pagar a fatura agora? (s/n): ");
                        if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                            System.out.print("Valor a pagar: R$ ");
                            double vf = scanner.nextDouble();
                            scanner.nextLine();
                            double antes = conta.getFatura();
                            conta.pagarFatura(vf);
                            if (conta.getFatura() < antes) {
                                System.out.printf("Pagamento de R$ %.2f realizado! Fatura restante: R$ %.2f%n", vf, conta.getFatura());
                            } else {
                                System.out.println("Valor inválido ou maior que a fatura.");
                            }
                        }
                    }
                    break;        

                case 9:
                    if (cli == null) { System.out.println("\n Cadastre um cliente primeiro (opção 1)."); break; }

                    if (role == ROLE_CONSUMIDOR) {
                        new MenuCompra(cli, listaClientes, scanner).iniciar();

                    } else if (role == ROLE_VENDEDOR) {
                        new MenuProdutos(cli, scanner).iniciar();

                    } else {
                        System.out.println("Selecione um papel primeiro (opção 1 ou 2).");
                    }
                    break;

                case 0:
                    System.out.println("\nEncerrando o sistema...");
                    break;

                default:
                    System.out.println("\nOpção inválida.");
                    break;
            }
        }

        scanner.close();
    }
}
