package com.tasnetwork.calibration.energymeter.deployment;


import javafx.beans.property.SimpleStringProperty;
public class liveTableData {

	  private SimpleStringProperty serialNo = new SimpleStringProperty();
	  private SimpleStringProperty testPointName = new SimpleStringProperty();
	  private SimpleStringProperty rack1_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack2_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack3_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack4_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack5_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack6_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack7_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack8_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack9_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack10_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack11_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack12_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack13_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack14_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack15_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack16_Data = new SimpleStringProperty();
/**/	  private SimpleStringProperty rack17_Data = new SimpleStringProperty();/**/
	  private SimpleStringProperty rack18_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack19_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack20_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack21_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack22_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack23_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack24_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack25_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack26_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack27_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack28_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack29_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack30_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack31_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack32_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack33_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack34_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack35_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack36_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack37_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack38_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack39_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack40_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack41_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack42_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack43_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack44_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack45_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack46_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack47_Data = new SimpleStringProperty();
	  private SimpleStringProperty rack48_Data = new SimpleStringProperty();
	  private SimpleStringProperty executionStatus = new SimpleStringProperty();

	  
	  public liveTableData(String sNo,String tpName, String executeStatus,
			  String rack1,String rack2,String rack3,String rack4,String rack5,String rack6, String rack7,String rack8,String rack9,String rack10,
			  String rack11,String rack12, String rack13,String rack14,String rack15,String rack16, String rack17,String rack18,String rack19,String rack20,
			  String rack21,String rack22, String rack23,String rack24,String rack25,String rack26, String rack27,String rack28,String rack29,String rack30,
			  String rack31,String rack32, String rack33,String rack34,String rack35,String rack36, String rack37,String rack38,String rack39,String rack40,
			  String rack41,String rack42, String rack43,String rack44,String rack45,String rack46, String rack47,String rack48)
      {
          this.serialNo = new SimpleStringProperty(sNo);
          this.testPointName = new SimpleStringProperty(tpName);
          this.executionStatus = new SimpleStringProperty(executeStatus);
          this.rack1_Data = new SimpleStringProperty(rack1);
          this.rack2_Data = new SimpleStringProperty(rack2);
          this.rack3_Data = new SimpleStringProperty(rack3);
          this.rack4_Data = new SimpleStringProperty(rack4);
          this.rack5_Data = new SimpleStringProperty(rack5);
          this.rack6_Data = new SimpleStringProperty(rack6);
          this.rack7_Data = new SimpleStringProperty(rack7);
          this.rack8_Data = new SimpleStringProperty(rack8);
          this.rack9_Data = new SimpleStringProperty(rack9);
          this.rack10_Data = new SimpleStringProperty(rack10);
          this.rack11_Data = new SimpleStringProperty(rack11);
          this.rack12_Data = new SimpleStringProperty(rack12);
          this.rack13_Data = new SimpleStringProperty(rack13);

          this.rack14_Data = new SimpleStringProperty(rack14);
          this.rack15_Data = new SimpleStringProperty(rack15);
          this.rack16_Data = new SimpleStringProperty(rack16);
          this.rack17_Data = new SimpleStringProperty(rack17);
          this.rack18_Data = new SimpleStringProperty(rack18);
          this.rack19_Data = new SimpleStringProperty(rack19);
          this.rack20_Data = new SimpleStringProperty(rack20);
          
          this.rack21_Data = new SimpleStringProperty(rack21);
          this.rack22_Data = new SimpleStringProperty(rack22);
          this.rack23_Data = new SimpleStringProperty(rack23);
          this.rack24_Data = new SimpleStringProperty(rack24);
          this.rack25_Data = new SimpleStringProperty(rack25);
          this.rack26_Data = new SimpleStringProperty(rack26);
          this.rack27_Data = new SimpleStringProperty(rack27);
          this.rack28_Data = new SimpleStringProperty(rack28);
          this.rack29_Data = new SimpleStringProperty(rack29);
          this.rack30_Data = new SimpleStringProperty(rack30);
          
          this.rack31_Data = new SimpleStringProperty(rack31);
          this.rack32_Data = new SimpleStringProperty(rack32);
          this.rack33_Data = new SimpleStringProperty(rack33);
          this.rack34_Data = new SimpleStringProperty(rack34);
          this.rack35_Data = new SimpleStringProperty(rack35);
          this.rack36_Data = new SimpleStringProperty(rack36);
          this.rack37_Data = new SimpleStringProperty(rack37);
          this.rack38_Data = new SimpleStringProperty(rack38);
          this.rack39_Data = new SimpleStringProperty(rack39);
          this.rack40_Data = new SimpleStringProperty(rack40);
          
          this.rack41_Data = new SimpleStringProperty(rack41);
          this.rack42_Data = new SimpleStringProperty(rack42);
          this.rack43_Data = new SimpleStringProperty(rack43);
          this.rack44_Data = new SimpleStringProperty(rack44);
          this.rack45_Data = new SimpleStringProperty(rack45);
          this.rack46_Data = new SimpleStringProperty(rack46);
          this.rack47_Data = new SimpleStringProperty(rack47);
          this.rack48_Data = new SimpleStringProperty(rack48);

      }
	  
