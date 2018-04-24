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

import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.*

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
            if (StringUtils.isEmpty(email.receivers)) {
                throw MissingMailParameterException(MissingMailParameterException.Where.EXTENSION,
                        'receivers')
            }

            // zip check
            File zipFile = new File(zipPath)
            if (!zipFile.exists()) {
                printf "%-29s = %s\n",
                        ['[EmailTask]   [sendEnclosureZip]', "${zipPath} was not found"]
                return
            }

            def smtpMap = loadLocalProperties()
            def smtpHost = smtpMap.smtpHost
            def smtpUser = smtpMap.smtpUser
            def smtpPassword = smtpMap.smtpPassword

            Properties properties = System.getProperties()
            properties.setProperty(JAVA_MAIL_SMTP_HOST, smtpHost)
            properties.setProperty(JAVA_MAIL_SMTP_AUTH, "true")
            Session session = Session.getDefaultInstance(properties)
            MimeMessage message = new MimeMessage(session)
            // nickname
            if (StringUtils.isEmpty(email.nickname)) {
                message.setFrom(new InternetAddress(smtpUser))
            } else {
                InternetAddress from = new InternetAddress(MimeUtility.encodeWord(
                        MimeUtility.encodeWord("${email.nickname}") + " <${smtpUser}>"))
                message.setFrom(from)
            }

            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(email.receivers))
            message.setSubject(email.theme)
            // content
            BodyPart contentbodyPart = new MimeBodyPart()
            if (!StringUtils.isEmpty(email.content)) {
                contentbodyPart.setText(email.content + '\n\n\n\n')
            }
            // enclosure
            BodyPart enclosureBodyPart = new MimeBodyPart()
            DataSource source = new FileDataSource(zipFile.absolutePath)
            enclosureBodyPart.setDataHandler(new DataHandler(source))
            enclosureBodyPart.setFileName(zipFile.name)
            // multipart
            Multipart multipart = new MimeMultipart()
            multipart.addBodyPart(contentbodyPart)
            multipart.addBodyPart(enclosureBodyPart)

            message.setContent(multipart)
            Transport.send(message, smtpUser, smtpPassword)
            printf "%-32s = %s\n",
                    ['[EmailTask]   [sendEnclosureZip]', "${zipPath}"]
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    def sendHtmlEmail(EmailExtension email, List<String> htmlPaths) {
        try {
            if (StringUtils.isEmpty(email.receivers)) {
                throw MissingMailParameterException(MissingMailParameterException.Where.EXTENSION,
                        'receivers')
            }

            // html check
            List<File> safeHtmlFiles = new ArrayList<>()
            for (String path : htmlPaths) {
                File htmlFile = new File(path)
                if (!htmlFile.exists()) {
                    printf "%-29s = %s\n",
                            ['[EmailTask]   [sendHtmlEmail]', "${htmlFile.absolutePath} was not found"]
                } else {
                    safeHtmlFiles.add(htmlFile)
                }
            }
            if (safeHtmlFiles.size() == 0) return


            def smtpMap = loadLocalProperties()
            def smtpHost = smtpMap.smtpHost
            def smtpUser = smtpMap.smtpUser
            def smtpPassword = smtpMap.smtpPassword

            Properties properties = System.getProperties()
            properties.setProperty(JAVA_MAIL_SMTP_HOST, smtpHost)
            properties.setProperty(JAVA_MAIL_SMTP_AUTH, "true")
            Session session = Session.getDefaultInstance(properties)
            for (File htmlFile : safeHtmlFiles) {
                MimeMessage message = new MimeMessage(session)
                // nickname
                if (StringUtils.isEmpty(email.nickname)) {
                    message.setFrom(new InternetAddress(smtpUser))
                } else {
                    InternetAddress from = new InternetAddress(MimeUtility.encodeWord(
                            MimeUtility.encodeWord("${email.nickname}") + " <${smtpUser}>"))
                    message.setFrom(from)
                }
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
                        ['[EmailTask]   [sendHtmlEmail]', "${htmlFile.absolutePath}"]
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    def loadLocalProperties() {
        Properties localProperties = new Properties()
        localProperties.load(project.rootProject.file(LOCAL_PROPERTIES).newDataInputStream())
        def smtpHost = localProperties.getProperty(SMTP_HOST)
        def smtpUser = localProperties.getProperty(SMTP_USER)
        def smtpPassword = localProperties.getProperty(SMTP_PASSWORD)
        if (StringUtils.isEmpty(smtpHost)) {
            throw MissingMailParameterException(MissingMailParameterException.Where.LOCAL,
                    SMTP_HOST)
        }
        if (StringUtils.isEmpty(smtpUser)) {
            throw MissingMailParameterException(MissingMailParameterException.Where.LOCAL,
                    SMTP_USER)
        }
        if (StringUtils.isEmpty(smtpPassword)) {
            throw MissingMailParameterException(MissingMailParameterException.Where.LOCAL,
                    SMTP_PASSWORD)
        }
        return [smtpHost: smtpHost, smtpUser: smtpUser, smtpPassword: smtpPassword]
    }
}
