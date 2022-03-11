package com.nepplus.apipractice_okhttp_20220303

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

// 다른 모든 화면이 공통적으로 가질 기능 / 멤버변수를 모아두는 (부모)클래스

abstract class BaseActivity:AppCompatActivity() {

    // Context 계열의 파라미터에 대입할때 , 보통 this로 대입
    // 인터페이스가 엮이기 시작하면?  "this@어느화면"  추가로 고려

    //  미리 mContext 변수에, 화면의 this를 담아두고 => 모든 액티비티에 상속으로 물려주자
    val mContext =this

    // setupEvents / setValues 함수를 만들어두고, 물려주자
    // 실제 함수를 구현해서 물려줘봐야, 어차피 오버라이딩해서 사용한다
    //  => 차라리 추상메소드로 물려줘서,  의무로 반드시 오버라이딩하게 만들자
    abstract fun setupEvents()
    abstract  fun setValues()

    // 실제 구현 내용을 같이 물려주는 함수(일반 함수)
    // 액션바 설정 기능
    fun setCustomActionBar(){

        val defaultActionBar = supportActionBar!!
        defaultActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        defaultActionBar.setCustomView(R.layout.my_custom_action_bar)

        val toolbar =  defaultActionBar.customView.parent as Toolbar
        toolbar.setContentInsetsAbsolute(0, 0)
    }
}