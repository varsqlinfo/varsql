/*
**
*ytkim
*varsql base object js
 */

if (typeof window != "undefined") {
    if (typeof window.VARSQLCont == "undefined") {
        window.VARSQLCont = {};
    }
}else{
	if(!VARSQLCont){
		VARSQLCont = {};
	}
}


;(function(VARSQLCont) {
"use strict";

function set(str) {
	return str.split(",");
}

var _constants = {
	newline :'\n'
	,tab : '\t'
	,querySuffix : ';  '
	,queryParameterPrefix:'#{'
	,queryParameterSuffix:'}'
};

var _dto = {
	'-7':{name : 'BIT',isNum : false ,val : '', javaType:'char'}
	,'-6':{name : 'TINYINT',isNum : true ,val : 0, javaType:'int'}
	,'5':{name : 'SMALLINT',isNum : true ,val : 0, javaType:'int'}
	,'4':{name : 'INTEGER',isNum : true ,val : 0, javaType:'int'}
	,'-5':{name : 'BIGINT',isNum : true ,val : 0, javaType:'long'}
	,'6':{name : 'FLOAT',isNum : true ,val : 0.0, javaType:'float'}
	,'7':{name : 'REAL',isNum : true ,val : 0, javaType:'Integer'}
	,'8':{name : 'DOUBLE',isNum : true ,val : 0, javaType:'double'}
	,'2':{name : 'NUMERIC',isNum : true ,val : 0, javaType:'int'}
	,'3':{name : 'DECIMAL',isNum : true ,val : 0, javaType:'int'}
	,'1':{name : 'CHAR',isNum : false ,val : '', javaType:'char'}
	,'12':{name : 'VARCHAR',isNum : false ,val : '', javaType:'String'}
	,'-1':{name : 'LONGVARCHAR',isNum : false ,val : '', javaType:'String'}
	,'91':{name : 'DATE',isNum : false ,val : 'current_date', javaType:'String'}
	,'92':{name : 'TIME',isNum : false ,val : 'current_time', javaType:'String'}
	,'93':{name : 'TIMESTAMP',isNum : false ,val : 'current_timestamp', javaType:'String'}
	,'-2':{name : 'BINARY',isNum : false ,val : '', javaType:'String'}
	,'-3':{name : 'VARBINARY',isNum : false ,val : '', javaType:'String'}
	,'-4':{name : 'LONGVARBINARY',isNum : false ,val : '', javaType:'String'}
	,'0':{name : 'NULL',isNum : false ,val : '', javaType:'String'}
	,'1111':{name : 'NVARCHAR2',isNum : false ,val : '', javaType:'String' , getLen : function (len){
		return parseInt(len /2,10);
	}}
	//,'1111':{name : 'OTHER',isNum : false ,val : '', javaType:'Object'}
	,'2000':{name : 'JAVA_OBJECT',isNum : false ,val : '', javaType:'Object'}
	,'2001':{name : 'DISTINCT',isNum : false ,val : '', javaType:'Object'}
	,'2002':{name : 'STRUCT',isNum : false ,val : '', javaType:'Object'}
	,'2003':{name : 'ARRAY',isNum : false ,val : '', javaType:'Object'}
	,'2004':{name : 'BLOB',isNum : false ,val : '', javaType:'Object'}
	,'2005':{name : 'CLOB',isNum : false ,val : '', javaType:'String'}
	,'2006':{name : 'REF',isNum : false ,val : '', javaType:'Object'}
	,'70':{name : 'DATALINK',isNum : false ,val : '', javaType:'Object'}
	,'16':{name : 'BOOLEAN',isNum : false ,val : '', javaType:'boolean'}
	,'-8':{name : 'ROWID',isNum : false ,val : '', javaType:'Object'}
	,'-15':{name : 'NCHAR',isNum : false ,val : '', javaType:'String'}
	,'-9':{name : 'NVARCHAR',isNum : false ,val : '', javaType:'String'}
	,'-16':{name : 'LONGNVARCHAR',isNum : false ,val : '', javaType:'String'}
	,'2011':{name : 'NCLOB',isNum : false ,val : '', javaType:'String'}
	,'2009':{name : 'SQLXML',isNum : false ,val : '', javaType:'String'}
	,'9999':{name : 'OTHER',isNum : false ,val : '', javaType:'Object'}
};

var DEFAULT_HINTS = [
	'BIT'
	,'TINYINT'
	,'SMALLINT'
	,'INTEGER'
	,'BIGINT'
	,'FLOAT'
	,'REAL'
	,'DOUBLE'
	,'NUMERIC'
	,'DECIMAL'
	,'CHAR'
	,'VARCHAR'
	,'LONGVARCHAR'
	,'DATE'
	,'TIME'
	,'TIMESTAMP'
	,'BINARY'
	,'VARBINARY'
	,'LONGVARBINARY'
	,'BLOB'
	,'CLOB'
	,'CURRENT_TIMESTAMP'
];

var TABLE_COL_KEYS ={
	NAME :'name'
	,TYPE_NAME :'typeName'
	,DATA_TYPE :'dataType'
	,NULLABLE :'nullable'
	,PRIMAY_KEY :'primayKey'
	,CONSTRAINTS :'constraints'
	,COMMENT :'comment'
	,SIZE :'length'
}

var DEFINE_INFO = {
	MARIADB : {
		type :'text/x-mariadb'
	}
	,MSSQL : {
		type :'text/x-mssql'
	}
	,MYSQL : {
		type :'text/x-mssql'
	}
	,ORACLE : { 
		type : 'text/x-plsql'
		,hint : set('VARCHAR2,NVARCHAR2,NCHAR2')
		,dataType :{'91' :{val : 'sysdate'}, '92' :{val : 'sysdate'}}
	}
	,H2 : { 
		type : 'text/x-mariadb'
	}
	,'DEFAULT' : {
		type :  'text/x-sql',hint : [] ,dataType:{}
	}
}
var defaultInfo = DEFINE_INFO['DEFAULT'];

function getDBTypeObj (dbType , field){
	var dbTypeInfo = DEFINE_INFO[dbType] || defaultInfo;
	return dbTypeInfo[field] || defaultInfo.field;
}

var dataType = {};

dataType.getDataTypeInfo = function (dataType){
	var tmpDataType= _dto[dataType]; 
	if(typeof  tmpDataType !=='undefined'){
		return  tmpDataType;
	}else{
		return _dto['9999'] ;
	}
}

VARSQLCont.init  = function (dbType, uiBase){
	var _this =this; 
	
	uiBase.sqlHints = DEFAULT_HINTS.concat(getDBTypeObj(dbType , 'hint'));
	uiBase.mimetype =getDBTypeObj(dbType , 'type');
	
	_dto= VARSQL.util.objectMerge(_dto,getDBTypeObj(dbType , 'dataType'));
	
	_this.dataType = dataType;
	_this.constants = _constants;
	_this.tableColKey = TABLE_COL_KEYS;
}

}(VARSQLCont));