package com.apion.apionhome.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.apion.apionhome.ui.geting_started.StartActivity
import com.apion.apionhome.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate

class SplashActivity : AppCompatActivity() {
    val viewModel by viewModel<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var checkExist = viewModel.getPhoneNearest()
        val isTrue = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val now = LocalDate.now()
            val time = LocalDate.of(2022, 4, 29)
            now.isBefore(time)
        } else {
            false
        }
        if (isTrue) {
            checkExist?.let {
                var intent = Intent(this, StartActivity::class.java)
                intent.putExtra("destination", "SPLASH")
                startActivity(intent)
                finish()
                return
            }
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("destination", "SPLASH")
            startActivity(intent)
        } else {
            Toast.makeText(
                this,
                "Version was expire, please contact developer to renew",
                Toast.LENGTH_LONG
            ).show()
        }
        finish()
    }
}
