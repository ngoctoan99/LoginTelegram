package com.camera.logintelegramsdk

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.camera.logintelegramsdk.databinding.ActivityMainBinding
import org.telegram.passport.PassportScope
import org.telegram.passport.TelegramPassport
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private val payload: String = UUID.randomUUID().toString()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button: Button = findViewById(R.id.telegram)
        button.setOnLongClickListener(loginLongClickListener)
        button.setOnClickListener {
            println(payload)
            val req = TelegramPassport.AuthRequest()
            req.botID = 9592183
            req.nonce = payload
            req.publicKey ="-----BEGIN RSA PUBLIC KEY-----\n" +
                    "MIIBCgKCAQEAyMEdY1aR+sCR3ZSJrtztKTKqigvO/vBfqACJLZtS7QMgCGXJ6XIR\n" +
                    "yy7mx66W0/sOFa7/1mAZtEoIokDP3ShoqF4fVNb6XeqgQfaUHd8wJpDWHcR2OFwv\n" +
                    "plUUI1PLTktZ9uW2WE23b+ixNwJjJGwBDJPQEQFBE+vfmH0JP503wr5INS1poWg/\n" +
                    "j25sIWeYPHYeOrFp/eXaqhISP6G+q2IeTaWTXpwZj4LzXq5YOpk4bYEQ6mvRq7D1\n" +
                    "aHWfYmlEGepfaYR8Q0YqvvhYtMte3ITnuSJs171+GDqpdKcSwHnd6FudwGO4pcCO\n" +
                    "j4WcDuXc2CTHgH8gFTNhp/Y8/SpDOhvn9QIDAQAB\n" +
                    "-----END RSA PUBLIC KEY-----"
            req.scope = PassportScope(
                // PassportScopeElementOneOfSeveral(PassportScope.PASSPORT, PassportScope.IDENTITY_CARD).withSelfie(),
                // PassportScopeElementOne(PassportScope.PERSONAL_DETAILS).withNativeNames(),
                // PassportScope.DRIVER_LICENSE,
                // PassportScope.ADDRESS,
                // PassportScope.ADDRESS_DOCUMENT,
                PassportScope.PERSONAL_DETAILS,
                PassportScope.PASSPORT,
                // PassportScope.IDENTITY_CARD,
                PassportScope.PHONE_NUMBER,
                PassportScope.EMAIL,
                // PassportScope.TEMPORARY_REGISTRATION,
                // PassportScope.PASSPORT_REGISTRATION,
                // PassportScope.RENTAL_AGREEMENT,
                // PassportScope.BANK_STATEMENT,
                // PassportScope.UTILITY_BILL,
                // PassportScope.ADDRESS,
                // PassportScope.INTERNAL_PASSPORT
            )
            val tgPassportResult = 352 // this can be any integer less than 0xFFFF
            TelegramPassport.request(this@MainActivity, req, tgPassportResult)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("payload", payload)
    }

    private val loginLongClickListener = View.OnLongClickListener {
        TelegramPassport.showAppInstallAlert(this@MainActivity)
        true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 352) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            } else if (resultCode == TelegramPassport.RESULT_ERROR) {
                if (data != null) {
                    AlertDialog.Builder(this)
                        .setTitle("Fail")
                        .setMessage(data.getStringExtra("error"))
                        .setPositiveButton("ok", null)
                        .show()
                }
            } else {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
            }
        }
    }

}