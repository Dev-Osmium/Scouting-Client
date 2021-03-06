package org.usfirst.frc.team25.scouting.client.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.usfirst.frc.team25.scouting.client.models.Autonomous;
import org.usfirst.frc.team25.scouting.client.models.PostMatch;
import org.usfirst.frc.team25.scouting.client.models.PreMatch;
import org.usfirst.frc.team25.scouting.client.models.ScoutEntry;
import org.usfirst.frc.team25.scouting.client.models.TeleOp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/** Object model holding all data for an event
 * 
 * @author sng
 *
 */
public class EventReport {
	
	String removeCommas(String s){
		String newString = "";
	     for(int i = 0; i < s.length(); i++) {
	            if(s.charAt(i)!=',')
	                newString += s.charAt(i);
	            else newString+="; ";
	     }
	     return newString;
	}
	
	public boolean isTeamPlaying(int teamNum){
		for(int i : teamReports.keySet())
			if(teamNum==i)
				return true;
		return false;
	}
	

	
	public String quickTeamReport(int teamNum){
		String formatString = "<html>";
		TeamReport report = teamReports.get(teamNum);
		
		formatString+="<h2>Team " + teamNum;
		if(report.teamName!=null)
			formatString+=" - " + report.teamName;
		formatString+="</h2><h3>Auto</h3>";
		
		formatString+="Cross baseline: "+Statistics.round(report.reachBaselinePercentage,2)+"% ("
				+report.totalReachBaseline+"/"+report.entries.size()+")"
				+ "<br>";
		formatString+="Place gear: "+Statistics.round(report.avgAutoGears*100,2)+"% ("
				+report.totalAutoGearSuccess+"/"+report.entries.size()+")"
				+"<br>";
		formatString+="Avg. kPa: "+ Statistics.round(report.avgAutoKpa, 2)+"<br>";
		formatString+="Gear attempt success: "+ Statistics.round(report.avgAutoKpa, 2)+"% ("
				+ report.totalAutoGearSuccess+"/"+report.totalAutoGearAttempt+")<br>";
		formatString+="Left peg: "+ Statistics.round(report.leftPegPercent, 2)+"% ("
				+ report.totalLeftPegSuccess+"/"+report.totalLeftPegAttempt+")<br>";
		formatString+="Center peg: "+ Statistics.round(report.centerPegPercent, 2)+"% ("
				+ report.totalCenterPegSuccess+"/"+report.totalCenterPegAttempt+")<br>";
		formatString+="Right peg: "+ Statistics.round(report.rightPegPercent, 2)+"% ("
				+ report.totalRightPegSuccess+"/"+report.totalRightPegAttempt+")<br>";
		
		formatString+="<h3>Tele-Op</h3>";
		formatString+="Avg. gears: "+Statistics.round(report.avgTeleOpGears,2)+"<br>";
		formatString+="Gear counts: ";
		for(int i : report.teleOpGears)
			formatString+=i+", ";
		formatString+="<br>";
		formatString+="Avg. kPa: "+Statistics.round(report.avgTeleOpKpa,2)+"<br>";
		formatString+="Avg. dropped gears: "+Statistics.round(report.avgDroppedGears,2)+"<br>";
		formatString+="Gear focus: "+Statistics.round(report.gearFocusPercent,2)+"%<br>";
		formatString+="Avg. gear focus gears: "+Statistics.round(report.avgTeleOpGearsGearFocus,2)+"<br>";
		formatString+="Fuel focus: "+Statistics.round(report.fuelFocusPercent,2)+"%<br>";
		formatString+="Avg. kPa fuel focus: "+Statistics.round(report.avgTeleOpKpaFuelFocus,2)+"<br>";
		formatString+="Takeoff success: " +Statistics.round(report.takeoffPercentage, 2)+"% ("
				+ report.totalTakeoffSuccesses+"/"+report.entries.size()+")<br>";
		formatString+="Takeoff attempt: " +Statistics.round(report.takeoffAttemptPercentage,2)+"% ("
				+report.totalTakeoffAttempts+"/"+report.entries.size()+")<br>";
		formatString+="<h3>Overall</h3>";
		formatString+="Avg. score (modified OPR): " + Statistics.round(report.avgMatchScore,2)+"<br>";
		formatString+="Total gears: "+Statistics.round(report.avgAutoGears+report.avgTeleOpGears,2)+"<br>";
		formatString+="Total kPa: " + Statistics.round(report.avgAutoKpa+report.avgTeleOpKpa,2)+"<br>";
		formatString+="Pilot play: "+Statistics.round(report.pilotPlayPercentage,2)+"%<br>";
		formatString+="Do not pick: "+(report.doNotPick ? "Yes" : "No" )+"<br>";
		formatString+="Gear floor pickup: "+(report.hasPickup ? "Yes" : "No" )+"<br>";
		formatString+="Active gear: "+(report.isActive ? "Yes" : "No" )+"<br>";
		formatString+="Fuel intake: "+(report.hasIntake ? "Yes" : "No" )+"<br>";
		formatString+="Frequent comments: " + report.frequentRobotCommentStr+" "+report.frequentPilotCommentStr;
		formatString+="</html>";
		return formatString;
	}
	
