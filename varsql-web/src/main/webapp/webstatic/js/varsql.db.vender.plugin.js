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
		var $$objectType = 'package';
		
		try{
			var itemArr = resData.items;
			
			var gridObj = $.pubGrid(_self.options.objectTypeTabContentEleId+'>#package',{
				asideOptions :{
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
		    			
		    			_self._dbObjectMetadataList($.extend({},_self.options.param,{objectType:$$objectType,'objectName':item.name}), '_'+$$objectType+'TabCtrl');
					}
					,contextMenu :{
						callback: function(key,sObj) {
							var ele = this.element, sItem = this.gridItem;
							var tmpName = sItem.name;
							
							if(key=='refresh'){
								_self._removeMetaCache($$objectType,tmpName);
								ele.attr('refresh','Y');
								ele.trigger('click.pubgridrow');
								return ; 
							}
							
							
							if(key =='copy'){
								gridObj.copyData();
								return ; 
							}
						},
						items: [
							{key : "copy" , "name": "Copy"}
						]
					}
				}
			});
		}catch(e){
			VARSQL.log.info(e);
		}
	}
	//package tab control
	,_packageTabCtrl : function (metaTabId, param , refreshFlag){
		var _self =this; 
		var tabObj = $.pubTab(metaTabId);
		var $$objectType = 'package';
		
		if(tabObj){
			tabObj.itemClick();
			return ;
		}
		
		$.pubTab(metaTabId,{
			items : [
				{name: "Column", key: "column"}
				,{name: "DDL", key: "ddl"}
			]
			,height:20
			,overItemViewMode :'drop'
			,click : function (item){
				var tabEle= $(this)
					,objectName = _self.selectMetadata[$$objectType];
				
				var itemKey = item.key;
				
				var sEle = $(_self._getMetadataObjectEleId($$objectType)+' [data-meta-tab="'+itemKey+'"]');
		
				if(!sEle.hasClass('on')){
					$(_self._getMetadataObjectEleId($$objectType)+' .on[data-meta-tab]').removeClass('on');
					sEle.addClass('on');
				}
				
				var cacheData = _self._getMetaCache($$objectType, objectName, itemKey);
				
				if('column' == itemKey){
					if(cacheData){
						_self._packageColumn(cacheData, param, itemKey, false);
						return ; 
					}else{
						
						param.objectName = objectName; 
						param.cacheKey = itemKey;
						
						_self._getMetadataInfo(param, function (resData, param){
							_self._packageColumn(resData, param, itemKey, true);
						})
					}
				}else if('ddl' == itemKey){
					if(cacheData){
						_self.metadataDDLView($$objectType,itemKey, cacheData);
						return ; 
					}else{
						_self._createDDL({
							objectType : $$objectType
							,objName :  objectName
						}, function (data){
							_self.metadataDDLView($$objectType,itemKey, data);
						});
					}
				}
			}
		}).itemClick();
	}
	//패키지에 대한 메타 정보 보기 .
	,_packageColumn :function (colData ,reqParam){
		var _self = this;
 		var $$objectType = 'package';
		
		var metaEleInfo = _self._getMetadataElement($$objectType,eleName);
		
		var metaEleId = metaEleInfo.eleId; 
		
		var gridObj = $.pubGrid(metaEleId);
		
		if(metaEleInfo.isCreate ===true){
			if(gridObj) gridObj.destroy();
		}
		
		var items = colData.items;
		
		gridObj = $.pubGrid(metaEleId);
		
		if(gridObj){
			gridObj.setData(items,'reDraw');
			return ;
		}
		
		gridObj = $.pubGrid(metaEleId, {
			headerOptions : {redraw : false}
			,asideOptions :{lineNumber : {enable : true	,width : 30}}
			,tColItem :  [
				{ label: '컬럼명', key: 'name',width:80 },
				{ label: '데이터타입', key: 'typeAndLength' },
				{ label: '널여부', key: 'nullable',width:45},
				{ label: 'Key', key: 'constraints',width:45},
				{ label: '설명', key: 'comment',width:45}
			]
			,tbodyItem :items
			,rowOptions :{
				contextMenu : {
					beforeSelect :function (){
						$(this).trigger('click');
					}
					,callback: function(key,sObj) {
						
						if(key =='copy'){
							gridObj.copyData();
							return ; 
						}
					},
					items: [
						{key : "copy" , "name": "Copy", hotkey :'Ctrl+C'}
					]
				}
			}
		});
	}
	,_packageMetaResize : function (){
		var gridObj = $.pubGrid(this._getMetadataObjectEleId('package') + "column");
		
		if(gridObj){
			gridObj.resizeDraw();
		}
	}
}

VARSQL.vender['ORACLE'] = $oracle;

}(jQuery,VARSQL));