package com.camnter.android.staticanalysis.plugin.utils

/**
 * @author CaMnter
 */

class StringUtils {

    static def isEmpty(String target) {
        if (target == null || target.length() == 0) return true
        return false
    }

}