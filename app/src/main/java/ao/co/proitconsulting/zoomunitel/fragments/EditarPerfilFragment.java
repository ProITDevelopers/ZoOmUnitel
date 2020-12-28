package ao.co.proitconsulting.zoomunitel.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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
import ao.co.proitconsulting.zoomunitel.activities.RegisterActivity;
import ao.co.proitconsulting.zoomunitel.activities.imagePicker.ImagePickerActivity;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;
import ao.co.proitconsulting.zoomunitel.localDB.AppPrefsSettings;
import ao.co.proitconsulting.zoomunitel.models.UsuarioPerfil;
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

    private static final String TAG = "TAG_EditPerf_Fragment";

    public static final int REQUEST_IMAGE = 100;

    private View view;
    private UsuarioPerfil usuarioPerfil;
    private CircleImageView userPhoto;
    private TextView txtUserNameInitial;
    private AppCompatEditText editNome, editTelefone, editEmail;
    private Button btnSalvarPerfil;
    private Uri selectedImage;
    private String postPath="";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_editar_perfil, container, false);
        initViews();

        return view;
    }

    private void initViews(){
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
        btnSalvarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Salvando perfil...", Toast.LENGTH_SHORT).show();
            }
        });

        usuarioPerfil = AppPrefsSettings.getInstance().getUser();
        carregarDadosOffline(usuarioPerfil);

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
                        .load(usuarioPerfil.userPhoto)
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

                    salvarFoto(postPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void salvarFoto(String postPath) {

        final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(getContext()).build();
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
                    // loading profile image from local cache
                    loadProfile(selectedImage.toString());


                    String errorMessage="", mensagem="";


                    try {
                        errorMessage = response.body().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        mensagem = jsonObject.getString("msg");

                        MetodosUsados.mostrarMensagem(getContext(),mensagem);
                        mostrarMensagemPopUp(mensagem);

                        Log.v(TAG,"ResponseBody: "+errorMessage);


                    }catch (JSONException | IOException err){
                        Log.v(TAG, err.toString());
                    }




                } else {
                    waitingDialog.cancel();

                    String responseErrorMsg ="",mensagem ="";

                    try {
                        responseErrorMsg = response.errorBody().string();

                        Log.v(TAG,"Error code: "+response.code()+", ErrorBody msg: "+responseErrorMsg);

                        JSONObject jsonObject = new JSONObject(responseErrorMsg);

                        mensagem = jsonObject.getJSONObject("erro").getString("mensagem");

//                        MetodosUsados.mostrarMensagem(LoginActivity.this,mensagem);
                        mostrarMensagemPopUp(mensagem);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (JSONException err){
                        Log.v(TAG, err.toString());
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                waitingDialog.cancel();
                if (!MetodosUsados.conexaoInternetTrafego(getContext(),TAG)) {
                    MetodosUsados.mostrarMensagem(getContext(), R.string.msg_erro_internet);
                } else if ("timeout".equals(t.getMessage())) {
                    MetodosUsados.mostrarMensagem(getContext(), R.string.msg_erro_internet_timeout);
                } else {
                    MetodosUsados.mostrarMensagem(getContext(), R.string.msg_erro);
                }
                Log.i(TAG, "onFailure" + t.getMessage());
            }
        });

    }

    private void loadProfile(String url) {
//        Log.d(TAG, "Image cache path: " + url);

        txtUserNameInitial.setVisibility(View.GONE);
        Picasso.get().load(url).placeholder(R.drawable.user_placeholder).into(userPhoto);

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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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