package com.naldana.ejemplo11

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.icu.util.Output
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        var email = sharedPref.getString(getString(R.string.save_email_key), "")
        tv_data.text = email

        bt_save.setOnClickListener {

            with(sharedPref.edit()) {
                putString(getString(R.string.save_email_key), et_option.text.toString())
                commit()

            }

            tv_data.text = et_option.text.toString() // Solamente para mostrar el valor de inmediato
        }

        bt_write_external.setOnClickListener{
            email = sharedPref.getString(getString(R.string.save_email_key), "")
            val filename= "ok.txt"
            try{
                var sdprro= Environment.getExternalStorageDirectory()
                var rutaprro= File(sdprro.path, filename);
                var creaprrooooooo = OutputStreamWriter(FileOutputStream(rutaprro));
                creaprrooooooo.write(email);
                creaprrooooooo.flush();
                creaprrooooooo.close();
            }catch(e:FileNotFoundException){
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }



        bt_write_internal.setOnClickListener {
            email = sharedPref.getString(getString(R.string.save_email_key), "")
            val filename = "email.txt"
            val fileContent = "email: $email"


            openFileOutput(filename,Context.MODE_PRIVATE).use {
                it.write(fileContent.toByteArray())
            }

        }


        bt_read_internal.setOnClickListener{

            val filename = "email.txt"
            openFileInput(filename).use {
                val text = it.bufferedReader().readText()
                tv_data.text = text
            }
        }
    }
    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION)
        } else {
            write()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                write()
            }
        }
    }

    private fun write() {
        val dir = "${Environment.getExternalStorageDirectory()}/$packageName"
        File(dir).mkdirs()
        val file = "%1\$tY%1\$tm%1\$td%1\$tH%1\$tM%1\$tS.log".format(Date())
        File("$dir/$file").printWriter().use {
            it.println("text")
        }
    }


}
