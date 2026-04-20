
public class cliente {
   private String nome;
   private String CPF;
   private endereco endereco;
   private contaBancaria contaCliente;

   public cliente() {
   }

   public String getName() {
      return this.nome;
   }

   public String getCPF() {
      return this.CPF;
   }

   public void setNome(String var1) {
      this.nome = var1;
   }

   public void setCPF(String var1) {
      this.CPF = var1;
   }

   public endereco getEndereco() {
      return this.endereco;
   }

   public contaBancaria getContaCliente() {
      return this.contaCliente;
   }

}
