/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author joshua.cruz
 */
public interface Categoria_if {
    public void addCategoria(Categoria categoria);
    List<Categoria_if> getSubcategorias();
    String getNome();
    void setNome(String nome);
    ArrayList<Produto> getProdutos();
}
