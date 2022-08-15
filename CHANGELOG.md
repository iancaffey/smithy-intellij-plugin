<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Smithy IntelliJ Plugin Changelog

## [2.0.1]
### Fixed
- Fixed grammar of IDL 1.0 service shapes.

## [2.0.0]
### Added
- Added support for IDL 2.0.
- Added support for merging trait values.
- Added support for importing shapes within metadata before a namespace has been declared (which will insert the
  best-guess namespace).
- Added gutter icons to navigate between externally applied traits and their target shape.
- Added live template for creating an annotation trait.
- Added live templates for enum and intEnum shapes.
- Added inlay hint for the shape name of inline input/output shapes.
- Added support for annotating unnecessary commas (with a quick fix to remove all commas within the file).
- Added support for annotating shapes with circular references as errors.
- Added go-to contributor for resource identifiers and properties.

### Changed
- Updated the create-file action to take into account the Smithy build version as well as guess the expected namespace.
- Improved code-completion when typing out members in structured traits.
- Expanded keyword highlighting to include boolean and null literals.
- Improved quote handling in incomplete strings.
- Improved default spacing for shape declaration (to enforce whitespace).

### Fixed
- Fixed the renaming behavior of member ids to only update the member name.

## [1.5.5]
### Added
- Added support for an editor notification when a Smithy file is not located in a source root.
- Added support for resolving shape references which have been defined multiple times across dependencies (which will
  ultimately get merged during build time).
- Added support for treating document shape references (and any nested member/value shape references) as soft (which
  prevents it being highlighted as an error).

### Fixed
- Fixed NPE thrown when attempting to index Smithy files without a namespace (e.g. validation files with only a version
  + metadata).

## [1.5.4]
### Added
- Added support for incremental shape resolution (with file-based indexes to improve overall resolution performance).
- Added support for displaying documentation traits and external documentation links of shapes in documentation popups.
- Added support for line breaks in quoted strings.
- Added support for singular unescaped double quotes within text blocks.
- Added support for highlighting null literals like keywords.

### Changed
- Updated map entry shape resolution to resolve to the 'value' member to align with how structure fields are resolved.
- Updated unicode escape sequence highlighting to cover the entire sequence.
- Improved the overall styling of documentation popups to better align with standard IntelliJ styling.

## [1.5.3]
### Added
- Added namespace and enclosing shape (for shape members) information to documentation popups.
- Added support for resolving shape member ids.
- Added support for inferring the shape of node values (providing the same documentation and reference benefits as explicit members).
- Added support for annotating unresolved member shapes as errors.
- Added support for annotating duplicate members as errors (with a quick fix to delete the duplicate).
- Added support for annotating imports which conflict with a shape defined in the current file as an error (with a quick fix to remove the import).
- Added support for annotating unknown members as errors.
- Added support for annotating shapes which are missing required members as errors.

### Changed
- Improved the performance of shape resolution by caching resolved shapes and invalidating upon PSI modifications.
- Improved the import optimizer to omit imports for prelude shapes which do not conflict with an existing shape in the enclosing namespace.
- Improved the incremental parsing of applied trait statements.
- Removed documentation and externalDocumentation traits from the documentation popup.

### Fixed
- Fixed relative shape id resolution for prelude shapes.
- Adjusted keyword highlighting to only highlight the “type” of top-level statements (shape ids will no longer highlight namespace or shape name elements as keywords).

## [1.5.2]
### Added
- Added support for annotating @deprecated shape usage as a warning.
- Added support for annotating @unstable shape usage as a warning.
- Added support for annotating @private shape usage outside the declared namespace as an error.
- Added support for annotating list/set/map shape declarations which are missing required members as an error.
- Added support for annotating map keys which do not target string shapes as errors.
- Added support for annotating traits which target non-trait shapes as errors.
- Added support for omitting imports for prelude shapes which do not conflict with existing shapes in the current namespace.

### Changed
- Removed the bundled Smithy prelude (shape resolution will now resolve to the current version of the prelude bundled in Smithy build tooling)
- Improved the indexing speed of Smithy JSON AST

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