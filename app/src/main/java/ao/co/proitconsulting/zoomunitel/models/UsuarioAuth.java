package ao.co.proitconsulting.zoomunitel.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UsuarioAuth implements Serializable {

    @SerializedName("userid")
    public int userId;

    @SerializedName("nome")
    public String userName;

    @SerializedName("email")
    public String userEmail;

    @SerializedName("token")
    public String userToken;






}
