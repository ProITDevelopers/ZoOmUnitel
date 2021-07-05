package ao.co.proitconsulting.zoomunitel;

import android.app.Application;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class ZoOmUnitelApplication extends Application {

    private static ZoOmUnitelApplication mInstance;


    @Override
    public void onCreate() {
        super.onCreate();


        mInstance = this;

        /* Picasso */
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);



    }

    public static ZoOmUnitelApplication getInstance() {
        return mInstance;
    }


}
