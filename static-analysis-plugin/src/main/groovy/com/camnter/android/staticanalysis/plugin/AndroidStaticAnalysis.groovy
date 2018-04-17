package com.camnter.android.staticanalysis.plugin

import com.camnter.android.staticanalysis.plugin.utils.StringUtils
import org.gradle.api.Project

/**
 * @author CaMnter
 */

class AndroidStaticAnalysis {

    static final def DEFAULT_PMD_VERSION = "6.2.0"
    static final def DEFAULT_CHECKSTYLE_VERSION = "8.8"
    static final def DEFAULT_FINDBUGS_VERSION = "3.0.1"

    def lintConfig = ""

    def checkstyleMaxErrors = 30
    def checkstyleConfigFile = ""
    def checkstyleSuppressionsPath = ""

    def findBugsEffort = "max"
    def findBugsExcludeFilter = ""
    def findBugsReportLevel = "high"
    def findBugsIgnoreFailures = true

    def pmdRuleSets = []
    def pmdRuleSetFiles = ""
    def pmdIgnoreFailures = true

    def pmdVersion = DEFAULT_PMD_VERSION
    def findBugsVersion = DEFAULT_FINDBUGS_VERSION
    def checkstyleVersion = DEFAULT_CHECKSTYLE_VERSION

    static def refitAnalysis(Project project, AndroidStaticAnalysis analysis) {
        def configDir = "${project.rootDir}/static-analysis-plugin-config"
        if (StringUtils.isEmpty(analysis.lintConfig)) {
            analysis.lintConfig = "$configDir/lint/lint.xml"
        }
        if (StringUtils.isEmpty(analysis.checkstyleConfigFile)) {
            analysis.checkstyleConfigFile = "$configDir/checkstyle/checkstyle.xml"
        }
        if (StringUtils.isEmpty(analysis.checkstyleSuppressionsPath)) {
            analysis.checkstyleSuppressionsPath = "$configDir/checkstyle/suppressions.xml"
        }
        if (StringUtils.isEmpty(analysis.findBugsExcludeFilter)) {
            analysis.findBugsExcludeFilter = "$configDir/findbugs/findbugs-filter.xml"
        }
        if (StringUtils.isEmpty(analysis.pmdRuleSetFiles)) {
            analysis.pmdRuleSetFiles = "$configDir/pmd/pmd-ruleset.xml"
        }
    }
}