package com.example.tdm2_tp2_exo2.contact

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tdm2_tp2_exo2.R
import kotlinx.android.synthetic.main.contact_child.view.*

class  ContactAdapter(var clickListner: OnContactListener, var listcontact:List<Contact>) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    private val TAG: String = "AppDebug"

    private var items=listcontact



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContactViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.contact_child,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {

            is ContactViewHolder -> {
                holder.bind(items.get(position), clickListner)
            }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(contact: List<Contact>){
        items = contact
    }

    class ContactViewHolder
    constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){

        val name = itemView.name
        val num = itemView.num
        val email = itemView.email
        val select= itemView.select

        fun bind(contact: Contact, action: OnContactListener){


            name.setText(contact.name)
            num.setText(contact.num)
            email.setText(contact.email)
            if(contact.select){
                select.setChecked(true)

            }

            itemView.setOnClickListener{
                action.onContactClick(contact, adapterPosition )
            }



        }



    }

    public interface OnContactListener {
        fun onContactClick(contact: Contact, position: Int)
    }

}