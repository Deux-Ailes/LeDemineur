package com.example.jeufun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Menu extends AppCompatActivity {
    private EditText pseudo;
    private EditText texte;
    private Button boutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.texte = findViewById(R.id.inPutDiff2);
        this.pseudo = findViewById(R.id.inPutDiff);
        this.boutton = findViewById(R.id.btn_Diff);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.boutton.setOnClickListener(v->{
            Parametre param = new Parametre(this.pseudo.getText().toString(), Integer.valueOf(this.texte.getText().toString()));
            Intent intent = new Intent(Menu.this,MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("PARAM",param);
            intent.putExtras(bundle);


        });

    }
}