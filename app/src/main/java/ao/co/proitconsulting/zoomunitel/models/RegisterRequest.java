package ao.co.proitconsulting.zoomunitel.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegisterRequest implements Serializable {

    @SerializedName("nome")
    public String nome;

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("telefone")
    public String telefone;

}
