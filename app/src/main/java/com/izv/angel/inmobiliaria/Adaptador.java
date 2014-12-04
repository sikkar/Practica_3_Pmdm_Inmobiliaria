package com.izv.angel.inmobiliaria;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class Adaptador extends CursorAdapter {


    public class ViewHolder{
        public TextView tv1,tv2,tv3,tv4;
        public ImageView iv;
    }

    public Adaptador(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater i = LayoutInflater.from(parent.getContext());
        View v = i.inflate(R.layout.detalle, parent, false);
        return v;
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {

        ViewHolder vh = new ViewHolder();

        vh.tv1=(TextView) convertView.findViewById(R.id.tvLocalidad);
        vh.tv2=(TextView) convertView.findViewById(R.id.tvDireccion);
        vh.tv3=(TextView) convertView.findViewById(R.id.tvTipo);
        vh.tv4=(TextView) convertView.findViewById(R.id.tvPrecio);
        vh.iv =(ImageView) convertView.findViewById(R.id.ivCasa);

        Inmueble inm = GestorInmueble.getRow(cursor);

        vh.tv1.setText(inm.getLocalidad());
        vh.tv2.setText(inm.getDireccion());
        vh.tv3.setText(inm.getTipo());
        vh.tv4.setText(inm.getPrecio()+" €");
        if(inm.getTipo().equals("Casa")){
            vh.iv.setImageResource(R.drawable.casa);
        }else{
            if(inm.getTipo().equals("Garaje")){
                vh.iv.setImageResource(R.drawable.cochera);
            }
            else{
                vh.iv.setImageResource(R.drawable.trastero);
            }
        }
        //vh.iv.setImageURI();
    }

}
