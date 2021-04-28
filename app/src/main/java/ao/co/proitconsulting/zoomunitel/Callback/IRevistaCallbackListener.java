package ao.co.proitconsulting.zoomunitel.Callback;

import java.util.List;

import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;

public interface IRevistaCallbackListener {
    void onRevistaLoadSuccess(List<RevistaZoOm> revistaZoOmsModels);
    void onRevistaLoadFailed(String message);
}
