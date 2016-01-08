package heath.android.sample.ui.tab;

import heath.android.sample.R;

/**
 * Created by heath on 2016/1/7.
 */
public class TabImageText {

    public static String[] getTabTxt(){
        String[] names = {
                "測試",
                "測試一",
                "測試二二",
                "測試三",
                "測試四"
        };
        return names;
    }

    public static int[] getTabImg(){
        int[] ids = {
                R.drawable.tab,
                R.drawable.tab,
                R.drawable.tab,
                R.drawable.tab,
                R.drawable.tab
        };
        return ids;
    }

    public static int[] getTabsImgPressed(){
        int[] ids = {
                R.drawable.tab_pressed,
                R.drawable.tab_pressed,
                R.drawable.tab_pressed,
                R.drawable.tab_pressed,
                R.drawable.tab_pressed
        };
        return ids;
    }

    public static Class[] getFragments(){
        Class[] clz = {
                SampleFragment.class,
                SampleFragment.class,
                SampleFragment.class,
                SampleFragment.class,
                NestedFragment.class
        };
        return clz;
    }
}
