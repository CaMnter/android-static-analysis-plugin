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
import com.camnter.android.staticanalysis.plugin.task.flow.*
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
        project.androidStaticAnalysis.extensions.create('email', EmailExtension)

        project.afterEvaluate {
            def reportsDir = "${project.buildDir}/android-static-analysis"

            AndroidStaticAnalysis analysis = project.androidStaticAnalysis

            def createDefaultRulesTask = AndroidStaticAnalysis.isCreateDefaultRulesTask(analysis,
                    reportsDir)
            def rulesCheck = null
            def rulesDebug = null
            def rulesRelease = null
            if (createDefaultRulesTask) {
                rulesCheck =
                        AnalysisTaskManager.createDefaultRulesTask(project, reportsDir, analysis,
                                'check')
                rulesDebug =
                        AnalysisTaskManager.createDefaultRulesTask(project, reportsDir, analysis,
                                'debug')
                rulesRelease =
                        AnalysisTaskManager.createDefaultRulesTask(project, reportsDir, analysis,
                                'release')
            }

            AndroidStaticAnalysis.refitAnalysis(analysis, reportsDir)

            // check
            def check = project.tasks.findByName('check')
            configAnalysis(project, check, analysis, rulesCheck, reportsDir, 'check')
            configEmail(project, check, analysis.email, reportsDir, 'check')

            // assembleDebug
            if (analysis.debugAnalysis) {
                def assembleDebug = project.tasks.findByName('assembleDebug')
                // TODO switch
                configAnalysis(project, assembleDebug, analysis, rulesDebug, reportsDir, 'debug')
            }

            // assembleRelease
            if (analysis.releaseAnalysis) {
                def assembleRelease = project.tasks.findByName('assembleRelease')
                // TODO switch
                configAnalysis(project, assembleRelease, analysis, rulesRelease, reportsDir,
                        'release')
            }
        }
    }

    static def configAnalysis(Project project, Task target, AndroidStaticAnalysis analysis,
            Task rules,
            String reportsDir, String suffix) {
        PmdCheckTaskChain pmdTaskChain = new PmdCheckTaskChain(project, analysis, reportsDir,
                suffix)
        LintCheckTaskChain lintTaskChain = new LintCheckTaskChain(project, analysis, reportsDir,
                suffix)
        FindBugsCheckTaskChain findBugsTaskChain = new FindBugsCheckTaskChain(project, analysis,
                reportsDir,
                suffix)
        CheckstyleCheckTaskChain checkstyleTaskChain = new CheckstyleCheckTaskChain(project,
                analysis,
                reportsDir, suffix)
        TargetTaskChain targetTaskChain = new TargetTaskChain(target)

        pmdTaskChain.next = lintTaskChain
        lintTaskChain.next = findBugsTaskChain
        findBugsTaskChain.next = checkstyleTaskChain
        checkstyleTaskChain.next = targetTaskChain
        /*
         * rules -> pmd -> lint -> findbugs -> checkstyle -> target
         * pmd -> lint -> findbugs -> checkstyle -> target
         */
        pmdTaskChain.execute(null != rules ? rules : null)
    }

    static def configEmail(Project project, Task target, EmailExtension emailExtension,
            String reportsDir,
            String suffix) {
        if (emailExtension != null && emailExtension.send) {
            switch (emailExtension.enclosureType) {
                case EmailExtension.ZIP:
                    def zip = AnalysisTaskManager.createZipTask(project, reportsDir, suffix)
                    def email = AnalysisTaskManager.createEmailTask(project, reportsDir,
                            emailExtension, suffix)
                    // ... -> target -> zip -> email
                    TaskUtils.adjustTaskPriorities(target, zip)
                    TaskUtils.adjustTaskPriorities(zip, email)
                    break
                case EmailExtension.HTML:
                    def email = AnalysisTaskManager.createEmailTask(project, reportsDir,
                            emailExtension, suffix)
                    // ... -> target -> email
                    TaskUtils.adjustTaskPriorities(target, email)
                    break
            }
        }
    }
}
