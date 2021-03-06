package org.usfirst.frc.team25.scouting.client.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.usfirst.frc.team25.scouting.client.models.ScoutEntry;

/** Object model containing individual reports of teams in events and methods to process data
 * 
 * @author sng
 *
 */
public class TeamReport {
	
	transient ArrayList<ScoutEntry> entries;
	
	int teamNum; //transient because it's the key of the HashMap in EventReport
	String teamName;

	double avgPointsPerCycle, avgCycles, sdCycles , reachBaselinePercentage, 
		avgHighGoals, avgLowGoals,sdHighGoals, sdLowGoals, sdPointsPerCycle;
	
	double avgAutoScore, avgTeleOpScore, avgMatchScore, avgAutoKpa, avgTeleOpKpa, avgAutoGears, 
		avgTeleOpGears, avgTotalFuel, avgHoppers;
	
	
	double sdAutoScore, sdTeleOpScore, sdMatchScore, sdAutoKpa, sdTeleOpKpa, 
		sdAutoGears, sdTeleOpGears, sdTotalFuel;
	
	String autoGearPegLoc = "";
	
	
	double takeoffAttemptPercentage, takeoffAttemptSuccessPercentage, takeoffPercentage;// attempt is out of all matches; success is for each attempt
	double pilotPlayPercentage;
	
	ArrayList<String> frequentRobotComment, frequentPilotComment;
	
	double autoGearAttemptSuccessPercent, leftPegPercent, centerPegPercent, rightPegPercent, avgDroppedGears;
	boolean hasPickup, hasIntake, isActive, doNotPick;
	
	double avgTeleOpKpaFuelFocus, avgTeleOpGearsGearFocus, fuelFocusPercent, gearFocusPercent;

	//TODO calculate these values and update EventReport
	transient double autoAbility, teleOpAbility, driveTeamAbility, robotQualities;
	transient double firstPickAbility, secondPickAbility;

	//Instance variables below should not be serialized but may be accessed by EventReports for analysis
	
	transient String frequentRobotCommentStr = "", frequentPilotCommentStr = "";
	transient String allComments;
	
	int totalTakeoffAttempts, totalTakeoffSuccesses, totalPilotPlaying, 
		totalReachBaseline, totalAutoShootsKey;
	ArrayList<Integer> totalHoppers= new ArrayList<>(), totalFuel= new ArrayList<>(), teleOpGears= new ArrayList<>(), 
			 autoScores= new ArrayList<>(), teleOpScores= new ArrayList<>(), matchScores= new ArrayList<>(),
		totalCycles= new ArrayList<>(), totalHighGoals= new ArrayList<>(), totalLowGoals= new ArrayList<>(), autoGears= new ArrayList<>(),
		totalDroppedGears= new ArrayList<>(),  totalTeleOpGearsGearFocus = new ArrayList<>();
	ArrayList<Double> totalPointsPerCycle = new ArrayList<>(), autoKpas= new ArrayList<>(), teleOpKpa= new ArrayList<>(),totalTeleOpKpaFuelFocus = new ArrayList<>();
	
	int totalAutoGearAttempt, totalAutoGearSuccess, totalLeftPegAttempt, totalLeftPegSuccess, totalCenterPegAttempt, totalCenterPegSuccess,
		totalRightPegAttempt, totalRightPegSuccess;
	
	public TeamReport(int teamNum){
		this.teamNum = teamNum;
		entries = new ArrayList<ScoutEntry>(); 
	}
	
	/** Method to fetch the nickname of a team from a file 
	 * 
	 * @param dataLocation location of the TeamNameList file generated by <code>exportTeamList</code>
	 */
	public void autoGetTeamName(File dataLocation){
		String data = FileManager.getFileString(dataLocation);
		String[] values = data.split(",\n");
		
		for(int i = 0; i < values.length; i++){	
			if(values[i].split(",")[0].equals(Integer.toString(teamNum))){
				
				teamName = values[i].split(",")[1];
				return; //Terminates the method
			}	
		}
	}
	
