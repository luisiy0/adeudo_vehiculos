package finanzas.col.gob.mx.adeudo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import finanzas.col.gob.mx.adeudo.DB.DBHelper;
import finanzas.col.gob.mx.adeudo.Utilities.Utils;
import me.leolin.shortcutbadger.ShortcutBadger;

public class VehiculoPagado extends Activity {

    private DBHelper mydb ;

    // File url to download
    private static String file_url = "https://www.finanzas.col.gob.mx/finanzas/pdf_prueba.asp";

    // Progress Dialog
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo_pagado);
        ShortcutBadger.with(getApplicationContext()).count(0); //for 1.1.3
        mydb = new DBHelper(this);
        CargarDatos();
    }

    private void CargarDatos(){

        Intent intent = getIntent();

        ArrayList array_list = mydb.GetReciboDetalle(intent.getStringExtra(Utils.Recibo).toString());

        TextView Linea = (TextView) findViewById(R.id.txtRecibo);
        Linea.setText(intent.getStringExtra(Utils.Recibo));

        Linea = (TextView) findViewById(R.id.txtPlaca);
        Linea.setText(array_list.get(0).toString());

        Linea = (TextView) findViewById(R.id.txtSerie);
        Linea.setText(array_list.get(1).toString());


    }


    public void Descargar(View v){

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(file_url));
        startActivity(i);

    }

}
