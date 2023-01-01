/*
**
*ytkim
*varsql base object js
 */

;(function() {
"use strict";

if (typeof window != "undefined") {
    if (typeof window.VARSQLCont == "undefined") {
        window.VARSQLCont = {};
    }
}else{
	if(!VARSQLCont){
		VARSQLCont = {};
	}
}

function setHints(str) {
	return str.split(",");
}

/**
 * :{name : 'TIMESTAMP'	-- dataType 여부.
 * ,isNum : false	-- 숫자 여부
 * , isDate : true	-- date
 * , val : 'current_timestamp'	-- generate insert, update default value
 * , javaType:'String'	-- java type
 * , isSize: false -> ddl create size check
 * }
 */
var _dto = {
	'-7':{name : 'BIT',isNum : false ,val : '', javaType:'Boolean'}
	,'-6':{name : 'TINYINT',isNum : true ,val : 0, javaType:'int'}
	,'5':{name : 'SMALLINT',isNum : true ,val : 0, javaType:'int'}
	,'4':{name : 'INTEGER',isNum : true ,val : 0, javaType:'int'}
	,'-5':{name : 'BIGINT',isNum : true ,val : 0, javaType:'long'}
	,'6':{name : 'FLOAT',isNum : true ,val : 0.0, javaType:'float'}
	,'7':{name : 'REAL',isNum : true ,val : 0, javaType:'float'}
	,'8':{name : 'DOUBLE',isNum : true ,val : 0, javaType:'double'}
	,'2':{name : 'NUMERIC',isNum : true ,val : 0, javaType:'BigDecimal'}
	,'3':{name : 'DECIMAL',isNum : true ,val : 0, javaType:'BigDecimal'}
	,'1':{name : 'CHAR',isNum : false ,val : '', javaType:'String'}
	,'12':{name : 'VARCHAR',isNum : false ,val : '', javaType:'String'}
	,'-1':{name : 'LONGVARCHAR',isNum : false ,val : '', javaType:'String'}
	,'91':{name : 'DATE',isNum : false, isDate : true, val : 'current_date', javaType:'Date', isSize: false}
	,'92':{name : 'TIME',isNum : false, isDate : true, val : 'current_time', javaType:'Time', isSize: false}
	,'93':{name : 'TIMESTAMP',isNum : false, isDate : true, val : 'current_timestamp', javaType:'Timestamp', isSize: false}
	,'-2':{name : 'BINARY',isNum : false ,val : '', javaType:'String'}
	,'-3':{name : 'VARBINARY',isNum : false ,val : '', javaType:'byte[]'}
	,'-4':{name : 'LONGVARBINARY',isNum : false ,val : '', javaType:'byte[]'}
	,'0':{name : 'NULL',isNum : false ,val : '', javaType:'Object'}
	,'1111':{name : 'NVARCHAR2',isNum : false ,val : '', javaType:'String'}
	,'2000':{name : 'JAVA_OBJECT',isNum : false ,val : '', javaType:'Object', isSize: false}
	,'2001':{name : 'DISTINCT',isNum : false ,val : '', javaType:'Object', isSize: false}
	,'2002':{name : 'STRUCT',isNum : false ,val : '', javaType:'Object'}
	,'2003':{name : 'ARRAY',isNum : false ,val : '', javaType:'Object'}
	,'2004':{name : 'BLOB',isNum : false ,val : '', javaType:'Object'}
	,'2005':{name : 'CLOB',isNum : false ,val : '', javaType:'String'}
	,'2006':{name : 'REF',isNum : false ,val : '', javaType:'Object'}
	,'70':{name : 'DATALINK',isNum : false ,val : '', javaType:'Object', isSize: false}
	,'16':{name : 'BOOLEAN',isNum : false ,val : '', javaType:'boolean', isSize: false}
	,'-8':{name : 'ROWID',isNum : false ,val : '', javaType:'Object', isSize: false}
	,'-15':{name : 'NCHAR',isNum : false ,val : '', javaType:'String'}
	,'-9':{name : 'NVARCHAR',isNum : false ,val : '', javaType:'String'}
	,'-16':{name : 'LONGNVARCHAR',isNum : false ,val : '', javaType:'String'}
	,'2011':{name : 'NCLOB',isNum : false ,val : '', javaType:'String'}
	,'2009':{name : 'SQLXML',isNum : false ,val : '', javaType:'String', isSize: false}
	,'9999':{name : 'OTHER',isNum : false ,val : '', javaType:'Object', isSize: false}

};

_dto['INT'] = VARSQL.util.objectMerge({},_dto['4'],{name:'INT'});
_dto['TEXT'] = VARSQL.util.objectMerge({},_dto['2005'],{name:'TEXT'});
_dto['DATETIME'] =VARSQL.util.objectMerge({},_dto['93'],{name:'DATETIME'});


var DEFINE_INFO = {
	MARIADB : {
		type :'text/x-mariadb'
		,formatType : 'mariadb '
	}
	,SQLSERVER : {
		type :'text/x-mssql'
		,formatType : 'tsql'
		,setDataType :  function (pdto){
			pdto['DATETIME'] =VARSQL.util.objectMerge({},_dto['93'],{name:'DATETIME',val : 'getDate()'});
			return pdto;
		}
		,getDefaultValue : function(columnInfo){
			var val = columnInfo.defaultVal;
			
			return (val||'').replace(/^\(|\)$/g,'');
		}
		,mainObjectServiceHeader :{
			table :[
				{key :'schema', label:'schema', width: 30}
				,{key :'name', label:'Table', width: 100, formatter : function (obj){	// 보여질 값을 처리.
					var item = obj.item;
					return item.name;
				}}
				,{key :'remarks', label:'Desc', width: 100}
			]
			,view :[
				{key :'schema', label:'schema', width: 30}
				,{key :'name', label:'Table', width: 100, formatter : function (obj){	// 보여질 값을 처리.
					var item = obj.item;
					return item.name;
				}}
				,{key :'remarks', label:'Desc', width: 100}
			]
		} 
	}
	,MYSQL : {
		type :'text/x-mysql'
		,formatType : 'mysql'
	}
	,ORACLE : {
		type : 'text/x-plsql'
		,hint : setHints('VARCHAR2,NVARCHAR2,NCHAR2')
		,setDataType :  function (pdto){
			pdto['VARCHAR2'] = VARSQL.util.objectMerge({},pdto['12'],{name:'VARCHAR2'});
			pdto['NVARCHAR2'] = VARSQL.util.objectMerge({},pdto['1111'],{name:'NVARCHAR2'});
			pdto['NCHAR2'] = VARSQL.util.objectMerge({},pdto['1111'],{name:'NVARCHAR2'});
			pdto['NUMBER'] = VARSQL.util.objectMerge({},pdto['4'],{name:'NUMBER'});
			pdto['91'] = VARSQL.util.objectMerge(pdto['91'],{val:'sysdate'});
			pdto['92'] = VARSQL.util.objectMerge(pdto['91'],{val:'sysdate'});
			return pdto;
		}
		,formatType : 'plsql'
	}
	,TIBERO :{
		type : 'text/x-plsql'
		,hint : setHints('VARCHAR2,NVARCHAR2,NCHAR2,NUMBER')
		,formatType : 'plsql'
		,setDataType :  function (pdto){
			pdto['VARCHAR2'] = VARSQL.util.objectMerge({},pdto['12'],{name:'VARCHAR2'});
			pdto['NVARCHAR2'] = VARSQL.util.objectMerge({},pdto['1111'],{name:'NVARCHAR2'});
			pdto['NCHAR2'] = VARSQL.util.objectMerge({},pdto['1111'],{name:'NVARCHAR2'});
			pdto['NUMBER'] = VARSQL.util.objectMerge({},pdto['4'],{name:'NUMBER'});
			pdto['91'] = VARSQL.util.objectMerge(pdto['91'],{val:'sysdate'});
			pdto['92'] = VARSQL.util.objectMerge(pdto['91'],{val:'sysdate'});

			return pdto;
		}
	}
	,H2 : {
		type : 'text/x-mariadb'
		,formatType : 'sql'
	}
	,POSTGRESQL :{
		type :'text/x-mariadb'
		,formatType : 'postgresql'
		,setDataType :  function (pdto){
			pdto['CHARACTER VARYING'] = VARSQL.util.objectMerge({},pdto['12'],{name:'VARCHAR2'});
			pdto['CHARACTER'] = VARSQL.util.objectMerge({},pdto['1']);
			
			pdto['DOUBLE PRECISION'] = VARSQL.util.objectMerge({},pdto['2']);
			pdto['BIGSERIAL'] = VARSQL.util.objectMerge({},pdto['4']);
			pdto['TIMESTAMP WITHOUT TIME ZONE'] = VARSQL.util.objectMerge({},pdto['93']);

			return pdto;
		}
	}
	,DB2  :{
		type :'text/x-mariadb'
		,formatType : 'DB2'
	}
	,'DEFAULT' : {
		type :  'text/x-sql',hint : [] ,dataType:{}
		,formatType : 'sql'
		,mainObjectServiceHeader :{
			table :[
				{key :'name', label:'Table', width:200}
				,{key :'remarks', label:'Desc'}
			]
			,view :[
				{key :'name', label:'Table', width: 150}
				,{key :'remarks', label:'Desc', width: 100}
			]
			,procedure : [
				{key :'name', label:'Procedure',width:200}
				,{key :'status', label:'Status'}
				,{key :'remarks', label:'Desc'}
			]
			,function : [
				{key :'name', label:'Function',width:200}
				,{key :'status', label:'Status'}
				,{key :'remarks', label:'Desc'}
			]
			,index : [
				{key :'name', label:'Index',width:200}
				,{key :'tblName', label:'Table'}
				,{key :'type', label:'Type'}
				,{key :'tableSpace', label:'Tablespace'}
				,{key :'bufferPool', label:'Buffer Pool'}
				,{key :'status', label:'Status'}
			]
			,trigger : [
				{key :'name', label:'Trigger', width:120}
				,{key :'tblName', label:'Table', width:100}
				,{key :'eventType', label:'Type'}
				,{key :'timing', label:'timing'}
				,{key :'status', label:'Status'}
				,{key :'created', label:'CREATED'}
			]
			,sequence : [
				{key :'name', label:'Sequence',width:200}
				,{key :'status', label:'Status'}
				,{key :'created', label:'CREATED'}
			]
		}
		,mainMetaGridHeader : {
			tableColumn : [
				{ label: VARSQL.messageFormat('grid.column.name'), key: 'name',width:80 },
				{ label: VARSQL.messageFormat('grid.data.type'), key: 'typeAndLength' },
				{ label: VARSQL.messageFormat('grid.key'), key: 'constraints',width:45},
				{ label: VARSQL.messageFormat('grid.default.value'), key: 'defaultVal',width:45},
				{ label: VARSQL.messageFormat('grid.nullable'), key: 'nullable',width:45},
				{ label: VARSQL.messageFormat('grid.desc'), key: 'comment',width:45}
			]
			,viewColumn : [
				{ label: VARSQL.messageFormat('grid.column.name'), key: 'name',width:80 },
				{ label: VARSQL.messageFormat('grid.data.type'), key: 'typeName' },
				{ label: VARSQL.messageFormat('grid.desc'), key: 'comment',width:45}
			]
			,procedureColumn : [
				{ label: VARSQL.messageFormat('grid.parameter.name'), key: 'name',width:80 },
				{ label: VARSQL.messageFormat('grid.data.type') , key: 'typeName' },
				{ label: VARSQL.messageFormat('grid.inout.name'), key: 'columnType',width:45},
				{ label: VARSQL.messageFormat('grid.desc'), key: 'comment',width:45}
			]
			,functionColumn : [
				{ label: VARSQL.messageFormat('grid.parameter.name'), key: 'name',width:80 }, // 파라미터
				{ label: VARSQL.messageFormat('grid.data.type') , key: 'typeName' },		// datatype
				{ label: VARSQL.messageFormat('grid.inout.name'), key: 'columnType',width:45},	// in/out
				{ label: VARSQL.messageFormat('grid.desc'), key: 'comment',width:45}	// 설명
			]
			,indexColumn : [
				{ label: VARSQL.messageFormat('grid.column.name'), key: 'name',width:80 },
				{ label: 'POSITION', key: 'no',width:80 },
				{ label: 'ASC OR DESC', key: 'ascOrdesc' }
			]
			,triggerInfo : [
				{ label: 'Name', key: 'name'},
				{ label: 'Value', key: 'val',width:80 }
			]
			,sequenceInfo : [
				{ label: 'Name', key: 'name'},
				{ label: 'Value', key: 'val',width:80 },
			]

		}
	}
}

var G_CURRENT_DTO = _dto;
var G_CURRENT_DBTYPE_INFO = DEFINE_INFO['DEFAULT'];

VARSQLCont = {
	constants : {
		newline :'\n'
		,tab : '\t'
		,querySuffix : ';  '
		,queryParameterPrefix:'#{'
		,queryParameterSuffix:'}'
	}
	,tableColKey : {
		NAME :'name'
		,ALIAS :'alias'
		,TYPE_NAME :'typeName'
		,TYPE_CODE :'typeCode'
		,NULLABLE :'nullable'
		,PRIMAY_KEY :'primayKey'
		,CONSTRAINTS :'constraints'
		,COMMENT :'comment'
		,SIZE :'length'
	}
	// db 비교 키 값.
	,compareColKey : [
		{ label: '컬럼명', key: 'name'},
		{ label: '데이터타입', key: 'typeAndLength'},
		{ label: 'Key', key: 'constraints'},
		{ label: '기본값', key: 'defaultVal'},
		{ label: '널여부', key: 'nullable'},
		{ label: '설명', key: 'comment'}
	]
	,init : function (dbType){
		G_CURRENT_DBTYPE_INFO = DEFINE_INFO[dbType] || DEFINE_INFO['DEFAULT'];

		if(VARSQL.isFunction(G_CURRENT_DBTYPE_INFO.setDataType)){
			G_CURRENT_DBTYPE_INFO.setDataType(G_CURRENT_DTO);
		}

		// name 을 키로 등록.
		for(var key  in G_CURRENT_DTO){
			var item = G_CURRENT_DTO[key];
			G_CURRENT_DTO[item.name] =item;
		}
	}
	,getDataTypeInfo : function (dataType){
		dataType= (dataType+'').toUpperCase();
	
		var tmpDataType= G_CURRENT_DTO[dataType];
	
		if(typeof tmpDataType !=='undefined'){
			return tmpDataType;
		}else{
			return G_CURRENT_DTO['9999'] ;
		}
	}
	// js formatter format type
	,formatType : function (){
		return G_CURRENT_DBTYPE_INFO.formatType;
	}
	,editorMimetype : function (){
		return G_CURRENT_DBTYPE_INFO.type;
	}
	,allDataType : function (){
		var result =[];
		var dupChk ={};
		for(var key in G_CURRENT_DTO){
			var item = G_CURRENT_DTO[key];
	
			if(!dupChk[item.name] && item.name != 'NULL'){
				result.push({
					type : item.name
					,name : item.name
				})
				dupChk[item.name] = true;
			}
		}
	
		return result;
	}
	// 컬럼 기본 값 
	,getDefaultValue : function(columnInfo){
		var reval = columnInfo.defaultVal||'';
		
		if(VARSQL.isFunction(G_CURRENT_DBTYPE_INFO.getDefaultValue)){
			reval = G_CURRENT_DBTYPE_INFO.getDefaultValue(columnInfo);
		}

		if(reval !=''){
			return reval;
		}
		
		var dataType = this.getDataTypeInfo(columnInfo.dataType);
		
		if(dataType.isNum===true){
			return -1;
		}
				
		return "'"+VARSQL.util.toLowerCase(columnInfo.name)+"'";
	}
	// main object service header
	,getMainObjectServiceHeader : function (headerType){
			
		if(G_CURRENT_DBTYPE_INFO.mainObjectServiceHeader && G_CURRENT_DBTYPE_INFO.mainObjectServiceHeader[headerType]){
			return G_CURRENT_DBTYPE_INFO.mainObjectServiceHeader[headerType];
		}

		return DEFINE_INFO['DEFAULT'].mainObjectServiceHeader[headerType];
	}
	// main object service header
	,getMainObjectMetaHeader : function (headerType){
			
		if(G_CURRENT_DBTYPE_INFO.mainMetaGridHeader && G_CURRENT_DBTYPE_INFO.mainMetaGridHeader[headerType]){
			return G_CURRENT_DBTYPE_INFO.mainMetaGridHeader[headerType];
		}

		return DEFINE_INFO['DEFAULT'].mainMetaGridHeader[headerType];
	}
}

}());