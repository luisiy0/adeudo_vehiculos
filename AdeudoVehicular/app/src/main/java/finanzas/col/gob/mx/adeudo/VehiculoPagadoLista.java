package finanzas.col.gob.mx.adeudo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import finanzas.col.gob.mx.adeudo.DB.DBHelper;
import finanzas.col.gob.mx.adeudo.Utilities.ListaAdapterPagado;
import finanzas.col.gob.mx.adeudo.Utilities.Utils;
import me.leolin.shortcutbadger.ShortcutBadger;

public class VehiculoPagadoLista extends Activity {


    public static final String KEY_Placa = "placa";
    public static final String KEY_Recibo = "recibo";
    public static final String KEY_Descargado = "descargado";

    ListView list;
    ListaAdapterPagado adapter;

    private DBHelper mydb ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo_pagado_lista);
        cargarDatos();
    }

    private  void cargarDatos(){
        ShortcutBadger.with(getApplicationContext()).count(0); //for 1.1.3
        mydb = new DBHelper(this);
        final ArrayList<HashMap<String, String>> rowsList ;
        rowsList = mydb.GetPlacaRecibo();
        list=(ListView)findViewById(R.id.listView);

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_pagos, list,
                false);

        list.addHeaderView(header,null,false);
        adapter= new ListaAdapterPagado(this, rowsList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent intent = new Intent(getApplicationContext(), VehiculoPagado.class);
                intent.putExtra(Utils.Recibo, rowsList.get(pos-1).get(VehiculoPagadoLista.KEY_Recibo));
                startActivity(intent);
            }
        });

    }


}
