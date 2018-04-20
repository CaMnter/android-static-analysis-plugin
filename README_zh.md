# android-static-analysis-plugin

<br>

[ ![Download](https://api.bintray.com/packages/camnter/maven/android-static-analysis-plugin/images/download.svg) ](https://bintray.com/camnter/maven/android-static-analysis-plugin/_latestVersion)  

<br>

**Android 大杂烩静态代码质量检查 gradle plugin** . **(｡>﹏<｡)**   

<br>
<br>

# Gradle

### `project`  **build.gradle**

```groovy
com.camnter.gradle.plugin:static-analysis-plugin:1.0.2
```

### `module`  **build.gradle**

```groovy
apply plugin: 'com.camnter.gradle.plugin.static.analysis'
```

<br>
<br>


# 关于配置

**你可以不做任何配置。插件自动生成一组规则。**   

**只要在你想要应用的模块添加依赖配置即可。**

### `module`  **build.gradle**

```groovy
apply plugin: 'com.camnter.gradle.plugin.static.analysis'
```

<br>
<br>

# 如果你想要配置

**全部默认配置:**

```groovy
androidStaticAnalysis {
    pmd {
        toolVersion = "6.2.0"
        ignoreFailures = true
        ruleSets = []
        ruleSetFiles = "${project.buildDir}/android-static-analysis/default-rules/pmd-ruleset.xml"
    }
    lint {
        lintConfig = "${project.buildDir}/android-static-analysis/default-rules/lint.xml"
    }
    findBugs {
        toolVersion = "3.0.1"
        ignoreFailures = true
        effort = "max"
        excludeFilter =
                "${project.buildDir}/android-static-analysis/default-rules/findbugs-filter.xml"
        reportLevel = "high"
    }
    checkstyle {
        toolVersion = "8.8"
        ignoreFailures = true
        configDir = "${project.buildDir}/android-static-analysis/default-rules/checkstyle.xml"
        maxErrors = 30
        maxWarnings = 2147483647
        suppressionsPath = "${project.buildDir}/android-static-analysis/default-rules/suppressions.xml"
    }
}
```

<br>
<br>

# 运行

```shell
gradle check
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
