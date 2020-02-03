package com.example.gestorarticles;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static int ARTICLE_ADD = 1;
    private static int ARTICLE_UPDATE = 2;

    private GestorProductosDataSource bd;
    private adapterGestorProductos scTasks;
    private filtroProductos filtroActual;

    private ImageView entrada_salida, historico, eliminar;

    private static String[] from = new String[]{GestorProductosDataSource.GESTORARTICLES_CODIARTICLE, GestorProductosDataSource.GESTORARTICLES_DESCRIPCION, GestorProductosDataSource.GESTORARTICLES_STOCK};
    private static int[] to = new int[]{R.id.tvCodiArticle, R.id.tvDescripcio, R.id.tvNumUnitats};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("GESTOR ARTICULOS");
        bd = new GestorProductosDataSource(this);

        filtroActual = filtroProductos.FILTER_ALL;

        Cursor cursorArticles = bd.productos();

        scTasks = new adapterGestorProductos(this, R.layout.layout_article, cursorArticles, from, to, 1);

        ListView lv = findViewById(R.id.lvArticles);

        entrada_salida = (ImageView) findViewById(R.id.img_entrada_salida);
        historico = (ImageView) findViewById(R.id.img_historico);
        eliminar = (ImageView) findViewById(R.id.img_eliminar);

        entrada_salida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //entrada_salida();
            }
        });

        lv.setAdapter(scTasks);

        lv.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                    //MODIFICAR ID DEL ARTICULO
                    modificarProducto(id);
                }
            }
        );
    }

    private void filtro_lista_productos() {

        Cursor cursosProductos = null;

        // FILTRO, T0DO, CON STOCK O SIN STOCK
        switch (filtroActual) {
            case FILTER_ALL:
                cursosProductos = bd.productos();
                break;
            case FILTER_STOCK:
                cursosProductos = bd.productosConStock();
                break;
            case FILTER_SIN_STOCK:
                cursosProductos = bd.productosSinStosck();
                break;
        }

        // ENVIAR DATOS Y ACTUALIZAR
        scTasks.changeCursor(cursosProductos);
        scTasks.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnAdd:
                nuevoProducto();
                return true;
            case R.id.TODOS:
                filtroTodo();
                return true;
            case R.id.filtroConStock:
                filtroConStock();
                return true;
            case R.id.filtroSinStock:
                filtreNoStock();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ARTICLE_ADD) {
            if (resultCode == RESULT_OK) {
                //CARGAR TAREAS
                filtro_lista_productos();
            }
        }

        if (requestCode == ARTICLE_UPDATE) {
            if (resultCode == RESULT_OK) {
                filtro_lista_productos();
            }
        }
    }

    private void nuevoProducto() {
        //-1 PARA DIFERENCIAR CREACION Y MODIFICACION EN LA MISMA ACTIVITY
        Bundle bundle = new Bundle();
        bundle.putLong("id",-1);

        Intent i = new Intent(this, productoNuevo.class );
        i.putExtras(bundle);
        startActivityForResult(i,ARTICLE_ADD);
    }

    private void modificarProducto(long id) {
        //LLAMAMOS A LA ACTIVITY DEL PRODUCTO ENVIANDO -1 COMO ID
        Bundle bundle = new Bundle();
        bundle.putLong("id",id);

        Intent i = new Intent(this, productoNuevo.class );
        i.putExtras(bundle);
        startActivityForResult(i,ARTICLE_UPDATE);
    }

    public void eliminarProducto(final int _id) {
        // COMFIRMAMOS
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("ESTAS SEGURO DE QUERER ELIMINAR EL PRODUCTO?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                bd.delete(_id);
                filtro_lista_productos();
            }
        });

        builder.setNegativeButton("No", null);

        builder.show();
    }

    private void filtroTodo() {
        // TODOS LOS PRODUCTOS, CON O SIN STOCK
        Cursor cursorProductos = bd.productos();
        filtroActual = filtroProductos.FILTER_ALL;

        // REFRESCAMOS EL ADAPTER
        scTasks.changeCursor(cursorProductos);
        scTasks.notifyDataSetChanged();

        // VAMOS AL PRIMER REGISTRO
        ListView lv = (ListView) findViewById(R.id.lvArticles);
        lv.setSelection(0);

        Toast.makeText(this, "Todos los productos", Toast.LENGTH_SHORT).show();
    }

    private void filtroConStock() {
        // BUSCAMOS PRODUCTOS CON STOCK A 1 O +
        Cursor cursorProductos = bd.productosConStock();
        filtroActual = filtroProductos.FILTER_STOCK;

        // REFRESCAMOS ADAPTER
        scTasks.changeCursor(cursorProductos);
        scTasks.notifyDataSetChanged();

        // VAMOS AL PRIMER REGISTRO
        ListView lv = (ListView) findViewById(R.id.lvArticles);
        lv.setSelection(0);

        Toast.makeText(this, "PRODUCTOS CON STOCK", Toast.LENGTH_SHORT).show();
    }

    private void filtreNoStock() {
        // BUSCAMOS PRODUCTOS CON STOCK A 1 O +
        Cursor cursorProductos = bd.productosSinStosck();
        filtroActual = filtroProductos.FILTER_SIN_STOCK;

        // REFRESCAMOS ADAPTER
        scTasks.changeCursor(cursorProductos);
        scTasks.notifyDataSetChanged();

        // VAMOS AL PRIMER REGISTRO
        ListView lv = (ListView) findViewById(R.id.lvArticles);
        lv.setSelection(0);

        Toast.makeText(this, "PRODUCTOS SIN STOCK O STOCK NEGATIVO", Toast.LENGTH_SHORT).show();
    }
/*
    private void entrada_salida() {
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title);

        // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builder.create();
    }
 */
}
