package com.tasnetwork.spring.orm.service;

import java.time.LocalDate;
import java.time.LocalDateTime; // Import LocalDateTime
import java.util.List;
import java.util.ArrayList; // Import ArrayList
import java.util.stream.Collectors; // Import Collectors

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasnetwork.calibration.energymeter.project.ReportGeneratorController.SearchQuery;
import com.tasnetwork.spring.orm.model.ProjectRun;
import com.tasnetwork.spring.orm.model.Result;
import com.tasnetwork.spring.orm.repository.ResultRepo;

@Service
public class ResultService {

	@Autowired
	private ResultRepo resultRepo;

	@Transactional
	public Long saveToDb (Result result) {
		Result resultData = resultRepo.save(result);
		return resultData.getId();
	}

	public Result findByProjectRunAndFanSerialNumberAndTestPointName(ProjectRun projectRun, String fanSerialNumber,
			String testPointName) {
		return resultRepo.findByProjectRunAndFanSerialNumberAndTestPointName(projectRun, fanSerialNumber, testPointName);
	}
	
	public Result findByFanSerialNumberAndTestPointName(String fanSerialNumber, String testPointName) {
		return resultRepo.findByFanSerialNumberAndTestPointName(fanSerialNumber, testPointName);
	}

	public Result findByFanSerialNumberAndTestPointNameAndProjectRunAndTestStatus(
			String fanSerialNumber,
			String testPointName, 
			ProjectRun currentProjectRun, 
			String status) {
		return resultRepo.findByFanSerialNumberAndTestPointNameAndProjectRunAndTestStatus(fanSerialNumber, testPointName, currentProjectRun, status);
	}

	public List<Result> findByFanSerialNumber(String fanSerialNumber) {
		return resultRepo.findByFanSerialNumber(fanSerialNumber);
	}

	public List<Result> findall() {
		return resultRepo.findAll();
	}

	/**
	 * Finds Results based on a list of serial number search queries and a date range.
	 * This method translates the SearchQuery objects into concrete serial numbers
	 * and converts LocalDate to LocalDateTime for database filtering.
	 *
	 * @param serialSearchQueries A list of SearchQuery objects representing exact serial numbers or ranges.
	 * If this list is empty, no serial number filtering is applied.
	 * @param fromDate            The start date for filtering (inclusive). Can be null.
	 * @param toDate              The end date for filtering (inclusive). Can be null.
	 * @return A list of Results matching the combined criteria.
	 */
	public List<Result> findBySerialAndDateRange(List<SearchQuery> serialSearchQueries, LocalDate fromDate,
			LocalDate toDate) {
		
		// 1. Prepare serial numbers for the repository query
		List<String> exactSerialNumbersForDbQuery = new ArrayList<>();
        if (serialSearchQueries != null && !serialSearchQueries.isEmpty()) {
            for (SearchQuery query : serialSearchQueries) {
                if (query.isRange()) {
                    int paddingLength = query.getNumericPartLengthForRange();
                    for (int i = query.getStartNumeric(); i <= query.getEndNumeric(); i++) {
                        exactSerialNumbersForDbQuery.add(String.format("%s%0" + paddingLength + "d", query.getPrefix(), i));
                    }
                } else {
                    exactSerialNumbersForDbQuery.add(query.getExactSerialNumber());
                }
            }
        }

        // If no serial numbers are specified (list is empty), pass null or empty list to the repository
        // The @Query annotation in ResultRepo handles `null` or `EMPTY` list for serialNumbers.
        
        // 2. Convert LocalDate to LocalDateTime for database comparison
        LocalDateTime fromDateTime = (fromDate != null) ? fromDate.atStartOfDay() : null;
        // To include the entire 'toDate', set it to the very end of that day.
        LocalDateTime toDateTime = (toDate != null) ? toDate.atTime(23, 59, 59, 999999999) : null;
        
        // 3. Call the new repository method
		return resultRepo.findBySerialNumbersAndDateTimeRange(exactSerialNumbersForDbQuery, fromDateTime, toDateTime);
	}
}