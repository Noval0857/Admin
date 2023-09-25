package umbjm.ft.inf.produk

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class KeranjangAdapter(private val keranjangItem: List<KeranjangItem>, private val statusOptions: Array<String>) :
    RecyclerView.Adapter<KeranjangAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val namaPView: TextView = itemView.findViewById(R.id.namaPView)
        val hargaView: TextView = itemView.findViewById(R.id.hargaView)
        val statusView: TextView = itemView.findViewById(R.id.statusView)
        val statusSpinner: Spinner = itemView.findViewById(R.id.statusSpinner)
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
        holder.statusView.text = keranjangItem.status
        Picasso.get().load(keranjangItem.image).into(holder.imageView)

        // Inisialisasi ArrayAdapter untuk Spinner
        val adapter = ArrayAdapter(holder.itemView.context, android.R.layout.simple_spinner_item, statusOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.statusSpinner.adapter = adapter

        // Menangani perubahan pilihan Spinner
        holder.statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Update teks status sesuai dengan pilihan Spinner yang dipilih
                val selectedStatus = statusOptions[position]
                holder.statusView.text = selectedStatus

                // Panggil updateStatus() untuk memperbarui status pesanan di Firebase
                val idPesanan = keranjangItem.idPesanan // Dapatkan ID pesanan yang sesuai dengan item yang sedang ditampilkan
                if (idPesanan != null) {
                    updateStatus(selectedStatus, idPesanan)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected (optional)
            }
        }
    }

    private fun updateStatus(newStatus: String, idPesanan: String) {
        // Di sini Anda dapat menambahkan logika untuk memperbarui status pesanan di Firebase Database
        val database = FirebaseDatabase.getInstance().getReference("User")
        val pesananReference = database.child("Pesanan").child(idPesanan)

        // Buat objek data yang akan diperbarui
        val updateData = HashMap<String, Any>()
        updateData["status"] = newStatus // Mengganti status dengan status yang baru dipilih dari Spinner


        // Perbarui status pesanan di Firebase Database
        pesananReference.updateChildren(updateData)
            .addOnSuccessListener {
            }
            .addOnFailureListener {

            }
    }
}
