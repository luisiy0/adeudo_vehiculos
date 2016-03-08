package finanzas.col.gob.mx.adeudo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Handler;

import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;

import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;


import finanzas.col.gob.mx.adeudo.DB.DBHelper;
import finanzas.col.gob.mx.adeudo.Utilities.Utils;


public class Principal extends AppCompatActivity {

    private Handler handler;

    private DBHelper mydb ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);



        mydb = new DBHelper(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        NoPermitirEspacios();
        handler = new Handler();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void NoPermitirEspacios(){

        final AutoCompleteTextView Placa = (AutoCompleteTextView) findViewById(R.id.TextPlaca);
        final EditText Numero = (EditText) findViewById(R.id.TextNumSerie);
        final Button boton = (Button) findViewById(R.id.button);




        //solo mayuscular
        Placa.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(7)});
        Numero.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(4)});

        Placa.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String str = s.toString();
                if (str.contains(" ")) {
                    Log.v("", "Cannot begin with space");
                    Placa.setText(str.trim());
                    Placa.setSelection(start);
                }
                if (Placa.length() == 7) {
                    Numero.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        Numero.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String str = s.toString();
                if (str.contains(" ")) {
                    Log.v("", "Cannot begin with space");
                    Numero.setText(str.trim());
                    Numero.setSelection(start);
                }
                if (Placa.length() > 0 &&
                        Numero.length() == 4) {
                    //esconder el teclado
                    if (getCurrentFocus() != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                    boton.setFocusableInTouchMode(true);
                    boton.requestFocus();
                    boton.setFocusableInTouchMode(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });



    }

    public void BotonClick(View view){

        final AutoCompleteTextView Placa = (AutoCompleteTextView) findViewById(R.id.TextPlaca);
        final EditText NumSerie = (EditText) findViewById(R.id.TextNumSerie);

        if (Placa.length() > 0 &&
                NumSerie.length() > 0) {
            if(!Utils.isNetworkAvailable(view.getContext())){
                // sin conexion a internet
                // existe datos de esa placa
                if(mydb.existePlaca(Placa.getText().toString(),NumSerie.getText().toString() )){
                    Intent intent = new Intent(getBaseContext(), VehiculoFragment.class);
                    intent.putExtra(Utils.Internet, Utils.NoInternet);
                    intent.putExtra(Utils.Placa, Placa.getText().toString());
                    intent.putExtra(Utils.Serie, NumSerie.getText().toString());
                    startActivity(intent);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Sin conexión a internet", Toast.LENGTH_LONG);
                    toast.show();
                }
            }else{

                final ProgressDialog dialog = ProgressDialog.show(Principal.this, "Espere un momento",
                        "Recuperando información", true);

                dialog.setCancelable(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
/*
                            //llave aes
                            String iv = "pruebapruebaprue"; //tamano 16
                            IvParameterSpec ivspec;
                            KeyGenerator keygen;
                            Key key;

                            ivspec = new IvParameterSpec(iv.getBytes());

                            keygen = KeyGenerator.getInstance("AES");
                            keygen.init(128);
                            key = keygen.generateKey();

                            SecretKeySpec keyspec = new SecretKeySpec(key.getEncoded(), "AES");

                            //llave aes


                            // encriptar

                            Cipher cipher;
                            byte[] encrypted;

                            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
                            encrypted = cipher.doFinal(Placa.getText().toString().getBytes());


                            // encriptar

                            Log.d("Encrypted",encrypted.toString());
                            Log.d("Key",Base64.encodeToString(keyspec.getEncoded(), Base64.DEFAULT));
*/

                            final List<String> respuesta = Utils.ServicioWebV2(Placa.getText().toString(), NumSerie.getText().toString(),getBaseContext());
                            if(respuesta.get(0).toString().equals(Utils.CORRECTO)) {
                                mydb.GuardarPlaca(Placa.getText().toString(),NumSerie.getText().toString());
                                Intent intent = new Intent(getBaseContext(), VehiculoFragment.class);
                                intent.putExtra(Utils.JsonStr, respuesta.get(1).toString());
                                intent.putExtra(Utils.Internet, Utils.SiInternet);
                                intent.putExtra(Utils.Placa, Placa.getText().toString());
                                intent.putExtra(Utils.Serie, NumSerie.getText().toString());
                                startActivity(intent);
                            }
                            else{
                                handler.post(new Runnable() { // This thread runs in the UI
                                    @Override
                                    public void run() {

                                        switch (Integer.parseInt(respuesta.get(0).toString()))
                                        {
                                            case -1:
                                                Toast.makeText(getApplicationContext(), "La placa no existe", Toast.LENGTH_LONG).show(); // Update the UI
                                                break;
                                            case -2:
                                                Toast.makeText(getApplicationContext(), "El numero de serie no coincide", Toast.LENGTH_LONG).show(); // Update the UI
                                                break;
                                            case -3:
                                                Intent intent = new Intent(getBaseContext(), MapaReceptoria.class);
                                                startActivity(intent);
                                                break;
                                            case -4:
                                                Toast.makeText(getApplicationContext(), "No existe adeudos", Toast.LENGTH_LONG).show(); // Update the UI
                                                break;
                                            default:
                                                Toast.makeText(getApplicationContext(), "Por el momento no hay servicio, favor de intentarlo más tarde", Toast.LENGTH_LONG).show(); // Update the UI
                                                break;
                                        }
                                    }
                                });
                            }

                        } catch (Exception e) {
                            Log.e("error",e.getMessage());
                        }
                        dialog.dismiss();
                    }
                }).start();

            }


        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Favor de llenar la información", Toast.LENGTH_SHORT);
            toast.show();
        }



    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final ArrayList<HashMap<String, String>> rowsList ;
        rowsList = mydb.getRecibos();

        if(rowsList.size() > 0)
            getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getBaseContext(), VehiculoPagadoLista.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void ListaAutocompletar(){
        AutoCompleteTextView Placa = (AutoCompleteTextView) findViewById(R.id.TextPlaca);
        ArrayList<String> array_list = mydb.getPlacas();
        //list de autocompletar
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array_list.toArray(new String[array_list.size()] ));
        Placa.setAdapter(adapter);
        Placa.setThreshold(1);
    }

    @Override
    public void onResume(){
        super.onResume();
        ListaAutocompletar();
    }
}
