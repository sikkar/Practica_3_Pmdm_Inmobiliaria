package com.izv.angel.inmobiliaria;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class Principal extends Activity {

    private Adaptador ad;
    private final int ACTIVIDADAGREGAR=1;
    private final int ACTIVIDADEDITAR=2;
    private final int ACTIVIDADDOS=3;
    private GestorInmueble gi;
    private boolean horizontal;
    private ListView lv;
    private FragmentoDos fdos;


    /*****************************************************/
    /*                 metodos on                        */
    /*****************************************************/

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        if (id == R.id.action_borrar) {
            Cursor cursor = (Cursor) lv.getItemAtPosition(index);
            Inmueble inm = GestorInmueble.getRow(cursor);
            gi.delete(inm);
            ad.changeCursor(gi.getCursor());
            initComponents();
        } else {
            if (id == R.id.action_editar) {
                Intent i = new Intent(Principal.this,Agregar.class);
                i.setType("editar");
                Cursor cursor = (Cursor) lv.getItemAtPosition(index);
                Inmueble inm = GestorInmueble.getRow(cursor);
                i.putExtra("inmueble",inm);
                startActivityForResult(i,ACTIVIDADEDITAR);
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        gi=new GestorInmueble(this);
        fdos = (FragmentoDos) getFragmentManager().findFragmentById(R.id.fragmento2land);
        horizontal = fdos!=null && fdos.isInLayout();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.longmenu, menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initComponents();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gi.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_aniadir) {
            Intent i = new Intent(Principal.this,Agregar.class);
            i.setType("agregar");
            startActivityForResult(i,ACTIVIDADAGREGAR);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Inmueble inm;
        long id;
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTIVIDADAGREGAR:
                    gi.open();
                    inm=(Inmueble)data.getExtras().getSerializable("inmueble");
                    id=gi.insert(inm);
                    gi.getCursor().close();
                    ad.changeCursor(gi.getCursor());
                    Toast.makeText(this, R.string.insertado, Toast.LENGTH_LONG).show();
                    break;
                case ACTIVIDADEDITAR:
                    gi.open();
                    inm=(Inmueble)data.getExtras().getSerializable("inmueble");
                    int num=gi.update(inm);
                    gi.getCursor().close();
                    ad.changeCursor(gi.getCursor());
                    Toast.makeText(this, R.string.edicion, Toast.LENGTH_LONG).show();
                    break;
               /*
                case ACTIVIDADDOS:
                    if(data!=null) {
                        fdos = (FragmentoDos) getFragmentManager().findFragmentById(R.id.fragmento2land);
                        fdos.llenarListView(data.getLongExtra("id", 0));
                        break;
                    }*/
            }
        }
    }


    /*****************************************************/
    /*                     auxiliares                    */
    /*****************************************************/

    public void initComponents(){
        gi.open();
        Cursor c = gi.getCursor(null,null,"localidad");
        ad = new Adaptador(this,c);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(ad);
        lv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(horizontal){
                    fdos.llenarListView(id);
                }else{
                    Intent intent = new Intent(Principal.this,Secundaria.class);
                    intent.putExtra("id",id);
                    startActivityForResult(intent, ACTIVIDADDOS);
                }
            }
        });
        registerForContextMenu(lv);
    }

}

