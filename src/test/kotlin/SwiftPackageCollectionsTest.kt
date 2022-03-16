/**
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
import com.thizzer.swift.packages.collections.packageCollection
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SwiftPackageCollectionsTest {

    @Test
    fun prettyPrintDiffPackageCollectionTest() {
        val collection = packageCollection {  }
        Assertions.assertNotEquals(collection.toJson(true), collection.toJson(false))
    }
}