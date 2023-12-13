package com.hq.tool.misc

import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

//    var regularExpression =
//        "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
//                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)"

        /**
         * 车牌号码Pattern
         */
       private val PLATE_NUMBER_PATTERN: Pattern = Pattern
            .compile("^[\u0391-\uFFE5]{1}[a-zA-Z0-9]{6}$")

        /**
         * 编码Pattern
         */
        private val CODE_PATTERN: Pattern = Pattern
            .compile("^[a-zA-Z0-9]+$")

        /**
         * 固定电话编码Pattern
         */
        private val PHONE_NUMBER_PATTERN: Pattern = Pattern
            .compile("0\\d{2,3}-[0-9]+")

        /**
         * 邮政编码Pattern
         */
        private val POST_CODE_PATTERN: Pattern = Pattern.compile("\\d{6}")

        /**
         * 面积Pattern
         */
        private val AREA_PATTERN: Pattern = Pattern.compile("\\d*.?\\d*")


    /**
     * 手机号码Pattern
     */
    private val MOBILE_NUMBER_PATTERN: Pattern = Pattern
            .compile("^(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57]|19[89]|166)[0-9]{8}")
private val ID_CARD: Pattern = Pattern.compile(
            "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}(" +
                    "(0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"
    )

/**
 * 银行帐号Pattern
 */
val ACCOUNT_NUMBER_PATTERN: Pattern = Pattern
        .compile("\\d{16,21}")


    fun String.isNumeric(): Boolean {
        val pattern: Pattern = Pattern.compile("[0-9]*")
        val isNum: Matcher = pattern.matcher(this)
        return isNum.matches()
    }





        /**
         * 车牌号码是否正确
         *
         * @param s
         * @return
         */
        fun String.isPlateNumber(): Boolean {
            val m: Matcher = PLATE_NUMBER_PATTERN.matcher(this)
            return m.matches()
        }

        /**
         * 证件号码是否正确
         *
         * @param s
         * @return
         */
        fun String.isIDCode(): Boolean {
            val m: Matcher = ID_CARD.matcher(this)
            return m.matches()
        }

/// <summary>
/// 身份证
/// </summary>
/// <param name="Id"></param>
/// <returns></returns>
fun String.isIDCodePro(): Boolean {
    return when (length) {
        15 -> {
            try {
                val n = toLong()
                if (n < Math.pow(10.0, 14.0)) {
                    return false
                }
                val address =
                    "11x22x35x44x53x12x23x36x45x54x13x31x37x46x61x14x32x41x50x62x15x33x42x51x63x21x34x43x52x64x65x71x81x82x91"
                if (!address.contains(substring(0,2))) {
                    return false
                }
                val birth = StringBuilder(substring(6, 14))
                birth.insert(4, "-")
                birth.insert(7, "-")
                val time = Date()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd");
                if (dateFormat.parse(birth.toString()) == null) {
                    return false
                }
                true
                //正确
            } catch (e: Exception) {
                false
            }
        }
        18 -> {
            try {
                val n = substring(0, 17).toLong()
                if (n < Math.pow(10.0, 16.0)) {
                    return false
                }
                val address =
                    "11x22x35x44x53x12x23x36x45x54x13x31x37x46x61x14x32x41x50x62x15x33x42x51x63x21x34x43x52x64x65x71x81x82x83x91"
                if (!address.contains(substring(0,2))) {
                    return false
                }
                val birth = StringBuilder(substring(6, 14))
                birth.insert(4, "-")
                birth.insert(7, "-")
                val dateFormat = SimpleDateFormat("yyyy-MM-dd");
                if (dateFormat.parse(birth.toString()) == null) {
                    return false
                }
                val arrVarifyCode = "1,0,x,9,8,7,6,5,4,3,2".split(",").toTypedArray()
                val Wi = "7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2".split(",").toTypedArray()
                val Ai = substring(0, 17).toCharArray()
                var sum = 0
                for (i in 0..16) {
                    sum += Wi[i].toInt() * Ai[i].toString().toInt()
                }
                val y = sum % 11
                if (arrVarifyCode[y] != substring(17, 18).toLowerCase()) {
                    return false
                }
                true //正确
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
        else -> {
            false
        }
    }
}


        /**
         * 编码是否正确
         *
         * @param s
         * @return
         */
        fun String.isCode(): Boolean {
            val m: Matcher = CODE_PATTERN.matcher(this)
            return m.matches()
        }

        /**
         * 固话编码是否正确
         *
         * @param s
         * @return
         */
        fun String.isPhoneNumber(): Boolean {
            val m: Matcher = PHONE_NUMBER_PATTERN.matcher(this)
            return m.matches()
        }

        /**
         * 邮政编码是否正确
         *
         * @param s
         * @return
         */
        fun String.isPostCode(): Boolean {
            val m: Matcher = POST_CODE_PATTERN.matcher(this)
            return m.matches()
        }

        /**
         * 面积是否正确
         *
         * @param s
         * @return
         */
        fun String.isArea(): Boolean {
            val m: Matcher = AREA_PATTERN.matcher(this)
            return m.matches()
        }

        /**
         * 手机号码否正确
         *
         * @param s
         * @return
         */
        fun String.isMobileNumber(): Boolean {
            val m: Matcher = MOBILE_NUMBER_PATTERN.matcher(this)
            return m.matches()
        }

        /**
         * 银行账号否正确
         *
         * @param s
         * @return
         */
        fun String.isAccountNumber(): Boolean {
            val m: Matcher = ACCOUNT_NUMBER_PATTERN.matcher(this)
            return m.matches()
        }
/**
 *  密码验证的正则表达式 (6-16位字母和数字组合)
 * */
fun String.pwd(): Boolean {
    val m: Matcher = Pattern.compile("^(?![0-9]+\$)(?![a-zA-Z]+\$)[0-9A-Za-z]{6,16}$").matcher(this)
    return m.matches()
}


/**
 *  必须包含 数字,字母,符号 3项组合的 正则表达式
 * */
fun String.pwdHigh(): Boolean {
    val m: Matcher = Pattern.compile("^(?:(?=.*[0-9].*)(?=.*[A-Za-z].*)(?=.*[,\\.#%'\\+\\*-:;^_`].*))[,\\._`~!@#\$%^&*()+=|{}\\+\\*-:;.<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？^_`0-9A-Za-z]{8,20}$").matcher(this)
    return m.matches()
}

fun String.hasNumber(): Boolean {
    val m: Matcher = Pattern.compile(".*\\d+.*").matcher(this)
    return m.matches()
}

fun String.hasZM(): Boolean {
    val m: Matcher = Pattern.compile(".*[A-Za-z]+.*").matcher(this)
    return m.matches()
}
fun String.hasTSZM(): Boolean {
    val m: Matcher = Pattern.compile(".*[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+.*").matcher(this)
    return m.matches()
}
