package ao.co.proitconsulting.zoomunitel.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "TAG_LoginActivity";
    private AppCompatEditText editEmail;
    private ShowHidePasswordEditText editPassword;
    private TextView txtForgotPassword,txtRegister;
    private Button btnLogin;
    private String emailTelefone,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //InitViews
        initViews();
    }

    private void initViews() {
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtRegister = findViewById(R.id.txtRegister);
        btnLogin = findViewById(R.id.btnLogin);

        SpannableString spannableString = new SpannableString(getString(R.string.hint_registe_se));
        ForegroundColorSpan fcsOrange = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.orange_unitel));
        spannableString.setSpan(fcsOrange,19,29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtRegister.setText(spannableString);

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MetodosUsados.mostrarMensagem(LoginActivity.this,txtForgotPassword.getText().toString());

//                Intent intent = new Intent(LoginActivity.this, AlterarPalavraPasseActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
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
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
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
//            loginRequest.email = emailTelefone;

        }else {
            if (emailTelefone.matches("9[1-9][0-9]\\d{6}")){
//                loginRequest.telefone = emailTelefone;
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
                MetodosUsados.mostrarMensagem(LoginActivity.this,R.string.msg_erro_internet);
            } else {
//                loginRequest.password = password;
//                loginRequest.rememberMe = true;
//                autenticacaoLogin(loginRequest);
                launchHomeScreen();
            }
        }
    }


    private void autenticacaoLogin(/*LoginRequest loginRequest*/) {

//        MetodosUsados.showLoadingDialog(getString(R.string.msg_login_auth_verification));
//
//
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<UsuarioAuth> call = apiInterface.autenticarCliente(loginRequest);
//        call.enqueue(new Callback<UsuarioAuth>() {
//            @Override
//            public void onResponse(@NonNull Call<UsuarioAuth> call, @NonNull Response<UsuarioAuth> response) {
//
//
//                MetodosUsados.changeMessageDialog(getString(R.string.msg_login_auth_validando));
//                if (response.isSuccessful() && response.body() != null) {
//                    UsuarioAuth userToken = response.body();
//
//
//                    AppPrefsSettings.getInstance().saveAuthToken(userToken.tokenuser);
//                    AppPrefsSettings.getInstance().saveTokenTime(userToken.expiracao);
//
//
//                    carregarMeuPerfil(userToken.tokenuser);
//
//
//                } else {
//
//                    MetodosUsados.hideLoadingDialog();
//
//                    String message ="";
//
//                    try {
//                        message = response.errorBody().string();
//                        Log.v("Error code 400",message);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (message.equals("\"Password ou Email Errados\"")){
//                        MetodosUsados.mostrarMensagem(LoginActivity.this,getString(R.string.msg_email_palavra_passe_errada));
//                    }
//
//                    if (message.equals("\"Usuario Não Existe\"")){
//                        Snackbar.make(login_root, getString(R.string.msg_erro_user_not_found), Snackbar.LENGTH_LONG)
//                                .setActionTextColor(ContextCompat.getColor(LoginActivity.this, R.color.login_register_text_color))
//                                .setAction(getString(R.string.criar_conta), new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        startActivity(intent);
//                                    }
//                                }).show();
//                    }
//
//
//
//
//
//
//
//
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<UsuarioAuth> call, @NonNull Throwable t) {
//                MetodosUsados.hideLoadingDialog();
//                if (!MetodosUsados.conexaoInternetTrafego(LoginActivity.this,TAG)){
//                    MetodosUsados.mostrarMensagem(LoginActivity.this,R.string.msg_erro_internet);
//                }else  if ("timeout".equals(t.getMessage())) {
//                    MetodosUsados.mostrarMensagem(LoginActivity.this,R.string.msg_erro_internet_timeout);
//                }else {
//                    MetodosUsados.mostrarMensagem(LoginActivity.this,R.string.msg_erro);
//                }
//                Log.i(TAG,"onFailure" + t.getMessage());
//
//                try {
//                    Snackbar
//                            .make(login_root, t.getMessage(), 4000)
//                            .setActionTextColor(Color.MAGENTA)
//                            .show();
//                } catch (Exception e) {
//                    Log.d(TAG, String.valueOf(e.getMessage()));
//                }
//            }
//        });

    }

    private void carregarMeuPerfil(String token) {
//        MetodosUsados.changeMessageDialog(getString(R.string.msg_login_auth_carregando_dados));
//        String bearerToken = Common.bearerApi.concat(token);
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<List<UsuarioPerfil>>  usuarioCall = apiInterface.getPerfilLogin(bearerToken);
//
//        usuarioCall.enqueue(new Callback<List<UsuarioPerfil>>() {
//            @Override
//            public void onResponse(@NonNull Call<List<UsuarioPerfil>> call, @NonNull Response<List<UsuarioPerfil>> response) {
//
//                if (response.isSuccessful()) {
//
//
//                    MetodosUsados.hideLoadingDialog();
//                    if (response.body()!=null){
//                        usuarioPerfil = response.body().get(0);
//                        AppPrefsSettings.getInstance().saveUser(usuarioPerfil);
//
//
//                        launchHomeScreen();
//                    }
//
//                } else {
//                    MetodosUsados.hideLoadingDialog();
//                    AppPrefsSettings.getInstance().clearAppPrefs();
//                }
//
//
//
//
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<List<UsuarioPerfil>> call, @NonNull Throwable t) {
//                AppPrefsSettings.getInstance().clearAppPrefs();
//                MetodosUsados.hideLoadingDialog();
//                if (!MetodosUsados.conexaoInternetTrafego(LoginActivity.this,TAG)){
//                    MetodosUsados.mostrarMensagem(LoginActivity.this,R.string.msg_erro_internet);
//                }else  if ("timeout".equals(t.getMessage())) {
//                    MetodosUsados.mostrarMensagem(LoginActivity.this,R.string.msg_erro_internet_timeout);
//                }else {
//                    MetodosUsados.mostrarMensagem(LoginActivity.this,R.string.msg_erro);
//                }
//
//            }
//        });
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

//        String title = "Olá, ".concat(usuarioPerfil.primeiroNome);
//        String message = "Seja bem-vindo(a) ao ".concat(getString(R.string.app_name));
//        notificationHelper.createNotification(title,message,false);

        finish();
    }
}