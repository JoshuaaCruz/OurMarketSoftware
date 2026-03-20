/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.categoria_produto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author joshua.cruz
 */
public class Categoria implements Categoria_if{
    
    private String nome, descricao;
    
    private final ArrayList<Categoria_if> subcategoriasList;
    private final ArrayList<Produto> produtos;
    
    private boolean hasChildren; //se não, não permitir certos métodos, não sei se necessário
    
    {
        subcategoriasList = new ArrayList<>();
        produtos = new ArrayList();
    }
    
    
    public Categoria(String nome) {
        this.nome = nome;
    }
    
    @Override
    public List<Categoria_if> getSubcategorias() {
        return Collections.unmodifiableList(subcategoriasList);
    }

    @Override
    public void addCategoria(Categoria categoria) {
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

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

 

    /**
     * @return the produtos
     */
    public ArrayList<Produto> getProdutos() {
        return produtos;
    }
    
    public void addProduto(Produto produto) {
        // TODO: deve verificar se a categoria jaa existe
        
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}
