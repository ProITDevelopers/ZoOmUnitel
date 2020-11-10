package ao.co.proitconsulting.zoomunitel.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;

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
//                registrandoUsuario();
                launchHomeScreen();
            }
        }
    }

//    private void registrandoUsuario(){
//
//        MetodosUsados.showLoadingDialog(getString(R.string.msg_register_quase_pronto));
//
//        RegisterRequest registerRequest = new RegisterRequest();
//        registerRequest.nome = nome;
//        registerRequest.telemovel = telefone;
//        registerRequest.email = email;
//        registerRequest.password = senha;
//
//
//        RequestBody nome = RequestBody.create(MediaType.parse("text/plain"),registerRequest.nome);
//        RequestBody telemovel = RequestBody.create(MediaType.parse("text/plain"), registerRequest.telemovel);
//        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), registerRequest.email);
//        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), registerRequest.password);
//
//
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<Void> call = apiInterface.registrarUsuario(nome,telemovel, email,password);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
//                if (response.isSuccessful()){
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            MetodosUsados.hideLoadingDialog();
//
//                            editNome.setText("");
//                            editTelefone.setText("");
//                            editEmail.setText("");
//                            editPass.setText("");
//                            editConfirmPass.setText("");
//
//
//
//
//                            dialog_txtConfirmSucesso.setText(getString(R.string.msg_conta_criada_sucesso));
//                            dialogLayoutSuccess.show();
//
//                        }
//                    },2000);
//
//
//                } else {
//                    ErrorResponce errorResponce = ErrorUtils.parseError(response);
//                    MetodosUsados.hideLoadingDialog();
//
//                    try {
//
//                        Snackbar.make(register_root, errorResponce.getError(), 4000)
//                                .setActionTextColor(Color.WHITE)
//                                .show();
//                    }catch (Exception e){
//                        Log.i(TAG,"autenticacaoVerif snakBar" + e.getMessage());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
//                MetodosUsados.hideLoadingDialog();
//                if (!MetodosUsados.conexaoInternetTrafego(RegisterActivity.this,TAG)){
//                    MetodosUsados.mostrarMensagem(RegisterActivity.this,R.string.msg_erro_internet);
//                }else  if ("timeout".equals(t.getMessage())) {
//                    MetodosUsados.mostrarMensagem(RegisterActivity.this,R.string.msg_erro_internet_timeout);
//                }else {
//                    MetodosUsados.mostrarMensagem(RegisterActivity.this,R.string.msg_erro);
//                }
//                Log.i(TAG,"onFailure" + t.getMessage());
//
//            }
//        });
//    }

    private void launchHomeScreen() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}