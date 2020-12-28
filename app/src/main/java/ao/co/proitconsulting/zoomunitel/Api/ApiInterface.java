package ao.co.proitconsulting.zoomunitel.Api;

import java.util.List;

import ao.co.proitconsulting.zoomunitel.models.LoginRequest;
import ao.co.proitconsulting.zoomunitel.models.RegisterRequest;
import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;
import ao.co.proitconsulting.zoomunitel.models.UsuarioAuth;
import ao.co.proitconsulting.zoomunitel.models.UsuarioPerfil;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface ApiInterface {

    @POST("/signin")
    Call<UsuarioAuth> autenticarCliente(@Body LoginRequest loginRequest);

    @POST("/register")
    Call<ResponseBody> registrarCliente(@Body RegisterRequest registerRequest);

    @Multipart
    @PUT("/user/image")
    Call<ResponseBody> actualizarFotoPerfil(@Part MultipartBody.Part imagem);

    @GET("/user")
    Call<List<UsuarioPerfil>> getAll_USERS_PROFILES();

    @GET("/revista")
    Call<List<RevistaZoOm>> getAllTodasRevistas();



}
