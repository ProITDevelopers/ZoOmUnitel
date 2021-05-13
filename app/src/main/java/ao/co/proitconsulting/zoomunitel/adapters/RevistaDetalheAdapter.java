package ao.co.proitconsulting.zoomunitel.adapters;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import ao.co.proitconsulting.zoomunitel.Api.TLSSocketFactory;
import ao.co.proitconsulting.zoomunitel.Callback.IRecyclerClickListener;
import ao.co.proitconsulting.zoomunitel.EventBus.RevistaDetailClick;
import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.helper.Common;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;
import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;
import okhttp3.OkHttpClient;

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
                        R.layout.revista_item_detail,
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RevistaViewHolder holder, int position) {

        holder.setDetalheInfo(revistaZoOmList.get(position));

        //Event
        holder.setListener(new IRecyclerClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Common.selectedRevista = revistaZoOmList.get(position);
                Common.selectedRevistaPosition = position;
                EventBus.getDefault().postSticky(new RevistaDetailClick(true, revistaZoOmList.get(position)));

            }
        });

    }

    @Override
    public int getItemCount() {
        return revistaZoOmList.size();
    }

    class RevistaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView rvImgBackgnd;
        private RoundedImageView rvImg;
        private TextView txtRvTitle, txtData;
        private TextView txtDescricao;
        private Button btnVermais;
        private IRecyclerClickListener listener;

        RevistaViewHolder(@NonNull View itemView) {
            super(itemView);
            rvImgBackgnd = itemView.findViewById(R.id.rvImgBackgnd);
            rvImg = itemView.findViewById(R.id.rvImg);
            txtRvTitle = itemView.findViewById(R.id.txtRvTitle);
            txtData = itemView.findViewById(R.id.txtData);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);

            btnVermais = itemView.findViewById(R.id.btnVermais);
            btnVermais.setOnClickListener(this);


        }

        void setDetalheInfo(final RevistaZoOm revistaZoOm){

            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

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







            txtRvTitle.setText(revistaZoOm.getTitle());

            String time = MetodosUsados.getTimestamp(revistaZoOm.getCreated_at());

            txtData.setText(time);
            txtDescricao.setText(revistaZoOm.getDescricao());








        }

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.onItemClickListener(view,getAdapterPosition());
        }





    }





}
