package ao.co.proitconsulting.zoomunitel.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ao.co.proitconsulting.zoomunitel.Api.ApiClient;
import ao.co.proitconsulting.zoomunitel.Api.ApiInterface;
import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;
import ao.co.proitconsulting.zoomunitel.localDB.AppPrefsSettings;
import ao.co.proitconsulting.zoomunitel.models.LoginRequest;
import ao.co.proitconsulting.zoomunitel.models.RegisterRequest;
import ao.co.proitconsulting.zoomunitel.models.UsuarioAuth;
import ao.co.proitconsulting.zoomunitel.models.UsuarioPerfil;
import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "TAG_RegisterActivity";
    private AppCompatEditText editNome,editTelefone,editEmail;
    private ShowHidePasswordEditText editPass,editConfirmPass;
    private Button btnRegistro;
    private TextView txtCancelar;

    private String nome,email,telefone,senha,confirmSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
    }

    private void initViews() {

        editNome = findViewById(R.id.editNome);
        editTelefone = findViewById(R.id.editTelefone);
        editEmail = findViewById(R.id.editEmail);
        editPass = findViewById(R.id.editPass);
        editConfirmPass = findViewById(R.id.editConfirmPass);

        btnRegistro = findViewById(R.id.btnRegistro);
        txtCancelar = findViewById(R.id.txtCancelar);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarCampos()){
                    verificarConecxaoInternet();
                }
            }
        });
        txtCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean verificarCampos() {

        nome = editNome.getText().toString().trim();
        email = editEmail.getText().toString().trim();
        telefone = editTelefone.getText().toString().trim();
        senha = editPass.getText().toString().trim();
        confirmSenha = editConfirmPass.getText().toString().trim();



        if (nome.isEmpty()){
            editNome.setError(getString(R.string.msg_erro_campo_vazio));
//            MetodosUsados.mostrarMensagem(this,"Preencha o campo: 'Nome'");
            return false;
        }

//        if (!primeiroNome.matches("^[a-zA-Z\\s]+$")){
        if (nome.matches(".*\\d.*")){
            editNome.setError(getString(R.string.msg_erro_campo_com_letras));
//            MetodosUsados.mostrarMensagem(this,"Verifique o campo: 'Nome'");
            return false;
        }

        if (nome.length()<2){
            editNome.setError(getString(R.string.msg_erro_min_duas_letras));
//            MetodosUsados.mostrarMensagem(this,"'Nome', duas letras no mínimo");
            return false;
        }



        if (email.isEmpty()){
            editEmail.setError(getString(R.string.msg_erro_campo_vazio));
//            MetodosUsados.mostrarMensagem(this,"Preencha o campo: 'Email'");
            return false;
        }

        if (!MetodosUsados.validarEmail(email)){
            editEmail.setError(getString(R.string.msg_erro_email_invalido));
//            MetodosUsados.mostrarMensagem(this,"'Email' inválido");
            return false;
        }


        if (telefone.isEmpty()){
            editTelefone.setError(getString(R.string.msg_erro_campo_vazio));
//            MetodosUsados.mostrarMensagem(this,"Preencha o campo: 'Telemóvel'");
            return false;
        }

        if (!telefone.matches("9[1-9][0-9]\\d{6}")){
            editTelefone.setError(getString(R.string.msg_erro_num_telefone_invalido));
//            MetodosUsados.mostrarMensagem(this,"'Telemóvel' inválido");
            return false;
        }



        if (senha.isEmpty()){
            editPass.setError(getString(R.string.msg_erro_campo_vazio));
            MetodosUsados.mostrarMensagem(this,"Preencha o campo: 'Senha'");
            return false;
        }

        if (senha.length()<=5){
            editPass.setError(getString(R.string.msg_erro_password_fraca));
            MetodosUsados.mostrarMensagem(this,"'Senha' fraca");
            return false;
        }

        if (!confirmSenha.equals(senha)){
            editConfirmPass.setError(getString(R.string.msg_erro_password_diferentes));
            MetodosUsados.mostrarMensagem(this,"'Senha' fraca");
            return false;
        }



        return true;
    }

    private void verificarConecxaoInternet() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr!=null){
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null) {
                MetodosUsados.mostrarMensagem(this,R.string.msg_erro_internet);
            } else {
                registrandoUsuario();

            }
        }
    }

    private void registrandoUsuario(){

        MetodosUsados.showLoadingDialog(getString(R.string.msg_register_enviando_dados));

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.nome = nome;
        registerRequest.telefone = telefone;
        registerRequest.email = email;
        registerRequest.password = senha;

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.registrarCliente(registerRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
                if (response.isSuccessful()){

                    editNome.setText("");
                    editTelefone.setText("");
                    editEmail.setText("");
                    editPass.setText("");
                    editConfirmPass.setText("");

                    MetodosUsados.hideLoadingDialog();
                    String errorMessage="", mensagem="";


                    try {
                        errorMessage = response.body().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        mensagem = jsonObject.getString("msg");

                        MetodosUsados.mostrarMensagem(RegisterActivity.this,mensagem);
                        mostrarMensagemPopUp(mensagem,"true");

                        Log.v(TAG,"ResponseBody: "+errorMessage);


                    }catch (JSONException | IOException err){
                        Log.v(TAG, err.toString());
                    }


                } else {

                    MetodosUsados.hideLoadingDialog();

                    String responseErrorMsg ="",mensagem ="";

                    try {
                        responseErrorMsg = response.errorBody().string();

                        Log.v(TAG,"Error code: "+response.code()+", ErrorBody msg: "+responseErrorMsg);

                        JSONObject jsonObject = new JSONObject(responseErrorMsg);

                        mensagem = jsonObject.getJSONObject("erro").getString("mensagem");

//                        MetodosUsados.mostrarMensagem(LoginActivity.this,mensagem);
                        mostrarMensagemPopUp(mensagem,"false");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (JSONException err){
                        Log.v(TAG, err.toString());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                MetodosUsados.hideLoadingDialog();
                if (!MetodosUsados.conexaoInternetTrafego(RegisterActivity.this,TAG)){
                    MetodosUsados.mostrarMensagem(RegisterActivity.this,R.string.msg_erro_internet);
                }else  if ("timeout".equals(t.getMessage())) {
                    MetodosUsados.mostrarMensagem(RegisterActivity.this,R.string.msg_erro_internet_timeout);
                }else {
                    MetodosUsados.mostrarMensagem(RegisterActivity.this,R.string.msg_erro);
                }
                Log.i(TAG,"onFailure" + t.getMessage());

            }
        });
    }

    private void autenticacaoLogin() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.email_Telefone = email;
        loginRequest.password = senha;

        final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(this).build();
        waitingDialog.setMessage(getString(R.string.msg_register_quase_pronto));
        waitingDialog.setCancelable(false);
        waitingDialog.show();


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<UsuarioAuth> call = apiInterface.autenticarCliente(loginRequest);
        call.enqueue(new Callback<UsuarioAuth>() {
            @Override
            public void onResponse(@NonNull Call<UsuarioAuth> call, @NonNull Response<UsuarioAuth> response) {


                if (response.isSuccessful() && response.body() != null) {

                    waitingDialog.dismiss();
                    waitingDialog.cancel();

                    UsuarioAuth usuarioAuth = response.body();

                    UsuarioPerfil usuarioPerfil = new UsuarioPerfil(
                            usuarioAuth.userId,
                            usuarioAuth.userName,
                            usuarioAuth.userEmail);

                    AppPrefsSettings.getInstance().saveUser(usuarioPerfil);

                    AppPrefsSettings.getInstance().saveAuthToken(usuarioAuth.userToken);



                    launchHomeScreen();


                }
            }

            @Override
            public void onFailure(@NonNull Call<UsuarioAuth> call, @NonNull Throwable t) {
                waitingDialog.dismiss();
                waitingDialog.cancel();
                if (!MetodosUsados.conexaoInternetTrafego(RegisterActivity.this,TAG)){
                    MetodosUsados.mostrarMensagem(RegisterActivity.this,R.string.msg_erro_internet);
                }else  if ("timeout".equals(t.getMessage())) {
                    MetodosUsados.mostrarMensagem(RegisterActivity.this,R.string.msg_erro_internet_timeout);
                }else {
                    MetodosUsados.mostrarMensagem(RegisterActivity.this,R.string.msg_erro);
                }
                Log.v(TAG,"onFailure" + t.getMessage());

            }
        });

    }

    private void launchHomeScreen() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void mostrarMensagemPopUp(String msg, final String status) {
        SpannableString title = new SpannableString(getString(R.string.app_name));
        title.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.orange_unitel)),
                0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString message = new SpannableString(msg);
        message.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.blue_unitel)),
                0, message.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString ok = new SpannableString(getString(R.string.text_ok));
        ok.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.orange_unitel)),
                0, ok.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (status.equals("true")){
                    autenticacaoLogin();
                }
            }
        });

        builder.show();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        super.onBackPressed();
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