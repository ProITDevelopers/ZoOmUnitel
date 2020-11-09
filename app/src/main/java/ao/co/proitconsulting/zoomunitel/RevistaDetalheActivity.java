package ao.co.proitconsulting.zoomunitel;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

public class RevistaDetalheActivity extends AppCompatActivity {

    private String toolbarTitle;
    private int position;
    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent()!=null){
            toolbarTitle = getIntent().getStringExtra("toolbarTitle");
            position = getIntent().getIntExtra("position",0);
        }
        setContentView(R.layout.activity_revista_detalhe);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle(toolbarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewPager2 = findViewById(R.id.viewPagerImageDetail);

        viewPager2.setAdapter(new RevistaDetalheAdapter(Common.getAllRevistas()));
        viewPager2.setCurrentItem(position);

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
                slideHandler.postDelayed(sliderRunnable,10000); // Slide duration 10 seconds

                toolbarTitle = Common.getAllRevistas().get(position).getTitle();

                if (getSupportActionBar()!=null){
                    getSupportActionBar().setTitle(toolbarTitle);
                }


                if (position+1 == Common.getAllRevistas().size()){

                    slideHandler.postDelayed(runnable,10000); // Slide duration 10 seconds

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
    protected void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        slideHandler.postDelayed(sliderRunnable,10000); // Slide duration 10 seconds
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);


    }
}