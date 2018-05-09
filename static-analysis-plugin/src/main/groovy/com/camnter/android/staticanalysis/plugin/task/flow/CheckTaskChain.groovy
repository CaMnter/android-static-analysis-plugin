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

import com.camnter.android.staticanalysis.plugin.extension.AndroidStaticAnalysis
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * @author CaMnter
 */

abstract class CheckTaskChain extends BaseTaskChain {

    Task task
    Project project
    AndroidStaticAnalysis analysis
    String reportsDir
    String suffix

    CheckTaskChain(Project project, AndroidStaticAnalysis analysis, String reportsDir,
            String suffix) {
        this.project = project
        this.analysis = analysis
        this.reportsDir = reportsDir
        this.suffix = suffix
    }
}
