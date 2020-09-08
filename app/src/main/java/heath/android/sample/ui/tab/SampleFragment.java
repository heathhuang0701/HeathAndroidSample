package heath.android.sample.ui.tab;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by heath on 2016/1/7.
 */
public class SampleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView tvTitle = new TextView(getActivity());
        tvTitle.setText(getArguments().getString("content"));
        tvTitle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setTextSize(30);
        return tvTitle;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("heath", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("heath", "onDestroy");
    }
}
