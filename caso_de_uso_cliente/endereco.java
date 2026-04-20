
public class endereco {
   private String estado;
   private String cidade;
   private String logradouro;
   private int numero;
   private String complemento;

   public endereco(String estado, String cidade, String logradouro, int numero, String complemento) {
      this.estado = estado;
      this.cidade = cidade;
      this.logradouro = logradouro;
      this.numero = numero;
      this.complemento = complemento;
   }

   public String getEstado() {
      return this.estado;
   }

   public void setEstado(String var1) {
      this.estado = var1;
   }

   public String getCidade() {
      return this.cidade;
   }

   public void setCidade(String var1) {
      this.cidade = var1;
   }

   public String getLogradouro() {
      return this.logradouro;
   }

   public void setLogradouro(String var1) {
      this.logradouro = var1;
   }

   public int getNumero() {
      return this.numero;
   }

   public void setNumero(int var1) {
      this.numero = var1;
   }

   public String getComplemento() {
      return this.complemento;
   }

   public void setComplemento(String var1) {
      this.complemento = var1;
   }
}
