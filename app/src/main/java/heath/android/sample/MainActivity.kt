package heath.android.sample

import android.content.Intent
import android.os.Bundle
import heath.android.sample.model.ModelStringList
import me.yokeyword.fragmentation.SupportFragment

/**
 * Created by heath on 2016/1/6.
 */
class MainActivity : StringListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationTitle("Samples")
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        updateItems(R.raw.sample_data)
        setItemClickListener { parent, _, position, _ ->
            try {
                val model = parent.adapter.getItem(position) as ModelStringList
                if (model.viewType == "fragment") {
                    val fragment = Class.forName(packageName + model.viewPath)
                    start(fragment as SupportFragment)
                } else {
                    val activity = Class.forName(packageName + model.viewPath)
                    startActivity(Intent(mContext, activity).putExtra("title", model.title))
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        }
    }
}