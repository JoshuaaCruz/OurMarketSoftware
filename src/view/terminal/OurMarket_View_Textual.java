package view.terminal;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Cupom;
import model.OurMarket;
import model.categoria_produto.Categoria_if;
import model.categoria_produto.Produto;
import model.cliente.Cliente;
import model.cliente.Endereco;
import model.contaBancaria.ContaBancaria;
import view.OurMarket_View;
import view.Menu_if;
import model.cliente.Cliente.ClienteBuilder;

public class OurMarket_View_Textual implements OurMarket_View {

    private final OurMarket model;
    private final Scanner scanner;

    public OurMarket_View_Textual(OurMarket model) {
        this.model = model;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void mostre() {
        menuPrincipal();
    }

    private void menuPrincipal() {
        int opcao = -1;

        while (opcao != 0) {
            Cliente logado = model.getClienteLogado();
            System.out.println("\n===========================");
            System.out.println("       MENU PRINCIPAL      ");
            System.out.println("===========================");
            if (logado != null) {
                System.out.println(" Cliente: " + logado.getName());
            } else {
                System.out.println(" Cliente deslogado. Cadastre-se ou faça login!");
            }
            System.out.println("---------------------------");
            System.out.println("1. Cadastrar Cliente e Conta");
            System.out.println("2. Login");
            System.out.println("3. Logoff");
            System.out.println("4. Ver dados do Cliente");
            System.out.println("5. Consultar Saldo");
            System.out.println("6. Depositar");
            System.out.println("7. Sacar");
            System.out.println("8. Gerenciar Forma de Pagamento");
            System.out.println("9. Ver / Pagar Fatura do Cartão");
            System.out.println("10. Gerenciar Produtos");
            System.out.println("11. Gerenciar Categorias");
            System.out.println("12. Gerenciar Endereços");
            System.out.println("13. Ver Produtos em Destaque (Mais Vendidos)");
            System.out.println("14. Buscar Produto / Categoria");
            System.out.println("0. Sair");
            System.out.print("Escolha uma operação: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarClienteEConta();
                    break;
                case 2:
                    fazerLogin();
                    break;
                case 3:
                    fazerLogoff();
                    break;
                case 4:
                    verDadosCliente();
                    break;
                case 5:
                    consultarSaldo();
                    break;
                case 6:
                    depositar();
                    break;
                case 7:
                    sacar();
                    break;
                case 8:
                    gerenciarFormaPagamento();
                    break;
                case 9:
                    verPagarFaturaCartao();
                    break;
                case 10:
                    gerenciarProdutos();
                    break;
                case 11:
                    gerenciarCategorias();
                    break;
                case 12:
                    gerenciarEnderecos();
                    break;
                case 13:
                    verProdutosEmDestaque();
                    break;
                case 14:
                    new Busca_View_Textual(model, scanner).iniciar();
                    break;
                case 0:
                    System.out.println("\nEncerrando o sistema...");
                    break;
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha um número do menu.");
                    break;
            }
        }
    }

    private void cadastrarClienteEConta() {
        if (model.getClienteLogado() != null) {
            System.out.println("\n Você já está logado. Faça logoff antes de cadastrar uma nova conta.");
            return;
        }

        System.out.println("\n--- 1. DADOS DO CLIENTE ---");
        ClienteBuilder clienteBuilder = Cliente.builder();
        String nome = "";
        while (nome.isEmpty()) {
            System.out.print("Nome completo: ");
            nome = scanner.nextLine().trim();
            if (nome.isEmpty()) {
                System.out.println(" Nome não pode ser vazio. Tente novamente.");
                continue;
            }
            if (nome.matches("default[\\d]*")) {
                Cliente cliente = clienteDefault(nome);
                boolean clienteValido = !(model.loginExiste(cliente.getLogin()) ||
                        model.cpfExiste(cliente.getCPF()));
                if (!clienteValido) {
                    System.out.println("Conta default inválida. Tente novamente");
                    nome = "";
                    continue;
                }
                model.adicionarCliente(cliente);
                model.login(cliente.getLogin(), cliente.getSenha());
                model.salvar();

                System.out.println("\n Cadastro realizado com sucesso! Você já está logado como '" + cliente.getName() + "'.");

                verificarAniversario(cliente);
                return;
            }
        }

        String cpf = "";
        while (cpf.isEmpty()) {
            System.out.print("CPF: ");
            cpf = scanner.nextLine().trim();
            if (cpf.isEmpty()) {
                System.out.println(" CPF não pode ser vazio. Tente novamente.");
            } else if (model.cpfExiste(cpf)) {
                System.out.println(" Este CPF já está cadastrado. Tente outro.");
                cpf = "";
            }
        }

        System.out.print("Data de nascimento (dia/mês/ano [dd/MM/yyyy], Enter para pular): ");
        String dataNasc = scanner.nextLine().trim();
        LocalDate dataDeNasc = null;
        if (!dataNasc.isEmpty()) {
            try {
                dataDeNasc = LocalDate.parse(dataNasc, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println(" Formato inválido, data de nascimento não salva.");
            }
        }

        System.out.println("\n--- 2. CREDENCIAIS DE ACESSO ---");
        String login = "";
        while (login.isEmpty()) {
            System.out.print("Login: ");
            login = scanner.nextLine().trim();
            if (login.isEmpty()) {
                System.out.println(" Login não pode ser vazio. Tente novamente.");
            } else if (model.loginExiste(login)) {
                System.out.println(" Este login já está em uso. Escolha outro.");
                login = "";
            }
        }
        String senha = "";
        while (senha.isEmpty()) {
            System.out.print("Senha: ");
            senha = scanner.nextLine().trim();
            if (senha.isEmpty()) {
                System.out.println(" Senha não pode ser vazia. Tente novamente.");
            }
        }

        Cliente cliente = clienteBuilder
                .nome(nome)
                .cpf(cpf)
                .dataNascimento(dataDeNasc)
                .login(login)
                .senha(senha)
                .build();

        System.out.println("\n--- 3. ENDEREÇO ---");
        Endereco_Menu_View_Textual endMenu = new Endereco_Menu_View_Textual(cliente, scanner);
        cliente.addEndereco(endMenu.coletarCampos());

        System.out.println("\n--- 4. DADOS DA CONTA ---");
        ContaBancaria conta = new ContaBancaria();
        System.out.print("Número da conta: ");
        conta.setNumero(scanner.nextLine());
        double limite = -1;
        while (limite < 0) {
            System.out.print("Defina o limite inicial do cartão de crédito: R$ ");
            limite = scanner.nextDouble();
            scanner.nextLine();
            if (limite < 0) {
                System.out.println(" O limite não pode ser negativo.");
            }
        }
        conta.setLimiteFatura(limite);
        cliente.setContaCliente(conta);

        model.adicionarCliente(cliente);
        model.login(login, senha);
        model.salvar();

        System.out.println("\n Cadastro realizado com sucesso! Você já está logado como '" + cliente.getName() + "'.");

        verificarAniversario(cliente);
    }

    private void verDadosCliente() {
        Cliente cliente = getClienteLogado();
        if (cliente == null) {
            return;
        }

        System.out.println("\n--- Dados do Cliente ---");
        System.out.println("Cliente: " + cliente.getName());
        System.out.println("CPF: " + cliente.getCPF());

        List<Endereco> enderecos = cliente.getEnderecos();
        if (!enderecos.isEmpty()) {
            System.out.println("Endereços:");
            for (int i = 0; i < enderecos.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + enderecos.get(i));
            }
        }

        if (cliente.getContaCliente() != null) {
            System.out.println("Conta nº: " + cliente.getContaCliente().getNumero());
        }
    }

    private void consultarSaldo() {
        ContaBancaria conta = getContaCadastrada();
        if (conta == null) {
            return;
        }

        System.out.println("\n--- Saldo ---");
        System.out.println("Saldo atual: R$ " + conta.getSaldoConta());
    }

    private void depositar() {
        ContaBancaria conta = getContaCadastrada();
        if (conta == null) {
            return;
        }

        System.out.print("\nDigite o valor para depósito: R$ ");
        double valorDeposito = scanner.nextDouble();
        scanner.nextLine();
        
        if (conta.depositar(valorDeposito)) {
            model.salvar();
            System.out.println("Depósito de R$ " + String.format("%.2f", valorDeposito) + " realizado com sucesso!");
        } else {
            System.out.println("Valor de depósito inválido.");
        }
    }

    private void sacar() {
        ContaBancaria conta = getContaCadastrada();
        if (conta == null) {
            return;
        }

        System.out.print("\nDigite o valor para saque: R$ ");
        double valorSaque = scanner.nextDouble();
        scanner.nextLine();
        boolean sucessoSaque = conta.sacar(valorSaque);
        if (sucessoSaque) {
            model.salvar();
            System.out.println("Saque de R$ " + valorSaque + " realizado com sucesso!");
        } else {
            System.out.println("Erro no saque.Saldo insuficiente ou valor inválido.");
        }
    }

    private void gerenciarFormaPagamento() {
        ContaBancaria conta = getContaCadastrada();
        if (conta == null) {
            return;
        }

        Menu_if menuFormaPagamento = new MenuFormaPagamento_View_Textual(conta, scanner);
        menuFormaPagamento.mostre();
        model.salvar();
    }

    private void verPagarFaturaCartao() {
        ContaBancaria conta = getContaCadastrada();
        if (conta == null) {
            return;
        }

        if (!conta.temFormaPagamento("Cartão de Crédito")) {
            System.out.println("\n Cartão de Crédito não está cadastrado. Acesse Gerenciar Forma de Pagamento.");
            return;
        }

        System.out.println("\n--- Fatura do Cartão de Crédito ---");
        System.out.printf("Fatura atual:      R$ %.2f%n", conta.getFatura());
        System.out.printf("Limite total:      R$ %.2f%n", conta.getLimiteFatura());
        System.out.printf("Limite disponível: R$ %.2f%n", conta.getLimiteFatura() - conta.getFatura());

        if (conta.getFatura() <= 0) {
            return;
        }

        System.out.print("\nDeseja pagar a fatura agora? (s/n): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("s")) {
            return;
        }

        System.out.print("Valor a pagar: R$ ");
        double valor = scanner.nextDouble();
        scanner.nextLine();

        if (conta.pagarFatura(valor)) {
            model.salvar();
            System.out.printf("Pagamento de R$ %.2f realizado! Fatura restante: R$ %.2f%n", valor, conta.getFatura());
        } else {
            System.out.println("Erro: Valor inválido, maior que a fatura ou saldo em conta insuficiente.");
        }
    }

    private void gerenciarProdutos() {
        Cliente cliente = getClienteLogado();
        if (cliente == null) {
            return;
        }

        Menu_if menuProdutos = new Produtos_Menu_View_Textual(model, scanner);
        menuProdutos.mostre();
        model.salvar();
    }

    private void gerenciarCategorias() {
        System.out.println("\n [Acesso Restrito] O menu de categorias é exclusivo para Administradores.");
        System.out.print("Digite a senha de administrador para continuar (ou qualquer outra coisa para cancelar): ");

        String senha = scanner.nextLine();

        if (senha.equals("123")) {
            System.out.println("\n Acesso concedido!");
            Menu_if menuCategorias = new Categoria_Menu_View_Textual(model, scanner);
            menuCategorias.mostre();
            model.salvar();
        } else {
            System.out.println("\n Senha incorreta. Acesso negado, voltando ao menu principal...");
        }
    }
    
    private void verProdutosEmDestaque() {
        System.out.println("\n--- PRODUTOS EM DESTAQUE (MAIS VENDIDOS) ---");
        List<Categoria_if> categoriasDestaque = new ArrayList<>();
        coletarCategoriasDestaque(model.getCategoriaRaiz(), categoriasDestaque);

        if (categoriasDestaque.isEmpty()) {
            System.out.println(" Nenhuma categoria em destaque no momento.");
            return;
        }

        for (Categoria_if cat : categoriasDestaque) {
            System.out.println("\n >> Categoria: " + cat.getNome() + " <<");
            
            List<Produto> produtos = new ArrayList<>();
            coletarProdutosCategoria(cat, produtos);
            if (produtos.isEmpty()) {
                System.out.println("   (Sem produtos nesta categoria)");
                continue;
            }

            // ordenar maior -> menor em vendas
            produtos.sort((p1, p2) -> Integer.compare(p2.getVendas(), p1.getVendas()));

            int limite = Math.min(5, produtos.size()); // Mostrar Top 5
            for (int i = 0; i < limite; i++) {
                Produto p = produtos.get(i);
                System.out.printf("   %d. %s (Vendas: %d) - R$ %.2f\n", i + 1, p.getNome(), p.getVendas(), p.getPrecoBase());
            }
        }
    }

    private void coletarProdutosCategoria(Categoria_if cat, List<Produto> produtos) {
        produtos.addAll(cat.getProdutos());
        for (Categoria_if sub : cat.getSubcategorias()) {
            coletarProdutosCategoria(sub, produtos);
        }
    }

    private void coletarCategoriasDestaque(Categoria_if cat, List<Categoria_if> lista) {
        if (cat.isDestaqueAdmin()) {
            lista.add(cat);
        }
        for (Categoria_if sub : cat.getSubcategorias()) {
            coletarCategoriasDestaque(sub, lista);
        }
    }

    private void gerenciarEnderecos() {
        Cliente cliente = getClienteLogado();
        if (cliente == null){ 
            return;
        }
        Menu_if menuEnderecos = new Endereco_Menu_View_Textual(cliente, scanner);
        menuEnderecos.mostre();
        model.salvar();
    }

    private void verificarAniversario(Cliente cliente) {
        if (cliente.getDataNascimento() == null) return;
        if (MonthDay.from(cliente.getDataNascimento()).equals(MonthDay.now())) {
            System.out.println("\n Feliz Aniversário, " + cliente.getName() + "!");
            System.out.println(" Como presente, use o cupom abaixo na sua próxima compra:");
            System.out.println(" Código: " + Cupom.ANIVERSARIO.getCodigo());
            System.out.println(" " + Cupom.ANIVERSARIO.getDescricao());
        }
    }

    private void fazerLogin() {
        if (model.getClienteLogado() != null) {
            System.out.println("\n Já existe uma sessão ativa para '" + model.getClienteLogado().getName() + "'. Faça logoff primeiro.");
            return;
        }

        System.out.println("\n--- LOGIN ---");
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Cliente cliente = model.login(login, senha);
        if (cliente != null) {
            System.out.println("\n Bem-vindo(a), " + cliente.getName() + "!");
            verificarAniversario(cliente);
        } else {
            System.out.println("\n Login ou senha incorretos.");
        }
    }

    private void fazerLogoff() {
        Cliente logado = model.getClienteLogado();
        if (logado == null) {
            System.out.println("\n Nenhuma sessão ativa.");
            return;
        }
        model.logoff();
        System.out.println("\n Sessão encerrada. Até logo, " + logado.getName() + "!");
    }

    private Cliente getClienteLogado() {
        Cliente cliente = model.getClienteLogado();
        if (cliente == null) {
            System.out.println("\n Erro: Nenhum cliente logado. Escolha a opção 1 ou 2 primeiro.");
        }
        return cliente;
    }

    private ContaBancaria getContaCadastrada() {
        Cliente cliente = getClienteLogado();
        if (cliente == null) {
            return null;
        }

        ContaBancaria conta = cliente.getContaCliente();
        if (conta == null) {
            System.out.println("\n Erro: Nenhuma conta cadastrada. Escolha a opção 1 primeiro.");
        }
        return conta;
    }

    private Cliente clienteDefault(String nome) {
        ClienteBuilder clienteBuilder = Cliente.builder();
        ContaBancaria conta = new ContaBancaria();
        conta.setLimiteFatura(Double.MAX_VALUE);
        conta.setNumero("12333");
        clienteBuilder.nome(nome)
                .cpf("12345" + nome.replace("default", ""))
                .dataNascimento(LocalDate.MIN)
                .login(nome)
                .senha(nome)
                .endereco(new Endereco("SC", "Brusque",
                        "Rua", 51, "Casa"))
                .contaCliente(conta);
        return clienteBuilder.build();
    }
}

