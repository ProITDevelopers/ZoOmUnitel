package ao.co.proitconsulting.zoomunitel.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import ao.co.proitconsulting.zoomunitel.R;

public class PerfilFragment extends Fragment {


    private View view;
    private Button btnEditPerfil;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_perfil, container, false);

        btnEditPerfil = view.findViewById(R.id.btnEditPerfil);
        btnEditPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(PerfilFragment.this)
                        .navigate(R.id.action_nav_perfil_to_nav_editar_perfil);
            }
        });

        return view;
    }
}