package org.pwr.tirt.plangen.logic;

import org.json.JSONArray;

public interface ITaskListener {
    /*
    Description here
     */
    void dataDownloaded(String data);
    void downloadingFailed();
}
