package com.example.aml.model

import com.google.gson.annotations.SerializedName

data class FormData(
    @SerializedName("report_uid")
    val reportUid: String,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("report_name")
    val reportName: String,

    val age: Int,
    val sex: String,

    @SerializedName("family_history")
    val familyHistory: String,

    @SerializedName("symptom_duration")
    val symptomDuration: Int,

    @SerializedName("nasal_discharge")
    val nasalDischarge: Int,

    val anosmia: Int,

    @SerializedName("facial_pain")
    val facialPain: Int,

    val fever: Int,
    val congestion: Int,

    @SerializedName("pain_fluctuate")
    val painFluctuate: String,

    @SerializedName("cough_yes_no")
    val coughYesNo: String,

    val otherSymptoms: String?,    // nullable karena optional
    val ingusPhotoUri: String?,    // Uri disimpan sebagai String path

    @SerializedName("checkup_status")
    val checkupStatus: String,

    @SerializedName("doctor_hospital")
    val doctorHospital: String
)
