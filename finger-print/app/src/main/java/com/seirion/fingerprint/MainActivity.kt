package com.seirion.fingerprint

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.ajalt.reprint.core.AuthenticationResult
import com.github.ajalt.reprint.core.Reprint
import com.github.ajalt.reprint.rxjava2.RxReprint

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Reprint.initialize(this)
        initUi()
    }

    private fun initUi() {
        if (Reprint.isHardwarePresent() && Reprint.hasFingerprintRegistered()) {
            Log.d(TAG, "finger print ready")
            button.setOnClickListener { startBiometrics() }
        }
    }

    @SuppressLint("CheckResult")
    private fun startBiometrics() {
        Log.d(TAG, "startBiometrics()")
        text.text = "Listening"
        RxReprint.authenticate()
            .subscribe({ res ->
            when (res.status) {
                AuthenticationResult.Status.SUCCESS -> text.text = "SUCCESS"
                AuthenticationResult.Status.NONFATAL_FAILURE -> text.text = "NONFATAL_FAILURE"
                AuthenticationResult.Status.FATAL_FAILURE -> text.text = "NONFATAL_FAILURE"
            }
        }, { Log.d(TAG, "failed : $it")})
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
