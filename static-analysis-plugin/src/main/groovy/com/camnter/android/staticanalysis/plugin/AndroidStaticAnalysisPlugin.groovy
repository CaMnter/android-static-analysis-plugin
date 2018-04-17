package com.camnter.android.staticanalysis.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.quality.CheckstylePlugin
import org.gradle.api.plugins.quality.FindBugsPlugin
import org.gradle.api.plugins.quality.PmdPlugin

class AndroidStaticAnalysisPlugin implements Plugin<Project> {

    def configDir
    def reportsDir

    @Override
    void apply(Project project) {

        PluginUtils.applyPluginIfNotApply(project, PmdPlugin.class)
        PluginUtils.applyPluginIfNotApply(project, FindBugsPlugin.class)
        PluginUtils.applyPluginIfNotApply(project, CheckstylePlugin.class)

        reportsDir = "${project.buildDir}/android-static-analysis"
        configDir = "${project.rootDir}/android-static-analysis-config"

        def pmd = AnalysisTaskManager.createPmdTask(project, configDir, reportsDir)
        def lint = AnalysisTaskManager.configAndroidLint(project, configDir, reportsDir)
        def findbugs = AnalysisTaskManager.createFindBugsTask(project, configDir, reportsDir)
        def checkstyle = AnalysisTaskManager.createCheckStyleTask(project, configDir, reportsDir)

        def check = project.tasks.findByName('check')
        check.dependsOn pmd, lint, checkstyle, findbugs

        project.extensions.create('androidStaticAnalysis',
                AndroidStaticAnalysis)
    }


}