	public String allianceReport(int t1, int t2, int t3){
		String formatString = "<html>";
		TeamReport r1 = teamReports.get(t1), r2=teamReports.get(t2),r3=teamReports.get(t3);
		
		Alliance a = new Alliance(r1, r2, r3);
		a.calculateStats();
		
		formatString+="<h2>"+t1+", "+t2+", "+t3+"</h2><h3>Auto</h3>";
		
		formatString+="1+ BL cross: "
				+Statistics.round(a.atLeastOneBaselinePercent,2)
				+"%<br>";
		formatString+="2+ BL cross: "
				+Statistics.round(a.atLeastTwoBaselinePercent,2)
				+"%<br>";
		formatString+="3 BL cross: "
				+Statistics.round(a.allBaselinePercent,2)
				+"%<br>";
		formatString+="Place gear: "
				+ Statistics.round(a.autoGearPercent, 2)
				+"%<br>";
		formatString+="Avg. kPa: "+ Statistics.round(a.autoKpa, 2)+"<br>";
		
		formatString+="<h3>Tele-Op</h3>";
		formatString+="Avg. kPa: "+Statistics.round(a.teleopKpa,2)+"<br>";
		formatString+="1+ takeoff: "
				+Statistics.round(a.atLeastOneTakeoffPercent,2)
				+"%<br>";
		formatString+="2+ takeoff: "
				+Statistics.round(a.atLeastTwoTakeoffPercent,2)
				+"%<br>";
		formatString+="3 takeoff: "
				+Statistics.round(a.allTakeoffPercent,2)
				+"%<br>";
		formatString+="<h3>Overall</h3>";
		formatString+="Total gears: "+Statistics.round(a.totalGears,2)+"<br>";
		formatString+="Total kPa: " + Statistics.round(a.totalKpa,2)+"<br>";
		formatString+="Avg. score (predicted): " + Statistics.round(a.predictedScore,2)+"<br>";
		formatString+="</html>";
		return formatString;
		
	}
	
	/** Unsorted list of ScoutEntrys TODO create method to sort them
	 * 
	 */
	ArrayList<ScoutEntry> scoutEntries;
	File teamNameList;
	String event;
	HashMap<Integer, TeamReport> teamReports = new HashMap<Integer, TeamReport>(); 
	
	public EventReport(ArrayList<ScoutEntry> entries){
		scoutEntries = entries;
		for(ScoutEntry entry : scoutEntries){
			entry.calculateDerivedStats();
			
			int teamNum = entry.getPreMatch().getTeamNum();
			if(!teamReports.containsKey(teamNum))
				teamReports.put(teamNum, new TeamReport(teamNum));
			
			teamReports.get(teamNum).addEntry(entry);
		}
		event = scoutEntries.get(0).getPreMatch().getCurrentEvent();
		
		
	}
	
	public void processTeamReports(){
		
		for(Integer key : teamReports.keySet()){
		
			TeamReport report = teamReports.get(key);
			if(teamNameList!=null){
				report.autoGetTeamName(teamNameList);
				
			}
			report.calculateStats();
			
			
			teamReports.put(key, report);
			
		}
		
		
	}
	
	
	
