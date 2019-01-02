<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<style>

[data-compare-area="grid"]{
	display:block;
}
[data-compare-mode="text"] [data-compare-area="grid"]{
	display:none;
}

[data-compare-area="text"]{
	display:none;
}

[data-compare-mode="text"] [data-compare-area="text"]{
	display:block;
}

.CodeMirror-merge, .CodeMirror-merge .CodeMirror {
    height: 650px;
}

.CodeMirror-merge {
    box-sizing: content-box;
}

</style>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manage.menu.dbcomparemgmt" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
	    			
<div class="row" id="varsqlVueArea">
	<div  style="z-index:100;position:absolute;width:100%; height:100%;">
		<table>
			
		</table>
		<div style="height:100%;background-repeat:no-repeat;background-image:url(${pageContext.request.contextPath}/webstatic/imgs/progressLoader.gif);background-position:center center;    background-color: #d3caca;
    opacity: 0.3;"></div>
	</div>
	
	<div class="col-xs-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				대상
				<select class="input-sm" v-model="diffItem.source" @change="sourceChange(diffItem.source)" style="width:30%">
					<option value="">선택</option>
					<option v-for="(item,index) in dbList" :value="item.VCONNID">{{item.VNAME}}</option>
				</select>
				타켓
				<select class="input-sm" v-model="diffItem.target" style="width:30%">
					<option value="">선택</option>
					<option v-for="(item,index) in dbList" v-if="diffItem.source != item.VCONNID" :value="item.VCONNID">{{item.VNAME}}</option>
				</select>
				오브젝트
				<select class="input-sm" v-model="diffItem.objectType" style="width:10%">
					<option value="">선택</option>
					<option v-for="(item,index) in objectList" :value="item.contentid">{{item.name}}</option>
				</select>
				<button @click="objectListSearch()" type="button" class="btn btn-sm btn-primary" style="margin-bottom: 3px">
					조회
				</button>
			</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-xs-5">
						<div class="panel panel-default" style="height:250px;padding-bottom:10px;margin-bottom:10px;">
							<div class="panel-body" style="padding: 0px;">	
								<div class="col-xs-6">
									<div style="margin:3px;">
										<div>대상</div>
										<div id="sourceObjectMeta" class="row" style="height:200px;">
											
										</div>
									</div>
								</div>
								<div class="col-xs-6">
									<div style="margin:3px;">
										<div>타켓</div>
										<div id="targetObjectMeta" class="row" style="height:200px;">
											
										</div>
									</div>
								</div>
							</div>
						</div>
						<div>
							<div class="panel-heading" style="padding:3px;">
								비교 결과
							</div>
							<div class="panel-body" style="padding: 0px;">	
								<pre v-html="compareResult" id="compareResultArea" style="height:400px;overflow:auto;">
								
								</pre>
							</div>
						</div>
					</div>
					<div class="col-xs-7" style="padding-left:0px;">
						<div class="panel panel-default" style="padding-left: 0px; height: 685px;">
							비교상세 : <span style="font-weight: bold;padding-left:5px;">{{compareObjectName}}&nbsp;</span>
							<div class="panel-body" style="padding: 5px;" :data-compare-mode="diffItem.objectType != 'table'?'text':''">	
								<div data-compare-area="grid">
									<div class="col-xs-6">
										<div style="margin:3px;">
											<div class="row">컬럼</div>
											<div id="sourceColumn" class="row" style="height:550px;">
											
											</div>
										</div>
									</div>
									<div class="col-xs-6">
										<div style="margin:3px;">
											<div class="row">컬럼</div>
											<div id="targetColumn" class="row" style="height:550px;">
											
											</div>
										</div>
									</div>
								</div>
								<div data-compare-area="text">
									<div id="compareTextArea" style="height: 650px;"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
		
</div>
<!-- /.row -->


