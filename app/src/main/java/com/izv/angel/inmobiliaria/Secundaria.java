package com.izv.angel.inmobiliaria;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;


public class Secundaria extends Activity {

    protected static final int CAMERA_REQUEST = 1;
    protected static final int GALLERY_PICTURE = 2;
    private Intent pictureActionIntent = null;
    private GestorFoto gf;
    private AdaptadorFotos adf;
    private long id;
    private ListView lv;

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
            Foto foto = GestorFoto.getRow(cursor);
            File file = new File(foto.getFoto());
            if(file.delete()){
                gf.delete(foto);
                adf.changeCursor(gf.getCursor());
                initComponents();
            }else{
                Log.v("no se puede borrar",file.getName());
            }

        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_secundaria);
        gf = new GestorFoto(this);
        Intent i=getIntent();
        id=i.getLongExtra("id",0);
        Log.v("idinmueble",id+"");
        FragmentoDos fdos = (FragmentoDos) getFragmentManager().findFragmentById(R.id.fragment2);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.longmenufotos, menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initComponents();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gf.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_secundaria, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_aniadir) {
            abrirDialogo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InputStream stream = null;
        Bitmap imagen=null;
        String currentDateTimeString;
        File archivo=null;
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_PICTURE:
                    gf.open();
                    try {
                        stream = getContentResolver().openInputStream(data.getData());
                        imagen = BitmapFactory.decodeStream(stream);
                        stream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    currentDateTimeString=currentDateTimeString.replace(" ","_");
                    currentDateTimeString=currentDateTimeString.replace("/","_");
                    archivo = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM),"inmueble_"+id+"_"+currentDateTimeString+".jpg");
                    try {
                        FileOutputStream out = new FileOutputStream(archivo);
                        imagen.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Foto fot= new Foto();
                    fot.setIdInmueble(id);
                    fot.setFoto(archivo.getPath());
                    gf.insert(fot);
                    break;
            }
        }
    }
    /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Intent i = getIntent();
        i.putExtra("id", id);
        setResult(RESULT_OK, i);
        if(Configuration.ORIENTATION_LANDSCAPE==Configuration.ORIENTATION_LANDSCAPE){
            finish();
        }
    }
    */

    /*****************************************************/
    /*                     auxiliares                    */
    /*****************************************************/

    public void initComponents(){
        gf.open();
        Cursor c = gf.getCursor(null,null,null,id);
        adf = new AdaptadorFotos(this,c);
        lv = (ListView) findViewById(R.id.listView2);
        lv.setAdapter(adf);
        lv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = view.getTag();
                AdaptadorFotos.ViewHolder vh;
                vh = (AdaptadorFotos.ViewHolder)o;
                String s=(String)vh.iv.getTag();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + s), "image/jpeg");
                startActivity(intent);
            }
        });
        registerForContextMenu(lv);
    }

    private void abrirDialogo() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle(R.string.foto_inmueble);
        myAlertDialog.setMessage(R.string.abrir_donde);

        myAlertDialog.setPositiveButton(R.string.galeria, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                pictureActionIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
                pictureActionIntent.setType("image/*");
                pictureActionIntent.putExtra("return-data", true);
                startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
            }
        });

        myAlertDialog.setNegativeButton(R.string.camara,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                pictureActionIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (pictureActionIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        gf.open();
                        Foto fot= new Foto();
                        fot.setIdInmueble(id);
                        fot.setFoto(photoFile.getPath());
                        gf.insert(fot);
                    } catch (IOException ex) {

                    }
                    if (photoFile != null) {
                        pictureActionIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
                        startActivityForResult(pictureActionIntent, CAMERA_REQUEST);
                    }
                }
            }
        });
        myAlertDialog.show();
    }

    private File createImageFile() throws IOException {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        currentDateTimeString=currentDateTimeString.replace(" ","_");
        currentDateTimeString=currentDateTimeString.replace("/","_");
        String imageFileName="inmueble_"+id+"_"+currentDateTimeString+"";
        File storageDir =  getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir  );
        // Save a file: path for use with ACTION_VIEW intents
        Log.v("ruta",image.getAbsolutePath()) ;
        return image;
    }


/*//metodo para coger la ruta absoluta de un Uri, no utilizado finalmente
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    */
}
