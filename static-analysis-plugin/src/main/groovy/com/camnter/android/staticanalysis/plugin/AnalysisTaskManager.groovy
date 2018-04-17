package com.camnter.android.staticanalysis.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Project
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.FindBugs
import org.gradle.api.plugins.quality.Pmd

class AnalysisTaskManager {

    static final def DEFAULT_CHECKSTYLE_VERSION = "8.8"
    static final def DEFAULT_PMD_VERSION = "6.2.0"
    static final def DEFAULT_FINDBUGS_VERSION = "3.0.1"

    static def createCheckStyleTask(Project project, String configDir, String reportsDir) {
        VersionHooker.setCheckstyleVersion(project, DEFAULT_CHECKSTYLE_VERSION)
        return project.task(type: Checkstyle,
                overwrite: false, 'checkstyleTask') { Checkstyle task ->
            task.with {
                // TODO
                maxErrors = 100
                configFile = project.file("$configDir/checkstyle/checkstyle.xml")
                configProperties.checkstyleSuppressionsPath =
                        project.file("$configDir/checkstyle/suppressions.xml").absolutePath
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

    static def createFindBugsTask(Project project, String configDir, String reportsDir) {
        VersionHooker.setFindBugsVersion(project, DEFAULT_FINDBUGS_VERSION)
        def findBugsTask = project.task(type: FindBugs,
                overwrite: true, 'findBugsTask') { FindBugs task ->
            task.with {
                ignoreFailures = false
                effort = "max"
                reportLevel = "high"
                excludeFilter = project.file("$configDir/findbugs/findbugs-filter.xml")
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

    static def createPmdTask(Project project, String configDir, String reportsDir) {
        VersionHooker.setFindBugsVersion(project, DEFAULT_PMD_VERSION)
        return project.task(type: Pmd,
                overwrite: true, 'pmdTask') { Pmd task ->
            task.with {
                ignoreFailures = false
                ruleSetFiles = project.files("$configDir/pmd/pmd-ruleset.xml")
                ruleSets = []
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

    static def configAndroidLint(Project project, String configDir, String reportsDir) {
        if (project.plugins.hasPlugin(AppPlugin.class)) {
            AppExtension android = project.extensions.findByType(AppExtension.class)
            android.with {
                lintOptions.with {
                    abortOnError = false
                    xmlReport = false
                    htmlReport = true
                    lintConfig = project.file("$configDir/lint/lint.xml")
                    htmlOutput = project.file("$reportsDir/lint/lint-result.html")
                    xmlOutput = project.file("$reportsDir/lint/lint-result.xml")
                }
            }
        }
        return project.tasks.findByName('lint')
    }

}