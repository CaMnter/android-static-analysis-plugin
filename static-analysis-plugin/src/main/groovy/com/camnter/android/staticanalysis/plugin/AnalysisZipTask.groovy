package com.camnter.android.staticanalysis.plugin

import com.camnter.android.staticanalysis.plugin.utils.ZipUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

/**
 * @author CaMnter
 */

class AnalysisZipTask extends DefaultTask {

    @Input
    @Optional
    String inputDir

    @Input
    @Optional
    String zipPath

    @TaskAction
    void main() {
        ZipUtils.toZip(inputDir, zipPath, true)
    }
}
