package umbjm.ft.inf.produk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ProdukAdapter(private val produkItems: ArrayList<Products>) : RecyclerView.Adapter<ProdukAdapter.ProdukHolder>(){

    inner class ProdukHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val outImage: ImageView = itemView.findViewById(R.id.imageView)
        val outName: TextView = itemView.findViewById(R.id.nameView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdukHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.produk, parent, false)
        return ProdukHolder(view)
    }

    override fun getItemCount(): Int {
        return produkItems.size
    }

    override fun onBindViewHolder(holder: ProdukHolder, position: Int) {
        val currentitem = produkItems[position]
        holder.outName.text = currentitem.name
        // Periksa apakah currentitem.image tidak kosong dan tidak null
        if (!currentitem.image.isNullOrEmpty()) {
            Picasso.get().load(currentitem.image).into(holder.outImage)
        } else {
            // Handle kasus jika path gambar kosong atau null, misalnya tampilkan gambar placeholder
            Picasso.get().load(R.drawable.placeholder_image).into(holder.outImage)
        }
    }

}