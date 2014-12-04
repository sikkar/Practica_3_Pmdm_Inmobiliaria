package com.izv.angel.inmobiliaria;


import android.provider.BaseColumns;

public class Contrato {

    private Contrato (){
    }

    public static abstract class TablaInmueble implements BaseColumns {
        public static final String TABLA = "inmueble";
        public static final String LOCALIDAD ="localidad";
        public static final String DIRECCION = "direccion";
        public static final String TIPO="tipo";
        public static final String PRECIO = "precio";
    }

    public static abstract class TablaFotos implements BaseColumns {
        public static final String TABLA = "fotos";
        public static final String IDIMUEBLE ="idinmueble";
        public static final String FOTO = "foto";
    }


}
