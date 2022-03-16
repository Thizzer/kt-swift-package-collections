/**
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.thizzer.swift.packages.collections

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.time.Instant
import java.time.temporal.ChronoUnit

data class GeneratedBy(var name: String)

data class CertificateIssuer(var commonName: String? = null, var organization: String? = null, var organizationalUnit: String? = null)

data class CertificateSubject(var commonName: String? = null, var organization: String? = null, var organizationalUnit: String? = null, var userID: String? = null)

data class Certificate(var issuer: CertificateIssuer? = null, var subject: CertificateSubject? = null)

data class Signature(val certificate: Certificate, val signature: String)

class PackageCollection(
    val formatVersion: String = "1.0",
    var generatedAt: Instant = Instant.now(),
    var generatedBy: GeneratedBy? = null,
    val keywords: MutableList<String> = mutableListOf(),
    var name: String? = null,
    var overview: String? = null,
    val packages: PackageList = PackageList(),
    var signature: Signature? = null
) {

    fun generatedBy(name: String) {
        generatedBy = GeneratedBy(name)
    }

    fun packages(configure: PackageList.() -> Unit): PackageList {
        packages.configure()
        return packages
    }

    fun keywords(vararg keyword: String) {
        keywords.addAll(keyword)
    }

    fun toJson(pretty: Boolean = false): String {
        generatedAt = generatedAt.truncatedTo(ChronoUnit.SECONDS)

        val jsonMapper = jacksonObjectMapper()
        jsonMapper.registerModule(JavaTimeModule())
        jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        jsonMapper.setDefaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.NON_EMPTY, JsonInclude.Include.ALWAYS))
        jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        return if (pretty) {
            jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this)
        } else {
            jsonMapper.writeValueAsString(this)
        }
    }

    override fun toString(): String {
        return toJson()
    }
}

fun packageCollection(configure: PackageCollection.() -> Unit): PackageCollection {
    val collection = PackageCollection()
    collection.configure()
    return collection
}