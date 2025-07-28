package com.example.aml

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aml.model.Hospital
import com.example.aml.model.HospitalAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class MapActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tvLocation: TextView
    private lateinit var rvHospitals: RecyclerView
    private val hospitalList = mutableListOf<Hospital>()
    private var userLat: Double? = null
    private var userLon: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        tvLocation = findViewById(R.id.tvLocation)
        rvHospitals = findViewById(R.id.rvHospitals)
        rvHospitals.layoutManager = LinearLayoutManager(this)
        rvHospitals.adapter = HospitalAdapter(hospitalList)
        val btnOpenMap = findViewById<Button>(R.id.btnOpenMap)
        btnOpenMap.setOnClickListener {
            if (userLat != null && userLon != null) {
                val gmmIntentUri = Uri.parse("geo:$userLat,$userLon?q=$userLat,$userLon(Lokasi Saya)")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

                // Jangan pakai setPackage agar lebih kompatibel
                if (mapIntent.resolveActivity(packageManager) != null) {
                    startActivity(mapIntent)
                } else {
                    // Fallback ke browser kalau Google Maps tidak bisa handle intent-nya
                    val fallbackUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$userLat,$userLon")
                    val browserIntent = Intent(Intent.ACTION_VIEW, fallbackUri)
                    startActivity(browserIntent)
                    Log.w("MapActivity", "Fallback ke browser karena Maps tidak dikenali sebagai package")
                }
            } else {
                Log.w("MapActivity", "Lokasi belum tersedia")
            }
        }


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getUserLocation()
    }

    private fun getUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                Log.d("MapActivity", "Lokasi didapat: ${location.latitude}, ${location.longitude}")
                userLat = location.latitude
                userLon = location.longitude
                getAddress(location.latitude, location.longitude)
                getNearbyHospitals(location.latitude, location.longitude)
            } else {
                Log.e("MapActivity", "Lokasi NULL")
            }
        }


    }

    private fun getAddress(lat: Double, lon: Double) {
        val url = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=$lat&lon=$lon"
        val client = OkHttpClient()
        val request = Request.Builder().url(url)
            .header("User-Agent", "AndroidApp")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val bodyString = response.body?.string() ?: ""
                Log.d("MapActivity", "Response address JSON: $bodyString")

                val json = JSONObject(bodyString)
                val address = json.getJSONObject("address")
                val locationName = listOfNotNull(
                    address.optString("suburb", null),
                    address.optString("village", null),
                    address.optString("city_district", null),
                    address.optString("city", null)
                ).joinToString(", ")

                runOnUiThread { tvLocation.text = locationName }
            }


            override fun onFailure(call: Call, e: IOException) {
                Log.e("MapActivity", "Gagal getAddress: ${e.message}")

                runOnUiThread { tvLocation.text = "Gagal dapat lokasi" }
            }
        })
    }

    private fun getNearbyHospitals(lat: Double, lon: Double) {
        val radius = 10000  // 10 km
        val overpassUrl = "https://overpass-api.de/api/interpreter"
        val query = """
            [out:json];
            (
              node["amenity"="hospital"](around:$radius,$lat,$lon);
              way["amenity"="hospital"](around:$radius,$lat,$lon);
              relation["amenity"="hospital"](around:$radius,$lat,$lon);
            );
            out center;
        """.trimIndent()

        val client = OkHttpClient()
        val body = "data=$query".toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(overpassUrl)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {

                val res = response.body?.string() ?: return
                val json = JSONObject(res)
                val elements = json.getJSONArray("elements")
                Log.d("MapActivity", "Overpass response: $res")
                hospitalList.clear()
                for (i in 0 until elements.length()) {
                    val el = elements.getJSONObject(i)
                    val tags = el.optJSONObject("tags") ?: continue
                    val name = tags.optString("name", "Unnamed Hospital")

                    val latH = el.optDouble("lat", el.optJSONObject("center")?.optDouble("lat") ?: 0.0)
                    val lonH = el.optDouble("lon", el.optJSONObject("center")?.optDouble("lon") ?: 0.0)
                    val dist = calculateDistance(lat, lon, latH, lonH)

                    hospitalList.add(Hospital(name, latH, lonH, dist))
                }

                hospitalList.sortBy { it.distance }
                runOnUiThread { rvHospitals.adapter?.notifyDataSetChanged() }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("HospitalFetch", "Error: ${e.message}")
                Log.e("MapActivity", "Gagal fetch RS: ${e.message}")

            }
        })
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
        val loc1 = Location("").apply {
            latitude = lat1
            longitude = lon1
        }
        val loc2 = Location("").apply {
            latitude = lat2
            longitude = lon2
        }
        return loc1.distanceTo(loc2).toInt()
    }
}

