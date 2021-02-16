package com.android.skinex.user_Consulting

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.skinex.R
import com.android.skinex.activity.GuideLine
import com.android.skinex.activity.LoginActivity
import com.android.skinex.activity.VisiterType
import com.android.skinex.camera2Api.CameraX
import com.android.skinex.databinding.*
import com.android.skinex.publicObject.Location
import com.android.skinex.publicObject.Validation
import com.android.skinex.publicObject.Visiter
import com.android.skinex.qrscanner.QrScanner
import com.android.skinex.user_Consulting.adapters.CauseOfBurnedAdapter
import com.android.skinex.user_Consulting.adapters.PartListAdapter
import com.google.zxing.integration.android.IntentIntegrator
import java.io.*
import java.text.SimpleDateFormat
import java.time.LocalDate.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class  UserInfoActivity : AppCompatActivity() {

    private lateinit var binding: UserInfoBinding

    //on Activity Result Request Code 상수
    val REQUEST_TAKE_PHOTO_10 = 1
    val REQUEST_TAKE_PHOTO_20 = 2

        //storage 문자열
        val storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=storagewpias;AccountKey=G+ZyYwRLvxTFebMpLqsSeNI/V+1ALImJqs1MAG1rD315BN1TRO7Q8CpcKv0KOmRB9hasKF4pJqZkTEJ3TEAlPw=="

        //AzureAsyncTask에 들어가는 image uri 및 input stream 배열
//        lateinit var imageUri: Uri
//        lateinit var imageUri2: Uri
//        var inputStreamArr = ArrayList<InputStream>()
//        var imageLengthArr = ArrayList<Int>()

        //사진 원본파일 이미지 저장용 path
//        var currentPhotoPath = ""

        //촬영 모드 구분용도
        var cameraMode = ""

        var MYyear = 0
        var MYmonth = 0
        var MYday = 0

//        var popup = false

        var bodyPartFront = arrayListOf("머리", "어깨", "가슴", "배", "팔", "손", "음부", "다리", "발", "호흡기")
        var bodyPartBack = arrayListOf("머리", "어깨", "등", "허리", "팔", "손", "엉덩이", "다리", "발")
        var causeCategory = arrayListOf("열탕", "화염", "전기", "접촉", "저온", "화학", "증기", "마찰", "햇빛", "흡입")
        var arr = ArrayList<String>()

    var fHead = arrayListOf("이마", "눈", "코", "입", "귀", "볼", "목", "뒤통수", "목덜미", "기타")
    var bHead = arrayListOf("이마", "눈", "코", "입", "귀", "볼", "목", "뒤통수", "목덜미", "기타")
    var fShoulder = arrayListOf("좌측 어깨", "우측 어깨", "양쪽 어깨", "기타")
    var bShoulder = arrayListOf("좌측 어깨", "우측 어깨", "양쪽 어깨", "기타")
    var chest = arrayListOf("좌측 가슴", "우측 가슴", "가슴 전체", "기타")
    var back = arrayListOf("좌측 등", "우측 등", "등 전체", "기타")
    var belly = arrayListOf("좌측 배", "우측 배", "배 전체", "기타")
    var waist = arrayListOf("좌측 허리", "우측 허리", "허리 전체", "기타")
    var fArms = arrayListOf("좌측 팔뚝", "우측 팔뚝", "팔뚝 전체", "좌측 하박", "우측 하박", "하박 전체", "기타")
    var bArms = arrayListOf("좌측 팔뚝", "우측 팔뚝", "팔뚝 전체", "좌측 하박", "우측 하박", "하박 전체", "기타")
    var fHands = arrayListOf(
        "좌측 손바닥",
        "우측 손바닥",
        "양쪽 손바닥",
        "좌측 손등",
        "우측 손등",
        "양쪽 손등",
        "좌측 손가락",
        "우측 손가락",
        "양쪽 손가락",
        "기타"
    )
    var bHands = arrayListOf(
        "좌측 손바닥",
        "우측 손바닥",
        "양쪽 손바닥",
        "좌측 손등",
        "우측 손등",
        "양쪽 손등",
        "좌측 손가락",
        "우측 손가락",
        "양쪽 손가락",
        "기타"
    )
    var privatePart = arrayListOf("음부", "기타")
    var hip = arrayListOf("좌측 엉덩이", "우측 엉덩이", "엉덩이 전체", "기타")
    var fLegs = arrayListOf("좌측 허벅지", "우측 허벅지", "양쪽 허벅지", "좌측 종아리", "우측 종아리", "양쪽 종아리", "기타")
    var bLegs = arrayListOf("좌측 허벅지", "우측 허벅지", "양쪽 허벅지", "좌측 종아리", "우측 종아리", "양쪽 종아리", "기타")
    var fFoot = arrayListOf(
        "좌측 발등",
        "우측 발등",
        "양쪽 발등",
        "좌측 발가락",
        "우측 발가락",
        "양쪽 발가락",
        "좌측 발바닥",
        "우측 발바닥",
        "양쪽 발바닥",
        "좌측 뒤꿈치",
        "우측 뒤꿈치",
        "양쪽 뒤꿈치",
        "기타"
    )
    var bFoot = arrayListOf(
        "좌측 발등",
        "우측 발등",
        "양쪽 발등",
        "좌측 발가락",
        "우측 발가락",
        "양쪽 발가락",
        "좌측 발바닥",
        "우측 발바닥",
        "양쪽 발바닥",
        "좌측 뒤꿈치",
        "우측 뒤꿈치",
        "양쪽 뒤꿈치",
        "기타"
    )
    var respiratory = arrayListOf("호흡기", "기타")
    var m_insertUserMap = HashMap<String, String>()
        //액티비티 초기화될때 validation 전역 변수들을 초기화 시켜준다
        init {
            Validation.valiInit()
            Location.init()
//            print("testprint")
        }
    var m_testMap = HashMap<String, String>()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = UserInfoBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)

            birthCalendar()
            popupCalendar()
            bodyPartCheck()
            genderTouch()
            causeRecyclerViewActivated()
            photoGraphingAlert()
            setDescendentViews(window.decorView.rootView)
            eventInit()
            Setting()
            radiobuutonset()
            returningset()

            binding.back.setOnClickListener { back() }

            binding.mulm.setOnClickListener { guide() }

        }

    override fun onBackPressed() {
//        super.onBackPressed()
    }

    fun guide(){
        val intent = Intent(this, GuideLine::class.java)
        startActivity(intent)
    }

    fun genderset() {
        if(Visiter.Visi.gender =="남성"|| Visiter.Visi.gender == "") {
            binding.genderMan.performClick()
        } else {
            binding.genderWoman.performClick()
        }
    }

    fun returningset() {
        binding.userInfoText1.setText(Visiter.Visi.name)
        binding.birth.setText(Visiter.Visi.birth)
        binding.whenBurned.setText(Visiter.Visi.burndate)
        Log.d("성별", Visiter.Visi.gender)
        genderset()
    }

    //라디오버튼 성별 클릭시 하나만 나머지 해제
    fun radiobuutonset() {
        binding.genderRadioGroup.setOnCheckedChangeListener{
            radioGroup, checkId ->
            when(checkId) {
                R.id.genderMan ->{}
                R.id.genderWoman ->{}

            }
        }
    }

        //신체 부위 리싸이클러뷰 활성화 및 부위 버튼 활성화
        //validation을 위한 이벤트 이 펑션에 등록
        //액티비티 시작될때 초기화될 설정들 정리
        @SuppressLint("SimpleDateFormat")
        fun eventInit() {


            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

            binding.bodyFront.isChecked = true

            Log.d("Check", binding.bodyFront.isChecked.toString())

        }

    //생년월일 시기 캘린더 팝업 이벤트
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun birthCalendar(){
        var now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now().toString()
        } else {
            SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        }

        var year = now.split("-")[0]
        var month = (now.split("-")[1]).padStart(2, '0')
        var day = now.split("-")[2].padStart(2, '0')

        MYyear = year.toInt()
        MYmonth = month.toInt()
        MYday = day.toInt()

        binding.birth.setOnClickListener{
            val dp = DatePickerDialog(
                this,
                AlertDialog.THEME_HOLO_DARK,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    binding.birth.setText(
                        "${year}-${(month + 1).toString().padStart(2, '0')}-${
                            dayOfMonth.toString().padStart(
                                2,
                                '0'
                            )
                        }"
                    )
                },
                MYyear,
                MYmonth,
                MYday

            )
            dp.setOnCancelListener {
            }
            dp.datePicker.maxDate = Calendar.getInstance().timeInMillis
            dp.show()
        }
    }

    //화상입은 시기 캘린더 팝업 이벤트
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun popupCalendar(){
        var now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now().toString()
        } else {
            SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        }

        var year = now.split("-")[0]
        var month = (now.split("-")[1]).padStart(2, '0')
        var day = now.split("-")[2].padStart(2, '0')

        MYyear = year.toInt()
        MYmonth = month.toInt()
        MYday = day.toInt()

        binding.whenBurned.setOnClickListener{
            val dp = DatePickerDialog(
                this,
                AlertDialog.THEME_HOLO_DARK,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    binding.whenBurned.setText(
                        "${year}-${(month + 1).toString().padStart(2, '0')}-${
                            dayOfMonth.toString().padStart(
                                2,
                                '0'
                            )
                        }"
                    )
                },
                MYyear,
                MYmonth,
                MYday

            )
            dp.setOnCancelListener {
            }
            dp.datePicker.maxDate = Calendar.getInstance().timeInMillis
            dp.show()
        }
    }


        fun bodyPartCheck() {
            Log.d("오나", "왔다")

            binding.bodyFront.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    buttonView.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.white
                        )
                    )
                    binding.bodyBack.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.ocean_blue
                        )
                    )
                    frontActivated()
                } else {
                    buttonView.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.ocean_blue
                        )
                    )
                    binding.bodyBack.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.white
                        )
                    )
                }
            }

            binding.bodyBack.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    buttonView.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.white
                        )
                    )
                    binding.bodyFront.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.ocean_blue
                        )
                    )
                    backActivated()
                } else {
                    buttonView.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.ocean_blue
                        )
                    )
                    binding.bodyFront.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.white
                        )
                    )
                }
            }
        }

    //앞 버튼 터치시 실행되는 리싸이클러 뷰
        fun frontActivated() {
            var partListAdapter =
                    PartListAdapter(bodyPartFront)
            binding.bodyPartRecyclerView.layoutManager = LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )
            binding.bodyPartRecyclerView.adapter = partListAdapter

        binding.bodyImage.setImageResource(R.drawable.f)

        }



        //뒤 버튼 터치시 실행되는 리싸이클러 뷰
        fun backActivated() {
            var partListAdapter =
                    PartListAdapter(bodyPartBack)

            binding.bodyPartRecyclerView.layoutManager = LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )
            binding.bodyPartRecyclerView.adapter = partListAdapter

            binding.bodyImage.setImageResource(R.drawable.b)

        }


    fun Setting() {
        m_insertUserMap["PATIENTNUM"] = "Patientnum817"
        m_insertUserMap["NAME"] = "Name"
        m_insertUserMap["GENDER"] = "Patient"
        m_insertUserMap["BIRTHDAY"] = "BIRTHDAY"
        m_insertUserMap["INSERTDAY"] = "InsertDay"
        m_insertUserMap["BURNDATE"] = "BurnDate"
        m_insertUserMap["BODYSTYLE"] = "BodyStyle"
        m_insertUserMap["BODYDETAIL"] = "BodyDetail"
        m_insertUserMap["BURNSTYLE"] = "BurnStyle"
        m_insertUserMap["CAMERAURL1"] = "CAMERAURL1"
        m_insertUserMap["CAMERAURL2"] = "CAMERAURL2"
        m_insertUserMap["RESULT"] = "RESULT"

    }

        //화상 원인 버튼 카테고리 뿌려주는 펑션
        fun causeRecyclerViewActivated() {
            binding.causeOfBurnedRecyclerView.layoutManager = GridLayoutManager(this, 4)
            binding.causeOfBurnedRecyclerView.adapter =
                CauseOfBurnedAdapter(
                    causeCategory
                )
        }

        //업로드 실패 알럿
        fun failAlert(ment: String) {
            Toast.makeText(this, ment, Toast.LENGTH_SHORT).show()
//            var dialog = Dialog(this)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setContentView(R.layout.custom_alert)
//            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//            var img = dialog.img_alert
//            var title = dialog.txt_alert_title
//            var sub = dialog.txt_alert_sub
//            var btn_left = dialog.btn_alert_left
//            var btn_right = dialog.btn_alert_right
//
//            img.setImageResource(R.drawable.alert_ck_x)
//
//            title.text = "실패"
//            sub.text = ment
//
//            btn_left.visibility = View.GONE
//
//            btn_right.text = "OK"
//            btn_right.setBackgroundResource(R.drawable.btn_blue)
//
//            popup = true
//
//            dialog.setOnDismissListener {
//                popup = false
//                dialog = Dialog(this)
//            }
//
//            btn_right.setOnClickListener {
//                dialog.dismiss()
//            }
//
//            dialog.show()

        }


        //에딧 텍스트 아닌 부분 클릭시 키보드 사라지는 펑션
        fun hideKeyboard() {
            var imm = (this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)

            imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            window?.decorView?.clearFocus()
        }

        //최상위 뷰 태그 및 하위 뷰 태그에 hideKeboard를 적용하는 펑션
        fun setDescendentViews(view: View) {

            if (view !is EditText) {
                view.setOnTouchListener { v, event ->
                    hideKeyboard()
                    return@setOnTouchListener false
                }
            }

            if (view is RecyclerView) {
                view.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                        hideKeyboard()
                    }

                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        hideKeyboard()
                        return false
                    }

                    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                    }
                })
            }

            if (view is ViewGroup) {
                for (innerview in view) {
                    setDescendentViews(innerview)
                }
            }
        }

    fun back() {
        val intent = Intent(this, VisiterType::class.java)
        startActivity(intent)
    }


    //라디오버튼 성별 클릭시 하나만 나머지 해제
    fun genderTouch() {
        binding.genderMan.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked) {
                Visiter.Visi.gender = binding.genderMan.text.toString()
            }
        }
        binding.genderWoman.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked) {
                Visiter.Visi.gender = binding.genderWoman.text.toString()
            }
        }
    }

    //촬영 클릭시 전체촬영 이벤트
    fun photoGraphingAlert(){
        m_testMap["NAME"] = binding.userInfoText1.text.toString()
//        m_testMap["BIRTH"] = binding.birth.text.toString()
//        m_testMap["BRUNDATE"] = binding.whenBurned.text.toString()
        binding.longDistanceShot.setOnClickListener {
            //카메라x부분 - > QR로 수정
            //longDistancePopup()
            //var intent0 = Intent(this, CameraX::class.java) // 다음 화면으로 이동하기 위한 인텐트 객체 생성
            //intent0.putExtra("username", binding.userInfoText1.text.toString()) // 입력해준 텍스트 값을 담은 뒤 username라는 키로
            val integrator = IntentIntegrator(this)
            integrator.setBarcodeImageEnabled(true)
            integrator.setBeepEnabled(false)
            integrator.captureActivity = QrScanner::class.java
            integrator.setOrientationLocked(false) //세로모드
            integrator.initiateScan()
            Visiter.Visi.name = binding.userInfoText1.text.toString()
            Visiter.Visi.birth = binding.birth.text.toString()
            Visiter.Visi.burndate = binding.whenBurned.text.toString()
            //startActivity(intent0)
            //사진나오는 화면에 유저 정보를 담아서 보내주기위함
//            var intent3 = Intent(this, ResultInfoActivity::class.java) // 다음 화면으로 이동하기 위한 인텐트 객체 생성
//            intent3.putExtra("username", binding.userInfoText1.text.toString()) // 입력해준 텍스트 값을 담은 뒤 username라는 키로
//            startActivity(intent3)
        }
    }
