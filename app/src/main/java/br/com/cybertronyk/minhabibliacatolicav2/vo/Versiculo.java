package br.com.cybertronyk.minhabibliacatolicav2.vo;

/**
 * Created by cleiton.dantas on 23/10/2015.
 */
public class Versiculo {
        private int id;
        private String texto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return texto.toString();
    }
}
