package umbjm.ft.inf.produk.idcard

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

class IdcardAdapter(private val idcardItem: ArrayList<IdcardItem>) : RecyclerView.Adapter<IdcardAdapter.IdcardHolder>() {

    inner class IdcardHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val nameView: TextView = itemView.findViewById(R.id.namaView)
        val hargaView: TextView = itemView.findViewById(R.id.hargaView)
        val icEdit: Button = itemView.findViewById(R.id.editButton)
        val icDelete: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdcardAdapter.IdcardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return IdcardHolder(view)
    }


    override fun getItemCount(): Int {
        return idcardItem.size
    }

    override fun onBindViewHolder(holder: IdcardAdapter.IdcardHolder, position: Int) {
        val currentitem = idcardItem[position]
        holder.nameView.text = currentitem.nameIdcard
        holder.hargaView.text = currentitem.harga
        // Periksa apakah currentitem.image tidak kosong dan tidak null
        if (!currentitem.imageIdcard.isNullOrEmpty()) {
            Picasso.get().load(currentitem.imageIdcard).into(holder.imageView)
        } else {
            // Handle kasus jika path gambar kosong atau null, misalnya tampilkan gambar placeholder
            Picasso.get().load(R.drawable.placeholder_image).into(holder.imageView)
        }

        // Menambahkan OnClickListener ke tombol edit
        holder.icEdit.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, IdcardUpdate::class.java)

            // Mengirim data yang diperlukan ke aktivitas pengeditan
            intent.putExtra("idCard", currentitem.idCard)
            intent.putExtra("nameIdcard", currentitem.nameIdcard)
            intent.putExtra("imageIdcard", currentitem.imageIdcard)
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