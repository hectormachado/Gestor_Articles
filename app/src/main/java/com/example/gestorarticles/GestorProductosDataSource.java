package com.example.gestorarticles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GestorProductosDataSource {

    public static final String table_GESTORARTICLES = "gestorarticles";
    public static final String GESTORARTICLES_ID = "_id";
    public static final String GESTORARTICLES_CODIARTICLE = "codiarticle";
    public static final String GESTORARTICLES_DESCRIPCION = "descripcio";
    public static final String GESTORARTICLES_PVP = "pvp";
    public static final String GESTORARTICLES_STOCK = "stock";

    private GestorProductosHelper dbHelper;
    private SQLiteDatabase dbW, dbR;

    // CONSTRUCTOR
    public GestorProductosDataSource(Context ctx) {
        // DB Connection
        dbHelper = new GestorProductosHelper(ctx);

      //CREAMOS LA BD DE ESCRITURA Y LECTURA POR SEPARADO
        open();
    }

    private void open() {
        dbW = dbHelper.getWritableDatabase();
        dbR = dbHelper.getReadableDatabase();
    }

    // DESTRUCTOR
    protected void finalize () {
       //CERRAMOS
        dbW.close();
        dbR.close();
    }

    // CONSULTES
    public Cursor productos() {
        /* Consulta de todos los productos */
        String query = "SELECT * FROM gestorarticles";
        return dbR.rawQuery(query, null);
    }

    public Cursor productosSinStosck() {
        /* Consulta de todos los productos sin Stock */
        String query = "SELECT * FROM gestorarticles WHERE stock <= ?";
        String[] args = new String[] {"0"};
        return dbR.rawQuery(query, args);
    }

    public Cursor productosConStock() {
        /* Consulta de todos los productos con Stock */
        String query = "SELECT * FROM gestorarticles WHERE stock > ?";
        String[] args = new String[] {"0"};
        return dbR.rawQuery(query, args);
    }

    public Cursor article(long id) {
        /* Consulta de tots els productos amb Stock */
        String query = "SELECT * FROM gestorarticles WHERE _id = ?";
        String[] args = new String[] {String.valueOf(id)};
        return dbR.rawQuery(query, args);
    }

    public Cursor countArticle(String id) {
        /* Consulta de tots els productos amb Stock */
        String query = "SELECT COUNT(codiarticle) AS codiarticle FROM gestorarticles WHERE codiarticle = ?";
        String[] args = new String[] {id};
        return dbR.rawQuery(query, args);
    }

    // MODIFICACIO DB
    public long insert(String codiArticle, String descricpio, float pvp, int stock) {
        // Creem una nova tasca i retornem el id crear per si el necessiten
        ContentValues values = new ContentValues();
        values.put(GESTORARTICLES_CODIARTICLE, codiArticle);
        values.put(GESTORARTICLES_DESCRIPCION, descricpio);
        values.put(GESTORARTICLES_PVP, pvp);
        values.put(GESTORARTICLES_STOCK, stock);

        return dbW.insert(table_GESTORARTICLES,null,values);
    }

    public void update(long id, String descripcio, float pvp, int stock) {
        // Modifiquem els valors de las tasca amb clau primària "id"
        ContentValues values = new ContentValues();
        values.put(GESTORARTICLES_DESCRIPCION, descripcio);
        values.put(GESTORARTICLES_PVP,pvp);
        values.put(GESTORARTICLES_STOCK,stock);

        String[] args = new String[] {String.valueOf(id)};
        dbW.update(table_GESTORARTICLES,values, GESTORARTICLES_ID + " = ?", args);
    }

    public void delete(long id) {
        // Eliminem l'artile amb clau primària "id"
        String[] args = new String[] {String.valueOf(id)};
        dbW.delete(table_GESTORARTICLES,GESTORARTICLES_ID + " = ?", args);
    }

    public void dropDataBase() {
        // Fem un drop de la data base
        String exec = "DROP TABLE IF EXISTS gestorarticles";
        dbW.execSQL(exec);
    }

}
