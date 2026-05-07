
import java.util.List;

public class Cliente {
   private String nome;
   private String CPF;
   private Endereco endereco;
   private contaBancaria contaCliente;
   private ColecaoProdutos estoque, carrinho;
   private double nota;

   public Cliente() {
      this.estoque = new ColecaoProdutos();
      this.carrinho = new ColecaoProdutos();
   }

   public String getName() {
      return this.nome;
   }

   public String getCPF() {
      return this.CPF;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public void setCPF(String cpf) {
      this.CPF = cpf;
   }

   public Endereco getEndereco() {
      return this.endereco;
   }

   public contaBancaria getContaCliente() {
      return this.contaCliente;
   }

       public ColecaoProdutos getEstoque() {
        return this.estoque;
    }

    public ColecaoProdutos getCarrinho() {
        return this.carrinho;
    }

    public double getNota() {
        this.calculaNotaColecao(this.estoque);
        return this.nota;
    }

    public void calculaNotaColecao(ColecaoProdutos colecao) {
        if (colecao == null || colecao.getProdutos().isEmpty()) {
            this.nota = 0;
            return;
        }
        
        List<Produto> produtos = colecao.getProdutos();
        double somaNotas = 0;
        
        for (Produto produto : produtos) {
            somaNotas += produto.getNota();
        }
        
        this.nota = somaNotas / produtos.size();
    }

    public int getQuantidadeProdutosVendidos() {
      // TODO verificar algum jeito melhor de fazer essa funcao
      // throw new UnsupportedOperationException("Unimplemented method 'getQuantidadeProdutosVendidos'");
      return estoque.getQuantidadeProdutos();
    }

}
