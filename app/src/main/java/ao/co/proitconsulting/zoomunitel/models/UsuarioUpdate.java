package ao.co.proitconsulting.zoomunitel.models;

import com.google.gson.annotations.SerializedName;

public class UsuarioUpdate {

    @SerializedName("nome")
    public String userNome;

    @SerializedName("telefone")
    public String userTelefone;

    @SerializedName("email")
    public String userEmail;

}
