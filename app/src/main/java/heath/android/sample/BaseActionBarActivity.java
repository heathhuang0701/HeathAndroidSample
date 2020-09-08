package heath.android.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class BaseActionBarActivity extends AppCompatActivity {
    private static String TAG = "BaseActionBarActivity";
    protected BaseActionBarActivity mActivity;
    protected Context mContext;
    private ArrayList<Bitmap> _bitmaps;
    protected ProgressDialog loadingDialog;
    protected Toolbar toolbar;
    private RelativeLayout view_main;
    private Bitmap __navigation, __background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base_actionbar);
        mContext = this;
        mActivity = this;

        __findViews();
        __setViews();
    }

    @Override
    protected void onDestroy() {
        if (__background != null) {
            __background.recycle();
        }
        if (__navigation != null) {
            __navigation.recycle();
        }
        __background = null;
        __navigation = null;

        System.gc();
        super.onDestroy();
    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutResID, null);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        view_main.addView(view);
    }

    @Override
    public void setContentView(View view) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        view_main.addView(view);
    }

    private void __initLoadingDialog() {
        loadingDialog = new ProgressDialog(mContext);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(true);
        loadingDialog.setMessage(getString(R.string.please_wait));
    }

    private void __findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        view_main = (RelativeLayout) findViewById(R.id.view_main);
    }

    private void __setViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent() != null && getIntent().hasExtra("title")) {
            setNavigationTitle(getIntent().getStringExtra("title"));
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        __initLoadingDialog();
    }

    protected ProgressDialog getLoadingDialog() {
        if (loadingDialog == null) {
            __initLoadingDialog();
        }

        return loadingDialog;
    }

    public void startLoading() {
        if (!mActivity.isFinishing()) {
            loadingDialog.show();
        }
    }

    public void stopLoading() {
        if (!mActivity.isFinishing()) {
            loadingDialog.dismiss();
        }
    }

    protected void setLoadingText(String text) {
        loadingDialog.setMessage(text);
    }

    protected void setNavigationTitle(String text) {
        getSupportActionBar().setTitle(text);
    }

    protected void setNavigationTitle(int resource_id) {
        getSupportActionBar().setTitle(resource_id);
    }

    protected void addUsedBitmap(Bitmap bmp) {
        if (_bitmaps == null) {
            _bitmaps = new ArrayList<Bitmap>();
        }
        _bitmaps.add(bmp);
    }

    protected void recycleAllUsedBitmaps() {
        if (_bitmaps == null) {
            return;
        }

        for (Bitmap bmp : _bitmaps) {
            if (bmp != null && !bmp.isRecycled()) {
                bmp.recycle();
                bmp = null;
            }
        }
    }

    protected RelativeLayout getRootView() {
        return view_main;
    }
}
