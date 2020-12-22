package ao.co.proitconsulting.zoomunitel.Api;

import java.util.List;

import ao.co.proitconsulting.zoomunitel.models.LoginRequest;
import ao.co.proitconsulting.zoomunitel.models.RegisterRequest;
import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;
import ao.co.proitconsulting.zoomunitel.models.UsuarioAuth;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("/signin")
    Call<UsuarioAuth> autenticarCliente(@Body LoginRequest loginRequest);

    @POST("/register")
    Call<ResponseBody> registrarCliente(@Body RegisterRequest registerRequest);

    @GET("/revista")
    Call<List<RevistaZoOm>> getAllTodasRevistas();



}
