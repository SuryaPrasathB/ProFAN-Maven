package com.tasnetwork.calibration.energymeter.deployment;

import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.deployment.MeterParamsController;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.deployment.TextBoxDialog.UserInfoPlatFormRunnable;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ProjectStatusRefresh  implements Initializable {
	static int MAX = 100;
	static int CurrentTP_ProgressBarMax = 60;
	static int REF_STD_REFRESH_MAX = 50;//100;
	static double refStdProgressCounter= 0;
	static int ProjectBarMax = 5;

	Thread ProgressBarTaskThread;
	Thread ProgressBarPlatFormLaterThread;
	static Timer ProgressBarTimerTaskTimer;

	static Thread UI_TableRefreshPlatFormLaterThread;

	Timer UIRefreshTaskTimer;

	ProgressBarTask mProgressBarTask;
	PlatFormRunnable ProgressBarPlatFormRunnable;
	static TestPointProgressBarTimerTask mTestPointProgressBarTimerTask;

	static Thread ExeuteTimerPlatFormLaterThread;
	static ExeuteTimerPlatFormRunnable ExeuteTimerPlatFormRunnable;

	public static void setProjectBarMax(int InputValue){
		ProjectBarMax = InputValue;

	}

	public static int getProjectBarMax(){
		return ProjectBarMax ;

	}

	public static void setCurrentTP_ProgressBarMax(int InputValue){
		CurrentTP_ProgressBarMax = InputValue;

	}

	public static int getCurrentTP_ProgressBarMax(){
		return CurrentTP_ProgressBarMax ;

	}

	public static void setRefStdProgressCounter(double input){
		refStdProgressCounter =input;

	}

	public static void startRefStdProgress(ProgressBar barProgress){
		ApplicationLauncher.logger.info("startRefStdProgress :Entry");

		barProgress.setProgress(-1.0f);

	}

	public static void stopRefStdProgress(ProgressBar barProgress){
		ApplicationLauncher.logger.info("stopRefStdProgress :Entry");

		barProgress.setProgress(0.0f);

	}

	public static void ResetRefStdProgressCounter(){
		refStdProgressCounter =0;

	}





	public static void RefStdProgressBarRefresh(ProgressBar barProgress){
		barProgress.setProgress(refStdProgressCounter/REF_STD_REFRESH_MAX);
		refStdProgressCounter++;
		if(refStdProgressCounter > REF_STD_REFRESH_MAX ){
			barProgress.setProgress(1);
			refStdProgressCounter =1;
		}

	}



	public static void ProjectProgressBarRefresh(ProgressBar barProjectProgress,ProgressIndicator barPinProjectStatus,int UpdateValue){
		final double BarUpdateValue = UpdateValue;
		barProjectProgress.setProgress(BarUpdateValue/getProjectBarMax());
		barPinProjectStatus.setProgress(BarUpdateValue/getProjectBarMax());
		if(BarUpdateValue > getProjectBarMax() ){
			barProjectProgress.setProgress(0);
			barPinProjectStatus.setProgress(0);
		}

	}


	class ProgressBarTask extends Task<Void>{

		@Override
		protected Void call() throws Exception {
			for (int i = 1; i <= MAX; i++) {
				updateProgress(i, MAX);
				Thread.sleep(100);
			}
			return null;
		}

	}



	class PlatFormRunnable implements Runnable{

		ProgressBar bar;

		public PlatFormRunnable(ProgressBar b) {
			bar = b;
		}

		@Override
		public void run() {
			for (int i = 1; i <= MAX; i++) {

				final double update_i = i;


				//Update JavaFX UI with runLater() in UI thread
				Platform.runLater(new Runnable(){

					@Override
					public void run() {
						bar.setProgress(update_i/MAX);
					}
				});

				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
					ApplicationLauncher.logger.error("PlatFormRunnable :Exception:"+ ex.getMessage());
					Logger.getLogger(ProjectStatusRefresh.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

	}

	public static void ResetTestPointProgressBar(ProgressBar bar){
		bar.setProgress(0);

	}


	static class TestPointProgressBarTimerTask extends TimerTask{

		ProgressBar bar;
		double count;
		int timeDisplayCounter;

		public TestPointProgressBarTimerTask(ProgressBar b) {
			bar = b;
			timeDisplayCounter = getCurrentTP_ProgressBarMax();
			count = 0;
		}

		@Override
		public void run() {


			timeDisplayCounter--;
			int sec = (timeDisplayCounter % 60);
			int min = ((timeDisplayCounter / 60)%60);
			int hours = ((timeDisplayCounter/60)/60);
			String PrintTime = String.format("%02d", hours) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec);
			ApplicationLauncher.logger.debug("TestPointProgressBarTimerTask: DisplayTestPointExecutionTimeFormat: " +PrintTime);
			Platform.runLater(() -> {
				ProjectExecutionController.DisplayTestPointExecutionTimeFormat.setValue(PrintTime); 
			});
			bar.setProgress(++count/getCurrentTP_ProgressBarMax());

			if(count >= getCurrentTP_ProgressBarMax() ){
				ProgressBarTimerTaskTimer.cancel();
			}
			if( !ProjectExecutionController.getUI_TableRefreshFlag()){
				bar.setProgress(0);
				ProgressBarTimerTaskTimer.cancel();
			}

		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub


		ApplicationLauncher.logger.info("JavaFX_TimerTask:Init");




	}

	public static void TriggerProgressBarTimerTask(ProgressBar barTC_TimeProgressBar){
		ProgressBarTimerTaskTimer = new Timer();
		mTestPointProgressBarTimerTask = new TestPointProgressBarTimerTask(barTC_TimeProgressBar);
		ProgressBarTimerTaskTimer.scheduleAtFixedRate(mTestPointProgressBarTimerTask, 0, 1000);

	}

	public void TriggerProgressBarPlatFormLater(ProgressBar barTC_TimeProgressBar){


		ProgressBarPlatFormRunnable = new PlatFormRunnable(barTC_TimeProgressBar);
		ProgressBarPlatFormLaterThread = new Thread(ProgressBarPlatFormRunnable);
		ProgressBarPlatFormLaterThread.start();
		ApplicationLauncher.logger.info("TriggerProgressBarPlatFormLater :Exit");

	}

	public void TriggerProgressBarTask(ProgressBar barTC_TimeProgressBar){



		mProgressBarTask = new ProgressBarTask();
		barTC_TimeProgressBar.setProgress(0);
		barTC_TimeProgressBar.progressProperty().bind(mProgressBarTask.progressProperty());
		ProgressBarTaskThread = new Thread(mProgressBarTask);
		ProgressBarTaskThread.start();
		ApplicationLauncher.logger.info("TriggerProgressBarTask :Exit");

	}



	public void TriggerExecuteTimerPlatFormLater(TextField time_display, String time){

		ExeuteTimerPlatFormRunnable = new ExeuteTimerPlatFormRunnable(time_display, time);
		ExeuteTimerPlatFormLaterThread = new Thread(ExeuteTimerPlatFormRunnable);
		ExeuteTimerPlatFormLaterThread.start();
		ApplicationLauncher.logger.info("TriggerExecuteTimerPlatFormLater :Exit");

	}

	class ExeuteTimerPlatFormRunnable implements Runnable{

		TextField txt_time_display;
		String time_disp_value;

		public ExeuteTimerPlatFormRunnable(TextField time_display, String time) {
			txt_time_display = time_display;
			time_disp_value = time;
		}

		@Override
		public void run() {
			ApplicationLauncher.logger.info("ExeuteTimerPlatFormRunnable entry");
			Platform.runLater(new Runnable(){

				@Override
				public void run() {
					setTimeDisplayValue(txt_time_display,time_disp_value);
				}
			});
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error("ExeuteTimerPlatFormRunnable :InterruptedException:"+ ex.getMessage());
				Logger.getLogger(ProjectStatusRefresh.class.getName()).log(Level.SEVERE, null, ex);
			} 

			ApplicationLauncher.logger.info("ExeuteTimerPlatFormRunnable exit");
		}

	}



	public static void setTimeDisplayValue(TextField txt_time_display, String time_disp_value){
		txt_time_display.setText(time_disp_value);
	}



}