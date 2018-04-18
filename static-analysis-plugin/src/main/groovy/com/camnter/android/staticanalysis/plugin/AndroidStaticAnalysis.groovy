package com.camnter.android.staticanalysis.plugin

import com.camnter.android.staticanalysis.plugin.extension.CheckstyleExtension
import com.camnter.android.staticanalysis.plugin.extension.FindBugsExtension
import com.camnter.android.staticanalysis.plugin.extension.LintExtension
import com.camnter.android.staticanalysis.plugin.extension.PmdExtension
import com.camnter.android.staticanalysis.plugin.utils.StringUtils
import org.gradle.api.Project

/**
 * @author CaMnter
 */

class AndroidStaticAnalysis {

    public static final def DEFAULT_PMD_VERSION = "6.2.0"
    public static final def DEFAULT_CHECKSTYLE_VERSION = "8.8"
    public static final def DEFAULT_FINDBUGS_VERSION = "3.0.1"

    PmdExtension pmd
    LintExtension lint
    FindBugsExtension findBugs
    CheckstyleExtension checkstyle

    static def refitAnalysis(Project project, AndroidStaticAnalysis analysis) {

        def configDir = "${project.rootDir}/static-analysis-plugin-config"

        if (analysis.pmd == null) {
            analysis.pmd = new PmdExtension()
        }
        refitPmdExtension(configDir, analysis.pmd)

        if (analysis.lint == null) {
            analysis.lint = new LintExtension()
        }
        refitLintExtension(configDir, analysis.lint)

        if (analysis.findBugs == null) {
            analysis.findBugs = new FindBugsExtension()
        }
        refitFindBugsExtension(configDir, analysis.findBugs)

        if (analysis.checkstyle == null) {
            analysis.checkstyle = new CheckstyleExtension()
        }
        refitCheckstyleExtension(configDir, analysis.checkstyle)
    }

    static def refitCheckstyleExtension(String configDir, CheckstyleExtension checkstyle) {
        checkstyle.with {
            if (StringUtils.isEmpty(toolVersion)) toolVersion = DEFAULT_CHECKSTYLE_VERSION
            if (StringUtils.isEmpty(configDir)) configDir = "$configDir/checkstyle/checkstyle.xml"
            if (StringUtils.isEmpty(suppressionsPath)) {
                suppressionsPath = "$configDir/checkstyle/suppressions.xml"
            }
        }
    }

    static def refitFindBugsExtension(String configDir, FindBugsExtension findBugs) {
        findBugs.with {
            if (StringUtils.isEmpty(toolVersion)) toolVersion = DEFAULT_FINDBUGS_VERSION
            if (StringUtils.isEmpty(excludeFilter)) {
                excludeFilter = "$configDir/findbugs/findbugs-filter.xml"
            }
        }
    }

    static def refitPmdExtension(String configDir, PmdExtension pmd) {
        pmd.with {
            if (StringUtils.isEmpty(toolVersion)) toolVersion = DEFAULT_PMD_VERSION
            if (StringUtils.isEmpty(ruleSetFiles)) {
                ruleSetFiles = "$configDir/checkstyle/suppressions.xml"
            }
        }
    }

    static def refitLintExtension(String configDir, LintExtension lint) {
        lint.with {
            if (StringUtils.isEmpty(lintConfig)) lintConfig = "$configDir/lint/lint.xml"
        }
    }
}