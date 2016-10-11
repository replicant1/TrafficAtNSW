package rod.bailey.trafficatnsw.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * A collection of static utility methods for operating on the contents of the "assets" directory for this app.
 */
public class AssetUtils {

    /**
     * Loads a given file from the /assets folder for this app.
     *
     * @param context App context
     * @param assetFileName Simple file name to be loaded.
     * @return Contents of the asset file in String form
     */
    public static String loadAssetFileAsString(Context context, String assetFileName) throws IOException {
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = context.getAssets().open(assetFileName);

            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            return new String(buffer);
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }

}