	  public final Object getSerialNo() {
		  return serialNo.get();
	  }

	  // Define a setter for the property's value
	  public final void setSerialNo(String value) {
		  serialNo.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty serialNoProperty() {
		  return serialNo;
	  }

	  public final String getTestPointName() {
		  return testPointName.get();
	  }

	  // Define a setter for the property's value
	  public final void setTestPointName(String value) {
		  testPointName.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty testPointNameProperty() {
		  return testPointName;
	  }
	  
	  public final String getExecutionStatus() {
		  return executionStatus.get();
	  }

	  // Define a setter for the property's value
	  public final void setExecutionStatus(String value) {
		  executionStatus.set(value);
	  }
	  
	  public SimpleStringProperty getExecutionStatusProperty() {
		  return executionStatus;
	  }
	  
	  public final String getRack1_Data() {
		  return rack1_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack1_Data(String value) {
		  rack1_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack1_DataProperty() {
		  return rack1_Data;
	  }
	  public final String getRack2_Data() {
		  return rack2_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack2_Data(String value) {
		  rack2_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack2_DataProperty() {
		  return rack2_Data;
	  }
	  
	  public final String getRack3_Data() {
		  return rack3_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack3_Data(String value) {
		  rack3_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack3_DataProperty() {
		  return rack3_Data;
	  }
	  
	  public final String getRack4_Data() {
		  return rack4_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack4_Data(String value) {
		  rack4_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack4_DataProperty() {
		  return rack4_Data;
	  }
	  
	  public final String getRack5_Data() {
		  return rack5_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack5_Data(String value) {
		  rack5_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack5_DataProperty() {
		  return rack5_Data;
	  }
	  
	  public final String getRack6_Data() {
		  return rack6_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack6_Data(String value) {
		  rack6_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack6_DataProperty() {
		  return rack6_Data;
	  }
	  
	  public final String getRack7_Data() {
		  return rack7_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack7_Data(String value) {
		  rack7_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack7_DataProperty() {
		  return rack7_Data;
	  }
	  
	  public final String getRack8_Data() {
		  return rack8_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack8_Data(String value) {
		  rack8_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack8_DataProperty() {
		  return rack8_Data;
	  }
	  
	  public final String getRack9_Data() {
		  return rack9_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack9_Data(String value) {
		  rack9_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack9_DataProperty() {
		  return rack9_Data;
	  }
	  
	  public final String getRack10_Data() {
		  return rack10_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack10_Data(String value) {
		  rack10_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack10_DataProperty() {
		  return rack10_Data;
	  }
	  
	  public final String getRack11_Data() {
		  return rack11_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack11_Data(String value) {
		  rack11_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack11_DataProperty() {
		  return rack11_Data;
	  }
	  
	  public final String getRack12_Data() {
		  return rack12_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack12_Data(String value) {
		  rack12_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack12_DataProperty() {
		  return rack12_Data;
	  }
	  
	  public final String getRack13_Data() {
		  return rack13_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack13_Data(String value) {
		  rack13_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack13_DataProperty() {
		  return rack13_Data;
	  }
	  
	  public final String getRack14_Data() {
		  return rack14_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack14_Data(String value) {
		  rack14_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack14_DataProperty() {
		  return rack14_Data;
	  }
	  
	  public final String getRack15_Data() {
		  return rack15_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack15_Data(String value) {
		  rack15_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack15_DataProperty() {
		  return rack15_Data;
	  }
	  
	  public final String getRack16_Data() {
		  return rack16_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack16_Data(String value) {
		  rack16_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack16_DataProperty() {
		  return rack16_Data;
	  }
	  
	  public final String getRack17_Data() {
		  return rack17_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack17_Data(String value) {
		  rack17_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack17_DataProperty() {
		  return rack17_Data;
	  }
	  
	  public final String getRack18_Data() {
		  return rack18_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack18_Data(String value) {
		  rack18_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack18_DataProperty() {
		  return rack18_Data;
	  }
	  
	  public final String getRack19_Data() {
		  return rack19_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack19_Data(String value) {
		  rack19_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack19_DataProperty() {
		  return rack19_Data;
	  }
	  
	  public final String getRack20_Data() {
		  return rack20_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack20_Data(String value) {
		  rack20_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack20_DataProperty() {
		  return rack20_Data;
	  }
	  
	  public final String getRack21_Data() {
		  return rack21_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack21_Data(String value) {
		  rack21_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack21_DataProperty() {
		  return rack21_Data;
	  }
	  
	  public final String getRack22_Data() {
		  return rack22_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack22_Data(String value) {
		  rack22_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack22_DataProperty() {
		  return rack22_Data;
	  }
	  
	  public final String getRack23_Data() {
		  return rack23_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack23_Data(String value) {
		  rack23_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack23_DataProperty() {
		  return rack23_Data;
	  }
	  
	  public final String getRack24_Data() {
		  return rack24_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack24_Data(String value) {
		  rack24_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack24_DataProperty() {
		  return rack24_Data;
	  }
	  
	  public final String getRack25_Data() {
		  return rack25_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack25_Data(String value) {
		  rack25_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack25_DataProperty() {
		  return rack25_Data;
	  }
	  
	  public final String getRack26_Data() {
		  return rack26_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack26_Data(String value) {
		  rack26_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack26_DataProperty() {
		  return rack26_Data;
	  }
	  
	  public final String getRack27_Data() {
		  return rack27_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack27_Data(String value) {
		  rack27_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack27_DataProperty() {
		  return rack27_Data;
	  }
	  
	  public final String getRack28_Data() {
		  return rack28_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack28_Data(String value) {
		  rack28_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack28_DataProperty() {
		  return rack28_Data;
	  }
	  
	  public final String getRack29_Data() {
		  return rack29_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack29_Data(String value) {
		  rack29_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack29_DataProperty() {
		  return rack29_Data;
	  }
	  
	  public final String getRack30_Data() {
		  return rack30_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack30_Data(String value) {
		  rack30_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack30_DataProperty() {
		  return rack30_Data;
	  }
	  
	  public final String getRack31_Data() {
		  return rack31_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack31_Data(String value) {
		  rack31_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack31_DataProperty() {
		  return rack31_Data;
	  }
	  
	  public final String getRack32_Data() {
		  return rack32_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack32_Data(String value) {
		  rack32_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack32_DataProperty() {
		  return rack32_Data;
	  }
	  
	  public final String getRack33_Data() {
		  return rack33_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack33_Data(String value) {
		  rack33_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack33_DataProperty() {
		  return rack33_Data;
	  }
	  
	  public final String getRack34_Data() {
		  return rack34_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack34_Data(String value) {
		  rack34_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack34_DataProperty() {
		  return rack34_Data;
	  }
	  
	  public final String getRack35_Data() {
		  return rack35_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack35_Data(String value) {
		  rack35_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack35_DataProperty() {
		  return rack35_Data;
	  }
	  
	  public final String getRack36_Data() {
		  return rack36_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack36_Data(String value) {
		  rack36_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack36_DataProperty() {
		  return rack36_Data;
	  }
	  
	  public final String getRack37_Data() {
		  return rack37_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack37_Data(String value) {
		  rack37_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack37_DataProperty() {
		  return rack37_Data;
	  }
	  
	  public final String getRack38_Data() {
		  return rack38_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack38_Data(String value) {
		  rack38_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack38_DataProperty() {
		  return rack38_Data;
	  }
	  
	  public final String getRack39_Data() {
		  return rack39_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack39_Data(String value) {
		  rack39_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack39_DataProperty() {
		  return rack39_Data;
	  }
	  
	  public final String getRack40_Data() {
		  return rack40_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack40_Data(String value) {
		  rack40_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack40_DataProperty() {
		  return rack40_Data;
	  }
	  
	  public final String getRack41_Data() {
		  return rack41_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack41_Data(String value) {
		  rack41_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack41_DataProperty() {
		  return rack41_Data;
	  }
	  
	  public final String getRack42_Data() {
		  return rack42_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack42_Data(String value) {
		  rack42_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack42_DataProperty() {
		  return rack42_Data;
	  }
	  
	  public final String getRack43_Data() {
		  return rack43_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack43_Data(String value) {
		  rack43_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack43_DataProperty() {
		  return rack43_Data;
	  }
	  
	  public final String getRack44_Data() {
		  return rack44_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack44_Data(String value) {
		  rack44_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack44_DataProperty() {
		  return rack44_Data;
	  }
	  
	  public final String getRack45_Data() {
		  return rack45_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack45_Data(String value) {
		  rack45_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack45_DataProperty() {
		  return rack45_Data;
	  }
	  
	  public final String getRack46_Data() {
		  return rack46_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack46_Data(String value) {
		  rack46_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack46_DataProperty() {
		  return rack46_Data;
	  }
	  
	  public final String getRack47_Data() {
		  return rack47_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack47_Data(String value) {
		  rack47_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack47_DataProperty() {
		  return rack47_Data;
	  }
	  
	  public final String getRack48_Data() {
		  return rack48_Data.get();
	  }

	  // Define a setter for the property's value
	  public final void setRack48_Data(String value) {
		  rack48_Data.set(value);
	  }

	  // Define a getter for the property itself
	  public SimpleStringProperty rack48_DataProperty() {
		  return rack48_Data;
	  }
	  
	  

}
