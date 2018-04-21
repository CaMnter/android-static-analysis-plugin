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

import com.camnter.android.staticanalysis.plugin.extension.*
import com.camnter.android.staticanalysis.plugin.utils.PluginUtils
import com.camnter.android.staticanalysis.plugin.utils.TaskUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
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
        // project.androidStaticAnalysis.extensions.create('email', EmailExtension)

        project.afterEvaluate {
            def reportsDir = "${project.buildDir}/android-static-analysis"

            AndroidStaticAnalysis analysis = project.androidStaticAnalysis
            def createDefalutRulesTask = AndroidStaticAnalysis.isCreateDefaultRulesTask(analysis)
            def rules = null
            if (createDefalutRulesTask) {
                rules = AnalysisTaskManager.createDefaultRulesTask(project, reportsDir, analysis)
            }

            AndroidStaticAnalysis.refitAnalysis(analysis, reportsDir)
            def pmd = AnalysisTaskManager.createPmdTask(project, analysis, reportsDir)
            def lint = AnalysisTaskManager.configAndroidLint(project, analysis, reportsDir)
            def findbugs = AnalysisTaskManager.createFindBugsTask(project, analysis, reportsDir)
            def checkstyle = AnalysisTaskManager.createCheckstyleTask(project, analysis,
                    reportsDir)

            def taskParcel = new TaskParcel()
            taskParcel.rules = rules
            taskParcel.pmd = pmd
            taskParcel.lint = lint
            taskParcel.findbugs = findbugs
            taskParcel.checkstyle = checkstyle

            def check = project.tasks.findByName('check')
            configAnalysis(check, taskParcel)

            // assembleDebug
            if (analysis.debugAnalysis) {
                def assembleDebug = project.tasks.findByName('assembleDebug')
                configAnalysis(assembleDebug, taskParcel)
            }

            // assembleRelease
            if (analysis.releaseAnalysis) {
                def assembleRelease = project.tasks.findByName('assembleRelease')
                configAnalysis(assembleRelease, taskParcel)
            }
        }
    }

    private static final class TaskParcel {
        def rules
        def pmd
        def lint
        def findbugs
        def checkstyle
    }

    static def configAnalysis(Task target, TaskParcel taskParcel) {
        if (null != taskParcel.rules) {
            // rules -> pmd -> lint -> findbugs -> checkstyle -> target
            TaskUtils.adjustTaskPriorities(taskParcel.rules, taskParcel.pmd)
            TaskUtils.adjustTaskPriorities(taskParcel.pmd, taskParcel.lint)
            TaskUtils.adjustTaskPriorities(taskParcel.lint, taskParcel.findbugs)
            TaskUtils.adjustTaskPriorities(taskParcel.findbugs, taskParcel.checkstyle)
            TaskUtils.adjustTaskPriorities(taskParcel.checkstyle, target)
        } else {
            // pmd -> lint -> findbugs -> checkstyle -> target
            target.dependsOn taskParcel.pmd, taskParcel.lint, taskParcel.findbugs,
                    taskParcel.checkstyle
        }
    }

    def configEmail(Project project, String reportsDir, Task check) {
        if (analysis.email != null && analysis.email.send) {
            if (EmailExtension.ZIP == analysis.email.enclosureType) {
                def zip = AnalysisTaskManager.createZipTask(project, reportsDir)
                def email = AnalysisTaskManager.createEmailTask(project, reportsDir,
                        analysis.email)
                // ... -> check -> zip -> email
                TaskUtils.adjustTaskPriorities(check, zip)
                TaskUtils.adjustTaskPriorities(zip, email)
            } else if (EmailExtension.HTML == analysis.email.enclosureType) {
                def email = AnalysisTaskManager.createEmailTask(project, reportsDir,
                        analysis.email)
                // ... -> check -> email
                TaskUtils.adjustTaskPriorities(check, email)
            }
        }
    }
}
