package finanzas.col.gob.mx.adeudo.Utilities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import finanzas.col.gob.mx.adeudo.R;
import finanzas.col.gob.mx.adeudo.VehiculoPagadoLista;

/**
 * Created by lnunez on 07/12/2015.
 */
public class ListaAdapterPagado extends BaseAdapter {


    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;


    public ListaAdapterPagado(Activity a,ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

//    @Override
//    public boolean isEnabled(int position) {
//        return false;
//    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.pagado_list_row, null);
        }

        HashMap<String, String> song = data.get(position);



        TextView Texto = (TextView) vi.findViewById(R.id.txtPlaca);
        Texto.setText(song.get(VehiculoPagadoLista.KEY_Placa));
        Texto = (TextView) vi.findViewById(R.id.txtRecibo);
        Texto.setText(song.get(VehiculoPagadoLista.KEY_Recibo));

        ImageView Descargado = (ImageView) vi.findViewById(R.id.txtDescargado);
        Descargado.setImageDrawable(vi.getContext().getResources().getDrawable(R.drawable.check));



        return vi;
    }


}
