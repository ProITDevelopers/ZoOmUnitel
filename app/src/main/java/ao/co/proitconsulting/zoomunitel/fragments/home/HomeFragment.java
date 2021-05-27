package ao.co.proitconsulting.zoomunitel.fragments.home;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.activities.MainActivity;
import ao.co.proitconsulting.zoomunitel.adapters.RevistaZoOmAdapter;
import ao.co.proitconsulting.zoomunitel.helper.Common;
import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;
import dmax.dialog.SpotsDialog;

public class HomeFragment extends Fragment {

    private static final String TAG = "TAG_HomeFragment";
    private HomeViewModel homeViewModel;
    private AlertDialog waitingDialog;

    private View view;
    private ImageView imgBackgnd;
    private TextView txtPosition;
    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();
    private static int TIME_DELAY = 3500; // Slide duration 3 seconds

    private ConstraintLayout coordinatorLayout;
    private RelativeLayout errorLayout;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        view = inflater.inflate(R.layout.fragment_home, container, false);

        FrameLayout frameLayout = ((MainActivity) getActivity()).getFrameLayoutImgToolbar();
        if (frameLayout != null) {
            frameLayout.setVisibility(View.VISIBLE);
        }

        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        errorLayout = view.findViewById(R.id.erroLayout);



        waitingDialog = new SpotsDialog.Builder().setContext(getContext()).build();
        waitingDialog.setMessage("Carregando...");
        waitingDialog.setCancelable(false);
        waitingDialog.show();

        imgBackgnd = view.findViewById(R.id.imgBackgnd);
        txtPosition = view.findViewById(R.id.txtPosition);
        viewPager2 = view.findViewById(R.id.viewPagerImageSlider);

        homeViewModel.getRevistaZoomList().observe(this, new Observer<List<RevistaZoOm>>() {
            @Override
            public void onChanged(List<RevistaZoOm> revistaZoOms) {
                waitingDialog.dismiss();

                if (Common.revistaZoOmList!=null)
                    Common.revistaZoOmList.clear();
                Common.revistaZoOmList.addAll(revistaZoOms);
                //Create adapter
                setAdapters(revistaZoOms);
            }
        });

        homeViewModel.getMessageError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                waitingDialog.dismiss();
                mostrarMensagemPopUp(s);
            }
        });


        return view;
    }

    private void setAdapters(final List<RevistaZoOm>revistaZoOmList) {

        viewPager2.setAdapter(new RevistaZoOmAdapter(getContext(), revistaZoOmList));

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
                page.setScaleY(0.60f + r * 0.25f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(final int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);
                slideHandler.postDelayed(sliderRunnable,TIME_DELAY); // Slide duration 3 seconds

                final RevistaZoOm revistaZoOm = revistaZoOmList.get(position);

                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

                    imgBackgnd.setImageResource(R.color.colorPrimary);

                }else{
                    Picasso.get()
                            .load(Common.IMAGE_PATH + revistaZoOm.getImagem())
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.color.colorPrimary)
                            .into(imgBackgnd, new Callback() {

                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(Common.IMAGE_PATH + revistaZoOm.getImagem()).fit().into(imgBackgnd);
                                }
                            });
                }



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

    private void mostrarMensagemPopUp(String msg) {
        SpannableString title = new SpannableString(getString(R.string.app_name));
        title.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.white)),
                0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString message = new SpannableString(msg);
        message.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.blue_unitel)),
                0, message.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString ok = new SpannableString(getString(R.string.text_ok));
        ok.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.white)),
                0, ok.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            if (getContext()!=null){
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext(),R.style.MyDialogTheme);

                builder.setTitle(title);
                builder.setMessage(message);
                builder.setCancelable(false);

                builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        mostarMsnErro();
                    }
                });

                builder.show();
            }

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.MyDialogTheme);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setCancelable(false);

            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    mostarMsnErro();
                }
            });

            builder.show();
        }



    }

    private void mostarMsnErro(){

        if (errorLayout.getVisibility() == View.GONE){
            errorLayout.setVisibility(View.VISIBLE);

            coordinatorLayout.setVisibility(View.GONE);

        }
    }

}