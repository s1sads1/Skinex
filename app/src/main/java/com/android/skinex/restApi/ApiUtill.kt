package com.android.skinex.restApi

import android.util.Log

//import com.phonegap.WPIAS.restApi.ApiInterface

class ApiUtill {

    // **************************** U S E R ****************************

    // 회원가입
    val insert_visiter_url : String = "https://172.24.192.1:5000/INSERT_VISITER/"

    fun getINSERT_VISITER() : ApiInterface {
        Log.d("URL", "d:"+ insert_visiter_url.toString())
        return ApiRequest().getStringResponse(insert_visiter_url)!!.create(
            ApiInterface::class.java)
    }

//    // 회원정보조회
//    val select_user_url : String = "https://wpias.azurewebsites.net/SELECT_USERWITHTOKENOS/"
//
//    fun getSELECT_USERWITHTOKENOS() : ApiInterface {
//        return ApiRequest().getResponse(select_user_url)!!.create(ApiInterface::class.java)
//    }
//
//    // 사용자 질문조회
//    val select_myquestion_url : String = "https://wpias.azurewebsites.net/SELECT_MYQUESTION/"
//
//    fun getSELECT_MYQUESTION() : ApiInterface {
//        return ApiRequest().getResponse(select_myquestion_url)!!.create(
//            ApiInterface::class.java)
//    }
//
//    // 사용자 질문케이스조회
//    val select_mycase_url : String = "https://wpias.azurewebsites.net/SELECT_MYCASE/"
//
//    fun getSELECT_MYCASE() : ApiInterface {
//        return ApiRequest().getResponse(select_mycase_url)!!.create(ApiInterface::class.java)
//    }
//
//    // 경과추가 답변미요청
//    val insert_case_url : String = "https://wpias.azurewebsites.net/INSERT_CASE/"
//
//    fun getINSERT_CASE() : ApiInterface {
//        return ApiRequest().getStringResponse(insert_case_url)!!.create(
//            ApiInterface::class.java)
//    }
//
//    // 경과추가 답변요청
//    val insert_caserequest_url : String = "https://wpias.azurewebsites.net/INSERT_CASEREQUEST/"
//
//    fun getINSERT_CASEREQUEST() : ApiInterface {
//        return ApiRequest().getStringResponse(insert_caserequest_url)!!.create(
//            ApiInterface::class.java)
//    }
//
//
//    // 답변 리뷰 등록
//    val update_feedback_url : String = "https://wpias.azurewebsites.net/UPDATE_FEEDBACK/"
//
//    fun getUPDATE_FEEDBACK() : ApiInterface {
//        return ApiRequest().getStringResponse(update_feedback_url)!!.create(
//            ApiInterface::class.java)
//    }
//
    // 사용자 질문등록
    val insert_question_url : String = "https://172.18.208.1:5000/INSERT_QUESTION/"

    fun getINSERT_QUESTION() : ApiInterface {
        return ApiRequest().getStringResponse(insert_question_url)!!.create(
            ApiInterface::class.java)
    }
//
//    // 사용자 질문등록 + 위치추가
//    val insert_questionwitharea_url : String = "https://wpias.azurewebsites.net/INSERT_QUESTIONWITHAREA/"
//
//    fun getINSERT_QUESTIONWITHAREA() : ApiInterface {
//        return ApiRequest().getStringResponse(insert_questionwitharea_url)!!.create(
//            ApiInterface::class.java)
//    }
//
//    // 설정창 스위치1 변경
//    val update_switch1_url : String = "https://wpias.azurewebsites.net/UPDATE_SWITCH1/"
//
//    fun getUPDATE_SWITCH1() : ApiInterface {
//        return ApiRequest().getStringResponse(update_switch1_url)!!.create(
//            ApiInterface::class.java)
//    }
//
//    // 설정창 스위치2 변경
//    val update_switch2_url : String = "https://wpias.azurewebsites.net/UPDATE_SWITCH2/"
//
//    fun getUPDATE_SWITCH2() : ApiInterface {
//        return ApiRequest().getStringResponse(update_switch2_url)!!.create(
//            ApiInterface::class.java)
//    }
//
//    //PUSH 메세지 전송
//    val send_FCM_url : String = "https://fcm.googleapis.com/fcm/send/"
//
//    fun sendFCM(): ApiInterface {
//        return ApiRequest().getInterAction(send_FCM_url)!!.create(ApiInterface::class.java)
//    }
//
//    // 로그아웃시 토큰 공백으로 변경
//    val update_tokenlogout_url : String = "https://wpias.azurewebsites.net/UPDATE_TOKENLOGOUT/"
//
//    fun getUPDATE_TOKENLOGOUT() : ApiInterface {
//        return ApiRequest().getStringResponse(update_tokenlogout_url)!!.create(
//            ApiInterface::class.java)
//    }
//
//
//    // 상대방 푸시정보 조회
//    val select_checkagree_url : String = "https://wpias.azurewebsites.net/SELECT_CHECKAGREE/"
//
//    fun getSELECT_CHECKAGREE() : ApiInterface {
//        return ApiRequest().getResponse(select_checkagree_url)!!.create(
//            ApiInterface::class.java)
//    }
//
//    // 시,도 조회
//    val select_city_url : String = "https://wpias.azurewebsites.net/SELECT_CITY/"
//
//    fun getSELECT_CITY() : ApiInterface {
//        return ApiRequest().getResponse(select_city_url)!!.create(
//            ApiInterface::class.java)
//    }
//
//    // 구 조회
//    val select_district_url : String = "https://wpias.azurewebsites.net/SELECT_DISTRICT/"
//
//    fun getSELECT_DISTRICT() : ApiInterface {
//        return ApiRequest().getResponse(select_district_url)!!.create(
//            ApiInterface::class.java)
//    }

