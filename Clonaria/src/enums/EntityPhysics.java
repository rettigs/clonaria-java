package enums;

public enum EntityPhysics{
	 			//	Collision Action	Gravity		Air Resistance	Examples
	 			//	--------------------------------------------------------
	SOLID,		//	Stop				Yes				Yes			Players, dropped items, zombies
	LIQUID,		//	Stop/turn around	Yes				No			Water, lava, honey, quicksand, wet concrete
	BOUNCY,		//	Turn around			Yes				No			Fireballs
	REFLECTIVE,	//	Turn around			No				No			Demon eyes, ricocheted bullets, energy beams
	FLOATING,	//	N/A	(no collision)	No				No			Flying/digging enemies such as worms, wyverns, etc.
	PHASING		//	N/A	(no collision)	In air only		No			Wraiths (they can fly through blocks, but not through the air)
}