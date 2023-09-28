package umbjm.ft.inf.produk.banner

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import umbjm.ft.inf.produk.R
import umbjm.ft.inf.produk.idcard.IdcardUpdate

class BannerAdapter(private val bannerItem: ArrayList<BannerItem>) : RecyclerView.Adapter<BannerAdapter.BannerHolder>(){

    inner class BannerHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val nameView: TextView = itemView.findViewById(R.id.namaView)
        val hargaView: TextView = itemView.findViewById(R.id.hargaView)
        val icEdit: Button = itemView.findViewById(R.id.editButton)
        val icDelete: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerAdapter.BannerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return BannerHolder(view)
    }

    override fun getItemCount(): Int {
        return bannerItem.size
    }

    override fun onBindViewHolder(holder: BannerAdapter.BannerHolder, position: Int) {
        val currentitem = bannerItem[position]
        holder.nameView.text = currentitem.nameBanner
        holder.hargaView.text = currentitem.harga
        if (!currentitem.imageBanner.isNullOrEmpty()) {
            Picasso.get().load(currentitem.imageBanner).into(holder.imageView)
        } else {
            // Handle kasus jika path gambar kosong atau null, misalnya tampilkan gambar placeholder
            Picasso.get().load(R.drawable.placeholder_image).into(holder.imageView)
        }

        holder.icEdit.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, BannerUpdate::class.java)

            // Mengirim data yang diperlukan ke aktivitas pengeditan
            intent.putExtra("idBanner", currentitem.idBanner)
            intent.putExtra("nameBanner", currentitem.nameBanner)
            intent.putExtra("imageBanner", currentitem.imageBanner)
            intent.putExtra("harga", currentitem.harga)

            // Memulai aktivitas pengeditan
            context.startActivity(intent)
        }

        holder.icDelete.setOnClickListener {
            val idBanner = currentitem.idBanner
            deleteItemFromDatabase(idBanner!!)
        }
    }

    private fun deleteItemFromDatabase(idBanner: String) {
        val database = FirebaseDatabase.getInstance("https://mydigitalprinting-60323-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Products/Banner")
        val itemReference = database.child(idBanner)

        itemReference.removeValue()
            .addOnSuccessListener {
                // Item berhasil dihapus dari database
                // Hapus item dari daftar lokal juga
                val removedItem = bannerItem.firstOrNull { it.idBanner == idBanner }
                removedItem?.let {
                    val position = bannerItem.indexOf(it)
                    bannerItem.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
            .addOnFailureListener { e ->
                // Error handling jika gagal menghapus item
            }
    }

}