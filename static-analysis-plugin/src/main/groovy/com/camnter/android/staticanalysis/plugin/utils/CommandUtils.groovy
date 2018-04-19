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

class CommandUtils {

    static void command(String command, Closure outputClosure, Closure errorClosure) {
        PluginUtils.dispatchSystem {
            commandByOsX(command, outputClosure, errorClosure)
        } {
            commandByOsX(command, outputClosure, errorClosure)
        } {
            commandByWindow(command, outputClosure, errorClosure)
        }
    }

    private static void commandByOsX(String command, Closure outputClosure, Closure errorClosure) {
        try {
            println "[AndroidStaticAnalysisPlugin]   [CommandUtils]   [command] = ${command}"
            def process = command.execute()
        } catch (Exception e) {
            errorClosure.call(e.message)
        }
    }

    private static void commandByOsXByBash(String command, Closure outputClosure,
            Closure errorClosure) {
        try {
            println "[AndroidStaticAnalysisPlugin]   [CommandUtils]   [command] = ${command}"
            def process = ['bash', '-c', command].execute()
        } catch (Exception e) {
            errorClosure.call(e.message)
        }
    }

    private static void commandByWindow(String command, Closure outputClosure,
            Closure errorClosure) {
        try {
            println "[AndroidStaticAnalysisPlugin]   [CommandUtils]   [command] = ${command}"
            def process = ("cmd /c start  /b ${command}").execute()
        } catch (Exception e) {
            errorClosure.call(e.message)
        }
    }

    private static void printCommandInfo(Process process, Closure outputClosure,
            Closure errorClosure) {
        def output = new StringBuilder()
        def error = new StringBuilder()
        process.consumeProcessOutput(output, error)
        process.waitFor()
        if ('' != output.toString() && 0 != output.length()) {
            def outputString = output.toString()
            printf "%6s:  %s", ['output', outputString]
            if (outputClosure != null) outputClosure.call(outputString)
        }
        if ('' != error.toString() && 0 != error.length()) {
            def errorString = error.toString()
            printf "%6s:  %s", ['error', errorString]
            if (errorClosure != null) errorClosure.call(errorString)
        }
    }

    static void chmod(String path) {
        command("chmod 755 $path") {} {}
    }
}