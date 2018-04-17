package com.camnter.android.staticanalysis.plugin

import com.camnter.android.staticanalysis.plugin.utils.PluginUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.quality.CheckstylePlugin
import org.gradle.api.plugins.quality.CodeQualityExtension
import org.gradle.api.plugins.quality.FindBugsPlugin
import org.gradle.api.plugins.quality.PmdPlugin

import java.lang.reflect.Field

/**
 * @author CaMnter
 */

class VersionHooker {

    private static def setCodeQualityExtensionVersion(Class<? extends Plugin> pluginClazz,
            Plugin plugin,
            String version) {
        try {
            Field extensionField = pluginClazz.class.getDeclaredField('extension')
            extensionField.setAccessible(true)
            def extension = extensionField.get(plugin)
            (extension as CodeQualityExtension).setToolVersion(version)
        } catch (Exception ignored) {
            // ignored
        }
    }

    static def setCheckstyleVersion(Project project, String version) {
        CheckstylePlugin checkstylePlugin = PluginUtils.getPluginIfApply(project,
                CheckstylePlugin.class)
        if (checkstylePlugin == null) return

        setCodeQualityExtensionVersion(CheckstylePlugin.class, checkstylePlugin, version)
    }

    static def setFindBugsVersion(Project project, String version) {
        FindBugsPlugin findBugsPlugin = PluginUtils.getPluginIfApply(project,
                FindBugsPlugin.class)
        if (findBugsPlugin == null) return

        setCodeQualityExtensionVersion(FindBugsPlugin.class, findBugsPlugin, version)
    }

    static def setPmsVersion(Project project, String version) {
        PmdPlugin pmdPlugin = PluginUtils.getPluginIfApply(project,
                PmdPlugin.class)
        if (pmdPlugin == null) return

        setCodeQualityExtensionVersion(PmdPlugin.class, pmdPlugin, version)
    }
}