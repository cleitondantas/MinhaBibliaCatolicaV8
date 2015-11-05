package br.com.v8developmentstudio.minhabibliacatolica;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ExpandableListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import br.com.v8developmentstudio.minhabibliacatolica.adapter.MyAdapter;
import br.com.v8developmentstudio.minhabibliacatolica.adapter.MyExpandableAdapter;
import br.com.v8developmentstudio.minhabibliacatolica.adapter.MyRecyclerScroll;
import br.com.v8developmentstudio.minhabibliacatolica.dao.PersistenceDao;
import br.com.v8developmentstudio.minhabibliacatolica.vo.Capitulo;
import br.com.v8developmentstudio.minhabibliacatolica.vo.Livro;
import br.com.v8developmentstudio.minhabibliacatolica.vo.RelacLivroCap;
import br.com.v8developmentstudio.minhabibliacatolica.vo.Versiculo;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Livro> livroArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ExpandableListView expandableList;
    private  MyExpandableAdapter adapter;
    private DrawerLayout drawer;
    private PersistenceDao persistenceDao = new PersistenceDao(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instancia as Variaveis
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        final FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        final FloatingActionButton fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        final android.view.animation.Animation animeFloating = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.rotate);
        final android.view.animation.Animation animeFloating2 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.rotate2);
        fab2.hide();
        fab3.hide();
        fab4.hide();

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        expandableList = (ExpandableListView) findViewById(R.id.lvExp);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

       // persistenceDao.getDeleteBase(PersistenceDao.DATABASE_NAME);
        if(!persistenceDao.getExiteBase(PersistenceDao.DATABASE_NAME)) {
            persistenceDao.openDB();
            persistenceDao.copiaBanco(PersistenceDao.DATABASE_NAME);
        }
        List<Livro> livroList = persistenceDao.getLivros(persistenceDao.openDB());
        livroArrayList.addAll(livroList);
        setGroupParents(livroArrayList);

        adapter = new MyExpandableAdapter(livroArrayList, this);
        adapter.setInflater((android.view.LayoutInflater) this.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE), this);
        expandableList.setDividerHeight(3);
        expandableList.setChildDivider(getResources().getDrawable(R.color.background_material_dark));
        expandableList.setAdapter(adapter);

        //Instancio a Primeira pagina
        int idLivro = persistenceDao.getEstadoLivroPreferences();
        int idCapitulo = persistenceDao.getEstadoCapituloPreferences();
        String itemsData[] = recuperaVersiculosSelecionados(idLivro,idCapitulo);
        createView(recyclerView, itemsData);

        recyclerView.setOnScrollListener(new MyRecyclerScroll() {
            @Override
            public void show() {
                fab.startAnimation(animeFloating);
                fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {
                fab.startAnimation(animeFloating2);
                fab.animate().translationY(fab.getHeight() + 200).setInterpolator(new AccelerateInterpolator(2)).start();
                fab2.hide();
                fab3.hide();
                fab4.hide();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mGestureDetector.onTouchEvent(e)) {
                    Toast.makeText(MainActivity.this,"Versículo "+ (1+recyclerView.getChildPosition(child)), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (View.GONE == fab2.getVisibility()) {
                    fab.startAnimation(animeFloating);
                    fab2.show();
                    fab3.show();
                    fab4.show();
                } else {
                    fab.startAnimation(animeFloating2);
                    fab2.hide();
                    fab3.hide();
                    fab4.hide();
                }
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
    public void createView(RecyclerView recyclerView, String[] itemsData) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter mAdapter = new MyAdapter(itemsData);
        recyclerView.setAdapter(mAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setClickable(true);
    }


    public String[] recuperaVersiculosSelecionados(final int idLivroSelecionado,final int idCapituloSelecionado){

        setTitle(livroArrayList.get(idLivroSelecionado).getAbreviacao() + " " + ((livroArrayList.get(idLivroSelecionado)).getCapituloList()).get(idCapituloSelecionado).getTitulo());

        RelacLivroCap relacLivroCap = persistenceDao.getRelacLivroCap(persistenceDao.openDB(), idLivroSelecionado, idCapituloSelecionado);

        List<Versiculo>  versiculoList =  persistenceDao.getCapitulos(persistenceDao.openDB(), relacLivroCap.getNome_tabela());
        String[] array = new String[versiculoList.size()];
        for (int i=0;i < versiculoList.size();i++){
            array[i] = (i+1)+") "+ versiculoList.get(i).getTexto();
        }
        return array;

    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }



    public DrawerLayout getDrawer() {
        return drawer;
    }
}
