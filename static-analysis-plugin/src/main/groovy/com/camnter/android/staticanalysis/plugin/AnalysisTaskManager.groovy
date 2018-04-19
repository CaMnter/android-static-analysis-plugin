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
import com.camnter.android.staticanalysis.plugin.extension.AndroidStaticAnalysis
import com.camnter.android.staticanalysis.plugin.extension.EmailExtension
import com.camnter.android.staticanalysis.plugin.task.AnalysisZipTask
import com.camnter.android.staticanalysis.plugin.task.EmailTask
import org.gradle.api.Project
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.FindBugs
import org.gradle.api.plugins.quality.Pmd

/**
 * @author CaMnter
 */

class AnalysisTaskManager {

    static def createCheckstyleTask(Project project, AndroidStaticAnalysis analysis,
            String reportsDir) {
        VersionHooker.setCheckstyleVersion(project, analysis.checkstyle.toolVersion)
        return project.task(type: Checkstyle,
                overwrite: false, 'staticAnalysisCheckstyle') { Checkstyle task ->
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
            String reportsDir) {
        VersionHooker.setFindBugsVersion(project, analysis.findBugs.toolVersion)
        def findBugsTask = project.task(type: FindBugs,
                overwrite: true, 'staticAnalysisFindBugs') { FindBugs task ->
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

    static def createPmdTask(Project project, AndroidStaticAnalysis analysis, String reportsDir) {
        VersionHooker.setPmsVersion(project, analysis.pmd.toolVersion)
        return project.task(type: Pmd,
                overwrite: true, 'staticAnalysisPmd') { Pmd task ->
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

    static def createZipTask(Project project, String reportsDir) {
        return project.task(type: AnalysisZipTask,
                overwrite: true, 'analysisZipTask') { AnalysisZipTask task ->
            task.inputDir = "${reportsDir}/static-analysis-report"
            task.zipPath = "${reportsDir}/static-analysis-report.zip"
        }
    }

    static def createEmailTask(Project project, String reportsDir, EmailExtension email) {
        return project.task(type: EmailTask,
                overwrite: true, 'analysisEmailTask') { EmailTask task ->
            task.email = email
            task.zipPath = "${reportsDir}/static-analysis-report.zip"
            task.htmlPaths = ["${reportsDir}/static-analysis-report/checkstyle.html",
                              "${reportsDir}/static-analysis-report/findbugs.html",
                              "${reportsDir}/static-analysis-report/pmd.html",
                              "${reportsDir}/static-analysis-report/lint-result.html"]
        }
    }
}