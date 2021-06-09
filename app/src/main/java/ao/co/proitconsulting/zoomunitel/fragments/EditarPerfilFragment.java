package ao.co.proitconsulting.zoomunitel.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ao.co.proitconsulting.zoomunitel.Api.ApiClient;
import ao.co.proitconsulting.zoomunitel.Api.ApiInterface;
import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.activities.MainActivity;
import ao.co.proitconsulting.zoomunitel.activities.imagePicker.ImagePickerActivity;
import ao.co.proitconsulting.zoomunitel.helper.Common;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;
import ao.co.proitconsulting.zoomunitel.localDB.AppPrefsSettings;
import ao.co.proitconsulting.zoomunitel.models.UsuarioPerfil;
import ao.co.proitconsulting.zoomunitel.models.UsuarioUpdate;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilFragment extends Fragment {

    private static final String TAG = "TAG_EditFragment";

    public static final int REQUEST_IMAGE = 100;

    private View view;
    private UsuarioPerfil usuarioPerfil;
    private CircleImageView userPhoto;
    private TextView txtUserNameInitial;
    private Drawable nomeDrawable, telefoneDrawable, emailDrawable;
    private AppCompatEditText editNome, editTelefone, editEmail;
    private Button btnSalvarPerfil;
    private Uri selectedImage;
    private String postPath="";
    private String nome,email,telefone;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_editar_perfil, container, false);
        initViews();

        return view;
    }

    private void initViews(){
        FrameLayout frameLayout = ((MainActivity) getActivity()).getFrameLayoutImgToolbar();
        if (frameLayout != null) {
            frameLayout.setVisibility(View.GONE);
        }

        userPhoto = view.findViewById(R.id.userPhoto);
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarPermissaoFotoCameraGaleria();
            }
        });

        txtUserNameInitial = view.findViewById(R.id.txtUserNameInitial);
        editNome = view.findViewById(R.id.editNome);
        editTelefone = view.findViewById(R.id.editTelefone);
        editEmail = view.findViewById(R.id.editEmail);
        btnSalvarPerfil = view.findViewById(R.id.btnSalvarPerfil);


        setEditTextTint_Pre_lollipop();

        usuarioPerfil = AppPrefsSettings.getInstance().getUser();
        carregarDadosOffline(usuarioPerfil);


        btnSalvarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificarCampos()){
                    verificarConecxaoInternet();
                }
            }
        });

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setEditTextTint_Pre_lollipop(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            nomeDrawable = getResources().getDrawable(R.drawable.ic_baseline_person_24);
            telefoneDrawable = getResources().getDrawable(R.drawable.ic_baseline_phone_android_24);
            emailDrawable = getResources().getDrawable(R.drawable.ic_baseline_email_24);

            nomeDrawable = DrawableCompat.wrap(nomeDrawable);
            telefoneDrawable = DrawableCompat.wrap(telefoneDrawable);
            emailDrawable = DrawableCompat.wrap(emailDrawable);

            DrawableCompat.setTint(nomeDrawable,getResources().getColor(R.color.orange_unitel));
            DrawableCompat.setTintMode(nomeDrawable, PorterDuff.Mode.SRC_IN);
            editNome.setCompoundDrawablesWithIntrinsicBounds(nomeDrawable,null,null,null);

            DrawableCompat.setTint(telefoneDrawable,getResources().getColor(R.color.orange_unitel));
            DrawableCompat.setTintMode(telefoneDrawable, PorterDuff.Mode.SRC_IN);
            editTelefone.setCompoundDrawablesWithIntrinsicBounds(telefoneDrawable,null,null,null);

            DrawableCompat.setTint(emailDrawable,getResources().getColor(R.color.orange_unitel));
            DrawableCompat.setTintMode(emailDrawable, PorterDuff.Mode.SRC_IN);
            editEmail.setCompoundDrawablesWithIntrinsicBounds(emailDrawable,null,null,null);
        }

    }

    private void carregarDadosOffline(UsuarioPerfil usuarioPerfil) {
        if (usuarioPerfil!=null){
            editNome.setText(usuarioPerfil.userNome);
            editNome.setSelection(editNome.getText().length());

            editTelefone.setText(usuarioPerfil.userTelefone);
            editTelefone.setSelection(editTelefone.getText().length());

            editEmail.setText(usuarioPerfil.userEmail);
            editEmail.setSelection(editEmail.getText().length());


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
                        .into(userPhoto);
            }
        }
    }

    private void verificarPermissaoFotoCameraGaleria() {
        Dexter.withContext(getContext())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getContext(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });

    }

    private void launchCameraIntent() {
        Intent intent = new Intent(getContext(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(getContext(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private boolean verificarCampos() {

        nome = editNome.getText().toString().trim();
        telefone = editTelefone.getText().toString().trim();
        email = editEmail.getText().toString().trim();



        if (nome.isEmpty()){
            editNome.setError(getString(R.string.msg_erro_campo_vazio));
            return false;
        }


        if (nome.matches(".*\\d.*")){
            editNome.setError(getString(R.string.msg_erro_campo_com_letras));
            return false;
        }

        if (nome.length()<2){
            editNome.setError(getString(R.string.msg_erro_min_duas_letras));
            return false;
        }



        if (email.isEmpty()){
            editEmail.setError(getString(R.string.msg_erro_campo_vazio));
            return false;
        }

        if (!MetodosUsados.validarEmail(email)){
            editEmail.setError(getString(R.string.msg_erro_email_invalido));
            return false;
        }


        if (telefone.isEmpty()){
            editTelefone.setError(getString(R.string.msg_erro_campo_vazio));
            return false;
        }

        if (!telefone.matches("9[1-9][0-9]\\d{6}")){
            editTelefone.setError(getString(R.string.msg_erro_num_telefone_invalido));
            return false;
        }


        return true;
    }

    private void verificarConecxaoInternet() {
        if (getActivity()!=null) {
            ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conMgr!=null){
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null) {
                    MetodosUsados.mostrarMensagem(getActivity(), R.string.msg_erro_internet);
                } else {
                    actualizarPerfil();

                }
            }
        }

    }

    private void actualizarPerfil(){
        final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(getContext()).setTheme(R.style.CustomSpotsDialog).build();
        waitingDialog.setMessage(getString(R.string.salvando_dados));
        waitingDialog.setCancelable(false);
        waitingDialog.show();

        UsuarioUpdate usuarioUpdate = new UsuarioUpdate();
        usuarioUpdate.userNome = nome;
        usuarioUpdate.userTelefone = telefone;
        usuarioUpdate.userEmail = email;

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> enviarDadosPerfil = apiInterface.actualizarPerfil(usuarioUpdate);

        enviarDadosPerfil.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    waitingDialog.cancel();
                    // loading profile image from local cache


                    String mensagem="";

                    mostrarMensagemPopUp(getString(R.string.perfil_atualizado_sucesso));



                    try {
                        mensagem = response.body().string();
                        JSONObject jsonObject = new JSONObject(mensagem);
                        mensagem = jsonObject.getString("msg");

                        Log.d(TAG,"ResponseBody: "+mensagem);
                    }catch (JSONException | IOException err){
                        Log.d(TAG, err.toString());
                    }
                    verificarConecxaoNETPerfil();


                } else {
                    waitingDialog.cancel();

                    String responseErrorMsg ="",mensagem ="";

                    try {
                        responseErrorMsg = response.errorBody().string();

                        Log.d(TAG,"Error code: "+response.code()+", ErrorBody msg: "+responseErrorMsg);

                        if (responseErrorMsg.contains("Tunnel")){
                            mostrarMensagemPopUp(getString(R.string.msg_erro_servidor));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                waitingDialog.cancel();
                if (!MetodosUsados.conexaoInternetTrafego(getContext(),TAG)) {
                    MetodosUsados.mostrarMensagem(getContext(), R.string.msg_erro_internet);
                } else if (t.getMessage().contains("timeout")) {
                    MetodosUsados.mostrarMensagem(getContext(), R.string.msg_erro_internet_timeout);
                } else {
                    MetodosUsados.mostrarMensagem(getContext(), R.string.msg_erro_servidor);
                }
                Log.d(TAG, "onFailure" + t.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);


                    selectedImage = uri;
                    postPath = selectedImage.getPath();

                    Log.d("TAG", "Image Get Uri path: " + uri.getPath());
                    Log.d("TAG", "Image Get Uri toString(): " + uri.toString());

//                    // loading profile image from local cache
//                    loadProfile(uri.toString());

//                    salvarFoto(postPath);
                    verificarConecxaoInternetFOTO();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void verificarConecxaoInternetFOTO() {
        if (getActivity()!=null) {
            ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conMgr!=null){
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null) {
                    MetodosUsados.mostrarMensagem(getActivity(), R.string.msg_erro_internet);
                } else {
                    salvarFoto(postPath);

                }
            }
        }

    }

    private void salvarFoto(String postPath) {

        final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(getContext()).setTheme(R.style.CustomSpotsDialog).build();
        waitingDialog.setMessage(getString(R.string.salvando_foto));
        waitingDialog.setCancelable(false);
        waitingDialog.show();

        File file = new File(postPath);

//        String filename  = file.getName();
        String filename  = System.currentTimeMillis() + ".jpg";
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file);
        MultipartBody.Part imagem  = MultipartBody.Part.createFormData("image",filename,requestFile);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> enviarFoto = apiInterface.actualizarFotoPerfil(imagem);

        enviarFoto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    waitingDialog.cancel();

                    mostrarMensagemPopUp(getString(R.string.foto_atualizada_sucesso));
                    // loading profile image from local cache
                    loadProfile(selectedImage.toString());


                } else {
                    waitingDialog.cancel();


                    String responseErrorMsg ="";

                    try {
                        responseErrorMsg = response.errorBody().string();

                        Log.d(TAG,"Error code: "+response.code()+", ErrorBody msg: "+responseErrorMsg);
                        mostrarMensagemPopUp(responseErrorMsg);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                waitingDialog.cancel();
                if (!MetodosUsados.conexaoInternetTrafego(getContext(),TAG)) {
                    MetodosUsados.mostrarMensagem(getContext(), R.string.msg_erro_internet);
                } else if (t.getMessage().contains("timeout")) {
                    MetodosUsados.mostrarMensagem(getContext(), R.string.msg_erro_internet_timeout);
                } else {
                    MetodosUsados.mostrarMensagem(getContext(), R.string.msg_erro_servidor);
                }
                Log.d(TAG, "onFailure" + t.getMessage());
            }
        });

    }

    private void loadProfile(String url) {
//        Log.d(TAG, "Image cache path: " + url);

        txtUserNameInitial.setVisibility(View.GONE);
        Picasso.get().load(url).placeholder(R.drawable.user_placeholder).into(userPhoto);
        verificarConecxaoNETPerfil();

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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            if (getContext()!=null){
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext(),R.style.MyDialogTheme);

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

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.MyDialogTheme);
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

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<UsuarioPerfil>> usuarioCall = apiInterface.get_USER_PROFILE(usuarioPerfil.userId);

        usuarioCall.enqueue(new Callback<List<UsuarioPerfil>>() {
            @Override
            public void onResponse(@NonNull Call<List<UsuarioPerfil>> call, @NonNull Response<List<UsuarioPerfil>> response) {

                if (response.isSuccessful()) {

                    if (response.body()!=null && response.body().size()>0){

                        UsuarioPerfil usuarioPerfil = response.body().get(0);
                        AppPrefsSettings.getInstance().saveUser(usuarioPerfil);

                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<UsuarioPerfil>> call, @NonNull Throwable t) {

            }
        });
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));

        builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });


        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}