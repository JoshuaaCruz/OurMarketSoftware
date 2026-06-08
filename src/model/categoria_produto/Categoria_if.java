package model.categoria_produto;

import java.util.ArrayList;
import java.util.List;


public interface Categoria_if {
    public void addSubCategoria(Categoria categoria);
    List<Categoria_if> getSubcategorias();
    String getNome();
    void setNome(String nome);
    String getDescricao();
    void setDescricao(String descricao);
    ArrayList<Produto> getProdutos();
    boolean isDestaqueAdmin();
    void setDestaqueAdmin(boolean destaque);
}
