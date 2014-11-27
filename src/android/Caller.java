package com.hochkraft.plugins.privilegedcall;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * This class echoes a string called from JavaScript.
 */
//Changed from CDNsms to sms
public class Caller extends CordovaPlugin {

    private static final String FEATURE_NOT_SUPPORTED = "FEATURE_NOT_SUPPORTED";
    public static final String ACTION_DIAL_NUMBER = "dialNumber";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (ACTION_DIAL_NUMBER.equals(action)) {
            String phoneNumber = args.getString(0);
            //String message = args.getString(1);

//            boolean isSupported = this.cordova.getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
//
//            if (!isSupported) {
//                JSONObject errorObject = new JSONObject();
//
//                errorObject.put("code", FEATURE_NOT_SUPPORTED);
//                errorObject.put("message", "Calls are not supported on this device");
//
//                callbackContext.sendPluginResult(new PluginResult(Status.ERROR, errorObject));
//                return false;
//            }
            return this.makeCall(phoneNumber, callbackContext);

            //return true;
        }

        return false;
    }

    private boolean makeCall(String phoneNumber, final CallbackContext callbackContext) throws JSONException {
        try {

//                String toDial = "tel:" +phoneNumber;
//                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(toDial));
//                       Context context =  cordova.getActivity().getApplicationContext();
//            context.startActivity(callIntent);
            //Toast.makeText(this, "normal fone", Toast.LENGTH_LONG).show();

            String url = "tel:" + phoneNumber;
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
            startActivity(intent);
            //this.cordova.getActivity().startActivity(callIntent);
            callbackContext.success();
            return true;

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            callbackContext.error(e.getMessage());
            return false;
        }

    }

    private class EndCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (TelephonyManager.CALL_STATE_RINGING == state) {
                //Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }
            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                //wait for phone to go offhook (probably set a boolean flag) so you know your app initiated the call.
                isPhoneCalling = true;
            }
            if (TelephonyManager.CALL_STATE_IDLE == state) {
                //when this state occurs, and your flag is set, restart your app
                if (isPhoneCalling) {

					//Log.i(LOG_TAG, "restart app");
                    // restart app
//                    //Intent i = getBaseContext().getPackageManager()
//                            .getLaunchIntentForPackage(
//                                    getBaseContext().getPackageName());
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);

                    isPhoneCalling = false;
                }
            }
        }
    }
}
