package ao.co.proitconsulting.zoomunitel.fragments;

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
import ao.co.proitconsulting.zoomunitel.adapters.DefinicoesSobreNosAdapter;
import ao.co.proitconsulting.zoomunitel.helper.Common;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;
import ao.co.proitconsulting.zoomunitel.utilsClasses.trocarTema.ThemeView;

public class DefinicoesFragment extends Fragment implements View.OnClickListener {


    private View view;
    private TextView txtIdioma,txtModoList,txtModoGrid,txtDarkMode,txtTema;
    private SwitchCompat switchListMode,switchGridMode,switchDarkMode;
    private ThemeView themeView;
    private RecyclerView recyclerViewSobre;
    private LinearLayoutManager layoutManager;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_definicoes, container, false);

        initViews();



        return view;
    }

    private void initViews(){
        txtIdioma =  view.findViewById(R.id.txtIdioma);
        txtModoList =  view.findViewById(R.id.txtModoList);
        txtModoGrid =  view.findViewById(R.id.txtModoGrid);
        txtDarkMode =  view.findViewById(R.id.txtDarkMode);
        txtTema =  view.findViewById(R.id.txtTema);

        switchListMode =  view.findViewById(R.id.switchListMode);
        switchGridMode =  view.findViewById(R.id.switchGridMode);
        switchDarkMode =  view.findViewById(R.id.switchDarkMode);

        themeView = view.findViewById(R.id.theme_selected);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerViewSobre = view.findViewById(R.id.recyclerViewSobre);
        recyclerViewSobre.setHasFixedSize(true);
        recyclerViewSobre.setLayoutManager(layoutManager);

        viewsClicked();

        loadSobreNos();
    }

    private void viewsClicked(){
        txtIdioma.setOnClickListener(this);
        txtModoList.setOnClickListener(this);
        txtModoGrid.setOnClickListener(this);
        txtDarkMode.setOnClickListener(this);
        txtTema.setOnClickListener(this);
        themeView.setOnClickListener(this);

        switchListMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
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
                    MetodosUsados.mostrarMensagem(getContext(),""+txtModoGrid.getText().toString().concat(" activado."));
                }else{
                    MetodosUsados.mostrarMensagem(getContext(),""+txtModoGrid.getText().toString().concat(" desactivado."));
                }
            }
        });


        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    MetodosUsados.mostrarMensagem(getContext(),""+txtDarkMode.getText().toString().concat(" activado."));
                }else{
                    MetodosUsados.mostrarMensagem(getContext(),""+txtDarkMode.getText().toString().concat(" desactivado."));
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

            case R.id.txtIdioma :
                MetodosUsados.mostrarMensagem(getContext(),""+txtIdioma.getText().toString());
                break;

            case R.id.txtModoList :
                checkSwitchTextViews(switchListMode,txtModoList);
                break;

            case R.id.txtModoGrid :
                checkSwitchTextViews(switchGridMode,txtModoGrid);
                break;

            case R.id.txtDarkMode :
                checkSwitchTextViews(switchDarkMode,txtDarkMode);
                break;

            case R.id.txtTema :
                MetodosUsados.mostrarMensagem(getContext(),"Trocar o "+txtTema.getText().toString());
                break;

            case R.id.theme_selected :
                MetodosUsados.mostrarMensagem(getContext(),"Trocar o "+txtTema.getText().toString());
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