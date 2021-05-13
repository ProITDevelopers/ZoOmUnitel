package ao.co.proitconsulting.zoomunitel.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import ao.co.proitconsulting.zoomunitel.Api.ApiClient;
import ao.co.proitconsulting.zoomunitel.Api.ApiInterface;
import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;
import ao.co.proitconsulting.zoomunitel.localDB.AppPrefsSettings;
import ao.co.proitconsulting.zoomunitel.models.LoginRequest;
import ao.co.proitconsulting.zoomunitel.models.UsuarioAuth;
import ao.co.proitconsulting.zoomunitel.models.UsuarioPerfil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "TAG_LoginActivity";
    private RelativeLayout login_root;
    private Drawable editEmailDrawable, editPasswordDrawable;
    private AppCompatEditText editEmail;
    private ShowHidePasswordEditText editPassword;
    private TextView txtForgotPassword,txtRegister;
    private Button btnLogin;
    private String emailTelefone,password;
    private LoginRequest loginRequest = new LoginRequest();
    private UsuarioPerfil usuarioPerfil = new UsuarioPerfil();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MetodosUsados.showCustomUI(this);
        setContentView(R.layout.activity_login);

        //InitViews
        initViews();
    }

    private void initViews() {
        login_root = findViewById(R.id.login_root);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtRegister = findViewById(R.id.txtRegister);
        btnLogin = findViewById(R.id.btnLogin);

        SpannableString spannableString = new SpannableString(getString(R.string.hint_registe_se));
        ForegroundColorSpan fcsOrange = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.blue_unitel));
        spannableString.setSpan(fcsOrange,19,29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtRegister.setText(spannableString);

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, AlterarPalavraPasseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificarCamposEmailTelefone()) {
                    verificarConecxaoInternet();
                }
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        setEditTextTint_Pre_lollipop();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setEditTextTint_Pre_lollipop(){
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            editEmailDrawable = getResources().getDrawable(R.drawable.ic_baseline_phone_email_24);
            editPasswordDrawable = getResources().getDrawable(R.drawable.ic_baseline_lock_24);

            editEmailDrawable = DrawableCompat.wrap(editEmailDrawable);
            editPasswordDrawable = DrawableCompat.wrap(editPasswordDrawable);

            DrawableCompat.setTint(editEmailDrawable,getResources().getColor(R.color.orange_unitel));
            DrawableCompat.setTintMode(editEmailDrawable, PorterDuff.Mode.SRC_IN);

            DrawableCompat.setTint(editPasswordDrawable,getResources().getColor(R.color.orange_unitel));
            DrawableCompat.setTintMode(editPasswordDrawable, PorterDuff.Mode.SRC_IN);

            editEmail.setCompoundDrawablesWithIntrinsicBounds(editEmailDrawable,null,null,null);
            editPassword.setCompoundDrawablesWithIntrinsicBounds(editPasswordDrawable,null,null,null);
        }

    }

    private boolean verificarCamposEmailTelefone() {

        emailTelefone = editEmail.getText().toString().trim();
        password = editPassword.getText().toString().trim();

        if (emailTelefone.isEmpty()){
            editEmail.setError(getString(R.string.msg_erro_campo_vazio));
            return false;
        }

        if (MetodosUsados.validarEmail(emailTelefone)) {
            emailTelefone = emailTelefone.toLowerCase();
            loginRequest.email_Telefone = emailTelefone;

        }else {
            if (emailTelefone.matches("9[1-9][0-9]\\d{6}")){
                loginRequest.email_Telefone = emailTelefone;
                return true;
            } else {
                editEmail.requestFocus();
                editEmail.setError(getString(R.string.msg_erro_campo_com_email_telefone));
                return false;
            }
        }

        if (password.isEmpty()) {
            editPassword.requestFocus();
            editPassword.setError(getString(R.string.msg_erro_campo_vazio));
            return false;
        }



        editEmail.onEditorAction(EditorInfo.IME_ACTION_DONE);
        editPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);



        return true;
    }

    private void verificarConecxaoInternet() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr!=null){
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null) {

                MetodosUsados.mostrarMensagem(this,getString(R.string.msg_erro_internet));
            } else {
                loginRequest.password = password;
                autenticacaoLogin(loginRequest);

            }
        }
    }


    private void autenticacaoLogin(LoginRequest loginRequest) {

        MetodosUsados.showLoadingDialog(getString(R.string.msg_login_auth_verification));


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<UsuarioAuth> call = apiInterface.autenticarCliente(loginRequest);
        call.enqueue(new Callback<UsuarioAuth>() {
            @Override
            public void onResponse(@NonNull Call<UsuarioAuth> call, @NonNull Response<UsuarioAuth> response) {


                MetodosUsados.changeMessageDialog(getString(R.string.msg_login_auth_validando));
                if (response.isSuccessful() && response.body() != null) {
                    UsuarioAuth usuarioAuth = response.body();

                    AppPrefsSettings.getInstance().saveAuthToken(usuarioAuth.userToken);
//                    carregarTODOSPerfiS(usuarioAuth.userId);

                    carregarMeuPerfil(usuarioAuth.userId);


                } else {

                    MetodosUsados.hideLoadingDialog();

                    String responseErrorMsg ="",mensagem ="";

                    try {
                        responseErrorMsg = response.errorBody().string();

                        Log.v(TAG,"Error code: "+response.code()+", ErrorBody msg: "+responseErrorMsg);

                        if (responseErrorMsg.contains("Tunnel")){
                            mostrarMensagemPopUp(getString(R.string.msg_erro_servidor));
//                            showCustomToast(LoginActivity.this, R.drawable.ic_baseline_error_24,getString(R.string.msg_erro_servidor));
                        }else{
                            JSONObject jsonObject = new JSONObject(responseErrorMsg);

                            mensagem = jsonObject.getJSONObject("erro").getString("mensagem");

                            mostrarMensagemPopUp(mensagem);

//                            showCustomToast(LoginActivity.this, R.drawable.ic_baseline_error_24,mensagem);
                        }



                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (JSONException err){
                        Log.v(TAG, err.toString());
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<UsuarioAuth> call, @NonNull Throwable t) {
                MetodosUsados.hideLoadingDialog();
                if (!MetodosUsados.conexaoInternetTrafego(LoginActivity.this,TAG)){
                    MetodosUsados.mostrarMensagem(LoginActivity.this, getString(R.string.msg_erro_internet));
                }else  if ("timeout".equals(t.getMessage())) {
                    MetodosUsados.mostrarMensagem(LoginActivity.this, getString(R.string.msg_erro_internet_timeout));
                }else {
                    MetodosUsados.mostrarMensagem(LoginActivity.this, getString(R.string.msg_erro_servidor));
                }
                Log.v(TAG,"onFailure: " + t.getMessage());

            }
        });

    }

    private void carregarMeuPerfil(int userID) {
        MetodosUsados.changeMessageDialog(getString(R.string.msg_login_auth_carregando_dados));
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<UsuarioPerfil>>  usuarioCall = apiInterface.get_USER_PROFILE(userID);

        usuarioCall.enqueue(new Callback<List<UsuarioPerfil>>() {
            @Override
            public void onResponse(@NonNull Call<List<UsuarioPerfil>> call, @NonNull Response<List<UsuarioPerfil>> response) {

                if (response.isSuccessful()) {


                    MetodosUsados.hideLoadingDialog();
                    if (response.body()!=null){

                        usuarioPerfil = response.body().get(0);
                        AppPrefsSettings.getInstance().saveUser(usuarioPerfil);
                        launchHomeScreen();
                    }

                } else {
                    MetodosUsados.hideLoadingDialog();
                    AppPrefsSettings.getInstance().clearAppPrefs();
                }




            }

            @Override
            public void onFailure(@NonNull Call<List<UsuarioPerfil>> call, @NonNull Throwable t) {
                AppPrefsSettings.getInstance().clearAppPrefs();
                MetodosUsados.hideLoadingDialog();
                if (!MetodosUsados.conexaoInternetTrafego(LoginActivity.this,TAG)){
                    MetodosUsados.mostrarMensagem(LoginActivity.this, getString(R.string.msg_erro_internet));
                }else  if ("timeout".equals(t.getMessage())) {
                    MetodosUsados.mostrarMensagem(LoginActivity.this, getString(R.string.msg_erro_internet_timeout));
                }else {
                    MetodosUsados.mostrarMensagem(LoginActivity.this, getString(R.string.msg_erro_servidor));
                }

            }
        });
    }



    private void launchHomeScreen() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        String title = "Olá, ".concat(usuarioPerfil.userNome);
        String message = "Seja bem-vindo(a) ao ".concat(getString(R.string.app_name));
//        notificationHelper.createNotification(title,message,false);

        finish();
    }

    private void mostrarMensagemPopUp(String msg) {
        SpannableString title = new SpannableString(getString(R.string.app_name));
        title.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.white)),
                0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString message = new SpannableString(msg);
        message.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.blue_unitel)),
                0, message.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString ok = new SpannableString(getString(R.string.text_ok));
        ok.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.white)),
                0, ok.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this, R.style.MyDialogTheme);

            builder.setTitle(title);
            builder.setMessage(message);

            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
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

            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }


    }

    private void showCustomToast(Context mContexto, int imageResource, String mensagem) {
        ViewGroup viewGroup = findViewById(R.id.container_toast);
        View view = getLayoutInflater().inflate(R.layout.custom_toast_message,viewGroup);

        ImageView imgToast = view.findViewById(R.id.imgToast);
        imgToast.setImageResource(imageResource);

        TextView txtMessage = view.findViewById(R.id.txtToast);
        txtMessage.setText(mensagem);

        Toast toast = new Toast(mContexto);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.BOTTOM,0,20);
        toast.show();

    }


    @Override
    protected void onResume() {
        MetodosUsados.spotsDialog(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        MetodosUsados.hideLoadingDialog();
        super.onDestroy();
    }
}