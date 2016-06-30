package heath.android.sample.multimedia.qrcode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import heath.android.sample.BaseActionBarActivity;

/**
 * Created by heath on 2016/6/30.
 */
public class QrcodeGenerator extends BaseActionBarActivity {
    EditText et;
    Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        et = new EditText(mContext);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle("請輸入要編碼的文字")
                .setView(et)
                .setCancelable(false)
                .setPositiveButton("產生", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = et.getText().toString();
                        if (!TextUtils.isEmpty(content)) {
                            try {
                                DisplayMetrics dm = getResources().getDisplayMetrics();
                                bm = encodeAsBitmap(content, dm.widthPixels, dm.widthPixels);
                                ImageView img = new ImageView(mContext);
                                img.setImageBitmap(bm);
                                setContentView(img);
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).setNegativeButton("清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        et.setText("");
                    }
                });
        builder.create().show();
    }

    private Bitmap encodeAsBitmap(String str, int width, int height) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, width, height, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }
}