	public void setTeamNameList(File list){
		teamNameList = list;
	}
	
	public void generateTeamReportSpreadsheet(File outputDirectory){
		final String COMMA = ",";
		String header = "teamNum,teamName,avgAutoScore,sdAutoScore,avgTeleOpScore,sdTeleOpScore,avgMatchScore,sdMatchScore,reachBaselinePercentage,avgAutoKpa,sdAutoKpa,avgTeleOpKpa,sdTeleOpKpa,avgAutoGears,sdAutoGears,autoGearAttemptSuccessPercent,autoGearPegLoc,leftPegPercent,rightPegPercent,centerPegPercent,totalLeftPegSuccess,totalRightPegSuccess,totalCenterPegSuccess,avgTeleOpGears,sdTeleOpGears,avgDroppedGears,avgHighGoals,sdHighGoals,avgLowGoals,sdLowGoals,avgHoppers,avgPointsPerCycle,sdPointsPerCycle,avgCycles,sdCycles,takeoffPercentage,takeoffAttemptPercentage,takeoffAttemptSuccessPercentage,pilotPlayPercentage,avgTeleOpKpaFuelFocus,avgTeleOpGearsGearFocus,fuelFocusPercent,gearFocusPercent,hasPickup,hasIntake,isActive,doNotPick,frequentRobotCommentStr,frequentPilotCommentStr,allComments,\n";
		
		String fileContents = header;
		for(int key : teamReports.keySet()){
			TeamReport report = teamReports.get(key);
			fileContents += report.teamNum+COMMA+report.teamName+COMMA+report.avgAutoScore+COMMA+report.sdAutoScore+COMMA+report.avgTeleOpScore+COMMA+report.sdTeleOpScore+COMMA+report.avgMatchScore+COMMA+report.sdMatchScore+COMMA+report.reachBaselinePercentage+COMMA+report.avgAutoKpa+COMMA+report.sdAutoKpa+COMMA+report.avgTeleOpKpa+COMMA+report.sdTeleOpKpa+COMMA+report.avgAutoGears+COMMA+report.sdAutoGears+COMMA+report.autoGearAttemptSuccessPercent+COMMA+report.autoGearPegLoc+COMMA+report.leftPegPercent+COMMA+report.rightPegPercent+COMMA+report.centerPegPercent+COMMA+report.totalLeftPegSuccess+COMMA+report.totalRightPegSuccess+COMMA+report.totalCenterPegSuccess+COMMA+report.avgTeleOpGears+COMMA+report.sdTeleOpGears+COMMA+report.avgDroppedGears+COMMA+report.avgHighGoals+COMMA+report.sdHighGoals+COMMA+report.avgLowGoals+COMMA+report.sdLowGoals+COMMA+report.avgHoppers+COMMA+report.avgPointsPerCycle+COMMA+report.sdPointsPerCycle+COMMA+report.avgCycles+COMMA+report.sdCycles+COMMA+report.takeoffPercentage+COMMA+report.takeoffAttemptPercentage+COMMA+report.takeoffAttemptSuccessPercentage+COMMA+report.pilotPlayPercentage+COMMA+report.avgTeleOpKpaFuelFocus+COMMA+report.avgTeleOpGearsGearFocus+COMMA+report.fuelFocusPercent+COMMA+report.gearFocusPercent+COMMA+report.hasPickup+COMMA+report.hasIntake+COMMA+report.isActive+COMMA+report.doNotPick+COMMA+report.frequentRobotCommentStr+COMMA+report.frequentPilotCommentStr+COMMA+report.allComments+COMMA+'\n';
			
		}
				
		
		try {
			FileManager.outputFile(outputDirectory.getAbsolutePath() + "\\TeamReports - " + event , "csv", fileContents);
		} catch (FileNotFoundException e) {
			// 
			e.printStackTrace();
		}
	}
	
