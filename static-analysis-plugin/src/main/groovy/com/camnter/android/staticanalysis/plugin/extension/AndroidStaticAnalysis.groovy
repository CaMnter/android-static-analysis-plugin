package com.camnter.android.staticanalysis.plugin.extension

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

        def baseConfigDir = "${project.rootDir}/static-analysis-plugin-config"

        if (analysis.pmd == null) {
            analysis.pmd = new PmdExtension()
        }
        refitPmdExtension(baseConfigDir, analysis.pmd)

        if (analysis.lint == null) {
            analysis.lint = new LintExtension()
        }
        refitLintExtension(baseConfigDir, analysis.lint)

        if (analysis.findBugs == null) {
            analysis.findBugs = new FindBugsExtension()
        }
        refitFindBugsExtension(baseConfigDir, analysis.findBugs)

        if (analysis.checkstyle == null) {
            analysis.checkstyle = new CheckstyleExtension()
        }
        refitCheckstyleExtension(baseConfigDir, analysis.checkstyle)
    }

    static def refitCheckstyleExtension(String baseConfigDir, CheckstyleExtension checkstyle) {
        checkstyle.with {
            if (StringUtils.isEmpty(toolVersion)) toolVersion = DEFAULT_CHECKSTYLE_VERSION
            if (StringUtils.isEmpty(configDir)) {
                configDir = "$baseConfigDir/checkstyle/checkstyle.xml"
            }
            if (StringUtils.isEmpty(suppressionsPath)) {
                suppressionsPath = "$baseConfigDir/checkstyle/suppressions.xml"
            }
        }
    }

    static def refitFindBugsExtension(String baseConfigDir, FindBugsExtension findBugs) {
        findBugs.with {
            if (StringUtils.isEmpty(toolVersion)) toolVersion = DEFAULT_FINDBUGS_VERSION
            if (StringUtils.isEmpty(excludeFilter)) {
                excludeFilter = "$baseConfigDir/findbugs/findbugs-filter.xml"
            }
        }
    }

    static def refitPmdExtension(String baseConfigDir, PmdExtension pmd) {
        pmd.with {
            if (StringUtils.isEmpty(toolVersion)) toolVersion = DEFAULT_PMD_VERSION
            if (StringUtils.isEmpty(ruleSetFiles)) {
                ruleSetFiles = "$baseConfigDir/pmd/pmd-ruleset.xml"
            }
        }
    }

    static def refitLintExtension(String baseConfigDir, LintExtension lint) {
        lint.with {
            if (StringUtils.isEmpty(lintConfig)) lintConfig = "$baseConfigDir/lint/lint.xml"
        }
    }
}