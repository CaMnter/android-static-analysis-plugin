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