package ao.co.proitconsulting.zoomunitel.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import ao.co.proitconsulting.zoomunitel.Api.ApiClient;
import ao.co.proitconsulting.zoomunitel.Api.ApiInterface;
import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.adapters.RevistaZoOmAdapter;
import ao.co.proitconsulting.zoomunitel.helper.Common;
import ao.co.proitconsulting.zoomunitel.helper.MetodosUsados;
import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final String TAG = "TAG_HomeFragment";

    private View view;
    private ImageView imgBackgnd;
    private TextView txtPosition;
    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();
    private static int TIME_DELAY = 3000; // Slide duration 3 seconds



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        imgBackgnd = view.findViewById(R.id.imgBackgnd);
        txtPosition = view.findViewById(R.id.txtPosition);
        viewPager2 = view.findViewById(R.id.viewPagerImageSlider);


        verificarConecxaoNET();


        return view;
    }

    private void verificarConecxaoNET() {
        if (getActivity()!=null) {
            ConnectivityManager conMgr =  (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conMgr!=null) {
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null){
//                    swipeRefreshEstab.setRefreshing(false);
                    MetodosUsados.mostrarMensagem(getContext(),getString(R.string.msg_erro_internet));
//                    mostarMsnErro();
                } else {
                    carregarListaRevistas();
                }
            }
        }
    }

    private void carregarListaRevistas() {

        final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(getContext()).build();
        waitingDialog.setMessage("Carregando...");
        waitingDialog.setCancelable(false);
        waitingDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<RevistaZoOm>> rv = apiInterface.getAllTodasRevistas();
        rv.enqueue(new retrofit2.Callback<List<RevistaZoOm>>() {
            @Override
            public void onResponse(@NonNull Call<List<RevistaZoOm>> call, @NonNull Response<List<RevistaZoOm>> response) {

                if (response.isSuccessful()) {
//                    swipeRefreshEstab.setRefreshing(false);

                    if (Common.revistaZoOmList!=null)
                        Common.revistaZoOmList.clear();

                    if (response.body()!=null){

                        waitingDialog.dismiss();
                        waitingDialog.cancel();

                        Common.revistaZoOmList=response.body();
                        setAdapters(response.body());


                    }

                } else {
//                    swipeRefreshEstab.setRefreshing(false);
                    waitingDialog.dismiss();
                    waitingDialog.cancel();
                }


            }

            @Override
            public void onFailure(@NonNull Call<List<RevistaZoOm>> call, @NonNull Throwable t) {
                waitingDialog.dismiss();
                waitingDialog.cancel();
//                swipeRefreshEstab.setRefreshing(false);
                if (!MetodosUsados.conexaoInternetTrafego(getContext(),TAG)){
                    MetodosUsados.mostrarMensagem(getContext(),R.string.msg_erro_internet);
                }else  if ("timeout".equals(t.getMessage())) {
                    MetodosUsados.mostrarMensagem(getContext(),R.string.msg_erro_internet_timeout);
                }else {
                    MetodosUsados.mostrarMensagem(getContext(),R.string.msg_erro);
                }
            }
        });
    }

    private void setAdapters(final List<RevistaZoOm>revistaZoOmList) {

        viewPager2.setAdapter(new RevistaZoOmAdapter(revistaZoOmList));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.25f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(final int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);
                slideHandler.postDelayed(sliderRunnable,TIME_DELAY); // Slide duration 3 seconds

//                Picasso.get().load(Common.getAllRevistas().get(position).getImagem()).fit().into(imgBackgnd);

                Picasso.get()
                        .load(Common.IMAGE_PATH + revistaZoOmList.get(position).getImagem())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.color.colorPrimary)
                        .into(imgBackgnd, new Callback() {

                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(Common.IMAGE_PATH + revistaZoOmList.get(position).getImagem()).fit().into(imgBackgnd);
                            }
                        });

                txtPosition.setText(String.valueOf(position+1).concat("/").concat(String.valueOf(revistaZoOmList.size())));

                if (position+1 == revistaZoOmList.size()){

                    slideHandler.postDelayed(runnable,TIME_DELAY); // Slide duration 3 seconds

                }
            }
        });


    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(0);
        }
    };


    @Override
    public void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        slideHandler.postDelayed(sliderRunnable,TIME_DELAY); // Slide duration 3 seconds
    }

}