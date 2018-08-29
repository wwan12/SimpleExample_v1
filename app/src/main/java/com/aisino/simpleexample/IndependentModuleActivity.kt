package com.aisino.simpleexample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.aisino.independentmodule.handwrite.LandscapeActivity
import com.aisino.independentmodule.link.NFC
import com.aisino.independentmodule.link.NFC_READ
import kotlinx.android.synthetic.main.activity_independentmodule.*

/**
 * Created by lenovo on 2017/12/6.
 */
class IndependentModuleActivity : AppCompatActivity() {
    val indList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_independentmodule)
        ind_list.createButtons(this, indList, { view, type -> toM(view, type) })
    }


    init {
        indList.apply { add("Landscape") }
        indList.apply { add("nfc") }

    }

    fun toM(view: View, type: String) {
        view.setOnClickListener {
            when (type) {
                indList[0] -> startActivityForResult(Intent(this, LandscapeActivity::class.java), 1)
                indList[1] -> {
                    val nfc = NFC(this, NFC_READ, "")

                    nfc.msgListenr = { msg: String -> Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()}
                }
            }
        }
    }
}