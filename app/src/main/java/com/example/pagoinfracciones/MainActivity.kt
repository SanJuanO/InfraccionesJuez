package com.example.pagoinfracciones

import android.app.Activity
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pagoinfracciones.utilidades.Utilidades


import com.google.android.material.bottomnavigation.BottomNavigationView

import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*


class MainActivity : AppCompatActivity() {
    var TOKEN: String=""
    var id: String=""
    var correo= String()
    var toque:Boolean= false
    var permisos = ArrayList<String>()
    var bluetoothAdapter: BluetoothAdapter? = null
    var dispositivoBluetooth: BluetoothDevice? = null
    var bluetoothSocket: BluetoothSocket? = null

    private val REQUEST_DISPOSITIVO = 425
    private val LIMITE_CARACTERES_POR_LINEA = 32
    val TAG_DEBUG = "tag_debug"
    private val IR_A_DIBUJAR = 632
    private val COD_PERMISOS = 872
    private val INTENT_CAMARA = 123
    private val INTENT_GALERIA = 321
    private val ANCHO_IMG_58_MM = 384
    private val MODE_PRINT_IMG = 0
    // identificador unico default
    private val aplicacionUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    // Para el flujo de datos de entrada y salida del socket bluetooth
    var outputStream: OutputStream? = null
    var inputStream: InputStream? = null
    @Volatile
    private var pararLectura = false
    var URL:String="https://appis-apizaco.gesdesapplication.com/api/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val preferences = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)
        val temp = preferences.getString("correo", "")
        val t = preferences.getString("iduser", "")!!
        id=t
        correo = temp.toString()
        URL +="EditTokenUsuarios"


        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationViewinicio)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val vent = Infracciones.newInstance()
        openFragment(vent)

    }
    override fun onBackPressed() { // Añade más funciones si fuese necesario
        finish()
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {

            R.id.infracciones -> {
                tituloma.text="Infracciones"

                val inci = Infracciones.newInstance()
                openFragment(inci)
                return@OnNavigationItemSelectedListener true
            }
            R.id.perfil -> {
                tituloma.text="Perfil"
                //alertas.setBackgroundColor(R.drawable.btn_orange_light)
                val inci = Perfil.newInstance()
                openFragment(inci)
                return@OnNavigationItemSelectedListener true
            }

            R.id.PAGOS-> {
                tituloma.text="Corte"
                //alertas.setBackgroundColor(R.drawable.btn_orange_light)
                val inci = Pagos.newInstance()
                openFragment(inci)
                return@OnNavigationItemSelectedListener true
            }



        }
        false
    }
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    fun permisosobtener() {


        val datos = JSONObject()
        try {
            datos.put("Correo", correo)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val requstQueue = Volley.newRequestQueue(this)
        val progressDialog = ProgressDialog(this,
            R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST, "https://inventarios.gesdesapplication.com/api/UsuariosApi/getUsuarioByCorreo", datos,
            Response.Listener<JSONObject> { response ->
                try {
                    progressDialog?.dismiss()
                    val result = response.get("resultado") as Int
                    progressDialog?.dismiss()
                    if (result == 1) {
                        try {


                            val guias = response.getJSONObject("data")
                            id = guias.getString("id")


                            val preferencias = this.getSharedPreferences("variables", Context.MODE_PRIVATE)

                            val editor = preferencias.edit()
                            val costos = guias.getJSONArray("listaPermisos")
                            for (i in 0 until costos.length()) {
                                val producto = costos.getJSONObject(i)
                            }


                        } catch (es: Exception) {
                            Log.d("sergio1", "" + es.toString())
                            finish()
                            progressDialog?.dismiss()
                        }

                    } else {
                        Toast.makeText(this, "Articulo no encotrado", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                    progressDialog?.dismiss()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {

                }
            }
        ) {

            //here I want to post data to sever
        }

        val MY_SOCKET_TIMEOUT_MS = 15000
        val maxRetries = 2
        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                maxRetries,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )

        requstQueue.add(jsonObjectRequest)


    }



}
