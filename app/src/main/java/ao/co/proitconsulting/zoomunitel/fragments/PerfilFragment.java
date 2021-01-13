package ao.co.proitconsulting.zoomunitel.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.squareup.picasso.Picasso;

import java.util.List;

import ao.co.proitconsulting.zoomunitel.Api.ApiClient;
import ao.co.proitconsulting.zoomunitel.Api.ApiInterface;
import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.activities.LoginActivity;
import ao.co.proitconsulting.zoomunitel.helper.Common;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;
import ao.co.proitconsulting.zoomunitel.localDB.AppPrefsSettings;
import ao.co.proitconsulting.zoomunitel.models.UsuarioPerfil;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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



        usuarioPerfil = AppPrefsSettings.getInstance().getUser();
        carregarDadosOffline(usuarioPerfil);

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
        verificarConecxaoNETPerfil();
        super.onResume();
    }

    private void verificarConecxaoNETPerfil() {
        if (getActivity()!=null) {
            ConnectivityManager conMgr =  (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conMgr!=null) {
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo != null){
                    carregarMeuPerfil();
                }
            }
        }
    }

    private void carregarMeuPerfil() {

        final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(getContext()).build();
        waitingDialog.setMessage(getString(R.string.msg_login_auth_carregando_dados));
        waitingDialog.setCancelable(false);
        waitingDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<UsuarioPerfil>> usuarioCall = apiInterface.get_USER_PROFILE(usuarioPerfil.userId);

        usuarioCall.enqueue(new Callback<List<UsuarioPerfil>>() {
            @Override
            public void onResponse(@NonNull Call<List<UsuarioPerfil>> call, @NonNull Response<List<UsuarioPerfil>> response) {

                if (response.isSuccessful()) {


                    waitingDialog.cancel();
                    waitingDialog.dismiss();
                    if (response.body()!=null){

                        usuarioPerfil = response.body().get(0);
                        AppPrefsSettings.getInstance().saveUser(usuarioPerfil);
                        carregarDadosOffline(usuarioPerfil);
                    }

                } else {
                    waitingDialog.cancel();
                    waitingDialog.dismiss();
                }




            }

            @Override
            public void onFailure(@NonNull Call<List<UsuarioPerfil>> call, @NonNull Throwable t) {
                waitingDialog.cancel();
                waitingDialog.dismiss();

            }
        });
    }
}