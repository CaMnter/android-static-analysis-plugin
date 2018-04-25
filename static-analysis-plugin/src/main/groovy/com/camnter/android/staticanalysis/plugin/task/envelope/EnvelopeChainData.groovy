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

package com.camnter.android.staticanalysis.plugin.task.envelope

import com.camnter.android.staticanalysis.plugin.extension.EmailExtension
import org.gradle.api.Project

import javax.mail.Session

/**
 * @author CaMnter
 */

class EnvelopeChainData {

    final Project project

    EmailExtension email
    String smtpHost
    String smtpUser
    String smtpPassword
    Session session

    String zipPath
    File zipFile

    List<String> htmlPaths
    List<File> safeHtmlFiles

    EnvelopeChainData(Project project) {
        this.project = project
    }
}
