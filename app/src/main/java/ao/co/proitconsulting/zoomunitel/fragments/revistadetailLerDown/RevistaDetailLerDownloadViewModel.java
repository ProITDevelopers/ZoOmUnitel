package ao.co.proitconsulting.zoomunitel.fragments.revistadetailLerDown;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ao.co.proitconsulting.zoomunitel.helper.Common;
import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;

public class RevistaDetailLerDownloadViewModel extends ViewModel {
    private MutableLiveData<List<RevistaZoOm>> mutableLiveDataRevista;

    public RevistaDetailLerDownloadViewModel() {

    }

    public MutableLiveData<List<RevistaZoOm>> getMutableLiveDataRevista() {
        if (mutableLiveDataRevista == null)
            mutableLiveDataRevista = new MutableLiveData<>();
        mutableLiveDataRevista.setValue(Common.revistaZoOmList);

        return mutableLiveDataRevista;
    }

    public void setRevistaModel(List<RevistaZoOm> revistaModel){
        if (mutableLiveDataRevista != null)
            mutableLiveDataRevista.setValue(revistaModel);
    }
}
