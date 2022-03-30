<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Smithy IntelliJ Plugin Changelog

## [1.4.0]
### Added
- Added support for resolving `SmithyShapeId` references (only within the current project for now). 
- Added support for rendering documentation of the target `SmithyShape` which a `SmithyShapeId` references on hover.
- Added go-to contributors for both shapes and members (for use within project search dialogs).
- Added [structure view](https://www.jetbrains.com/help/idea/viewing-structure-of-a-source-file.html) support (with new icons for shapes and members).
- Added support for all `SmithyShapeId` to be renamed.
- Added all shapes and members to code completion dropdowns.

### Changed
- Adjusted file icon to match plugin icon.

### Fixed
- Fixed parsing of traits with an empty arguments list.