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
import com.camnter.android.staticanalysis.plugin.task.envelope.Envelope
import com.camnter.android.staticanalysis.plugin.task.envelope.EnvelopeChainData
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

/**
 * @author CaMnter
 */

class EmailTask extends DefaultTask {

    static final def LOCAL_PROPERTIES = 'local.properties'
    static final def SMTP_HOST = 'asap.smtpHost'
    static final def SMTP_USER = 'asap.smtpUser'
    static final def SMTP_PASSWORD = 'asap.smtpPassword'

    static final def JAVA_MAIL_SMTP_HOST = 'mail.smtp.host'
    static final def JAVA_MAIL_SMTP_AUTH = 'mail.smtp.auth'
    static final def RECEIVERS_DIVIDE = ';'

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
            sendHtmlEmail(email, htmlPaths)
        } else if (EmailExtension.ZIP == email.enclosureType) {
            sendEnclosureZip(email, zipPath)
        }
    }

    def sendEnclosureZip(EmailExtension email, String zipPath) {
        try {
            EnvelopeChainData data = new EnvelopeChainData(project)
            data.email = email
            data.zipPath = zipPath

            Envelope.ReceiversCheckChain<Envelope.ZipCheckChain> receiversCheckChain = new Envelope.ReceiversCheckChain<Envelope.ZipCheckChain>(
                    data)
            Envelope.ZipCheckChain<Envelope.LocalPropertiesChain> zipCheckChain = new Envelope.ZipCheckChain<Envelope.LocalPropertiesChain>(
                    data)
            // TODO default NetEase QQ
            Envelope.LocalPropertiesChain<Envelope.DefaultSessionChain> localPropertiesChain = new Envelope.LocalPropertiesChain<Envelope.DefaultSessionChain>(
                    data)
            Envelope.DefaultSessionChain<Envelope.ZipLetterChain> defaultSessionChain = new Envelope.DefaultSessionChain<Envelope.ZipLetterChain>(
                    data)
            Envelope.ZipLetterChain zipLetterChain = new Envelope.ZipLetterChain(data)

            receiversCheckChain.next = zipCheckChain
            zipCheckChain.next = localPropertiesChain
            localPropertiesChain.next = defaultSessionChain
            defaultSessionChain.next = zipLetterChain

            receiversCheckChain.execute()
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    def sendHtmlEmail(EmailExtension email, List<String> htmlPaths) {
        try {
            EnvelopeChainData data = new EnvelopeChainData(project)
            data.email = email
            data.htmlPaths = htmlPaths

            Envelope.ReceiversCheckChain<Envelope.ZipCheckChain> receiversCheckChain = new Envelope.ReceiversCheckChain<Envelope.ZipCheckChain>(
                    data)
            Envelope.HtmlCheckChain<Envelope.LocalPropertiesChain> htmlCheckChain = new Envelope.HtmlCheckChain<Envelope.LocalPropertiesChain>(
                    data)
            // TODO default NetEase QQ
            Envelope.LocalPropertiesChain<Envelope.DefaultSessionChain> localPropertiesChain = new Envelope.LocalPropertiesChain<Envelope.DefaultSessionChain>(
                    data)
            Envelope.DefaultSessionChain<Envelope.ZipLetterChain> defaultSessionChain = new Envelope.DefaultSessionChain<Envelope.ZipLetterChain>(
                    data)
            Envelope.HtmlLetterChain htmlLetterChain = new Envelope.HtmlLetterChain(data)

            receiversCheckChain.next = htmlCheckChain
            htmlCheckChain.next = localPropertiesChain
            localPropertiesChain.next = defaultSessionChain
            defaultSessionChain.next = htmlLetterChain

            receiversCheckChain.execute()
        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}
