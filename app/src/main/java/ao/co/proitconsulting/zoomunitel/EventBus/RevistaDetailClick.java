package ao.co.proitconsulting.zoomunitel.EventBus;

import ao.co.proitconsulting.zoomunitel.models.RevistaZoOm;

public class RevistaDetailClick {
    private boolean success;
    private RevistaZoOm revistaZoOm;

    public RevistaDetailClick(boolean success, RevistaZoOm revistaZoOm) {
        this.success = success;
        this.revistaZoOm = revistaZoOm;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public RevistaZoOm getRevista() {
        return revistaZoOm;
    }


}
