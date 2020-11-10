package ao.co.proitconsulting.zoomunitel.helper;

import java.util.ArrayList;
import java.util.List;

import ao.co.proitconsulting.zoomunitel.BuildConfig;
import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;
import ao.co.proitconsulting.zoomunitel.models.SobreNos;

public class Common {

    public static final String SHARE_URL_PLAYSTORE = "https://play.google.com/store/apps/details?id=";
    public static final String SHARE_URL_GOOGLE_DRIVE = "https://drive.google.com/file/d/1GOJxw-OENmFA2uL_s0QKkudofZNySaqW/view?usp=sharing";

    public static String bearerApi = "Bearer ";

    public static final int SPAN_COUNT_ONE = 1;
    public static final int SPAN_COUNT_TWO = 2;

    public static final int VIEW_TYPE_LIST = 1;
    public static final int VIEW_TYPE_GRID = 2;

    public static final String MANUAL_XPRESS_LINK = "https://express2020.s3.us-east-2.amazonaws.com/Docs/Manual_XpressLengueno_android.pdf";

    public static final String Batman_Imagem = "https://firebasestorage.googleapis.com/v0/b/firetutorial-9cfcc.appspot.com/o/zoOmunitel%2Fbats.png?alt=media&token=65e5fca6-5405-4bf5-9c07-c7ec7e3990b8";
    public static final String Batman_LINK = "https://firebasestorage.googleapis.com/v0/b/firetutorial-9cfcc.appspot.com/o/zoOmunitel%2Fbatman_spiderman.pdf?alt=media&token=87ce83c4-1ed5-4b4b-b9a4-70ec18a19025";

    public static final String CapAmerica_Imagem = "https://firebasestorage.googleapis.com/v0/b/firetutorial-9cfcc.appspot.com/o/zoOmunitel%2Fcapamerica.png?alt=media&token=d2c579bf-51b9-426a-951f-6e78b91db0ad";
    public static final String CapAmerica_LINK = "https://firebasestorage.googleapis.com/v0/b/firetutorial-9cfcc.appspot.com/o/zoOmunitel%2Fheroes_history.pdf?alt=media&token=610f1d92-43c4-400a-b08e-04bab0b179bd";

    public static final String Hulk_Imagem = "https://firebasestorage.googleapis.com/v0/b/firetutorial-9cfcc.appspot.com/o/zoOmunitel%2Fhulk.png?alt=media&token=cb21b684-8f61-441f-b967-59b26ab689d3";
    public static final String Hulk_LINK = MANUAL_XPRESS_LINK;

    public static final String Spiderman_Imagem = "https://firebasestorage.googleapis.com/v0/b/firetutorial-9cfcc.appspot.com/o/zoOmunitel%2Fspider.jpg?alt=media&token=e1a42e03-df7d-45df-8f42-2870e24e7f05";
    public static final String Spiderman_LINK = "https://firebasestorage.googleapis.com/v0/b/firetutorial-9cfcc.appspot.com/o/zoOmunitel%2Fbatman_spiderman.pdf?alt=media&token=87ce83c4-1ed5-4b4b-b9a4-70ec18a19025";

    public static final String Thanos_Imagem = "https://firebasestorage.googleapis.com/v0/b/firetutorial-9cfcc.appspot.com/o/zoOmunitel%2Fthanos.jpg?alt=media&token=66e3d163-0d70-4998-a3ec-6d6b096089f9";
    public static final String Thannos_LINK = CapAmerica_LINK;

    public static final String Batman_Spiderman_Imagem = "https://firebasestorage.googleapis.com/v0/b/firetutorial-9cfcc.appspot.com/o/zoOmunitel%2Fbatman_spider.png?alt=media&token=c9e121e4-4671-4ce7-b48f-95dc5dc87aba";
    public static final String Batman_Spiderman_LINK = "https://firebasestorage.googleapis.com/v0/b/firetutorial-9cfcc.appspot.com/o/zoOmunitel%2Fbatman_spiderman.pdf?alt=media&token=87ce83c4-1ed5-4b4b-b9a4-70ec18a19025";




