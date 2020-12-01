package ao.co.proitconsulting.zoomunitel.activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;

public class AlterarPalavraPasseActivity extends AppCompatActivity implements View.OnClickListener {

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
        dialogLayout_ALTERPASS_INSERT_CODE.show();
        limparEmailTelefone();
        dialogLayout_ALTERPASS_EMAIL_PHONE.cancel();

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


}