package com.example.aml.homepage.checkup

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aml.model.FormData
import com.example.aml.utility.DeviceIdManager
import com.example.aml.utility.SessionManager
import java.util.UUID

class FormViewModel : ViewModel() {

    private val _reportUid = MutableLiveData(UUID.randomUUID().toString())
    val reportUid: LiveData<String> get() = _reportUid

    private val _reportName = MutableLiveData<String>()
    val reportName: LiveData<String> get() = _reportName

    private val _age = MutableLiveData<Int>()
    val age: LiveData<Int> get() = _age

    private val _sex = MutableLiveData<String>()
    val sex: LiveData<String> get() = _sex

    private val _checkupStatus = MutableLiveData<String>()
    val checkupStatus: LiveData<String> get() = _checkupStatus

    private val _doctorHospital = MutableLiveData<String>()
    val doctorHospital: LiveData<String> get() = _doctorHospital

    private val _familyHistory = MutableLiveData<String>()
    val familyHistory: LiveData<String> get() = _familyHistory

    // Likert-scale values (1–5)
    private val _symptomDuration = MutableLiveData(1)
    val symptomDuration: LiveData<Int> get() = _symptomDuration

    private val _nasalDischarge = MutableLiveData(1)
    val nasalDischarge: LiveData<Int> get() = _nasalDischarge

    private val _anosmia = MutableLiveData(1)
    val anosmia: LiveData<Int> get() = _anosmia

    private val _facialPain = MutableLiveData(1)
    val facialPain: LiveData<Int> get() = _facialPain

    private val _fever = MutableLiveData(1)
    val fever: LiveData<Int> get() = _fever

    private val _congestion = MutableLiveData(1)
    val congestion: LiveData<Int> get() = _congestion

    // Yes/No questions
    private val _painFluctuate = MutableLiveData("No")
    val painFluctuate: LiveData<String> get() = _painFluctuate

    private val _coughYesNo = MutableLiveData("No")
    val coughYesNo: LiveData<String> get() = _coughYesNo


    fun setPersonalInfo(
        reportName: String,
        age: Int,
        sex: String,
        checkup: String,
        doctorHospitalName: String,
        history: String
    ) {
        _reportName.value = reportName
        _age.value = age
        _sex.value = sex
        _checkupStatus.value = checkup
        _doctorHospital.value = doctorHospitalName
        _familyHistory.value = history
    }

    fun setScreeningInfo(
        symptomDuration: Int,
        nasalDischarge: Int,
        anosmia: Int,
        facialPain: Int,
        fever: Int,
        congestion: Int
    ) {
        _symptomDuration.value = symptomDuration
        _nasalDischarge.value = nasalDischarge
        _anosmia.value = anosmia
        _facialPain.value = facialPain
        _fever.value = fever
        _congestion.value = congestion
    }

    fun setExtraSymptoms(painFluctuate: String, coughYesNo: String) {
        _painFluctuate.value = painFluctuate
        _coughYesNo.value = coughYesNo
    }

    fun toFormData(context: Context): FormData {
        val storedId = SessionManager.getUserId(context)
        val userId = if (storedId.isBlank()) {
            DeviceIdManager.getDeviceId(context)
        } else {
            storedId
        }

        return FormData(
            userId = userId,
            reportUid = _reportUid.value ?: UUID.randomUUID().toString(),
            reportName = _reportName.value.orEmpty(),
            age = _age.value ?: 0,
            sex = _sex.value.orEmpty(),
            checkupStatus = _checkupStatus.value.orEmpty(),
            doctorHospital = _doctorHospital.value.orEmpty(),
            familyHistory = _familyHistory.value.orEmpty(),

            symptomDuration = _symptomDuration.value ?: 1,
            nasalDischarge = _nasalDischarge.value ?: 1,
            anosmia = _anosmia.value ?: 1,
            facialPain = _facialPain.value ?: 1,
            fever = _fever.value ?: 1,
            congestion = _congestion.value ?: 1,

            painFluctuate = _painFluctuate.value.orEmpty(),
            coughYesNo = _coughYesNo.value.orEmpty()
        )
    }

    fun setFromFormData(data: FormData) {
        _reportUid.value = data.reportUid
        _reportName.value = data.reportName
        _age.value = data.age
        _sex.value = data.sex
        _checkupStatus.value = data.checkupStatus
        _doctorHospital.value = data.doctorHospital
        _familyHistory.value = data.familyHistory

        _symptomDuration.value = data.symptomDuration
        _nasalDischarge.value = data.nasalDischarge
        _anosmia.value = data.anosmia
        _facialPain.value = data.facialPain
        _fever.value = data.fever
        _congestion.value = data.congestion

        _painFluctuate.value = data.painFluctuate
        _coughYesNo.value = data.coughYesNo
    }

    fun setSymptomDuration(value: Int) {
        _symptomDuration.value = value
    }

    fun getAnosmia(): Int? {
        return _anosmia.value
    }

    fun setAnosmia(value: Int) {
        _anosmia.value = value
    }

    fun getFacialPain(): Int? {
        return _facialPain.value
    }

    fun setFacialPain(value: Int) {
        _facialPain.value = value
    }

    fun getFever(): Int? {
        return _fever.value
    }

    fun setFever(value: Int) {
        _fever.value = value
    }

    fun getCongestion(): Int? {
        return _congestion.value
    }

    fun setCongestion(value: Int) {
        _congestion.value = value
    }

    private val _ingusPhotoUri = MutableLiveData<Uri>()
    val ingusPhotoUri: LiveData<Uri> get() = _ingusPhotoUri
    fun setIngusPhotoUri(uri: Uri) {
        _ingusPhotoUri.value = uri
    }

    private val _otherSymptoms = MutableLiveData<String>()
    val otherSymptoms: String? get() = _otherSymptoms.value
    fun setOtherSymptoms(value: String) {
        _otherSymptoms.value = value
    }

    fun setNasalDischarge(value: Int) {
        _nasalDischarge.value = value
    }

    fun getNasalDischarge(): Int? {
        return _nasalDischarge.value
    }

}
