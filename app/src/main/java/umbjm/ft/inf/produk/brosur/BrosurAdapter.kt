package umbjm.ft.inf.produk.brosur

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import umbjm.ft.inf.produk.R

class BrosurAdapter(private val brosurItem: ArrayList<BrosurItem>) : RecyclerView.Adapter<BrosurAdapter.BrosurHolder>() {

    inner class BrosurHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val nameView: TextView = itemView.findViewById(R.id.namaView)
        val hargaView: TextView = itemView.findViewById(R.id.hargaView)
        val icEdit: Button = itemView.findViewById(R.id.editButton)
        val icDelete: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrosurAdapter.BrosurHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return BrosurHolder(view)
    }


    override fun getItemCount(): Int {
        return brosurItem.size
    }

    override fun onBindViewHolder(holder: BrosurAdapter.BrosurHolder, position: Int) {
        val currentitem = brosurItem[position]
        holder.nameView.text = currentitem.nameBrosur
        holder.hargaView.text = currentitem.harga
        // Periksa apakah currentitem.image tidak kosong dan tidak null
        if (!currentitem.imageBrosur.isNullOrEmpty()) {
            Picasso.get().load(currentitem.imageBrosur).into(holder.imageView)
        } else {
            // Handle kasus jika path gambar kosong atau null, misalnya tampilkan gambar placeholder
            Picasso.get().load(R.drawable.placeholder_image).into(holder.imageView)
        }

        // Menambahkan OnClickListener ke tombol edit
        holder.icEdit.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, BrosurUpdate::class.java)

            // Mengirim data yang diperlukan ke aktivitas pengeditan
            intent.putExtra("idBrosur", currentitem.idBrosur)
            intent.putExtra("nameBrosur", currentitem.nameBrosur)
            intent.putExtra("imageBrosur", currentitem.imageBrosur)
            intent.putExtra("harga", currentitem.harga)

            // Memulai aktivitas pengeditan
            context.startActivity(intent)
        }

        // Menambahkan OnClickListener ke tombol delete
        holder.icDelete.setOnClickListener {
            // Implementasi aksi yang sesuai di sini
            // Misalnya, Anda dapat memanggil sebuah fungsi untuk menghapus item.
        }
    }
}