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
dependencies {
    com.camnter.gradle.plugin:static-analysis-plugin:1.0.3
}
```

### `module`  **build.gradle**

```groovy
apply plugin: 'com.camnter.gradle.plugin.static.analysis'
```

<br>
<br>


# 关于配置

**你可以不做任何配置。插件自动生成一组规则。只要在你想要应用的模块添加依赖配置即可。你可以找到自动生成的规则文件。**   
   
`${project.buildDir}/android-static-analysis/default-rules`

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
    // eg: true or false
    debugAnalysis = false
    // eg: true or false
    releaseAnalysis = true
    pmd {
        // eg: "6.2.0"
        toolVersion = "6.2.0"
        // eg: true or false
        ignoreFailures = true
        // eg: ["${project.project.rootDir}/a.xml", "${project.project.rootDir}/b.xml"]
        ruleSets = []
        // eg: "${project.project.rootDir}/c.xml"
        ruleSetFiles = "${project.buildDir}/android-static-analysis/default-rules/pmd-ruleset.xml"
    }
    lint {
        // eg: "${project.project.rootDir}/d.xml"
        lintConfig = "${project.buildDir}/android-static-analysis/default-rules/lint.xml"
    }
    findBugs {
        // eg: "3.0.1"
        toolVersion = "3.0.1"
        // eg: true or false
        ignoreFailures = true
        // "min", "default", "max"
        effort = "max"
        // eg: "${project.project.rootDir}/e.xml"
        excludeFilter =
                "${project.buildDir}/android-static-analysis/default-rules/findbugs-filter.xml"
        // eg: "low", "medium", "high"        
        reportLevel = "high"
    }
    checkstyle {
        // eg: "8.8"
        toolVersion = "8.8"
        // eg: true or false
        ignoreFailures = true
        // eg: "${project.project.rootDir}/f.xml"
        configDir = "${project.buildDir}/android-static-analysis/default-rules/checkstyle.xml"
        // eg: 0 - Integer.MAX_VALUE
        maxErrors = 30
        // eg: 0 - Integer.MAX_VALUE
        maxWarnings = 2147483647
        // eg: "${project.project.rootDir}/h.xml"
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
 
**如果你打开了配置**   

```groovy
androidStaticAnalysis {
    debugAnalysis = true
    releaseAnalysis = true
}
```

**运行以下命令也会执行**   

```shell
gradle assembleDebug
```

```shell
gradle assembleRelease
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
