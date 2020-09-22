package heath.android.sample.ui.other

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import heath.android.sample.R
import heath.android.sample.ui.StringListAdapter
import heath.android.sample.ui.TemplateFragment
import heath.android.sample.utils.readRawData
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import me.yokeyword.fragmentation.SupportActivity

class OtherSamples : SupportActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_container)

        if (findFragment(OtherSamplesFragment::class.java) == null) {
            loadRootFragment(R.id.fragment_container, OtherSamplesFragment())
        }
    }

    class OtherSamplesFragment : TemplateFragment() {

        private val subjectItemClick by lazy { PublishSubject.create<Int>() }
        private val adapter by lazy { StringListAdapter(subjectItemClick) }

        override fun getLayoutResourceId() = R.layout.recycler_view

        override fun setViews(view: View) {
            with(view.rootView as RecyclerView) {
                hasFixedSize()
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))

                adapter = this@OtherSamplesFragment.adapter
            }
        }

        override fun setListeners(view: View) {
            subjectItemClick
                .subscribe {
                    when(it) {
                        0 -> start(FindDifferenceFragment.newInstance())
                        1 -> start(BackpressureTestFragment.newInstance())
                    }
                }
                .addTo(disposable)
        }

        override fun initOthers(view: View) {
            adapter.setData(readRawData(requireContext(), R.raw.other_sample))
        }

        override fun bindData() {

        }
    }
}