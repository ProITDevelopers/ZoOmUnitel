package ao.co.proitconsulting.zoomunitel.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ao.co.proitconsulting.zoomunitel.R;

public class PerfilFragment extends Fragment {


    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_perfil, container, false);

        return view;
    }
}