package com.example.semproject.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.semproject.R
import com.example.semproject.model.ProductModel
import com.example.semproject.ui.activity.UpdateProductActivity

class ProductAdapter(
    val context: Context,
    var data : ArrayList<ProductModel>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){

    class ProductViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView){
        val pName : TextView = itemView.findViewById(R.id.displayName)
        val pPrice : TextView = itemView.findViewById(R.id.displayPrice)
        val pDesc : TextView = itemView.findViewById(R.id.displayDesc)
        val editBtn: TextView =itemView.findViewById(R.id.displayEdit)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView : View = LayoutInflater.from(context).inflate(R.layout.sample_product,parent,false)
        return ProductViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.pName.text=data[position].productName
        holder.pPrice.text=data[position].price.toString()
        holder.pDesc.text=data[position].productDesc


        holder.editBtn.setOnClickListener{
            val intent = Intent(context, UpdateProductActivity::class.java)
            intent.putExtra("productId",data[position].productId)
            context.startActivity(intent)
        }
    }

    fun updateData(products: List<ProductModel>){
        data.clear()
        data.addAll(products)
        notifyDataSetChanged()



    }

    fun getProductId(position: Int):String{
        return data[position].productId
    }
}

