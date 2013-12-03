package com.maestro.schoologyapp.api.lunchcounts;

import java.util.*;

import javax.ws.rs.*;

import com.maestro.schoologyapp.api.lunchcounts.entity.*;
import com.sirra.appcore.sql.*;
import com.sirra.appcore.util.*;
import com.sirra.server.json.*;
import com.sirra.server.rest.*;
import com.sirra.server.rest.annotations.*;

public class LunchCounts extends ApiBase {

	
	@GET
	@Parameters({"date"})
	public List<LunchCount> getLunchCounts(String plainDateStr) {
		SqlParams sqlParams = getSqlParams();
		
		sqlParams.setNumItemsToRetrieve(1000);
		sqlParams.addConstraint("lunchcounts.date = '" + plainDateStr + "'");
		
		List<LunchCount> lunchCounts = SqlSearch.search(LunchCount.class, "SELECT ${columns} FROM lunchcounts order by teacherName", sqlParams);
		return lunchCounts;	
	}
	
	@GET
	@BY_ID
	public LunchCount getLunchCount(String lunchCountId) {
		LunchCount lunchCount = get(LunchCount.class, lunchCountId);
		return lunchCount;
	}
	
	@GET
	@PathParameters({"date", "total"})
	@Parameters({"date"})
	public int getTotalLunchCount(String date) {
		Data data = SqlSearch.singleSearch("SELECT sum(count) FROM lunchcounts where date = '" + date + "'", new Columns("total"), new SqlParams());
		
		return data.getInt("total", 0);
	}

	@POST
	@BY_ID
	@Parameters({"lunchCountId", "date", "teacherName", "count", "notes"})
	public void recordLunchCount(String lunchCountId, String plainDateStr, String teacherName, int count, String notes) {
		LunchCount lunchCount = get(LunchCount.class, lunchCountId);
		
		if(lunchCount == null) {
			lunchCount = new LunchCount();
		}
		
		lunchCount.setDate(plainDateStr);
		lunchCount.setTeacherName(teacherName);
		lunchCount.setCount(count);
		lunchCount.setNotes(notes);
		
		save(lunchCount);
	}
	
	
	@DELETE
	@BY_ID
	public void deleteLunchCounts(String lunchCountId) {
		LunchCount lunchCount = getLunchCount(lunchCountId);
		delete(lunchCount);
	}
}