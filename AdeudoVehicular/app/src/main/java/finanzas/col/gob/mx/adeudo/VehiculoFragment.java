package finanzas.col.gob.mx.adeudo;

import android.app.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import finanzas.col.gob.mx.adeudo.DB.DBContract;
import finanzas.col.gob.mx.adeudo.DB.DBHelper;
import finanzas.col.gob.mx.adeudo.Utilities.Utils;

public class VehiculoFragment extends Activity {


    private int px;

    public static final String KEY_Ejercicio = "ejercicio";
    public static final String KEY_Concepto = "concepto";
    public static final String KEY_Importe = "importe";
    static final String KEY_Valor_Total_Ejercicio = "Valor_Total_Ejercicio";
    static final String KEY_Texto_Ejercicio = "ejercicio_texto";

    private static final String LOG_TAG = "Pantalla Vehiculo" ;

    private DBHelper mydb ;

    static final int Mapa_Result = 1;

    GoogleCloudMessaging gcm;
    private static String PROJECT_NUMBER = "592631822563";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehiculo_fragment);

        Resources r = this.getResources();
        px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                2,
                r.getDisplayMetrics()
        );
        mydb = new DBHelper(this);
        cargarPestanas();
        cargarDatos();
    }

    private void cargarDatos(){

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);

        Intent intent = getIntent();
        TextView Texto;

        Integer VehiculoId =  mydb.GetVehiculoId(intent.getStringExtra(Utils.Placa), intent.getStringExtra(Utils.Serie));

        if(intent.getStringExtra(Utils.Internet).equals(Utils.NoInternet)){

            //cargar datos desde la BD
            ArrayList<String> array_list = mydb.GetVehiculosLinea(VehiculoId);

            Texto = (TextView) findViewById(R.id.TextTotal);
            Texto.setText("$" + decimalFormat.format(Double.parseDouble(array_list.get(2))));

            Texto = (TextView) findViewById(R.id.TextLinea);
            Texto.setText(array_list.get(0));

            Texto = (TextView) findViewById(R.id.Fecha);
            Texto.setText("Vence " + array_list.get(1));

            array_list.clear();

            array_list = mydb.GetVehiculosDetalle(VehiculoId);

            Texto = (TextView) findViewById(R.id.TextNombre);
            Texto.setText(array_list.get(0));

            Texto = (TextView) findViewById(R.id.TextPlaca);
            Texto.setText(intent.getStringExtra(Utils.Placa));

            Texto = (TextView) findViewById(R.id.TextModelo);
            Texto.setText(array_list.get(1));

            Texto = (TextView) findViewById(R.id.TextNumeroSerie);
            Texto.setText(array_list.get(2));

            Texto = (TextView) findViewById(R.id.TextIdVehiculo);
            Texto.setText(array_list.get(3));

            Texto = (TextView) findViewById(R.id.TextMarcaVehiculo);
            Texto.setText(array_list.get(4));

            Texto = (TextView) findViewById(R.id.TextLineaVehiculo);
            Texto.setText(array_list.get(5));

            Texto = (TextView) findViewById(R.id.TextVersionVehiculo);
            Texto.setText(array_list.get(6));

            if(array_list.get(7).length() > 0)
            {
                LinearLayout linearSubsidio = (LinearLayout) findViewById(R.id.layoutSubsidio);
                linearSubsidio.setVisibility(View.VISIBLE);

                Texto = (TextView) findViewById(R.id.txtSubsidio);
                Texto.setText(array_list.get(7));
            }

            ArrayList<HashMap<String, String>> rowsList ;
            ArrayList <HashMap<String, String>> frameList = new ArrayList <HashMap<String, String>>();
            rowsList = mydb.GetVehiculosConcepto(VehiculoId);

            HashMap<String, String> Fila = new HashMap<String, String>();
            String EjercicioAnterior = "";
            Integer TotalMes = 0;

            for(int i=0; i < rowsList.size(); i ++){
                Fila = rowsList.get(i);
                // creating new HashMap

                if(EjercicioAnterior.length() > 0  && Fila.get(DBContract.Vehiculo_Concepto.COLUMN_EJERCICIO).equals(EjercicioAnterior))
                {
                   Log.d("bien","bien");
                }else {
                    if(TotalMes > 0){
                        HashMap<String, String> mapValor = new HashMap<String, String>();

                        mapValor.put(KEY_Valor_Total_Ejercicio,"$" + decimalFormat.format(Double.parseDouble(TotalMes.toString())).toString()  );
                        mapValor.put(KEY_Ejercicio,EjercicioAnterior);
                        mapValor.put(KEY_Texto_Ejercicio,"Total ejercicio: " + EjercicioAnterior);
                        frameList.add(mapValor); // totales
                        TotalMes = 0;
                    }
                }

                EjercicioAnterior = Fila.get(DBContract.Vehiculo_Concepto.COLUMN_EJERCICIO);
                Double x =Double.parseDouble(Fila.get(DBContract.Vehiculo_Concepto.COLUMN_IMPORTE).substring(1, Fila.get(DBContract.Vehiculo_Concepto.COLUMN_IMPORTE).length()))  ;

                TotalMes += x.intValue();


            }

            if(TotalMes > 0){
                HashMap<String, String> mapValor = new HashMap<String, String>();

                mapValor.put(KEY_Valor_Total_Ejercicio, "$" + decimalFormat.format(Double.parseDouble(TotalMes.toString())).toString());
                mapValor.put(KEY_Ejercicio,EjercicioAnterior);
                mapValor.put(KEY_Texto_Ejercicio,"Total ejercicio: " + EjercicioAnterior);
                frameList.add(mapValor); // totales
            }

            DibujarConceptos(frameList,rowsList);

            Toast.makeText(getApplicationContext(), "Información de la última consulta, no hay conexión a internet", Toast.LENGTH_LONG).show(); // Update the UI

        }else{
            try{



                JSONObject JsonObject = new JSONObject(intent.getStringExtra(Utils.JsonStr));

                Double Total = Double.parseDouble(JsonObject.getString("total"));
                String Linea = JsonObject.getString("linea");
                String FechaVencimiento = JsonObject.getString("fecha");

                Boolean existe = mydb.existePlaca(intent.getStringExtra(Utils.Placa), intent.getStringExtra(Utils.Serie));

                String Modelo = JsonObject.getString("cve_modelo");
                String Serie = JsonObject.getString("num_serie");
                String IdVehiculo = JsonObject.getString("txt_id_vehiculo");
                String Marca = JsonObject.getString("txt_marca");
                String LineaVehicular = JsonObject.getString("txt_linea");
                String Version = JsonObject.getString("txt_version");

                String MotivoPredial =  JsonObject.getString("motivo_predial");
                String MotivoAgua =  JsonObject.getString("motivo_agua");
                String AdeudoEstatal =  JsonObject.getString("adeudo_estatal");
                String Subsidio = "";




                RegistrarId(Linea);

                Texto = (TextView) findViewById(R.id.TextTotal);
                Texto.setText("$" + decimalFormat.format(Total).toString());

                Texto = (TextView) findViewById(R.id.TextLinea);
                Texto.setText(Linea);

                Texto = (TextView) findViewById(R.id.Fecha);
                Texto.setText("Vence " + FechaVencimiento);



                //existe en vehiculo linea
                if(existe)
                {
                    //actualiza
                    mydb.UpdateVehiculoLinea(VehiculoId, Linea, FechaVencimiento, Total);
                }else {
                    //guardar en vehiculo_linea
                    mydb.InsertVehiculoLinea(VehiculoId, Linea, FechaVencimiento, Total);
                }

                StringBuilder myName = new StringBuilder(new String(JsonObject.getString("Nombre").getBytes("ISO-8859-1"), "UTF-8"));

                //nombre
                for(int i=0;i<myName.length();i++){
                    char character = myName.charAt(i);
                    int ascii = (int) character;//63
                    if (ascii == 63)
                    {
                        myName.setCharAt(i, 'Ñ');
                    }
                }

                Texto = (TextView) findViewById(R.id.TextNombre);
                Texto.setText(myName.toString());

                Texto = (TextView) findViewById(R.id.TextPlaca);
                Texto.setText(JsonObject.getString("num_placa"));

                Texto = (TextView) findViewById(R.id.TextModelo);
                Texto.setText(Modelo);

                Texto = (TextView) findViewById(R.id.TextNumeroSerie);
                Texto.setText(Serie);

                Texto = (TextView) findViewById(R.id.TextIdVehiculo);
                Texto.setText(IdVehiculo);

                Texto = (TextView) findViewById(R.id.TextMarcaVehiculo);
                Texto.setText(Marca);

                Texto = (TextView) findViewById(R.id.TextLineaVehiculo);
                Texto.setText(LineaVehicular);

                Texto = (TextView) findViewById(R.id.TextVersionVehiculo);
                Texto.setText(Version);


                if(MotivoPredial.length() > 0 ||
                        MotivoAgua.length() > 0 ||
                        AdeudoEstatal.equals("1")){
                    LinearLayout linearSubsidio = (LinearLayout) findViewById(R.id.layoutSubsidio);
                    linearSubsidio.setVisibility(View.VISIBLE);

                    Subsidio = "Sin derecho al subsidio por adeudos:\n";

                    Texto = (TextView) findViewById(R.id.txtSubsidio);


                    if (MotivoPredial.length() > 0){
                        Subsidio = Subsidio + "Predial en " + MotivoPredial + "\n";
                    }

                    if (MotivoAgua.length() > 0){
                        Subsidio = Subsidio + "Agua Potable en " + MotivoAgua  + "\n";
                    }

                    if (AdeudoEstatal.equals("1")){
                        Subsidio = Subsidio + "El estado" ;
                    }

                    Texto.setText(Subsidio);

                }

                //existe en vehiculo linea
                if(existe)
                {
                    //actualiza
                    mydb.UpdateVehiculoDetalle(VehiculoId,myName.toString(),Modelo,Serie,IdVehiculo,Marca,LineaVehicular,Version,Subsidio);
                }else {
                    //guardar en vehiculo_linea
                    mydb.InsertVehiculoDetalle(VehiculoId, myName.toString(), Modelo, Serie, IdVehiculo, Marca, LineaVehicular, Version,Subsidio);
                }


                JSONArray JsonArray = new JSONArray(JsonObject.getString("Lista1"));
                JSONObject JsonObject2 ;


                ArrayList <HashMap<String, String>> rowsList = new ArrayList <HashMap<String, String>>();
                ArrayList <HashMap<String, String>> frameList = new ArrayList <HashMap<String, String>>();

                String EjercicioAnterior = "";
                Integer TotalMes = 0;

                //eliminar tabla conceptos
                mydb.DeleteVehiculoConcepto(VehiculoId);

                for (int i = 0; i < JsonArray.length(); ++i) {
                    JsonObject2 = JsonArray.getJSONObject(i);
                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();
                    if(EjercicioAnterior.length() > 0  && JsonObject2.getString("num_ejercicio_fiscal").equals(EjercicioAnterior))
                    {
                        map.put(KEY_Ejercicio,  JsonObject2.getString("num_ejercicio_fiscal"));
                    }else {
                        map.put(KEY_Ejercicio, JsonObject2.getString("num_ejercicio_fiscal")); //ejercicio
                        if(TotalMes > 0){
                            HashMap<String, String> mapValor = new HashMap<String, String>();

                            mapValor.put(KEY_Valor_Total_Ejercicio,"$" + decimalFormat.format(Double.parseDouble(TotalMes.toString())).toString()  );
                            mapValor.put(KEY_Ejercicio,EjercicioAnterior);
                            mapValor.put(KEY_Texto_Ejercicio,"Total ejercicio: " + EjercicioAnterior);
                            frameList.add(mapValor); // totales
                            TotalMes = 0;
                        }
                    }
                    map.put(KEY_Concepto, JsonObject2.getString("txt_concepto")); // concepto
                    map.put(KEY_Importe, "$" + decimalFormat.format(Double.parseDouble(JsonObject2.getString("imp_efectivo"))).toString() ); // importe

                    // adding HashList to ArrayList
                    rowsList.add(map);
                    EjercicioAnterior = JsonObject2.getString("num_ejercicio_fiscal");
                    TotalMes += Integer.parseInt(JsonObject2.getString("imp_efectivo"));



                }

                if(TotalMes > 0){
                    HashMap<String, String> mapValor = new HashMap<String, String>();

                    mapValor.put(KEY_Valor_Total_Ejercicio, "$" + decimalFormat.format(Double.parseDouble(TotalMes.toString())).toString());
                    mapValor.put(KEY_Ejercicio,EjercicioAnterior);
                    mapValor.put(KEY_Texto_Ejercicio,"Total ejercicio: " + EjercicioAnterior);
                    frameList.add(mapValor); // totales
                }

                DibujarConceptos(frameList,rowsList);

                //insertar en vehiculo detalle
                mydb.InsertVehiculoConcepto(VehiculoId,rowsList);



            }catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            catch (Exception e){
                Log.e(LOG_TAG, "Error ", e);
            }
        }



    }

    private void cargarPestanas(){

        final TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Información", null);
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Conceptos", null);
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Donde Pagar", null);
        tabs.addTab(spec);

        tabs.getTabWidget().getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < tabs.getTabWidget().getChildCount(); i++) {
                    LinearLayout x = (LinearLayout) tabs.getTabWidget().getChildAt(i);
                    // .setBackgroundResource(R.color.primary_dark); // unselected
                    TextView z = (TextView) x.getChildAt(1);
                    x.setBackgroundResource(R.color.primary_dark);
                    z.setTextColor(getResources().getColor(R.color.accent));
                }

                tabs.getTabWidget().setCurrentTab(2);
                tabs.getTabWidget().getChildAt(2)
                        .setBackgroundResource(R.color.accent); // selected // //have   // to// change

                LinearLayout x = (LinearLayout)  tabs.getTabWidget().getChildAt(2);
                TextView z = (TextView) x.getChildAt(1);
                z.setTextColor(getResources().getColor(R.color.fondo));

                Intent intent = new Intent(getBaseContext(), Mapa.class);
                intent.putExtra("tab", String.valueOf(tabs.getCurrentTab() ));


                startActivityForResult(intent, Mapa_Result);

            }
        });

        tabs.setCurrentTab(0);

        for (int i = 0; i < tabs.getTabWidget().getChildCount(); i++) {
            LinearLayout x = (LinearLayout) tabs.getTabWidget().getChildAt(i);
                   // .setBackgroundResource(R.color.primary_dark); // unselected
            TextView z = (TextView) x.getChildAt(1);
            x.setBackgroundResource(R.color.primary_dark);
            z.setTextColor((getResources().getColor(R.color.accent)));
        }
        tabs.getTabWidget().setCurrentTab(0);
        tabs.getTabWidget().getChildAt(tabs.getCurrentTab())
                .setBackgroundResource(R.color.accent); // selected // //have   // to// change

        LinearLayout x = (LinearLayout)  tabs.getTabWidget().getChildAt(tabs.getCurrentTab());
        TextView z = (TextView) x.getChildAt(1);
        z.setTextColor(getResources().getColor(R.color.fondo));


        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String arg0) {
                for (int i = 0; i < tabs.getTabWidget().getChildCount(); i++) {
                    LinearLayout x = (LinearLayout) tabs.getTabWidget().getChildAt(i);
                    // .setBackgroundResource(R.color.primary_dark); // unselected
                    TextView z = (TextView) x.getChildAt(1);
                    x.setBackgroundResource(R.color.primary_dark);
                    z.setTextColor(getResources().getColor(R.color.accent));
                }
                tabs.getTabWidget().setCurrentTab(0);
                tabs.getTabWidget().getChildAt(tabs.getCurrentTab())
                        .setBackgroundResource(R.color.accent); // selected // //have   // to// change

                LinearLayout x = (LinearLayout)  tabs.getTabWidget().getChildAt(tabs.getCurrentTab());
                TextView z = (TextView) x.getChildAt(1);
                z.setTextColor(getResources().getColor(R.color.fondo));

            }
        });

    }

    private void DibujarConceptos(ArrayList<HashMap<String, String>> totales,ArrayList<HashMap<String, String>> conceptos) {

        LinearLayout listDatos = (LinearLayout) findViewById(R.id.listDatos);
        TextView x;
        final int sdk = android.os.Build.VERSION.SDK_INT;

        for(int i=0; i < totales.size(); i++){
            LinearLayout listHijo = new LinearLayout(listDatos.getContext());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            for(int j=0; j < conceptos.size(); j++){

                if (totales.get(i).get(KEY_Ejercicio).toString().equals(conceptos.get(j).get(KEY_Ejercicio).toString())) {
                    //layout principal
                    LinearLayout lay2 = new LinearLayout(listDatos.getContext());
                    lay2.setOrientation(LinearLayout.HORIZONTAL);

                    //layout principal

                    //text ejercicio
                    x = new TextView(listDatos.getContext());
                    layoutParams = new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                    layoutParams.weight = (float) .20;
                    x.setText(conceptos.get(j).get(KEY_Ejercicio).toString());
                    x.setTextColor(listDatos.getResources().getColor(R.color.negro));
                    x.setTextAppearance(listDatos.getContext(), android.R.style.TextAppearance_Small);
                    lay2.addView(x, layoutParams);
                    //text ejercicio

                    // text concepto
                    x = new TextView(listDatos.getContext());
                    layoutParams = new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                    layoutParams.weight = (float) .60;
                    x.setText(conceptos.get(j).get(VehiculoFragment.KEY_Concepto).toString());
                    x.setTextColor(listDatos.getResources().getColor(R.color.negro));
                    x.setTextAppearance(listDatos.getContext(), android.R.style.TextAppearance_Small);
                    lay2.addView(x, layoutParams);
                    //text concepto

                    // text importe
                    x = new TextView(listDatos.getContext());
                    layoutParams = new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.RIGHT;
                    layoutParams.weight = (float) .20;
                    x.setText(conceptos.get(j).get(VehiculoFragment.KEY_Importe).toString());
                    x.setTextColor(listDatos.getResources().getColor(R.color.negro));
                    x.setTextAppearance(listDatos.getContext(), android.R.style.TextAppearance_Small);

                    if(sdk > Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        x.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    }

                    lay2.addView(x, layoutParams);
                    //text importe

                    layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(px, px, px, 0);
                    lay2.setLayoutParams(layoutParams);

                    listDatos.addView(lay2, layoutParams);
                }
            }

            x  = new TextView(listDatos.getContext());
            layoutParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.weight = (float) .80;
            x.setText(totales.get(i).get(KEY_Texto_Ejercicio));
            x.setTypeface(null, Typeface.BOLD);

            listHijo.addView(x, layoutParams);

            x  = new TextView(listDatos.getContext());
            layoutParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.weight = (float) .20;
            x.setText(totales.get(i).get(KEY_Valor_Total_Ejercicio));
            x.setTypeface(null, Typeface.BOLD);
            if(sdk > Build.VERSION_CODES.JELLY_BEAN_MR1) {
                x.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            }

            listHijo.addView(x, layoutParams);

            layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(px, px, px, 0);
            listHijo.setOrientation(LinearLayout.HORIZONTAL);
            //listHijo.setBackground(getDrawable(R.color.divisor));

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                listHijo.setBackgroundDrawable( getResources().getDrawable(R.color.divisor) );
            } else {
                listHijo.setBackground( getResources().getDrawable(R.color.divisor));
            }
            listHijo.setLayoutParams(layoutParams);

            listDatos.addView(listHijo,layoutParams);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == Mapa_Result) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);

                for (int i = 0; i < tabs.getTabWidget().getChildCount(); i++) {
                    LinearLayout x = (LinearLayout) tabs.getTabWidget().getChildAt(i);
                    // .setBackgroundResource(R.color.primary_dark); // unselected
                    TextView z = (TextView) x.getChildAt(1);
                    x.setBackgroundResource(R.color.primary_dark);
                    z.setTextColor(getResources().getColor(R.color.accent));
                }
                tabs.getTabWidget().setCurrentTab(Integer.parseInt(data.getStringExtra("tab")));
                tabs.getTabWidget().getChildAt(tabs.getCurrentTab())
                        .setBackgroundResource(R.color.accent); // selected // //have   // to// change

                LinearLayout x = (LinearLayout)  tabs.getTabWidget().getChildAt(tabs.getCurrentTab());
                TextView z = (TextView) x.getChildAt(1);
                z.setTextColor(getResources().getColor(R.color.fondo));

                // Do something with the contact here (bigger example below)
            }
        }
    }

    private void RegistrarId(final String Linea){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    String regid;
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM", msg);
                    Utils.ServicioWeRegistrarId(Linea, regid, getBaseContext());
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                catch(Exception e){
                    msg = "Error :" + e.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i("GCM Id: " , msg);

            }
        }.execute(null, null, null);
    }

    public void GuardarPantalla(View v){
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {


            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/OrdenPago" + now + ".jpg";



            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();



            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

            Uri contentUri = Uri.fromFile(imageFile);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

            Toast.makeText(getApplicationContext(), "La orden de pago se guardo en sus imágenes", Toast.LENGTH_LONG).show();

            //openScreenshot(imageFile);

            v.setVisibility(View.INVISIBLE);


        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

}
