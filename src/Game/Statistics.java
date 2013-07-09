package Game;

public class Statistics {
	
	private static final Statistics instance = new Statistics();

	// initialise in resetStats() to make sure we reset every counter we need
	private int							laserActivationCounter;
	private int							laserEnergyCounter;
	private int							wholeSpentBolts;
	private int							wholeCollectedBolts;
	private int							tailwhipCounter;
	private int							groundpoundCounter;
	private int							generatorsRepaired;
	private int							generatorsUsed;
	private int							killedPigsCounter;
	private int							tailwhipKills;
	private int							groundPoundKills;
	private int							laserKills;
	private int							ambushKills;
	

	public static Statistics getInstance() {
		return instance;
	}
	
	public void resetStats() {
		generatorsRepaired 			= 0;
		generatorsUsed 				= 0;
		groundpoundCounter 			= 0;
		laserActivationCounter 		= 0;
		laserEnergyCounter 			= 0;
		tailwhipCounter 			= 0;
		wholeCollectedBolts 		= 0;
		wholeSpentBolts 			= 0;
	}

	public int getLaserActivationCounter() {
		return laserActivationCounter;
	}

	public int getLaserEnergyCounter() {
		return laserEnergyCounter;
	}

	public int getWholeSpentBolts() {
		return wholeSpentBolts;
	}

	public int getWholeCollectedBolts() {
		return wholeCollectedBolts;
	}

	public int getTailwhipCounter() {
		return tailwhipCounter;
	}

	public int getGroundpoundCounter() {
		return groundpoundCounter;
	}

	public int getGeneratorsRepaired() {
		return generatorsRepaired;
	}

	public int getGeneratorsUsed() {
		return generatorsUsed;
	}

	public void incLaserActivationCounter() {
		++laserActivationCounter;
	}

	public void incLaserEnergyCounter() {
		++laserEnergyCounter;
	}

	public void incWholeSpentBolts() {
		++wholeSpentBolts;
	}

	public void incWholeCollectedBolts() {
		++wholeCollectedBolts;
	}

	public void incTailwhipCounter() {
		++tailwhipCounter;
	}

	public void incGroundpoundCounter() {
		++groundpoundCounter;
	}

	public void incGeneratorsRepaired() {
		++generatorsRepaired;
	}

	public void incGeneratorsUsed() {
		++generatorsUsed;
	}

	public int getKilledPigsCounter() {
		return killedPigsCounter;
	}
	public void incKilledPigsCounter() {
		++killedPigsCounter;
	}
	
	public int getTailwhipKills() {
		return tailwhipKills;
	}

	public int getGroundPoundKills() {
		return groundPoundKills;
	}

	public int getLaserKills() {
		return laserKills;
	}

	public void incLaserKills() {
		++laserKills;
	}
	public void incTailwhipKills() {
		++tailwhipKills;
	}

	public void incGroundPoundKills() {
		++groundPoundKills;
	}

	public int getAmbushKills() {
		return ambushKills;
	}

	public void incAmbushKills() {
		++ambushKills;
	}
	
	
}
