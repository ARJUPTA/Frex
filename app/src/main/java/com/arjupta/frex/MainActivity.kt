package com.arjupta.frex

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    val data = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseAuth.getInstance().signInAnonymously()
        getData()
    }

    fun export(view: View?) {
        //generate data
//        val data = StringBuilder()
//        data.append("Time,Distance")
//        for (i in 0..4) {
//            data.append("""$i,${i * i}""".trimIndent())
//        }
        try {
            //saving the file into device
            val h = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();
            val out: FileOutputStream = openFileOutput("$h.csv", Context.MODE_PRIVATE)
            out.write(data.toString().toByteArray())
            out.close()

            //exporting
            val context: Context = applicationContext
            val filelocation = File(filesDir, "$h.csv")
            val path: Uri = FileProvider.getUriForFile(
                context,
                "com.arjupta.frex.fileprovider",
                filelocation
            )
            val fileIntent = Intent(Intent.ACTION_SEND)
            fileIntent.type = "text/csv"
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data")
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            fileIntent.putExtra(Intent.EXTRA_STREAM, path)
            startActivity(Intent.createChooser(fileIntent, "Send mail"))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    private val fire: FirebaseFirestore = FirebaseFirestore.getInstance()
//    private val doc : dataDoc? = null

    fun getData() {
        data.append("Name,Age,Email")
        val ref : CollectionReference = fire.collection("newrsj").document("Records").collection("1")
        ref.get()
            .addOnSuccessListener { result ->
                for (document in result) {
//                    data.append("""${document["name"]},${document["age"]},${document["email"]}""".trimIndent())
                    data.append(
                        """
                    
                    ${document["name"]},${document["age"]},${document["email"]}
                    """.trimIndent()
                    )
                }
                Toast.makeText(this,"Done",Toast.LENGTH_LONG).show()
            }

    }
}