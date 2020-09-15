package com.arjupta.frex

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseSource {
    private val fire: FirebaseFirestore = FirebaseFirestore.getInstance()
//    private val doc : dataDoc? = null

    fun getData() {
        val data = StringBuilder()
        data.append("Name,Age,Email")
        val ref :CollectionReference = fire.collection("newrsj").document("Records").collection("1")
        ref.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    data.append("""${document["name"]},${document["age"]},${document["email"]}""".trimIndent())
                }
            }
        data.toString()
    }

}