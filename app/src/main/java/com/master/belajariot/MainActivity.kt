package com.master.belajariot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var tvTemp:TextView
        var tvHumidity:TextView
        var imgLamp:ImageView
        var tvStateLamp:TextView
        var swLamp:SwitchMaterial

        tvTemp = findViewById(R.id.tvTemp)
        tvHumidity = findViewById(R.id.tvHum)
        imgLamp = findViewById(R.id.imgLamp)
        tvStateLamp = findViewById(R.id.tvLamp)
        swLamp = findViewById(R.id.swLamp)

        var dbSuhu = Firebase.database.getReference("suhu")
        dbSuhu.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // get value from firebase
                var  valueSuhu = snapshot.child("suhu").value
                var  valueHum = snapshot.child("hum").value

                // set value to TextView
                tvTemp.text = "$valueSuhu \u2103"
                tvHumidity.text = "$valueHum %"
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        swLamp.setOnCheckedChangeListener { compountButton, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "ON", Toast.LENGTH_SHORT).show()
                tvStateLamp.text = "ON"
                imgLamp.setImageDrawable(resources.getDrawable(R.drawable.light_on))

                // koneksi ke firebase (set ON)

                var onLamp = Firebase.database.getReference("lampu")
                onLamp.setValue(1)

            } else {
                Toast.makeText(this, "OFF", Toast.LENGTH_SHORT).show()
                tvStateLamp.text = "OFF"
                imgLamp.setImageDrawable(resources.getDrawable(R.drawable.light_off))

                //koneksi ke firebase (set OFF)
                var onLamp = Firebase.database.getReference("lampu")
                onLamp.setValue(0)

            }
        }
    }
}