package com.example.gestorarticles;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class productoNuevo extends AppCompatActivity {

    static private long IDProducto;
    static private GestorProductosDataSource bd;
    static private String desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detallarticle);

        bd = new GestorProductosDataSource(this);

        setTitle("PRODUCTO");

        TextView txt;
        EditText edt;

        txt = (TextView) findViewById(R.id.tvStock);
        edt = (EditText) findViewById(R.id.edtStock);

        final Intent EntradasSalidas = new Intent(this, EntradaSalida.class );

        Button btnEntrada = (Button) findViewById(R.id.btnEntrada);
        btnEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(EntradasSalidas);
            }
        });

        Button btnSalida = (Button) findViewById(R.id.btnSalida);
        btnSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(EntradasSalidas);
            }
        });

        Button btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                aceptar();
            }
        });


        Button  btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                eliminar();
            }
        });


        Button  btnCancel = (Button) findViewById(R.id.btnCancelar);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelar();
            }
        });

        // BUSCAMOS ID (SI ES -1 ES DE CREACION)
        IDProducto = this.getIntent().getExtras().getLong("id");

        if (IDProducto != -1) {
            // MOSTRAMOS DATOS SI NO ES UNA CREACION
            datosProducto();
        }
        else {
            // CREAMOS Y ESCONDEMOS EL FINALIZAR

            txt.setVisibility(View.GONE);
            edt.setVisibility(View.GONE);
            edt.setText("0");
            btnEntrada.setVisibility(View.GONE);
            btnSalida.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);

        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void datosProducto() {

        // FUNCION PARA BUSCAR 1 ARTICULO
        Cursor datos = bd.article(IDProducto);
        datos.moveToFirst();

        // PONEMOS LOS DETALES EN LA PANTALLA DE MODIFICAR
        EditText edt;
        float precio;

        edt = findViewById(R.id.edtCodiArticle);
        edt.setFocusable(false);
        edt.setText(datos.getString(datos.getColumnIndex(GestorProductosDataSource.GESTORARTICLES_CODIARTICLE)));

        edt = findViewById(R.id.edtDescripcio);

        desc = datos.getString(datos.getColumnIndex(GestorProductosDataSource.GESTORARTICLES_DESCRIPCION));
        setTitle("PRODUCTO: " + desc);

        edt.setText(desc);

        edt = findViewById(R.id.edtStock);
        edt.setFocusable(false);
        edt.setText(datos.getString(datos.getColumnIndex(GestorProductosDataSource.GESTORARTICLES_STOCK)));

        precio = datos.getFloat(datos.getColumnIndex(GestorProductosDataSource.GESTORARTICLES_PVP));
        
        edt = findViewById(R.id.edtPreu);
        edt.setText(String.valueOf(precio));
    }

    private void aceptar() {
        EditText edt;
        int count;

        // CODIGO DEBE SER UNICO
        edt = findViewById(R.id.edtCodiArticle);
        String codiArticle = edt.getText().toString();

        Cursor datos = bd.countArticle(codiArticle);
        boolean tengoDatos = datos.moveToFirst();

        if (tengoDatos) {
            count = Integer.parseInt(datos.getString(datos.getColumnIndex(GestorProductosDataSource.GESTORARTICLES_CODIARTICLE)));

            if (count > 0 && IDProducto == -1) {
                Toast.makeText(this, "UN PRODUCTO YA TIENE ESA ID", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (codiArticle.length() <= 0) {
            Toast.makeText(this, "FALTA EL CODIGO DEL PRODUCTO", Toast.LENGTH_SHORT).show();
            return;
        }

        //DESCRIPCION OBLIGATORIA
        edt = findViewById(R.id.edtDescripcio);
        String descripcio = edt.getText().toString();

        if (descripcio.length() <= 0) {
            Toast.makeText(this, "FALTA DESCRIPCION DEL PRODUCTO", Toast.LENGTH_SHORT).show();
            return;
        }

        edt = findViewById(R.id.edtStock);
        int stock = Integer.valueOf(edt.getText().toString());

        edt = findViewById(R.id.edtPreu);
        float preu = Float.valueOf(edt.getText().toString());

        // CREAMOS O GUARDAMOS?
        if (IDProducto == -1) {
            IDProducto = bd.insert(codiArticle, descripcio, preu, stock);
        }
        else {
            bd.update(IDProducto,descripcio,preu,stock);
        }

        Intent i = new Intent();
        i.putExtra("id", IDProducto);
        setResult(RESULT_OK, i);

        finish();
    }



    private void cancelar() {
        Intent i = new Intent();
        i.putExtra("id", IDProducto);
        setResult(RESULT_CANCELED, i);

        finish();
    }

    private void eliminar() {

        // CONFIRMAMOS ELIMINACION
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("ESTAS SEGURO DE ELIMINAR EL PRODUCTO: " + desc + " ?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                bd.delete(IDProducto);

                Intent i = new Intent();
                i.putExtra("id", -1);  // DEVOLVEMOS -1 PARA INFORMAR DE QUE ELIMINAMOS
                setResult(RESULT_OK, i);

                finish();
            }
        });

        builder.setNegativeButton("No", null);

        builder.show();

    }
}
