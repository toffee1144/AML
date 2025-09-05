package com.mentari.sinuscan.homepage.checkup

import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mentari.sinuscan.model.FormData
import com.mentari.sinuscan.utility.DeviceIdManager
import com.mentari.sinuscan.utility.SessionManager
import java.util.UUID

class FormViewModel : ViewModel() {

    private val _reportUid = MutableLiveData(UUID.randomUUID().toString())
    val reportUid: LiveData<String> get() = _reportUid
    fun setReportUid(value: String) { _reportUid.value = value }
    fun getReportUid(): String? = _reportUid.value

    private val _reportName = MutableLiveData<String>()
    val reportName: LiveData<String> get() = _reportName
    fun setReportName(value: String) { _reportName.value = value }
    fun getReportName(): String? = _reportName.value

    private val _age = MutableLiveData<Int>()
    val age: LiveData<Int> get() = _age
    fun setAge(value: Int) { _age.value = value }
    fun getAge(): Int? = _age.value

    private val _sex = MutableLiveData<String>()
    val sex: LiveData<String> get() = _sex
    fun setSex(value: String) { _sex.value = value }
    fun getSex(): String? = _sex.value

    private val _checkupStatus = MutableLiveData<String>()
    val checkupStatus: LiveData<String> get() = _checkupStatus
    fun setCheckupStatus(value: String) { _checkupStatus.value = value }
    fun getCheckupStatus(): String? = _checkupStatus.value

    private val _doctorHospital = MutableLiveData<String>()
    val doctorHospital: LiveData<String> get() = _doctorHospital
    fun setDoctorHospital(value: String) { _doctorHospital.value = value }
    fun getDoctorHospital(): String? = _doctorHospital.value

    private val _familyHistory = MutableLiveData<String>()
    val familyHistory: LiveData<String> get() = _familyHistory
    fun setFamilyHistory(value: String) { _familyHistory.value = value }
    fun getFamilyHistory(): String? = _familyHistory.value

    private val _symptomDuration = MutableLiveData(1)
    val symptomDuration: LiveData<Int> get() = _symptomDuration
    fun setSymptomDuration(value: Int) { _symptomDuration.value = value }
    fun getSymptomDuration(): Int? = _symptomDuration.value

    private val _ingusFlow = MutableLiveData(1)
    val ingusFlow: LiveData<Int> get() = _ingusFlow
    fun setIngusFlow(value: Int) { _ingusFlow.value = value }
    fun getIngusFlow(): Int? = _ingusFlow.value

    private val _ingusFlowText = MutableLiveData<String>()
    val ingusFlowText: LiveData<String> get() = _ingusFlowText
    fun setIngusFlowText(value: String) { _ingusFlowText.value = value }
    fun getIngusFlowText(): String? = _ingusFlowText.value

    private val _nasalDischarge = MutableLiveData(1)
    val nasalDischarge: LiveData<Int> get() = _nasalDischarge
    fun setNasalDischarge(value: Int) { _nasalDischarge.value = value }
    fun getNasalDischarge(): Int? = _nasalDischarge.value

    private val _anosmia = MutableLiveData(1)
    val anosmia: LiveData<Int> get() = _anosmia
    fun setAnosmia(value: Int) { _anosmia.value = value }
    fun getAnosmia(): Int? = _anosmia.value

    private val _facialPain = MutableLiveData(1)
    val facialPain: LiveData<Int> get() = _facialPain
    fun setFacialPain(value: Int) { _facialPain.value = value }
    fun getFacialPain(): Int? = _facialPain.value

    private val _fever = MutableLiveData(1)
    val fever: LiveData<Int> get() = _fever
    fun setFever(value: Int) { _fever.value = value }
    fun getFever(): Int? = _fever.value

    private val _congestion = MutableLiveData(1)
    val congestion: LiveData<Int> get() = _congestion
    fun setCongestion(value: Int) { _congestion.value = value }
    fun getCongestion(): Int? = _congestion.value

    private val _coughYesNo = MutableLiveData("No")
    val coughYesNo: LiveData<String> get() = _coughYesNo
    fun setCoughYesNo(value: String) { _coughYesNo.value = value }
    fun getCoughYesNo(): String? = _coughYesNo.value

