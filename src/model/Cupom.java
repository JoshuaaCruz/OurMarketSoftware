package model;

//o cupom de desconto em si ->  0.10 = 10% off
public class Cupom {

    public static final Cupom ANIVERSARIO = new Cupom("FELIZANIVERSARIO", 0.10, "Cupom de Aniversário (10% de desconto)");

    private final String codigo;
    private final double desconto;
    private final String descricao;

    public Cupom(String codigo, double desconto, String descricao) {
        this.codigo = codigo.toUpperCase();
        this.desconto = desconto;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public double getDesconto() {
        return desconto;
    }

    public String getDescricao() {
        return descricao;
    }

    /**
     * Aplica cupom em valor total
     * @param total the original price
     * @return the discounted price
     */
    public double aplicar(double total) {
        return total * (1.0 - desconto);
    }

    @Override
    public String toString() {
        return codigo + " — " + descricao;
    }
}
