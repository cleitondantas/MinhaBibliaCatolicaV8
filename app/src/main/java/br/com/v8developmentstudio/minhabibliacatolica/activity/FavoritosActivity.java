package br.com.v8developmentstudio.minhabibliacatolica.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import br.com.v8developmentstudio.minhabibliacatolica.MainActivity;
import br.com.v8developmentstudio.minhabibliacatolica.R;
import br.com.v8developmentstudio.minhabibliacatolica.adapter.CardAdapter;
import br.com.v8developmentstudio.minhabibliacatolica.dao.PersistenceDao;

public class FavoritosActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private PersistenceDao persistenceDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        persistenceDao = new PersistenceDao(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_favoritos);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CardAdapter(persistenceDao.recuperaListaFavoritos(persistenceDao.openDB()));
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FavoritosActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onBackPressed();
    }
}
