package ao.co.proitconsulting.zoomunitel.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import java.io.File;

import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;
import dmax.dialog.SpotsDialog;

public class PdfViewActivity extends AppCompatActivity {

    private String mLinK;
    private PDFView pdfView;
    private AlertDialog waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent()!=null){
            mLinK = getIntent().getStringExtra("link");
        }
        setContentView(R.layout.activity_pdf_view);
        pdfView = findViewById(R.id.pdf_viewer);

        waitingDialog = new SpotsDialog.Builder().setContext(this).build();
        waitingDialog.setMessage("Por favor aguarde...");
        waitingDialog.setCancelable(false);
        waitingDialog.show();

        verificarConnecxao();
    }

    private void verificarConnecxao() {
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr!=null){
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null){
                waitingDialog.cancel();
                waitingDialog.dismiss();
                MetodosUsados.mostrarMensagem(this,getString(R.string.msg_erro_internet));
            }else{
                carregarPDF();
            }
        }
    }

    private void carregarPDF() {



        FileLoader.with(this)
                .load(mLinK)
                .asFile(new FileRequestListener<File>() {

                    @Override
                    public void onLoad(FileLoadRequest fileLoadRequest, FileResponse<File> fileResponse) {


                        waitingDialog.setMessage("Carregando...");


                        waitingDialog.cancel();
                        waitingDialog.dismiss();

                        File pdfFile = fileResponse.getBody();

                        pdfView.fromFile(pdfFile)
                                .password(null) // If have password
                                .defaultPage(0) // Open default page, you can remember this value to open from the last time
                                .enableSwipe(true)
                                .swipeHorizontal(false)
                                .enableDoubletap(true) // Double tap to zoom
                                .onDraw(new OnDrawListener() {
                                    @Override
                                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
                                        // Code here if you want to do something
                                    }
                                })
                                .onDrawAll(new OnDrawListener() {
                                    @Override
                                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
                                        // Code here if you want to do something
                                    }
                                })
                                .onPageError(new OnPageErrorListener() {
                                    @Override
                                    public void onPageError(int page, Throwable t) {
                                        Toast.makeText(PdfViewActivity.this, "Erro ao abrir a página "+page, Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .onPageChange(new OnPageChangeListener() {
                                    @Override
                                    public void onPageChanged(int page, int pageCount) {
                                        // Code here if you want to do something
                                    }
                                })
                                .onTap(new OnTapListener() {
                                    @Override
                                    public boolean onTap(MotionEvent e) {
                                        return true;
                                    }
                                })
                                .onRender(new OnRenderListener() {
                                    @Override
                                    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                                        pdfView.fitToWidth(); //Fixed screen size
                                    }
                                })
                                .enableAnnotationRendering(true)
                                .invalidPageColor(Color.WHITE)
                                .load();
                    }

                    @Override
                    public void onError(FileLoadRequest fileLoadRequest, Throwable throwable) {
                        Toast.makeText(PdfViewActivity.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        waitingDialog.cancel();
                        waitingDialog.dismiss();
                    }
                });


    }



    @Override
    protected void onDestroy() {
        waitingDialog.cancel();
        waitingDialog.dismiss();
        super.onDestroy();
    }
}