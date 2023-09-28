package umbjm.ft.inf.produk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class KeranjangAdapter(private val keranjangItem: List<KeranjangItem>) :
    RecyclerView.Adapter<KeranjangAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: RecyclerView = itemView.findViewById(R.id.imagesRecyclerView)
        val namaPView: TextView = itemView.findViewById(R.id.namaPView)
        val hargaView: TextView = itemView.findViewById(R.id.hargaView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.keranjang_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return keranjangItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val keranjangItem = keranjangItem[position]

        holder.namaPView.text = keranjangItem.namaProject
        holder.hargaView.text = keranjangItem.harga
        val imagesAdapter = ImagesAdapter(keranjangItem.image ?: emptyList())
        holder.imageView.adapter = imagesAdapter
        holder.imageView.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)



    }
}
