package umbjm.ft.inf.produk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import umbjm.ft.inf.produk.banner.BannerActivity
import umbjm.ft.inf.produk.brosur.BrosurActivity
import umbjm.ft.inf.produk.databinding.ActivityMainBinding
import umbjm.ft.inf.produk.idcard.IdcardActivity
import umbjm.ft.inf.produk.sertifikat.SertifikatActivity
import umbjm.ft.inf.produk.sticker.StickerActivity
import umbjm.ft.inf.produk.user.UserActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnProduk.setOnClickListener {
            startActivity(Intent(this, ProductsActivity::class.java))
        }

        binding.btnBanner.setOnClickListener {
            startActivity(Intent(this, BannerActivity::class.java))
        }

        binding.btnBrosur.setOnClickListener {
            startActivity(Intent(this, BrosurActivity::class.java))
        }

        binding.btnIdcard.setOnClickListener {
            startActivity(Intent(this, IdcardActivity::class.java))
        }

        binding.btnSticker.setOnClickListener {
            startActivity(Intent(this, StickerActivity::class.java))
        }

        binding.btnSertifikat.setOnClickListener {
            startActivity(Intent(this, SertifikatActivity::class.java))
        }

        binding.btnUser.setOnClickListener {
            startActivity(Intent(this, KeranjangActivity::class.java))

        }
    }
}