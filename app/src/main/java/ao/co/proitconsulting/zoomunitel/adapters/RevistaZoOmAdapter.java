package ao.co.proitconsulting.zoomunitel.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import ao.co.proitconsulting.zoomunitel.EventBus.RevistaClick;
import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.helper.Common;
import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;
import okhttp3.OkHttpClient;

public class RevistaZoOmAdapter extends RecyclerView.Adapter<RevistaZoOmAdapter.RevistaViewHolder> {


    private Context context;
    private List<RevistaZoOm> revistaZoOmList;

    public RevistaZoOmAdapter(Context context, List<RevistaZoOm> revistaZoOmList) {
        this.context = context;
        this.revistaZoOmList = revistaZoOmList;
    }

    @NonNull
    @Override
    public RevistaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RevistaViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.revista_slide_item,
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RevistaViewHolder holder, int position) {
        holder.setImage(revistaZoOmList.get(position), context);
        //Event
        holder.setListener(new IRecyclerClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Common.selectedRevista = revistaZoOmList.get(position);
                Common.selectedRevistaPosition = position;
                EventBus.getDefault().postSticky(new RevistaClick(true, revistaZoOmList.get(position)));

            }
        });

    }

    @Override
    public int getItemCount() {
        return revistaZoOmList.size();
    }

    static class RevistaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RoundedImageView imageView;
        private IRecyclerClickListener listener;

        RevistaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
            itemView.setOnClickListener(this);
        }

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        void setImage(final RevistaZoOm revistaZoOm, Context context){
//            Picasso.get().load(revistaZoOm.getImagem()).fit().placeholder(R.drawable.magazine_placeholder).into(imageView);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                /*
                 ** PICASSO REQUEST FOR WORKING ON PRE LOLLIPOP DEVICES
                 */
                try {

                    OkHttpClient.Builder okb=new OkHttpClient.Builder()
                            .sslSocketFactory(new TLSSocketFactory());
                    OkHttpClient ok=okb.build();

                    Picasso p=new Picasso.Builder(context)
                            .downloader(new OkHttp3Downloader(ok))
                            .build();

                    p.load(Common.IMAGE_PATH + revistaZoOm.getImagem())
                            .placeholder(R.drawable.magazine_placeholder)
                            .into(imageView);

                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }else{
                Picasso.get()
                        .load(Common.IMAGE_PATH + revistaZoOm.getImagem())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.magazine_placeholder)
                        .into(imageView, new Callback() {

                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(Common.IMAGE_PATH + revistaZoOm.getImagem()).fit().placeholder(R.drawable.magazine_placeholder).into(imageView);
                            }
                        });
            }






        }

        @Override
        public void onClick(View view) {
            listener.onItemClickListener(view,getAdapterPosition());
        }

    }


}
