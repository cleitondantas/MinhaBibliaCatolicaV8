package br.com.cybertronyk.minhabibliacatolicav2.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;

import br.com.cybertronyk.minhabibliacatolicav2.MainActivity;
import br.com.v8developmentstudio.minhabibliacatolica.R;

public class AnotacoesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anotacoes);
        CardView  cv = (CardView) findViewById(R.id.id_cv);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AnotacoesActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onBackPressed();
    }
}
