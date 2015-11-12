package br.com.v8developmentstudio.minhabibliacatolica;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ExpandableListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import  android.view.animation.Animation;
import static android.view.GestureDetector.SimpleOnGestureListener;

import br.com.v8developmentstudio.minhabibliacatolica.adapter.MyAdapter;
import br.com.v8developmentstudio.minhabibliacatolica.adapter.MyExpandableAdapter;
import br.com.v8developmentstudio.minhabibliacatolica.adapter.MyRecyclerScroll;
import br.com.v8developmentstudio.minhabibliacatolica.dao.PersistenceDao;
import br.com.v8developmentstudio.minhabibliacatolica.vo.Capitulo;
import br.com.v8developmentstudio.minhabibliacatolica.vo.ItemData;
import br.com.v8developmentstudio.minhabibliacatolica.vo.Livro;
import br.com.v8developmentstudio.minhabibliacatolica.vo.Marcacoes;
import br.com.v8developmentstudio.minhabibliacatolica.vo.RelacLivroCap;
import br.com.v8developmentstudio.minhabibliacatolica.vo.Versiculo;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,RecyclerView.OnItemTouchListener, View.OnClickListener,  ActionMode.Callback {

    private ArrayList<Livro> livroArrayList = new ArrayList<>();


    private String tituloActionBar;
    private RecyclerView recyclerView;
    private ExpandableListView expandableList;
    private MyExpandableAdapter MyExpandAdapter;
    private DrawerLayout drawer;
    private ActionMode actionMode;
    private GestureDetectorCompat gestureDetector;
    private MyAdapter adapter =null;
    private PersistenceDao persistenceDao = new PersistenceDao(this);
    private  FloatingActionButton fab,fab2,fab3,fab4,fabMark,fabSublinhe,fabMarkColor1,fabMarkColor2,fabMarkColor3;
    private Animation animeFloating,animeFloating2;
    private int idLivro, idCapitulo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instancia as Variaveis
        fab =  (FloatingActionButton) findViewById(R.id.fab);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        fabMark = (FloatingActionButton) findViewById(R.id.fab_mark);
        fabSublinhe = (FloatingActionButton) findViewById(R.id.fab_sublinhe);
        fabMarkColor1 = (FloatingActionButton) findViewById(R.id.fab_color1);
        fabMarkColor2 = (FloatingActionButton) findViewById(R.id.fab_color2);
        fabMarkColor3 = (FloatingActionButton) findViewById(R.id.fab_color3);
        animeFloating = AnimationUtils.loadAnimation(this, R.anim.rotate);
        animeFloating2 = AnimationUtils.loadAnimation(this, R.anim.rotate2);
        final FloatingActionButton[] allFloatingActionButtons = new FloatingActionButton[]{fab2,fab3,fab4,fabMark,fabSublinhe,fabMarkColor1,fabMarkColor2,fabMarkColor3};
        hideAllFab(allFloatingActionButtons);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        expandableList = (ExpandableListView) findViewById(R.id.id_expandable_listView);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(this);
        gestureDetector = new GestureDetectorCompat(this, new RecyclerViewOnGestureListener());

        persistenceDao.getDeleteBase(PersistenceDao.DATABASE_NAME);
        if(!persistenceDao.getExiteBase(PersistenceDao.DATABASE_NAME)) {
            persistenceDao.openDB();
            persistenceDao.copiaBanco(PersistenceDao.DATABASE_NAME);
        }
        List<Livro> livroList = persistenceDao.getLivros(persistenceDao.openDB());
        livroArrayList.addAll(livroList);
        setGroupParents(livroArrayList);

        MyExpandAdapter = new MyExpandableAdapter(livroArrayList, this);
        MyExpandAdapter.setInflater((android.view.LayoutInflater) this.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE), this);
        expandableList.setDividerHeight(3);
        expandableList.setChildDivider(getResources().getDrawable(R.color.background_material_dark));
        expandableList.setAdapter(MyExpandAdapter);

        //Instancio a Primeira pagina
        this.idLivro = persistenceDao.getEstadoLivroPreferences();
        this.idCapitulo = persistenceDao.getEstadoCapituloPreferences();
        ItemData itemsData[] = recuperaVersiculosSelecionados(idLivro, idCapitulo);
        createView(recyclerView, itemsData);


        recyclerView.setOnScrollListener(new MyRecyclerScroll() {
            @Override
            public void show() {
                fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {
                fab.animate().translationY(fab.getHeight() + 200).setInterpolator(new AccelerateInterpolator(2)).start();
                hideAllFab(allFloatingActionButtons);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (View.GONE == fab2.getVisibility()) {
                    fab.startAnimation(animeFloating);
                    showAllFab(fab2, fab3, fab4);
                    fab2.animate().translationY(0).setInterpolator(new DecelerateInterpolator(5)).setStartDelay(50);
                    fab3.animate().translationY(0).setInterpolator(new DecelerateInterpolator(5)).setStartDelay(100);
                    fab4.animate().translationY(0).setInterpolator(new DecelerateInterpolator(5)).setStartDelay(150);

                } else {
                    fab.startAnimation(animeFloating2);

                    fab2.animate().translationY(fab.getHeight() + 100).setInterpolator(new AccelerateInterpolator(3)).start();
                    fab3.animate().translationY(fab2.getHeight() + 100).setInterpolator(new AccelerateInterpolator(3)).start();
                    fab4.animate().translationY(fab3.getHeight() + 100).setInterpolator(new AccelerateInterpolator(3)).start();
                    hideAllFab(fab2, fab3, fab4, fabMark, fabSublinhe, fabMarkColor1, fabMarkColor2, fabMarkColor3);

                }
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (View.GONE == fabMark.getVisibility()) {
                    showAllFab(fabMark, fabSublinhe, fabMarkColor1, fabMarkColor2, fabMarkColor3);
                    hideAllFab(fab3, fab4);
                } else {
                    showAllFab(fab3, fab4);
                    hideAllFab(fabMark, fabSublinhe, fabMarkColor1, fabMarkColor2, fabMarkColor3);
                }
            }
        });

        fabMarkColor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Marcacoes marcacoes;
                List<Marcacoes> marcacoesList = new ArrayList<>();
                for (Integer versiculo : adapter.getSelectedItems()) {
                    marcacoes = new Marcacoes();
                    marcacoes.setIdNumCap(idCapitulo);
                    marcacoes.setIdLivro(idLivro);
                    marcacoes.setIdVersiculo((versiculo+1));
                    marcacoes.setFavorito(false);
                    marcacoes.setMarcacao_color(Integer.toString(R.color.colorAccent));

                    marcacoesList.add(marcacoes);
                }

                persistenceDao.salvarMarcarcoes(persistenceDao.openDB(), idLivro, idCapitulo,marcacoesList);
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void myToggleSelection(int idx) {
        adapter.toggleSelection(idx);
        String title = getString(R.string.selected_count, adapter.getSelectedItemCount());
        setTitle(title);
        if(adapter.getSelectedItemCount()==0){
            setTitle(tituloActionBar);
        }

    }
    // Método que popula os dados
    public void setGroupParents(List<Livro> livroArrayList) {
        Capitulo capitulo;
        ArrayList<Capitulo> capitulos;
        for (int c = 0; c < livroArrayList.size(); c++) {
            capitulos = new ArrayList<>();
            for (int i = 0; i < livroArrayList.get(c).getQtdCapitulos(); i++) {
                capitulo = new Capitulo();
                capitulo.setId(i);
                capitulo.setTitulo("Capitulo " + (i + 1));
                capitulos.add(capitulo);

            }
            livroArrayList.get(c).setCapituloList(capitulos);
        }
    }

    // Cria a visualização da lista
    public void createView(RecyclerView recyclerView, ItemData[] itemsData) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(itemsData);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setClickable(true);
    }


    public ItemData[] recuperaVersiculosSelecionados(final int idLivroSelecionado,final int idCapituloSelecionado){
        this.idLivro = idLivroSelecionado;
        this.idCapitulo =idCapituloSelecionado;
        tituloActionBar=livroArrayList.get(idLivroSelecionado).getAbreviacao() + " " + ((livroArrayList.get(idLivroSelecionado)).getCapituloList()).get(idCapituloSelecionado).getTitulo();
        setTitle(tituloActionBar);
        List<Marcacoes>  marcacoesList=  persistenceDao.recuperaMarcacoes(persistenceDao.openDB(), idLivroSelecionado, idCapituloSelecionado);
        RelacLivroCap relacLivroCap = persistenceDao.getRelacLivroCap(persistenceDao.openDB(), idLivroSelecionado, idCapituloSelecionado);
        List<Versiculo>  versiculoList =  persistenceDao.getCapitulos(persistenceDao.openDB(), relacLivroCap.getNome_tabela());

        ItemData[] array = new ItemData[versiculoList.size()];

        for (int i=0;i < versiculoList.size();i++){
            array[i]= new ItemData();
            for (int x=0;x < marcacoesList.size();x++){
                if(marcacoesList.get(x).getIdVersiculo()==(i+1)){
                    array[i].setFavorito(marcacoesList.get(x).getFavorito());
                    array[i].setSublinhado(marcacoesList.get(x).getSublinhado());
                    array[i].setMarcacao_color(marcacoesList.get(x).getMarcacao_color());
                }
            }
            array[i].setTexto((i+1)+") "+ versiculoList.get(i).getTexto());
        }
        return array;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(adapter.getSelectedItemCount()!=0){
                adapter.clearSelections();
                setTitle(tituloActionBar);
                return;
            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // Métodos do Import
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }



    public DrawerLayout getDrawer() {
        return drawer;
    }

    public String getTituloActionBar() {
        return tituloActionBar;
    }

    public void setTituloActionBar(String tituloActionBar) {
        this.tituloActionBar = tituloActionBar;
    }
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.menu_cab_delete, menu);
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                List<Integer> selectedItemPositions = adapter.getSelectedItems();
                int currPos;
                for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
                    currPos = selectedItemPositions.get(i);

                }
                actionMode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        adapter.clearSelections();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.action_bar) {
            // fab click
            //addItemToList();
        } else if (v.getId() == R.id.container_list_item) {
            // item click
            int idx = recyclerView.getChildAdapterPosition(v);
            if (adapter.getSelectedItemCount() >0) {
                myToggleSelection(idx);
                return;
            }

        }

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    private class RecyclerViewOnGestureListener extends SimpleOnGestureListener{
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            view.setId(R.id.container_list_item);
            onClick(view);
            return super.onSingleTapConfirmed(e);
        }

        public void onLongPress(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (adapter.getSelectedItemCount()>0) {
                Toast.makeText(MainActivity.this,"Selecione com Click Simples", Toast.LENGTH_SHORT).show();
                return;
            }
            // Start the CAB using the ActionMode.Callback defined above

            int idx = recyclerView.getChildAdapterPosition(view);
            myToggleSelection(idx);
            super.onLongPress(e);
        }

    }


    //Métodos de Apoio

    /**
     *  Chama o Hide de todos dos FloatingActionButton Passados
     */
    private void hideAllFab(FloatingActionButton... fabsArray){
        for (int i=0;i<fabsArray.length;i++){
            fabsArray[i].hide();
        }
    }
    /**
     *
     */
    private void showAllFab(FloatingActionButton... fabsArray){
        for (int i=0;i<fabsArray.length;i++){
            fabsArray[i].show();
        }
    }
}
