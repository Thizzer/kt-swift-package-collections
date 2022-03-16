# Kotlin Swift Package Collections

Simple Kotlin library for generating Swift package collections.

## Example

```kotlin
val myPackageCollection = packageCollection {  
    name = "Package Collection"
    overview = "My overwhelming package collection."
    
    packages {
        `package` {
            keywords("package")
            
            versions {
                version("1.0.0") {
                    manifest("1.0") {
                        packageName = "my-library"
                        products {
                            product {
                                name = "MyLibrary"
                                targets("MyLibrary")
                                type {
                                    library(VersionProductType.LibraryType.automatic)
                                }
                            }
                        }
                        
                        targets {
                            target {
                                moduleName = "MyLibrary"
                                name = "MyLibrary"
                            }
                        }
                    }
                }
            }
        }
    }
}

val myPackageCollectionJson = myPackageCollection.toJson()
```