package heath.android.sample.utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by heath on 2016/6/22.
 */
public class FileUtils {
    public static String APP_DIRECTORY = "sample";

    public static void logToSDCard(Context mContext, String file_name, String str) {
        File file = new File(getExternalDir(), file_name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        long now = System.currentTimeMillis();
        String input = String.valueOf(now) + "\n" + TimeUtils.convertMillisecondsToTimeString(now, "yyyy-MM-dd'T'HH:mm:ss") + "\n" + str + "\n\n";
        addToFile(mContext, file.getAbsolutePath(), input);
    }

    public static boolean addToFile(Context mContext, String filepath, String data) {
        try {
            File myFile = new File(filepath);
            if (!myFile.exists()) {
                boolean created = myFile.createNewFile();
                if (created) {
                    _write(myFile, data);
                    return true;
                }
                return false;
            }
            _write(myFile, data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void _write(File file, String data) throws IOException {
        FileWriter writer = new FileWriter(file, true);
        BufferedWriter bufferWritter = new BufferedWriter(writer);
        bufferWritter.write(data);
        bufferWritter.close();
    }

    public static String getExternalDir() {
        String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File dir = new File(tSDCardPath + "/" + APP_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir.getAbsolutePath();
    }
}
