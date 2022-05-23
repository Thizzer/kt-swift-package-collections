/**
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.thizzer.swift.packages.collections

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.RSASSASigner
import org.apache.commons.codec.binary.Base64
import org.cryptacular.x509.dn.NameReader
import org.cryptacular.x509.dn.StandardAttributeType
import java.security.PrivateKey
import java.security.cert.X509Certificate

internal fun PackageCollection.signInternal(signer: JWSSigner, certificateChain: List<X509Certificate>?) {
    if(packages.isEmpty()) {
        throw UnsupportedOperationException("Cannot sign collection without packages.")
    }

    val certificateChainBase64 = mutableListOf<com.nimbusds.jose.util.Base64>()
    certificateChain?.forEach {
        certificateChainBase64.add(com.nimbusds.jose.util.Base64(Base64.encodeBase64String(it.encoded)))
    }

    val jwtHeaderBuilder = JWSHeader.Builder(JWSAlgorithm.RS256)
    jwtHeaderBuilder.x509CertChain(certificateChainBase64)

    val jwsObject = JWSObject(
        jwtHeaderBuilder.build(),
        Payload(toJson(true, includeSignature = false))
    )

    jwsObject.sign(signer)

    this.signature = Signature(getPackageCollectionCertificate(certificateChain?.firstOrNull()), jwsObject.serialize())
}

internal fun PackageCollection.signInternal(privateKey: PrivateKey, certificateChain: List<X509Certificate>?) {
    signInternal(RSASSASigner(privateKey), certificateChain)
}

internal fun getPackageCollectionCertificate(certificate: X509Certificate?): Certificate {
    if(certificate == null) {
        return Certificate()
    }

    val reader = NameReader(certificate)
    val issuer = reader.readIssuer()
    val subject = reader.readSubject()

    val certIssuer = CertificateIssuer(issuer.getValue(StandardAttributeType.CommonName), issuer.getValue(StandardAttributeType.OrganizationName), issuer.getValue(StandardAttributeType.OrganizationalUnitName))
    val certSubject = CertificateSubject(subject.getValue(StandardAttributeType.CommonName), subject.getValue(StandardAttributeType.OrganizationName), subject.getValue(StandardAttributeType.OrganizationalUnitName), subject.getValue(StandardAttributeType.UserId))

    return Certificate(certIssuer, certSubject)
}

internal fun PackageCollection.clone(): PackageCollection {
    val clone = PackageCollection( this.formatVersion,
        this.name,
        this.overview,
        this.keywords,
        this.revision,
        this.generatedBy,
        this.packages,
        this.signature
    )
    clone.generatedAt = this.generatedAt

    return clone;
}

internal fun Any.toJsonString(pretty: Boolean = false): String {
    val jsonMapper = jacksonObjectMapper()
    jsonMapper.registerModule(JavaTimeModule())
    jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    jsonMapper.setDefaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.NON_EMPTY, JsonInclude.Include.ALWAYS))
    jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    val prettyPrinter = DefaultPrettyPrinter()
    prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE)

    return if (pretty) {
        jsonMapper.writer(prettyPrinter).writeValueAsString(this)
    } else {
        jsonMapper.writeValueAsString(this)
    }
}