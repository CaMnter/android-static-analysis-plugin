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
    com.camnter.gradle.plugin:static-analysis-plugin:1.0.6
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
        // eg: true or false
        enable = true
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
        // eg: true or false
        enable = true
        // eg: "${project.project.rootDir}/d.xml"
        lintConfig = "${project.buildDir}/android-static-analysis/default-rules/lint.xml"
    }
    findBugs {
        // eg: true or false
        enable = true
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
        // eg: true or false
        enable = true
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
        suppressionsPath = "${project.buildDir}/android-static-analysis/default-rules/checkstyle-suppressions.xml"
    }
}
```

<br>
<br>

# Execute

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

# Automatically send mail

```groovy
androidStaticAnalysis {
    email{
        // whether to send email automatically, default is false
        // eg: true or false
        send = true
        // sender nickname
        nickname = 'CaMnter'
        // email theme
        theme = 'Android static analysis'
        // email content
        // failure when enclosureType = 'html'
        // valid when enclosureType = 'zip'
        content = 'Android static analysis'
        // the recipient, many separated by ;
        // eg: 'a@gmail.com;b@gmail.com'
        receivers = 'yuanyu.camnter@gmail.com'
        // cc, many people separated by ;
        // eg: 'c@gmail.com;d@gmail.com'
        carbonCopy = 'yuanyu.camnter@gmail.com'
        // Analyze content, html format or zip format, default html format
        // eg: html or zip
        enclosureType = 'html'
    }
}
```

This is only part of.

<br>



## `Stmp related configuration`  

Complete the following configuration within **local.properties**

```groovy
asap.smtpHost=company stmp server address
asap.smtpUser=company email
asap.smtpPassword=company email password
```

<br>

### `Google Mail Stmp Configuration`  

**1.** Open ["Forwarding and POP/IMAP" in gmail settings page](https://mail.google.com/mail/u/0/#settings/fwdandpop). 
  
**2.** Check on "**Enable IMAP**".
  
**3.** Open [Two-step verification](https://myaccount.google.com/security?hl=zh-CN#signin). 
  
**4.** Create [gmail app password](https://security.google.com/settings/security/apppasswords).  

**Gmail app password** as **gmail** **stmp password**.

```groovy
asap.smtpHost=smtp.gmail.com
asap.smtpUser=gmail
asap.smtpPassword=gmail app password
```

![gmail_1](https://github.com/CaMnter/android-static-analysis-plugin/blob/master/screenshots/gmail_1.jpg)
<img src="https://github.com/CaMnter/android-static-analysis-plugin/blob/master/screenshots/gmail_2.jpg" width="600x"/>
<img src="https://github.com/CaMnter/android-static-analysis-plugin/blob/master/screenshots/gmail_3.jpg" width="600x"/>

<br>

### `QQ Mail Stmp Configuration`     

**1.** **QQ** mailbox setting page，Open "**POP3/SMTP Service**".  

**2.** Generate an authorization code.

 
**Authorization code** as **QQ mailbox** **stmp password**. 

```groovy
asap.smtpHost=smtp.qq.com
asap.smtpUser=qq email
asap.smtpPassword=authorization code
```

![qq_1](https://github.com/CaMnter/android-static-analysis-plugin/blob/master/screenshots/qq_1.jpg)
<img src="https://github.com/CaMnter/android-static-analysis-plugin/blob/master/screenshots/qq_2.jpg" width="600x"/>

<br>

### `NetEase Email Stmp Configuration`     

**1.** **NetEase Mailbox** Settings page, open "**POP3/SMTP Service**".  
   
**2.** Set **Client Authorization Password**.

**Client Authorization Password** as the **stmp password** for **NetEase Mailbox**.

```groovy
asap.smtpHost=smtp.163.com
asap.smtpUser=NetEase mailbox
asap.smtpPassword=Client Authorization Password
```

![netease_1](https://github.com/CaMnter/android-static-analysis-plugin/blob/master/screenshots/netease_1.jpg)
![netease_2](https://github.com/CaMnter/android-static-analysis-plugin/blob/master/screenshots/netease_2.jpg)

<br>

### `Sina Mail Stmp Configuration`     

**Sina Mailbox** Settings page, open "**POP3/SMTP Service**".   

```groovy
asap.smtpHost=smtp.sina.com
asap.smtpUser=sina mailbox
asap.smtpPassword=sina mailbox password
```

![sina_1](https://github.com/CaMnter/android-static-analysis-plugin/blob/master/screenshots/sina_1.jpg)

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
