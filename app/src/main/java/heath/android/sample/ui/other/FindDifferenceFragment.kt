package heath.android.sample.ui.other

import android.graphics.Point
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import heath.android.sample.R
import heath.android.sample.ui.TemplateFragment
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_find_difference.*
import kotlinx.android.synthetic.main.fragment_find_difference.view.*
import timber.log.Timber

class FindDifferenceFragment : TemplateFragment() {
    private val RECT_WIDTH = 50
    private val RECT_HEIGHT = 50
    private val coordinates = listOf(
        Point(550, 100),
        Point(30, 400),
        Point(210, 250),
        Point(430, 400)
    )
    private val subjectImage1ActionDownPoint by lazy { PublishSubject.create<Point>() }
    private val subjectImage1ActionUpPoint by lazy { PublishSubject.create<Point>() }
    private val subjectImage2ActionDownPoint by lazy { PublishSubject.create<Point>() }
    private val subjectImage2ActionUpPoint by lazy { PublishSubject.create<Point>() }
    private val diffLocations by lazy { arrayListOf<Rect>() }

    override fun getLayoutResourceId() = R.layout.fragment_find_difference

    override fun setViews(view: View) {
        coordinates.forEach {
            diffLocations.add(Rect(
                it.x - RECT_WIDTH/2,
                it.y - RECT_HEIGHT/2,
                it.x + RECT_WIDTH/2,
                it.y + RECT_HEIGHT/2
            ))
        }
        view.imgv_1.checkImageResource = R.drawable.red_brush_circle
        view.imgv_2.checkImageResource = R.drawable.red_brush_circle
    }

    override fun setListeners(view: View) {
        view.imgv_1.setOnTouchListener { iv, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> subjectImage1ActionDownPoint.onNext(Point(motionEvent.x.toInt(), motionEvent.y.toInt()))
                MotionEvent.ACTION_UP -> {
                    subjectImage1ActionUpPoint.onNext(Point(motionEvent.x.toInt(), motionEvent.y.toInt()))
                    iv.performClick()
                }
            }
            true
        }
        view.imgv_2.setOnTouchListener { iv, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> subjectImage2ActionDownPoint.onNext(Point(motionEvent.x.toInt(), motionEvent.y.toInt()))
                MotionEvent.ACTION_UP -> {
                    subjectImage2ActionUpPoint.onNext(Point(motionEvent.x.toInt(), motionEvent.y.toInt()))
                    iv.performClick()
                }
            }
            true
        }
    }

    override fun initOthers(view: View) {
        view.imgv_1.updateRects(diffLocations)
        view.imgv_2.updateRects(diffLocations)
    }

    override fun bindData() {
        Observables.zip(
            subjectImage1ActionDownPoint,
            subjectImage1ActionUpPoint
        )
            .map { it.first }
            .subscribe {
                diffLocations.forEachIndexed { index, rect ->
                    if (rect.contains(it.x, it.y)) {
                        Timber.e("point:$it")
                        rect.setEmpty()
                        imgv_1.addCheckPoint(coordinates[index])
                        imgv_2.addCheckPoint(coordinates[index])
                        return@forEachIndexed
                    }
                }
            }
            .addTo(disposable)

        Observables.zip(
            subjectImage2ActionDownPoint,
            subjectImage2ActionUpPoint
        )
            .map { it.first }
            .subscribe {
                diffLocations.forEachIndexed { index, rect ->
                    if (rect.contains(it.x, it.y)) {
                        Timber.e("point:$it")
                        rect.setEmpty()
                        imgv_1.addCheckPoint(coordinates[index])
                        imgv_2.addCheckPoint(coordinates[index])
                        return@forEachIndexed
                    }
                }
            }
            .addTo(disposable)
    }

    companion object {
        fun newInstance() = FindDifferenceFragment()
    }
}