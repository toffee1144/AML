
📝 Changelog
🚧 Pertanyaan kini ditampilkan per halaman (1-per-page)
Sebelumnya semua pertanyaan tampil di satu halaman, kini ditampilkan satu per satu agar lebih user-friendly dan fokus.

👤 Tampilan awal laman profil + tombol logout
Menambahkan halaman profil awal yang menampilkan nama pengguna dan email, serta fitur logout.

🆕 Menambahkan tampilan detail profil

Halaman ProfilIdentityActivity dan DetailProfilActivity kini menampilkan informasi pengguna dari SessionManager.

Desain responsif sesuai mockup.

🔧 Perbaikan fungsi laporan

Menambahkan kolom dob (date of birth) ke tabel users.

Informasi DOB disimpan di database dan ditampilkan di profil (pending API).

Struktur JSON login diperluas untuk menampung data tambahan.

🛠️ To Do
✏️ Tambahkan fungsi edit data identitas pengguna

🧩 Lengkapi isi tabel users di database dengan kolom:

age (tidak diperlukan jika ada dob)

date_of_birth

sex

📦 Fungsionalitas edit profil

Kirim perubahan ke server

Update session data

Validasi form input (email, password, DOB)

🗂️ Set up lokasi penyimpanan foto pengguna

Tentukan: internal storage, upload server, atau cloud

⏳ Auto logout saat token expired
(jika backend menggunakan JWT di masa depan)