    public static List<RevistaZoOm> getAllRevistas(){
        List<RevistaZoOm> revistaZoOmList = new ArrayList<>();

        RevistaZoOm batman = new RevistaZoOm(
                1,
                "Batman e as Tartarugas Ninjas",
                "As aventuras de Batman e seus animais de estimação. Muita Pancada em Gotham City." +
                        "\n\n\uD83E\uDD87\uD83D\uDC22 \uD83D\uDC22 \uD83D\uDC22 \uD83D\uDC22",
                Batman_Imagem,
                Batman_LINK,
                4.5f);

        revistaZoOmList.add(batman);

        RevistaZoOm capamerica = new RevistaZoOm(
                2,
                "Capitão América - No limite",
                "As aventuras do super-soldado Steve Rogers. Soldado forte com a sua incrível bandeja... quer dizer... com o seu incrível escudo que não obedece as leis da física." +
                        "\n\n" +" "+"\uD83D\uDCAD"+
                        "\n\uD83E\uDD14",
                CapAmerica_Imagem,
                CapAmerica_LINK,
                3.0f);

        revistaZoOmList.add(capamerica);

        RevistaZoOm hulk = new RevistaZoOm(
                3,
                "HULK - Futuro imperfeito",
                "Bruce Banner sofre de Covid-19 após uma mudança repentina na linha do tempo. Até quando Flash? Até quando? \uD83D\uDE4C\uD83D\uDE1E",
                Hulk_Imagem,
                Hulk_LINK,
                3.5f);

        revistaZoOmList.add(hulk);

        RevistaZoOm spiderman = new RevistaZoOm(
                4,
                "Homem-Aranha - Guerra Civil",
                "Peter Parker descobre que a sua namorada está grávida de 9 meses, sendo que os estão juntos apenas 3 meses. \n\n\uD83C\uDFB6Mormão, mormão, mormão, mormão, mormão, mormão, mormão...",
                Spiderman_Imagem,
                Spiderman_LINK,
                4.0f);

        revistaZoOmList.add(spiderman);

        RevistaZoOm thanos = new RevistaZoOm(
                5,
                "THANOS e as Jóias do Infinito",
                "O vilão está de volta. Numa tentativa de mantér o equilíbrio em todo universo, durante a pandemia, Thanos chega ao planeta Terra estalando os dedos, com o objectivo de eliminar os fracos e é brutalmente assaltado no Kikolo. \uD83E\uDD23 \uD83E\uDD23 \uD83E\uDD23",
                Thanos_Imagem,
                Thannos_LINK,
                5.0f);

        revistaZoOmList.add(thanos);

        RevistaZoOm batman_spiderman = new RevistaZoOm(
                6,
                "Batman & Homem-Aranha",
                "Peter Parker junta-se ao melhor detective, Batman, para resolver o mistério da gravidez da sua namorada, Mary Jane. Batman alerta a Peter dizendo: 'não será fácil, pois já fazem 6 meses que a MJ tem tido esse hábito.' \n\n\uD83C\uDFB6Mormão, mormão, mormão, mormão, mormão, mormão, mormão...",
                Batman_Spiderman_Imagem,
                Batman_Spiderman_LINK,
                4.5f);

        revistaZoOmList.add(batman_spiderman);




        return revistaZoOmList;
    }


    public static List<SobreNos> getSobreNosList(){
        List<SobreNos> sobreNosList = new ArrayList<>();

        sobreNosList.add(new SobreNos(1,"ZoOm Unitel","ZoOm Unitel é o aplicativo onde encontrarás as melhores revistas de Marketing."));
        sobreNosList.add(new SobreNos(2,"Versão", BuildConfig.VERSION_NAME));
        sobreNosList.add(new SobreNos(3,"Desenvolvedor","Copyright © 2020 - ZoOm Unitel"+"\n"+"Powered by Pro-IT Consulting"));
        sobreNosList.add(new SobreNos(4,"Enviar feedback","Tem alguma dúvida? Estamos felizes em ajudar."));
        sobreNosList.add(new SobreNos(5,"Partilhar","Partilhe o link da app com os seus contactos."));

        return sobreNosList;
    }
}
