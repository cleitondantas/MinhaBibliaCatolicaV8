package br.com.v8developmentstudio.minhabibliacatolica.vo;

/**
 * Created by cleiton.dantas on 11/11/2015.
 */
public class Marcacoes {
    private int id;
    private int idLivro;
    private int idNumCap;
    private int idVersiculo;
    private Boolean sublinhado;
    private String marcacao_color;
    private Boolean favorito;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public int getIdNumCap() {
        return idNumCap;
    }

    public void setIdNumCap(int idNumCap) {
        this.idNumCap = idNumCap;
    }

    public int getIdVersiculo() {
        return idVersiculo;
    }

    public void setIdVersiculo(int idVersiculo) {
        this.idVersiculo = idVersiculo;
    }

    public Boolean getSublinhado() {
        return sublinhado;
    }

    public void setSublinhado(Boolean sublinhado) {
        this.sublinhado = sublinhado;
    }

    public String getMarcacao_color() {
        return marcacao_color;
    }

    public void setMarcacao_color(String marcacao_color) {
        this.marcacao_color = marcacao_color;
    }

    public Boolean getFavorito() {
        return favorito;
    }

    public void setFavorito(Boolean favorito) {
        this.favorito = favorito;
    }

}
