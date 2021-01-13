package ao.co.proitconsulting.zoomunitel.activities;



import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.graphics.drawable.DrawableCompat;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ao.co.proitconsulting.zoomunitel.Api.ApiClient;
import ao.co.proitconsulting.zoomunitel.Api.ApiInterface;
import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;
import ao.co.proitconsulting.zoomunitel.localDB.AppPrefsSettings;
import ao.co.proitconsulting.zoomunitel.models.PasswordRequest;
import ao.co.proitconsulting.zoomunitel.models.RegisterRequest;
import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlterarPalavraPasseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TAG_PassWActivity";

    //DIALOG_LAYOUT_ALTERAR_PASS_ALTER_PASS_OPTIONS
    private Dialog dialogLayout_ALTERPASS_OPTIONS;
    private ImageView imgBtnFecharTelef;
    private TextView dialog_txt_email,dialog_txt_telefone;
//-------------------------------------------------------------//
//-------------------------------------------------------------//
    //DIALOG_LAYOUT_ALTERAR_PASS_EMAIL_PHONE
    private Dialog dialogLayout_ALTERPASS_EMAIL_PHONE;
    private TextView dialog_txt_insert_value;
    private RelativeLayout dialog_relative_email,dialog_relative_telefone;
    private AppCompatEditText dialog_EditEmail,dialog_EditTelefone;
    private Button dialog_btn_cancelar_emailPhone,dialog_btn_continuar_emailPhone;
//-------------------------------------------------------------//
//-------------------------------------------------------------//
    //DIALOG_LAYOUT_ALTERAR_PASS_INSERT_CODE
    private Dialog dialogLayout_ALTERPASS_INSERT_CODE;
    private TextView txtTenteOutraVez, txt_EmailTelefone_Result;
    private ShowHidePasswordEditText dialog_EditConfirmCode;
    private Button dialog_btn_cancelar_InsertCode,dialog_btn_confirmar_InsertCode;
//-------------------------------------------------------------//
//-------------------------------------------------------------//
    //DIALOG_LAYOUT_ALTERAR_PASS_NEWPASS
    private Dialog dialogLayout_ALTERPASS_NEWPASS;
    private ShowHidePasswordEditText dialog_EditPassword,dialog_editConfirmPassword;
    private Button dialog_btn_cancelar_NewPass,dialog_btn_enviar_NewPass;
//-------------------------------------------------------------//
//-------------------------------------------------------------//
    //DIALOG_LAYOUT_SUCESSO
    private Dialog dialogLayout_SUCESSO;
    private TextView dialog_txtConfirmSucesso;
    private ImageView imgStatusSuccess;
    private Button dialog_btn_sucesso;
