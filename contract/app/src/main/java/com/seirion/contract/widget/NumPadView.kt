package com.seirion.contract.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import com.seirion.contract.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


open class NumPadView : GridLayout {

    private val keyInput = PublishSubject.create<String>()

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        columnCount = COLUMN
        rowCount = ROW
        alignmentMode = GridLayout.ALIGN_BOUNDS
        removeAllViews()

        for (i in 1..10) {
            val numButton = makeNumButton(i)
            numButton.setOnClickListener { v -> keyInput.onNext((v as TextView).text.toString()) }
            numButton.layoutParams = numButtonLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT)
            addView(numButton)
        }

        addView(makeEmptyKey(), 9)

        val delete = makeDeleteKey()
        delete.setOnClickListener { keyInput.onNext(KEY_BACK) }
        addView(delete)
    }

    fun observeKeyInput(): Observable<String> {
        return keyInput
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (!changed) return

        val width = measuredWidth / COLUMN
        val height = width * 3 / 4
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            val layoutParams = view.layoutParams
            layoutParams.width = width
            layoutParams.height = height
            getChildAt(i).layoutParams = layoutParams
        }
    }

    private fun makeNumButton(number: Int): View {
        val textView = LayoutInflater.from(context).inflate(R.layout.layout_pin_item, this, false)
        if (textView is TextView) {
            textView.text = (number % 10).toString()
        }
        return textView
    }

    private fun makeEmptyKey() =
        TextView(context).apply {
            layoutParams = numButtonLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT)
            isEnabled = false
        }

    private fun makeDeleteKey(): View {
        val textView = LayoutInflater.from(context).inflate(R.layout.layout_pin_item, this, false)
        if (textView is TextView) {
            textView.text = "Del"
        }
        return textView
    }

    private fun numButtonLayoutParams(size: Int): GridLayout.LayoutParams {
        return GridLayout.LayoutParams().apply {
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            width = size
            height = size
            setGravity(Gravity.CENTER)
        }
    }

    companion object {
        private const val COLUMN = 3
        private const val ROW = 4
        const val KEY_BACK = "KEY_BACK"
    }
}