<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div class="popup user-select-on">
	<div id="epViewArea" class="item-detail-page">
		<template v-if="viewMode===false">
			<table class="wh100">
				<tr>
					<td class="text-center" style="text-align: center; font-size: 3em;">
						<spring:message code="msg.retry" text="다시 시도 해주세요"/><br/>
						<spring:message code="error.400" text="잘못된 요청 입니다"/>
					</td>
				</tr>
			</table>
		</template>
		<template v-else>
			<div class="header-area">
				<h3><spring:message code="detail.view" /></h3>
				<div class="view-btn">
					<ul>
						<li>
							<a href="javascript:;" @click="setLayoutMode('top')" title="top-bottom(3:7)"><i class="layout-icon top"></i></a>
						</li>
						<li>
							<a href="javascript:;" @click="setLayoutMode('left')" title="left-right(3:7)"><i class="layout-icon left"></i></a>
						</li>
						<li>
							<a href="javascript:;" @click="setLayoutMode('right')"title="left-right(7:3)"><i class="layout-icon right"></i></a>
						</li>
					</ul>
				</div>
			</div>
			<div id="layoutArea" class="main-content"></div>
		</template>

		<div id="detailViewTemplate" style="display: none;">
			<div class="item-detail-area" style="overflow: auto;">
				<table class="w100" style="table-layout:initial;">
					<thead style="display:none;">
						<tr>
							<td class="item-col-label">
							</td>
							<td class="item-col-content">
								<pre id="calcHeight" class="content-view"></pre>
							</td>
						</tr>
					</thead>
					<tbody>
						<tr v-for="(colItem,index) in colInfos">
							<td class="item-col-label">{{colItem.label}}</td>
							<td class="item-col-content">
								<template v-if="colItem.dbType=='CLOB'">
									<pre class="content-view huge">{{viewItem[colItem.key]}}</pre>
								</template>
								<template v-else>
									<pre class="content-view" :class="(viewItem[colItem.key+'_cls']||'')">{{viewItem[colItem.key]}}</pre>
								</template>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

<script>

