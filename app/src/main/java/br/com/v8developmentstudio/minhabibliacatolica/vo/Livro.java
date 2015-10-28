package br.com.v8developmentstudio.minhabibliacatolica.vo;

import java.util.List;

/**
 * Created by cleiton.dantas on 23/10/2015.
 */
public class Livro {
    private int id;
    private String tituloLivro;
    private String abreviacao;
    private int qtdCapitulos;
    private List<Capitulo> capituloList;


    public int getQtdCapitulos() {
        return qtdCapitulos;
    }

    public void setQtdCapitulos(int qtdCapitulos) {
        this.qtdCapitulos = qtdCapitulos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTituloLivro() {
        return tituloLivro;
    }
    public void setTituloLivro(String tituloLivro) {
        this.tituloLivro = tituloLivro;
    }
    public List<Capitulo> getCapituloList() {
        return capituloList;
    }

    public void setCapituloList(List<Capitulo> capituloList) {
        this.capituloList = capituloList;
    }

    public String getAbreviacao() {
        return abreviacao;
    }

    public void setAbreviacao(String abreviacao) {
        this.abreviacao = abreviacao;
    }


    @Override
    public String toString() {
        return tituloLivro.toString();
    }
}
