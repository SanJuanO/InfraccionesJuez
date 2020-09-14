package com.example.pagoinfracciones

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import kotlinx.android.synthetic.main.activity_descuento.*
import java.io.ByteArrayOutputStream
import java.util.*


class Descuento : AppCompatActivity() {
    var falta =  ArrayList<String>()
    var ImgbaseTRASERA: String? = ""
    var ImgbaseDELANTERA: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_descuento)
        falta.add("Descuento")
        falta.add("10%")
        falta.add("20%")
        falta.add("30%")
        falta.add("40%")
        falta.add("50%")
        falta.add("60%")
        falta.add("70%")
        falta.add("80%")
        falta.add("90%")
        falta.add("100%")

        val preferencias = this.getSharedPreferences("variables", Context.MODE_PRIVATE)

        val editor = preferencias.edit()
        editor.putString("delantera", "")
        editor.putString("trasera", "")
        editor.putString("cancelar", "si")


        editor.commit()
        INETRAS.setOnClickListener(View.OnClickListener { tomarFotot() })
        INEADELANTE.setOnClickListener(View.OnClickListener { tomarFoto() })

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, falta)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        faltaa.setAdapter(adapter)
        button4.setOnClickListener(View.OnClickListener {
            var idu = faltaa.selectedItemPosition

            if(ImgbaseDELANTERA=="")
{
    Toast.makeText(this, "Falta fotografia del INE delantero", Toast.LENGTH_SHORT).show()


}
 else if(ImgbaseTRASERA=="")
{
    Toast.makeText(this, "Falta fotografia del INE trasero", Toast.LENGTH_SHORT).show()

}

else if(idu==0)
{
    Toast.makeText(this, "Selecciona un descuento", Toast.LENGTH_SHORT).show()

}
            else {
    var rest = 20 * idu
    var result = 200 - rest
                val temp= idu*10
    val preferencias = this.getSharedPreferences("variables", Context.MODE_PRIVATE)

    val editor = preferencias.edit()
    editor.putString("delantera", ImgbaseDELANTERA)
    editor.putString("trasera", ImgbaseTRASERA)
    editor.putString("cancelar", "no")
    editor.putInt("tarifanueva", result)
    editor.putInt("descuento", temp)

    editor.commit()
    finish()

}

        })

            faltaa.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                var idu=  faltaa.selectedItemPosition
                if(idu!=0) {
                    var rest = 20 * idu
                    var result = 200 - rest
                    ncostos.setText("$$result")
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        })
            button5.setOnClickListener(View.OnClickListener {

                val preferencias = this.getSharedPreferences("variables", Context.MODE_PRIVATE)

                val editor = preferencias.edit()
                editor.putString("delantera", "")
                editor.putString("trasera", "")
                editor.putString("cancelar", "si")


                editor.commit()
finish()


        })
    }

    fun tomarFoto() {
        val imageTakeIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (imageTakeIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(
                imageTakeIntent,
                1
            )
        }
    }
    fun tomarFotot() {
        val imageTakeIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (imageTakeIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(
                imageTakeIntent,
                2
            )
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // if(resultCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val extras = data!!.extras
            val imageBitmap = extras!!["data"] as Bitmap?
            val roundedBitmapDrawable =
                RoundedBitmapDrawableFactory.create(resources, imageBitmap)
            roundedBitmapDrawable.isCircular = false
            INEADELANTE.setImageDrawable(roundedBitmapDrawable)
            val encodedImage = encodeImage(imageBitmap)
            ImgbaseDELANTERA = encodedImage
        }
        // if(resultCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            val extras = data!!.extras
            val imageBitmap = extras!!["data"] as Bitmap?
            val roundedBitmapDrawable =
                RoundedBitmapDrawableFactory.create(resources, imageBitmap)
            roundedBitmapDrawable.isCircular = false
            INETRAS.setImageDrawable(roundedBitmapDrawable)
            val encodedImage = encodeImage(imageBitmap)
            ImgbaseTRASERA = encodedImage
        }
    }
    private fun encodeImage(bm: Bitmap?): String {
        val baos = ByteArrayOutputStream()
        bm!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

}