    // 사용자 질문등록 + 위치추가 + 거주지 추가
    val insert_questionwitharea_url2 : String = "https://wpias.azurewebsites.net/INSERT_QUESTIONWITHAREA2/"

    fun getINSERT_QUESTIONWITHAREA2() : ApiInterface {
        return ApiRequest().getStringResponse(insert_questionwitharea_url2)!!.create(
            ApiInterface::class.java)
    }


//    // **************************** DOCTOR ****************************
//
//    // 신규 질문 조회
//    val select_newquestion_url : String = "https://wpias.azurewebsites.net/SELECT_NEWQUESTION/"
//
//    fun getSELECT_NEWQUESTION() : ApiInterface {
//        return ApiRequest().getResponse(select_newquestion_url)!!.create(
//            ApiInterface::class.java)
//    }
//
//    // 의사 답변횟수 조회
//    val select_myanswercount_url : String = "https://wpias.azurewebsites.net/SELECT_MYANSWERCOUNT/"
//
//    fun getSELECT_MYANSWERCOUNT() : ApiInterface {
//        return ApiRequest().getResponse(select_myanswercount_url)!!.create(
//            ApiInterface::class.java)
//    }
//
//    // 질문 Lock Check
//    val select_lockcheck_url : String = "https://wpias.azurewebsites.net/SELECT_LOCKCHECK/"
//
//    fun getSELECT_LOCKCHECK() : ApiInterface {
//        return ApiRequest().getResponse(select_lockcheck_url)!!.create(
//            ApiInterface::class.java)
//    }
//
//    // Lock 상태 업데이트
//    val update_lock_url : String = "https://wpias.azurewebsites.net/UPDATE_LOCK/"
//
//    fun getUPDATE_LOCK() : ApiInterface {
//        return ApiRequest().getStringResponse(update_lock_url)!!.create(
//            ApiInterface::class.java)
//    }
//
//    // Lock 상태 업데이트
//    val select_newcase_url : String = "https://wpias.azurewebsites.net/SELECT_NEWCASE/"
//
//    fun getSELECT_NEWCASE() : ApiInterface {
//        return ApiRequest().getResponse(select_newcase_url)!!.create(
//            ApiInterface::class.java)
//    }
//
//    // Lock 상태 업데이트
//    val update_firstanswer_url : String = "https://wpias.azurewebsites.net/UPDATE_FIRSTANSWER/"
//
//    fun getUPDATE_FIRSTANSWER() : ApiInterface {
//        return ApiRequest().getStringResponse(update_firstanswer_url)!!.create(
//            ApiInterface::class.java)
//    }
//
//    // 내가 한 답변 조회(의사)
//    val select_myanswer200106_url : String = "https://wpias.azurewebsites.net/SELECT_MYANSWER200106/"
//
//    fun getSELECT_MYANSWER200106() : ApiInterface {
//        return ApiRequest().getResponse(select_myanswer200106_url)!!.create(
//            ApiInterface::class.java)
//    }
//
//    // 의사 계정 내가 한 답변 경과 조회
//    val select_myanswercase_url : String = "https://wpias.azurewebsites.net/SELECT_MYANSWERCASE/"
//
//    fun getSELECT_MYANSWERCASE() : ApiInterface {
//        return ApiRequest().getResponse(select_myanswercase_url)!!.create(
//            ApiInterface::class.java)
//    }
//
//    // 사용자 리뷰에 댓글 등록
//    val update_reply_url : String = "https://wpias.azurewebsites.net/UPDATE_REPLY/"
//
//    fun getUPDATE_REPLY() : ApiInterface {
//        return ApiRequest().getStringResponse(update_reply_url)!!.create(ApiInterface::class.java)
//    }
}