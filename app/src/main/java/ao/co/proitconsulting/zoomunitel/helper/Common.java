package ao.co.proitconsulting.zoomunitel.helper;

import java.util.ArrayList;
import java.util.List;

import ao.co.proitconsulting.zoomunitel.BuildConfig;
import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;
import ao.co.proitconsulting.zoomunitel.models.SobreNos;

public class Common {

    public static final String SHARE_URL_PLAYSTORE = "https://play.google.com/store/apps/details?id=";
    public static final String SHARE_URL_GOOGLE_DRIVE = "https://drive.google.com/file/d/1GOJxw-OENmFA2uL_s0QKkudofZNySaqW/view?usp=sharing";

    public static String BASE_URL_ZOOM_UNITEL = "https://a19797c3225a.ngrok.io/";


    public static final int SPAN_COUNT_ONE = 1;
    public static final int SPAN_COUNT_TWO = 2;

    public static final int VIEW_TYPE_LIST = 1;
    public static final int VIEW_TYPE_GRID = 2;

    public static final String IMAGEM_01_EDICAO_01_2018 =
            "https://appaws.s3.us-east-2.amazonaws.com/Unitel+Revistas/revista_zoom_edicao_01_2018.jpeg";
    public static final String REVISTA_ZOOM_EDICAO_01_2018_PDF =
            "https://appaws.s3.us-east-2.amazonaws.com/Unitel+Revistas/Unitel_RevistaA4_ZOOM_edicao_01_2018_bx.pdf";

    public static final String IMAGEM_02_EDICAO_02_2019 =
            "https://appaws.s3.us-east-2.amazonaws.com/Unitel+Revistas/revista_zoom_edicao_02_2019.jpeg";
    public static final String REVISTA_ZOOM_EDICAO_02_2019_PDF =
            "https://appaws.s3.us-east-2.amazonaws.com/Unitel+Revistas/Unitel_Revista_ZOOM_edicao_02_2019.pdf";

    public static final String IMAGEM_03_EDICAO_03_2020 =
            "https://appaws.s3.us-east-2.amazonaws.com/Unitel+Revistas/revista_zoom_edicao_03_2020.jpeg";
    public static final String REVISTA_ZOOM_EDICAO_03_2020_PDF =
            "https://appaws.s3.us-east-2.amazonaws.com/Unitel+Revistas/Unitel_Revista_ZOOM_edicao_03_2020.pdf";


    public static List<RevistaZoOm> revistaZoOmList = new ArrayList<>();

    public static String IMAGE_PATH = BASE_URL_ZOOM_UNITEL+"revista/image/";
    public static String PDF_PATH = BASE_URL_ZOOM_UNITEL+"revista/view/";




//    public static List<RevistaZoOm> getAllRevistas(){
//        List<RevistaZoOm> revistaZoOmList = new ArrayList<>();
//
//        RevistaZoOm zoom_unitel_edicao01 = new RevistaZoOm(
//                1,
//                "ZoOm Unitel - Edição Nº01 | 2018",
//                "É com enorme prazer que lhe dou as boas vindas a esta primeira edição " +
//                        "da revista “Zoom”.\n\n" +
//                        "A ideia e o conceito desta revista foram concebidos pela Lúcia Moreira, " +
//                        "a nossa responsável da Academia Unitel, que me explicou, no ano " +
//                        "passado, que tinha o incrível privilégio de poder conhecer e interagir " +
//                        "com quase todos os departamentos e direcções dentro da empresa.",
//                "01",
//                IMAGEM_01_EDICAO_01_2018,
//                REVISTA_ZOOM_EDICAO_01_2018_PDF,
//                2.5f);
//
//        revistaZoOmList.add(zoom_unitel_edicao01);
//
//        RevistaZoOm zoom_unitel_edicao02 = new RevistaZoOm(
//                2,
//                "ZoOm Unitel - Edição Nº02 | 2019",
//                        "Nesta segunda edição em que a Unitel comemora dezoito anos de " +
//                        "existência, são trazidos dezoito artigos, que abordam temas desde o " +
//                        "que nos faz ter orgulho em sermos Unitel desde a sua génese, a criação " +
//                        "do nosso ADN corporativo, os desafios que enfrentamos diariamente " +
//                        "para manter operacionais os nossos serviços e distribuirmos os nossos " +
//                        "produtos até aos locais mais recônditos, que permite a cada cliente " +
//                        "utilizar com satisfação a rede móvel com a maior cobertura e a melhor " +
//                        "qualidade em Angola.",
//                "02",
//                IMAGEM_02_EDICAO_02_2019,
//                REVISTA_ZOOM_EDICAO_02_2019_PDF,
//                3.5f);
//
//        revistaZoOmList.add(zoom_unitel_edicao02);
//
//        RevistaZoOm zoom_unitel_edicao03 = new RevistaZoOm(
//                3,
//                "ZoOm Unitel - Edição Nº03 | 2020",
//                "Nesta terceira edição da ZoOm Magazine, dezassete artigos " +
//                        "constituem a revista em que a Era da Informação é o nosso tópico " +
//                        "central.\n\n" +
//                        "O nosso Director-Geral brindou-nos com uma entrevista acerca da " +
//                        "transformação digital da Unitel perante o paradigma da Indústria 4.0.",
//                "03",
//                IMAGEM_03_EDICAO_03_2020,
//                REVISTA_ZOOM_EDICAO_03_2020_PDF,
//                4.5f);
//
//        revistaZoOmList.add(zoom_unitel_edicao03);
//
//
//
//        return revistaZoOmList;
//    }


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
