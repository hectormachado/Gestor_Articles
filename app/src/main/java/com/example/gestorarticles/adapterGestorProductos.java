package com.example.gestorarticles;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

public class adapterGestorProductos extends android.widget.SimpleCursorAdapter {

    private static final String noStock = "#f54021";
    private static final String siStock = "#FFFFFF";

    private  MainActivity gestorProductos;

    public adapterGestorProductos(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        gestorProductos = (MainActivity) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);

        // COGER OBJETO CON UNA LINEA DEL CURSOR
        Cursor article = (Cursor) getItem(position);
        int stock = article.getInt(article.getColumnIndexOrThrow(GestorProductosDataSource.GESTORARTICLES_STOCK));

        // PINTAMOS FONDO SEGUN STOCK
        if (stock <= 0) {
            view.setBackgroundColor(Color.parseColor(noStock));
        }
        else {
            view.setBackgroundColor(Color.parseColor(siStock));
        }

        return view;
    }
}