package com.seirion.contract

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.seirion.contract.widget.NumPadView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_pin_auth.*


class PinAuthActivity : AppCompatActivity() {

    private var input = ArrayList<String>()
    private var inputStr = ""
    private lateinit var state: State
    private val indicator = ArrayList<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_auth)
        initUi()
    }

    @SuppressLint("CheckResult")
    private fun initUi() {
        if (hasPinCode()) {
            state = State.INPUT
            inputStr = getPinCode()
        } else {
            state = State.CREATE
        }
        setText()
        for (i in 0 until MAX_NUM) {
            indicator.add(layoutPin.getChildAt(i))
        }

        numPadView.observeKeyInput()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "key input: $it")
                if (it == NumPadView.KEY_BACK) {
                    delete()
                } else {
                    put(it)
                }
            }, {
                Log.e(TAG, "key input error: $it")
            })
    }

    private fun setText() {
        text.setText(
            when (state) {
                State.CREATE -> R.string.pin_create
                State.CONFIRM -> R.string.pin_confirm
                State.INPUT -> R.string.pin_input
                State.RETRY -> R.string.pin_retry
                else -> R.string.pin_create
            }
        )
    }

    private fun hasPinCode(): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        return !TextUtils.isEmpty(prefs.getString(PREF_PIN_CODE,null))
    }

    private fun getPinCode() =
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .getString(PREF_PIN_CODE, "")!!

    private fun setPinCode() {
        val str = input.joinToString("")
        Log.v(TAG, "pin code: $str")
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .edit()
            .putString(PREF_PIN_CODE, str)
            .apply()
    }

    private fun switchState() {
        when (state) {
            State.CREATE -> {
                state = State.CONFIRM
                inputStr = input.joinToString("")
            }
            State.CONFIRM, State.INPUT, State.RETRY -> {
                state = if (inputStr == input.joinToString("")) State.FINISH else State.RETRY
            }
        }
        setText()
    }

    private fun put(num: String) {
        if (input.size < MAX_NUM) {
            indicator[input.size].isSelected = true
            input.add(num)
            if (input.size == MAX_NUM) {
                switchState()
            }
        }
        Log.v(TAG, "put: ${input.joinToString("")}")
    }

    private fun delete() {
        if (!input.isEmpty()) {
            input.removeAt(input.size - 1)
            indicator[input.size].isSelected = false
        }
        Log.v(TAG, "delete: ${input.joinToString("")}")
    }

    companion object {
        private val TAG = PinAuthActivity::class.java.simpleName
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
        CREATE, CONFIRM, INPUT, RETRY, FINISH
    }
}

