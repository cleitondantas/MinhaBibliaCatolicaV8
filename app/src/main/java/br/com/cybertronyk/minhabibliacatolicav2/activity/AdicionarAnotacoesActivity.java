package br.com.cybertronyk.minhabibliacatolicav2.activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import br.com.cybertronyk.minhabibliacatolicav2.vo.Constantes;
import br.com.v8developmentstudio.minhabibliacatolica.R;

public class AdicionarAnotacoesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_anotacoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String anotacoes = getIntent().getStringExtra(Constantes.ANOTACOES);
        String tituloAnotacoes = getIntent().getStringExtra(Constantes.TITULO_ANOTACOES);

        TextView textViewAnotacoes = (TextView) findViewById(R.id.id_cv_texto_anotacoes);
        TextView textViewTitulo = (TextView) findViewById(R.id.id_cv_title_anotacoes);

        textViewTitulo.setText(tituloAnotacoes);
        textViewAnotacoes.setText(anotacoes);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdicionarAnotacoesActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onBackPressed();
    }

}
