package br.com.v8developmentstudio.minhabibliacatolica.vo;

/**
 * Created by cleiton.dantas on 19/10/2015.
 */
public class ItemData {
    private CharSequence texto;
    private Boolean sublinhado;
    private String marcacao_color;
    private Boolean favorito;

    public ItemData() {

    }

    public ItemData(String texto, Boolean sublinhado,String marcacao_color,Boolean favorito) {
        this.texto = texto;
        this.sublinhado = sublinhado;
        this.marcacao_color = marcacao_color;
        this.favorito = favorito;

    }

    public CharSequence getTexto() {
        return texto;
    }

    public void setTexto(CharSequence title) {
        this.texto = title;
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

    @Override
    public String toString() {
        return texto.toString();
    }
}
