/*
 * Copyright (C) 2018 CaMnter yuanyu.camnter@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.camnter.android.staticanalysis.plugin.extension

import com.camnter.android.staticanalysis.plugin.utils.StringUtils
import org.gradle.api.Project

/**
 * @author CaMnter
 */

class AndroidStaticAnalysis {

    public static final def DEFAULT_PMD_VERSION = '6.2.0'
    public static final def DEFAULT_CHECKSTYLE_VERSION = '8.8'
    public static final def DEFAULT_FINDBUGS_VERSION = '3.0.1'

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

    static def refitFindBugsExtension(String baseConfigDir, FindBugsExtension findBugs) {
        findBugs.with {
            if (StringUtils.isEmpty(toolVersion)) toolVersion = DEFAULT_FINDBUGS_VERSION
            if (StringUtils.isEmpty(excludeFilter)) {
                excludeFilter = "$baseConfigDir/findbugs/findbugs-filter.xml"
            }
        }
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

    static def isCreateDefaultRulesTask(AndroidStaticAnalysis analysis) {
        PmdExtension pmd = analysis.pmd
        LintExtension lint = analysis.lint
        FindBugsExtension findBugs = analysis.findBugs
        CheckstyleExtension checkstyle = analysis.checkstyle
        return StringUtils.isEmpty(pmd.ruleSetFiles) || StringUtils.isEmpty(lint.lintConfig) ||
                StringUtils.isEmpty(findBugs.excludeFilter) ||
                StringUtils.isEmpty(checkstyle.configDir) ||
                StringUtils.isEmpty(checkstyle.suppressionsPath)
    }
}