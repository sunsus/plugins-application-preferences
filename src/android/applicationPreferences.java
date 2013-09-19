package com.simonmacdonald.prefs;

import java.util.Iterator;
import java.util.Map;

import org.apache.cordova.CordovaPlugin;
//import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
//import android.util.Log;

public class AppPreferences extends CordovaPlugin {

    //private static final String LOG_TAG = "AppPrefs";
    //private static final int NO_PROPERTY = 0;
    //private static final int NO_PREFERENCE_ACTIVITY = 1;

    @Override
    public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
    	
        //PluginResult.Status status = PluginResult.Status.OK;
        String result = "";

        //SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.ctx.getActivity());
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(cordova.getActivity());
        
        try {
            if (action.equals("get")) {
                String key = args.getString(0);
                if (sharedPrefs.contains(key)) {
                    Object obj = sharedPrefs.getAll().get(key);
                    
                    //return new PluginResult(status, obj.toString());
                    callbackContext.success(obj.toString());
                    return true;
                } else {
                    //return createErrorObj(NO_PROPERTY, "No such property called " + key);
                	callbackContext.error("No such property called " + key);
                	return false;
                }
            } else if (action.equals("set")) {
                String key = args.getString(0);
                String value = args.getString(1);
                if (sharedPrefs.contains(key)) {
                    Editor editor = sharedPrefs.edit();
                    if ("true".equals(value.toLowerCase()) || "false".equals(value.toLowerCase())) {
                        editor.putBoolean(key, Boolean.parseBoolean(value));
                    } else {
                        editor.putString(key, value);
                    }
                    //return new PluginResult(status, editor.commit());
                    editor.commit();
                    callbackContext.success();
                    return true;
                } else {
                    //return createErrorObj(NO_PROPERTY, "No such property called " + key);
                	callbackContext.error("No such property called " + key);
                	return false;
                }
            } else if (action.equals("load")) {
                JSONObject obj = new JSONObject();
                Map<String,?> prefs = sharedPrefs.getAll();
                Iterator<?> it = prefs.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry <?,?>pairs = (Map.Entry<?,?>)it.next();
                    obj.put(pairs.getKey().toString(), pairs.getValue().toString());
                }
                //return new PluginResult(status, obj);
                callbackContext.success(obj);
                return true;
            } else if (action.equals("show")) {
                String activityName = args.getString(0);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //intent.setClassName(this.ctx.getContext(), activityName);
                intent.setClassName(cordova.getActivity(), activityName);
                try {
                    //this.ctx.startActivity(intent);
                	cordova.startActivityForResult(this, intent, 0);
                } catch (ActivityNotFoundException e) {
                    //return createErrorObj(NO_PREFERENCE_ACTIVITY, "No preferences activity called " + activityName);
                	callbackContext.error("No preferences activity called " + activityName);
                	return false;
                }
            } 
        } catch (JSONException e) {
            //status = PluginResult.Status.JSON_EXCEPTION;
        	callbackContext.error(e.getMessage());
        	return false;
        }
        //return new PluginResult(status, result);
        callbackContext.success(result);
		return true;
    }

    /**
    private PluginResult createErrorObj(int code, String message) throws JSONException {
        JSONObject errorObj = new JSONObject();
        errorObj.put("code", code);
        errorObj.put("message", message);
        return new PluginResult(PluginResult.Status.ERROR, errorObj);
    }**/

}
