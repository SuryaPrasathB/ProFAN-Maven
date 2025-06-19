package com.tasnetwork.spring.orm.repository;

import java.time.LocalDateTime; // Use LocalDateTime for consistency with Result model
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tasnetwork.calibration.energymeter.project.ReportGeneratorController.SearchQuery; // Keep this import for the service layer

import com.tasnetwork.spring.orm.model.ProjectRun;
import com.tasnetwork.spring.orm.model.Result;

public interface ResultRepo extends JpaRepository<Result, Long>{

	Result findByProjectRunAndFanSerialNumberAndTestPointName(ProjectRun projectRun, String fanSerialNumber, String testPointName);
	
	Result findByFanSerialNumberAndTestPointName(String fanSerialNumber, String testPointName);

	Result findByFanSerialNumberAndTestPointNameAndProjectRunAndTestStatus(String fanSerialNumber, String testPointName,
			ProjectRun currentProjectRun, String status);

	List<Result> findByFanSerialNumber(String fanSerialNumber);
	
	/**
     * Finds Results based on a list of exact serial numbers and a date range.
     * This method uses a custom JPQL query to handle the dynamic filtering.
     *
     * @param serialNumbers A list of exact serial numbers to match. If this list is empty or null,
     * serial number filtering is not applied.
     * @param fromDateTime  The start LocalDateTime for filtering (inclusive). Can be null.
     * @param toDateTime    The end LocalDateTime for filtering (inclusive). Can be null.
     * @return A list of Results matching the combined criteria.
     */
    @Query("SELECT r FROM Result r WHERE " +
           "(:serialNumbers IS NULL OR :serialNumbers IS EMPTY OR r.fanSerialNumber IN :serialNumbers) AND " +
           "(:fromDateTime IS NULL OR r.dateTime >= :fromDateTime) AND " +
           "(:toDateTime IS NULL OR r.dateTime <= :toDateTime)")
    List<Result> findBySerialNumbersAndDateTimeRange(
            @Param("serialNumbers") List<String> serialNumbers,
            @Param("fromDateTime") LocalDateTime fromDateTime,
            @Param("toDateTime") LocalDateTime toDateTime);
}