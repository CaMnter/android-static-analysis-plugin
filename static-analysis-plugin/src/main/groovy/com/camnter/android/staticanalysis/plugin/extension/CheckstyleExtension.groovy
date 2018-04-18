package com.camnter.android.staticanalysis.plugin.extension

/**
 * @author CaMnter
 */

class CheckstyleExtension extends AnalysisExtension {

    public String configDir
    public int maxErrors = 30
    public int maxWarnings = Integer.MAX_VALUE
    public String suppressionsPath

}