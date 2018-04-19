package com.camnter.android.staticanalysis.plugin

import com.camnter.android.staticanalysis.plugin.extension.AndroidStaticAnalysis
import com.camnter.android.staticanalysis.plugin.extension.CheckstyleExtension
import com.camnter.android.staticanalysis.plugin.extension.EmailExtension
import com.camnter.android.staticanalysis.plugin.extension.FindBugsExtension
import com.camnter.android.staticanalysis.plugin.extension.LintExtension
import com.camnter.android.staticanalysis.plugin.extension.PmdExtension
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
        project.androidStaticAnalysis.extensions.create('pmd', PmdExtension)
        project.androidStaticAnalysis.extensions.create('lint', LintExtension)
        project.androidStaticAnalysis.extensions.create('findBugs', FindBugsExtension)
        project.androidStaticAnalysis.extensions.create('checkstyle', CheckstyleExtension)
        project.androidStaticAnalysis.extensions.create('email', EmailExtension)

        project.afterEvaluate {
            AndroidStaticAnalysis analysis = project.androidStaticAnalysis
            AndroidStaticAnalysis.refitAnalysis(project, analysis)

            def reportsDir = "${project.buildDir}/android-static-analysis"

            def pmd = AnalysisTaskManager.createPmdTask(project, analysis, reportsDir)
            def lint = AnalysisTaskManager.configAndroidLint(project, analysis, reportsDir)
            def findbugs = AnalysisTaskManager.createFindBugsTask(project, analysis, reportsDir)
            def checkstyle = AnalysisTaskManager.createCheckstyleTask(project, analysis,
                    reportsDir)

            // TODO extension
            def zip = AnalysisTaskManager.createZipTask(project, reportsDir)

            // ...   -> check
            def check = project.tasks.findByName('check')
            check.dependsOn pmd, lint, checkstyle, findbugs

            // check -> zip
            zip.dependsOn(check)
            zip.mustRunAfter(check)
            check.finalizedBy(zip)
        }
    }
}