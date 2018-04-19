package com.camnter.android.staticanalysis.plugin.extension

import com.camnter.android.staticanalysis.plugin.utils.StringUtils

/**
 * @author CaMnter
 */

class EmailExtension {

    static final def ZIP = 'zip'
    static final def HTML = 'html'

    public boolean send = false
    public String theme = ''
    public String content = ''
    public String receivers = ''
    public String carbonCopy = ''
    // html or zip
    public String enclosureType = 'html'

    static def buildCommand(EmailExtension email, String enclosureAbsolutePath) {
        def command = ''
        if (StringUtils.isEmpty(email.receivers)) return
        if (HTML == email.enclosureType && enclosureAbsolutePath.endsWith(HTML)) {
            command =
                    "mutt -s '${email.theme}'  -e 'set content_type=\"text/html\"'  ${email.receivers}"
            if (!StringUtils.isEmpty(email.carbonCopy)) {
                command = "${command}  -c ${email.carbonCopy}"
            }
            command = "${command}  < ${enclosureAbsolutePath}"
        } else if (ZIP == email.enclosureType && enclosureAbsolutePath.endsWith(ZIP)) {
            command = "echo '${email.content}' | mutt -s '${email.theme}'  ${email.receivers}"
            if (!StringUtils.isEmpty(email.carbonCopy)) {
                command = "${command}  -c ${email.carbonCopy}"
            }
            command = "${command}  -a ${enclosureAbsolutePath}"
        }
        return command
    }

}