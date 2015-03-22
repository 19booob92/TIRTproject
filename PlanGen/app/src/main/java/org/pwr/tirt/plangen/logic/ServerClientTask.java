package org.pwr.tirt.plangen.logic;

import android.content.Context;
import android.os.AsyncTask;

import org.pwr.tirt.plangen.utils.Constants;

public class ServerClientTask extends AsyncTask<String, Void, String> {
    private Context context;
    private ITaskListener listener;
    private boolean noInternet = false;

    public ServerClientTask(Context context, ITaskListener listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        if(!ServerClient.checkInternetConenction(context))
            noInternet = true;
    }

    @Override
    protected String doInBackground(String... params) {
        if(!noInternet)
            return ServerClient.getDataFromServer();
        else
            return Constants.FAIL_MESSAGE;
    }

    @Override
    protected void onPostExecute(String param) {
        listener.dataDownloaded(param);
    }
}
