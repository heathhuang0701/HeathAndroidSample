package heath.android.sample.multimedia.qrcode;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import heath.android.sample.BaseActionBarActivity;
import heath.android.sample.Config;
import heath.android.sample.R;

/**
 * Created by heath on 2016/6/30.
 */
public class CheckCameraPermission extends BaseActionBarActivity {
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private static final int QRCODE_SCANNER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("檢查相機權限");
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            goScan();
        } else {
            requestCameraPermission();
        }
    }

    private void goScan() {
        Intent intent = new Intent(mContext, QrcodeScanner.class);
        startActivityForResult(intent, QRCODE_SCANNER);
    }

    private void requestCameraPermission() {
        Log.w(Config.TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(Config.TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(Config.TAG, "Camera permission granted - initialize the camera source");
            goScan();
            return;
        }

        Log.e(Config.TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.not_enable_permission)
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.back, listener)
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QRCODE_SCANNER) {
            onBackPressed();
        }
    }
}
