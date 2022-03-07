package com.nepplus.apipractice_okhttp_20220303

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.nepplus.apipractice_okhttp_20220303.databinding.ActivityLoginBinding
import com.nepplus.apipractice_okhttp_20220303.utils.ServerUtil
import org.json.JSONObject

class LoginActivity : BaseActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_login)

        setupEvents()
        setValues()
    }


    override fun setupEvents(){

        binding.btnSignUp.setOnClickListener {

            // 단순 화면 이동
            val myIntent =Intent(mContext, SignUpActivity::class.java)
            startActivity(myIntent)
        }


        binding.btnLogin.setOnClickListener {

            // id, 비번 추출
            val inputId = binding.edtId.text.toString()
            val inputPw = binding.edtPassword.text.toString()

            // API 서버에 아이디/ 비번을 보내서 실제로 회원인지 검사 => 로그인시도
            //  아이디: test@test.com
            //  비번: Test!123
            ServerUtil.postReqestLogin(inputId,inputPw, object :ServerUtil.JsonResponseHandler{
                override fun onResponse(jsonObject: JSONObject) {

                    // 화면의 입장에서, 로그인 결과를 받아서 처리할 코드
                    // 서버에 다녀오고 실행 : 라이브러리가 자동으로 백그라운데어서 돌도록 만들어둔 코드
                    val code = jsonObject.getInt("code")
                    if(code==200){
                            runOnUiThread {

//                                // 토스트를 띄우는 코드만, UI 전담 쓰레드에서 실행하도록
//                                Toast.makeText(mContext, "로그인성공", Toast.LENGTH_SHORT).show()
                                
                                //[도전과제] 로그인 한 사람의 닉네임을 추출, "000님, 환영합니다.!" 로 토스트 띄우기
                                val dataObj = jsonObject.getJSONObject("data")
                                val userObj = dataObj.getJSONObject("user")
                                val nickname =  userObj.getString("nick_name")

                                runOnUiThread {
                                    Toast.makeText(mContext, "${nickname}님, 환영합니다!", Toast.LENGTH_SHORT).show()
                                }
                                
                                // 메인 화면으로 진입
                                
                                
                            }
                    }
                    else{
                        val message = jsonObject.getString("message")

                        // 토스트: UI 조작 => 백그라운드에서 UI를 건드리면, 위험한 동작으로 간주하고  앱을 강제 종료
                            runOnUiThread {
                                
                                // 토스트를 띄우는 코드만, UI 전담 쓰레드에서 실행하도록
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                            }
                    }

                }
            })

        }
    }

    override fun setValues() {

    }


}