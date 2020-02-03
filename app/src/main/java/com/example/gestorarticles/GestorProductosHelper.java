package com.example.gestorarticles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GestorProductosHelper extends SQLiteOpenHelper {

    // database version
    private static final int database_VERSION = 1;

    // database name
    private static final String database_NAME = "GestorArticlesDataBase";

    public GestorProductosHelper(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }

    private String CREATE_GESTORARTICLES =
            "CREATE TABLE gestorarticles ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "codiarticle TEXT," +
                    "descripcio TEXT," +
                    "pvp FLOAT," +
                    "stock INTEGER)";

    //"FOREIGN KEY(codiArticle) REFERENCES Articles_Magatzem(_id))";


    private String CREATE_GESTORMOVIMIENTOS =
            "CREATE TABLE gestormovimientos ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "codiarticle TEXT," +
                    "DIA TEXT," +
                    "CANTIDAD INT," +
                    "TIPO TEXT)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GESTORARTICLES);
        db.execSQL(CREATE_GESTORMOVIMIENTOS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("oldVersion", String.valueOf(oldVersion));
        Log.d("newVersion", String.valueOf(newVersion));

        if(oldVersion < 2) {
            db.execSQL(CREATE_GESTORMOVIMIENTOS);
        }
    }
}