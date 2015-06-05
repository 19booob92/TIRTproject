package org.pwr.tirt.plangen.logic;

import org.json.JSONArray;
/**
 * Interface with methods called on {@link GVServerClient}s listeners
 */
public interface ITaskListener {
    void dataDownloaded(String data);
    void downloadingFailed();
}