	/** Generates summary and team Excel spreadsheets 
	 * 
	 * @param outputDirectory Output directory for generated fields
	 */
	public void generateRawSpreadsheet(File outputDirectory){
		final String COMMA = ",";
		String header = "Scout Name,Match Num,Scouting Pos,Team Num,Pilot Playing,High goals auto, "
				+ "Low goals auto,Attempt auto gear,Gears auto,Auto gear peg,Reached baseline,Hopper used auto,"
				+ "High goals tele,Low goals tele,Gears tele,Gears dropped tele,Gears dropped loc,Hoppers tele,Cycles,Takeoff attempt,"
				+ "Takeoff success,Match focus,Robot comment,Robot quick comment,Pilot comment,Pilot quick comment,";
		ArrayList<String> keys = new ArrayList<>();
		ArrayList<String> pilotKeys = new ArrayList<>();
			

		for(String key : scoutEntries.get(0).getPostMatch().getRobotQuickCommentSelections().keySet()){
			header+=removeCommas(key)+",";
			keys.add(key);
		}
		for(ScoutEntry entry : scoutEntries){
			if(entry.getPreMatch().isPilotPlaying()){
				for(String key : entry.getPostMatch().getPilotQuickCommentSelections().keySet()){
					header+=removeCommas(key)+",";
					pilotKeys.add(key);
				}
				break;
			}
		}
		
		
		String fileContents = header + "\n";
		for(ScoutEntry entry : scoutEntries){
			PreMatch pre = entry.getPreMatch();
			Autonomous auto = entry.getAuto();
			TeleOp tele = entry.getTeleOp();
			PostMatch post = entry.getPostMatch(); 
			
			fileContents+=pre.getScoutName()+COMMA + pre.getMatchNum()+COMMA+pre.getScoutPos()+COMMA+
					pre.getTeamNum()+COMMA+pre.isPilotPlaying()+COMMA;
			fileContents+=auto.getHighGoals()+COMMA+auto.getLowGoals()+COMMA+auto.isAttemptGear()+COMMA+
					(auto.isSuccessGear() ? 1 : 0)+COMMA+auto.getGearPeg()+COMMA+
					auto.isBaselineCrossed()+COMMA+auto.isUseHoppers()+COMMA;
					
			fileContents+=tele.getHighGoals()+COMMA+tele.getLowGoals()+COMMA+tele.getGearsDelivered()+COMMA+
					tele.getGearsDropped()+COMMA+tele.getGearsDroppedLoc()+COMMA+tele.getHopppersUsed()+COMMA+tele.getNumCycles()
					+COMMA+tele.isAttemptTakeoff()+COMMA+tele.isReadyTakeoff()+COMMA;
			fileContents+=post.getFocus()+COMMA+
					post.getRobotComment()+COMMA+post.getRobotQuickCommentStr()+COMMA+post.getPilotComment()+
					COMMA+post.getPilotQuickCommentStr()+COMMA;
			
			for(String key : keys)
				fileContents+=post.getRobotQuickCommentSelections().get(key)+COMMA;
			
			if(pre.isPilotPlaying())
				for(String key : pilotKeys)
					fileContents+=post.getPilotQuickCommentSelections().get(key)+COMMA;
			
			fileContents+='\n';	
		}
				
		
		try {
			FileManager.outputFile(outputDirectory.getAbsolutePath() + "\\Data - All - " + event , "csv", fileContents);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/** Serializes the ArrayList of all ScoutEntrys into a JSON file
	 * @param outputDirectory
	 * @return true if operation is successful, false otherwise
	 */
	public boolean generateCombineJson(File outputDirectory){
		Gson gson = new Gson();
		String jsonString = gson.toJson(scoutEntries);
		try {
			FileManager.outputFile(outputDirectory.getAbsolutePath() + "\\Data - All - " + event , "json", jsonString);
		} catch (FileNotFoundException e) {
			
			return false;
		}
		return true;
	}
	
	/** Serializes the HashMap of all TeamReports
	 * @param outputDirectory
	 */
	public void generateTeamReportJson(File outputDirectory){

		Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
		
		ArrayList<TeamReport> teamReportList = new ArrayList<>();
		
		for(int key : teamReports.keySet())
			teamReportList.add(teamReports.get(key));
		
		String jsonString = gson.toJson(teamReportList);
		try {
			FileManager.outputFile(outputDirectory.getAbsolutePath() + "\\TeamReports - " + event , "json", jsonString);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public TeamReport getTeamReport(int teamNum){
		return teamReports.get(teamNum);
	}

}
