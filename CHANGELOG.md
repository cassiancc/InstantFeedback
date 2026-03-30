# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [3.1.1+1.20.1-VanillaBackport] - 2026-03-30
#### Changed
- Critical fix: Resolved Mixin mapping errors that were causing a crash on startup

### [3.1.0+1.20.1-VanillaBackport] - 2026-03-25
- This is a backport of InstantFeedback version 3.1.0+1.21.11 to Minecraft 1.20.1
- Dependencies:
  - Vanilla Backport 1.20.1 1.1.5.4
  - Platform 1.20.1 1.2.11.5
#### Added
- Added Potted Pale Bush
- Added Potted Tall Pale Bush

#### Changed
- Changed Pale Garden ambient particles
  - They now spawn much more rarely
  - They have a fade-out
  - They disappear if a player is too close or if the light level is too high
- Retextured the Pale Pumpkin and Carved Pale Pumpkin
  - Emissive textures use Vanilla Backport's emissive rendering system
- Adjusted Pale Garden fog for 1.20.1 compatibility
- Adjusted fallen pale oak generation chance as they were too common
- Pale Bushes and Tall Pale Bushes now have an XZ offset
- Pale Bushes, Tall Pale Bushes, Pale Roses, Pale Pumpkins, and Carved Pale Pumpkins can be composted
- Pale Roses and Potted Pale Roses now emit a light level of 3
- Pale Roses can be crafted into White Dye
- Suspicious stew made from the Pale Rose has had its effect duration changed to 15 seconds
- When bone mealed by a player or dispenser, grass blocks in the Pale Garden now have a chance to grow the following in addition to grass and eyeblossoms:
  - Pale Bush -- 20% chance
  - Pale Rose -- 20% chance
  - Pale Pumpkin -- 1% chance
  - Carved Pale Pumpkin -- 0.5% chance
- Pale Bushes can now be bone mealed into Tall Pale Bushes
- Pale Bushes and Tall Pale Bushes can now only be picked up with shears or a tool enchanted with Silk Touch
- Replaced Pale Garden Leaf Piles with leaf litter
- Pale Carved Pumpkins can now be dispensed to build Snow Golems
- Snow Golems wearing a Pale Carved Pumpkin will throw flaming snowballs
  - Flaming snowballs deal 1 tick of fire damage to the entity they hit
- Pale Carved Pumpkins can be sheared off of Snow Golems
  - Once the pumpkin is sheared off, the Snow Golem loses its ability to throw flaming snowballs
- Changed the blocks that Pale Bushes and Tall Pale Bushes can be placed on
- Creaking damage buff has been scaled based on game difficulty:
  - Easy -- 5hp
  - Normal -- 10hp
  - Hard -- 20hp
- Dark Frogs can now also be spawned in the Deep Dark
- Reordered creative inventory to be able to anchor Instant Feedback items to vanilla items

#### Fixed
- Fixed music continuing to play in the Pale Garden (VanillaBackport bug)
- Fixed egg variants not being thrown from dispensers (VanillaBackport bug)
- Reordered worldgen steps to prevent inappropriate replacement of logs by vegetation

#### Removed
- Removed falling spruce needles, as VanillaBackport adds their own identical spruce needles

## [3.1.0] - 2026-01-12
### Added
- Config to toggle specific features

## [3.0.1] - 2025-11-22
### Fixed
- Spruce falling leave particle color

## [3.0.0] - 2025-10-23
### Added
- Happy Ghasts are now affected by speed potions *(Added flying speed attribute to speed potions)*
- Bundles can be undyed in cauldrons
- Friends 'Till The End advancement
- Happy Ghast calling mechanic *(Goat horns can be used to make them fly towards you)*

## [2.1.0] - 2025-05-29
### Changed
- Improved pale garden fog
- Updated to 1.21.6-pre1

### Fixed
- Potted tall flower translations

## [2.0.0] - 2025-04-11
### Added
- Potted cactus flower
- Potted tall flowers (Rose Bush, Peony, Lilac, Sunflower & Pitcher Plant)
- Falling needle spruce particles
- Muddy pigs in swamps

### Fixed
- Potted pale rose translation

## [1.5.3] - 2024-12-25
### Added
- Potted pale rose

## [1.5.2] - 2024-12-18
### Added
- Brazilian Portuguese translation
- German translation

### Fixed
- East / west direction for fallen trees

## [1.5.1] - 2024-12-13
### Added
- Suspicious stew recipe

### Fixed
- Froglight blockstate (@cassiancc)
- Various block/item tags (@cassiancc)
- Added Cerulean Froglights to Functional Blocks (@cassiancc)

## [1.5.0] - 2024-12-13
### Added
- Dark Frogs (@cassiancc)
- Cerulean Froglights (@cassiancc)

## [1.4.0] - 2024-12-12
### Added
- Contact information
- Carved pale pumpkin snow golem

### Changed
- Improve map colors

### Fixed
- Enderman holdable block tag
- Block mineable tools
- Replaceable blocks

## [1.3.0] - 2024-12-12
### Added
- Items to vanilla item groups (@cassiancc)

### Fixed
- Item models

## [1.2.1] - 2024-12-04
### Fixed
- Ambient particles spawning during daytime
- Update item model format to 1.21.4

## [1.2.0] - 2024-10-31
### Removed
- Creaking closing their eyes when looked at

## [1.1.1] - 2024-10-07
### Fixed
- Crash during daytime

## [1.1.0] - 2024-10-07
### Added
- Block loot tables
- Fallen trees

### Changed
- Improved ambient particles

### Fixed
- Mod icon

## [1.0.0] - 2024-10-07
Initial release