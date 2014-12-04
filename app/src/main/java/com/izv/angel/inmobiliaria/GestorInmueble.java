package com.izv.angel.inmobiliaria;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class GestorInmueble {

    private Ayudante abd;
    private SQLiteDatabase bd;

    public GestorInmueble(Context c) {
        abd = new Ayudante(c);
    }

    public void open() {
        bd = abd.getWritableDatabase();
    }

    public void openRead() {
        bd = abd.getReadableDatabase();
    }

    public void close() {
        abd.close();
    }

    public long insert(Inmueble objeto) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaInmueble.LOCALIDAD, objeto.getLocalidad());
        valores.put(Contrato.TablaInmueble.DIRECCION,objeto.getDireccion());
        valores.put(Contrato.TablaInmueble.TIPO, objeto.getTipo());
        valores.put(Contrato.TablaInmueble.PRECIO, objeto.getPrecio());
        long id = bd.insert(Contrato.TablaInmueble.TABLA, null, valores);
        //id es el codigo autonumerico que me devuelve al insertar en la tabla.
        return id;
    }

    public int delete(Inmueble objeto) {
        String condicion = Contrato.TablaInmueble._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };
        int cuenta = bd.delete(Contrato.TablaInmueble.TABLA, condicion , argumentos);
        return cuenta;
    }

    public int update(Inmueble objeto) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaInmueble.LOCALIDAD, objeto.getLocalidad());
        valores.put(Contrato.TablaInmueble.DIRECCION,objeto.getDireccion());
        valores.put(Contrato.TablaInmueble.TIPO, objeto.getTipo());
        valores.put(Contrato.TablaInmueble.PRECIO, objeto.getPrecio());
        String condicion = Contrato.TablaInmueble._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };
        int cuenta = bd.update(Contrato.TablaInmueble.TABLA, valores, condicion, argumentos);
        return cuenta;
    }

    public List <Inmueble> select () {
        return select(null,null,null);
    }

    public List <Inmueble> select (String condicion, String [] parametros, String orden) {
        List<Inmueble> alj = new ArrayList<Inmueble>();
        Cursor cursor = bd.query(Contrato.TablaInmueble.TABLA, null, condicion, parametros, null, null, orden);
        cursor.moveToFirst();
        Inmueble inm;
        while (!cursor.isAfterLast()) {
            inm = getRow(cursor);  // pasa un registro de la tabla a un objeto de la tabla jugador
            alj.add(inm);
            cursor.moveToNext();
        }
        cursor.close();
        return alj;
    }

    public static Inmueble getRow(Cursor c) {
        Inmueble inm = new Inmueble();
        inm.setId(c.getLong(0));
        inm.setLocalidad(c.getString(1));
        inm.setDireccion(c.getString(2));
        inm.setTipo(c.getString(3));
        inm.setPrecio(c.getInt(4));
        return inm;
    }

    public Cursor getCursor(String condicion,String[] parametros,String orden) {
        Cursor cursor = bd.query(Contrato.TablaInmueble.TABLA, null, condicion, parametros, null, null, orden);
        return cursor;
    }

    public Cursor getCursor() {
        return getCursor(null,null,null);
    }
}
