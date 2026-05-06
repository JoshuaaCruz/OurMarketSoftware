import java.util.Scanner;

public class main {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        Endereco end = null;
        Cliente cli = null;
        contaBancaria conta = null;

        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n===========================");
            System.out.println("       MENU PRINCIPAL      ");
            System.out.println("===========================");
            System.out.println("1. Cadastrar Cliente e Conta");
            System.out.println("2. Ver dados do Cliente");
            System.out.println("3. Consultar Saldo");
            System.out.println("4. Depositar");
            System.out.println("5. Sacar");
            System.out.println("6. Gerenciar Produtos");
            System.out.println("7. Gerenciar Categorias");
            System.out.println("0. Sair");
            System.out.print("Escolha uma operação: ");

            opcao = scanner.nextInt();
            
            scanner.nextLine(); 

            switch (opcao) {
                case 1:
                    System.out.println("\n--- 1. DADOS DO CLIENTE ---");
                    cli = new Cliente();
                    System.out.print("Nome completo: ");
                    cli.setNome(scanner.nextLine());
                    System.out.print("CPF: ");
                    cli.setCPF(scanner.nextLine());
                    
                    System.out.println("\n--- 2. ENDEREÇO ---");
                    System.out.print("Estado (UF): ");
                    String uf = scanner.nextLine();
                    System.out.print("Cidade: ");
                    String cidade = scanner.nextLine();
                    System.out.print("Rua: ");
                    String rua = scanner.nextLine();
                    System.out.print("Número: ");
                    int numero = scanner.nextInt();
                    scanner.nextLine(); 
                    System.out.print("Complemento (Apto, Casa, etc): ");
                    String comp = scanner.nextLine();
                    end = new Endereco(uf, cidade, rua, numero, comp);
                    
                    System.out.println("\n--- 3. DADOS DA CONTA ---");
                    conta = new contaBancaria();
                    System.out.print("Defina o limite inicial do cartão de crédito: R$ ");
                    conta.setLimiteFatura(scanner.nextDouble());
                    
                    System.out.println("\n Cadastro realizado com sucesso!");
                    break;
                    
                case 2:
                    if (cli == null) {
                        System.out.println("\n Erro: Nenhum cliente cadastrado. Escolha a opção 1 primeiro.");
                        break;
                    }
                    System.out.println("\n--- Dados do Cliente ---");
                    System.out.println("Cliente: " + cli.getName()); 
                    System.out.println("CPF: " + cli.getCPF());
                    break;
                    
                case 3:
                    if (conta == null) {
                        System.out.println("\n Erro: Nenhuma conta cadastrada. Escolha a opção 1 primeiro.");
                        break;
                    }
                    System.out.println("\n--- Saldo ---");
                    System.out.println("Saldo atual: R$ " + conta.getsaldoConta());
                    break;
                    
                case 4:
                    if (conta == null) {
                        System.out.println("\n Erro: Nenhuma conta cadastrada. Escolha a opção 1 primeiro.");
                        break;
                    }
                    System.out.print("\nDigite o valor para depósito: R$ ");
                    double valorDeposito = scanner.nextDouble();
                    conta.depositar(valorDeposito);
                    System.out.println("Depósito de R$ " + valorDeposito + " realizado com sucesso!");
                    break;
                    
                case 5:
                    if (conta == null) {
                        System.out.println("\n Erro: Nenhuma conta cadastrada. Escolha a opção 1 primeiro.");
                        break;
                    }
                    System.out.print("\nDigite o valor para saque: R$ ");
                    double valorSaque = scanner.nextDouble();
                    boolean sucessoSaque = conta.sacar(valorSaque);
                    if (sucessoSaque) {
                        System.out.println("Saque de R$ " + valorSaque + " realizado com sucesso!");
                    } else {
                        System.out.println("Saldo insuficiente para realizar o saque.");
                    }
                    break;
                    
                case 6:
                    if (cli == null) {
                        System.out.println("\n Erro: Nenhum cliente cadastrado. Escolha a opção 1 primeiro.");
                        break;
                    }
                    MenuProdutos menuProdutos = new MenuProdutos(cli, scanner);
                    menuProdutos.iniciar();
                    break;
                case 7:
                    if (cli == null) {
                        System.out.println("\n Erro: Nenhum cliente cadastrado. Escolha a opção 1 primeiro.");
                        break;
                    }
                    MenuCategoria menuCategoria = new MenuCategoria(cli, scanner);
                    menuCategoria.iniciar();
                    break;
                case 0:
                    System.out.println("\nEncerrando o sistema...");
                    break;
                    
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha um número do menu.");
                    break;
            }
        }
        
        scanner.close();
    }
}
