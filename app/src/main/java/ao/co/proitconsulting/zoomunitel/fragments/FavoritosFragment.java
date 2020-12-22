package ao.co.proitconsulting.zoomunitel.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.adapters.FavoritosAdapter;
import ao.co.proitconsulting.zoomunitel.helper.Common;

public class FavoritosFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favoritos, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        setupAdapter();

        return view;
    }

    private void setupAdapter(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        FavoritosAdapter favoritosAdapter = new FavoritosAdapter(Common.revistaZoOmList);
        recyclerView.setAdapter(favoritosAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);

    }
}