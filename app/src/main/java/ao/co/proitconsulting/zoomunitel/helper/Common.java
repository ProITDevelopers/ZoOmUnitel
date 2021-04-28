package ao.co.proitconsulting.zoomunitel.helper;

import java.util.ArrayList;
import java.util.List;

import ao.co.proitconsulting.zoomunitel.BuildConfig;
import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;
import ao.co.proitconsulting.zoomunitel.models.SobreNos;

public class Common {

    public static final String SHARE_URL_PLAYSTORE = "https://play.google.com/store/apps/details?id=";
    public static final String SHARE_URL_GOOGLE_DRIVE = "https://drive.google.com/file/d/1GOJxw-OENmFA2uL_s0QKkudofZNySaqW/view?usp=sharing";

    public static String BASE_URL_ZOOM_UNITEL = "http://41.78.18.144:3000/";
//    public static String BASE_URL_ZOOM_UNITEL = "https://01663c1f4839.ngrok.io/";


    public static final int SPAN_COUNT_ONE = 1;
    public static final int SPAN_COUNT_TWO = 2;

    public static final int VIEW_TYPE_LIST = 1;
    public static final int VIEW_TYPE_GRID = 2;


    public static List<RevistaZoOm> revistaZoOmList = new ArrayList<>();
    public static int selectedRevistaPosition;
    public static RevistaZoOm selectedRevista;

    public static String USER_IMAGE_PATH = BASE_URL_ZOOM_UNITEL+"user/image/";
    public static String IMAGE_PATH = BASE_URL_ZOOM_UNITEL+"revista/image/";
    public static String PDF_PATH = BASE_URL_ZOOM_UNITEL+"revista/view/";



    public static List<SobreNos> getSobreNosList(){
        List<SobreNos> sobreNosList = new ArrayList<>();

        sobreNosList.add(new SobreNos(1,"ZoOm Unitel","A ZoOm Magazine é uma publicação anual promovida pela Academia " +
                "Unitel, dedicada à partilha de conhecimento sobre projectos (internos e externos), tecnologias e investigação."));
        sobreNosList.add(new SobreNos(2,"Versão", BuildConfig.VERSION_NAME));
        sobreNosList.add(new SobreNos(3,"Desenvolvedor","Copyright © 2020 - ZoOm Unitel"+"\n"+"Powered by Pro-IT Consulting"));
        sobreNosList.add(new SobreNos(4,"Enviar feedback","Tem alguma dúvida? Estamos felizes em ajudar."));
        sobreNosList.add(new SobreNos(5,"Partilhar","Partilhe o link da app com os seus contactos."));

        return sobreNosList;
    }
}
