package com.hq.tool.misc

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import java.lang.Exception

object Reflect {
    fun <T>getEntity(clsName:String): T {
        val clazz = Class.forName(clsName)
        return clazz.newInstance() as T
    }

    fun <T>getEntity(clsName:String,vararg ps:Any): T {
        val clazz = Class.forName(clsName)
        val initClazz= clazz.constructors[0]
        return initClazz.newInstance(*ps) as T
    }

    fun startActivity(activity: Activity,clsName:String): Unit {
        val targetClass = Class.forName(clsName)
//        val targetInstance = targetClass.newInstance()
        val intent = Intent(activity, targetClass)
        intent.component = ComponentName(activity, targetClass)
        activity.startActivity(intent)

    }
    /**
     * 根据给定的类型名和字段名，返回R文件中的字段的值
     * @param typeName 属于哪个类别的属性 （id,layout,drawable,string,color,attr......）
     * @param fieldName 字段名 * @return 字段的值 * @throws Exception
     * */
    fun getFieldValue(typeName: String, fieldName: String, context: Context): Int {
        var i = -1
        i = try {
            val clazz = Class.forName(context.packageName.toString() + ".R$" + typeName)
            clazz.getField(fieldName).getInt(null)
        } catch (e: Exception) {
//            Log.d("" + context.getClass(),
//                "没有找到" + context.getPackageName()
//                    .toString() + ".R$" + typeName + "类型资源 " + fieldName + "请copy相应文件到对应的目录."
//            )
            return -1
        }
        return i
    }
}