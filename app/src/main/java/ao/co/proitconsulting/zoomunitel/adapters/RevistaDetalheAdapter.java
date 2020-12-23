package ao.co.proitconsulting.zoomunitel.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.activities.WebViewActivity;
import ao.co.proitconsulting.zoomunitel.helper.Common;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;
import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;

public class RevistaDetalheAdapter extends RecyclerView.Adapter<RevistaDetalheAdapter.RevistaViewHolder>{

    public String PDF_FILE_NAME ="";
    public Activity activity;
    private List<RevistaZoOm> revistaZoOmList;


    public RevistaDetalheAdapter(Activity activity, List<RevistaZoOm> revistaZoOmList) {
        this.activity = activity;
        this.revistaZoOmList = revistaZoOmList;
    }

    @NonNull
    @Override
    public RevistaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RevistaViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.detail_item_container,
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
        private TextView txtRvTitle, txtData;
        private TextView txtDescricao;
        private CardView cardViewDownload,cardViewLer;
        private Button btnDownload,btnLer;


        RevistaViewHolder(@NonNull View itemView) {
            super(itemView);
            rvImgBackgnd = itemView.findViewById(R.id.rvImgBackgnd);
            rvImg = itemView.findViewById(R.id.rvImg);
            txtRvTitle = itemView.findViewById(R.id.txtRvTitle);
            txtData = itemView.findViewById(R.id.txtData);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            cardViewDownload = itemView.findViewById(R.id.cardViewDownload);
            btnDownload = itemView.findViewById(R.id.btnDownload);
            cardViewLer = itemView.findViewById(R.id.cardViewLer);
            btnLer = itemView.findViewById(R.id.btnLer);
        }

        void setDetalheInfo(final RevistaZoOm revistaZoOm){


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
            rvImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), ""+revistaZoOm.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });



            txtRvTitle.setText(revistaZoOm.getTitle());

            String time = MetodosUsados.getTimestamp(revistaZoOm.getCreated_at());

            txtData.setText(time);
            txtDescricao.setText(revistaZoOm.getDescricao());

            cardViewDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnDownload.performClick();
                }
            });

            btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verificarPermissaoArmazenamento(revistaZoOm);
                }
            });

            cardViewLer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnLer.performClick();
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
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("link",Common.PDF_PATH + revistaZoOm.getPdfLink());
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
        new DownloadFileFromURL().execute(uriPDF.toString());

    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        public ProgressDialog pDialog;
        public String resultMessage="";

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            SpannableString title = new SpannableString("Baixando o ficheiro");
            title.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.orange_unitel)),
                    0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableString message = new SpannableString("Por favor aguarde...");
            message.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.blue_unitel)),
                    0, message.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            pDialog = new ProgressDialog(activity, R.style.MyAlertDialogStyle);
            pDialog.setTitle(title);
            pDialog.setMessage(message);
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected  String doInBackground(String... f_url){
            int count;
            try{
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                int lenghtOfFile = connection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                File folder = new File(Environment.getExternalStorageDirectory() +
                        File.separator + "ZoOM_Unitel");

                String storageDir = folder.getAbsolutePath();

                if (!folder.exists())
                    folder.mkdirs();



                File pdf_File = new File(storageDir+PDF_FILE_NAME);

                if(pdf_File.exists()){
                    resultMessage = "Já baixou este ficheiro\n"+storageDir;

                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {
                            MetodosUsados.mostrarMensagem(activity,resultMessage);
                        }
                    });

                }else {
                    OutputStream output = new FileOutputStream(pdf_File);

                    byte data[] = new byte[1024];
                    long total = 0;

                    while((count = input.read(data)) != -1){
                        total += count;

                        publishProgress(""+(int)((total*100)/lenghtOfFile));

                        output.write(data, 0, count);
                    }
                    output.flush();

                    output.close();
                    input.close();

                    resultMessage = "Guardado em\n" +storageDir;
                }



            }catch (Exception e){
                Log.e("Error: ", e.getMessage());
            }

            return null;

        }

        protected void onProgressUpdate(String... progress){
            pDialog.setProgress(Integer.parseInt(progress[0]));

            if (Integer.parseInt(progress[0])==100){
                MetodosUsados.mostrarMensagem(activity, resultMessage);
            }
        }

        @Override
        protected void onPostExecute(String file_url){
           pDialog.dismiss();
           pDialog.cancel();

        }
    }

    private void verificarConnecxaoDOWNLOAD(RevistaZoOm revistaZoOm) {


        ConnectivityManager conMgr =  (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr!=null){
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null){
                Toast.makeText(activity, "Sem acesso a internet!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(activity, "Sem acesso a internet!", Toast.LENGTH_SHORT).show();
            }else{
                gotoWebView(revistaZoOm);;
            }
        }
    }





}
