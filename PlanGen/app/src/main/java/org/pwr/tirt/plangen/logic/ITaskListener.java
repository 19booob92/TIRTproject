package org.pwr.tirt.plangen.logic;

import org.json.JSONArray;

public interface ITaskListener {
    void dataDownloaded(String data);
    void downloadingFailed();
}
