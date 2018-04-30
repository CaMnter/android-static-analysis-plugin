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
    com.camnter.gradle.plugin:static-analysis-plugin:1.0.4
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

# 自动发邮件

```groovy
androidStaticAnalysis {
    email{
        // 是否自动发邮件，默认 false
        // eg: true or false
        send = true
        // 发信人昵称
        nickname = 'CaMnter'
        // 邮件主题
        theme = 'Android static analysis'
        // 邮件内容
        // enclosureType = 'html' 的时候，失效
        // enclosureType = 'zip'  的时候，有效
        content = 'Android static analysis'
        // 收信人，多人则以 ; 号隔开
        // eg: 'a@gmail.com;b@gmail.com'
        receivers = 'yuanyu.camnter@gmail.com'
        // 抄送，多人则以 ; 号隔开
        // eg: 'c@gmail.com;d@gmail.com'
        carbonCopy = 'yuanyu.camnter@gmail.com'
        // eg: html or zip
        // 分析内容，html 格式还是 zip 格式，默认 html 格式
        enclosureType = 'html'
    }
}
```

这只是一部分。

<br>



## `Stmp 相关配置`  

在 **local.properties** 内完成以下配置

```groovy
asap.smtpHost=公司 stmp 服务器地址
asap.smtpUser=公司邮箱
asap.smtpPassword=公司邮箱密码
```

<br>

### `Google 邮箱 Stmp 配置`  

**1.** 打开 [gmail 设置页面中的 "转发和 POP/IMAP"](https://mail.google.com/mail/u/0/#settings/fwdandpop)。 
  
**2.** 勾选上 "**启用 IMAP**"。 
  
**3.** 开启 [两步验证](https://myaccount.google.com/security?hl=zh-CN#signin)。  
  
**4.** 生成 [gmail 专用密码](https://security.google.com/settings/security/apppasswords)。  

**gmail 专用密码** 作为 **gmail** 的 **stmp password**。

```groovy
asap.smtpHost=smtp.gmail.com
asap.smtpUser=gmail 邮箱
asap.smtpPassword=刚才申请的 gmail 专用密码
```

![gmail_1](https://github.com/CaMnter/android-static-analysis-plugin/blob/master/screenshots/gmail_1.jpg)
<img src="https://github.com/CaMnter/android-static-analysis-plugin/blob/master/screenshots/gmail_2.jpg" width="600x"/>
<img src="https://github.com/CaMnter/android-static-analysis-plugin/blob/master/screenshots/gmail_3.jpg" width="600x"/>

<br>

### `QQ 邮箱 Stmp 配置`     

**1.** **QQ** 邮箱设置页面，打开 "**POP3/SMTP服务**"。  

**2.** 生成授权码。  

 
**授权码** 作为 **QQ 邮箱** 的 **stmp password**。 

```groovy
asap.smtpHost=smtp.qq.com
asap.smtpUser=qq 邮箱
asap.smtpPassword=刚才申请的 qq 邮箱授权码
```

![qq_1](https://github.com/CaMnter/android-static-analysis-plugin/blob/master/screenshots/qq_1.jpg)
<img src="https://github.com/CaMnter/android-static-analysis-plugin/blob/master/screenshots/qq_2.jpg" width="600x"/>

<br>

### `网易 邮箱 Stmp 配置`     

**1.** **网易邮箱** 设置页面，打开 "**POP3/SMTP服务**"。  
   
**2.** 设置 **客户端授权密码**。

**客户端授权密码** 作为 **网易邮箱** 的 **stmp password**。

```groovy
asap.smtpHost=smtp.163.com
asap.smtpUser=网易 邮箱
asap.smtpPassword=刚才申请的 网易邮箱 客户端授权密码
```

![netease_1](https://github.com/CaMnter/android-static-analysis-plugin/blob/master/screenshots/netease_1.jpg)
![netease_2](https://github.com/CaMnter/android-static-analysis-plugin/blob/master/screenshots/netease_2.jpg)

<br>

### `Sina 邮箱 Stmp 配置`     

**Sina 邮箱** 设置页面，打开 "**POP3/SMTP服务**"   

```groovy
asap.smtpHost=smtp.sina.com
asap.smtpUser=sina 邮箱
asap.smtpPassword=sina 邮箱密码
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