<script>
VarsqlAPP.vueServiceBean( {
	el: '#varsqlVueArea'
	,data: {
		dbList : ${varsqlfn:objectToJson(dbList)}
		,diffItem : {
			source :''
			,target :''
			,objectType : ''
		}
		,compareObjectName :''
		,objectList : []
		,sourceItems : false	// 원천 item 목록.
		,targetItems : false
		,compareItem : {
			sourceNameMap : {}
			, targetNameMap : {}
			, objectColNameMap :{}
		}
	}
	,computed: {
		compareResult : function (){
			if(this.sourceItems !==false && this.targetItems !== false){
				var compareFn = this[this.diffItem.objectType+'Compare'];
				if(compareFn){
					return compareFn.call(this);
				}else{
					return this.otherCompare.call(this);
				}
			}
			
			return '';
		}
	}
	,methods:{
		init : function(){
			var _self = this;
			
			$('#compareResultArea').on('click','.table-name' , function (e){
				var sEle = $(this);
				
				var tblName = sEle.data('table-name');
				
				_self.tableObjectMetaView(tblName);
			})
			
			$('#compareResultArea').on('click','.object-name' , function (e){
				var sEle = $(this);
				
				var objName = sEle.data('object-name');
				
				_self.otherMetaView(objName);
			})
		}
		// db object search.
		,objectListSearch : function(){
			var _self = this; 
			
			var diffItem = _self.diffItem;
			if(diffItem.source ==''){
				VARSQLUI.toast.open('타켓을 선택하세요.');
				return ;
			}
			if(diffItem.target ==''){
				VARSQLUI.toast.open('대상을 선택하세요.');
				return ;
			}
			
			if(diffItem.objectType ==''){
				VARSQLUI.toast.open('objectType을 선택하세요.');
				return ;
			}
			
			_self.targetItems = false;
			_self.sourceItems = false;
			
			var objectType = diffItem.objectType; 
			// source data load
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/diff/objectList'}
				,loadSelector : '#sourceObjectMeta'
				,data :  {
					vconnid : diffItem.source 
					,objectType : objectType
				}
				,success: function(resData) {
					
					var objViewFn = _self[objectType+'ObjectView'];
					
					if(objViewFn){
						objViewFn.call(_self, resData,'source');
					}else{
						_self.otherObjectView.call(_self, resData,'source');
					}
				}
			})
			
			// target data load
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/diff/objectList'}
				,loadSelector : '#targetObjectMeta'
				,data :  {
					vconnid : diffItem.target 
					,objectType : objectType
				}
				,success: function(resData) {
					var objViewFn = _self[objectType+'ObjectView'];
					
					if(objViewFn){
						objViewFn.call(_self, resData,'target');
					}else{
						_self.otherObjectView.call(_self, resData,'target');
					}
				}
			})
		}
		// 테이블 비교
		,tableCompare : function (){
			var sourceItems =this.sourceItems
				,targetItems = this.targetItems;
			
			var compareColKey = ['name','typeAndLength','constraints','defaultVal','nullable','comment'];
			var compareColKeyLength =compareColKey.length;
			var maxLen = Math.max(sourceItems.length,targetItems.length);
			
			var sourceNameMap = {}, targetNameMap={}, targetCompareNameMap={};
			var sourceItem, targetItem;
			for(var i =0 ;i < maxLen; i++){
				sourceItem = sourceItems[i];
				if(sourceItem) sourceNameMap[sourceItem.name] = sourceItem;
				
				targetItem = targetItems[i];
				if(targetItem) {
					targetCompareNameMap[targetItem.name] = targetItem;
					targetNameMap[targetItem.name] = targetItem;
				}
			}
			
			this.compareItem.sourceNameMap = sourceNameMap; 
			this.compareItem.targetNameMap = targetNameMap;
			
			var compareResult = [];
			var objectColNameMap = {};
			
			var sourceColList , targetColList;
			var compareLog;
			var compareFlag = false; 
			for(var key in sourceNameMap){
				
				compareLog = [];
				compareFlag = false; 
				if(targetCompareNameMap.hasOwnProperty(key)){
					compareLog.push('<a href="javascript:;" class="table-info table-name" data-table-name="'+key+'">'+key +'</a>테이블 정보가 다릅니다.<div class="column-compare-log" data-tbl-name="'+key+'">');
					
					sourceItem = sourceNameMap[key];
					targetItem = targetCompareNameMap[key];
					
					if(sourceItem.remarks != targetItem.remarks){
						compareFlag = true; 
						compareLog.push('테이블의 설명이 같지 않습니다. 대상 : ['+sourceNameMap[key].remarks + '] 타켓 : ['+targetCompareNameMap[key].remarks +'] \n');
					}
					
					sourceColList = sourceItem.colList; 
					targetColList = targetItem.colList;
					
					var sourceColLen = sourceColList.length; 
					var targetColLen = targetColList.length;
					
					if(sourceColLen != targetColLen){
						compareFlag = true; 
						compareLog.push('컬럼 카운트  대상 : '+sourceColLen+ ' 타켓 : '+targetColLen+' \n');
					}
					
					var maxColLen = Math.max(sourceColLen,targetColLen);
					
					var sourceColMap = {};
					var targetColMap = {};
					var sourceColItem, targetColItem;
					var colNameMap = {};
					for(var i =0 ; i< maxColLen; i++){
						sourceColItem = sourceColList[i];
						if(sourceColItem){
							sourceColMap[sourceColItem.name] = sourceColItem;
							colNameMap[sourceColItem.name] = '';
						}
					
						targetColItem= targetColList[i];
						if(targetColItem){
							targetColMap[targetColItem.name] = targetColItem;
							colNameMap[targetColItem.name] = '';
						}
					}
					
					objectColNameMap[key] = colNameMap; // table columun name 저장. 
					
					for(var sourceColKey in sourceColMap){
						sourceColItem = sourceColMap[sourceColKey];
						targetColItem = targetColMap[sourceColKey];
						
						delete targetColMap[sourceColKey];
						
						if(sourceColItem != targetColItem){
							if(targetColItem){
								var firstFlag = true;
								var addFlag = false; 
								var colItemKey;
								for(var colKeyIdx =0; colKeyIdx <compareColKeyLength; colKeyIdx++){
									colItemKey = compareColKey[colKeyIdx];
									if($.trim(sourceColItem[colItemKey]) != $.trim(targetColItem[colItemKey])){
										addFlag = true; 
										compareFlag = true;
										compareLog.push((firstFlag===true ? '  컬럼 : '+sourceColKey+' \t ' : '')+colItemKey +' 대상 : ['+sourceColItem[colItemKey]+ '] 타켓 : ['+targetColItem[colItemKey]+'], ');
										
										firstFlag = false;
									}
								}
								if(addFlag) compareLog.push('\n');
							}else{
								compareFlag = true; 
								compareLog.push('대상 테이블에 ['+sourceColKey+ '] 컬럼이 존재 하지 않습니다.\n');
							}
						}
					}
					
					for(var targetColKey in targetColMap){
						compareFlag = true; 
						compareLog.push('타켓 테이블에 ['+targetColKey+ '] 컬럼이 존재 하지 않습니다.\n');
					}
					
					compareLog.push('</div>');
					
					if(compareFlag){
						compareResult.push(compareLog.join(''))
					}
				}else {
					compareResult.push('<span style="color:#f60b0b;">대상에 <a href="javascript:;" class="table-name" data-table-name="'+key+'">'+key+'</a> 테이블이 존재 하지 않습니다 </span>');
				}
				
				delete targetCompareNameMap[key];
			}
			
			this.compareItem.objectColNameMap =objectColNameMap;
			
			for(var key in targetCompareNameMap){
				compareResult.push('<span style="color:#b55e34;">타켓에 [ <a href="javascript:;" class="table-name" data-table-name="'+key+'">'+key+ '</a>] 테이블이 존재 하지 않습니다.</span>');
			}
			
			return compareResult.join('\n');
		}
		// 테이블 데이터 비교.
		,tableObjectView : function (resData, mode){
			var _self = this; 
			
			if(mode=='source'){
				var itemArr = resData.items;
				this.sourceItems = itemArr;
				$.pubGrid('#sourceObjectMeta',{
					setting : {
						enable : true
						,click : false
						,enableSearch : true
						,enableSpeed : true
					}
					,headerOptions:{
						resize:{
							update :  function (item){
								$.pubGrid('#targetObjectMeta').setHeaderWidth(item.index , item.width);
							}
						}
					}
					,asideOptions :{
						lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
					}
					,tColItem : [
						{key :'name', label:'Table', width:200, sort:true}
						,{key :'remarks', label:'설명', sort:true}
					]
					,tbodyItem :itemArr
					,rowOptions :{
						click : function (idx, item){
							var sObj = $(this);
							
							_self.tableObjectMetaView(item);
						}
					}
					,scroll :{
						vertical : {
							onUpdate : function (item){
								$.pubGrid('#targetObjectMeta').moveVScrollPosition(item.position,'',false);
							}
						}
						,horizontal :{
							onUpdate : function (item){ 
								$.pubGrid('#targetObjectMeta').moveHScrollPosition(item.position,'',false);
							}
						}
					}
				});
			}else{
				var itemArr = resData.items; 
				this.targetItems = itemArr;
				
				$.pubGrid('#targetObjectMeta',{
					setting : {
						enable : true
						,click : false
						,enableSearch : true
						,enableSpeed : true
					}
					,headerOptions:{
						resize:{
							update :  function (item){
								$.pubGrid('#sourceObjectMeta').setHeaderWidth(item.index , item.width);
							}
						}
					}
					,asideOptions :{
						lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
					}
					,tColItem : [
						{key :'name', label:'Table', width:200, sort:true}
						,{key :'remarks', label:'설명', sort:true}
					]
					,tbodyItem :itemArr
					,rowOptions :{
						click : function (idx, item){
							_self.tableObjectMetaView(item);
						}
					}
					,scroll :{
						vertical : {
							onUpdate : function (item){
								$.pubGrid('#sourceObjectMeta').moveVScrollPosition(item.position, '',false);
							}
						}
						,horizontal :{
							onUpdate : function (item){ 
								$.pubGrid('#sourceObjectMeta').moveHScrollPosition(item.position,'',false);
							}
						}
					}
				});
			}
		}
		// 테이블  column 비교. 
		, tableObjectMetaView:  function (viewItem){
			
			var tblName = viewItem;
			if(typeof viewItem==='object'){
				tblName = viewItem.name;
			}
			
			this.compareObjectName = tblName;
			
			var sourceItem = this.compareItem.sourceNameMap[tblName]||{}; 
			var targetItem = this.compareItem.targetNameMap[tblName] ||{};
			
			var sourceColList = sourceItem.colList||[];
			var targetColList = targetItem.colList||[];
			
			var compareSourceColList =sourceColList;
			var compareTargetColList =targetColList;
			
			if(sourceColList.length > 0  && targetColList.length > 0){
				
				compareSourceColList =[];
				compareTargetColList =[];
				
				var maxLen = Math.max(sourceColList.length,targetColList.length);
				
				var sourceColMap={};
				var targetColMap={};
				
				var colNameMap = {};
				
				for(var i =0 ; i < maxLen; i++){
					var sourceColItem = sourceColList[i];
					if(sourceColItem){
						sourceColMap[sourceColItem.name] = sourceColItem;
						colNameMap[sourceColItem.name] = '';
					}
				
					var targetColItem= targetColList[i];
					if(targetColItem){
						targetColMap[targetColItem.name] = targetColItem;
						colNameMap[targetColItem.name] = '';
					}
				}
				
				for(var key in colNameMap){
					var sourceCol = sourceColMap[key]||{'__ne' :true};
					var targetCol = targetColMap[key]||{'__ne' :true};
					
					for(var colKey in sourceCol){
						if($.trim(sourceCol[colKey]) != $.trim(targetCol[colKey])){
							sourceCol[colKey+'_ne'] = true;
							targetCol[colKey+'_ne'] = true
							sourceCol['__ne'] = true;
							targetCol['__ne'] = true
						}
					}
					
					compareSourceColList.push(sourceCol);
					compareTargetColList.push(targetCol);
				}
			}
			
			var errorFormatter =  function (item){
				if(item.item[item.colInfo.key +'_ne']){
					return '<span style="color:red;">'+item.item[item.colInfo.key]+'</span>';
				}else{
					if(typeof item.item[item.colInfo.key] ==='undefined'){
						return '<span style="color:red;">empty</span>';;
					}else{
						return item.item[item.colInfo.key];
					}
				}
			}
			
			var sourceColumnGridObj = $.pubGrid('#sourceColumn');
			if(sourceColumnGridObj){
				sourceColumnGridObj.setData(compareSourceColList,'reDraw');
			}else{
				$.pubGrid('#sourceColumn', {
					asideOptions :{lineNumber : {enable : true	,width : 30}}
					,tColItem : [
						{ label: '컬럼명', key: 'name',width:80 , render : 'html',formatter : errorFormatter},
						{ label: '데이타타입', key: 'typeAndLength', render : 'html',formatter : errorFormatter },
						{ label: 'Key', key: 'constraints', align:'center', width:45, render : 'html',formatter : errorFormatter},
						{ label: '기본값', key: 'defaultVal',width:45 , render : 'html',formatter : errorFormatter},
						{ label: '널여부', key: 'nullable',width:45 , render : 'html',formatter : errorFormatter},
						{ label: '설명', key: 'comment',width:45 , render : 'html',formatter : errorFormatter}
					]
					,rowOptions :{
						addStyle :function (a){
							return a['__ne'] ? 'background:#fbdcdc;' :'';
						}
					}
					,tbodyItem : compareSourceColList
					,headerOptions:{
						resize:{
							update :  function (item){
								$.pubGrid('#targetColumn').setHeaderWidth(item.index , item.width);
							}
						}
					}
					,scroll :{
						vertical : {
							onUpdate : function (item){
								$.pubGrid('#targetColumn').moveVScrollPosition(item.position,'',false);
							}
						}
						,horizontal :{
							onUpdate : function (item){ 
								$.pubGrid('#targetColumn').moveHScrollPosition(item.position,'',false);
							}
						}
					}
					,message : {
						empty : '테이블이 존재 하지 않습니다.'
					}
				});
			}
			
			var targetColumnGridObj = $.pubGrid('#targetColumn');
			
			if(targetColumnGridObj){
				targetColumnGridObj.setData(compareTargetColList,'reDraw');
			}else{
				$.pubGrid('#targetColumn', {
					asideOptions :{lineNumber : {enable : true	,width : 30}}
					,tColItem : [
						{ label: '컬럼명', key: 'name',width:80, render : 'html',formatter : errorFormatter},
						{ label: '데이타타입', key: 'typeAndLength', render : 'html',formatter : errorFormatter },
						{ label: 'Key', key: 'constraints', align:'center', width:45, render : 'html',formatter : errorFormatter},
						{ label: '기본값', key: 'defaultVal',width:45, render : 'html',formatter : errorFormatter},
						{ label: '널여부', key: 'nullable',width:45, render : 'html',formatter : errorFormatter},
						{ label: '설명', key: 'comment',width:45, render : 'html',formatter : errorFormatter}
					]
					,rowOptions :{
						addStyle :function (a){
							return a['__ne'] ? 'background:#fbdcdc;' :'';
						}
					}
					,tbodyItem : compareTargetColList
					,headerOptions:{
						resize:{
							update :  function (item){
								$.pubGrid('#sourceColumn').setHeaderWidth(item.index , item.width);
							}
						}
					}
					,scroll :{
						vertical : {
							onUpdate : function (item){
								$.pubGrid('#sourceColumn').moveVScrollPosition(item.position,'',false);
							}
						}
						,horizontal :{
							onUpdate : function (item){ 
								$.pubGrid('#sourceColumn').moveHScrollPosition(item.position,'',false);
							}
						}
					}
					,message : {
						empty : '테이블이 존재 하지 않습니다.'
					}
				});
			}
		}
		,otherObjectView  : function (resData, mode){
			var _self = this; 
			
			if(mode=='source'){
				var itemArr = resData.items;
				this.sourceItems = itemArr;
				$.pubGrid('#sourceObjectMeta',{
					setting : {
						enable : true
						,click : false
						,enableSearch : true
						,enableSpeed : true
					}
					,headerOptions:{
						resize:{
							update :  function (item){
								$.pubGrid('#targetObjectMeta').setHeaderWidth(item.index , item.width);
							}
						}
					}
					,asideOptions :{
						lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
					}
					,tColItem : [
						{key :'name', label:'Table', width:200, sort:true}
						,{key :'remarks', label:'설명', sort:true}
					]
					,tbodyItem :itemArr
					,rowOptions :{
						click : function (idx, item){
							var sObj = $(this);
							
							_self.otherMetaView(item);
						}
					}
					,scroll :{
						vertical : {
							onUpdate : function (item){
								$.pubGrid('#targetObjectMeta').moveVScrollPosition(item.position,'',false);
							}
						}
						,horizontal :{
							onUpdate : function (item){ 
								$.pubGrid('#targetObjectMeta').moveHScrollPosition(item.position,'',false);
							}
						}
					}
				});
			}else{
				var itemArr = resData.items; 
				this.targetItems = itemArr;
				
				$.pubGrid('#targetObjectMeta',{
					setting : {
						enable : true
						,click : false
						,enableSearch : true
						,enableSpeed : true
					}
					,headerOptions:{
						resize:{
							update :  function (item){
								$.pubGrid('#sourceObjectMeta').setHeaderWidth(item.index , item.width);
							}
						}
					}
					,asideOptions :{
						lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
					}
					,tColItem : [
						{key :'name', label:'Table', width:200, sort:true}
						,{key :'remarks', label:'설명', sort:true}
					]
					,tbodyItem :itemArr
					,rowOptions :{
						click : function (idx, item){
							_self.otherMetaView(item);
						}
					}
					,scroll :{
						vertical : {
							onUpdate : function (item){
								$.pubGrid('#sourceObjectMeta').moveVScrollPosition(item.position, '',false);
							}
						}
						,horizontal :{
							onUpdate : function (item){ 
								$.pubGrid('#sourceObjectMeta').moveHScrollPosition(item.position,'',false);
							}
						}
					}
				});
			}
		}
		,otherCompare : function (){
			var sourceItems =this.sourceItems
				,targetItems = this.targetItems;
		
			var maxLen = Math.max(sourceItems.length,targetItems.length);
			
			var sourceNameMap = {}, targetNameMap={}, targetCompareNameMap={};
			var sourceItem, targetItem;
			for(var i =0 ;i < maxLen; i++){
				sourceItem = sourceItems[i];
				if(sourceItem) sourceNameMap[sourceItem.name] = sourceItem;
				
				targetItem = targetItems[i];
				if(targetItem) {
					targetCompareNameMap[targetItem.name] = targetItem;
					targetNameMap[targetItem.name] = targetItem;
				}
			}
			
			this.compareItem.sourceNameMap = sourceNameMap; 
			this.compareItem.targetNameMap = targetNameMap;
			
			var compareResult = [];
			var objectColNameMap = {};
			
			var sourceColList , targetColList;
			var compareLog;
			var compareFlag = false; 
			for(var key in sourceNameMap){
				
				compareLog = [];
				compareFlag = false; 
				if(targetCompareNameMap.hasOwnProperty(key)){
					compareLog.push('<a href="javascript:;" class="table-info object-name" data-object-name="'+key+'">'+key +'</a> 정보가 다릅니다.<div class="column-compare-log" data-tbl-name="'+key+'">');
					sourceItem = sourceNameMap[key];
					targetItem = targetCompareNameMap[key];
					
					if(sourceItem.remarks != targetItem.remarks){
						compareFlag = true; 
						compareLog.push('설명이 같지 않습니다. 대상 : '+sourceNameMap[key].remark + ' 타켓 : '+targetCompareNameMap[key].remark +'\n');
					}
					
					compareLog.push('</div>');
					
					if(compareFlag){
						compareResult.push(compareLog.join(''))
					}
				}else {
					compareResult.push('<span style="color:#f60b0b;">대상에 <a href="javascript:;" class="object-name" data-object-name="'+key+'">'+key+'</a> 존재 하지 않습니다 </span>');
				}
				
				delete targetCompareNameMap[key];
			}
			
			this.compareItem.objectColNameMap =objectColNameMap;
			
			for(var key in targetCompareNameMap){
				compareResult.push('<span style="color:#b55e34;">타켓에 [ <a href="javascript:;" class="object-name" data-object-name="'+key+'">'+key+ '</a>] 존재 하지 않습니다.</span>');
			}
			
			return compareResult.join('\n');
		}
		,otherMetaView :  function (viewItem){
			var objName = viewItem;
			if(typeof viewItem==='object'){
				objName = viewItem.name;
			}
			
			this.compareObjectName = objName;
			
			var sourceItem = this.compareItem.sourceNameMap[objName]||{}; 
			var targetItem = this.compareItem.targetNameMap[objName]||{};
			
			var compareEle =document.getElementById('compareTextArea'); 
			compareEle.innerHTML = '';
			
			var mergeView = CodeMirror.MergeView(compareEle, {
				value: (sourceItem.createScript||''),
				orig: (targetItem.createScript||''),
				lineNumbers: true,
				mode: "text/x-sql",
				highlightDifferences: true,
				connect: 'align',
				collapseIdentical: false
			});
			
// 			var height = 650; 
// 			if (mergeView.leftOriginal()){
// 				mergeView.leftOriginal().setSize(null, height);
// 			}
// 			mergeView.editor().setSize(null, height);
// 			if (mergeView.rightOriginal()){
// 				mergeView.rightOriginal().setSize(null, height);
// 			}
			
		}
		,sourceChange : function (val){
			var _self = this; 
			
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/diff/objectType'}
				,data : {
					vconnid : val
				}
				,success: function(resData) {
					_self.objectList = resData.items;
				}
			})
			
		}
	}
});

</script>