(function() {

//VARSQL.unload('security');
VARSQLCont.init('${dbtype}');
var viewObj = VarsqlAPP.vueServiceBean( {
	el: '#epViewArea'
	,data: {
		colInfos :[]
		, itemList :[]
		, viewItem:{}
		, viewMode : true
	}
	,methods:{
		setViewMode : function (mode){
			this.viewMode = mode;
			if(mode != false){
				setLayout('top', false);
			}
		}
		,setLayoutMode : function (mode){
			setLayout(mode, true)
		}
		,setItemDetail : function (item){
			if(VARSQL.isUndefined(item)){
				item = this.itemList[0];
			}

			if(item['_$itemCheck'] !==true){

				for(var i=0; i <this.colInfos.length;i++){
					var colItem = this.colInfos[i];
					
					if(colItem.number){
						;
					}else if(VARSQLCont.getDataTypeInfo(colItem.dbType).isDate===true){
						;
					}else{
						var val = item[colItem.key];
						if(val){

							var line = val.split('\n').length;

							if(line > 1){
								item[colItem.key+'_cls'] = 'huge'+(line > 4 ? '' : line);
							}else{
								if(val.length > 70){
									item[colItem.key+'_cls'] = 'huge3';
								}
							}
						}
					}
				}
				item['_$itemCheck']  = true;
			}

			this.viewItem = item;
			
			this.$nextTick(function (){
				$('#detailItemArea').empty().html($('#detailViewTemplate').html());
			});
		}
		,setItem : function (pColInfos, pItemList){

			var colInfos = VARSQL.util.objectMerge([],pColInfos);
			var itemList = VARSQL.util.objectMerge([],pItemList);;

			var contentViewEleArr = document.querySelectorAll('.content-view');
			for(var i =0; i <contentViewEleArr.length; i++){
				contentViewEleArr[i].style = '';
			}

			this.colInfos = colInfos;
			this.itemList = itemList;

			if($('#itemList').length > 0){
				this.viewGrid();
				this.setItemDetail();
			}
		}
		,viewGrid : function (){

			var _this =this;
			var colInfos = _this.colInfos;
			for(var i =0 ;i < colInfos.length;i++){
				var item = colInfos[i];
				delete item['width'];
			}

			$.pubGrid('#itemList',{
				selectionMode : 'row'
				,setting : {
					enabled : true
					,click : false
					,enableSearch : true
					,enableColumnFix : true
				}
				,asideOptions :{
					lineNumber : {enabled : true}
				}
				,valueFilter : function (colInfo, objValue){
					var val = objValue[colInfo.key];
					if(colInfo.dbType =='CLOB' && !VARSQL.isBlank(val)){
						return val.substring(0, 2000);
					}else{
						return val;
					}
				}
				,rowOptions :{
					click : function (rowInfo){
						_this.setItemDetail(rowInfo.item);
					}
				}
				,bodyOptions : {
					keyNavHandler : function(moveInfo){
						_this.setItemDetail(moveInfo.item);
					}
				}
				,tColItem : this.colInfos
				,tbodyItem :this.itemList
			})
		}
	}
});

window.viewItem = function(colInfos, itemList){
	viewObj.setItem(colInfos, itemList);
}

var popid = 'pop_'+$varsqlConfig.conuid;

var openerCheckFlag = false;
if(opener){
	if(VARSQL.isFunction(opener[popid])){
		opener[popid]();
		openerCheckFlag = true;
	}
}

viewObj.setViewMode(openerCheckFlag);

var varsqlLayout;

function setLayout(mode, changeFlag){
	var config = {
		settings: {
			hasHeaders: false,
			constrainDragToContainer: true,
			reorderEnabled: false,
			selectionEnabled: false,
			popoutWholeStack: false,
			blockedPopoutsThrowError: true,
			closePopoutsOnUnload: true,
			showPopoutIcon: false,
			showMaximiseIcon: true,
			showCloseIcon: false
		},
		dimensions: {
			borderWidth: 10,
			minItemHeight: 10,
			minItemWidth: 10,
			headerHeight: 20,
			dragProxyWidth: 300,
			dragProxyHeight: 200
		},
		labels: {
			close: 'close',
			maximise: 'maximise',
			minimise: 'minimise'
		},
		content: []
	};

	if(mode =='top'){
		config.content =[{
			type: 'column',
			content: [
				{
					type:'component',
					componentName: 'Data',
					height :30,
					componentState: { text: 'Component 1' }
				},
				{
					type:'component',
					componentName: 'Detail',
					height :70,
					componentState: { text: 'Component 2' }
				}
			]
		}];

	}else{
		var firstW = (mode=='left' ? 30 : 70)
			,secondW = (mode=='left' ? 70 :30)
			,firstName = (mode=='left' ? 'Data' :'Detail')
			,secondName = (mode=='left' ? 'Detail' :'Data');

		config.content =[{
			type: 'row',
			content: [
				{
					type:'component',
					width: firstW,
					componentName: firstName,
					componentState: { text: 'Component 1' }
				},
				{
					type:'component',
					width: secondW,
					componentName: secondName,
					componentState: { text: 'Component 2' }
				}
			]
		}];
	}

	if(changeFlag === false){
		var saveState = VARSQL.localStorage('varsqlDetailLayout');

		try{
			if(!VARSQL.isBlank(saveState)){
				var currConfig = JSON.parse( saveState);
				config = !VARSQL.isBlank(currConfig) ? currConfig : config;
			}
		}catch(e){
			console.log(e);
		}
	}

	if(varsqlLayout){
		varsqlLayout.destroy();
		$('#layoutArea').empty()
	}

	varsqlLayout = new GoldenLayout( config, $('#layoutArea'));

	varsqlLayout.registerComponent( 'Data', function( container, componentState ){
		container.getElement().html('<div id="itemList" class="wh100" style=""></div>');
		var initResize = true;
		container.on('resize',function() {
			if(initResize === true){
				initResize = false;
				return ;
			}
			$.pubGrid('#itemList').resizeDraw();

		});
	});

	varsqlLayout.registerComponent( 'Detail', function( container, componentState ){
		container.getElement().html('<div id="detailItemArea" class="wh100" style=""></div>');
	});

	varsqlLayout.on('initialised', function( contentItem ){
		var firstFlag = true;
		var layoutSaveTimer;

		var layoutSaveTimer;
		var firstFlag = true;

		varsqlLayout.on( 'stateChanged', function(){
			if(firstFlag){
				viewObj.viewGrid();
				viewObj.setItemDetail();

				firstFlag = false;
				return ;
			}
			clearTimeout(layoutSaveTimer);

			layoutSaveTimer = setTimeout(function() {
				VARSQL.localStorage({key : 'varsqlDetailLayout', value : JSON.stringify(varsqlLayout.toConfig()) })
			}, 300);
		});
    });

	varsqlLayout.init();

	var windowResizeTimer;
	$(window).resize(function() {
		clearTimeout(windowResizeTimer);
		windowResizeTimer = setTimeout(function() {
			varsqlLayout.updateSize();
		}, 20);
	})



	if(changeFlag){
		localStorage.setItem('varsqlDetailLayout', JSON.stringify( varsqlLayout.toConfig()))
	}
}

}());
</script>