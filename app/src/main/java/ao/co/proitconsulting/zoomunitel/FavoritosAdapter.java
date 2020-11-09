package ao.co.proitconsulting.zoomunitel;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

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
                        R.layout.detail_item_container,
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
        private ImageView rvImgBackgnd;
        private RoundedImageView rvImg;
        private TextView txtRvTitle;
        private RatingBar ratingBar;
        private TextView txtDescricao;
        private Button btnDownload,btnLer;


        RevistaViewHolder(@NonNull View itemView) {
            super(itemView);
            rvImgBackgnd = itemView.findViewById(R.id.rvImgBackgnd);
            rvImg = itemView.findViewById(R.id.rvImg);
            txtRvTitle = itemView.findViewById(R.id.txtRvTitle);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            btnDownload = itemView.findViewById(R.id.btnDownload);
            btnLer = itemView.findViewById(R.id.btnLer);
        }

        void setDetalheInfo(final RevistaZoOm revistaZoOm){
//            Picasso.get().load(revistaZoOm.getImagem()).fit().placeholder(R.drawable.magazine_placeholder).into(imageView);

            Picasso.get()
                    .load(revistaZoOm.getImagem())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.color.colorPrimary)
                    .into(rvImgBackgnd, new Callback() {

                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(revistaZoOm.getImagem()).fit().into(rvImgBackgnd);
                        }
                    });

            Picasso.get()
                    .load(revistaZoOm.getImagem())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.magazine_placeholder)
                    .into(rvImg, new Callback() {

                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(revistaZoOm.getImagem()).fit().into(rvImg);
                        }
                    });
            rvImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), ""+revistaZoOm.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });



            txtRvTitle.setText(revistaZoOm.getTitle());
            ratingBar.setRating(revistaZoOm.getRating());
            txtDescricao.setText(revistaZoOm.getDescricao());

            btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Baixar "+revistaZoOm.getTitle()+"?", Toast.LENGTH_LONG).show();
                }
            });
            btnLer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),WebViewActivity.class);
                    intent.putExtra("link",revistaZoOm.getLink());
                    v.getContext().startActivity(intent);
                }
            });


        }
    }


}
