package ao.co.proitconsulting.zoomunitel.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginRequest implements Serializable {

    @SerializedName("email")
    public String email_Telefone;

    @SerializedName("password")
    public String password;


}
