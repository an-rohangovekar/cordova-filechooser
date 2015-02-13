package com.megster.cordova;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.media.AudioPlayer.STATE;
import org.json.JSONException;
import org.json.JSONObject;

public class FileChooser extends CordovaPlugin {

    private static final String TAG = "FileChooser";
    private static final String ACTION_OPEN = "open";
    private static final int PICK_FILE_REQUEST = 1;
    CallbackContext callback;

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
    	 Log.w(TAG, "action="+action);
        if (action.equals(ACTION_OPEN)) {
            chooseFile(callbackContext);
            return true;
        }
        if(action.equals("startPlaying"))
        {
        	 Log.w(TAG, "in ="+action);
        	startPlaying(args.getString(0));
        	return true;
        }
        if(action.equals("stopPlaying"))
        {
        	stopPlaying();
        	return true;
        }
        return false;
    }

    public void chooseFile(CallbackContext callbackContext) {

        // type and title should be configurable

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Intent chooser = Intent.createChooser(intent, "Select File");
        cordova.startActivityForResult(this, chooser, PICK_FILE_REQUEST);

        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        callback = callbackContext;
        callbackContext.sendPluginResult(pluginResult);
    }
    /**
     * Start or resume playing audio file.
     *
     * @param file              The name of the audio file.
     */
    private MediaPlayer player=null;
    public void startPlaying(String file) {
    	 Log.w(TAG, "startPlaying");
    	Uri myUri = Uri.parse(file);
    	stopPlaying();
    	player=MediaPlayer.create(this.cordova.getActivity().getApplicationContext(), myUri);
    	player.start();
    }
    public void stopPlaying() {
    	if (this.player != null) {
           this.player.stop();
               
            this.player.release();
            this.player = null;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_FILE_REQUEST && callback != null) {

            if (resultCode == Activity.RESULT_OK) {

                Uri uri = data.getData();
                Log.w(TAG, uri.toString());
                if (uri != null) {
                	Cursor returnCursor =this.cordova.getActivity().getContentResolver().query(uri, null, null, null, null);
                	Log.w(TAG,"1");
                	JSONObject json = new JSONObject();
                	if(returnCursor !=null)
                	{
                		int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
	                	Log.w(TAG,"2");
	                	returnCursor.moveToFirst();
	                	Log.w(TAG,"3");
	                    Log.w(TAG, returnCursor.getString(nameIndex));
	                    Log.w(TAG,"4");
	                    try {
							json.put("name",returnCursor.getString(nameIndex));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                	}
                	 
                    try {
                    	json.put("url",uri.toString());
                    	callback.success(json);
						} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                   
                    
                   

                } else {

                    callback.error("File uri was null");

                }

            } else if (resultCode == Activity.RESULT_CANCELED) {

                // TODO NO_RESULT or error callback?
                PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
                callback.sendPluginResult(pluginResult);

            } else {

                callback.error(resultCode);
            }
        }
    }
}
