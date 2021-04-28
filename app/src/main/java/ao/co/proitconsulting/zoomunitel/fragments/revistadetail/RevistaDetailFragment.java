package ao.co.proitconsulting.zoomunitel.fragments.revistadetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import ao.co.proitconsulting.zoomunitel.R;
import ao.co.proitconsulting.zoomunitel.adapters.RevistaDetalheAdapter;
import ao.co.proitconsulting.zoomunitel.helper.Common;
import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;

public class RevistaDetailFragment extends Fragment {

    private RevistaDetailViewModel revistaDetailViewModel;
    private View view;
    private ViewPager2 viewPager2;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        revistaDetailViewModel =
                new ViewModelProvider(this).get(RevistaDetailViewModel.class);
        view = inflater.inflate(R.layout.fragment_revista_detail, container, false);

        initViews();

        revistaDetailViewModel.getMutableLiveDataRevista().observe(this, new Observer<List<RevistaZoOm>>() {
            @Override
            public void onChanged(List<RevistaZoOm> revistaZoOms) {
                //Create adapter
                setAdapters(revistaZoOms);
            }
        });
        
        return view;
    }

    private void initViews() {
        ((AppCompatActivity)getActivity())
                .getSupportActionBar()
                .setTitle(getString(R.string.app_name));
//                .setTitle(Common.selectedRevista.getTitle());

        viewPager2 = view.findViewById(R.id.viewPagerImageDetail);
    }

    private void setAdapters(List<RevistaZoOm> revistaZoOms) {

        viewPager2.setAdapter(new RevistaDetalheAdapter(getActivity(), revistaZoOms));
        viewPager2.setCurrentItem(Common.selectedRevistaPosition, false);

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

                Common.selectedRevistaPosition = position;
            }


        });

    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().removeAllStickyEvents();
        super.onDestroyView();
    }
}
