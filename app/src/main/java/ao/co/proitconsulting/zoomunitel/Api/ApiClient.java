package ao.co.proitconsulting.zoomunitel.Api;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import ao.co.proitconsulting.zoomunitel.helper.Common;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {



    private static Retrofit retrofit = null;
    private static int REQUEST_TIMEOUT = 15000;
    private static OkHttpClient okHttpClient;


    public static Retrofit getClient() {

        if (okHttpClient == null)
            initOkHttp();

        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();


            retrofit = new Retrofit.Builder()
                    .baseUrl(Common.BASE_URL_ZOOM_UNITEL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    private static void initOkHttp() {

        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.MILLISECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.interceptors().add(interceptor);
        httpClient.interceptors().add(new AddTokenInterceptor());

        /*
        ** RETROFIT REQUEST FOR WORKING ON PRE LOLLIPOP DEVICES
         */
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            try {
                httpClient.sslSocketFactory(new TLSSocketFactory());
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }



        okHttpClient = httpClient.build();


    }




}
