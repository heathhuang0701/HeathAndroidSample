package heath.android.sample.ui.handler;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import heath.android.sample.BaseActionBarActivity;

/**
 * Created by heath on 2016/1/14.
 */
public class HandlerMessage extends BaseActionBarActivity {
    private static final String TAG = "HandlerMessage";
    private Handler handler = new MyHandler(this);
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvTitle = new TextView(this);
        tvTitle.setText(TAG);
        tvTitle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setTextSize(30);

        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i = 1; i <= 10; i++) {
                                Thread.sleep(300);
                                Bundle bundle = new Bundle();
                                bundle.putString("number1", String.valueOf(i));
                                Message msg = new Message();
                                msg.what = 1;
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i = 99; i >= 90; i--) {
                                Thread.sleep(300);
                                Bundle bundle = new Bundle();
                                bundle.putString("number2", String.valueOf(i));
                                Message msg = new Message();
                                msg.what = 2;
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                return true;
            }
        });

        setContentView(tvTitle);
    }

    public TextView getTextView() {
        return tvTitle;
    }

    private static class MyHandler extends Handler {
        private final WeakReference<HandlerMessage> mActivity;

        public MyHandler(HandlerMessage activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            HandlerMessage activity = mActivity.get();
            if (activity == null) {
                return;
            }

            switch (msg.what) {
                case 1:
                    activity.getTextView().setTextColor(Color.RED);
                    activity.getTextView().setText(msg.getData().getString("number1"));
                    break;
                case 2:
                    activity.getTextView().setTextColor(Color.BLUE);
                    activity.getTextView().setText(msg.getData().getString("number2"));
                    break;
            }
        }
    }
}
