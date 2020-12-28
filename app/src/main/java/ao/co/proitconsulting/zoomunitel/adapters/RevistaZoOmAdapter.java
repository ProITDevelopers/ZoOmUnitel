package ao.co.proitconsulting.zoomunitel.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.activities.RevistaDetalheActivity;
import ao.co.proitconsulting.zoomunitel.helper.Common;
import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;

public class RevistaZoOmAdapter extends RecyclerView.Adapter<RevistaZoOmAdapter.RevistaViewHolder> {


    private List<RevistaZoOm> revistaZoOmList;

    public RevistaZoOmAdapter(List<RevistaZoOm> revistaZoOmList) {
        this.revistaZoOmList = revistaZoOmList;
    }

    @NonNull
    @Override
    public RevistaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RevistaViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container,
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RevistaViewHolder holder, int position) {
        holder.setImage(revistaZoOmList.get(position));

    }

    @Override
    public int getItemCount() {
        return revistaZoOmList.size();
    }

    static class RevistaViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView imageView;

        RevistaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

        void setImage(final RevistaZoOm revistaZoOm){
//            Picasso.get().load(revistaZoOm.getImagem()).fit().placeholder(R.drawable.magazine_placeholder).into(imageView);

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
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), RevistaDetalheActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("toolbarTitle",revistaZoOm.getTitle());
                    intent.putExtra("position",getAdapterPosition());
                    v.getContext().startActivity(intent);
                }
            });


        }
    }


}
