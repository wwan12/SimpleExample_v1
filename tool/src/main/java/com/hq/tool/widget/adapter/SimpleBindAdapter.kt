package com.hq.tool.widget.adapter

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.BaseAdapter
import androidx.viewbinding.ViewBinding
import com.hq.tool.loge
import java.lang.reflect.InvocationTargetException


class SimpleBindAdapter<T,E:ViewBinding>(val activity: Activity,val cls: Class<E>,  val list: List<T>,val view:(T, E)->Unit):BaseAdapter() {
    override fun getCount(): Int {
        return  list.size
    }

    override fun getItem(position: Int): Any {
        return  position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val type = this::class.supertypes[0]
//        val cls = (E as ViewBinding)::class.java as Class<*>
        try {
            val inflate = cls.getDeclaredMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.javaPrimitiveType
            )
           val viewBinding = inflate.invoke(null, activity.layoutInflater, parent, false) as E
            view( list[position],viewBinding)
            return viewBinding.root
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return View(activity)
    }
}