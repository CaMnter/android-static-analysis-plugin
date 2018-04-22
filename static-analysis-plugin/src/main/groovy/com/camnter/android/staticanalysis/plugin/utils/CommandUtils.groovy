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

import java.util.concurrent.TimeoutException

/**
 * @author CaMnter
 */

class CommandUtils {

    static void command(String command, Closure outputClosure, Closure errorClosure) {
        commandByOsX(command, outputClosure, errorClosure)
    }

    static void mutt(String command) {
        try {
            println "[AndroidStaticAnalysisPlugin]   [CommandUtils]   [command] = ${command}"
            executeCommand(command, 20000)
        } catch (Exception ignored) {
        }
    }

    private static void commandByOsX(String command, Closure outputClosure, Closure errorClosure) {
        try {
            def process = command.execute()
            println "[AndroidStaticAnalysisPlugin]   [CommandUtils]   [command] = ${command}"
            printCommandInfo(process, outputClosure, errorClosure)
        } catch (Exception e) {
            errorClosure.call(e.message)
        }
    }

    /**
     * Run an external command to return the status. If the specified timeout is exceeded, a
     * TimeoutException is thrown
     * */
    static int executeCommand(final String command, final long timeout)
            throws IOException, InterruptedException, TimeoutException {
        Process process = command.execute()
        Worker worker = new Worker(process)
        worker.start()
        try {
            worker.join(timeout)
            if (worker.exit != null) {
                return worker.exit
            } else {
                throw new TimeoutException()
            }
        } catch (Exception ex) {
            worker.interrupt()
            throw ex
        } finally {
            process.destroy()
        }
    }

    private static class Worker extends Thread {

        private Integer exit
        private final Process process

        private Worker(Process process) {
            this.process = process
        }

        public void run() {
            try {
                exit = process.waitFor()
            } catch (InterruptedException ignore) {
                return
            }
        }
    }

    private static void printCommandInfo(Process process, Closure outputClosure,
            Closure errorClosure) {
        try {
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
        } catch (Exception ignored) {
        }
    }

    static void chmod(String path) {
        command("chmod 755 $path") {} {}
    }
}