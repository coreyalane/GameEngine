package corey.game.binary;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Console;
import java.util.Random;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import corey.game.Color;
import corey.game.DrawHelpers;
import corey.game.GameEngine;
import corey.game.GameObject;
import corey.game.RectangleHelpers;

public class Worker extends GameObject {

	public Worker(String name, GameEngine gameEngine, BinaryGameState gs, Point2D location, WorkerStats s) {
		super(name, gameEngine, location, .004, .002);
		this.gs = gs;
		this.gs.nWorkers += 1;
		this.currentRegion = this.gs.regionInfo.Add(location, s);
		this.s = s;
	}
	
	private static double UPDATE_RATE = 0.03; 
	private static double MOVE_SIZE = .005;
	private static int MAX_WORKERS = 50000;
	public static final double GREEN_PERIOD = 3600.0/7.0;
	public static final double BLUE_PERIOD = 3600.0/13.0;
	public static final double RED_PERIOD = 3600.0/17.0;
	public static final double COLOR_PERIOD = 3600.0/3.0;
	public static final double HEAT_PERIOD = 3600.0/20.0;
	
	private BinaryGameState gs;
	private int currentRegion;
	private double timeSinceLastUpdate = 0.0;

	static class WorkerStats implements Cloneable {
		public WorkerStats(boolean cleared) {
			if(cleared) {
				power = 0;
				fertility = 0;
				fragility = 0;
				mobility = 0;
				redness = 0;
				greenness = 0;
				blueness = 0;
				heat_tol = 0;
			}
		}
		
		public WorkerStats() {
			this(false);
		}
		
		static double MUTATE_STEP = .1;
		static double MUTATE_PROB = MUTATE_STEP;
		static Random rng = new Random();
		
		double power = 0.2;
		double fertility = 0.2;
		double fragility = 0.2;
		double mobility = 0.2;
		double redness = .5;
		double greenness = .5;
		double blueness = .5;
		double heat_tol = .5;
		
		public WorkerStats clone(){  
		    try{  
		        return (WorkerStats) super.clone();  
		    }catch(Exception e){ 
		        return null; 
		    }
		}
		
		public double Energy() {
			return (power + fertility + mobility + Math.abs((heat_tol-.5))*2) / 4.0;
		}
		
		WorkerStats GetMutated() {
			WorkerStats mutated = this.clone();
			mutated.Mutate();
			return mutated;
		}
		
		void Mutate() {
			if (Choose.p(MUTATE_PROB)) {
				power = m(power);
				fertility = m(fertility);
				fragility = m(fragility);
				mobility = m(mobility);
				redness = m(redness);
				greenness = m(greenness);
				blueness = m(blueness);
				heat_tol = m(heat_tol);
			}
		}
		
		double m(double stat) {
			if (Choose.p(MUTATE_PROB)) {
				double mutateStep = rng.nextDouble() * MUTATE_STEP;
				if(Choose.Binary()) {
					mutateStep *= -1;
				}
				double mutateStat = stat + mutateStep;
				if(mutateStat >= .95) {
					mutateStat = .95;
				}
				if(mutateStat <= 0.05) {
					mutateStat = 0.05;
				}
				return mutateStat;
			}
			else {
				return stat;
			}
		}
	}
	
	public WorkerStats s;
	
	@Override
	public void draw() {
		Rectangle2D expanded = RectangleHelpers.expand(getBoundingBox(), s.power*15);
		DrawHelpers.drawBoundingBox(expanded, new Color((float)s.redness, (float)s.greenness, (float)s.blueness));
	}
	
	@Override
	public void updatePreCollision(float secondsSinceLastUpdate) {
		// TODO Auto-generated method stub
		super.updatePreCollision(secondsSinceLastUpdate);
		
		timeSinceLastUpdate += secondsSinceLastUpdate;
		if(timeSinceLastUpdate > UPDATE_RATE) {
			doUpdateCycle();
			timeSinceLastUpdate -= UPDATE_RATE;
		}
	}

