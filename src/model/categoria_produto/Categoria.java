package model.categoria_produto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Categoria implements Categoria_if{
    
    private String nome, descricao;
    
    private final ArrayList<Categoria_if> subcategoriasList;
    private final ArrayList<Produto> produtos;
    
    private boolean hasChildren; //se não, não permitir certos métodos, não sei se necessário
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
        return Collections.unmodifiableList(subcategoriasList);
    }

    @Override
    public void addSubCategoria(Categoria categoria) {
        // TODO: deve verificar se a categoria jaa existe
        
        if(!this.subcategoriasList.contains(categoria)){
        subcategoriasList.add(categoria);
        }
        
    }
    
    public void removerSubcategoria(Categoria subcategoriaRemoved){
         if(this.subcategoriasList.contains(subcategoriaRemoved)){
        subcategoriasList.remove(subcategoriaRemoved);
        }
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
        
        if(!this.produtos.contains(produto)){
            produtos.add(produto);
        }
        
    }
    
    public void removerProduto(Produto produto){
        if(this.produtos.contains(produto)){
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
