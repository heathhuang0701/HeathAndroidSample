package heath.android.sample.multimedia.qrcode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import heath.android.sample.BaseActionBarActivity;
import heath.android.sample.Config;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by heath on 2016/6/30.
 */
public class QrcodeScanner extends BaseActionBarActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScannerView = new ZXingScannerView(mContext);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.d(Config.TAG, rawResult.getText()); // Prints scan results
        Log.d(Config.TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle("掃描成功")
                .setMessage(rawResult.getText())
                .setCancelable(false)
                .setPositiveButton("重新掃瞄", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mScannerView.resumeCameraPreview(QrcodeScanner.this);
                    }
                }).setNegativeButton("返回上一頁", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onBackPressed();
                    }
                });
        builder.create().show();
    }
}
