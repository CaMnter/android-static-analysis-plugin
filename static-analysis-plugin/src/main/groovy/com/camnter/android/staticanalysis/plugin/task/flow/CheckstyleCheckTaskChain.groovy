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
import com.camnter.android.staticanalysis.plugin.extension.CheckstyleExtension
import com.camnter.android.staticanalysis.plugin.utils.TaskUtils
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * @author CaMnter
 */

class CheckstyleCheckTaskChain extends CheckTaskChain {

    CheckstyleExtension checkstyleExtension

    CheckstyleCheckTaskChain(Project project, AndroidStaticAnalysis analysis, String reportsDir,
            String suffix) {
        super(project, analysis, reportsDir, suffix)
        this.checkstyleExtension = this.analysis.checkstyle
    }

    @Override
    Task duty(Task preTask) {
        if (checkstyleExtension.enable) {
            this.task =
                    AnalysisTaskManager.createCheckstyleTask(project, analysis, reportsDir, suffix)
            if (preTask != null) TaskUtils.adjustTaskPriorities(preTask, this.task)
            return this.task
        } else {
            return preTask
        }
    }
}
