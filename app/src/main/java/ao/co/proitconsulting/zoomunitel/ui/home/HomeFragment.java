package ao.co.proitconsulting.zoomunitel.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import ao.co.proitconsulting.zoomunitel.Common;
import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.RevistaZoOmAdapter;

public class HomeFragment extends Fragment {

    private View view;
    private ImageView imgBackgnd;
    private TextView txtPosition;
    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        imgBackgnd = view.findViewById(R.id.imgBackgnd);
        txtPosition = view.findViewById(R.id.txtPosition);
        viewPager2 = view.findViewById(R.id.viewPagerImageSlider);

        viewPager2.setAdapter(new RevistaZoOmAdapter(Common.getAllRevistas()));

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
                slideHandler.postDelayed(sliderRunnable,3000); // Slide duration 3 seconds

//                Picasso.get().load(Common.getAllRevistas().get(position).getImagem()).fit().into(imgBackgnd);

                Picasso.get()
                        .load(Common.getAllRevistas().get(position).getImagem())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.color.colorPrimary)
                        .into(imgBackgnd, new Callback() {

                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(Common.getAllRevistas().get(position).getImagem()).fit().into(imgBackgnd);
                            }
                        });

                txtPosition.setText(String.valueOf(position+1).concat("/").concat(String.valueOf(Common.getAllRevistas().size())));

                if (position+1 == Common.getAllRevistas().size()){

                    slideHandler.postDelayed(runnable,3000); // Slide duration 3 seconds

                }
            }
        });

        return view;
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
        slideHandler.postDelayed(sliderRunnable,3000); // Slide duration 3 seconds
    }

}