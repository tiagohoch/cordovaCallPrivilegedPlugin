package com.hochkraft.plugins.privilegedcall;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * This class echoes a string called from JavaScript.
 */
//Changed from CDNsms to sms
public class Caller extends CordovaPlugin {

    
    private static final String FEATURE_NOT_SUPPORTED = "FEATURE_NOT_SUPPORTED";
public static final String ACTION_DIAL_NUMBER = "dialNumber";
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("sendMessage")) {
            String phoneNumber = args.getString(0);
            //String message = args.getString(1);

            boolean isSupported = getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);

            if (!isSupported) {
                JSONObject errorObject = new JSONObject();

                errorObject.put("code", FEATURE_NOT_SUPPORTED);
                errorObject.put("message", "Calls are not supported on this device");

                callbackContext.sendPluginResult(new PluginResult(Status.ERROR, errorObject));
                return false;
            }

            this.makeCall(phoneNumber, callbackContext);

            return true;
        }

        return false;
    }

    private void makeCall(String phoneNumber, final CallbackContext callbackContext) throws JSONException {
        try {


            if (ACTION_DIAL_NUMBER.equals(action)) {
                String toDial = "tel:" +phoneNumber;
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(toDial));
                this.cordova.getActivity().startActivity(callIntent);
                callbackContext.success();
                return true;
            }
            callbackContext.error("Invalid action");
            return false;
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            callbackContext.error(e.getMessage());
            return false;
        }

    }

   
}
