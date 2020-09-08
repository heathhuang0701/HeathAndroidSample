package heath.android.sample.ui.tab;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import heath.android.sample.BaseActionBarActivity;
import heath.android.sample.R;

/**
 * Created by heath on 2016/1/7.
 */
public class FixedTab extends BaseActionBarActivity implements TabHost.OnTabChangeListener {

    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_tab);

        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.contentLayout);
        tabHost.getTabWidget().setDividerDrawable(null);
        tabHost.setOnTabChangedListener(this);
        initTab();
    }

    private void initTab(){
        String tabs[] = TabImageText.getTabTxt();
        for(int i=0;i<tabs.length;i++){
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(tabs[i]).setIndicator(getTabView(i));
            Bundle bundle = new Bundle();
            bundle.putString("content", TabImageText.getTabTxt()[i]);
            tabHost.addTab(tabSpec, TabImageText.getFragments()[i], bundle);
            tabHost.setTag(i);
        }
    }
    private View getTabView(int idx){
        View view = LayoutInflater.from(this).inflate(R.layout.tab_image_text,null);
        ((TextView)view.findViewById(R.id.tab_text)).setText(TabImageText.getTabTxt()[idx]);
        if( idx == 0) {
            ((TextView)view.findViewById(R.id.tab_text)).setTextColor(Color.RED);
            ((ImageView)view.findViewById(R.id.tab_image)).setImageResource(TabImageText.getTabsImgPressed()[idx]);
        } else {
            ((ImageView)view.findViewById(R.id.tab_image)).setImageResource(TabImageText.getTabImg()[idx]);
        }
        return view;
    }

    @Override
    public void onTabChanged(String tabId) {
        // TODO Auto-generated method stub
        updateTab();
    }

    private void updateTab(){
        TabWidget tabw = tabHost.getTabWidget();
        for(int i=0; i<tabw.getChildCount(); i++){
            View view=tabw.getChildAt(i);
            ImageView iv=(ImageView)view.findViewById(R.id.tab_image);
            if (i == tabHost.getCurrentTab()) {
                ((TextView)view.findViewById(R.id.tab_text)).setTextColor(Color.RED);
                iv.setImageResource(TabImageText.getTabsImgPressed()[i]);
                setNavigationTitle(TabImageText.getTabTxt()[i]);
            } else {
                ((TextView)view.findViewById(R.id.tab_text)).setTextColor(Color.parseColor("#444444"));
                iv.setImageResource(TabImageText.getTabImg()[i]);
            }
        }
    }
}
