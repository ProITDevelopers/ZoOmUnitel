package ao.co.proitconsulting.zoomunitel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends AppCompatActivity {

    private String mLinK;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshMain;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent()!=null){
            mLinK = getIntent().getStringExtra("link");
        }

        setContentView(R.layout.activity_web_view);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        swipeRefreshMain = findViewById(R.id.swipeRefresh);
        swipeRefreshMain.setColorSchemeResources(
                R.color.colorPrimary,R.color.colorAccent,R.color.transparentBlack);

        webView = findViewById(R.id.webview);

        verificarConnecxao();
    }

    private void verificarConnecxao() {


            ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

            if (conMgr!=null){
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null){
                    progressDialog.cancel();
                    swipeRefreshMain.setRefreshing(false);
                    Toast.makeText(this, "Sem acesso a internet!", Toast.LENGTH_SHORT).show();
                }else{
                    carregarSite();
                }
            }
    }


    private void carregarSite() {
        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl("https://docs.google.com/viewer?embedded=true&url="+mLinK);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('ndfHFb-c4YZDc-GSQQnc-LgbsSe ndfHFb-c4YZDc-to915-LgbsSe VIpgJd-TzA9Ye-eEGnhe ndfHFb-c4YZDc-LgbsSe')[0].style.display='none'; })()");

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);



            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {

                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('ndfHFb-c4YZDc-GSQQnc-LgbsSe ndfHFb-c4YZDc-to915-LgbsSe VIpgJd-TzA9Ye-eEGnhe ndfHFb-c4YZDc-LgbsSe')[0].style.display='none'; })()");

                if (progress < 100){
                    progressDialog.setMessage("Carregando...".concat("("+progress+"%"+")"));

                }

                if (progress >= 35){
                    swipeRefreshMain.setRefreshing(false);
                    progressDialog.setCancelable(false);
                    progressDialog.dismiss();
                }



                if(progress == 100) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onResume() {

        //handling swipe refresh
        swipeRefreshMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                verificarConnecxao();
            }
        });

        super.onResume();
    }

    @Override
    public void onDestroy() {
        progressDialog.dismiss();
        super.onDestroy();
    }

}