//    fun photoGraphingAlert(){
//
//        m_testMap["NAME"] = binding.userInfoText1.text.toString()
////        m_testMap["BIRTH"] = binding.birth.text.toString()
////        m_testMap["BRUNDATE"] = binding.whenBurned.text.toString()
//
//
//        binding.longDistanceShot.setOnClickListener {
//            longDistancePopup()
//
//            var intent0 = Intent(this, CameraX::class.java) // 다음 화면으로 이동하기 위한 인텐트 객체 생성
//            intent0.putExtra("username", binding.userInfoText1.text.toString()) // 입력해준 텍스트 값을 담은 뒤 username라는 키로
//            Visiter.Visi.name = binding.userInfoText1.text.toString()
////            Visiter.Visi.gender = binding.genderRadioGroup.checkedRadioButtonId
//            Visiter.Visi.birth = binding.birth.text.toString()
//            Visiter.Visi.burndate = binding.whenBurned.text.toString()
//            startActivity(intent0)
//
//            //사진나오는 화면에 유저 정보를 담아서 보내주기위함
////            var intent3 = Intent(this, ResultInfoActivity::class.java) // 다음 화면으로 이동하기 위한 인텐트 객체 생성
////            intent3.putExtra("username", binding.userInfoText1.text.toString()) // 입력해준 텍스트 값을 담은 뒤 username라는 키로
////            startActivity(intent3)
//
//        }
//    }

    //전체촬영
    @SuppressLint("SetTextI18n")
    fun longDistancePopup(){
        cameraMode = "long"
        dispatchTakePictureIntent()
    }

    //핸드폰에 내장된 카메라 관련 어플들을 불러오는 펑션
    //촬영 모드 구분
    private fun dispatchTakePictureIntent() {
        startActivityForResult(
           Intent(this, CameraX::class.java), REQUEST_TAKE_PHOTO_20)
    }
}