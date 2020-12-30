package com.hq.tool.misc

class MapTool {
    /**
     * 地球半径，单位：公里/千米
     */
    private val EARTH_RADIUS = 6378.137

    /**
     * 经纬度转化成弧度
     *
     * @param d 经度/纬度
     * @return 经纬度转化成的弧度
     */
    private fun radian(d: Double): Double {
        return d * Math.PI / 180.0
    }

    /**
     * 返回两个地理坐标之间的距离
     *
     * @param firsLongitude   第一个坐标的经度
     * @param firstLatitude   第一个坐标的纬度
     * @param secondLongitude 第二个坐标的经度
     * @param secondLatitude  第二个坐标的纬度
     * @return 两个坐标之间的距离，单位：公里/千米
     */
    fun distance(firsLongitude: Double, firstLatitude: Double,
                 secondLongitude: Double, secondLatitude: Double): Double {
        val firstRadianLongitude = radian(firsLongitude)
        val firstRadianLatitude = radian(firstLatitude)
        val secondRadianLongitude = radian(secondLongitude)
        val secondRadianLatitude = radian(secondLatitude)
        val a = firstRadianLatitude - secondRadianLatitude
        val b = firstRadianLongitude - secondRadianLongitude
        var cal = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2.0)
                + (Math.cos(firstRadianLatitude) * Math.cos(secondRadianLatitude)
                * Math.pow(Math.sin(b / 2), 2.0))))
        cal = cal * EARTH_RADIUS
        return Math.round(cal * 10000.0) / 10000.0
    }

    /**
     * 返回两个地理坐标之间的距离
     *
     * @param firstPoint  第一个坐标 例如："23.100919, 113.279868"
     * @param secondPoint 第二个坐标 例如："23.149286, 113.347584"
     * @return 两个坐标之间的距离，单位：公里/千米
     */
    fun distance(firstPoint: String, secondPoint: String): Double {
        if (firstPoint == "," || secondPoint == ",") {
            return 9999.00
        }
        val firstArray = firstPoint.split(",".toRegex()).toTypedArray()
        val secondArray = secondPoint.split(",".toRegex()).toTypedArray()
        val firstLatitude = java.lang.Double.valueOf(firstArray[0].trim { it <= ' ' })
        val firstLongitude = java.lang.Double.valueOf(firstArray[1].trim { it <= ' ' })
        val secondLatitude = java.lang.Double.valueOf(secondArray[0].trim { it <= ' ' })
        val secondLongitude = java.lang.Double.valueOf(secondArray[1].trim { it <= ' ' })
        return distance(firstLatitude, firstLongitude, secondLatitude, secondLongitude)
    }
}