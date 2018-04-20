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


            AndroidStaticAnalysis.refitAnalysis(project, analysis)
            def pmd = AnalysisTaskManager.createPmdTask(project, analysis, reportsDir)
            def lint = AnalysisTaskManager.configAndroidLint(project, analysis, reportsDir)
            def findbugs = AnalysisTaskManager.createFindBugsTask(project, analysis, reportsDir)
            def checkstyle = AnalysisTaskManager.createCheckstyleTask(project, analysis,
                    reportsDir)

            def check = project.tasks.findByName('check')
            if (null != rules) {
                // rules -> pmd
                TaskUtils.adjustTaskPriorities(rules, pmd)
            }
            // pmd -> lint -> findbugs -> checkstyle -> check
            TaskUtils.adjustTaskPriorities(pmd, lint)
            TaskUtils.adjustTaskPriorities(lint, findbugs)
            TaskUtils.adjustTaskPriorities(findbugs, checkstyle)
            TaskUtils.adjustTaskPriorities(checkstyle, check)

            // TODO assembleDebug
            // TODO assembleRelease
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
