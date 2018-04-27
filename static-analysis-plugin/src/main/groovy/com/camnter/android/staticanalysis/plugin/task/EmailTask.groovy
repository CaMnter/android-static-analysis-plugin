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

import static com.camnter.android.staticanalysis.plugin.task.envelope.Envelope.HostDispatcher.Host.*
import static com.camnter.android.staticanalysis.plugin.task.envelope.Envelope.HostDispatcher.getHost

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
        switch (email.enclosureType) {
            case EmailExtension.ZIP:
                sendEnclosureZip(email, zipPath)
                break
            case EmailExtension.HTML:
                sendHtmlEmail(email, htmlPaths)
                break
        }
    }

    def sendEnclosureZip(EmailExtension email, String zipPath) {
        try {
            EnvelopeChainData data = new EnvelopeChainData(project)
            data.email = email
            data.zipPath = zipPath

            Envelope.ReceiversCheckChain<Envelope.ZipCheckChain> receiversCheckChain = new Envelope.ReceiversCheckChain<Envelope.ZipCheckChain>(
                    data)
            Envelope.ZipLetterChain zipLetterChain = new Envelope.ZipLetterChain(data)


            switch (getHost(data)) {
                case QQ:
                    Envelope.ZipCheckChain<Envelope.QQSessionChain> zipCheckChain = new Envelope.ZipCheckChain<Envelope.QQSessionChain>(
                            data)
                    Envelope.QQSessionChain<Envelope.ZipLetterChain> realSessionChain = new Envelope.QQSessionChain<Envelope.ZipLetterChain>(
                            data)
                    receiversCheckChain.next = zipCheckChain
                    zipCheckChain.next = realSessionChain
                    realSessionChain.next = zipLetterChain
                    break
                case NetEase:
                    Envelope.ZipCheckChain<Envelope.NetEaseSessionChain> zipCheckChain = new Envelope.ZipCheckChain<Envelope.NetEaseSessionChain>(
                            data)
                    Envelope.NetEaseSessionChain<Envelope.ZipLetterChain> realSessionChain = new Envelope.NetEaseSessionChain<Envelope.ZipLetterChain>(
                            data)
                    receiversCheckChain.next = zipCheckChain
                    zipCheckChain.next = realSessionChain
                    realSessionChain.next = zipLetterChain
                    break
                case Other:
                    Envelope.ZipCheckChain<Envelope.DefaultSessionChain> zipCheckChain = new Envelope.ZipCheckChain<Envelope.DefaultSessionChain>(
                            data)
                    Envelope.DefaultSessionChain<Envelope.ZipLetterChain> realSessionChain = new Envelope.DefaultSessionChain<Envelope.ZipLetterChain>(
                            data)
                    receiversCheckChain.next = zipCheckChain
                    zipCheckChain.next = realSessionChain
                    realSessionChain.next = zipLetterChain
                    break
            }

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

            Envelope.ReceiversCheckChain<Envelope.HtmlCheckChain> receiversCheckChain = new Envelope.ReceiversCheckChain<Envelope.HtmlCheckChain>(
                    data)

            Envelope.HtmlLetterChain htmlLetterChain = new Envelope.HtmlLetterChain(data)

            switch (getHost(data)) {
                case QQ:
                    Envelope.HtmlCheckChain<Envelope.QQSessionChain> htmlCheckChain = new Envelope.HtmlCheckChain<Envelope.QQSessionChain>(
                            data)
                    Envelope.QQSessionChain<Envelope.HtmlLetterChain> realSessionChain = new Envelope.QQSessionChain<Envelope.HtmlLetterChain>(
                            data)
                    receiversCheckChain.next = htmlCheckChain
                    htmlCheckChain.next = realSessionChain
                    realSessionChain.next = htmlLetterChain
                    break
                case NetEase:
                    Envelope.HtmlCheckChain<Envelope.NetEaseSessionChain> htmlCheckChain = new Envelope.HtmlCheckChain<Envelope.NetEaseSessionChain>(
                            data)
                    Envelope.NetEaseSessionChain<Envelope.HtmlLetterChain> realSessionChain = new Envelope.NetEaseSessionChain<Envelope.HtmlLetterChain>(
                            data)
                    receiversCheckChain.next = htmlCheckChain
                    htmlCheckChain.next = realSessionChain
                    realSessionChain.next = htmlLetterChain
                    break
                case Other:
                    Envelope.HtmlCheckChain<Envelope.DefaultSessionChain> htmlCheckChain = new Envelope.HtmlCheckChain<Envelope.NetEaseSessionChain>(
                            data)
                    Envelope.DefaultSessionChain<Envelope.HtmlLetterChain> realSessionChain = new Envelope.DefaultSessionChain<Envelope.HtmlLetterChain>(
                            data)
                    receiversCheckChain.next = htmlCheckChain
                    htmlCheckChain.next = realSessionChain
                    realSessionChain.next = htmlLetterChain
                    break
            }

            receiversCheckChain.execute()
        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}
