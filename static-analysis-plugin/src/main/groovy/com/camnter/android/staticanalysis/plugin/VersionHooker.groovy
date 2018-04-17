package com.camnter.android.staticanalysis.plugin

import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.api.plugins.quality.CheckstylePlugin

import java.lang.reflect.Field

class VersionHooker {

    static def setCheckstyleVersion(Project project, String version) {
        final PluginContainer pluginManager = project.getPlugins()
        if (pluginManager.hasPlugin(CheckstylePlugin.class)) {
            try {
                CheckstylePlugin checkstylePlugin = pluginManager.getPlugin(CheckstylePlugin.class)
                Field extensionField = CheckstylePlugin.class.getDeclaredField('extension')
                extensionField.setAccessible(true)
                def extension = extensionField.get(checkstylePlugin)
                (extension as CheckstyleExtension).setToolVersion(version)
            } catch (Exception ignored) {
                // ignored
            }
        }
    }



}