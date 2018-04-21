# android-static-analysis-plugin

<br>

**Android hodgepodge static code quality check tool gradle plugin** . **(｡>﹏<｡)**

<br>
   
[中文版](https://github.com/CaMnter/android-static-analysis-plugin/blob/master/README_zh.md)    

<br>
  
[ ![Download](https://api.bintray.com/packages/camnter/maven/android-static-analysis-plugin/images/download.svg) ](https://bintray.com/camnter/maven/android-static-analysis-plugin/_latestVersion)   

<br>   

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


# About configuration

**You can do without any configuration. Plugin automatically generates a set of rules. You can find the automatically generated rules file
.**   

`${project.buildDir}/android-static-analysis/default-rules`

**Just add the dependency configuration to the module you want to apply.**

### `module`  **build.gradle**

```groovy
apply plugin: 'com.camnter.gradle.plugin.static.analysis'
```

<br>
<br>

# If you want to configure

**Full default configuration:**

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

# execute

```shell
gradle check
```
 
**If you open the configuration**   

```groovy
androidStaticAnalysis {
    debugAnalysis = true
    releaseAnalysis = true
}
```

**Run the following command will also be executed**   

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
