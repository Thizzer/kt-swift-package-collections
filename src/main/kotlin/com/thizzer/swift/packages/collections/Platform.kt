/**
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.thizzer.swift.packages.collections

data class Platform(val name: String, var version: String? = null) {
    companion object {
        val macOS: String = "macOS"
        val iOS: String = "iOS"
        val tvOS: String = "tvOS"
        val watchOS: String = "watchOS"
        val Linux: String = "Linux"
        val Android: String = "Android"
        val Windows: String = "Windows"
    }
}

class PlatformList : MutableList<Platform> by mutableListOf() {
    fun platform(name: String, version: String? = null, configure: Platform.() -> Unit): Platform {
        val platform = Platform(name, version)
        platform.configure()
        add(platform)
        return platform
    }
}

data class PlatformCompatibility(val platform: Platform, val swiftVersion: String)

class PlatformCompatibilityList : MutableList<PlatformCompatibility> by mutableListOf() {
    fun compatibility(platform: Platform, swiftVersion: String, configure: PlatformCompatibility.() -> Unit): PlatformCompatibility {
        val compat = PlatformCompatibility(platform, swiftVersion)
        compat.configure()
        add(compat)
        return compat
    }
}