package com.camnter.android.staticanalysis.plugin

import com.camnter.android.staticanalysis.plugin.utils.PluginUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.quality.CheckstylePlugin
import org.gradle.api.plugins.quality.FindBugsPlugin
import org.gradle.api.plugins.quality.PmdPlugin

/**
 * @author CaMnter
 */

class AndroidStaticAnalysisPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        PluginUtils.applyPluginIfNotApply(project, PmdPlugin.class)
        PluginUtils.applyPluginIfNotApply(project, FindBugsPlugin.class)
        PluginUtils.applyPluginIfNotApply(project, CheckstylePlugin.class)

        project.extensions.create('androidStaticAnalysis',
                AndroidStaticAnalysis)
        project.afterEvaluate {
            AndroidStaticAnalysis analysis = project.androidStaticAnalysis
            AndroidStaticAnalysis.refitAnalysis(project, analysis)

            def reportsDir = "${project.buildDir}/android-static-analysis"

            def pmd = AnalysisTaskManager.createPmdTask(project, analysis, reportsDir)
            def lint = AnalysisTaskManager.configAndroidLint(project, analysis, reportsDir)
            def findbugs = AnalysisTaskManager.createFindBugsTask(project, analysis, reportsDir)
            def checkstyle = AnalysisTaskManager.createCheckstyleTask(project, analysis,
                    reportsDir)

            def check = project.tasks.findByName('check')
            check.dependsOn pmd, lint, checkstyle, findbugs
        }
    }
}