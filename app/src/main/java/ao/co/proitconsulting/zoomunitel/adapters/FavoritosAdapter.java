package ao.co.proitconsulting.zoomunitel.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.RevistaViewHolder> {

    private List<RevistaZoOm> revistaZoOmList;

    public FavoritosAdapter(List<RevistaZoOm> revistaZoOmList) {
        this.revistaZoOmList = revistaZoOmList;
    }

    @NonNull
    @Override
    public RevistaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RevistaViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.favoritos_item_container,
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RevistaViewHolder holder, int position) {
//        viewPager2.setCurrentItem(position);
        holder.setDetalheInfo(revistaZoOmList.get(position));

    }

    @Override
    public int getItemCount() {
        return revistaZoOmList.size();
    }

    static class RevistaViewHolder extends RecyclerView.ViewHolder{
        private CardView cardViewFav;
        private ImageView rvImgBackgnd;
        private RoundedImageView rvImg;
        private TextView txtRvId;
        private ImageView imgDelete;
        private TextView txtRvTitle;
        private TextView txtDescricao;
        private RatingBar ratingBar;
        private ImageView imgFav;


        RevistaViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewFav = itemView.findViewById(R.id.cardViewFav);
            rvImgBackgnd = itemView.findViewById(R.id.rvImgBackgnd);
            rvImg = itemView.findViewById(R.id.rvImg);
            txtRvId = itemView.findViewById(R.id.txtRvId);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            txtRvTitle = itemView.findViewById(R.id.txtRvTitle);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            imgFav = itemView.findViewById(R.id.imgFav);
        }

        void setDetalheInfo(final RevistaZoOm revistaZoOm){
//            Picasso.get().load(revistaZoOm.getImagem()).fit().placeholder(R.drawable.magazine_placeholder).into(imageView);

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


            txtRvId.setText(revistaZoOm.getCategoria());
            txtRvTitle.setText(revistaZoOm.getTitle());
            txtDescricao.setText(revistaZoOm.getDescricao());
            ratingBar.setRating(revistaZoOm.getRating());

            cardViewFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RevistaDetalheActivity.class);
                    intent.putExtra("toolbarTitle",revistaZoOm.getTitle());
                    intent.putExtra("position",getAdapterPosition());
                    v.getContext().startActivity(intent);
                }
            });

//            imgDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(v.getContext(), "Eliminar "+revistaZoOm.getTitle()+" dos favoritos?", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//
//
//            imgFav.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(v.getContext(), "Já adicionou "+revistaZoOm.getTitle()+" aos favoritos!", Toast.LENGTH_SHORT).show();
//                }
//            });


        }
    }


}
