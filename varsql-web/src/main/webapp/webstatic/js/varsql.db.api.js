/*
**
*ytkim
*VARSQL api
 */

;(function(VARSQL) {
"use strict";

if (typeof window != "undefined") {
    if (typeof window.VARSQLApi == "undefined") {
        window.VARSQLApi = {};
    }
}else{
	if(!VARSQLApi){
		VARSQLApi = {};
	}
}

VARSQLApi.sql = {
	/**
	 * @method execute
	 * @param params {Object} - {sql : String}  
	 * @param callback {Object} - callback method
	 * @description 데이터 add
	 */
	execute : function (params , callback){
		params.conuid = $varsqlConfig.conuid;
		
		VARSQL.req.ajax({
		    loadSelector : 'body'
		    ,url:{type:VARSQL.uri.sql, url:'/base/sqlData'}
		    ,data:params
		    ,success:function (resData){
		    	callback(resData)
			}
		});
	}
}

VARSQLApi.preferences = {
	/**
	 * @method save
	 * @param prefInfo {Object} - {conuid : String ,prefKey : String ,  prefVal : String}  설정 정보.
	 * @param callback {Object} - callback method
	 * @description 데이터 add
	 */
	save : function (prefInfo , callback){
		VARSQL.req.ajax({
			url:{type:VARSQL.uri.database, url:'/preferences/save'}
			,data: prefInfo
			,success:function (resData){

				if(VARSQL.isFunction(callback)){
					callback.call(null, resData);
					return ;
				}
			}
		});
	}
}


VARSQLApi.sqlTemplate = {
	/**
	 * @method save
	 * @param prefInfo {Object} - {conuid : String ,prefKey : String ,  prefVal : String}  설정 정보.
	 * @param callback {Object} - callback method
	 * @description 데이터 add
	 */
	load : function (prefInfo , callback){
		VARSQL.req.ajax({
			url:{type:VARSQL.uri.database, url:'/utils/sqlTemplate'}
			,data: prefInfo
			,success:function (resData){

				if(VARSQL.isFunction(callback)){
					callback.call(null, resData);
					return ;
				}
			}
		});
	}
}

}(VARSQL));