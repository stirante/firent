# Firent

A bukkit plugin that adds magic skills to the game as well as some special blocks and mechanics.

### Features

* 13 unique skills with precisely crafted look and feel (list below)
* Double jump
* Increased damage, when attacking from behind
* Sound engine for playing simple melodies
* Mana and cooldown systems

### Skills

*Categories, category names and skill names will almost certainly change in the future.*

All skills are divided into 4 categories:

* Movement - skills, that are based on the caster's movement
  * Backstab - teleports behind chosen player and faces that player
  * Flash - teleports few blocks forward
  * Ninja attack - teleports to nearest player and deals damage. Repeats up to four times.
  * Rewind - rewinds caster's position in time by a few seconds
* Fire - skills, that use fire. Ignited players get extra damage from fire skills
  * Fire breath - creates a fire breath, which deals damage and ignites the players in range
  * Fire pillar - creates a pillar of fire, which deals damage and ignites the players in range
* Bullet - skills, that are based on shooting something
  * Bullet storm - fires 10 arrows quickly in chosen direction. The direction stays the same after starting the skill
  * Life drain - deals damage to nearby players, healing the caster
  * Missile - fires missiles, that automatically target nearby players
* Boost - skills, that empowers the caster
  * Charge - charges forward and stops after clashing with first enemy or after some time. Enemies hit with charge are thrown into the air
  * Ghost - makes caster invisible and boosts it's speed. The first attack will break the effects and the attack will be empowered
  * Stomp - caster jumps high into the air and upon hitting the ground, deals damage and throws into the air nearby enemies
  * Tornado - creates a tornado, that throws enemies into the air

## Prerequisites

This plugin requires at least Java 8. In case of Java 9 and newer, you need to allow illegal reflective access.

## Installing

Just place plugin jar inside plugins folder.

## Disclaimer

This plugin is still in development and may change heavily. 