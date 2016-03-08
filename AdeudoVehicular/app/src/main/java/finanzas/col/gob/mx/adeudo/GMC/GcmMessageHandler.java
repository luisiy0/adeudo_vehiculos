package finanzas.col.gob.mx.adeudo.GMC;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import finanzas.col.gob.mx.adeudo.DB.DBHelper;
import finanzas.col.gob.mx.adeudo.R;
import finanzas.col.gob.mx.adeudo.Utilities.Utils;
import finanzas.col.gob.mx.adeudo.VehiculoPagado;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by lnunez on 07/12/2015.
 */
public class GcmMessageHandler extends IntentService {

    private Handler handler;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }
    private DBHelper mydb ;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        showToast(extras.getString(Utils.Linea),extras.getString(Utils.Recibo));
        Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("linea"));
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    public void showToast(final String linea, final String recibo){
        handler.post(new Runnable() {
            public void run() {
                //Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
                mydb = new DBHelper(getApplicationContext());
                mydb.InsertRecibo(recibo,linea);
                Intent intent = new Intent(getApplicationContext(), VehiculoPagado.class);
                intent.putExtra("recibo", recibo);
                PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent, 0);

                Notification n  = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Finanzas y Administracion")
                        .setContentText("Adeudos Pagados")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pIntent)
                        //.setSound(alarmSound)
                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS)
                        .setAutoCancel(true).build();

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0, n);

                ShortcutBadger.with(getApplicationContext()).count(1); //for 1.1.3

            }
        });
    }
}
