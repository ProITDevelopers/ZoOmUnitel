package ao.co.proitconsulting.zoomunitel.models;

public class RevistaZoOm {
    private int id;
    private String title, descricao, imagem, link;
    private float rating;

    public RevistaZoOm() {
    }

    public RevistaZoOm(int id, String title, String descricao, String imagem, String link, float rating) {
        this.id = id;
        this.title = title;
        this.descricao = descricao;
        this.imagem = imagem;
        this.link = link;
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

    public String getImagem() {
        return imagem;
    }

    public String getLink() {
        return link;
    }

    public float getRating() {
        return rating;
    }
}
