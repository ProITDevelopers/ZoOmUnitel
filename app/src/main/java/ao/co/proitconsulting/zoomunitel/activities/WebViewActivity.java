package ao.co.proitconsulting.zoomunitel.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import ao.co.proitconsulting.zoomunitel.R;

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
        progressDialog.setCancelable(true);

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

//        mLinK = "https://2442078988ac.ngrok.io/revista/view/d7803f1a-5070-4c44-afaf-a9876203d1a8.pdf";
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setDomStorageEnabled(true);
        webView.clearCache(true);
        webView.clearHistory();
        /**
         * Enabling zoom-in controls
         * */
//        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        Uri pdfLink = Uri.parse("https://docs.google.com/viewer?embedded=true&url="+mLinK);

        webView.loadUrl(pdfLink.toString());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

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

//                view.loadUrl("javascript:(function() { " +
//                        "document.getElementsByClassName('ndfHFb-c4YZDc-GSQQnc-LgbsSe ndfHFb-c4YZDc-to915-LgbsSe VIpgJd-TzA9Ye-eEGnhe ndfHFb-c4YZDc-LgbsSe')[0].style.display='none'; })()");

                if (progress < 100){
                    progressDialog.setMessage("Carregando...".concat("("+progress+"%"+")"));
                    swipeRefreshMain.setEnabled(true);

                }

                if (progress >= 90){
                    swipeRefreshMain.setRefreshing(false);
                    progressDialog.dismiss();
                }



                if(progress == 100) {
                    progressDialog.dismiss();
                    swipeRefreshMain.setEnabled(true);
                }
            }
        });



        webView.setHorizontalScrollBarEnabled(false);
//        webView.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if (event.getPointerCount() > 1) {
//                    //Multi touch detected
//                    return true;
//                }
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        // save the x
//                        m_downX = event.getX();
//                    }
//                    break;
//
//                    case MotionEvent.ACTION_MOVE:
//                    case MotionEvent.ACTION_CANCEL:
//                    case MotionEvent.ACTION_UP: {
//                        // set x so that it doesn't move
//
//                        if (event.getY() == 0) {
//                            swipeRefreshMain.setEnabled(true);
//                        } else {
//                            swipeRefreshMain.setEnabled(false);
//                        }
//                        event.setLocation(m_downX, event.getY());
//                    }
//                    break;
//                }
//
//                return false;
//            }
//        });
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
        progressDialog.cancel();
        progressDialog.dismiss();
        super.onDestroy();
    }

}