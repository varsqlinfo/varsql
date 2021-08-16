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

VARSQLApi.preferences = {
	/**
	 * @method save
	 * @param prefInfo {Object} - {conuid : String ,prefKey : String ,  prefVal : String}  설정 정보.
	 * @param callback {Object} - callback method
	 * @description 데이타 add
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
	 * @description 데이타 add
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