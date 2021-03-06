package ao.co.proitconsulting.zoomunitel.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import ao.co.proitconsulting.zoomunitel.Api.TLSSocketFactory;
import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.activities.PdfViewActivity;
import ao.co.proitconsulting.zoomunitel.helper.Common;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;
import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;
import okhttp3.OkHttpClient;

public class RevistaDetalheLerDownloadAdapter extends RecyclerView.Adapter<RevistaDetalheLerDownloadAdapter.RevistaViewHolder>{

    public String PDF_FILE_NAME ="";
    public Activity activity;
    private List<RevistaZoOm> revistaZoOmList;


    public RevistaDetalheLerDownloadAdapter(Activity activity, List<RevistaZoOm> revistaZoOmList) {
        this.activity = activity;
        this.revistaZoOmList = revistaZoOmList;
    }

    @NonNull
    @Override
    public RevistaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RevistaViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.revista_item_detail_ler_down,
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RevistaViewHolder holder, int position) {

        holder.setDetalheInfo(revistaZoOmList.get(position));

    }

    @Override
    public int getItemCount() {
        return revistaZoOmList.size();
    }

    class RevistaViewHolder extends RecyclerView.ViewHolder{
        private ImageView rvImgBackgnd;
        private RoundedImageView rvImg;
        private Button btnDownload,btnLer;

        RevistaViewHolder(@NonNull View itemView) {
            super(itemView);
            rvImgBackgnd = itemView.findViewById(R.id.rvImgBackgnd);
            rvImg = itemView.findViewById(R.id.rvImg);

            btnDownload = itemView.findViewById(R.id.btnDownload);

            btnLer = itemView.findViewById(R.id.btnLer);
        }

        void setDetalheInfo(final RevistaZoOm revistaZoOm){

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

                /*
                 ** HttpURLConnection REQUEST FOR WORKING ON PRE LOLLIPOP DEVICES
                 */
                try {
                    rvImgBackgnd.setImageResource(R.color.colorPrimary);

                    OkHttpClient.Builder okb=new OkHttpClient.Builder()
                            .sslSocketFactory(new TLSSocketFactory());
                    OkHttpClient ok=okb.build();

                    Picasso p=new Picasso.Builder(activity)
                            .downloader(new OkHttp3Downloader(ok))
                            .build();


                    p.load(Common.IMAGE_PATH + revistaZoOm.getImagem())
                            .placeholder(R.drawable.magazine_placeholder)
                            .into(rvImg);

                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }else{
                Picasso.get()
                        .load(Common.IMAGE_PATH + revistaZoOm.getImagem())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.color.colorPrimary)
                        .into(rvImgBackgnd, new Callback() {

                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(Common.IMAGE_PATH + revistaZoOm.getImagem()).fit().into(rvImgBackgnd);
                            }
                        });

                Picasso.get()
                        .load(Common.IMAGE_PATH + revistaZoOm.getImagem())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.magazine_placeholder)
                        .into(rvImg, new Callback() {

                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(Common.IMAGE_PATH + revistaZoOm.getImagem()).fit().placeholder(R.drawable.magazine_placeholder).into(rvImg);
                            }
                        });
            }






            btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verificarPermissaoArmazenamento(revistaZoOm);
                }
            });


            btnLer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    verificarConnecxaoLER(revistaZoOm);


                }
            });


        }





    }

    private void gotoWebView(RevistaZoOm revistaZoOm) {
        Intent intent = new Intent(activity, PdfViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("revistaZoOm",revistaZoOm);
//        intent.putExtra("link",Common.PDF_PATH + revistaZoOm.getPdfLink());
        activity.startActivity(intent);
    }


    private void verificarPermissaoArmazenamento(final RevistaZoOm revistaZoOm) {
        Dexter.withContext(activity.getBaseContext())
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            verificarConnecxaoDOWNLOAD(revistaZoOm);

                        }else {
                            MetodosUsados.mostrarMensagem(activity,"A ZoOm Unitel precisa de permissão de Armazenamento para continuar.");
                        }


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void downLoadPDF(RevistaZoOm revistaZoOm) {

        String nomePDF = MetodosUsados.removeAcentos(revistaZoOm.getTitle());
        nomePDF = nomePDF.replaceAll("\\s+","_");
        PDF_FILE_NAME = "/"+nomePDF+".pdf";

        Uri uriPDF = Uri.parse(Common.PDF_PATH + revistaZoOm.getPdfLink());
//        new DownloadFileFromURL().execute(uriPDF.toString());

        // execute this when the downloader must be fired
        final DownloadTask downloadTask = new DownloadTask(activity);
        downloadTask.execute(uriPDF.toString());
    }

    // usually, subclasses of AsyncTask are declared inside the activity class.
    // that way, you can easily modify the UI thread from here
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;
        ProgressDialog mProgressDialog;
        String resultMessage ="";

        public DownloadTask(Context context) {
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SpannableString title = new SpannableString("Baixando o ficheiro");
            title.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.orange_unitel)),
                    0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableString message = new SpannableString("Por favor aguarde...");
            message.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.blue_unitel)),
                    0, message.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // instantiate it within the onCreate method
            mProgressDialog = new ProgressDialog(activity, R.style.MyAlertDialogStyle);
            mProgressDialog.setTitle(title);
            mProgressDialog.setMessage(message);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);

            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);


        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                mostrarMensagemPopUp("Download erro: "+result);
            else
                mostrarMensagemPopUp(resultMessage);

        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
//            HttpsURLConnection connection = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
//                connection = (HttpsURLConnection) url.openConnection();
                connection = (HttpURLConnection) url.openConnection();

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

                    /*
                     ** HttpURLConnection REQUEST FOR WORKING ON PRE LOLLIPOP DEVICES
                     */
//                    try {
//                        connection.setSSLSocketFactory(new TLSSocketFactory());
//                    } catch (KeyManagementException e) {
//                        e.printStackTrace();
//                    } catch (NoSuchAlgorithmException e) {
//                        e.printStackTrace();
//                    }

                }

                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();

                File folder = new File(Environment.getExternalStorageDirectory() +
                        File.separator + "ZoOM_Unitel");

                String storageDir = folder.getAbsolutePath();

                if (!folder.exists())
                    folder.mkdirs();



                File pdf_File = new File(storageDir+PDF_FILE_NAME);

                output = new FileOutputStream(pdf_File);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }


                resultMessage = "Guardado em\n" +storageDir;

            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
    }

    private void verificarConnecxaoDOWNLOAD(RevistaZoOm revistaZoOm) {


        ConnectivityManager conMgr =  (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr!=null){
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null){
                MetodosUsados.mostrarMensagem(activity, activity.getString(R.string.msg_erro_internet));

            }else{
                downLoadPDF(revistaZoOm);;
            }
        }
    }

    private void verificarConnecxaoLER(RevistaZoOm revistaZoOm) {


        ConnectivityManager conMgr =  (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr!=null){
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null){
                MetodosUsados.mostrarMensagem(activity, activity.getString(R.string.msg_erro_internet));
            }else{
                gotoWebView(revistaZoOm);;
            }
        }
    }

    private void mostrarMensagemPopUp(String msg) {
        SpannableString title = new SpannableString(activity.getString(R.string.app_name));
        title.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.orange_unitel)),
                0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString message = new SpannableString(msg);
        message.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.blue_unitel)),
                0, message.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString ok = new SpannableString(activity.getString(R.string.text_ok));
        ok.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.orange_unitel)),
                0, ok.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);

            builder.setTitle(title);
            builder.setMessage(message);

            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(title);
            builder.setMessage(message);

            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }



    }





}
