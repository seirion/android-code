package com.seirion.contract

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.ajalt.reprint.core.AuthenticationFailureReason
import com.github.ajalt.reprint.core.AuthenticationResult
import com.github.ajalt.reprint.core.Reprint
import com.github.ajalt.reprint.rxjava2.RxReprint
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Reprint.initialize(this)
    }

    override fun onStart() {
        super.onStart()
        initUi()
    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
        disposable = null
    }

    private fun initUi() {
        if (!Reprint.isHardwarePresent()) {
            Log.d(TAG, "finger print not support")
            text.text = "지문 인식 미지원 기기"
        }
        else if (!Reprint.hasFingerprintRegistered()) {
            Log.d(TAG, "finger print not registered")
            text.text = "지문 등록 되어 있지 않음"
        } else {
            Log.d(TAG, "finger print ready")
            text.text = "Listening"
            startBiometrics()
        }
    }

    private fun startBiometrics() {
        Log.d(TAG, "startBiometrics()")
        disposable = RxReprint.authenticate()
            .subscribe({ res ->
            when (res.status) {
                AuthenticationResult.Status.SUCCESS -> success()
                else -> fail(res)
            }
        }, { Log.d(TAG, "failed : $it")})
    }

    private fun success() {
        MainActivity.start(this)
        finish()
    }

    private fun fail(res: AuthenticationResult) {
        Log.e(TAG, "failed : ${res.failureReason}")
        text.text = when (res.failureReason) {
            AuthenticationFailureReason.AUTHENTICATION_FAILED -> "AUTHENTICATION_FAILED"
            AuthenticationFailureReason.TIMEOUT-> "TIMEOUT"
            else -> "Unknown"
        }
        startBiometrics()
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }
}
