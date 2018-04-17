package com.camnter.android.staticanalysis.plugin

class StringUtils {

    static def isEmpty(String target) {
        if (target == null || target.length() == 0) return true
        return false
    }

}