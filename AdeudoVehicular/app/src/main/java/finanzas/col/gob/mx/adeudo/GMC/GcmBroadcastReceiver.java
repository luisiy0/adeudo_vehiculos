package finanzas.col.gob.mx.adeudo.GMC;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by lnunez on 07/12/2015.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            // Explicitly specify that GcmMessageHandler will handle the intent.
            ComponentName comp = new ComponentName(context.getPackageName(),
                    GcmMessageHandler.class.getName());

            // Start the service, keeping the device awake while it is launching.
            startWakefulService(context, (intent.setComponent(comp)));
            setResultCode(Activity.RESULT_OK);
        }catch (Exception e){
            Log.e("Error:",e.getMessage());
        }
    }


}