//-------------------------------------------------------------//
//-------------------------------------------------------------//
    private String email,telefone,emailPhoneValue,confirmCode,senha,confirmSenha;

    private Drawable dialog_EditEmailDrawable, dialog_EditTelefoneDrawable, dialog_EditConfirmCodeDrawable;
    private Drawable dialog_EditPasswordDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MetodosUsados.showCustomUI(this);
        setContentView(R.layout.activity_alterar_palavra_passe);
        initViews();
    }

    private void initViews() {
        //-------------------------------------------------------------//
        //-------------------------------------------------------------//
        //DIALOG_LAYOUT_ALTERAR_PASS_ALTER_PASS_OPTIONS
        dialogLayout_ALTERPASS_OPTIONS = new Dialog(this);
        dialogLayout_ALTERPASS_OPTIONS.setContentView(R.layout.layout_alterarpass_options);
        dialogLayout_ALTERPASS_OPTIONS.setCancelable(false);
        if (dialogLayout_ALTERPASS_OPTIONS.getWindow()!=null)
            dialogLayout_ALTERPASS_OPTIONS.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        imgBtnFecharTelef = dialogLayout_ALTERPASS_OPTIONS.findViewById(R.id.imgBtnFecharTelef);
        dialog_txt_email = dialogLayout_ALTERPASS_OPTIONS.findViewById(R.id.dialog_txt_email);
        dialog_txt_telefone = dialogLayout_ALTERPASS_OPTIONS.findViewById(R.id.dialog_txt_telefone);

        imgBtnFecharTelef.setOnClickListener(this);
        dialog_txt_email.setOnClickListener(this);
        dialog_txt_telefone.setOnClickListener(this);

        dialogLayout_ALTERPASS_OPTIONS.show();

        //-------------------------------------------------------------//
        //-------------------------------------------------------------//
        //DIALOG_LAYOUT_ALTERAR_PASS_EMAIL_PHONE
        dialogLayout_ALTERPASS_EMAIL_PHONE = new Dialog(this);
        dialogLayout_ALTERPASS_EMAIL_PHONE.setContentView(R.layout.layout_alterarpass_email_telefone);
        dialogLayout_ALTERPASS_EMAIL_PHONE.setCancelable(false);
        if (dialogLayout_ALTERPASS_EMAIL_PHONE.getWindow()!=null)
            dialogLayout_ALTERPASS_EMAIL_PHONE.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog_txt_insert_value = dialogLayout_ALTERPASS_EMAIL_PHONE.findViewById(R.id.dialog_txt_insert_value);
        dialog_relative_email = dialogLayout_ALTERPASS_EMAIL_PHONE.findViewById(R.id.dialog_relative_email);
        dialog_relative_telefone = dialogLayout_ALTERPASS_EMAIL_PHONE.findViewById(R.id.dialog_relative_telefone);
        dialog_EditEmail = dialogLayout_ALTERPASS_EMAIL_PHONE.findViewById(R.id.dialog_EditEmail);
        dialog_EditTelefone = dialogLayout_ALTERPASS_EMAIL_PHONE.findViewById(R.id.dialog_EditTelefone);
        dialog_btn_cancelar_emailPhone = dialogLayout_ALTERPASS_EMAIL_PHONE.findViewById(R.id.dialog_btn_cancelar_emailPhone);
        dialog_btn_continuar_emailPhone = dialogLayout_ALTERPASS_EMAIL_PHONE.findViewById(R.id.dialog_btn_continuar_emailPhone);

        dialog_btn_cancelar_emailPhone.setOnClickListener(this);
        dialog_btn_continuar_emailPhone.setOnClickListener(this);



        //-------------------------------------------------------------//
        //-------------------------------------------------------------//
        //DIALOG_LAYOUT_ALTERAR_PASS_INSERT_CODE
        dialogLayout_ALTERPASS_INSERT_CODE = new Dialog(this);
        dialogLayout_ALTERPASS_INSERT_CODE.setContentView(R.layout.layout_alterarpass_insert_code);
        dialogLayout_ALTERPASS_INSERT_CODE.setCancelable(false);
        if (dialogLayout_ALTERPASS_INSERT_CODE.getWindow()!=null)
            dialogLayout_ALTERPASS_INSERT_CODE.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        txtTenteOutraVez = dialogLayout_ALTERPASS_INSERT_CODE.findViewById(R.id.txtTenteOutraVez);
        txt_EmailTelefone_Result = dialogLayout_ALTERPASS_INSERT_CODE.findViewById(R.id.txt_EmailTelefone_Result);
        dialog_EditConfirmCode = dialogLayout_ALTERPASS_INSERT_CODE.findViewById(R.id.dialog_EditConfirmCode);
        dialog_btn_cancelar_InsertCode = dialogLayout_ALTERPASS_INSERT_CODE.findViewById(R.id.dialog_btn_cancelar_InsertCode);
        dialog_btn_confirmar_InsertCode = dialogLayout_ALTERPASS_INSERT_CODE.findViewById(R.id.dialog_btn_confirmar_InsertCode);


        dialog_btn_cancelar_InsertCode.setOnClickListener(this);
        dialog_btn_confirmar_InsertCode.setOnClickListener(this);
        //-------------------------------------------------------------//
        //-------------------------------------------------------------//
        //DIALOG_LAYOUT_ALTERAR_PASS_NEWPASS
        dialogLayout_ALTERPASS_NEWPASS = new Dialog(this);
        dialogLayout_ALTERPASS_NEWPASS.setContentView(R.layout.layout_alterarpass_newpass);
        dialogLayout_ALTERPASS_NEWPASS.setCancelable(false);
        if (dialogLayout_ALTERPASS_NEWPASS.getWindow()!=null)
            dialogLayout_ALTERPASS_NEWPASS.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog_EditPassword = dialogLayout_ALTERPASS_NEWPASS.findViewById(R.id.dialog_EditPassword);
        dialog_editConfirmPassword = dialogLayout_ALTERPASS_NEWPASS.findViewById(R.id.dialog_editConfirmPassword);
        dialog_btn_cancelar_NewPass = dialogLayout_ALTERPASS_NEWPASS.findViewById(R.id.dialog_btn_cancelar_NewPass);
        dialog_btn_enviar_NewPass = dialogLayout_ALTERPASS_NEWPASS.findViewById(R.id.dialog_btn_enviar_NewPass);


        dialog_btn_cancelar_NewPass.setOnClickListener(this);
        dialog_btn_enviar_NewPass.setOnClickListener(this);
        //-------------------------------------------------------------//
        //-------------------------------------------------------------//
        //DIALOG_LAYOUT_SUCESSO
        dialogLayout_SUCESSO = new Dialog(this);
        dialogLayout_SUCESSO.setContentView(R.layout.layout_sucesso);
        dialogLayout_SUCESSO.setCancelable(false);
        if (dialogLayout_SUCESSO.getWindow()!=null)
            dialogLayout_SUCESSO.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog_txtConfirmSucesso = dialogLayout_SUCESSO.findViewById(R.id.dialog_txtConfirmSucesso);
        imgStatusSuccess = dialogLayout_SUCESSO.findViewById(R.id.imgStatusSuccess);
        dialog_btn_sucesso = dialogLayout_SUCESSO.findViewById(R.id.dialog_btn_sucesso);



        dialog_btn_sucesso.setOnClickListener(this);

        //-------------------------------------------------------------//
        //-------------------------------------------------------------//
        setEditTextTint_Pre_lollipop();

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setEditTextTint_Pre_lollipop(){
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {


            dialog_EditEmailDrawable = getResources().getDrawable(R.drawable.ic_baseline_email_24);
            dialog_EditTelefoneDrawable = getResources().getDrawable(R.drawable.ic_baseline_phone_android_24);
            dialog_EditConfirmCodeDrawable = getResources().getDrawable(R.drawable.ic_baseline_phonelink_lock_24);
            dialog_EditPasswordDrawable = getResources().getDrawable(R.drawable.ic_baseline_lock_24);

            dialog_EditEmailDrawable = DrawableCompat.wrap(dialog_EditEmailDrawable);
            dialog_EditTelefoneDrawable = DrawableCompat.wrap(dialog_EditTelefoneDrawable);
            dialog_EditConfirmCodeDrawable = DrawableCompat.wrap(dialog_EditConfirmCodeDrawable);
            dialog_EditPasswordDrawable = DrawableCompat.wrap(dialog_EditPasswordDrawable);

            DrawableCompat.setTint(dialog_EditEmailDrawable,getResources().getColor(R.color.orange_unitel));
            DrawableCompat.setTintMode(dialog_EditEmailDrawable, PorterDuff.Mode.SRC_IN);

            DrawableCompat.setTint(dialog_EditTelefoneDrawable,getResources().getColor(R.color.orange_unitel));
            DrawableCompat.setTintMode(dialog_EditTelefoneDrawable, PorterDuff.Mode.SRC_IN);

            DrawableCompat.setTint(dialog_EditConfirmCodeDrawable,getResources().getColor(R.color.orange_unitel));
            DrawableCompat.setTintMode(dialog_EditConfirmCodeDrawable, PorterDuff.Mode.SRC_IN);

            DrawableCompat.setTint(dialog_EditPasswordDrawable,getResources().getColor(R.color.orange_unitel));
            DrawableCompat.setTintMode(dialog_EditPasswordDrawable, PorterDuff.Mode.SRC_IN);

            dialog_EditEmail.setCompoundDrawablesWithIntrinsicBounds(dialog_EditEmailDrawable,null,null,null);
            dialog_EditTelefone.setCompoundDrawablesWithIntrinsicBounds(dialog_EditTelefoneDrawable,null,null,null);
            dialog_EditConfirmCode.setCompoundDrawablesWithIntrinsicBounds(dialog_EditConfirmCodeDrawable,null,null,null);
            dialog_EditPassword.setCompoundDrawablesWithIntrinsicBounds(dialog_EditPasswordDrawable,null,null,null);
            dialog_editConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(dialog_EditPasswordDrawable,null,null,null);
        }

    }

    @Override
    public void onClick(View v) {

        String txtmessage1="Insira o seu ";
        String txtmessage2 ="";
        String txtmessage3=" para alterar a sua palavra-passe.";
        String message="";
        switch (v.getId()) {
            //DIALOG_LAYOUT_ALTERAR_PASS_ALTER_PASS_OPTIONS
            case R.id.imgBtnFecharTelef:
                dialogLayout_ALTERPASS_OPTIONS.cancel();
                finish();
                break;

            case R.id.dialog_txt_email:
                emailPhoneValue = dialog_txt_email.getText().toString();
                txtmessage2 = emailPhoneValue;
                message = txtmessage1+txtmessage2+txtmessage3;

                dialog_txt_insert_value.setText(message);
                dialog_relative_email.setVisibility(View.VISIBLE);
                dialog_relative_telefone.setVisibility(View.GONE);
                dialogLayout_ALTERPASS_EMAIL_PHONE.show();
                dialogLayout_ALTERPASS_OPTIONS.cancel();
                break;

            case R.id.dialog_txt_telefone:
                emailPhoneValue = dialog_txt_telefone.getText().toString();
                txtmessage2 = "nº de "+emailPhoneValue;
                message = txtmessage1+txtmessage2+txtmessage3;
                dialog_txt_insert_value.setText(message);

                dialog_relative_telefone.setVisibility(View.VISIBLE);
                dialog_relative_email.setVisibility(View.GONE);
                dialogLayout_ALTERPASS_EMAIL_PHONE.show();
                dialogLayout_ALTERPASS_OPTIONS.cancel();
                break;
//-------------------------------------------------------------//
//-------------------------------------------------------------//
            //DIALOG_LAYOUT_ALTERAR_PASS_EMAIL_PHONE
            case R.id.dialog_btn_cancelar_emailPhone:
                dialogLayout_ALTERPASS_OPTIONS.show();
                dialogLayout_ALTERPASS_EMAIL_PHONE.cancel();
                break;

            case R.id.dialog_btn_continuar_emailPhone:

                if (emailPhoneValue.equals("Email")){
                    if (verificarCampoEmail()){
                        verificarConecxaoInternet(emailPhoneValue);
                    }
                }else{
                    if (verificarCampoTelefone()){
                        verificarConecxaoInternet(emailPhoneValue);
                    }
                }
                break;
//-------------------------------------------------------------//
//-------------------------------------------------------------//
            //DIALOG_LAYOUT_ALTERAR_PASS_INSERT_CODE
            case R.id.dialog_btn_cancelar_InsertCode:
                dialogLayout_ALTERPASS_EMAIL_PHONE.show();
                dialogLayout_ALTERPASS_INSERT_CODE.cancel();
                break;

            case R.id.dialog_btn_confirmar_InsertCode:
                if (verificarCampoConfimCode()){
                    verificarConecxaoInternetConfirmCode();
                }
                break;
//-------------------------------------------------------------//
//-------------------------------------------------------------//
            //DIALOG_LAYOUT_ALTERAR_PASS_NEWPASS
            case R.id.dialog_btn_cancelar_NewPass:
                dialogLayout_ALTERPASS_INSERT_CODE.show();
                dialogLayout_ALTERPASS_NEWPASS.cancel();
                break;

            case R.id.dialog_btn_enviar_NewPass:

                if (verificarCampoSenha()){
                    verificarConecxaoInternetSenha();
                }
                break;
//-------------------------------------------------------------//
//-------------------------------------------------------------//
            //DIALOG_LAYOUT_SUCESSO
            case R.id.dialog_btn_sucesso:
                dialogLayout_SUCESSO.cancel();
                finish();
                break;
        }
    }



    private boolean verificarCampoEmail() {
        email = dialog_EditEmail.getText().toString().trim();

        if (email.isEmpty()){
            dialog_EditEmail.setError(getString(R.string.msg_erro_campo_vazio));
            return false;
        }

        if (!MetodosUsados.validarEmail(email)){
            dialog_EditEmail.setError(getString(R.string.msg_erro_email_invalido));
            return false;
        }

        return true;
    }

    private boolean verificarCampoTelefone() {
        telefone = dialog_EditTelefone.getText().toString().trim();

        if (telefone.isEmpty()){
            dialog_EditTelefone.setError(getString(R.string.msg_erro_campo_vazio));
            return false;
        }

        if (!telefone.matches("9[1-9][0-9]\\d{6}")){
            dialog_EditTelefone.setError(getString(R.string.msg_erro_num_telefone_invalido));
            return false;
        }

        return true;
    }

    private void verificarConecxaoInternet(String emailPhoneValue) {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr!=null){
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null) {
                MetodosUsados.mostrarMensagem(this,R.string.msg_erro_internet);
            } else {

                if (emailPhoneValue.equals("Email")){
                    enviarEmail();
                }else{
                    enviarNumTelefone();
                }
            }
        }
    }



    private void enviarEmail() {

        final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(this).build();
        waitingDialog.setMessage(getString(R.string.msg_pass_send_email));
        waitingDialog.setCancelable(false);
        waitingDialog.show();


        PasswordRequest passwordRequest = new PasswordRequest();
        passwordRequest.email = email;


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.sendUserEmail(passwordRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
                if (response.isSuccessful()){

                    waitingDialog.dismiss();
                    waitingDialog.cancel();

                    dialogLayout_ALTERPASS_INSERT_CODE.show();
                    limparEmailTelefone();
                    dialogLayout_ALTERPASS_EMAIL_PHONE.cancel();

                } else {

                    waitingDialog.dismiss();
                    waitingDialog.cancel();

                    String responseErrorMsg ="",mensagem ="";

                    try {
                        responseErrorMsg = response.errorBody().string();

                        Log.v(TAG,"Error code: "+response.code()+", ErrorBody msg: "+responseErrorMsg);

                        if (responseErrorMsg.contains("Tunnel")){
                            mostrarMensagemPopUp(getString(R.string.msg_erro_servidor));
                        }else{
                            JSONObject jsonObject = new JSONObject(responseErrorMsg);

                            mensagem = jsonObject.getJSONObject("erro").getString("mensagem");

                            mostrarMensagemPopUp(mensagem);
                        }



                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (JSONException err){
                        Log.v(TAG, err.toString());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                waitingDialog.dismiss();
                waitingDialog.cancel();
                Log.i(TAG,"onFailure" + t.getMessage());

            }
        });

    }

    private void enviarNumTelefone() {
        dialogLayout_ALTERPASS_INSERT_CODE.show();
        limparEmailTelefone();
        dialogLayout_ALTERPASS_EMAIL_PHONE.cancel();
    }

    private void limparEmailTelefone(){
        dialog_EditEmail.setText(null);
        dialog_EditTelefone.setText(null);
        dialog_EditEmail.setError(null);
        dialog_EditTelefone.setError(null);
    }

    private boolean verificarCampoConfimCode() {
        confirmCode = dialog_EditConfirmCode.getText().toString().trim();

        if (confirmCode.isEmpty()){
            dialog_EditConfirmCode.setError(getString(R.string.msg_erro_campo_vazio));
            return false;
        }

        return true;
    }

    private void verificarConecxaoInternetConfirmCode() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr!=null){
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null) {
                MetodosUsados.mostrarMensagem(this,R.string.msg_erro_internet);
            } else {
                enviarConfirmCode();
            }
        }
    }

    private void enviarConfirmCode() {
        dialogLayout_ALTERPASS_NEWPASS.show();
        dialog_EditConfirmCode.setText(null);
        dialog_EditConfirmCode.setError(null);
        dialogLayout_ALTERPASS_INSERT_CODE.cancel();
    }

    private boolean verificarCampoSenha() {
        senha = dialog_EditPassword.getText().toString().trim();
        confirmSenha = dialog_editConfirmPassword.getText().toString().trim();

        if (senha.isEmpty()){
            dialog_EditPassword.setError(getString(R.string.msg_erro_campo_vazio));
            return false;
        }

        if (senha.length()<=5){
            dialog_EditPassword.setError(getString(R.string.msg_erro_password_fraca));
            return false;
        }

        if (!confirmSenha.equals(senha)){
            dialog_editConfirmPassword.setError(getString(R.string.msg_erro_password_diferentes));
            return false;
        }

        return true;
    }

    private void verificarConecxaoInternetSenha() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr!=null){
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null) {
                MetodosUsados.mostrarMensagem(this,R.string.msg_erro_internet);
            } else {
                enviarNovaSenha();
            }
        }
    }

    private void enviarNovaSenha() {
        imgStatusSuccess.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
        dialogLayout_SUCESSO.show();
        dialog_EditPassword.setText(null);
        dialog_editConfirmPassword.setText(null);
        dialog_EditPassword.setError(null);
        dialog_editConfirmPassword.setError(null);
        dialogLayout_ALTERPASS_NEWPASS.cancel();
    }

    private void verificarConecxao_SENDEMAIL() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr!=null){
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null) {
                MetodosUsados.mostrarMensagem(this,R.string.msg_erro_internet);
            } else {
//                enviarEmail();

            }
        }
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

            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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


}