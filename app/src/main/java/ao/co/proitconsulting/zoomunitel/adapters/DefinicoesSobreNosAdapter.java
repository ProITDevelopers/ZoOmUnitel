package ao.co.proitconsulting.zoomunitel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;
import ao.co.proitconsulting.zoomunitel.models.SobreNos;

public class DefinicoesSobreNosAdapter extends RecyclerView.Adapter<DefinicoesSobreNosAdapter.ItemViewHolder>{

    private Context context;
    private List<SobreNos> sobreNosList;

    public DefinicoesSobreNosAdapter(Context context, List<SobreNos> sobreNosList) {
        this.context = context;
        this.sobreNosList = sobreNosList;
    }



    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sobrenos_item_layout, parent, false);

        return new ItemViewHolder(view);

    }



    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        SobreNos sobreNos = sobreNosList.get(position);

        holder.txtTitle.setText(sobreNos.getTitle());
        holder.txtDescription.setText(sobreNos.getDescription());

    }

    @Override
    public int getItemCount() {
        return sobreNosList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle, txtDescription;

        public ItemViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription =  itemView.findViewById(R.id.txtDescription);

            itemView.setTag(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SobreNos sobreNos = sobreNosList.get(getAdapterPosition());

                    goToOptionSelected(sobreNos);


                }
            });
        }

        private void goToOptionSelected(SobreNos sobreNos) {
            switch (sobreNos.getId()){
                case 1:
//                Intent intent = new Intent(context,AlterarPalavraPasseActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(intent);
                    MetodosUsados.mostrarMensagem(context,sobreNos.getTitle());
                    break;
                case 2:
                    MetodosUsados.mostrarMensagem(context,sobreNos.getDescription());
                    break;

                case 3:
                    MetodosUsados.mostrarMensagem(context,sobreNos.getDescription());
                    break;
                case 4:
                    MetodosUsados.sendFeedback(context);
                    break;
                case 5:
                    MetodosUsados.shareTheApp(context);
                    break;

            }
        }




    }


}
