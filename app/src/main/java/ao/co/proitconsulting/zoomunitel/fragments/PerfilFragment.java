package ao.co.proitconsulting.zoomunitel.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.squareup.picasso.Picasso;

import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.helper.Common;
import ao.co.proitconsulting.zoomunitel.localDB.AppPrefsSettings;
import ao.co.proitconsulting.zoomunitel.models.UsuarioPerfil;
import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {

    private View view;

    private UsuarioPerfil usuarioPerfil;
    private CircleImageView imgUserPhoto;
    private TextView txtUserNameInitial, txtUserName, txtUserTelefone, txtUserEmail;
    private Button btnEditPerfil;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_perfil, container, false);

        initViews();






        return view;
    }

    private void initViews(){
        imgUserPhoto = view.findViewById(R.id.imgUserPhoto);
        txtUserNameInitial = view.findViewById(R.id.txtUserNameInitial);
        txtUserName = view.findViewById(R.id.txtUserName);
        txtUserTelefone = view.findViewById(R.id.txtUserTelefone);
        txtUserEmail = view.findViewById(R.id.txtUserEmail);

        btnEditPerfil = view.findViewById(R.id.btnEditPerfil);
        btnEditPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(PerfilFragment.this)
                        .navigate(R.id.action_nav_perfil_to_nav_editar_perfil);
            }
        });
    }

    private void carregarDadosOffline(UsuarioPerfil usuarioPerfil) {
        if (usuarioPerfil!=null){
            txtUserName.setText(usuarioPerfil.userNome);
            txtUserTelefone.setText(usuarioPerfil.userTelefone);
            txtUserEmail.setText(usuarioPerfil.userEmail);
            if (usuarioPerfil.userPhoto == null){
                if (usuarioPerfil.userNome != null){
                    String userNameInitial = String.valueOf(usuarioPerfil.userNome.charAt(0));
                    txtUserNameInitial.setText(userNameInitial.toUpperCase());
                    txtUserNameInitial.setVisibility(View.VISIBLE);

                }else {

                    if (usuarioPerfil.userEmail != null){
                        String userNameInitial = String.valueOf(usuarioPerfil.userEmail.charAt(0));
                        txtUserNameInitial.setText(userNameInitial.toUpperCase());
                    }
                }

            }else {
                txtUserNameInitial.setVisibility(View.GONE);
                Picasso.get()
                        .load(Common.USER_IMAGE_PATH + usuarioPerfil.userPhoto)
//                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .fit().centerCrop()
                        .placeholder(R.drawable.user_placeholder)
                        .into(imgUserPhoto);
            }
        }
    }

    @Override
    public void onResume() {
        usuarioPerfil = AppPrefsSettings.getInstance().getUser();
        carregarDadosOffline(usuarioPerfil);
        super.onResume();
    }


}