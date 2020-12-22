package ao.co.proitconsulting.zoomunitel.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UsuarioPerfil implements Serializable {

    @SerializedName("userid")
    public int userId;

    @SerializedName("nomeCompleto")
    public String userNome;

    @SerializedName("email")
    public String userEmail;

    @SerializedName("telefone")
    public String userTelefone;


    @SerializedName("imagem")
    public String userPhoto;



    public UsuarioPerfil() {
    }

    public UsuarioPerfil(int userId, String userNome, String userEmail) {
        this.userId = userId;
        this.userNome = userNome;
        this.userEmail = userEmail;

    }

    public UsuarioPerfil(int userId, String userNome, String userEmail, String userTelefone, String userPhoto) {
        this.userId = userId;
        this.userNome = userNome;
        this.userEmail = userEmail;
        this.userTelefone = userTelefone;
        this.userPhoto = userPhoto;
    }
}
