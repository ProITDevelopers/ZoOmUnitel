package ao.co.proitconsulting.zoomunitel.localDB;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import ao.co.proitconsulting.zoomunitel.ZoOmUnitelApplication;
import ao.co.proitconsulting.zoomunitel.models.UsuarioPerfil;


public class AppPrefsSettings {

    private static final String APP_SHARED_PREF_NAME = "ZM_UNITEL_REF";
    private static final String KEY_USER = "USUARIO_KEY";
    private static final String KEY_AUTH_TOKEN = "USER_AUTH_TOKEN";
    private static final String KEY_CHANGE_VIEW = "CHANGE_VIEW";

    private static AppPrefsSettings mInstance;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Gson gson;


    public static synchronized AppPrefsSettings getInstance() {
        if (mInstance == null) {
            mInstance = new AppPrefsSettings(ZoOmUnitelApplication.getInstance().getApplicationContext());
        }
        return mInstance;
    }

    private AppPrefsSettings(Context context) {
        super();
        sharedPreferences = context.getSharedPreferences(APP_SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        gson = new Gson();
    }


    //SAVE USER DATA
    public void saveUser(UsuarioPerfil usuario){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userInfo = gson.toJson(usuario);
        editor.putString(KEY_USER, userInfo);
        editor.apply();

    }

    //GET USER DATA
    public UsuarioPerfil getUser(){

        String userInfo = sharedPreferences.getString(KEY_USER, null);
        return  gson.fromJson(userInfo, UsuarioPerfil.class);
    }

    //SAVE TOKEN
    public void saveAuthToken(String authToken) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AUTH_TOKEN, authToken);
        editor.apply();
    }




    //GET TOKEN
    public String getAuthToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }







    //SAVE CHANGE_VIEW
    public void saveChangeView(int viewValue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_CHANGE_VIEW, viewValue);
        editor.apply();
    }

    //GET CHANGE_VIEW
    public int getListGridViewMode() {
        return sharedPreferences.getInt(KEY_CHANGE_VIEW, 1);
    }

    //DELETE KEY_AUTH_TOKEN
    public void deleteToken(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_AUTH_TOKEN);
        editor.apply();


    }


    //DELETE ALL DATA
    public void clearAppPrefs(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