	void calculateTotals(){
		
		totalTakeoffAttempts = totalTakeoffSuccesses = 0;
		totalPilotPlaying = totalReachBaseline = totalAutoShootsKey = 0;
		totalAutoGearSuccess = totalAutoGearAttempt = 0;
		totalLeftPegAttempt = totalLeftPegSuccess = totalCenterPegAttempt = totalCenterPegSuccess = 0;
		totalRightPegSuccess = totalRightPegAttempt = 0;
		
		
		for(ScoutEntry entry : entries){
			if(entry.getTeleOp().isAttemptTakeoff())
				totalTakeoffAttempts++;
			if(entry.getTeleOp().isReadyTakeoff())
				totalTakeoffSuccesses++;
			
			if(entry.getPreMatch().isPilotPlaying())
				totalPilotPlaying++;
			if(entry.auto.isBaselineCrossed())
				totalReachBaseline++;
			/*if(entry.getAuto().isShootsFromKey())
				totalAutoShootsKey++;*/
			
			if(entry.auto.isAttemptGear()){
				totalAutoGearAttempt++;
				if(entry.auto.getGearPeg().equals("Left"))
					totalLeftPegAttempt++;
				if(entry.auto.getGearPeg().equals("Center"))
					totalCenterPegAttempt++;
				if(entry.auto.getGearPeg().equals("Right"))
					totalRightPegAttempt++;
				
			}
			
			if(entry.auto.isSuccessGear()){
				totalAutoGearSuccess++;
				if(entry.auto.getGearPeg().equals("Left"))
					totalLeftPegSuccess++;
				if(entry.auto.getGearPeg().equals("Center"))
					totalCenterPegSuccess++;
				if(entry.auto.getGearPeg().equals("Right"))
					totalRightPegSuccess++;
				
			}
			
			
			totalHoppers.add((entry.auto.isUseHoppers() ? 1 : 0) 
					+ entry.teleOp.getHopppersUsed());
			totalFuel.add(entry.auto.getHighGoals()+entry.auto.getLowGoals()
					+entry.teleOp.getHighGoals()+entry.teleOp.getLowGoals());
			autoGears.add(entry.getAuto().isSuccessGear()? 1 : 0);
			teleOpGears.add(entry.getTeleOp().getGearsDelivered());
			teleOpKpa.add(entry.teleOpKpa);
			autoKpas.add(entry.autoKpa);
			autoScores.add(entry.autoScore);
			teleOpScores.add( entry.teleScore);
			matchScores.add(entry.totalScore);
			totalPointsPerCycle.add(entry.pointsPerCycle);
			totalCycles.add(entry.teleOp.getNumCycles());
			totalDroppedGears.add(entry.teleOp.getGearsDropped());
			
			totalHighGoals.add( entry.getAuto().getHighGoals()+entry.getTeleOp().getHighGoals());
			totalLowGoals.add(entry.getAuto().getLowGoals()+entry.getTeleOp().getLowGoals());
			
			if(entry.postMatch.getFocus().equals("Gears"))
				totalTeleOpGearsGearFocus.add(entry.teleOp.getGearsDelivered());
			if(entry.postMatch.getFocus().equals("Fuel"))
				totalTeleOpKpaFuelFocus.add(entry.teleOpKpa);
		}
			
		
	}
	
