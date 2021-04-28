package ao.co.proitconsulting.zoomunitel.fragments.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ao.co.proitconsulting.zoomunitel.Api.ApiClient;
import ao.co.proitconsulting.zoomunitel.Api.ApiInterface;
import ao.co.proitconsulting.zoomunitel.Callback.IRevistaCallbackListener;
import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel implements IRevistaCallbackListener {

    private MutableLiveData<List<RevistaZoOm>> revistaZoomList;
    private MutableLiveData<String> messageError;
    private IRevistaCallbackListener revistaCallbackListener;

    public HomeViewModel() {
        revistaCallbackListener = this;
    }

    public MutableLiveData<List<RevistaZoOm>> getRevistaZoomList() {
        if (revistaZoomList == null){
            revistaZoomList = new MutableLiveData<>();
            messageError = new MutableLiveData<>();
            loadRevistaZoomList();
        }
        return revistaZoomList;
    }

    private void loadRevistaZoomList() {

        final List<RevistaZoOm> tempList = new ArrayList<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<RevistaZoOm>> rv = apiInterface.getAllTodasRevistas();
        rv.enqueue(new Callback<List<RevistaZoOm>>() {
            @Override
            public void onResponse(@NonNull Call<List<RevistaZoOm>> call, @NonNull Response<List<RevistaZoOm>> response) {

                if (response.isSuccessful()) {

                    if (response.body()!=null){

                        if (response.body().size()>0){

                            tempList.addAll(response.body());

                            revistaCallbackListener.onRevistaLoadSuccess(tempList);
                        }
                    }

                } else {

                    String responseErrorMsg ="";

                    try {
                        responseErrorMsg = response.errorBody().string();

                        revistaCallbackListener.onRevistaLoadFailed(responseErrorMsg);



                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<List<RevistaZoOm>> call, @NonNull Throwable t) {

                revistaCallbackListener.onRevistaLoadFailed(t.getMessage());
            }
        });

    }

    @Override
    public void onRevistaLoadSuccess(List<RevistaZoOm> revistaZoOmsModels) {
        revistaZoomList.setValue(revistaZoOmsModels);
    }

    @Override
    public void onRevistaLoadFailed(String message) {
        messageError.setValue(message);
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }
}
