# android-static-analysis-plugin

<br>
<br>

# Gradle

项目 **build.gradle**

```gradle
com.camnter.gradle.plugin:static-analysis-plugin:1.0.1
```

模块 **build.gradle**

```gradle
apply plugin: 'com.camnter.gradle.plugin.static.analysis'
```

<br>
<br>

# 规则文件

**Pmd**: `${project.rootDir}/static-analysis-plugin-config/pmd/pmd-ruleset.xml`   

**lint**: `${project.rootDir}/static-analysis-plugin-config/lint/lint.xml`   

**findBugs**: `${project.rootDir}/static-analysis-plugin-config/findbugs/findbugs-filter.xml`   

**checkstyle**: `${project.rootDir}/static-analysis-plugin-config/checkstyle/checkstyle.xml`
**checkstyle-suppressions**: `${project.rootDir}/static-analysis-plugin-config/checkstyle/suppressions.xml`


<br>
<br>

# 简单配置

```gradle
androidStaticAnalysis {
    pmd {
        ruleSetFiles = "${project.rootDir}/static-analysis-plugin-config/pmd/pmd-ruleset.xml"
    }
    lint {
        lintConfig = "${project.rootDir}/static-analysis-plugin-config/lint/lint.xml"
    }
    findBugs {
        excludeFilter = "${project.rootDir}/static-analysis-plugin-config/findbugs/findbugs-filter.xml"
    }
    checkstyle {
        configDir = "${project.rootDir}/static-analysis-plugin-config/checkstyle/checkstyle.xml"
        suppressionsPath = "${project.rootDir}/static-analysis-plugin-config/checkstyle/suppressions.xml"
    }
}
```

<br>
<br>

# 全部默认配置

```gradle
androidStaticAnalysis {
    pmd {
        toolVersion = "6.2.0"
        ignoreFailures = true
        ruleSets = []
        ruleSetFiles = "${project.rootDir}/static-analysis-plugin-config/pmd/pmd-ruleset.xml"
    }
    lint {
        lintConfig = "${project.rootDir}/static-analysis-plugin-config/lint/lint.xml"
    }
    findBugs {
        toolVersion = "3.0.1"
        ignoreFailures = true
        effort = "max"
        excludeFilter =
                "${project.rootDir}/static-analysis-plugin-config/findbugs/findbugs-filter.xml"
        reportLevel = "high"
    }
    checkstyle {
        toolVersion = "8.8"
        ignoreFailures = true
        configDir = "${project.rootDir}/static-analysis-plugin-config/checkstyle/checkstyle.xml"
        maxErrors = 30
        maxWarnings = 2147483647
        suppressionsPath = "${project.rootDir}/static-analysis-plugin-config/checkstyle/suppressions.xml"
    }
}
```

<br>
<br>

# 运行

```shell
./gradlew check
```

<br>
<br>

# License

      Copyright (C) 2018 CaMnter yuanyu.camnter@gmail.com

      Licensed under the Apache License, Version 2.0 (the "License");
      you may not use this file except in compliance with the License.
      You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      See the License for the specific language governing permissions and
      limitations under the License.
