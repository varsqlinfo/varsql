/**
 * varsql.vender.mode.js
 * 
 * var sql add service object; 
 */


;(function($, VARSQL) {
"use strict";

VARSQL.vender = {};

var $oracle = {
	_package : function (resData, reqParam){
		var _self = this;
		try{
			var itemArr = resData.items;
			
			var gaidbj = $.pubGrid(_self.options.left_service_menu_contentId+'>#package',{
				height:'auto'
				,autoResize :false
				,headerOptions:{}
				,page :false
				,asideOptions :{
					lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
				}
				,tColItem : [
					{key :'name', label:'Package', width:200, sort:true}
					,{key :'remarks', label:'비고', sort:false}
				]
				,tbodyItem :itemArr
				,rowOptions :{
					click : function (idx, item){
						var sObj = $(this);
						
		    			var refresh = sObj.attr('refresh')=='Y'?true:false; 
		    			sObj.attr('refresh','N');
		    			
		    			$('.table-list-item.active').removeClass('active');
		    			sObj.addClass('active');
		    			
		    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':'package','objectName':item.name}), '_packageMetadata', refresh);
					}
					,contextMenu :{
						callback: function(key,sObj) {
							var ele = this.element, sItem = this.gridItem;
							var gubun='package'
								,tmpName = sItem.name;
							
							if(key=='refresh'){
								_self._removeMetaCache(gubun,tmpName);
								ele.attr('refresh','Y');
								ele.trigger('click.pubgridrow');
								return ; 
							}
							
							
							if(key =='copy'){
								tableObj.copyData();
								return ; 
							}
							
							if(key=='ddl_copy' || key=='ddl_paste'){
								_self._createDDL({
									gubunKey : key
									,gubun : 'table'
									,objName :  tmpName 
									,item : cacheData
								});
								return ;
							}
						},
						items: [
							{key : "copy" , "name": "복사"}
							,{divider:true}
							,{key : "create_ddl_top","name": "DDL 보기" 
								,subMenu:[
									{key : "ddl_copy","name": "복사하기"}
									,{key : "ddl_paste","name": "edit 영역에보기"}
								]
							}
							,{divider:true}
							,{key : "refresh" , "name": "새로고침"}
						]
					}
				}
			});
		}catch(e){
			VARSQL.log.info(e);
		}
	}
	//패키지에 대한 메타 정보 보기 .
	,_packageMetadata :function (colData ,reqParam){
		var _self = this;
		
		try{
			var items = colData.items;
			
			var colArr = [];
			$.each(items , function (i , item){
				colArr.push(item.name);
			});
			
			VARSQLHints.setTableColumns(reqParam.objectName ,colArr);
			
			var gridObj = {
				data:items
				,column : [
					{ label: '컬럼명', key: 'name',width:80 },
					{ label: '데이타타입', key: 'typeAndLength' },
					{ label: '널여부', key: 'nullable',width:45},
					{ label: 'Key', key: 'constraints',width:45},
					{ label: '설명', key: 'comment',width:45}
				]
			};
			
			_self.selectMetadata['table'] = reqParam.objectName;
			_self.setMetadataGrid(gridObj, 'table');
			}catch(e){
			VARSQL.log.info(e);
		}
	}
}

VARSQL.vender['ORACLE'] = $oracle;

}(jQuery,VARSQL));