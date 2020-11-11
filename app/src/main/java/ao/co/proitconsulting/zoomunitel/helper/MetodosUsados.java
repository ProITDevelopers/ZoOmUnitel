package ao.co.proitconsulting.zoomunitel.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.Normalizer;
import java.util.Random;
import java.util.regex.Pattern;

import ao.co.proitconsulting.zoomunitel.R;


public class MetodosUsados {

    public static final String TAG = "METODOS_USADOS";
    public static AlertDialog waitingDialog;


    //=====================SPOTS_DIALOG_LOADING===============================================//
    public static void spotsDialog(Context context) {
//        waitingDialog = new SpotsDialog.Builder().setContext(context).build();
    }

    public static void showLoadingDialog(String message){
        waitingDialog.setMessage(message);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
    }

    public static void changeMessageDialog(String message){
        waitingDialog.setMessage(message);
    }

    public static void hideLoadingDialog(){
        waitingDialog.dismiss();
        waitingDialog.cancel();
    }
    //=====================================================================//
    //=====================================================================//


    //==============MOSTRAR_MENSAGENS=======================================================//
    public static void mostrarMensagem(Context mContexto, int mensagem) {
        Toast.makeText(mContexto,mensagem,Toast.LENGTH_SHORT).show();
    }

    public static void mostrarMensagem(Context mContexto, String mensagem) {
        Toast.makeText(mContexto,mensagem,Toast.LENGTH_SHORT).show();
    }

    public static void mostrarMensagemSnackBar(View view, String mensagem) {
        Snackbar.make(view, mensagem, 4000)
                .setActionTextColor(Color.WHITE).show();
    }

    //=====================================================================//
    //=====================================================================//


    //====================VALIDAR_EMAIL=================================================//
    public static boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    //=====================================================================//
    //=====================================================================//
    public static String removeAcentos(String text) {
        return text == null ? null :
                Normalizer.normalize(text, Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }


    //======================ESCONDER_TECLADO===============================================//
    public static void esconderTeclado(Activity activity) {
        try {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

                if (inputMethodManager!=null)
                    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }catch (Exception e){
            Log.i(TAG,"esconder teclado " + e.getMessage() );
        }
    }
    //=====================================================================//
    //=====================================================================//

    //========================CHANGE_STATUS_BAR_COLOR=============================================//
    public static void changeStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(color);
        }
    }

    public static void showCustomUI(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    //=====================================================================//
    //=====================================================================//

    //========================PARTILHAR_LINK_DA_APP=============================================//
    public static void shareTheApp(Context context) {

        final String appPackageName = context.getPackageName();
        String appName = context.getString(R.string.app_name);
        String appCategory = "Revistas";

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String postData = "Obtenha o aplicativo " + appName +
                " para ter acesso as melhores " + appCategory + "\n" +
                Common.SHARE_URL_GOOGLE_DRIVE + appPackageName;


        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Baixar Agora!");
        shareIntent.putExtra(Intent.EXTRA_TEXT, postData);
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent, "Partilhar App"));
    }
    //=======================================================================//
    //=====================================================================//


    //======================GERAR_NUMEROS_ALEATORIOS===============================================//
    public static int randNumber(int min, int max){
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }


    //=======================================================================//
    //=====================================================================//

    public static void sendFeedback(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;

            body = "\n\n-------------------------------------------------\nPor favor não remova essa informação\n SO do Dispositivo: Android \n Versão do SO do Dispositivo: " +
                    Build.VERSION.RELEASE + "\n Versão da Aplicação: " + body + "\n Marca do Dispositivo: " + Build.BRAND +
                    "\n Modelo do Dispositivo: " + Build.MODEL + "\n Fabricante do Dispositivo: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"developer@proit-consulting.com"});
        /*intent.putExtra(Intent.EXTRA_SUBJECT, "Query from android app");*/
        intent.putExtra(Intent.EXTRA_SUBJECT, "Consulta do aplicativo Android");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.enviar_email_client)));
    }


    //======================TRAFEGO_INTERNET===============================================//

    public static boolean conexaoInternetTrafego(Context context, final String TAG){
        String site = "www.google.com";
        WebView webViewInternet = new WebView(context);
        final boolean[] valorRetorno = new boolean[1];

        webViewInternet.setWebViewClient(new WebViewClient());
        webViewInternet.loadUrl(site);

        webViewInternet.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String descricaoErro, String failingUrl) {
                super.onReceivedError(view, errorCode, descricaoErro, failingUrl);
                if (errorCode == -2) {
                    valorRetorno[0] = false;
                    Log.i(TAG,"webView ERROR " + descricaoErro );
                    Log.i(TAG,"webView ERROR " + errorCode );
                }
            }
        });

        webViewInternet.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                valorRetorno[0] = true;
                Log.i(TAG,"webView " + progress );
            }
        });
        Log.i(TAG,"webView " + valorRetorno[0]);

        return valorRetorno[0];
    }

}
