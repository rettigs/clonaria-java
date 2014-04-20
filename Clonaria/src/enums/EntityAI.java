package enums;

/*
 * Note:
 * Peaceful entities and entities who don't yet have targets are simply given random targets every so often.
 * Peaceful entities have no attack damage or attack animation.
 */
public enum EntityAI{
				//	Movement Pattern											Examples
				//	--------------------------------------------------------------------
NONE,			//	N/A															Players, items, projectiles, liquids
WALK,			//	Walk/jump toward target until within attack range			Zombies, skeletons, crabs
HOP,			//	Hop toward target until within attack range					Slimes, bunnies
FLY,			//	Fly/swim toward target until within attack range			Demon eyes, wraiths, birds, fish
HOVER,			//	Hover near target, switch to FLY if out of range			Corruptors
TELEPORT		//	Teleport to near target, switch to WALK if out of range		Dark casters, fire imps, chaos elementals (non-walking entities simply have infinite attack range)
}