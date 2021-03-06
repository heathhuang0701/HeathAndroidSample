package heath.android.sample.ui.other

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.view.RxView
import heath.android.sample.R
import heath.android.sample.ui.TemplateFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_backpressure_test.*
import kotlinx.android.synthetic.main.fragment_backpressure_test.view.*
import org.reactivestreams.Subscription
import timber.log.Timber
import java.text.SimpleDateFormat
import kotlin.random.Random.Default.nextInt

class BackpressureTestFragment : TemplateFragment() {

    private val adapter by lazy { StringAdapter() }
    private val processor by lazy { PublishProcessor.create<Pair<Int, List<Int>>>() }
    private var subscription: Subscription? = null
    private val population by lazy {
        val list = arrayListOf<Int>()
        for (i in 1..10000) list.add(i)
        list
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun getLayoutResourceId() = R.layout.fragment_backpressure_test

    override fun setViews(view: View) {
        view.recyclerView.adapter = adapter
    }

    override fun setListeners(view: View) {
        RxView.clicks(view.bt_start)
            .subscribe {
                fire()
            }
            .addTo(disposable)
    }

    override fun initOthers(view: View) {

    }

    override fun bindData() {
        processor
            .onBackpressureBuffer(100)
            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                handleData(it)
//                subscription?.request(10)
//                Timber.d("onNext request")
            }
                , {
                    it.printStackTrace()
                }
//                , {
//                    Timber.d("processor onComplete")
//                }
//                , {
//                    subscription = it
//                    it.request(10)
//                    Timber.d("onSubscribe request")
//                }
            )
            .addTo(disposable)
    }

    private fun fire() {
        Timber.i("fire start")
        for (i in 0 until 1000) {
            processor.onNext(
                Pair(i, population.shuffled().take(nextInt(50, 500)))
            )
        }
        Timber.i("fire end")
    }

    private fun handleData(pair: Pair<Int, List<Int>>) {
        Observable.fromIterable(pair.second)
            .buffer(100)
            .observeOn(Schedulers.computation())
            .map { list ->
                var sum = 0
                list.forEach { sum+=it }
                sum
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { sum ->
                val time = SimpleDateFormat("HH:mm:ss.SSS").format(System.currentTimeMillis())
                adapter.addEntry("$time $sum")
            }
            .addTo(disposable)
    }

    companion object {
        fun newInstance() = BackpressureTestFragment()
    }
}

class StringAdapter : ListAdapter<String, StringAdapter.ViewHolder>(object : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}) {

    val entries by lazy { arrayListOf<String>() }

    fun addEntry(data: String) {
        entries.add(data)
        super.submitList(entries)
    }

    override fun submitList(list: MutableList<String>?) {
        entries.clear()
        entries.addAll(list?: return)

        super.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TextView(parent.context))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)

        if (payloads.isEmpty()) {
            holder.bind(getItem(position) as String)
        } else {

        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(text: String) {
            (itemView as TextView).text = text
        }
    }
}