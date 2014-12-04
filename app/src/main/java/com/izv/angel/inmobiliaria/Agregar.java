package com.izv.angel.inmobiliaria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Agregar extends Activity {

    private EditText etDir,etLoc,etTip,etPrec;
    private Button bt1;
    private GestorInmueble gi;
    private Adaptador ad;
    private int posicion;
    private Inmueble aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_agregar);
        bt1 = (Button)findViewById(R.id.btAgregar);
        etLoc = (EditText) findViewById(R.id.etLocalidad);
        etDir = (EditText) findViewById(R.id.etDireccion);
        etTip = (EditText) findViewById(R.id.etTipo);
        etPrec = (EditText) findViewById(R.id.etPrecio);
        gi = new GestorInmueble(this);
        Intent i = getIntent();
        if(i.getType().equals("editar")){
            aux=(Inmueble)i.getExtras().getSerializable("inmueble");
            etLoc.setText(aux.getLocalidad());
            etDir.setText(aux.getDireccion());
            etTip.setText(aux.getTipo());
            etPrec.setText(aux.getPrecio()+"");
            bt1.setText(R.string.editarInm);
        }else{
            bt1.setText(R.string.agregarInm);
        }
    }

    protected void onResume() {
        super.onResume();
        //gi.open();
    }

    protected void onPause() {
        super.onPause();
        //gi.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agregar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void aniadir(View view){
        Intent i=getIntent();
        String localidad,direccion,tipo,precio;
        localidad = etLoc.getText().toString();
        direccion = etDir.getText().toString();
        tipo = etTip.getText().toString();
        precio = etPrec.getText().toString();
        if(i.getType().equals("agregar")){
           Inmueble inm = new Inmueble(localidad,direccion,tipo,Integer.valueOf(precio));
           i.putExtra("inmueble",inm);
        }
        if(i.getType().equals("editar")){
            aux.setLocalidad(localidad);
            aux.setDireccion(direccion);
            aux.setTipo(tipo);
            aux.setPrecio(Integer.valueOf(precio));
           i.putExtra("inmueble",aux);
        }
         /*long id=gi.insert(inm);
         //gi.getCursor().close();
         //ad.changeCursor(gi.getCursor());
         //Toast.makeText(this, "insertado con el id: " + id, Toast.LENGTH_LONG).show();*/
        setResult(RESULT_OK,i);
        finish();
    }
}
