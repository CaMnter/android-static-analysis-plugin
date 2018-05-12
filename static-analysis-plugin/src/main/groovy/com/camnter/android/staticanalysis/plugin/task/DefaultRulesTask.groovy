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

import com.camnter.android.staticanalysis.plugin.utils.ResourcesUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

/**
 * @author CaMnter
 */

class DefaultRulesTask extends DefaultTask {

    public static final String DEFAULT_PMD_RULE_PATH = "default-rules/pmd-ruleset.xml"
    public static final String DEFAULT_LINT_RULE_PATH = "default-rules/lint.xml"
    public static final String DEFAULT_FINDBUGS_RULE_PATH = "default-rules/findbugs-filter.xml"
    public static final String DEFAULT_CHECKSTYLE_RULE_PATH = "default-rules/checkstyle.xml"
    public static
    final String DEFAULT_CHECKSTYLE_SUPPRESSIONS_RULE_PATH = "default-rules/checkstyle-suppressions.xml"

    @Input
    @Optional
    String reportsDir

    @Input
    @Optional
    boolean createDefaultPmdRule = false

    @Input
    @Optional
    boolean createDefaultLintRule = false

    @Input
    @Optional
    boolean createDefaultFindBugsRule = false

    @Input
    @Optional
    boolean createDefaultCheckstyleRule = false

    @Input
    @Optional
    boolean createDefaultCheckstyleSuppressionsRule = false

    def classloader

    @TaskAction
    void main() {
        classloader = this.class.classLoader
        if (createDefaultPmdRule) {
            File ruleFile = new File(reportsDir, DEFAULT_PMD_RULE_PATH)

            printf "%-51s = %s\n",
                    ['[DefaultRulesTask]   [pmd-rule-file]', ruleFile.absolutePath]
            ResourcesUtils.copyResource(classloader, "META-INF/gradle-plugins/pmd-ruleset.xml",
                    ruleFile.absolutePath)
        }
        if (createDefaultLintRule) {
            File ruleFile = new File(reportsDir, DEFAULT_LINT_RULE_PATH)
            printf "%-51s = %s\n",
                    ['[DefaultRulesTask]   [lint-rule-file]', ruleFile.absolutePath]
            ResourcesUtils.copyResource(classloader, "META-INF/gradle-plugins/lint.xml",
                    ruleFile.absolutePath)
        }
        if (createDefaultFindBugsRule) {
            File ruleFile = new File(reportsDir, DEFAULT_FINDBUGS_RULE_PATH)
            printf "%-51s = %s\n",
                    ['[DefaultRulesTask]   [findbugs-rule-file]', ruleFile.absolutePath]
            ResourcesUtils.copyResource(classloader, "META-INF/gradle-plugins/findbugs-filter.xml",
                    ruleFile.absolutePath)
        }
        if (createDefaultCheckstyleRule) {
            File ruleFile = new File(reportsDir, DEFAULT_CHECKSTYLE_RULE_PATH)
            printf "%-51s = %s\n",
                    ['[DefaultRulesTask]   [checkstyle-rule-file]', ruleFile.absolutePath]
            ResourcesUtils.copyResource(classloader, "META-INF/gradle-plugins/checkstyle.xml",
                    ruleFile.absolutePath)
        }
        if (createDefaultCheckstyleSuppressionsRule) {
            File ruleFile = new File(reportsDir, DEFAULT_CHECKSTYLE_SUPPRESSIONS_RULE_PATH)
            printf "%-51s = %s\n",
                    ['[DefaultRulesTask]   [checkstyle-suppressions-file]', ruleFile.absolutePath]
            ResourcesUtils.copyResource(classloader,
                    "META-INF/gradle-plugins/checkstyle-suppressions.xml",
                    ruleFile.absolutePath)
        }
    }
}
