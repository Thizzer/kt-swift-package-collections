/**
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.thizzer.swift.packages.collections

import com.nimbusds.jose.JWSSigner
import java.security.PrivateKey
import java.security.cert.X509Certificate
import java.time.Instant
import java.time.temporal.ChronoUnit

data class GeneratedBy(var name: String)

data class CertificateIssuer(var commonName: String? = null, var organization: String? = null, var organizationalUnit: String? = null)

data class CertificateSubject(var commonName: String? = null, var organization: String? = null, var organizationalUnit: String? = null, var userID: String? = null)

data class Certificate(var issuer: CertificateIssuer? = null, var subject: CertificateSubject? = null)

data class Signature(val certificate: Certificate, val signature: String)

class PackageCollection(
    val formatVersion: String = "1.0",
    var name: String? = null,
    var overview: String? = null,
    val keywords: MutableList<String> = mutableListOf(),
    val revision: Int? = null,
    var generatedBy: GeneratedBy? = null,
    val packages: PackageList = PackageList(),
    var signature: Signature? = null
) {
    var generatedAt: Instant = Instant.now()
        set(value) {
            field = value.truncatedTo(ChronoUnit.SECONDS)
        }

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

    fun sign(privateKey: PrivateKey, certificate: X509Certificate) {
        sign(privateKey, listOf(certificate))
    }

    fun sign(privateKey: PrivateKey, certificateChain: List<X509Certificate>) {
        signInternal(privateKey, certificateChain)
    }

    fun sign(signer: JWSSigner, certificate: X509Certificate) {
        sign(signer, listOf(certificate))
    }

    fun sign(signer: JWSSigner, certificateChain: List<X509Certificate>) {
        signInternal(signer, certificateChain)
    }

    fun toJson(pretty: Boolean = false, includeSignature: Boolean = true): String {
        generatedAt = generatedAt.truncatedTo(ChronoUnit.SECONDS)

        val collectionToJson = if(includeSignature || signature == null) {
            this
        }
        else {
            val copyUnsigned = this.clone()
            copyUnsigned.signature = null

            copyUnsigned
        }

        return collectionToJson.toJsonString(pretty)
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