package com.example.aml.model

data class FormData(
    val reportUid: String,
    val userId: String,
    val reportName: String,
    val age: Int,
    val sex: String,
    val familyHistory: String,

    // Sinusitis Likert-based symptoms (1-5)
    val symptomDuration: Int,
    val nasalDischarge: Int,
    val anosmia: Int,
    val facialPain: Int,
    val fever: Int,
    val congestion: Int,

    // Yes/No symptoms
    val painFluctuate: String,
    val coughYesNo: String,

    // Tambahan dari ViewModel
    val otherSymptoms: String?,    // nullable karena optional
    val ingusPhotoUri: String?,    // Uri disimpan sebagai String path
    val checkupStatus: String,
    val doctorHospital: String
)
