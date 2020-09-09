package heath.android.sample.ui.other

import android.annotation.SuppressLint
import android.graphics.Point
import android.graphics.PointF
import android.graphics.RectF
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import heath.android.sample.R
import heath.android.sample.ui.TemplateFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_find_difference.*
import kotlinx.android.synthetic.main.fragment_find_difference.view.*
import java.util.concurrent.TimeUnit

class FindDifferenceFragment : TemplateFragment() {
    private val RECT_WIDTH = 80
    private val RECT_HEIGHT = 80
    private val coordinates = listOf(
        Point(862, 38),
        Point(619, 158),
        Point(670, 520),
        Point(95, 650),
        Point(590, 765)
    )
    private val scaledCoordinates by lazy { ArrayList<PointF>() }
    private val subjectImage1ActionDownPoint by lazy { PublishSubject.create<PointF>() }
    private val subjectImage1ActionUpPoint by lazy { PublishSubject.create<PointF>() }
    private val subjectImage2ActionDownPoint by lazy { PublishSubject.create<PointF>() }
    private val subjectImage2ActionUpPoint by lazy { PublishSubject.create<PointF>() }
    private val diffLocations by lazy { arrayListOf<RectF>() }

    private val subjectCheckedCount by lazy { BehaviorSubject.createDefault(0) }

    override fun getLayoutResourceId() = R.layout.fragment_find_difference

    override fun setViews(view: View) {
        view.imgv_1.checkImageResource = R.drawable.red_brush_circle
        view.imgv_2.checkImageResource = R.drawable.red_brush_circle
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setListeners(view: View) {
        view.imgv_1.setOnTouchListener { iv, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> subjectImage1ActionDownPoint.onNext(PointF(motionEvent.x, motionEvent.y))
                MotionEvent.ACTION_UP -> {
                    subjectImage1ActionUpPoint.onNext(PointF(motionEvent.x, motionEvent.y))
                }
            }
            true
        }
        view.imgv_2.setOnTouchListener { iv, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> subjectImage2ActionDownPoint.onNext(PointF(motionEvent.x, motionEvent.y))
                MotionEvent.ACTION_UP -> {
                    subjectImage2ActionUpPoint.onNext(PointF(motionEvent.x, motionEvent.y))
                    iv.performClick()
                }
            }
            true
        }
    }

    override fun initOthers(view: View) {

    }

    override fun bindData() {
        subjectImage1ActionDownPoint
            .switchMap {
                Observables.zip(
                    Observable.just(it),
                    subjectImage1ActionUpPoint
                )
            }
            .map { it.first }
            .subscribe { checkIfDiff(it) }
            .addTo(disposable)

        subjectImage2ActionDownPoint
            .switchMap {
                Observables.zip(
                    Observable.just(it),
                    subjectImage2ActionUpPoint
                )
            }
            .map { it.first }
            .subscribe { checkIfDiff(it) }
            .addTo(disposable)

        subjectCheckedCount
            .filter { it >= scaledCoordinates.size }
            .take(1)
            .delay(100, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Toast.makeText(requireContext(), "太棒了", Toast.LENGTH_LONG).show()
            }
            .addTo(disposable)
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        scaleCoordinate()

        scaledCoordinates.forEach {
            diffLocations.add(RectF(
                it.x - RECT_WIDTH / 2,
                it.y - RECT_HEIGHT / 2,
                it.x + RECT_WIDTH / 2,
                it.y + RECT_HEIGHT / 2
            ))
        }
//        imgv_1.updateRects(diffLocations)
//        imgv_2.updateRects(diffLocations)

        super.onEnterAnimationEnd(savedInstanceState)
    }

    private fun scaleCoordinate() {
        val imageViewWidth = imgv_1.width
        val imageViewHeight = imgv_1.height
        val widthScaleRatio = imageViewWidth / 900f
        val heightScaleRatio = imageViewHeight / 900f

        scaledCoordinates.addAll(coordinates.map { PointF(it.x * widthScaleRatio, it.y * heightScaleRatio) })
    }

    companion object {
        fun newInstance() = FindDifferenceFragment()
    }

    private fun checkIfDiff(point: PointF) {
        diffLocations.forEachIndexed { index, rect ->
            if (rect.contains(point.x, point.y)) {
                rect.setEmpty()
                imgv_1.addCheckPoint(scaledCoordinates[index])
                imgv_2.addCheckPoint(scaledCoordinates[index])
                val checkedCount = subjectCheckedCount.value!!.plus(1)
                subjectCheckedCount.onNext(checkedCount)
                return@forEachIndexed
            }
        }
    }
}