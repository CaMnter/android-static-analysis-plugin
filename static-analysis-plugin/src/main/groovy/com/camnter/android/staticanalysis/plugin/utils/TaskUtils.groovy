package com.camnter.android.staticanalysis.plugin.utils

import org.gradle.api.Task

/**
 * @author CaMnter
 */

class TaskUtils {

    static def adjustTaskPriorities(Task highPriorityTask, Task lowPriorityTask) {
        lowPriorityTask.dependsOn(highPriorityTask)
        lowPriorityTask.mustRunAfter(highPriorityTask)
        highPriorityTask.finalizedBy(lowPriorityTask)
    }

}