	public void calculateStats(){
		
		calculateTotals();
		
		takeoffAttemptPercentage = ((double) totalTakeoffAttempts)/entries.size()*100; //how often they attempt
		if(totalTakeoffAttempts ==0)
			takeoffAttemptSuccessPercentage = 0;
		else takeoffAttemptSuccessPercentage = ((double) totalTakeoffSuccesses)/totalTakeoffAttempts*100; //percentage for all attempts, "consistency"
		
		
		
		takeoffPercentage = ((double) totalTakeoffSuccesses)/entries.size()*100; //percentage for all matches
				
		
		avgHoppers = Statistics.average(Statistics.toDoubleArrayList(totalHoppers));
		
		
		avgTotalFuel = Statistics.average(Statistics.toDoubleArrayList(totalFuel));
		sdTotalFuel = Statistics.popStandardDeviation(Statistics.toDoubleArrayList(totalFuel));
		
		
		avgTeleOpGears = Statistics.average(Statistics.toDoubleArrayList(teleOpGears));
		sdTeleOpGears = Statistics.popStandardDeviation(Statistics.toDoubleArrayList(teleOpGears));
		
		avgDroppedGears = Statistics.average(Statistics.toDoubleArrayList(totalDroppedGears));
		
		
		avgAutoKpa = Statistics.average(autoKpas);
		sdAutoKpa = Statistics.popStandardDeviation(autoKpas);
		
		avgTeleOpKpa = Statistics.average(teleOpKpa);
		sdTeleOpKpa = Statistics.popStandardDeviation(teleOpKpa);
		
		avgAutoGears = Statistics.average(Statistics.toDoubleArrayList(autoGears));
		sdAutoGears = Statistics.popStandardDeviation(Statistics.toDoubleArrayList(autoGears));
		
		
		autoGearAttemptSuccessPercent = totalAutoGearAttempt!=0 ? (double) totalAutoGearSuccess/totalAutoGearAttempt*100 : 0;
		leftPegPercent = totalLeftPegAttempt!= 0 ? (double) totalLeftPegSuccess / totalLeftPegAttempt * 100 : 0;
		rightPegPercent = totalRightPegAttempt!= 0 ? (double) totalRightPegSuccess / totalRightPegAttempt * 100 : 0;
		centerPegPercent = totalCenterPegAttempt!=0 ? (double) totalCenterPegSuccess / totalCenterPegAttempt * 100 : 0;
		
		avgAutoScore = Statistics.average(Statistics.toDoubleArrayList(autoScores));
		sdAutoScore = Statistics.popStandardDeviation(Statistics.toDoubleArrayList(autoScores));
		
		
		avgTeleOpScore = Statistics.average(Statistics.toDoubleArrayList(teleOpScores));
		sdTeleOpScore = Statistics.popStandardDeviation(Statistics.toDoubleArrayList(teleOpScores));
		
		
		avgMatchScore = Statistics.average(Statistics.toDoubleArrayList(matchScores));
		sdMatchScore = Statistics.popStandardDeviation(Statistics.toDoubleArrayList(matchScores));
		
		pilotPlayPercentage = ((double) totalPilotPlaying)/entries.size()*100;
		
		avgPointsPerCycle = Statistics.average(totalPointsPerCycle);
		sdPointsPerCycle = Statistics.popStandardDeviation(totalPointsPerCycle);
		
		avgCycles = Statistics.average(Statistics.toDoubleArrayList(totalCycles));
		sdCycles = Statistics.popStandardDeviation(Statistics.toDoubleArrayList(totalCycles));
				
		reachBaselinePercentage = totalReachBaseline/((double) entries.size())*100;
		
		avgHighGoals = Statistics.average(Statistics.toDoubleArrayList(totalHighGoals));
		sdHighGoals = Statistics.popStandardDeviation(Statistics.toDoubleArrayList(totalHighGoals));
		
		avgLowGoals = Statistics.average(Statistics.toDoubleArrayList(totalLowGoals));
		sdLowGoals = Statistics.popStandardDeviation(Statistics.toDoubleArrayList(totalLowGoals));
		
		avgTeleOpKpaFuelFocus = Statistics.average(totalTeleOpKpaFuelFocus);
		avgTeleOpGearsGearFocus = Statistics.average(Statistics.toDoubleArrayList(totalTeleOpGearsGearFocus));
		
		fuelFocusPercent = (double) totalTeleOpKpaFuelFocus.size() / entries.size() * 100;
		gearFocusPercent = (double) totalTeleOpGearsGearFocus.size() / entries.size() * 100;
		
		/*if(totalAutoShootsKey/((double)entries.size())>=0.50)
			autoShootsKey = true;
		else autoShootsKey = false;*/
		
		HashMap<String, Integer> commentFrequencies = new HashMap<>();
		
		
		for(String key : entries.get(0).getPostMatch().getRobotQuickCommentSelections().keySet()){
			commentFrequencies.put(key, 0);
			for(ScoutEntry entry : entries)
				if(entry.getPostMatch().getRobotQuickCommentSelections().get(key))
					commentFrequencies.put(key, 1+commentFrequencies.get(key));
		}
		
		frequentRobotComment = new ArrayList<>();
		
		for(String key : commentFrequencies.keySet())
			if(commentFrequencies.get(key)>=entries.size()/4.0)
				frequentRobotComment.add(key);
		
		
		doNotPick = frequentRobotComment.contains("Do not pick (explain)");
		isActive = frequentRobotComment.contains("Active gear mech.");
		hasIntake = frequentRobotComment.contains("Fuel intake");
		hasPickup = frequentRobotComment.contains("Gear pickup");
		
		frequentRobotComment.remove("Do not pick (explain)");
		frequentRobotComment.remove("Active gear mech.");
		frequentRobotComment.remove("Fuel intake");
		frequentRobotComment.remove("Gear pickup");
		
		commentFrequencies = new HashMap<>();
		
		for(String key : entries.get(0).getPostMatch().getPilotQuickCommentSelections().keySet()){
			commentFrequencies.put(key, 0);
			for(ScoutEntry entry : entries)
				if(entry.getPreMatch().isPilotPlaying())
					if(entry.getPostMatch().getPilotQuickCommentSelections().get(key))
						commentFrequencies.put(key, 1+commentFrequencies.get(key));
		}
		
		frequentPilotComment = new ArrayList<>();
		
		for(String key : commentFrequencies.keySet())
			if(commentFrequencies.get(key)>=totalPilotPlaying/4.0)
				frequentPilotComment.add(key);
		
		for(String comment : frequentRobotComment)
			frequentRobotCommentStr+=removeCommas(comment)+';';
		for(String comment : frequentPilotComment)
			frequentPilotCommentStr+=removeCommas(comment)+"; ";
		
		computeRankingMetrics();
		
		allComments = "";
		for(ScoutEntry entry : entries){
			if(!entry.getPostMatch().getRobotComment().equals(""))
				allComments+=entry.getPostMatch().getRobotComment() + "; ";
			if(!entry.getPostMatch().getPilotComment().equals(""))
				allComments+=entry.getPostMatch().getPilotComment()+"; ";
			if(!autoGearPegLoc.contains(entry.getAuto().getGearPeg()))
				autoGearPegLoc+=entry.getAuto().getGearPeg()+"; ";
		}
			
				
	}

	String removeCommas(String s){
		String newString = "";
	     for(int i = 0; i < s.length(); i++) {
	            if(s.charAt(i)!=',')
	                newString += s.charAt(i);
	            else newString+="; ";
	     }
	     return newString;
	}	
	
	void computeRankingMetrics(){
		autoAbility = 0;
		teleOpAbility = 0;
		driveTeamAbility = 0;
		robotQualities = 0;
		firstPickAbility = 0;
		secondPickAbility = 0;
	}

	public int getTeamNum(){
		return teamNum;
	}
	
	String removeCommasAndBreaks(String s){
		String newString = "";
	     for(int i = 0; i < s.length(); i++) {
	            if(s.charAt(i)!=','&&s.charAt(i)!='\n')
	                newString += s.charAt(i);
	            else newString+="; ";
	     }
	     return newString;
	}
	

	public void addEntry(ScoutEntry entry){
		entry.getPostMatch().setRobotComment(removeCommasAndBreaks(entry.getPostMatch().getRobotComment()));
		
		entry.getPostMatch().setPilotComment(removeCommasAndBreaks(entry.getPostMatch().getPilotComment()));
		entries.add(entry);
	}
	

}
