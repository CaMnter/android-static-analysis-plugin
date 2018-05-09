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

package com.camnter.android.staticanalysis.plugin.task.flow

import com.camnter.android.staticanalysis.plugin.AnalysisTaskManager
import com.camnter.android.staticanalysis.plugin.extension.AndroidStaticAnalysis
import com.camnter.android.staticanalysis.plugin.extension.PmdExtension
import com.camnter.android.staticanalysis.plugin.utils.TaskUtils
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * @author CaMnter
 */

class PmdCheckTaskChain extends CheckTaskChain {

    PmdExtension pmdExtension

    PmdCheckTaskChain(Project project, AndroidStaticAnalysis analysis, String reportsDir,
            String suffix) {
        super(project, analysis, reportsDir, suffix)
        this.pmdExtension = this.analysis.pmd
    }

    @Override
    Task duty(Task preTask) {
        if (pmdExtension.enable) {
            this.task = AnalysisTaskManager.createPmdTask(project, analysis, reportsDir, suffix)
            if (preTask != null) TaskUtils.adjustTaskPriorities(preTask, this.task)
            return this.task
        } else {
            return preTask
        }
    }
}
