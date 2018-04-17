package com.camnter.android.staticanalysis.plugin

import org.gradle.api.Project

class AndroidStaticAnalysis {

    def configDir = ""
    def reportsDir = ""

    def maxErrors = 30

    static def refitAnalysis(Project project, AndroidStaticAnalysis analysis) {
        if (StringUtils.isEmpty(analysis.configDir)) {
            analysis.configDir = "${project.rootDir}/android-static-analysis-config"
        }
        if (StringUtils.isEmpty(analysis.reportsDir)) {
            analysis.reportsDir = "${project.buildDir}/android-static-analysis"
        }
    }

}