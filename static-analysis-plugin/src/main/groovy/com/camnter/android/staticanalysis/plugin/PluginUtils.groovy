package com.camnter.android.staticanalysis.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer

class PluginUtils {

    static def applyPluginIfNotApply(Project project, Class<? extends Plugin> pluginClazz) {
        PluginContainer pluginManager = project.getPlugins()
        if (pluginManager.hasPlugin(pluginClazz)) {
            return
        }
        pluginManager.apply(pluginClazz)
    }

    static <T extends Plugin> T getPluginIfApply(Project project,
            Class<? extends Plugin> pluginClazz) {
        PluginContainer pluginManager = project.getPlugins()
        if (pluginManager.hasPlugin(pluginClazz)) {
            return pluginManager.getPlugin(pluginClazz)
        } else {
            return null
        }
    }
}
