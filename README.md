# Kotlin Swift Package Collections

Simple Kotlin library for generating Swift package collections.

## Installation

### Maven

```xml
<dependency>
	<groupId>com.thizzer.kt-swift-package-collections</groupId>
	<artifactId>kt-swift-package-collections</artifactId>
	<version>1.2.0</version>
</dependency>
```

### Gradle

#### Groovy

```gradle
implementation group: 'com.thizzer.kt-swift-package-collections', name: 'kt-swift-package-collections', version: '1.2.0'
```

#### Kotlin

```kotlin
implementation("com.thizzer.kt-swift-package-collections:kt-swift-package-collections:1.2.0")
```

## Usage

```kotlin
val myPackageCollection = packageCollection {  
    name = "Package Collection"
    overview = "My overwhelming package collection."
    
    packages {
        `package` {
            keywords("package")
            
            versions {
                version("1.0.0") {
                    manifest("my-library") {
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