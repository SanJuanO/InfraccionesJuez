package com.example

import PagosModel
import android.app.Activity
import android.os.Bundle
import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pagoinfracciones.Pagos_pagados
import com.example.pagoinfracciones.R
import org.json.JSONObject

import java.util.*


class Pagosadapter() : RecyclerView.Adapter<Pagosadapter.ViewHolder>() {

    var pagosmodel: MutableList<PagosModel>  = ArrayList()
    lateinit var context:Context



    fun RecyclerAdapter(listapagos: MutableList<PagosModel>, context: Activity){
        this.pagosmodel = listapagos
        this.context = context
    }

    override fun onBindViewHolder(holder: Pagosadapter.ViewHolder, position: Int) {
        val item = pagosmodel.get(position)
        holder.bind(item, context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return Pagosadapter.ViewHolder(layoutInflater.inflate(R.layout.itemp, parent, false))
    }

    override fun getItemCount(): Int {
        return pagosmodel.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rowView = view

        val tvFolio=rowView.findViewById<TextView>(R.id.tvFolio)as TextView
        val tvciudadano=rowView.findViewById<TextView>(R.id.tvciudadano)
        val tvestatus=rowView.findViewById<TextView>(R.id.tvestatus)
        val tvfecha=rowView.findViewById<TextView>(R.id.tvfecha)
        val imgevi=rowView.findViewById<ImageView>(R.id.imginciden)


        fun bind(incidencia:PagosModel, context: Context){
            val incidencia=incidencia as PagosModel
            tvFolio.text = incidencia.FOLIO
            tvciudadano.text="ID: "+incidencia.CIUDADANO
            tvestatus.text=incidencia.ESTADO
            tvfecha.text=incidencia.FECHA

            itemView.setOnClickListener(View.OnClickListener {
                    val intent = Intent(context, Pagos_pagados::class.java)
                // start your next activity
                var bund=Bundle()
                intent.putExtra("id", incidencia.CIUDADANO)

                startActivity(context,intent,bund)
            })

        }

    }


}