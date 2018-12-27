<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manage.menu.dbcomparemgmt" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
	
	
	    			
<div class="row" id="varsqlVueArea">

	<div class="col-xs-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				대상
				<select class="input-sm" v-model="diffItem.source" @change="sourceChange(diffItem.source)" style="width:30%">
					<option value="">선택</option>
						<option  v-for="(item,index) in dbList" :value="item.VCONNID">{{item.VNAME}}</option>
				</select>
				타켓
				<select class="input-sm" v-model="diffItem.target" style="width:30%">
					<option value="">선택</option>
					<option v-for="(item,index) in dbList" :value="item.VCONNID">{{item.VNAME}}</option>
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
					<div class="col-xs-6">
						<div class="panel panel-default">
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
						<div class="panel panel-default">
							<div style="font-weight:bold;">{{compareObjectName}}&nbsp;</div>
							<div class="panel-body" style="padding-top:0px;">	
								<div class="col-xs-6">
									<div style="margin:3px;">
										컬럼
										<div id="sourceColumn" class="row" style="height:200px;">
										
										</div>
									</div>
								</div>
								<div class="col-xs-6">
									<div style="margin:3px;">
										컬럼
										<div id="targetColumn" class="row" style="height:200px;">
										
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-xs-6" style="padding-left:0px;">
						<div class="panel panel-default">
							<div class="panel-heading">
								비교 결과
							</div>
							<div class="panel-body" >	
								<pre v-html="compareResult" id="compareResultArea" style="height:400px;overflow:auto;">
								
								</pre>
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
		,compareObjectName :'select'
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
				return this[this.diffItem.objectType+'Compare'].call(this);
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
		}
		// db object search.
		,objectListSearch : function(no){
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
				url:{gubun:VARSQL.uri.manager, url:'/diff/objectList'}
				,loadSelector : '#sourceObjectMeta'
				,data :  {
					vconnid : diffItem.source 
					,objectType : objectType
				}
				,success: function(resData) {
					_self[objectType+'ObjectView'].call(_self, resData,'source');
				}
			})
			
			// target data load
			this.$ajax({
				url:{gubun:VARSQL.uri.manager, url:'/diff/objectList'}
				,loadSelector : '#targetObjectMeta'
				,data :  {
					vconnid : diffItem.target 
					,objectType : objectType
				}
				,success: function(resData) {
					_self[objectType+'ObjectView'].call(_self, resData,'target');
				}
			})
		}
		// 테이블 비교
		,tableCompare : function (){
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
					compareLog.push('<a href="javascript:;" class="table-info table-name" data-table-name="'+key+'">'+key +'</a>테이블 정보가 다릅니다.<div class="column-compare-log" data-tbl-name="'+key+'">');
					if(sourceItem) sourceItem = sourceNameMap[key];
					targetItem = targetCompareNameMap[key];
					
					if(sourceItem.remarks != targetItem.remarks){
						compareFlag = true; 
						compareLog.push('테이블의 설명이 같지 않습니다. 대상 : '+sourceNameMap[key].remark + ' 타켓 : '+targetCompareNameMap[key].remark +'\n');
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
								for(var colItemKey in sourceColItem){
									if($.trim(sourceColItem[colItemKey]) != $.trim(targetColItem[colItemKey])){
										addFlag = true; 
										compareFlag = true;
										compareLog.push((firstFlag===true ? '  컬럼 : '+sourceColKey+' \t ' : '')+colItemKey +' 대상 : ['+sourceColItem[colItemKey]+ '] 타켓 : ]'+targetColItem[colItemKey]+'],  ');
										
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
					compareResult.push('대상에 <a href="javascript:;" class="table-name" data-table-name="'+key+'">'+key+'</a> 테이블이 존재 하지 않습니다 ');
				}
				
				delete targetCompareNameMap[key];
			}
			
			this.compareItem.objectColNameMap =objectColNameMap;
			
			for(var key in targetCompareNameMap){
				compareFlag = true; 
				compareLog.push('타켓에 [ <a href="javascript:;" class="table-name" data-table-name="'+key+'">'+key+ '</a>] 테이블이 존재 하지 않습니다.\n');
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
		,sourceChange : function (val){
			var _self = this; 
			
			this.$ajax({
				url:{gubun:VARSQL.uri.manager, url:'/diff/objectType'}
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


(function (){

	function getClass(val) {
		return Object.prototype.toString.call(val)
			.match(/^\[object\s(.*)\]$/)[1];
	};

	function getType(val) {

		if (val === undefined) return 'undefined';
		if (val === null) return 'null';

		var type = typeof val;

		if (type === 'object'){
			type = getClass(val).toLowerCase();
		}

		if (type === 'number') {
			return (val.toString().indexOf('.') > 0) ? 'float' : 'integer';
		}

		return type;
	};

	function equal(a, b) {
		if (a !== b) {
			var atype = getType(a), btype = getType(b);

			if (atype === btype)
				return _equal.hasOwnProperty(atype) ? _equal[atype](a, b) : _equal.other(a, b);

			return false;
		}

		return true;
	}

	var _equal = {
		'array' : function(a, b) {
			if (a === b)
				return true;
			if (a.length !== b.length)
				return false;
			for (var i = 0; i < a.length; i++){
				if(!equal(a[i], b[i])) return false;
			};
			return true;
		}
		,'object' : function(a, b) {
			if (a === b)
				return true;
			for (var i in a) {
				if (b.hasOwnProperty(i)) {
					if (!equal(a[i],b[i])) return false;
				} else {
					return false;
				}
			}

			for (var i in b) {
				if (!a.hasOwnProperty(i)) {
					return false;
				}
			}
			return true;
		}
		,'date' : function(a, b) {
			return a.getTime() === b.getTime();
		}
		,'regexp' : function(a, b) {
			return a.toString() === b.toString();
		}
		,'other' : function (a, b){
			return a==b; 
		}
	}

	window.VARSQLCompare = function (a,b){
		return equal(a,b);
	};
	})()
</script>