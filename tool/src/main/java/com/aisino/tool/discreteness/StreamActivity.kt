package com.aisino.tool.discreteness

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.aisino.tool.ani.CircularAnim
import com.aisino.tool.ani.LoadAnim
import com.aisino.tool.discreteness.StreamActivity.app.mApplication

/**
 * Created by lenovo on 2017/12/6.
 */

class StreamActivity : AppCompatActivity() {

    object app{
        lateinit var mApplication:Application
    }

    val _result: ArrayList<(requestCode: Int, resultCode: Int, data: Intent?) -> Unit> = ArrayList()
    val _pause: ArrayList<() -> Unit> = ArrayList()
    val _restart: ArrayList<() -> Unit> = ArrayList()
    var loadLog: AlertDialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (mApplication==null){
            mApplication=application
        }
    }

    override fun onPause() {
        super.onPause()
        runStack(_pause)
    }


    override fun onRestart() {
        super.onRestart()
        runStack(_restart)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (_result.size == 0) {
            return
        } else {
            for (stack in _result) {
                stack(requestCode,resultCode,data)
            }
            _result.clear()
        }
    }

    protected fun View.setOnJumpListenter(cls: Class<*>) {
        this.setOnClickListener { v ->
            CircularAnim().fullActivity(this@StreamActivity, this).go(object : CircularAnim.OnAnimationEndListener {
                override fun onAnimationEnd() {
                    startActivity(Intent(this@StreamActivity, cls))
                }
            })
        }
    }

    private fun runStack(stacks: ArrayList<() -> Unit>) {
        if (stacks.size == 0) {
            return
        } else {
            for (stack in stacks) {
                stack()
            }
            stacks.clear()
        }
    }


    protected fun showLoad(): Unit {
        val load = AlertDialog.Builder(this)
        load.setView(LoadAnim(this))
        loadLog = load.show()
    }

    protected fun hideLoad(): Unit {
        if (loadLog != null && loadLog?.isShowing!!) {
            loadLog?.dismiss()
        }
    }
}