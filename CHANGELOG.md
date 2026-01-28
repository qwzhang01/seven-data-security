# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- GitHub Actions CI/CD workflows
- Code coverage reporting with JaCoCo
- Performance benchmark tests with JMH

### Changed
- Improved test coverage

## [1.2.23] - 2026-01-29

### Added
- Single table SELECT query processing with automatic table prefix
- Support for `SELECT *` → `SELECT table.*` conversion

### Fixed
- SQL print utility now properly handles special characters in parameters
- Fixed regex replacement issues with `$` and `\` characters

## [1.2.21] - 2026-01-15

### Added
- Data scope validation with whitelist support (`withoutRights`)
- Enhanced QueryWrapper parameter encryption for MyBatis-Plus

### Changed
- Improved field matching logic for camelCase and snake_case conversion

### Fixed
- Thread safety improvements in encryption context

## [1.2.0] - 2025-12-01

### Added
- `@EncryptField` annotation for automatic field encryption
- `Encrypt` wrapper type for type-safe encryption
- `DataScopeHelper` for fluent data scope API
- SQL printing interceptor for debugging

### Changed
- Upgraded to Spring Boot 3.1.5
- Upgraded to MyBatis-Plus 3.5.11

## [1.1.0] - 2025-10-15

### Added
- Data scope SQL rewriting with JOIN and WHERE injection
- Multiple encryption algorithm support (DES, AES, custom)
- Jackson serialization support for `Encrypt` type

### Fixed
- Memory leak in reflection cache

## [1.0.0] - 2025-09-01

### Added
- Initial release
- Basic field encryption/decryption via MyBatis interceptors
- Transparent encryption for INSERT/UPDATE operations
- Transparent decryption for SELECT operations
- Spring Boot auto-configuration

[Unreleased]: https://github.com/qwzhang01/seven-data-security/compare/v1.2.23...HEAD
[1.2.23]: https://github.com/qwzhang01/seven-data-security/compare/v1.2.21...v1.2.23
[1.2.21]: https://github.com/qwzhang01/seven-data-security/compare/v1.2.0...v1.2.21
[1.2.0]: https://github.com/qwzhang01/seven-data-security/compare/v1.1.0...v1.2.0
[1.1.0]: https://github.com/qwzhang01/seven-data-security/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/qwzhang01/seven-data-security/releases/tag/v1.0.0
