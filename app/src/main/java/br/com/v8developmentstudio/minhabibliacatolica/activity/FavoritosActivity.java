package br.com.v8developmentstudio.minhabibliacatolica.activity;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import br.com.v8developmentstudio.minhabibliacatolica.MainActivity;
import br.com.v8developmentstudio.minhabibliacatolica.R;
import br.com.v8developmentstudio.minhabibliacatolica.adapter.CardAdapter;
import br.com.v8developmentstudio.minhabibliacatolica.dao.PersistenceDao;
import br.com.v8developmentstudio.minhabibliacatolica.vo.ItemFavorito;

public class FavoritosActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener, View.OnClickListener{
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private PersistenceDao persistenceDao;
    private GestureDetectorCompat gestureDetector;
    private List<ItemFavorito> itemsfavoritos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        persistenceDao = new PersistenceDao(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_favoritos);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView .addOnItemTouchListener(this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        itemsfavoritos = persistenceDao.recuperaListaFavoritos(persistenceDao.openDB());
        mAdapter = new CardAdapter(itemsfavoritos);
        mRecyclerView.setAdapter(mAdapter);
        gestureDetector = new GestureDetectorCompat(this,new RecyclerViewOnGestureListener());

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FavoritosActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
       ItemFavorito item = itemsfavoritos.get(v.getId());
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("idLivro",item.getIdLivro());
        intent.putExtra("idCapitulo",item.getIdCapitulo());
        intent.putExtra("idVersiculo", item.getIdVersiculo());
        startActivity(intent);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    private class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            int idx = mRecyclerView.getChildAdapterPosition(view);
            view.setId(idx);
            onClick(view);
            return super.onSingleTapConfirmed(e);
        }

        public void onLongPress(MotionEvent e) {
            View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            int idx = mRecyclerView.getChildAdapterPosition(view);
            super.onLongPress(e);
        }
    }

}
