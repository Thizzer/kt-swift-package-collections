# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.2.0] - 2022-05-24

### Added
- Added convenience function to sign collection.
- Option to retrieve JSON content without signature.

### Changed
- Bumped Gradle version.
- Added moduleName and name to VersionTargetList.

### Fixed
- Forward version to inline Platform object.

## [1.1.0] - 2022-04-01

### Added
- Added license field to Version

### Changed
- Removed need to configure Package license field.

### Fixed
- Moved verifiedCompatibility to PackageVersion.

## [1.0.2] - 2022-03-29

### Changed 
- Always truncate dates to seconds in setter.

### Fixed
- Fix for packageName being used for wrong field.
- Fix for createdAt field for package.

## [1.0.1] - 2022-03-28

### Added
- Missing fields.

### Fixed
- Using package version as manifests key instead of tools version.

## [1.0.1] - 2022-03-16

### Added
- Initial release
