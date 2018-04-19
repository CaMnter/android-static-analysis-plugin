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

package com.camnter.android.staticanalysis.plugin.task

import com.camnter.android.staticanalysis.plugin.extension.EmailExtension
import com.camnter.android.staticanalysis.plugin.utils.CommandUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

/**
 * @author CaMnter
 */

class EmailTask extends DefaultTask {

    @Input
    @Optional
    EmailExtension email

    @Input
    @Optional
    String zipPath

    @Input
    @Optional
    List<String> htmlPaths

    @TaskAction
    void main() {
        if (!email.send) return
        if (EmailExtension.HTML == email.enclosureType) {
            for (String path : htmlPaths) {
                CommandUtils.chmod(path)
                CommandUtils.command(EmailExtension.buildCommand(email, path)) {} {}
            }
        } else if (EmailExtension.ZIP == email.enclosureType) {
            CommandUtils.chmod(zipPath)
            CommandUtils.command(EmailExtension.buildCommand(email, zipPath)) {} {}
        }
    }
}
