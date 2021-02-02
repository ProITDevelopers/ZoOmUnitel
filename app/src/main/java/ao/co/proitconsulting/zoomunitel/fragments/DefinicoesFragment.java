package ao.co.proitconsulting.zoomunitel.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.activities.AlterarPalavraPasseActivity;
import ao.co.proitconsulting.zoomunitel.adapters.DefinicoesSobreNosAdapter;
import ao.co.proitconsulting.zoomunitel.helper.Common;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;

public class DefinicoesFragment extends Fragment implements View.OnClickListener {


    private View view;

    private TextView txtAlterarPass,txtModoList,txtModoGrid;
    private SwitchCompat switchListMode,switchGridMode;
    private RecyclerView recyclerViewSobre;
    private LinearLayoutManager layoutManager;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_definicoes, container, false);

        initViews();



        return view;
    }

    private void initViews(){


        txtAlterarPass =  view.findViewById(R.id.txtAlterarPass);
        txtModoList =  view.findViewById(R.id.txtModoList);
        txtModoGrid =  view.findViewById(R.id.txtModoGrid);


        switchListMode =  view.findViewById(R.id.switchListMode);
        switchGridMode =  view.findViewById(R.id.switchGridMode);


        layoutManager = new LinearLayoutManager(getContext());
        recyclerViewSobre = view.findViewById(R.id.recyclerViewSobre);
        recyclerViewSobre.setHasFixedSize(true);
        recyclerViewSobre.setLayoutManager(layoutManager);

        viewsClicked();

        loadSobreNos();
    }

    private void viewsClicked(){
        txtAlterarPass.setOnClickListener(this);
        txtModoList.setOnClickListener(this);
        txtModoGrid.setOnClickListener(this);

        switchListMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    switchGridMode.setChecked(false);
                    MetodosUsados.mostrarMensagem(getContext(),""+txtModoList.getText().toString().concat(" activado."));
                }else{
                    MetodosUsados.mostrarMensagem(getContext(),""+txtModoList.getText().toString().concat(" desactivado."));
                }
            }
        });


        switchGridMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    switchListMode.setChecked(false);
                    MetodosUsados.mostrarMensagem(getContext(),""+txtModoGrid.getText().toString().concat(" activado."));
                }else{
                    MetodosUsados.mostrarMensagem(getContext(),""+txtModoGrid.getText().toString().concat(" desactivado."));
                }
            }
        });



    }

    private void loadSobreNos() {

        DefinicoesSobreNosAdapter definicoesSobreNosAdapter = new DefinicoesSobreNosAdapter(getContext(), Common.getSobreNosList());
        recyclerViewSobre.setAdapter(definicoesSobreNosAdapter);
        definicoesSobreNosAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.txtAlterarPass :
                Intent intent = new Intent(getContext(), AlterarPalavraPasseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.txtModoList :
                checkSwitchTextViews(switchListMode,txtModoList);
                break;

            case R.id.txtModoGrid :
                checkSwitchTextViews(switchGridMode,txtModoGrid);
                break;

        }
    }

    private void checkSwitchTextViews(SwitchCompat switchCompat, TextView textView){

        if (!switchCompat.isChecked()){
            switchCompat.setChecked(true);
            MetodosUsados.mostrarMensagem(getContext(),""+textView.getText().toString().concat(" activado."));
        }else{
            switchCompat.setChecked(false);
            MetodosUsados.mostrarMensagem(getContext(),""+textView.getText().toString().concat(" desactivado."));
        }
    }
}