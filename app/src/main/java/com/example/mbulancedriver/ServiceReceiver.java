package com.example.mbulancedriver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ServiceReceiver extends BroadcastReceiver {

    SharedPreferences sp;

    @Override
    public void onReceive(final Context context, Intent intent) {
        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);

                sp = PreferenceManager.getDefaultSharedPreferences(context);

                if(incomingNumber.startsWith("+")) {

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("num", incomingNumber);
                    editor.apply();

                }

            }
        },PhoneStateListener.LISTEN_CALL_STATE);


    }

}
