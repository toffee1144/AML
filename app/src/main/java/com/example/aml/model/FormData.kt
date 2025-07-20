package com.example.aml.model

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Text

data class FormData(

    @SerializedName("report_uid")
    val reportUid: String = "",

    @SerializedName("userId")
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

    @SerializedName("cough_yes_no")
    val coughYesNo: String,

    val ingusFlow: Int,

    val otherSymptoms: String?,    // nullable karena optional

    @SerializedName("ingus_photo_base64")
    val ingusPhotoBase64: String? = null,

    @SerializedName("checkup_status")
    val checkupStatus: String,

    @SerializedName("doctor_hospital")
    val doctorHospital: String
)
