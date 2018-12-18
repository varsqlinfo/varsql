package com.varsql.app.common.beans;

/**
 * 
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: VtconnectionRVO.java
* @DESC		: 커넥션 정보 읽기용.
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 2. 19. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class VtconnectionRVO{

	private String VCONNID;

	private String VNAME;

	private String VDBSCHEMA;

	private String VURL;

	private String VDRIVER;

	private String VTYPE;

	private String VQUERY;

	private String VID;

	private String VPW;

	private int MAX_ACTIVE;

	private int MIN_IDLE;

	private int TIMEOUT;

	private int EXPORTCOUNT;

	private String VCONNOPT;

	private String VPOOLOPT;

	private int VDBVERSION;

	private String REG_DT;

	private String REG_ID;

	private String UPD_DT;

	private String UPD_ID;

	private char USE_YN;

	private char DEL_YN;

	private char BASETABLE_YN;

	private char LAZYLOAD_YN;

	public String getVCONNID(){
		return this.VCONNID;
	}
	public void setVCONNID(String VCONNID){
		this.VCONNID=VCONNID;
	}
	public String getVNAME(){
		return this.VNAME;
	}
	public void setVNAME(String VNAME){
		this.VNAME=VNAME;
	}
	public String getVDBSCHEMA(){
		return this.VDBSCHEMA;
	}
	public void setVDBSCHEMA(String VDBSCHEMA){
		this.VDBSCHEMA=VDBSCHEMA;
	}
	public String getVURL(){
		return this.VURL;
	}
	public void setVURL(String VURL){
		this.VURL=VURL;
	}
	public String getVDRIVER(){
		return this.VDRIVER;
	}
	public void setVDRIVER(String VDRIVER){
		this.VDRIVER=VDRIVER;
	}
	public String getVTYPE(){
		return this.VTYPE;
	}
	public void setVTYPE(String VTYPE){
		this.VTYPE=VTYPE;
	}
	public String getVQUERY(){
		return this.VQUERY;
	}
	public void setVQUERY(String VQUERY){
		this.VQUERY=VQUERY;
	}
	public String getVID(){
		return this.VID;
	}
	public void setVID(String VID){
		this.VID=VID;
	}
	public String getVPW(){
		return this.VPW;
	}
	public void setVPW(String VPW){
		this.VPW=VPW;
	}
	public int getMAX_ACTIVE(){
		return this.MAX_ACTIVE;
	}
	public void setMAX_ACTIVE(int MAX_ACTIVE){
		this.MAX_ACTIVE=MAX_ACTIVE;
	}
	public int getMIN_IDLE(){
		return this.MIN_IDLE;
	}
	public void setMIN_IDLE(int MIN_IDLE){
		this.MIN_IDLE=MIN_IDLE;
	}
	public int getTIMEOUT(){
		return this.TIMEOUT;
	}
	public void setTIMEOUT(int TIMEOUT){
		this.TIMEOUT=TIMEOUT;
	}
	public int getEXPORTCOUNT(){
		return this.EXPORTCOUNT;
	}
	public void setEXPORTCOUNT(int EXPORTCOUNT){
		this.EXPORTCOUNT=EXPORTCOUNT;
	}
	public String getVCONNOPT(){
		return this.VCONNOPT;
	}
	public void setVCONNOPT(String VCONNOPT){
		this.VCONNOPT=VCONNOPT;
	}
	public String getVPOOLOPT(){
		return this.VPOOLOPT;
	}
	public void setVPOOLOPT(String VPOOLOPT){
		this.VPOOLOPT=VPOOLOPT;
	}
	public int getVDBVERSION(){
		return this.VDBVERSION;
	}
	public void setVDBVERSION(int VDBVERSION){
		this.VDBVERSION=VDBVERSION;
	}
	public String getREG_DT(){
		return this.REG_DT;
	}
	public void setREG_DT(String REG_DT){
		this.REG_DT=REG_DT;
	}
	public String getREG_ID(){
		return this.REG_ID;
	}
	public void setREG_ID(String REG_ID){
		this.REG_ID=REG_ID;
	}
	public String getUPD_DT(){
		return this.UPD_DT;
	}
	public void setUPD_DT(String UPD_DT){
		this.UPD_DT=UPD_DT;
	}
	public String getUPD_ID(){
		return this.UPD_ID;
	}
	public void setUPD_ID(String UPD_ID){
		this.UPD_ID=UPD_ID;
	}
	public char getUSE_YN(){
		return this.USE_YN;
	}
	public void setUSE_YN(char USE_YN){
		this.USE_YN=USE_YN;
	}
	public char getDEL_YN(){
		return this.DEL_YN;
	}
	public void setDEL_YN(char DEL_YN){
		this.DEL_YN=DEL_YN;
	}
	public char getBASETABLE_YN(){
		return this.BASETABLE_YN;
	}
	public void setBASETABLE_YN(char BASETABLE_YN){
		this.BASETABLE_YN=BASETABLE_YN;
	}
	public char getLAZYLOAD_YN(){
		return this.LAZYLOAD_YN;
	}
	public void setLAZYLOAD_YN(char LAZYLOAD_YN){
		this.LAZYLOAD_YN=LAZYLOAD_YN;
	}

}

