package model.categoria_produto;

import java.util.*;

public class Categoria implements Categoria_if {

    private String nome, descricao;

    private final ArrayList<Categoria_if> subcategoriasList;
    private final ArrayList<Produto> produtos;

    private boolean destaqueAdmin = false;

    {
        subcategoriasList = new ArrayList<>();
        produtos = new ArrayList<Produto>();
    }


    public Categoria(String nome) {
        this.nome = nome;
    }

    @Override
    public List<Categoria_if> getSubcategorias() {
        return subcategoriasList;
    }

    @Override
    public void addSubCategoria(Categoria categoria) {
        if (!this.subcategoriasList.contains(categoria)) {
            subcategoriasList.add(categoria);
        }

    }

    public boolean removerSubcategoria(Categoria subcategoriaRemoved) {
        Optional<List<Categoria_if>> optCategoria = this.localizarSubcategoriaList(subcategoriaRemoved);
        if (optCategoria.isEmpty()) return false;
        optCategoria.ifPresent(l -> l.remove(subcategoriaRemoved));
        return true;
    }

    private Optional<List<Categoria_if>> localizarSubcategoriaList(Categoria_if subcategoria) {

        List<Categoria_if> subcategorias = this.getSubcategorias();
        if (subcategorias.contains(subcategoria))
            return Optional.of(subcategorias);
        for (Categoria_if c : subcategorias) {
            Optional<List<Categoria_if>> cats = ((Categoria) c).localizarSubcategoriaList(subcategoria);
            if (cats.isPresent()) return cats;
        }
        return Optional.empty();
    }


    /**
     * @return the nome
     */
    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }

    @Override
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    /**
     * @return the produtos
     */
    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public void addProduto(Produto produto) {

        if (!this.produtos.contains(produto)) {
            produtos.add(produto);
        }

    }

    public void removerProduto(Produto produto) {
        if (this.produtos.contains(produto)) {
            produtos.remove(produto);
        }
    }


    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean isDestaqueAdmin() {
        return destaqueAdmin;
    }

    @Override
    public void setDestaqueAdmin(boolean destaqueAdmin) {
        this.destaqueAdmin = destaqueAdmin;
    }
}
