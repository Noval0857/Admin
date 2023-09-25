package umbjm.ft.inf.produk.sertifikat

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
import umbjm.ft.inf.produk.idcard.IdcardItem
import umbjm.ft.inf.produk.idcard.IdcardUpdate

class SertifikatAdapter(private val sertifItem: ArrayList<SertifikatItem>) : RecyclerView.Adapter<SertifikatAdapter.SertifikatHolder>() {

    inner class SertifikatHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val nameView: TextView = itemView.findViewById(R.id.namaView)
        val hargaView: TextView = itemView.findViewById(R.id.hargaView)
        val icEdit: Button = itemView.findViewById(R.id.editButton)
        val icDelete: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SertifikatAdapter.SertifikatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return SertifikatHolder(view)
    }


    override fun getItemCount(): Int {
        return sertifItem.size
    }

    override fun onBindViewHolder(holder: SertifikatAdapter.SertifikatHolder, position: Int) {
        val currentitem = sertifItem[position]
        holder.nameView.text = currentitem.nameSertifikat
        holder.hargaView.text = currentitem.harga
        // Periksa apakah currentitem.image tidak kosong dan tidak null
        if (!currentitem.imageSertifikat.isNullOrEmpty()) {
            Picasso.get().load(currentitem.imageSertifikat).into(holder.imageView)
        } else {
            // Handle kasus jika path gambar kosong atau null, misalnya tampilkan gambar placeholder
            Picasso.get().load(R.drawable.placeholder_image).into(holder.imageView)
        }

        // Menambahkan OnClickListener ke tombol edit
        holder.icEdit.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, SertifikatUpdate::class.java)

            // Mengirim data yang diperlukan ke aktivitas pengeditan
            intent.putExtra("idSertifikat", currentitem.idSertifikat)
            intent.putExtra("nameSertifikat", currentitem.nameSertifikat)
            intent.putExtra("imageSertifikat", currentitem.imageSertifikat)
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