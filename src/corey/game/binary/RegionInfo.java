package corey.game.binary;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class RegionInfo {

	public RegionInfo() {
		Clear();
	}
	
	private static final int COLS = 40;
	private static final int ROWS = 20;
	
	public static final int REGION_NUM = 0;
	public static final int REGION_SPREAD = (int)Math.pow(1+(REGION_NUM*2), 2);
	public static final int HEAT_SPREAD = COLS/2;
	
	private int [] regionCounts = new int[ROWS*COLS];
	private Worker.WorkerStats [] regionStats = new Worker.WorkerStats[ROWS*COLS];
	
	private int Row(Point2D point) {
		double x = point.getX();
		if(x >= 1.0) {
			x = .99;
		}
		if(x <= -1.0) {
			x = -.99;
		}
		return (int)Math.floor((x + 1.0)/2.0 * (double)ROWS);
	}
	
	private int Col(Point2D point) {
		double y = point.getY();
		if(y >= 1.0) {
			y = .99;
		}
		if(y <= -1.0) {
			y = -.99;
		}
		return (int)Math.floor((y + 1.0)/2.0 * (double)COLS);
	}
	
	public int getNumRegions() {
		return COLS*ROWS;
	}
	
	private int RegionId(Point2D point) {
		return Row(point)*COLS + Col(point);
	}
	
	private ArrayList<Integer> RegionIds(Point2D point, int num) {
		 int row = Row(point);
		 int col = Col(point);
		 ArrayList<Integer> idsToUpdate = new ArrayList<Integer>();
		 for(int i = row-num; i<row+num+1; i++){
			 if((i >= 0) && (i < ROWS)) {
				 for(int j = col-num; j<col+num+1; j++) {
					 if((j >=0) && (j < COLS)) {
						 idsToUpdate.add(new Integer(i*COLS+j));
					 }
				 }
			 }
		 }
		 return idsToUpdate;
	}
	
	private void Clear() {
		for(int i=0; i<ROWS*COLS; i++) {
			regionCounts[i] = 0;
			regionStats[i] = new Worker.WorkerStats(true);
		}
	}
	
	int Add(Point2D location, Worker.WorkerStats s) {
		ArrayList<Integer> ids = RegionIds(location, REGION_NUM);
		for(int id : ids){
			regionCounts[id]++;
			Worker.WorkerStats currentRegion = regionStats[id];
			currentRegion.power += s.power;
			currentRegion.greenness += s.greenness;
			currentRegion.redness += s.redness;
			currentRegion.blueness += s.blueness;
		}
		return RegionId(location);
	}
	
	void Remove(Point2D location, Worker.WorkerStats s) {
		for(int id : RegionIds(location, REGION_NUM)){
			regionCounts[id]--;
			Worker.WorkerStats currentRegion = regionStats[id];
			currentRegion.power -= s.power;
			currentRegion.greenness -= s.greenness;
			currentRegion.redness -= s.redness;
			currentRegion.blueness -= s.blueness;
		}
	}
	
	int Report(Point2D location, Worker.WorkerStats s, int oldId) {
		int newId = RegionId(location);
		if(oldId == newId){
			return oldId;
		}
		else {
			Add(location, s);
			Remove(location, s);
			return newId;
		}
	}
	
	void AddHeat(Point2D location, double magnitude) {
		//System.out.println("Adding heat");
		for(int spread = 0; spread<HEAT_SPREAD; spread++){
			//System.out.println(spread);
			ArrayList<Integer> regionIds = RegionIds(location, spread);
			//System.out.println();
			for(int id : regionIds) {
				//System.out.print(id);
				//System.out.print(" ");
				regionStats[id].heat_tol += magnitude/(double)HEAT_SPREAD;
				//System.out.print(id);
				//System.out.print(" ");
				//System.out.println(regionStats[id].heat_tol);
			}
		}
	}
	
	void RemoveHeat(Point2D location, double magnitude) {
		AddHeat(location, -magnitude);
	}
	
	int GetCount(int regionId) {
		return regionCounts[regionId];
	}
	
	Worker.WorkerStats GetStats(int regionId) {
		return regionStats[regionId];
	}
}
