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

package com.camnter.android.staticanalysis.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.camnter.android.staticanalysis.plugin.extension.*
import com.camnter.android.staticanalysis.plugin.task.AnalysisZipTask
import com.camnter.android.staticanalysis.plugin.task.DefaultRulesTask
import com.camnter.android.staticanalysis.plugin.task.EmailTask
import com.camnter.android.staticanalysis.plugin.utils.StringUtils
import org.gradle.api.Project
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.FindBugs
import org.gradle.api.plugins.quality.Pmd

/**
 * @author CaMnter
 */

class AnalysisTaskManager {

    static def createCheckstyleTask(Project project, AndroidStaticAnalysis analysis,
            String reportsDir, String suffix) {
        VersionHooker.setCheckstyleVersion(project, analysis.checkstyle.toolVersion)
        return project.task(type: Checkstyle,
                overwrite: false, "staticAnalysisCheckstyle${suffix.capitalize()}") {
            Checkstyle task ->
                task.with {
                    maxErrors = analysis.checkstyle.maxErrors
                    maxWarnings = analysis.checkstyle.maxWarnings
                    configFile = project.file(analysis.checkstyle.configDir)
                    configProperties.checkstyleSuppressionsPath =
                            project.file(analysis.checkstyle.suppressionsPath).absolutePath
                    source = 'src'
                    include('**/*.java')
                    exclude('**/gen/**')
                    reports.xml.enabled = true
                    reports.html.enabled = true
                    reports.with {
                        xml.enabled = false
                        html.enabled = true
                        xml.with {
                            destination = "$reportsDir/static-analysis-report/checkstyle.xml"
                        }
                        html.with {
                            destination = "$reportsDir/static-analysis-report/checkstyle.html"
                        }
                    }
                    classpath = project.files()
                }
        }
    }

    static def createFindBugsTask(Project project, AndroidStaticAnalysis analysis,
            String reportsDir, String suffix) {
        VersionHooker.setFindBugsVersion(project, analysis.findBugs.toolVersion)
        def findBugsTask = project.task(type: FindBugs,
                overwrite: true, "staticAnalysisFindBugs${suffix.capitalize()}") { FindBugs task ->
            task.with {
                ignoreFailures = analysis.findBugs.ignoreFailures
                effort = analysis.findBugs.effort
                reportLevel = analysis.findBugs.reportLevel
                excludeFilter = project.file(analysis.findBugs.excludeFilter)
                classes = project.files("${project.rootDir}/app/build/intermediates/classes")
                source = 'src'
                include('**/*.java')
                exclude('**/gen/**')
                reports.xml.enabled = false
                reports.html.enabled = true
                reports.with {
                    xml.enabled = false
                    html.enabled = true
                    xml.with {
                        destination = "$reportsDir/static-analysis-report/findbugs.xml"
                    }
                    html.with {
                        destination = "$reportsDir/static-analysis-report/findbugs.html"
                    }
                }
                classpath = project.files()
            }
        }
        return findBugsTask
    }

    static def createPmdTask(Project project, AndroidStaticAnalysis analysis, String reportsDir,
            String suffix) {
        VersionHooker.setPmsVersion(project, analysis.pmd.toolVersion)
        return project.task(type: Pmd,
                overwrite: true, "staticAnalysisPmd${suffix.capitalize()}") { Pmd task ->
            task.with {
                ignoreFailures = analysis.pmd.ignoreFailures
                ruleSetFiles = project.files(analysis.pmd.ruleSetFiles)
                ruleSets = analysis.pmd.ruleSets
                source = 'src'
                include('**/*.java')
                exclude('**/gen/**')
                reports.with {
                    xml.enabled = false
                    html.enabled = true
                    xml.with {
                        destination = "$reportsDir/static-analysis-report/pmd.xml"
                    }
                    html.with {
                        destination = "$reportsDir/static-analysis-report/pmd.html"
                    }
                }
            }
        }
    }

    static def configAndroidLint(Project project, AndroidStaticAnalysis analysis,
            String reportsDir) {
        if (project.plugins.hasPlugin(AppPlugin.class)) {
            AppExtension android = project.extensions.findByType(AppExtension.class)
            android.with {
                lintOptions.with {
                    abortOnError = false
                    xmlReport = false
                    htmlReport = true
                    lintConfig = project.file(analysis.lint.lintConfig)
                    htmlOutput = project.file("$reportsDir/static-analysis-report/lint-result.html")
                    xmlOutput = project.file("$reportsDir/static-analysis-report/lint-result.xml")
                }
            }
        }
        return project.tasks.findByName('lint')
    }

    static def createZipTask(Project project, String reportsDir, String suffix) {
        return project.task(type: AnalysisZipTask,
                overwrite: true, "analysisZip${suffix.capitalize()}") { AnalysisZipTask task ->
            task.with {
                inputDir = "${reportsDir}/static-analysis-report"
                zipPath = "${reportsDir}/static-analysis-report.zip"
            }
        }
    }

    static def createEmailTask(Project project, String reportsDir, EmailExtension email,
            String suffix) {
        return project.task(type: EmailTask,
                overwrite: true, "analysisEmail${suffix.capitalize()}") { EmailTask task ->
            task.with {
                task.email = email
                zipPath = "${reportsDir}/static-analysis-report.zip"
                htmlPaths = ["${reportsDir}/static-analysis-report/checkstyle.html",
                             "${reportsDir}/static-analysis-report/findbugs.html",
                             "${reportsDir}/static-analysis-report/pmd.html",
                             "${reportsDir}/static-analysis-report/lint-result.html"]
            }
        }
    }

    static def createDefaultRulesTask(Project project, String reportsDir,
            AndroidStaticAnalysis analysis, String suffix) {
        PmdExtension pmd = analysis.pmd
        LintExtension lint = analysis.lint
        FindBugsExtension findBugs = analysis.findBugs
        CheckstyleExtension checkstyle = analysis.checkstyle
        return project.task(type: DefaultRulesTask,
                overwrite: true, "analysisDefaultRules${suffix.capitalize()}") {
            DefaultRulesTask task ->
                task.with {
                    task.reportsDir = reportsDir

                    // default rule path or empty
                    setCreateDefaultPmdRule(StringUtils.isEmpty(
                            pmd.ruleSetFiles) || "${reportsDir}/${DefaultRulesTask.DEFAULT_PMD_RULE_PATH}" ==
                            pmd.ruleSetFiles)
                    setCreateDefaultLintRule(StringUtils.isEmpty(
                            lint.lintConfig) || "${reportsDir}/${DefaultRulesTask.DEFAULT_LINT_RULE_PATH}" ==
                            lint.lintConfig)
                    setCreateDefaultFindBugsRule(StringUtils.isEmpty(
                            findBugs.excludeFilter) || "${reportsDir}/${DefaultRulesTask.DEFAULT_FINDBUGS_RULE_PATH}" ==
                            findBugs.excludeFilter)
                    setCreateDefaultCheckstyleRule(StringUtils.isEmpty(
                            checkstyle.configDir) || "${reportsDir}/${DefaultRulesTask.DEFAULT_CHECKSTYLE_RULE_PATH}" ==
                            checkstyle.configDir)
                    setCreateDefaultCheckstyleSuppressionsRule(StringUtils.isEmpty(
                            checkstyle.suppressionsPath) || "${reportsDir}/${DefaultRulesTask.DEFAULT_CHECKSTYLE_SUPPRESSIONS_RULE_PATH}" ==
                            checkstyle.suppressionsPath)
                }
        }
    }
}