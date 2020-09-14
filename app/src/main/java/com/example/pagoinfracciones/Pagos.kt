package com.example.pagoinfracciones

import PagosModel
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.Pagosadapter
import com.example.pagoinfracciones.R
import kotlinx.android.synthetic.main.activity_infracciones_detalle.*
import kotlinx.android.synthetic.main.fragment_pagos.*
import kotlinx.android.synthetic.main.fragment_pagos.view.*


import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat

import java.util.*
lateinit var mRecyclerView2 : RecyclerView
val mAdapter2 : Pagosadapter = Pagosadapter()


var idinf = ArrayList<Int>()



class Pagos : Fragment() {
var totalsump=0
    var listGuias= ArrayList<PagosModel>()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getGuiasSocio()
        getcortes()
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        fecha.text = currentDate
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var rootView =inflater.inflate(R.layout.fragment_pagos, container, false)




        mRecyclerView2 = rootView.listapagos as RecyclerView
        mRecyclerView2.setHasFixedSize(true)
        mRecyclerView2.layoutManager = LinearLayoutManager(activity)
        mAdapter2.RecyclerAdapter(listGuias, requireActivity())
        mRecyclerView2.adapter = mAdapter2
        rootView.paga.setOnClickListener(View.OnClickListener {

        pagar()

        })

        return rootView
    }

    companion object {

        @JvmStatic
        fun newInstance() = Pagos()
    }

    fun getGuiasSocio()
    {
        val progressDialog = ProgressDialog(requireActivity(),

        R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()

        val preferencias = this.requireActivity().getSharedPreferences("variables", Context.MODE_PRIVATE)
        var pk = preferencias.getInt("pk", 0)!!

        val datos = JSONObject()
        try {
            datos.put("IdJuez",pk )

        } catch (e: Exception) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(activity)

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, "https://infracciones.gesdesapplication.com/api/Infracciones/CorteByIdJuez", datos,
            object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject) {

                    try {
                        progressDialog?.dismiss()

                        var mensaje = response.getInt("resultado")


                        if (mensaje == 1) {

                            try {
                                val costos = response.getJSONArray("datos")
                                var j=0
                                var u=0
                                for (i in 0 until costos.length()) {
                                    u=u+1
                                    val producto = costos.getJSONObject(i)
                                     j= (j+ producto.getInt("total"))
idinf.add(producto.getInt("id"))

                                }

total.setText("$"+j.toString())
textototal.setText("Cantidad: "+u )

                                totalsump=j


                            }catch (es :Exception){
                                progressDialog?.dismiss()

                            }

                        }else{
                            progressDialog?.dismiss()

                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        progressDialog?.dismiss()

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

    fun getcortes()
    {
        val progressDialog = ProgressDialog(requireActivity(),

            R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()

        val preferencias = this.requireActivity().getSharedPreferences("variables", Context.MODE_PRIVATE)
        var pk = preferencias.getInt("pk", 0)!!

        val datos = JSONObject()
        try {
            datos.put("IdJuez",pk )

        } catch (e: Exception) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(activity)

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, "https://infracciones.gesdesapplication.com/api/Infracciones/getCortesByIdJuez", datos,
            object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject) {

                    try {
                        progressDialog?.dismiss()

                        var mensaje = response.getInt("resultado")


                        if (mensaje == 1) {

                            try {


                                val costos = response.getJSONArray("datos")
                                for (i in 0 until costos.length()) {
                                    var aux = PagosModel()
                                    var u1 = costos.getJSONObject(i).getString("fecha")
                                    var u2 = costos.getJSONObject(i).getString("id")
                                    var u3 = costos.getJSONObject(i).getString("nombreJuez")
                                    var u4 = costos.getJSONObject(i).getInt("total")
                                    var tem = u4.toDouble()
                                    aux.FECHA="Fecha: "+u1
                                    aux.CIUDADANO =u2
                                    aux.ESTADO="Juez: "+u3
                                    aux.FOLIO="Cobrado: $"+"%.2f".format(tem)

                                    listGuias.add(aux)

                                }

                                mAdapter2.RecyclerAdapter(listGuias, activity!!)
                                mRecyclerView2.adapter = mAdapter2

                            }catch (es :Exception){
                                progressDialog?.dismiss()

                            }

                        }else{
                            progressDialog?.dismiss()

                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        progressDialog?.dismiss()

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

    fun pagar()
    {
        val progressDialog = ProgressDialog(requireActivity(),

            R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()
        val idsareas = JSONArray()

        val preferencias = this.requireActivity().getSharedPreferences("variables", Context.MODE_PRIVATE)
        var pk = preferencias.getInt("pk", 0)!!

        for (a in 0..idinf.size-1) {

            val imagen = JSONObject()

             val   idi = idinf.get(a)

            imagen.put("Id",idi )
            idsareas.put(imagen)
        }

        val datos = JSONObject()
        try {
            datos.put("Total",totalsump )
            datos.put("IdJuez",pk )
            datos.put("Infracciones",idsareas )

        } catch (e: Exception) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(activity)

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, "https://infracciones.gesdesapplication.com/api/Infracciones/GeneraCorte", datos,
            object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject) {

                    try {
                        progressDialog?.dismiss()

                        var mensaje = response.getInt("resultado")


                        if (mensaje == 1) {

                            try {


                                Toast.makeText(requireActivity(), "Corte Realizado", Toast.LENGTH_SHORT).show()

                                 listGuias.clear()

                                getGuiasSocio()
                                getcortes()

                            }catch (es :Exception){
                                progressDialog?.dismiss()

                            }

                        }else{
                            progressDialog?.dismiss()

                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        progressDialog?.dismiss()

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
