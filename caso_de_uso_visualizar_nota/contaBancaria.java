public class contaBancaria {

    private String numeroConta;
    private double saldoConta;
    private double fatura;
    private double limiteFatura;
    
    public String getNumero(){
        return numeroConta;
    }
    public double getsaldoConta(){
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
        fatura += valor;
    }
    
    
    
    public void depositar(double valor){
        if(valor >=0){
            saldoConta += valor;
        }
    }
    
    public boolean sacar(double valor){
        if(saldoConta >= valor){
            saldoConta -= valor;
            return true;
    }
          return false;
    }
    
    public boolean trasferir(double valor, contaBancaria contaDestino){
        if(this.sacar(valor)){
            contaDestino.depositar(valor);
            return true;
        }
        return false;
    }
    
}