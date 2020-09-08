package heath.android.sample.ui

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import heath.android.sample.R
import heath.android.sample.model.ModelStringList
import heath.android.sample.utils.sspToPx
import io.reactivex.subjects.PublishSubject

class StringListAdapter(
    private val clickEvent: PublishSubject<Int>
) : RecyclerView.Adapter<StringListAdapter.ViewHolder>() {

    private val data by lazy { arrayListOf<ModelStringList>() }

    fun setData(newData: ArrayList<ModelStringList>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            android.R.layout.simple_list_item_2,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            clickEvent.onNext(position)
        }
    }

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: ModelStringList) {
            val tvTitle = itemView.findViewById<TextView>(android.R.id.text1)
            val tvDescription = itemView.findViewById<TextView>(android.R.id.text2)
            tvTitle.setTypeface(null, Typeface.BOLD)
            tvTitle.textSize = sspToPx(itemView.context, R.dimen._14ssp)
            tvTitle.text = model.title
            tvDescription.text = model.description
        }
    }
}