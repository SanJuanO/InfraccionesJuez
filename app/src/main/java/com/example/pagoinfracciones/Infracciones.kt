package com.example.pagoinfracciones

import android.app.Dialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pagoinfracciones.utilidades.Utilidades
import kotlinx.android.synthetic.main.custompopup.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class Infracciones : Fragment() {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    var myDialog: Dialog? = null
    var busqueda = String()

    private var recyclerView: RecyclerView? = null
    private var adapter: CardAdapter_busqueda? = null
    private var planetArrayList: ArrayList<Planet>? = null




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_buscar, container, false)
        val theFilter =
            rootView.findViewById<EditText>(R.id.etBuscarProductosProductosList) as EditText

        myDialog = Dialog(requireActivity());

        recyclerView = rootView.findViewById<View>(R.id.recyclerViewb) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(requireActivity())
        planetArrayList = ArrayList()
        adapter = CardAdapter_busqueda(requireActivity(), planetArrayList)
        recyclerView!!.adapter = adapter


        theFilter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                busqueda = charSequence.toString()
guardaDatos()
            }

            override fun afterTextChanged(editable: Editable) {


            }
        })


        return rootView
    }

    companion object {

        @JvmStatic
        fun newInstance() = Infracciones()
    }


    fun guardaDatos() {
        planetArrayList!!.clear()
        adapter!!.notifyDataSetChanged()


        val datos = JSONObject()
        try {
            datos.put("FOLIO", busqueda)
            datos.put("PLACA", busqueda)


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(requireActivity())
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST,
            "https://infracciones.gesdesapplication.com/api/Infracciones/Find",
            datos,
            Response.Listener { response ->
                try {
                    val result = response["resultado"] as Int
                    if (result == 1) {

                        planetArrayList!!.clear()

                        adapter!!.notifyDataSetChanged()
                        val costos = response.getJSONArray("datos")
                        for (i in 0 until costos.length()) {
                            val producto = costos.getJSONObject(i)
                            val t=  producto.getBoolean("pagado")
                            var color="#FF0000"
                            if(t){
                                color="#00B9FF"
                            }

                            var planet = Planet(
                                producto.getString("folio"),
                                producto.getString("placa") ,
                                producto.getString("noParquimetro"),
                                producto.getString("falta"),
                                producto.getString("fecha"),
                               color,""
                            )
                         planetArrayList!!.add(planet)
                        }

                        adapter!!.notifyDataSetChanged()


                    } else {
                        val error = response.getString("mensaje")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Log.e("Rest Response", error.toString())
            }
        ) { //here I want to post data to sever
        }
        val MY_SOCKET_TIMEOUT_MS = 15000
        val maxRetries = 2
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            MY_SOCKET_TIMEOUT_MS,
            maxRetries,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requstQueue.add(jsonObjectRequest)
    }

}

