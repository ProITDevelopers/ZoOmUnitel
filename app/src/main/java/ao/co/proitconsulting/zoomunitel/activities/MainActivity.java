package ao.co.proitconsulting.zoomunitel.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import ao.co.proitconsulting.zoomunitel.Api.ApiClient;
import ao.co.proitconsulting.zoomunitel.Api.ApiInterface;
import ao.co.proitconsulting.zoomunitel.EventBus.RevistaClick;
import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.helper.Common;
import ao.co.proitconsulting.zoomunitel.localDB.AppPrefsSettings;
import ao.co.proitconsulting.zoomunitel.models.UsuarioPerfil;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private UsuarioPerfil usuarioPerfil;
    private CircleImageView imgUserPhoto;
    private TextView txtUserNameInitial, txtUserName, txtUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_revista_detail, R.id.nav_perfil, R.id.nav_definicoes)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View view = navigationView.getHeaderView(0);
        imgUserPhoto = view.findViewById(R.id.imgUserPhoto);
        txtUserNameInitial = view.findViewById(R.id.txtUserNameInitial);
        txtUserName = view.findViewById(R.id.txtUserName);
        txtUserEmail = view.findViewById(R.id.txtUserEmail);

        usuarioPerfil = AppPrefsSettings.getInstance().getUser();
        carregarDadosOffline(usuarioPerfil);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }



    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onRevistaItemClick(RevistaClick event){
        if (event.isSuccess()){
//            Toast.makeText(this, "Click to: "+event.getRevista().getTitle(), Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            navController.navigate(R.id.nav_revista_detail);
        }
    }

    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_options; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logOut) {
            mensagemLogOut();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        verificarConecxaoNETPerfil();
        super.onResume();
    }

    private void mensagemLogOut() {
        SpannableString title = new SpannableString(getString(R.string.terminar_sessao));
        title.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.white)),
                0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString message = new SpannableString(getString(R.string.msg_deseja_continuar));
        message.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.blue_unitel)),
                0, message.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString yes = new SpannableString(getString(R.string.text_sim));
        yes.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.blue_unitel)),
                0, yes.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString no = new SpannableString(getString(R.string.text_nao));
        no.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.white)),
                0, no.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this,R.style.MyDialogTheme);

            builder.setTitle(title);
            builder.setMessage(message);

            builder.setPositiveButton(yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    logOut();
                }
            });

            builder.setNegativeButton(no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
            builder.setTitle(title);
            builder.setMessage(message);

            builder.setPositiveButton(yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    logOut();
                }
            });

            builder.setNegativeButton(no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }



    }

    private void logOut() {
        AppPrefsSettings.getInstance().clearAppPrefs();
        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void carregarDadosOffline(UsuarioPerfil usuarioPerfil) {
        if (usuarioPerfil!=null){
            txtUserName.setText(usuarioPerfil.userNome);
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

    private void verificarConecxaoNETPerfil() {
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr!=null) {
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo != null){
                carregarMeuPerfil();
            }else{
                usuarioPerfil = AppPrefsSettings.getInstance().getUser();
                carregarDadosOffline(usuarioPerfil);
            }
        }
    }

    private void carregarMeuPerfil() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<UsuarioPerfil>> usuarioCall = apiInterface.get_USER_PROFILE(usuarioPerfil.userId);

        usuarioCall.enqueue(new Callback<List<UsuarioPerfil>>() {
            @Override
            public void onResponse(@NonNull Call<List<UsuarioPerfil>> call, @NonNull Response<List<UsuarioPerfil>> response) {

                if (response.isSuccessful()) {

                    if (response.body()!=null && response.body().size()>0){

                        usuarioPerfil = response.body().get(0);
                        AppPrefsSettings.getInstance().saveUser(usuarioPerfil);
                        carregarDadosOffline(usuarioPerfil);
                    }else{
                        mostrarMensagemPopUp("Seu perfil foi eliminado.");
                    }

                }




            }

            @Override
            public void onFailure(@NonNull Call<List<UsuarioPerfil>> call, @NonNull Throwable t) {

            }
        });
    }

    private void mostrarMensagemPopUp(String msg) {
        SpannableString title = new SpannableString(getString(R.string.app_name));
        title.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.orange_unitel)),
                0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString message = new SpannableString(msg);
        message.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.blue_unitel)),
                0, message.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString ok = new SpannableString(getString(R.string.text_ok));
        ok.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.orange_unitel)),
                0, ok.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);

            builder.setTitle(title);
            builder.setMessage(message);
            builder.setCancelable(false);

            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    logOut();
                }
            });

            builder.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setCancelable(false);

            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    logOut();
                }
            });

            builder.show();
        }


    }


}