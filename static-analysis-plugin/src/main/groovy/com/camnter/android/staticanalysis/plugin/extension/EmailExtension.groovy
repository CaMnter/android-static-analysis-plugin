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

package com.camnter.android.staticanalysis.plugin.extension

import com.camnter.android.staticanalysis.plugin.utils.StringUtils
import com.google.common.io.ByteStreams

/**
 * @author CaMnter
 */

class EmailExtension {

    public static final def ZIP = 'zip'
    public static final def HTML = 'html'

    public boolean send = false
    public String theme = ''
    public String content = ''
    public String receivers = ''
    public String carbonCopy = ''
    // html or zip
    public String enclosureType = 'html'

    static def buildCommand(EmailExtension email, String enclosureAbsolutePath) {
        def command = ''
        if (StringUtils.isEmpty(email.receivers)) return command
        if (StringUtils.isEmpty(enclosureAbsolutePath)) return command
        if (!new File(enclosureAbsolutePath).exists()) return command
        if (HTML == email.enclosureType && enclosureAbsolutePath.endsWith(HTML)) {
            command =
                    "/usr/local/bin/mutt -s \"${email.theme}\"  -e 'set content_type=\"text/html\"'  ${email.receivers}"
            if (!StringUtils.isEmpty(email.carbonCopy)) {
                command = "${command}  -c ${email.carbonCopy}"
            }
            command = "${command}  < ${enclosureAbsolutePath}"
        } else if (ZIP == email.enclosureType && enclosureAbsolutePath.endsWith(ZIP)) {
            command =
                    "echo \"${email.content}\" | /usr/local/bin/mutt -s '${email.theme}'  ${email.receivers}"
            if (!StringUtils.isEmpty(email.carbonCopy)) {
                command = "${command}  -c ${email.carbonCopy}"
            }
            command = "${command}  -a ${enclosureAbsolutePath}"
        }
        return command
    }
}