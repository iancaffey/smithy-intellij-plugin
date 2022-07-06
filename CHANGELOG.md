<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Smithy IntelliJ Plugin Changelog

## [1.5.1]
### Added
- Added navigation bar support with the ability to navigate to any shape in the current file (similar to the structure view).

### Changed
- Expanded support in the import optimizer for detecting redundant imports to shapes within the enclosing namespace.
- Improved the shape id optimizer to replace all occurrences of the shape ids in the file being replaced with an import at once.
- Improved the parsing logic when adding new entries to an object, shape, or trait to avoid intermittently breaking parsing state until the entry has been completed (to keep syntax highlighting working the entire time).

## [1.5.0]
### Added
- Expanded shape reference resolution to support dependencies (incl. pre-built models from JSON AST).
- Added support for viewing the documentation of shapes found within JSON AST (incl. rendering external documentation links).
- Added support for detecting unresolved shapes (with an ability to import them).
- Added support for detecting unused imports (with an ability to automatically remove them).
- Added support for detecting qualified shape ids (with an ability to import them).
- Added support for optimizing/rearranging imports (sorted lexicographically).

### Changed
- Improved the performance of reference resolution (by caching the resolved targets and updating when files have been changed).
- Improved the readability of the auto-complete dialog (adding icons to shapes/members, including prelude + AST shapes, and removing the previous keywords which had live templates).
- Improved the go-to dialogs by including the prelude shapes.

## [1.4.1]
### Added
- Enabled the plugin for use in 2022.1 (and all future builds).

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