	private void doUpdateCycle() {
		boolean setKill = false;
		if(gameEngine != null) {
			if (Choose.p(s.fertility * (1 - (((double)gs.nWorkers)/(double)MAX_WORKERS)))) {
				gameEngine.addGameObject(new Worker("worker", gameEngine, gs, newLocation(location), s.GetMutated()));
			}
			if (Choose.p(s.mobility)) {
				double transX = 0.0;
				double transY = 0.0;
				double weightedMoveSize = MOVE_SIZE * s.power * 3;
				if (Choose.Binary()) {
					transX += weightedMoveSize;
				}
				if (Choose.Binary()) {
					transX -= weightedMoveSize;
				}
				if (Choose.Binary()) {
					transY += weightedMoveSize;
				}
				if (Choose.Binary()) {
					transY -= weightedMoveSize;
				}
				if(((location.getX() + transX) > 1.0) || ((location.getX() - transX ) < -1.0)) {
					transX = -transX;
				}
				if(((location.getY() + transY) > 1.0) || ((location.getY() - transY ) < -1.0)) {
					transY = -transY;
				}
				translate(transX, transY);
				currentRegion = gs.regionInfo.Report(location, s, currentRegion);
				//System.out.println(gs.regionInfo.GetCount(currentRegion));
			}
			setKill = computeDeath();
		}
		if(setKill){
			killMe();
		}
	}
	
	private boolean computeDeath() {
		boolean setKill = true;
		
		double lifeProb = 1 - s.Energy();
		//double lifeProb = 1 - s.fragility;
		//System.out.println(lifeProb);
		
		double timePi = gs.totalTime * 2 * Math.PI;
		double regionPower = gs.regionInfo.GetStats(currentRegion).power;
		double regionCount = (double)gs.regionInfo.GetCount(currentRegion);
		double avgPower = (regionPower/(double)regionCount);
		double avgCountPerRegion = (double)MAX_WORKERS/((double)gs.regionInfo.getNumRegions());
		double cumHeat = -Math.cos((timePi / HEAT_PERIOD));
		//double cumHeat = -1.0;
		for(Environment env : gs.environments) {
			cumHeat += env.heatFactorAtDistance(location); //[-1 to 1]
		}
		double heatExtremity = Math.abs(cumHeat);
		double heatFactor = Math.abs(((cumHeat+1.0)/2.0) - s.heat_tol);
		
		//cumHeat += (s.heat_tol-.5)/2.0;
		cumHeat += (s.heat_tol-.5) * (1 - s.power);
		double probFreezing = 0.0;
		if(cumHeat < 0.0) {
			probFreezing = Math.min(-cumHeat, 1.0);
		}
		lifeProb *= (1-probFreezing);
		
		double probOverheating = 0.0;
		if(cumHeat > 0) {
			probOverheating = Math.min(cumHeat, 1.0);
		}
		lifeProb *= (1 - probOverheating);
		
		double probOvercrowding = Math.min(regionCount / (avgCountPerRegion * 3.0), 1.0) * (1-s.power); 
		lifeProb *= (1 - probOvercrowding);
		
		double relativePower = s.power / avgPower; //[0 to inf]
		double scaledRelativePower = Math.min(relativePower, 1.0)/1.0; //[0 to 1]
		double probStarving = (scaledRelativePower) * s.power;
		//System.out.println(probStarving);
		lifeProb *= (1 - probStarving);
		
		//edge penalty
		lifeProb *= 1 - Math.pow(location.getX(), 10.0);
		lifeProb *= 1 - Math.pow(location.getY(), 10.0);
	
		//System.out.println(lifeProb);
		if(Choose.p(lifeProb)) {
			setKill = false;
		}
		return setKill;
	}

