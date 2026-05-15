package contaBancaria;

public class ContaBancaria {

    private String numeroConta;
    private double saldoConta;
    private double fatura;
    private double limiteFatura;

    

    
    public String getNumero(){
        return numeroConta;
    }
    
    public void setNumero(String numeroConta) {
        this.numeroConta = numeroConta;
    }
    
    public double getSaldoConta(){
        return saldoConta;
    }
    
  /**
     * @return the limiteFatura
     */
    public double getLimiteFatura() {
        return limiteFatura;
    }

    /**
     * @param limiteFatura the limiteFatura to set
     */
    public void setLimiteFatura(double limiteFatura) {
        this.limiteFatura = limiteFatura;
    }

    /**
     * @return the fatura
     */


    public double getFatura() {
        return fatura;
    }
    
    public void incrementaFatura(double valor){
        if (valor > 0) {
            fatura += valor;
        }
    }
    
    public void pagarFatura(double valor){
        if(valor > 0 && fatura >= valor){
            fatura -= valor;
        }
    }
    
    
    
    public void depositar(double valor){
        if(valor > 0){
            saldoConta += valor;
        }
    }
    
    public boolean sacar(double valor){
        if(saldoConta >= valor && valor > 0){
            saldoConta -= valor;
            return true;
    }
          return false;
    }
    
    public boolean transferir(double valor, ContaBancaria contaDestino){
        if(contaDestino != null && this.sacar(valor)){
            contaDestino.depositar(valor);
            return true;
        }
        return false;
    }
    
}