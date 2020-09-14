package com.example.pagoinfracciones

import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pagoinfracciones.Note2
import com.example.pagoinfracciones.R
import com.example.pagoinfracciones.mAdapter2
import com.example.pagoinfracciones.mRecyclerView2

import kotlinx.android.synthetic.main.activity_pagos_pagados.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Pagos_pagados : AppCompatActivity() {

    private var listNotes2 = ArrayList<Note2>()

    var URL:String = ""

    var pk = String()
    var notesAdapter2 = NotesAdapter2(this, listNotes2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_pagados)

        pk = intent.getStringExtra("id").toString()
        getGuiasSocio()

    }

    inner class NotesAdapter2 : BaseAdapter {

        private var notesList2 = ArrayList<Note2>()
        private var context: Context? = null

        constructor(context: Context, notesList:  ArrayList<Note2>) : super() {
            this.notesList2 = notesList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.notepago, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }


            vh.fecha.text = notesList2[position].fecha
            vh.estatus.text = notesList2[position].estatus
            vh.repartidor.text = notesList2[position].repartidor
            vh.subtotal.text = notesList2[position].subtotal

            return view
        }

        override fun getItem(position: Int): Any {
            return notesList2[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return notesList2.size
        }
    }

    private class ViewHolder(view: View?) {
        val fecha: TextView
        val estatus: TextView
        val repartidor: TextView
        val subtotal: TextView

        init {
            this.fecha = view?.findViewById(R.id.pfecha) as TextView
            this.estatus = view?.findViewById(R.id.pestatus) as TextView
            this.repartidor = view?.findViewById(R.id.prepartidor) as TextView
            this.subtotal = view?.findViewById(R.id.psub) as TextView
        }
    }


    fun getGuiasSocio()
    {


        val datos = JSONObject()
        val pkk=pk.toInt()
        try {
            datos.put("Id",pkk )

        } catch (e: Exception) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, "https://infracciones.gesdesapplication.com/api/Infracciones/getCorte", datos,
            object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject) {

                    try {

                        var mensaje = response.getInt("resultado")


                        if (mensaje == 1) {

                            try {

                                val guias = response.getJSONObject("datos")
                                val tempo = guias.getString("total")
                                var tem = tempo.toDouble()

                                ptotal.text="$"+"%.2f".format(tem)
                                val fec = guias.getString("fecha")
                                pfecha.text=fec
                                val categorias: JSONArray = guias.getJSONArray("infracciones")

var a=0
                                for(i in 0..(categorias.length()-1)){
                                    a=a+1
                                    var u1 = categorias.getJSONObject(i).getString("folio")
                                    var u2 = categorias.getJSONObject(i).getString("nombreAgente")
                                    var u3 = categorias.getJSONObject(i).getString("placa")
                                    var u4 = categorias.getJSONObject(i).getString("total")
                                    var fecha = "Folio: "+u1
                                    var estatus =  "Agente: "+u2
                                    var repartidor = "Placa: "+u3
                                    var t = u4.toDouble()

                                    var sub =  "Pago: $"+"%.2f".format(t)
                                    listNotes2.add(Note2(i, fecha,estatus,repartidor,sub ))

                                }


                                ptextototal.text="Cantidad de Infracciónes: "+a.toString()




                                ppNotes.adapter = notesAdapter2

                            }catch (es :Exception){
                            }

                        }else{
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

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

