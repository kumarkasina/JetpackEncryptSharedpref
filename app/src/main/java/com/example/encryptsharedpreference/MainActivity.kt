package com.example.encryptsharedpreference

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EdgeEffect
import android.widget.EditText
import android.widget.Toast
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class MainActivity : AppCompatActivity() {

    //variables will be initialised.
    lateinit var masterKeyAlias: String
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // masterkey is used to encrypt data encryption keys
        masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        //(2) Get instance of EncryptedSharedPreferences class
        // as part of the params we pass the storage name, reference to
        // masterKey, context and the encryption schemes used to
        // encrypt SharedPreferences keys and values respectively.
        sharedPreferences = EncryptedSharedPreferences.create(
            "encrypted_shared_prefs",
            masterKeyAlias,
            MainActivity@this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        findViewById<Button>(R.id.encrypt_data).setOnClickListener {
            encryptAndStore()
        }

        findViewById<Button>(R.id.decrypt_data).setOnClickListener {
            descryptAndShow()
        }

    }

    private fun encryptAndStore() {

        val data = findViewById<EditText>(R.id.data)
        if(TextUtils.isEmpty(data.text.toString()))
            data.error = "Data cannot empty!!"

        //(3) Store text in sharepreferences as normally done
        //data will be encrypted as its stored
        sharedPreferences.edit().putString("DATA",data.text.toString()).apply()

        Toast.makeText(MainActivity@this,"Data encrypted and Stored!", Toast.LENGTH_SHORT).show()
    }

    //(4) retrieve text from storage and display it on a textview
    private fun descryptAndShow() {

        val data = findViewById<EditText>(R.id.data)
        // get data as it is normally done
        var restored = sharedPreferences.getString("DATA","")
        data.setText(restored)



    }

}