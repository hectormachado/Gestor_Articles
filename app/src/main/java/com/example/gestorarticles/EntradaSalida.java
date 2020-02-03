package com.example.gestorarticles;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;

public class EntradaSalida extends AppCompatActivity {



    private long IDProducto;
    private GestorProductosDataSource bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada_salida);

        getSupportActionBar().hide();



    }
}
