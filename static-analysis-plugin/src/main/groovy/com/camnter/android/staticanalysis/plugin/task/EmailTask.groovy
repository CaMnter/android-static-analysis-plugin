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