	private boolean computeDeathOld() {
		boolean setKill = false;
		
		double deathProb = s.fragility;
		
		double timePi = gs.totalTime * 2 * Math.PI;
		
		double cumHeat = -Math.cos((timePi / HEAT_PERIOD) + 1.0);
		//System.out.println(cumHeat);
//		for(Environment env : gs.environments) {
//			cumHeat += env.heatFactorAtDistance(location); //[-1 to 1]
//		}
		cumHeat += gs.regionInfo.GetStats(currentRegion).heat_tol;
		//System.out.println(cumHeat);
		double heatExtremity = Math.abs(cumHeat);
		double heatFactor = Math.abs(((cumHeat+1.0)/2.0) - s.heat_tol);
		//heatExtremity = 0;
		//heatFactor = 0;
		deathProb += heatFactor + (1.0*heatExtremity - .05);
		
		double regionPower = gs.regionInfo.GetStats(currentRegion).power;
		int regionCount = gs.regionInfo.GetCount(currentRegion);
		double avgPower = (regionPower/(double)regionCount);
		double avgCountPerRegion = (double)MAX_WORKERS/((double)gs.regionInfo.getNumRegions());
		
		//relative power bonus
		deathProb -= .7 * (avgPower - s.power);
		
		//high avg power penality
		deathProb += avgPower * s.power;
		
		//bonus to predator in crowded region
		deathProb -= (.3 * (gs.regionInfo.GetCount(currentRegion)/avgCountPerRegion) * s.power);
		
		//base overcrowding
		deathProb += (1.0 * (gs.regionInfo.GetCount(currentRegion)/avgCountPerRegion) * avgPower);
		
//		//colors competition
//		double avgGreen = gs.regionInfo.GetStats(currentRegion).greenness / regionCount;
//		double avgBlue = gs.regionInfo.GetStats(currentRegion).blueness / regionCount;
//		double avgRed = gs.regionInfo.GetStats(currentRegion).redness / regionCount;
//		deathProb -= Math.abs(s.greenness - avgBlue);
//		deathProb += Math.abs(s.greenness - avgRed);
//		deathProb -= Math.abs(s.blueness - avgRed);
//		deathProb += Math.abs(s.blueness - avgGreen);
//		deathProb -= Math.abs(s.redness - avgGreen);
//		deathProb += Math.abs(s.redness - avgBlue);
//		
//		//color bonus
//		deathProb -= (s.greenness)*(1 - s.mobility)*1.0;
//		deathProb += (s.greenness)*(heatExtremity)*.6;
//		deathProb -= (s.blueness)*(heatExtremity)*.6;
//		deathProb += (s.blueness)*(s.power)*.6;
//		deathProb -= (s.redness)*(s.power)*3;
//		deathProb += (s.redness)*(1 - s.mobility)*1.0;
		
		//seasons
		double colorSeason = Math.sin(timePi / COLOR_PERIOD);
		double greenSeason = Math.sin(timePi / GREEN_PERIOD);
		double blueSeason = -Math.sin(timePi / BLUE_PERIOD);
		double redSeason = Math.sin(timePi / RED_PERIOD);
		deathProb += s.redness * redSeason * .05 * colorSeason;
		deathProb += s.blueness * blueSeason * .05 * colorSeason;
		deathProb += s.greenness * greenSeason * .05 * colorSeason;
	
		//edge penalty
		deathProb += Math.pow(location.getX(), 10.0);
		deathProb += Math.pow(location.getY(), 10.0);
		
		if(deathProb < .025) {
			deathProb = .025;
		}
		
		if (Choose.p(deathProb)) {
			setKill = true;
		}
		return setKill;
	}
	
	void killMe() {
		gameEngine.removeGameObject(this);
		gameEngine = null;
		gs.nWorkers -= 1;
		gs.regionInfo.Remove(location, s);
		gs = null;
	}

	static Point2D newLocation(Point2D location) {
		double x = location.getX();
		double y = location.getY();
		if(Choose.Binary()){
			x += MOVE_SIZE;
		}
		else {
			x -= MOVE_SIZE;
		}
		if(Choose.Binary()){
			y += MOVE_SIZE;
		}
		else {
			y -= MOVE_SIZE;
		}
		return new Point2D.Double(x, y);
	}
}
