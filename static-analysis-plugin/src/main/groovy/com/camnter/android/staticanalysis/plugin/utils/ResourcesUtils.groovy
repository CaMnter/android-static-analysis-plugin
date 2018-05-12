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

package com.camnter.android.staticanalysis.plugin.utils

/**
 * @author CaMnter
 */

class ResourcesUtils {

    static final def BUFFER_SIZE = 2 * 1024

    static def copyResource(ClassLoader classLoader, String name, String toPath) {
        def inputStream = classLoader.getResourceAsStream(name)
        def outputStream = null
        try {
            File to = new File(toPath)
            if (!to.parentFile.exists()) {
                to.mkdirs()
            }
            if (to.exists()) {
                to.delete()
            }
            outputStream = new FileOutputStream(toPath)
            byte[] buffer = new byte[BUFFER_SIZE]
            int length
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length)
            }
        } catch (Exception ignore) {
            if (inputStream != null) {
                inputStream.close()
            }
            if (outputStream != null) {
                outputStream.close()
            }
        }
    }

    static def createFile(File target, String content) {
        def checkFileClosure = { File file ->
            if (file == null) return
            if (!file.parentFile.exists()) file.mkdirs()
            if (file.exists()) file.delete()
            file.createNewFile()
        }
        checkFileClosure.call(target)
        target.withWriter('utf-8') { writer -> writer.write content }
    }

}