package com.example.tdm2_tp2_exo2.contact

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tdm2_tp2_exo2.R
import kotlinx.android.synthetic.main.contact_child.view.*

class ContactAdapter(items : List<Contact>, ctx: Context) : RecyclerView.Adapter<ContactAdapter.ViewHolder>(){

    private var list = items
    private var context = ctx

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = list[position].name
        holder.num.text = list[position].num
        holder.email.text = list[position].email
        holder.select = list[position].select
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.contact_child,
                parent,
                false
            )
        )
    }




    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val name = v.name!!
        val num = v.num!!
        val email = v.email!!
        var select = v.select!!.didTouchFocusSelect()
    }
}