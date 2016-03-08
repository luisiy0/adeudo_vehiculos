package finanzas.col.gob.mx.adeudo.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import finanzas.col.gob.mx.adeudo.R;

/**
 * Created by lnunez on 26/11/2015.
 */
public class Utils {


    private static final String LOG_TAG = "Pantalla Principal" ;
    private static final String BASE_URL = "https://www.finanzas.col.gob.mx/nvo/ws/App.php?";

    //mensajes servidor
    public static final String CORRECTO = "Correcto";

    //putExtra Intents
    public static final String JsonStr = "JSON";
    public static final String Recibo = "recibo";
    public static final String Linea = "linea";
    public static final String Internet = "internet";
    public static final String NoInternet = "0";
    public static final String SiInternet = "1";
    public static final String Placa = "placa";
    public static final String Serie = "serie";
    public static final String Afirme = "AFIRME";
    public static final String BANBAJIO = "BANBAJIO";
    public static final String BANAMEX = "BANAMEX";
    public static final String BANORTE = "BANORTE";
    public static final String SANTANDER = "SANTANDER";
    public static final String SCOTIABANK = "SCOTIABANK";
    public static final String TELECOMM = "TELECOMM";
    public static final String HSBC = "HSBC";
    public static final String Banco = "banco";

    // receptoria
    public static final String Armeria = "Armeria";
    public static final String Colima = "Colima";
    public static final String Manzanillo = "Manzanillo";
    public static final String Tecoman = "Tecoman";
    public static final String TransporteColima = "Transporte Colima";
    public static final String TransporteManzanillo = "Transporte Manzanillo";
    public static final String OficinaCentral = "Oficina Central";
    public static final String ModuloVehicularVilla = "Modulo Vehicular Soriana";


    //  envio web
    private static final String WebPlaca2 = "Placa2";
    private static final String WebNumeroSerie2 = "Num_Serie2";
    private static final String Version = "V2";
    private static  final String Linea_c = "Linea_c";
    private static  final String Registro = "Registro";

    //recibir web
    private static final String Mensaje = "mensaje";





    //public static final String
    public static List<String> ServicioWebV2(String Placa, String Numero, Context contextt){
        BufferedReader reader = null;
        String JsonStr = null;
        List<String> respuesta = new ArrayList() ;
        try {

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(WebPlaca2, Placa)
                    .appendQueryParameter(WebNumeroSerie2, Numero)
                    .appendQueryParameter(Version, "2")
                    .build();

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            InputStream caInput = contextt.getResources().openRawResource(R.raw.gob);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            URL url = new URL(builtUri.toString());
            HttpsURLConnection urlConnection =
                    (HttpsURLConnection)url.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            JsonStr = buffer.toString();
            JSONObject JsonObject = new JSONObject(JsonStr);
            respuesta.add(JsonObject.getString(Mensaje));
            respuesta.add(JsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        }catch (JSONException e){
            Log.e(LOG_TAG,e.getMessage(),e);
            e.printStackTrace();
        }
        catch (Exception e){
            Log.e(LOG_TAG, "Error ", e);
        }
        return respuesta;
    }


    public static void ServicioWeRegistrarId(String Linea, String RegistroId ,Context contextt){

        try {

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(Linea_c, Linea)
                    .appendQueryParameter(Registro, RegistroId)
                    .build();

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            InputStream caInput = contextt.getResources().openRawResource(R.raw.gob);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            URL url = new URL(builtUri.toString());
            HttpsURLConnection urlConnection =
                    (HttpsURLConnection)url.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());

            Log.i("url:", builtUri.toString());

            urlConnection.getInputStream();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        }
        catch (Exception e){
            Log.e(LOG_TAG, "Error ", e);
        }
    }

    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

}
