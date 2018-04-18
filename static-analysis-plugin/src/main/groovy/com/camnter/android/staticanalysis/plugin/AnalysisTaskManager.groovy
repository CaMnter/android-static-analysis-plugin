package com.camnter.android.staticanalysis.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
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
                    lintConfig = project.file(analysis.lintConfig)
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
}