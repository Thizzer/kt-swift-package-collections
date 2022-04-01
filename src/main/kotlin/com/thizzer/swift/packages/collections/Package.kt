/**
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.thizzer.swift.packages.collections

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.Instant
import java.time.temporal.ChronoUnit

data class PackageLicense(val name: String, val url: String?)

data class VersionTarget(var moduleName: String? = null, var name: String? = null)

class VersionTargetList : MutableList<VersionTarget> by mutableListOf() {
    fun target(configure: VersionTarget.() -> Unit): VersionTarget {
        val target = VersionTarget()
        target.configure()
        add(target)
        return target
    }
}

@JsonInclude(JsonInclude.Include.ALWAYS)
class VersionProductType : MutableMap<String, List<String>?> by mutableMapOf() {
    enum class LibraryType {
        static, dynamic, automatic
    }

    fun library(type: LibraryType) {
        this["library"] = listOf(type.name)
    }

    fun executable() {
        this["executable"] = null
    }

    fun snippet() {
        this["snippet"] = null
    }

    fun plugin() {
        this["plugin"] = null
    }

    fun test() {
        this["test"] = null
    }
}

class VersionProduct(
    var name: String? = null,
    val targets: MutableList<String> = mutableListOf(),
    val type: VersionProductType = VersionProductType()
) {
    fun targets(vararg target: String) {
        targets.addAll(target)
    }

    fun type(configure: VersionProductType.() -> Unit) {
        type.configure()
    }
}

class VersionProductList : MutableList<VersionProduct> by mutableListOf() {
    fun product(configure: VersionProduct.() -> Unit): VersionProduct {
        val product = VersionProduct()
        product.configure()
        add(product)
        return product
    }
}

class VersionManifest(
    val toolsVersion: String = "5.3.0",
    var packageName: String? = null,
    val targets: VersionTargetList = VersionTargetList(),
    val products: VersionProductList = VersionProductList(),
    val minimumPlatformVersions: PlatformList = PlatformList(),
    val license: PackageLicense? = null
) {
    fun targets(configure: VersionTargetList.() -> Unit = {}) {
        targets.configure()
    }

    fun products(configure: VersionProductList.() -> Unit = {}) {
        products.configure()
    }

    fun minimumPlatformVersions(configure: PlatformList.() -> Unit = {}) {
        minimumPlatformVersions.configure()
    }
}

class PackageVersion(
    val defaultToolsVersion: String = "5.3.0",
    val version: String,
    var summary: String? = null,
    val manifests: MutableMap<String, VersionManifest> = mutableMapOf(),
    val verifiedCompatibility: PlatformCompatibilityList = PlatformCompatibilityList(),
    var license: PackageLicense? = null
) {
    var createdAt: Instant? = null
        set(value) {
            field = value?.truncatedTo(ChronoUnit.SECONDS)
        }

    fun manifest(packageName: String, configure: VersionManifest.() -> Unit = {}) {
        val packageManifest = VersionManifest(packageName = packageName)
        packageManifest.configure()
        manifests[defaultToolsVersion] = packageManifest
    }

    fun verifiedCompatibility(configure: PlatformCompatibilityList.() -> Unit = {}) {
        verifiedCompatibility.configure()
    }

    fun license(name: String, url: String) {
        license = PackageLicense(name, url)
    }
}

class PackageVersionList : MutableList<PackageVersion> by mutableListOf() {
    fun version(version: String, configure: PackageVersion.() -> Unit = {}): PackageVersion {
        val packageVersion = PackageVersion(version = version)
        packageVersion.configure()
        add(packageVersion)
        return packageVersion
    }
}

class Package(
    var url: String? = null,
    var summary: String? = null,
    var keywords: MutableList<String> = mutableListOf(),
    var readmeURL: String? = null,
    var license: PackageLicense? = null,
    var versions: PackageVersionList = PackageVersionList()
) {
    fun license(name: String, url: String) {
        license = PackageLicense(name, url)
    }

    fun keywords(vararg keyword: String) {
        keywords.addAll(keyword)
    }

    fun versions(configure: PackageVersionList.() -> Unit = {}) {
        versions.configure()
    }
}

class PackageList : MutableList<Package> by mutableListOf() {
    fun `package`(configure: Package.() -> Unit): Package {
        val pckg = Package()
        pckg.configure()
        add(pckg)
        return pckg
    }
}