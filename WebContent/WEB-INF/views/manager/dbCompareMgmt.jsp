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
					<template href="javascript:;" class="list-group-item" v-for="(item,index) in dbList">
						<option :value="item.VCONNID">{{item.VNAME}}</option>
	    			</template>
				</select>
				타켓
				<select class="input-sm" v-model="diffItem.target" style="width:30%">
					<option value="">선택</option>
					<template href="javascript:;" class="list-group-item" v-for="(item,index) in dbList">
						<option :value="item.VCONNID">{{item.VNAME}}</option>
	    			</template>
				</select>
				오브젝트
				<select class="input-sm" v-model="diffItem.objectType" style="width:10%">
					<option value="">선택</option>
					<template href="javascript:;" class="list-group-item" v-for="(item,index) in objectList">
						<option :value="item.contentid">{{item.name}}</option>
	    			</template>
				</select>
				<button @click="objectListSearch()" type="button" class="btn btn-sm btn-primary" style="margin-bottom: 3px">
					조회
				</button>
			</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-xs-7">
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
							<div class="panel-body">	
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
					<div class="col-xs-5">
						<div class="panel panel-default">
							<div class="panel-heading">
								비교 결과
							</div>
							<div class="panel-body" >	
								<pre v-html="compareResult" style="height:400px;overflow:auto;">
								
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
		,objectList : []
		,sourceItems : false	// 원천 item 목록.
		,targetItems : false
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
			
		}
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
			_self.targetItems = false;
			
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
		,tableCompare : function (){
			var sourceItems =this.sourceItems
				,targetItems = this.targetItems;
			
			var maxLen = Math.max(sourceItems.length,targetItems.length);
			
			var sourceNameMap = {}, targetNameMap={};
			var sourceItem, targetItem;
			for(var i =0 ;i < maxLen; i++){
				sourceItem = sourceItems[i];
				if(sourceItem) sourceNameMap[sourceItem.name] = sourceItem;
				
				targetItem = targetItems[i];
				if(targetItem) targetNameMap[targetItem.name] = targetItem; 
			}
			
			var compareResult = [];
			
			var sourceColList , targetColList;
			var compareLog;
			var compareFlag = false; 
			for(var key in sourceNameMap){
				
				compareLog = [];
				compareFlag = false; 
				if(targetNameMap.hasOwnProperty(key)){
					compareLog.push(key +' start ---------');
					if(sourceItem) sourceItem = sourceNameMap[key];
					targetItem = targetNameMap[key];
					
					if(sourceItem.remarks != targetItem.remarks){
						compareFlag = true; 
						compareLog.push('테이블의 설명이 같지 않습니다. 대상 : '+sourceNameMap[key].remark + ' 타켓 : '+targetNameMap[key].remark);
					}
					
					sourceColList = sourceItem.colList; 
					targetColList = targetItem.colList;
					
					var sourceColLen = sourceColList.length; 
					var targetColLen = targetColList.length;
					
					if(sourceColLen != targetColLen){
						compareFlag = true; 
						compareLog.push('테이블 컬럼 카운트가 다릅니다. 대상 : '+sourceColLen+ ' 타켓 : '+targetColLen);
					}
					
					var maxColLen = Math.max(sourceColLen,targetColLen);
					
					var sourceColMap = {};
					var targetColMap = {};
					var sourceColItem, targetColItem;
					for(var i =0 ; i< maxColLen; i++){
						sourceColItem = sourceColList[i];
						if(sourceColItem) sourceColMap[sourceColItem.name] = sourceColItem;
					
						targetColItem= targetColList[i];
						if(targetColItem) targetColMap[targetColItem.name] = targetColItem;
					}
					
					for(var key in sourceColMap){
						sourceColItem = sourceColMap[key];
						targetColItem = targetColMap[key];
						
						delete targetColMap[key];
						
						if(sourceColItem != targetColItem){
							if(targetColItem){
								for(var colItemKey in sourceColItem){
									if(sourceColItem[colItemKey] !=targetColItem[colItemKey]){
										compareFlag = true; 
										compareLog.push(colItemKey +' 대상 : '+sourceColItem[colItemKey]+ ' 타켓 : '+targetColItem[colItemKey]);
									}
								}
							}else{
								compareFlag = true; 
								compareLog.push('대상 테이블에 ['+key+ '] 컬럼이 존재 하지 않습니다.');
							}
						}
					}
					
					for(var key in targetColMap){
						compareFlag = true; 
						compareLog.push('타켓 테이블에 ['+key+ '] 컬럼이 존재 하지 않습니다.');
					}
					
					compareLog.push(key +' end ---------');
					
					if(compareFlag){
						compareResult.push(compareLog.join('\n'))
					}
				}else {
					compareResult.push('대상에 '+key+' 테이블이 존재 하지 않습니다 ');
				}
				
				delete targetNameMap[key];
			}
			
			for(var key in targetNameMap){
				compareFlag = true; 
				compareLog.push('타켓에 ['+key+ '] 테이블이 존재 하지 않습니다.');
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
							
							$.pubGrid('#sourceColumn', {
								headerOptions : {redraw : false}
								,asideOptions :{lineNumber : {enable : true	,width : 30}}
								,tColItem : [
									{ label: '컬럼명', key: 'name',width:80 },
									{ label: '데이타타입', key: 'typeAndLength' },
									{ label: 'Key', key: 'constraints', align:'center', width:45},
									{ label: '기본값', key: 'defaultVal',width:45},
									{ label: '널여부', key: 'nullable',width:45},
									{ label: '설명', key: 'comment',width:45}
								]
								,tbodyItem : item.colList
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
							});
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
							
							$.pubGrid('#targetColumn', {
								headerOptions : {redraw : false}
								,asideOptions :{lineNumber : {enable : true	,width : 30}}
								,tColItem : [
									{ label: '컬럼명', key: 'name',width:80 },
									{ label: '데이타타입', key: 'typeAndLength' },
									{ label: 'Key', key: 'constraints', align:'center', width:45},
									{ label: '기본값', key: 'defaultVal',width:45},
									{ label: '널여부', key: 'nullable',width:45},
									{ label: '설명', key: 'comment',width:45}
								]
								,tbodyItem : item.colList
								,rowOptions :{
									
								}
							});
							
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