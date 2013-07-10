package Game;

import org.newdawn.slick.Graphics;

public class Statistics {
	
	private static final Statistics instance = new Statistics();

	// initialise in resetStats() to make sure we reset every counter we need
	private int	laserActivationCounter;
	private int	laserEnergyCounter;
	private int	wholeSpentBolts;
	private int	wholeCollectedBolts;
	private int	tailwhipCounter;
	private int	groundpoundCounter;
	private int	generatorsRepaired;
	private int	generatorsUsed;
	private int	killedPigsCounter;
	private int	tailwhipKills;
	private int	groundPoundKills;
	private int	laserKills;
	private int	ambushKills;
	private int	originalTailwhipKill;
	private int	bounceofTailwhipKill;
	private int timeCounter;
	private int playerDeaths;
	private int suicides;
	

	private Statistics() {}
	
	public static Statistics getInstance() {
		return instance;
	}
	
	public void resetStats() {
		timeCounter 			= 0;
		generatorsRepaired 		= 0;
		generatorsUsed 			= 0;
		groundpoundCounter 		= 0;
		laserActivationCounter 	= 0;
		laserEnergyCounter 		= 0;
		tailwhipCounter 		= 0;
		wholeCollectedBolts 	= 0;
		wholeSpentBolts 		= 0;
		laserKills				= 0;
		ambushKills				= 0;
		groundPoundKills		= 0;
		tailwhipKills			= 0;
		killedPigsCounter 		= 0;
		originalTailwhipKill	= 0;
		bounceofTailwhipKill	= 0;
		playerDeaths			= 0;
		suicides				= 0;
	}

	public void printStats(){
		
		System.out.println(toString());
	}
	
	public void drawStats(Graphics g) {

		g.drawString(toString(), 50, 30);
	}
	
	public String toString() {
		return 
		"Deaths: "					+ playerDeaths				+ "\n"+
		"Time: "					+ getTime()			 		+ "\n"+
		"total Pigs killed " 		+ killedPigsCounter 		+ "\n"+
		"\t with tailwhip: " 		+ tailwhipKills				+ "\n"+
		"\t\t (original): " 		+ originalTailwhipKill	 	+ "\n"+
		"\t\t (bounce off): "		+ bounceofTailwhipKill		+ "\n"+
		"\t with groundpound: " 	+ groundPoundKills 			+ "\n"+
		"\t with laser: " 			+ laserKills		 		+ "\n"+
		"\t ambush kills: " 		+ ambushKills		 		+ "\n"+
		"\t suicides: " 			+ suicides					+ "\n"+
		"Laser activated: "			+ laserActivationCounter 	+ "\n"+
		"Generators repaired: " 	+ generatorsRepaired 		+ "\n"+
		"You used generators: " 	+ generatorsUsed 			+ "\n"+
		"tailwhips used: " 			+ tailwhipCounter			+ "\n"+
		"Groundpounds used: " 		+ groundpoundCounter 		+ "\n"+
		"Enery used: " 				+ laserEnergyCounter 		+ "\n"+
		"Collected bolts: " 		+ wholeCollectedBolts 		+ "\n"+
		"used bolts: " 				+ wholeSpentBolts 			+ "\n"+
		"";
		
	}
	
	public 	float getTime() {
		return ((int) (timeCounter/60f*100))/100f;
	}

	public void update() {
		++timeCounter;
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

	public int getOriginalTailwhipKill() {
		return originalTailwhipKill;
	}

	public void incOriginalTailwhipKill() {
		++originalTailwhipKill;
	}

	public int getBounceofTailwhipKill() {
		return bounceofTailwhipKill;
	}

	public void incBounceofTailwhipKill() {
		++bounceofTailwhipKill;
	}

	public int getPlayerDeaths() {
		return playerDeaths;
	}

	public void incPlayerDeaths() {
		++playerDeaths;
	}

	public int getSuicides() {
		return suicides;
	}

	public void incSuicides() {
		++suicides;
	}
	
}
