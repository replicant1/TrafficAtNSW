package rod.bailey.trafficatnsw.hazard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import rod.bailey.trafficatnsw.util.ConfigSingleton;
import rod.bailey.trafficatnsw.ui.ListWithEmptyMessage;
import rod.bailey.trafficatnsw.util.MLog;

/**
 * Created by rodbailey on 22/7/17.
 */
public class DownloadHazardFileTask extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = DownloadHazardFileTask.class.getSimpleName();
    private final Context ctx;
    private final ListWithEmptyMessage listWithEmptyMessage;
    private final HazardListMode mode;
    private ProgressDialog dialog;

    public DownloadHazardFileTask(Context ctx, ListWithEmptyMessage listWithEmptyMessage, HazardListMode mode) {
        this.ctx = ctx;
        this.listWithEmptyMessage = listWithEmptyMessage;
        this.mode = mode;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(ctx);
        dialog.setMessage("Loading incidents...");
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // Download the JSON file. The URL is in params[0].
        BufferedReader bufferedReader = null;
        Boolean hazardsLoadedOK = Boolean.TRUE;

        try {
            if (ConfigSingleton.getInstance()
                    .loadIncidentsFromLocalJSONFile()) {
                String assetFileName = ConfigSingleton.getInstance()
                        .localIncidentsJSONFile();
                InputStream input = ctx.getAssets().open(assetFileName);
                int size = input.available();
                byte[] buffer = new byte[size];
                input.read(buffer);
                input.close();
                String text = new String(buffer);

                HazardDatabase.Companion.getInstance().init(text);
            } else {
                URL url = new URL(ConfigSingleton.getInstance()
                        .remoteIncidentsJSONFile());
                InputStreamReader instreamReader = new InputStreamReader(
                        url.openStream());
                bufferedReader = new BufferedReader(instreamReader);

                String line = null;
                StringBuffer lineBuffer = new StringBuffer();

                while ((line = bufferedReader.readLine()) != null) {
                    lineBuffer.append(line);
                }

                HazardDatabase.Companion.getInstance().init(lineBuffer.toString());
                bufferedReader.close();
            }
        } catch (Exception e) {
            MLog.w(TAG, "Failed to load hazards JSON", e);
            hazardsLoadedOK = Boolean.FALSE;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                }
            }
        }

        return hazardsLoadedOK;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        // Force the listView to refresh its data

        if (dialog != null) {
            dialog.dismiss();
        }

        if (result == Boolean.TRUE) {
            listWithEmptyMessage.setAdapter(new HazardListAdapter(ctx, mode.getFilter()));
        } else {
            // We don't all mainLayout.setAdapter, which means that the old
            // (stale)
            // data will still remain visible.
            MLog.i(TAG,
                    "Failed to load camera image - showing error dialog");
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle("No Incident Data");
            builder.setCancelable(true);
            builder.setMessage("Tap the refresh icon at top right to try again.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
