package br.com.v8developmentstudio.minhabibliacatolica.bo;

import java.util.ArrayList;
import java.util.List;

import br.com.v8developmentstudio.minhabibliacatolica.dao.PersistenceDao;
import br.com.v8developmentstudio.minhabibliacatolica.vo.ItemData;
import br.com.v8developmentstudio.minhabibliacatolica.vo.Livro;
import br.com.v8developmentstudio.minhabibliacatolica.vo.Marcacoes;
import br.com.v8developmentstudio.minhabibliacatolica.vo.RelacLivroCap;
import br.com.v8developmentstudio.minhabibliacatolica.vo.Versiculo;

/**
 * Created by cleiton.dantas on 27/11/2015.
 */
public class MainBo {
    private String titulo;
    private List<Marcacoes> marcacoesList;
    private RelacLivroCap relacLivroCap;
    private List<Versiculo>  versiculoList;
    private Livro livro;
    private ItemData[] array;
    public ItemData[] recuperaVersiculosSelecionados(final PersistenceDao persistenceDao ,final int idLivroSelecionado,final int idCapituloSelecionado,final ArrayList<Livro> livroArrayList){
        livro = livroArrayList.get(idLivroSelecionado-1);//Dou -1 Pois eu passo o id mais ná verdade eu estou pegando em uma lista onde inicia com 0
        titulo = livro.getAbreviacao() + " " + livro.getCapituloList().get(idCapituloSelecionado-1).getTitulo();

        marcacoesList =  persistenceDao.recuperaMarcacoes(persistenceDao.openDB(), livro.getId(), idCapituloSelecionado);
        relacLivroCap = persistenceDao.getRelacLivroCap(persistenceDao.openDB(), livro.getId(), idCapituloSelecionado);
        versiculoList =  persistenceDao.getCapitulos(persistenceDao.openDB(), relacLivroCap.getNome_tabela());

        array = new ItemData[versiculoList.size()];

        for (int i=0;i < versiculoList.size();i++){
            array[i]= new ItemData();
            for (int x=0;x < marcacoesList.size();x++){
                if(marcacoesList.get(x).getIdVersiculo()==(i+1)){
                    array[i].setFavorito(marcacoesList.get(x).getFavorito());
                    array[i].setSublinhado(marcacoesList.get(x).getSublinhado());
                    array[i].setMarcacao_color(marcacoesList.get(x).getMarcacao_color());
                }
            }
            array[i].setTexto((i + 1) + ") " + versiculoList.get(i).getTexto());
        }
        return array;
    }
    public String getTitle(){
        return titulo;
    }
}
