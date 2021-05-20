package ao.co.proitconsulting.zoomunitel.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import java.io.File;
import java.util.List;

import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.helper.Common;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;
import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;
import dmax.dialog.SpotsDialog;

public class PdfViewActivity extends AppCompatActivity {

    private RevistaZoOm revistaZoOm;
    private PDFView pdfView;
    private AlertDialog waitingDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent()!=null){
            revistaZoOm = (RevistaZoOm) getIntent().getSerializableExtra("revistaZoOm");
        }
        setContentView(R.layout.activity_pdf_view);
        pdfView = findViewById(R.id.pdf_viewer);



        waitingDialog = new SpotsDialog.Builder().setContext(this).build();
        waitingDialog.setMessage("Por favor aguarde...");
        waitingDialog.setCancelable(true);

        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            verificarConnecxao();

                        }else {
                            finish();
                            MetodosUsados.mostrarMensagem(PdfViewActivity.this,"A ZoOm Unitel precisa de permissão de Armazenamento para continuar.");
                        }


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();





    }

    private void verificarConnecxao() {
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr!=null){
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null){
                MetodosUsados.mostrarMensagem(this,getString(R.string.msg_erro_internet));
            }else{
                carregarPDF();
            }
        }
    }

    private void carregarPDF() {
        waitingDialog.show();

        String mLink = Common.PDF_PATH + revistaZoOm.getPdfLink();

        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "ZoOM_Unitel");

        if (!folder.exists())
            folder.mkdirs();

        final String storageDir = folder.getAbsolutePath();
//        final String storageDir = folder.getPath();





        FileLoader.with(this)
                .load(mLink)
                .fromDirectory(storageDir,FileLoader.DIR_INTERNAL)
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
                                .swipeHorizontal(true)
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