    private val _otherSymptoms = MutableLiveData<String>()
    val otherSymptoms: LiveData<String> get() = _otherSymptoms
    fun setOtherSymptoms(value: String) { _otherSymptoms.value = value }
    fun getOtherSymptoms(): String? = _otherSymptoms.value

    private val _ingusPhotoUri = MutableLiveData<Uri?>()
    val ingusPhotoUri: LiveData<Uri?> get() = _ingusPhotoUri
    fun setIngusPhotoUri(uri: Uri?) { _ingusPhotoUri.value = uri }
    fun getIngusPhotoUri(): Uri? = _ingusPhotoUri.value

    // Set all personal info at once
    fun setPersonalInfo(
        reportName: String,
        age: Int,
        sex: String,
        checkup: String,
        doctorHospitalName: String,
        history: String
    ) {
        setReportName(reportName)
        setAge(age)
        setSex(sex)
        setCheckupStatus(checkup)
        setDoctorHospital(doctorHospitalName)
        setFamilyHistory(history)
    }

    // Set all screening info at once
    fun setScreeningInfo(
        symptomDuration: Int,
        nasalDischarge: Int,
        anosmia: Int,
        facialPain: Int,
        fever: Int,
        congestion: Int,
        ingusFlow: Int
    ) {
        setSymptomDuration(symptomDuration)
        setNasalDischarge(nasalDischarge)
        setAnosmia(anosmia)
        setFacialPain(facialPain)
        setFever(fever)
        setCongestion(congestion)
        setIngusFlow(ingusFlow)
    }

    // Set extra symptoms
    fun setExtraSymptoms(coughYesNo: String) {
        setCoughYesNo(coughYesNo)
    }

    // Convert ViewModel to FormData model
    fun toFormData(context: Context): FormData {
        val storedId = SessionManager.getUserId(context)
        val userId = if (storedId.isNullOrBlank()) {
            DeviceIdManager.getDeviceId(context)
        } else {
            storedId
        }

        val photoUri = getIngusPhotoUri()
        val ingusPhotoBase64 = photoUri?.let {
            try {
                val inputStream = context.contentResolver.openInputStream(it)
                val bytes = inputStream?.readBytes()
                inputStream?.close()
                if (bytes != null) Base64.encodeToString(bytes, Base64.NO_WRAP) else null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        return FormData(
            reportUid = getReportUid() ?: UUID.randomUUID().toString(),
            userId = userId,
            reportName = getReportName().orEmpty(),
            age = getAge() ?: 0,
            sex = getSex().orEmpty(),
            familyHistory = getFamilyHistory().orEmpty(),

            symptomDuration = getSymptomDuration() ?: 1,
            nasalDischarge = getNasalDischarge() ?: 1,
            ingusFlow = getIngusFlow() ?: 1,
            anosmia = getAnosmia() ?: 1,
            facialPain = getFacialPain() ?: 1,
            fever = getFever() ?: 1,
            congestion = getCongestion() ?: 1,

            coughYesNo = getCoughYesNo().orEmpty(),
            otherSymptoms = getOtherSymptoms(),
            ingusPhotoBase64 = ingusPhotoBase64,
            checkupStatus = getCheckupStatus().orEmpty(),
            doctorHospital = getDoctorHospital().orEmpty()
        )
    }

    // Restore data from existing FormData (if needed)
    fun setFromFormData(data: FormData) {
        setReportUid(data.reportUid)
        setReportName(data.reportName)
        setAge(data.age)
        setSex(data.sex)
        setCheckupStatus(data.checkupStatus)
        setDoctorHospital(data.doctorHospital)
        setFamilyHistory(data.familyHistory)

        setSymptomDuration(data.symptomDuration)
        setNasalDischarge(data.nasalDischarge)
        setAnosmia(data.anosmia)
        setFacialPain(data.facialPain)
        setFever(data.fever)
        setCongestion(data.congestion)

        setCoughYesNo(data.coughYesNo)
        setOtherSymptoms(data.otherSymptoms ?: "")
    }
}