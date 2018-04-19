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

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * @author CaMnter
 */

class ZipUtils {

    static final def BUFFER_SIZE = 2 * 1024

    static def toZip(String dir, String target, boolean keepDirStructure) {
        FileOutputStream out = new FileOutputStream(new File(target))
        ZipOutputStream zipOutputStream = null
        try {
            zipOutputStream = new ZipOutputStream(out)
            File sourceFile = new File(dir)
            compress(sourceFile, zipOutputStream, sourceFile.getName(), keepDirStructure)
        } catch (Exception ignored) {
            // ignored
        } finally {
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.close()
                } catch (IOException e) {
                    e.printStackTrace()
                }
            }
        }
    }

    private static def compress(File sourceFile, ZipOutputStream zos, String name,
            boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE]
        if (sourceFile.isFile()) {
            zos.putNextEntry(new ZipEntry(name))
            int len
            FileInputStream inputStream = new FileInputStream(sourceFile)
            while ((len = inputStream.read(buf)) != -1) {
                zos.write(buf, 0, len)
            }
            zos.closeEntry()
            inputStream.close()
        } else {
            File[] listFiles = sourceFile.listFiles()
            if (listFiles == null || listFiles.length == 0) {
                if (KeepDirStructure) {
                    zos.putNextEntry(new ZipEntry(name + "/"))
                    zos.closeEntry()
                }
            } else {
                for (File file : listFiles) {
                    if (KeepDirStructure) {
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure)
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure)
                    }
                }
            }
        }
    }
}