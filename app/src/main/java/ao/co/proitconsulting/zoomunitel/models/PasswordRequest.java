package ao.co.proitconsulting.zoomunitel.models;

import com.google.gson.annotations.SerializedName;

public class PasswordRequest {

    @SerializedName("email")
    public String email;

    @SerializedName("codigo")
    public String codigo;

    @SerializedName("novapassword")
    public String novapassword;
}
