package com.seirion.contract

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_pin_auth.*


class PinAuthActivity : AppCompatActivity(), View.OnClickListener {

    private var input = ArrayList<String>()
    private lateinit var state: State

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_auth)
        initUi()
    }

    private fun initUi() {
        state = if (hasPinCode()) State.INPUT else State.CREATE
        setText()
    }

    private fun setText() {
        text.setText(
            when (state) {
                State.CREATE -> R.string.pin_create
                State.CONFIRM -> R.string.pin_confirm
                State.INPUT -> R.string.pin_input
                State.RETRY -> R.string.pin_retry
            }
        )
    }

    private fun hasPinCode(): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        return !TextUtils.isEmpty(prefs.getString(PREF_PIN_CODE,null))
    }

    private fun setPinCode() {
        val str = input.reduce { acc, v -> acc + v } .toString()
        Log.v(TAG, "pin code: $str")
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .edit()
            .putString(PREF_PIN_CODE, str)
            .apply()
    }

    private fun userInput(v: View) = 0
    private fun put(num: Int) {
        if (input.size < MAX_NUM) input.add(num.toString())
    }
    private fun delete() {
        if (!input.isEmpty()) input.dropLast(1)
    }

    override fun onClick(v: View?) {
        if (v is TextView) {
            Log.d(TAG, "onClick(): ${v.text}")
            val kind = userInput(v)
            when (kind) {
                in 0..9 -> put(kind)
                DELETE -> delete()
            }
        }
    }


    companion object {
        private val TAG = PinAuthActivity::class.java.simpleName
        private const val DELETE = -1
        private const val MAX_NUM = 6
        private const val PREF_PIN_CODE = "PREF_PIN_CODE"

        fun start(activity: Activity) {
            Log.d(TAG, "$TAG.start()")
            val intent = Intent(activity, PinAuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity.startActivity(intent)
        }
    }

    enum class State {
        CREATE, CONFIRM, INPUT, RETRY
    }
}

