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

import com.camnter.android.staticanalysis.plugin.task.DefaultRulesTask
import com.camnter.android.staticanalysis.plugin.utils.StringUtils
import org.gradle.api.Project

/**
 * @author CaMnter
 */

class AndroidStaticAnalysis {

    public static final def DEFAULT_PMD_VERSION = '6.2.0'
    public static final def DEFAULT_CHECKSTYLE_VERSION = '8.8'
    public static final def DEFAULT_FINDBUGS_VERSION = '3.0.1'

    static def refitAnalysis(Project project, AndroidStaticAnalysis analysis, String reportsDir) {

        if (analysis.pmd == null) {
            analysis.pmd = new PmdExtension()
        }
        refitPmdExtension(analysis.pmd, reportsDir)

        if (analysis.lint == null) {
            analysis.lint = new LintExtension()
        }
        refitLintExtension(analysis.lint, reportsDir)

        if (analysis.findBugs == null) {
            analysis.findBugs = new FindBugsExtension()
        }
        refitFindBugsExtension(analysis.findBugs, reportsDir)

        if (analysis.checkstyle == null) {
            analysis.checkstyle = new CheckstyleExtension()
        }
        refitCheckstyleExtension(analysis.checkstyle, reportsDir)
    }

    static def refitPmdExtension(PmdExtension pmd, String reportsDir) {
        pmd.with {
            if (StringUtils.isEmpty(toolVersion)) toolVersion = DEFAULT_PMD_VERSION
            if (StringUtils.isEmpty(ruleSetFiles)) {
                ruleSetFiles =
                        new File(reportsDir, DefaultRulesTask.DEFAULT_PMD_RULE_PATH).absolutePath
            }
        }
    }

    static def refitLintExtension(LintExtension lint, String reportsDir) {
        lint.with {
            if (StringUtils.isEmpty(lintConfig)) {
                lintConfig =
                        new File(reportsDir, DefaultRulesTask.DEFAULT_LINT_RULE_PATH).absolutePath
            }
        }
    }

    static def refitFindBugsExtension(FindBugsExtension findBugs, String reportsDir) {
        findBugs.with {
            if (StringUtils.isEmpty(toolVersion)) toolVersion = DEFAULT_FINDBUGS_VERSION
            if (StringUtils.isEmpty(excludeFilter)) {
                excludeFilter = new File(reportsDir,
                        DefaultRulesTask.DEFAULT_FINDBUGS_RULE_PATH).absolutePath
            }
        }
    }

    static def refitCheckstyleExtension(CheckstyleExtension checkstyle, String reportsDir) {
        checkstyle.with {
            if (StringUtils.isEmpty(toolVersion)) toolVersion = DEFAULT_CHECKSTYLE_VERSION
            if (StringUtils.isEmpty(configDir)) {
                configDir = new File(reportsDir,
                        DefaultRulesTask.DEFAULT_CHECKSTYLE_RULE_PATH).absolutePath
            }
            if (StringUtils.isEmpty(suppressionsPath)) {
                suppressionsPath = new File(reportsDir,
                        DefaultRulesTask.DEFAULT_CHECKSTYLE_SUPPRESSIONS_RULE_PATH).absolutePath
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