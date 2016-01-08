package heath.android.sample.ui.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import heath.android.sample.R;

/**
 * Created by heath on 2016/1/7.
 */
public class NestedFragment extends Fragment {
    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.contentLayout);

        Bundle bundle_1 = new Bundle();
        bundle_1.putString("content", "A");
        mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator("Simple"),
                SampleFragment.class, bundle_1);

        Bundle bundle_2 = new Bundle();
        bundle_2.putString("content", "B");
        mTabHost.addTab(mTabHost.newTabSpec("contacts").setIndicator("Contacts"),
                SampleFragment.class, bundle_2);

        Bundle bundle_3 = new Bundle();
        bundle_3.putString("content", "C");
        mTabHost.addTab(mTabHost.newTabSpec("custom").setIndicator("Custom"),
                SampleFragment.class, bundle_3);

        Bundle bundle_4 = new Bundle();
        bundle_4.putString("content", "D");
        mTabHost.addTab(mTabHost.newTabSpec("throttle").setIndicator("Throttle"),
                SampleFragment.class, bundle_4);

        return mTabHost;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("heath", "onDestroyView");
        mTabHost = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("heath", "onDestroy");
    }
}
