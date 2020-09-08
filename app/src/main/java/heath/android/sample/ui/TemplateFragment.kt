package heath.android.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import me.yokeyword.fragmentation.SupportFragment

abstract class TemplateFragment : SupportFragment() {

    abstract fun getLayoutResourceId(): Int
    abstract fun setViews(view: View)
    abstract fun setListeners(view: View)
    abstract fun initOthers(view: View)
    abstract fun bindData()

    lateinit var disposable: CompositeDisposable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        disposable = CompositeDisposable()
        val view = inflater.inflate(getLayoutResourceId(), container, false)
        view?.let {
            setViews(view)
            setListeners(view)
            initOthers(view)
        }
        return view
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        bindData()
    }

    override fun onDestroyView() {
        disposable.dispose()
        super.onDestroyView()
    }
}