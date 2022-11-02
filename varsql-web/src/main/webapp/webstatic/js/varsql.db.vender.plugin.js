/**
 * varsql.vender.mode.js
 * 
 * var sql add service object; 
 * 확장 object 생성시 
 * serviceObject : _"serviceObjectName"
 * objectMetadata : _"serviceObjectName + meta tab id"
 * 
 * 
 */


;(function($, VARSQL) {
"use strict";

VARSQL.vender = {};

var $oracle = {
	serviceObject : {
		_package : function (resData, reqParam){
			var _self = this;
			
			try{
				var $$objectType = 'package';

				var gridObj = $.pubGrid(_self.objectTypeTab.getTabContentSelector({contentid: $$objectType}),{
					asideOptions :{
						lineNumber : {enable: true, width: 30, styleCss: 'text-align:right;padding-right:3px;'}				
					}
					,tColItem : [
						{key :'name', label:'Package', width:200, sort:true}
						,{key :'remarks', label:'비고', sort:false}
					]
					,tbodyItem: resData.list
					,setting : {
                        enabled : true
                        ,enableSearch : true
                    }
					,bodyOptions :{
						keyNavHandler : function(moveInfo){
	
							if(moveInfo.key == 13){
								return false;
							}else{
								_self.getObjectMetadata({'objectType': $$objectType, 'objectName': moveInfo.item.name, 'objectInfo': moveInfo.item});
							}
						}
					}
					,rowOptions :{
						click : function (rowInfo){
                            var item = rowInfo.item;
    
                            _self.getObjectMetadata({'objectType': $$objectType, 'objectName': item.name, 'objectInfo': item});
                        }
						,contextMenu :{
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
                                {key : "copy" , "name": "Copy"}
                            ]
						}
					}
				});
			}catch(e){
				VARSQL.log.info(e);
			}
		}
	}
	,objectMetadata :{
		//package 대한 메타 정보 보기 .
        _packageColumn :function (colData, callParam, tabKey, reloadFlag){
            var _self = this;
    
             var $$objectType = 'package';
    
             var metaEleId = _self.selectMetadata[$$objectType].metaTab.getTabContentSelector({'tabid': tabKey});
    
            var gridObj = $.pubGrid(metaEleId);
    
            var items = colData.list;
    
            if(gridObj){
                gridObj.setData(items,'reDraw');
                return ;
            }
    
            gridObj = $.pubGrid(metaEleId, {
                headerOptions : {redraw : false}
                ,setting : {
                    enabled : true
                    ,enableSearch : true
                }
                ,asideOptions: {lineNumber: {enabled: true, width : 30}}
                ,tColItem : [
					{ label: '컬럼명', key: 'name',width:80 },
					{ label: '데이터타입', key: 'typeAndLength' },
					{ label: '널여부', key: 'nullable',width:45},
					{ label: 'Key', key: 'constraints',width:45},
					{ label: '설명', key: 'comment',width:45}
				]
                ,tbodyItem :items
                ,message : {
                    empty : ''
                }
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
        ,_packageMetaResize : function (dimension){
            if(this.selectMetadata['package'].metaTab){
                var gridObj = $.pubGrid(this.selectMetadata['package'].metaTab.getTabContentSelector({'tabid' : 'column'}));
    
                if(gridObj){
                    gridObj.resizeDraw();
                }
            }
        }
	}
}

VARSQL.vender['ORACLE'] = $oracle;

}(jQuery,VARSQL));