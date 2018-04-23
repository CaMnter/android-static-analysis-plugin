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

import com.camnter.android.staticanalysis.plugin.exception.MissingMailParameterException
import com.camnter.android.staticanalysis.plugin.extension.EmailExtension
import com.camnter.android.staticanalysis.plugin.utils.StringUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

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
                sendHtmlEmail(email, path)
            }
        } else if (EmailExtension.ZIP == email.enclosureType) {
            // TODO
        }
    }

    def sendHtmlEmail(EmailExtension email, String htmlPath) {
        try {
            if (StringUtils.isEmpty(email.receivers)) {
                throw MissingMailParameterException(MissingMailParameterException.Where.EXTENSION,
                        'receivers')
            }

            // html check
            File htmlFile = new File(htmlPath)
            if (!htmlFile.exists()) {
                printf "%-29s = %s\n",
                        ['[EmailTask]   [sendHtmlEmail]', "${htmlFile} was not found"]
                return
            }

            def smtpMap = loadLocalProperties()
            def smtpHost = smtpMap.smtpHost
            def smtpUser = smtpMap.smtpUser
            def smtpPassword = smtpMap.smtpPassword

            Properties properties = System.getProperties()
            properties.setProperty("mail.smtp.host", smtpHost)
            properties.setProperty("mail.smtp.user", smtpUser)
            properties.setProperty("mail.smtp.password", smtpPassword)
            properties.setProperty("mail.smtp.auth", "true")
            Session session = Session.getDefaultInstance(properties)
            MimeMessage message = new MimeMessage(session)
            message.setFrom(new InternetAddress(smtpUser))
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(email.receivers))
            if (!StringUtils.isEmpty(email.carbonCopy)) {
                message.addRecipient(Message.RecipientType.CC,
                        new InternetAddress(email.carbonCopy))
            }
            message.setSubject(email.theme)

            StringBuilder builder = new StringBuilder()
            htmlFile.eachLine { String line -> builder.append(line) }
            message.setContent(builder.toString(), "text/html")
            Transport.send(message, smtpUser, smtpPassword)
            printf "%-29s = %s\n",
                    ['[EmailTask]   [sendHtmlEmail]', "${htmlFile}"]
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    def loadLocalProperties() {
        Properties localProperties = new Properties()
        localProperties.load(project.rootProject.file('local.properties').newDataInputStream())
        def smtpHost = localProperties.getProperty('asap.smtpHost')
        def smtpUser = localProperties.getProperty('asap.smtpUser')
        def smtpPassword = localProperties.getProperty('asap.smtpPassword')
        if (StringUtils.isEmpty(smtpHost)) {
            throw MissingMailParameterException(MissingMailParameterException.Where.LOCAL,
                    'asap.smtpHost')
        }
        if (StringUtils.isEmpty(smtpUser)) {
            throw MissingMailParameterException(MissingMailParameterException.Where.LOCAL,
                    'asap.smtpUser')
        }
        if (StringUtils.isEmpty(smtpPassword)) {
            throw MissingMailParameterException(MissingMailParameterException.Where.LOCAL,
                    'asap.smtpPassword')
        }
        return [smtpHost: smtpHost, smtpUser: smtpUser, smtpPassword: smtpPassword]
    }
}
