package com.example.aml.model

data class FormData(
    val reportUid: String,
    val userId: String,
    val reportName: String,
    val age: Int,
    val sex: String,
    val familyHistory: String,

    // Sinusitis Likert-based symptoms
    val symptomDuration: Int,     // Q1: >10 days
    val nasalDischarge: Int,      // Q2: purulent nasal discharge
    val anosmia: Int,             // Q3: loss of smell
    val facialPain: Int,          // Q4: facial pain/pressure
    val fever: Int,               // Q5: fever
    val congestion: Int,          // Q6: nasal congestion

    // Yes/No symptoms
    val painFluctuate: String,    // Q7: Yes / No
    val coughYesNo: String,      // Q8: Yes / No

    val checkupStatus: String,
    val doctorHospital: String

)
