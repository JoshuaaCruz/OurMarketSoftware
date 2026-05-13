public class Produto {
    private String nome;
    private String descricao;
    private double precoBase;
    private Cliente vendedor;
    private Categoria categoria;

    private double nota;
    
    public Produto(String nome, String descricao, double precoBase){
        this.nome=nome;
        this.descricao=descricao;
        this.precoBase=precoBase;
    }
    
    
    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @return the precoBase
     */
    public double getPrecoBase() {
        return precoBase;
    }

    /**
     * @return the vendedor
     */
    public Cliente getVendedor() {
        return vendedor;
    }

    /**
     * @return the categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * @param vendedor the vendedor to set
     */
    public void setVendedor(Cliente vendedor) {
        this.vendedor = vendedor;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPrecoBase(double precoBase) {
        this.precoBase = precoBase;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public double getNota() {
        return nota;
    }

}
