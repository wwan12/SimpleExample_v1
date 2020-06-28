package com.aisino.tool.model

import java.util.regex.Matcher
import java.util.regex.Pattern

class Rex {
    var regularExpression =
        "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)"

    fun isNumeric(str: String?): Boolean {
        val pattern: Pattern = Pattern.compile("[0-9]*")
        val isNum: Matcher = pattern.matcher(str)
        return if (!isNum.matches()) {
            false
        } else true
    }

    companion object {
        /**
         * 车牌号码Pattern
         */
        val PLATE_NUMBER_PATTERN: Pattern = Pattern
            .compile("^[\u0391-\uFFE5]{1}[a-zA-Z0-9]{6}$")

        /**
         * 证件号码Pattern
         */
        val ID_CODE_PATTERN: Pattern = Pattern
            .compile("^[a-zA-Z0-9]+$")

        /**
         * 编码Pattern
         */
        val CODE_PATTERN: Pattern = Pattern
            .compile("^[a-zA-Z0-9]+$")

        /**
         * 固定电话编码Pattern
         */
        val PHONE_NUMBER_PATTERN: Pattern = Pattern
            .compile("0\\d{2,3}-[0-9]+")

        /**
         * 邮政编码Pattern
         */
        val POST_CODE_PATTERN: Pattern = Pattern.compile("\\d{6}")

        /**
         * 面积Pattern
         */
        val AREA_PATTERN: Pattern = Pattern.compile("\\d*.?\\d*")

        /**
         * 手机号码Pattern
         */
        val MOBILE_NUMBER_PATTERN: Pattern = Pattern
            .compile("^(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57]|19[89]|166)[0-9]{8}")
        val ID_CARD: Pattern = Pattern.compile(
            "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}(" +
                    "(0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"
        )

        /**
         * 银行帐号Pattern
         */
        val ACCOUNT_NUMBER_PATTERN: Pattern = Pattern
            .compile("\\d{16,21}")

        /**
         * 车牌号码是否正确
         *
         * @param s
         * @return
         */
        fun isPlateNumber(s: String?): Boolean {
            val m: Matcher = PLATE_NUMBER_PATTERN.matcher(s)
            return m.matches()
        }

        /**
         * 证件号码是否正确
         *
         * @param s
         * @return
         */
        fun isIDCode(s: String?): Boolean {
            val m: Matcher = ID_CODE_PATTERN.matcher(s)
            return m.matches()
        }

        /**
         * 编码是否正确
         *
         * @param s
         * @return
         */
        fun isCode(s: String?): Boolean {
            val m: Matcher = CODE_PATTERN.matcher(s)
            return m.matches()
        }

        /**
         * 固话编码是否正确
         *
         * @param s
         * @return
         */
        fun isPhoneNumber(s: String?): Boolean {
            val m: Matcher = PHONE_NUMBER_PATTERN.matcher(s)
            return m.matches()
        }

        /**
         * 邮政编码是否正确
         *
         * @param s
         * @return
         */
        fun isPostCode(s: String?): Boolean {
            val m: Matcher = POST_CODE_PATTERN.matcher(s)
            return m.matches()
        }

        /**
         * 面积是否正确
         *
         * @param s
         * @return
         */
        fun isArea(s: String?): Boolean {
            val m: Matcher = AREA_PATTERN.matcher(s)
            return m.matches()
        }

        /**
         * 手机号码否正确
         *
         * @param s
         * @return
         */
        fun isMobileNumber(s: String?): Boolean {
            val m: Matcher = MOBILE_NUMBER_PATTERN.matcher(s)
            return m.matches()
        }

        fun isIdCard(s: String?): Boolean {
            val m: Matcher = ID_CARD.matcher(s)
            return m.matches()
        }

        /**
         * 银行账号否正确
         *
         * @param s
         * @return
         */
        fun isAccountNumber(s: String?): Boolean {
            val m: Matcher = ACCOUNT_NUMBER_PATTERN.matcher(s)
            return m.matches()
        }
    }
}