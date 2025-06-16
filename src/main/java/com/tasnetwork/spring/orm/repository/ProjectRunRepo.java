package com.tasnetwork.spring.orm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.ProjectRun;

@Repository
public interface ProjectRunRepo extends JpaRepository<ProjectRun, Long> {
	
	public List<ProjectRun> findByTestRunId(int testrunId);
	public List<ProjectRun> deleteByTestRunId(int testrunId);
	
	public ProjectRun findByDeploymentIdAndExecutionStatus(String deploymentId,String executionStatus);
	public ProjectRun findTopByProjectNameOrderByDeploymentIdDesc(String projectName);
	public ProjectRun findTopByProjectNameAndExecutionStatusOrderByDeploymentIdDesc(String projectName, String testStatus);
	
}
