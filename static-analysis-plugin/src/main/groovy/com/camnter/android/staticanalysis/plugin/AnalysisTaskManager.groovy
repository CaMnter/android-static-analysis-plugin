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
        VersionHooker.setCheckstyleVersion(project, analysis.checkstyleVersion)
        return project.task(type: Checkstyle,
                overwrite: false, 'staticAnalysisCheckstyle') { Checkstyle task ->
            task.with {
                maxErrors = analysis.checkstyleMaxErrors
                configFile = project.file(analysis.checkstyleConfigFile)
                configProperties.checkstyleSuppressionsPath =
                        project.file(analysis.checkstyleSuppressionsPath).absolutePath
                source = 'src'
                include('**/*.java')
                exclude('**/gen/**')
                reports.xml.enabled = true
                reports.html.enabled = true
                reports.with {
                    xml.enabled = false
                    html.enabled = true
                    xml.with {
                        destination = "$reportsDir/checkstyle/checkstyle.xml"
                    }
                    html.with {
                        destination = "$reportsDir/checkstyle/checkstyle.html"
                    }
                }
                classpath = project.files()
            }
        }
    }

    static def createFindBugsTask(Project project, AndroidStaticAnalysis analysis,
            String reportsDir) {
        VersionHooker.setFindBugsVersion(project, analysis.findBugsVersion)
        def findBugsTask = project.task(type: FindBugs,
                overwrite: true, 'staticAnalysisFindBugs') { FindBugs task ->
            task.with {
                ignoreFailures = analysis.findBugsIgnoreFailures
                effort = analysis.findBugsEffort
                reportLevel = analysis.findBugsReportLevel
                excludeFilter = project.file(analysis.findBugsExcludeFilter)
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
                        destination = "$reportsDir/findbugs/findbugs.xml"
                    }
                    html.with {
                        destination = "$reportsDir/findbugs/findbugs.html"
                    }
                }
                classpath = project.files()
            }
        }
        return findBugsTask
    }

    static def createPmdTask(Project project, AndroidStaticAnalysis analysis, String reportsDir) {
        VersionHooker.setPmsVersion(project, analysis.pmdVersion)
        return project.task(type: Pmd,
                overwrite: true, 'staticAnalysisPmd') { Pmd task ->
            task.with {
                ignoreFailures = analysis.pmdIgnoreFailures
                ruleSetFiles = project.files(analysis.pmdRuleSetFiles)
                ruleSets = analysis.pmdRuleSets
                source = 'src'
                include('**/*.java')
                exclude('**/gen/**')
                reports.with {
                    xml.enabled = false
                    html.enabled = true
                    xml.with {
                        destination = "$reportsDir/pmd/pmd.xml"
                    }
                    html.with {
                        destination = "$reportsDir/pmd/pmd.html"
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
                    htmlOutput = project.file("$reportsDir/lint/lint-result.html")
                    xmlOutput = project.file("$reportsDir/lint/lint-result.xml")
                }
            }
        }
        return project.tasks.findByName('lint')
    }
}