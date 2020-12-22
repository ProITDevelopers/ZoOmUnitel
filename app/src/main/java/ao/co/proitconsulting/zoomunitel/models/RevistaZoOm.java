package ao.co.proitconsulting.zoomunitel.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RevistaZoOm implements Serializable {

    @SerializedName("IDREVISTA")
    private int id;

    @SerializedName("NOME")
    private String title;

    @SerializedName("DESCRICAO")
    private String descricao;

    @SerializedName("FOTOKEY")
    private String imagem;

    @SerializedName("PDFKEY")
    private String pdfLink;

    @SerializedName("CATEGORIA")
    private String categoria;

    @SerializedName("CREATEAT")
    private String created_at;

    private float rating;


    public RevistaZoOm() {
    }

    public RevistaZoOm(int id, String title, String descricao, String categoria, String imagem, String pdfLink, float rating) {
        this.id = id;
        this.title = title;
        this.descricao = descricao;
        this.categoria = categoria;
        this.imagem = imagem;
        this.pdfLink = pdfLink;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescricao() {
        return descricao;
    }


    public String getCategoria() {
        return categoria;
    }

    public String getImagem() {
        return imagem;
    }


    public String getPdfLink() {
        return pdfLink;
    }

    public String getCreated_at() {
        return created_at;
    }

    public float getRating() {
        return rating;
    }
}
