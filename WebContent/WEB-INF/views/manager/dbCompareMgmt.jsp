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
    height: 665px;
}

.CodeMirror-merge {
    box-sizing: content-box;
}

aside#resizable, section#content{
	box-sizing: border-box;
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
}

aside#resizable { width: 40%; height: 700px; padding: 0.5em; float:left;}
aside#resizable h3 { text-align: center; margin: 0; }

section#content{width:calc(100% - 42%);height:700px; margin-left: 5px; vertical-align: top; float:left;}
section#content:after{content:"";display:block;clear:both;}

/* jquery UI override */
.ui-resizable-e:hover, .ui-resizable-e:active{background-color:#aaaaab; cursor:col-resize;}
.resizable-helper { z-index: 1000; width: 1px; height: 100%; position: absolute; top: 0px; left: 610.016px; border: 3px dotted rgb(58, 80, 232); display: none;}

.ui-widget-content a {
    color: #428bca;
}

.source-object-meta, .source-target-meta{
	height:205px;
}

.source-column,.target-column{
	height:645px;
}

.object-count {
    font-weight: bold;
}

.result-detail{
	display: none;
}

.result-wrapper.open .result-detail{
	display: block;
}

</style>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manage.menu.dbcomparemgmt" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
	    			 
<div class="row display-off" id="varsqlVueArea">
	<div class="col-xs-12">
		<div :class="loading==true?'':'hidden'" style="z-index:100;position:absolute;width:98%; height:100%;">
			<div style="width:100%;height:100%;background-color: #d3caca;opacity: 0.5;position: absolute;top: 0px;left: 0px;"></div>
			
			<div style="width:100%;height:100%;opacity: 1;position: absolute;top: 0px;left: 0px;">
				<table style="z-index:1001;height:100%;width:100%;">
					<tr>
						<td>
							<div style="text-align: center;">
								<div style="width: 200px; padding: 5px 10px 5px 10px; background-color: #ffffff; line-height: 15pt; margin: auto;">
									<span>
										<spring:message code="msg.analyzing" text="분석 중입니다."/>
									</span><br>
									<span><img src="${pageContext.request.contextPath}/webstatic/imgs/progressLoader.gif"></span>
								</div>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
			
		<div class="panel panel-default">
			<div class="panel-heading db-compare-header">
				<spring:message code="source" text="대상"/>
				<span>
					<select class="input-sm" v-model="diffItem.source" @change="sourceChange(diffItem.source)" style="width:18%">
						<option value=""><spring:message code="select" text="선택"/></option>
						<option v-for="(item,index) in dbList" :value="item.VCONNID">{{item.VNAME}}</option>
					</select>
					<select class="input-sm" v-model="diffItem.sourceSchema" style="width:12%" @change="sourceSchemaChange(diffItem.sourceSchema)">
						<option value=""><spring:message code="select" text="선택"/></option>
						<option v-for="(item,index) in sourceSchemaList" :value="item">{{item}}</option>
					</select>
				
				</span>
				<spring:message code="target" text="타켓"/>
				<span>
					<select class="input-sm" v-model="diffItem.target" @change="targetChange(diffItem.target)" style="width:18%">
						<option value=""><spring:message code="select" text="선택"/></option>
						<option v-for="(item,index) in dbList" :value="item.VCONNID">{{item.VNAME}}</option>
					</select>
					<select class="input-sm" v-model="diffItem.targetSchema" style="width:12%">
						<option value=""><spring:message code="select" text="선택"/></option>
						<option v-for="(item,index) in targetSchemaList" :value="item">{{item}}</option>
					</select>
				</span>
				<spring:message code="object" text="오브젝트"/>
				<select class="input-sm" v-model="diffItem.objectType" style="width:10%">
					<option value=""><spring:message code="select" text="선택"/></option>
					<option v-for="(item,index) in objectList" :value="item.contentid">{{item.name}}</option>
				</select>
				<button @click="getObjectList()" type="button" class="btn btn-sm btn-primary" style="margin-bottom: 3px">
					<spring:message code="btn.compare" text="비교"/>
				</button>
			</div>
			<div class="panel-body" id="compareArea">
				<div class="row" style="position: relative;">
					<div id="resizeHelper" class="resizable-helper"></div>
					<aside id="resizable" class="ui-widget-content">
						<div class="panel panel-default" style="height:275px;padding-bottom:10px;margin-bottom:10px;">
							<div class="panel-body" style="padding: 0px;">
								<div class="input-group floatright" style="margin:3px;">
									<input type="text" class="form-control input-sm" v-model="searchValue" @keyup.enter="searchObject()"> 
									<span class="input-group-btn">
										<button type="button" class="btn btn-default btn-sm" @click="searchObject()">
											<span class="glyphicon glyphicon-search"></span>
										</button>
									</span>
								</div>
								<div class="col-xs-6">
									<div style="margin:3px;">
										<div style="height:25px;">
											<span style="position: absolute;margin-top: 5px;">
												<spring:message code="source" text="대상"/> <span class="object-count"> [{{resultInfo.sourceCount}}]</span>
												<span><label for="scrollSync" style="margin-bottom:0px;">scrollSync</label> <input style="margin:0px;" id="scrollSync" type="checkbox" v-model="scrollSync" value="Y"></span>
											</span>
										</div>
										<div id="sourceObjectMeta" class="row source-object-meta"></div>
									</div>
								</div>
								<div class="col-xs-6">
									<div style="margin:3px;">
										<div style="height:25px;">
											<span style="position: absolute;margin-top: 5px;">
												<spring:message code="target" text="타켓"/> <span class="object-count"> [{{resultInfo.targetCount}}]</span>
											</span>
											<span class="pull-right">
												<button type="button" @click="selectItemCompare()" class="btn btn-sm btn-success" style="padding:2px 3px;">select compare</button>
											</span>
										</div>
										<div id="targetObjectMeta" class="row source-target-meta"></div>
									</div>
								</div>
							</div>
						</div>
						<div>
							<div class="panel-heading" style="padding:3px;">
								<span><spring:message code="compare.result" text="비교결과"/></span>
								<button type="button"  @click="resultDownload()" class="btn btn-sm btn-primary" style="margin-left:10px;margin-bottom: 3px;"><spring:message code="result.download" text="결과 다운로드"/></button>
							</div>
							<div id="compareResultArea" class="panel-body" style="height:400px;overflow:auto;padding:0px;">
<style>
.result-table{
}
</style>
<div :class="isCompleteCompare===true?'view':'hidden'">----------::결과::---------
	<table style="width:100%;border:1px solid #000000;border-collapse: collapse;">
		<colgroup>
			<col style="width:100px;">
			<col style="width:*;">
		</colgroup>
		<tr>
			<td style="vertical-align:top;padding:3px 10px;border:1px solid #000000;">동일한 수</td>
			<td class="result-wrapper" style="vertical-align:top;padding:3px 10px;border:1px solid #000000;">
				<a href="javascript:;" class="result-count">{{sameInfo.cnt}}</a>
				<div class="result-detail" :class="sameInfo.cnt > 0 ?'':'hidden'"><pre>{{sameInfo.htm}}</pre></div>
			</td>
		</tr>
		<tr>
			<td style="vertical-align:top;padding:3px 10px;border:1px solid #000000;">다른 수</td>
			<td class="result-wrapper" style="vertical-align:top;padding:3px 10px;border:1px solid #000000;">
				<a href="javascript:;" class="result-count">{{differentInfo.cnt}}</a>
				<div class="result-detail" :class="differentInfo.cnt > 0 ?'':'hidden'"><pre v-html="differentInfo.htm"></pre></div>
			</td>
		</tr>
		<tr>
			<td style="vertical-align:top;padding:3px 10px;border:1px solid #000000;">없는 수</td>
			<td class="result-wrapper" style="vertical-align:top;padding:3px 10px;border:1px solid #000000;">
				<a href="javascript:;" class="result-count">{{emptyInfo.cnt}}</a>
				<div class="result-detail" :class="emptyInfo.cnt > 0 ?'':'hidden'"><pre>{{emptyInfo.htm}}</pre></div>
			</td>
		</tr>
	</table>
</div>								
<pre>
	<div v-html="resultReport" :title="compareResult"></div>
</pre>
							</div>
						</div>
					</aside>
					<section id="content">
						<div class="panel panel-default" style="padding-left: 0px; height: 100%;padding-bottom: 10px; margin-bottom: 10px;">
							<spring:message code="compare.detail" text="비교상세"/> : <span style="font-weight: bold;padding-left:5px;">{{compareSourceItem.name}}&lt;-&gt;{{compareTargetItem.name}}</span>
							<div class="panel-body" style="padding: 5px 5px 0px 5px;" :data-compare-mode="currentObjectType != 'table'?'text':''">	
								<div data-compare-area="grid">
									<div class="col-xs-6">
										<div style="margin:3px;">
											<div class="row">
												<span class="text-ellipsis">
													<spring:message code="column" text="desc"/>
													<span :class="compareSourceItem.remarks != compareTargetItem.remarks ?'error':''">
														{{compareSourceItem.remarks}}
													</span>
												</span></div>
											<div id="sourceColumn" class="row source-column">
											
											</div>
										</div>
									</div>
									<div class="col-xs-6">
										<div style="margin:3px;">
											<div class="row">
												<span class="text-ellipsis">
													<spring:message code="column" text="desc"/>
													<span :class="compareSourceItem.remarks != compareTargetItem.remarks ?'error':''">
														{{compareTargetItem.remarks}}
													</span>
												</span>
											</div>
											<div id="targetColumn" class="row target-column" >
											
											</div>
										</div>
									</div>
								</div>
								<div data-compare-area="text">
									<div id="compareTextArea" style="height: 100%;"></div>
								</div>
							</div>
						</div>
					</section>
				</div>
			</div>
		</div>
	</div>

<script id="downloadTemplate" type="text/varsql-template">
	<span><spring:message code="source" text="대상"/> : {{currentObject.sourceSchema}}</span>
	<span><spring:message code="target" text="타켓"/> :  {{currentObject.targetSchema}}</span>
</script>

</div>
<!-- /.row -->

<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/codemirror.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/sql.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/diff/diff_match_patch.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/merge/merge.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/fuse/fuse.min.js"></script>


<script>
VarsqlAPP.vueServiceBean( {
	el: '#varsqlVueArea'
	,data: {
		dbList : ${varsqlfn:objectToJson(dbList)}
		,dbListMap : {}
		,searchValue : ''
		,sourceSearchObj :''
		,targetSearchObj :''
		,diffItem : {
			source :''
			,sourceSchema : ''
			,target :''
			,targetSchema : ''
			,objectType : ''
		}
		,scrollSync : 'Y'
		,currentObjectType : ''
		,currentObject : {}
		,loading:false
		,isCompleteCompare :false
		,compareObjectName :''
		,compareObjectMetaResult :{}
		,objectList : []
		
		,sourceSchemaList :[]
		,targetSchemaList :[]
		,sourceItems : false	// 원천 item 목록.
		,targetItems : false
		,compareItem : {
			sourceNameMap : {}
			, targetNameMap : {}
			, objectColNameMap :{}
		}
		,resultInfo:{
			sourceCount :0
			,targetCount :0
		}
		,sameInfo : {cnt: 0 , htm :[]}
		,differentInfo : {cnt: 0 , htm :[]}
		,emptyInfo : {cnt: 0 , htm :[]}
		,resultReport : ''
		
		,compareSourceItem:{}
		,compareTargetItem:{}
	}
	,computed: {
		compareResult : function (){
			
			if(this.isCompleteCompare ===true){
				return '';
			}
			
			if(this.sourceItems !==false && this.targetItems !== false){
				var compareFn = this[this.diffItem.objectType+'Compare'];
				
				this.isCompleteCompare = true; 
				this.loading =false;
				var resultVal = '';
				if(compareFn){
					resultVal =compareFn.call(this);
				}else{
					resultVal = this.otherCompare.call(this);
				}
				
				this.resultInfo.sourceCount = this.sourceItems.length;
				this.resultInfo.targetCount = this.targetItems.length;
				
				var options = {
					  shouldSort: true,
					  threshold: 0.1,
					  location: 0,
					  distance: 100,
					  maxPatternLength: 32,
					  minMatchCharLength: 1,
					  keys: [
					    "name",
					    "remarks",
						"_lower_name",
					]
				};
						
				this.sourceSearchObj = new Fuse(this.sourceItems, options);
				this.targetSearchObj = new Fuse(this.targetItems, options);
				
				this.sameInfo.htm = this.sameInfo.htm.join('\n'); 
				this.differentInfo.htm =this.differentInfo.htm.join('\n');
				this.emptyInfo.htm=this.emptyInfo.htm.join('\n');
				
				this.resultReport = resultVal;
			}
			return '';
		}
	}
	,methods:{
		init : function(){
			var _self = this;
			
			var dbList = _self.dbList;
			var dbListMap = {};
			for(var i =0, len = dbList.length; i< len ;i++){
				var dbInfo = dbList[i];
				dbListMap[dbInfo.VCONNID] = dbInfo; 
			}
			
			_self.dbListMap = dbListMap;
			
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
			
			// result count click
			$('#compareResultArea').on('click.result.click','.result-count' , function (e){
				var sEle = $(this);
				
				var resultEle = sEle.closest('.result-wrapper');
				
				if(resultEle.hasClass('open')){
					resultEle.removeClass('open');
				}else{
					resultEle.addClass('open');
				}
			})
			
			var helperEle = $('#resizeHelper');
			
			var minW = 10, maxW = 90; 
			var compareWidth;
			$( "#resizable" ).resizable({
	            handles: "e",
	            helper :'aa',
	            containment : '#compareArea',
	            start : function ( e, ui){
	            	ui.helper.remove();
	            	compareWidth = $('#compareArea').width(); 
					helperEle.show()
					helperEle.css('left', ui.size.width+'px');
				}
	            ,resize: function( e, ui){
	            	
	            	if(compareWidth < ui.size.width ){
	            		helperEle.css('left', compareWidth+'px');
	            	}else{
	            		helperEle.css('left', ui.size.width +'px');
	            	}
	            }
	            ,stop : function (e, ui){
	            	helperEle.hide()
					var currentWidth = ui.size.width;
					var totWidth = compareWidth;
					var currWidthPercent = currentWidth /totWidth*100;
					
					if(minW > currWidthPercent){
						currWidthPercent = minW;
					}else if(maxW <currWidthPercent){
						currWidthPercent = maxW;
					}
					
					$('#resizable').css({
						left : '0px'
						,"width" : currWidthPercent+'%'
						,'height' :'100%'
					});
	                $("#content").css("width",  (100-(currWidthPercent+2)) +"%");
	                
	                if($.pubGrid('#sourceObjectMeta')) $.pubGrid('#sourceObjectMeta').resizeDraw();
	                if($.pubGrid('#targetObjectMeta')) $.pubGrid('#targetObjectMeta').resizeDraw();
	                if($.pubGrid('#sourceColumn')) $.pubGrid('#sourceColumn').resizeDraw();
	                if($.pubGrid('#targetColumn')) $.pubGrid('#targetColumn').resizeDraw();
	                
				}
	        });
		}
		// object search
		,searchObject : function (){
			if(this.isCompleteCompare !== true){
				return ; 
			}
			var schVal = this.searchValue.toLowerCase();
			
			var sList , tList;
			if($.trim(schVal)==''){
				sList = this.sourceItems;
				tList = this.targetItems;
			}else{
				sList = this.sourceSearchObj.search(schVal);
				tList = this.targetSearchObj.search(schVal);
			}
			
			$.pubGrid('#sourceObjectMeta').setData(sList,'reDraw'); 
			$.pubGrid('#targetObjectMeta').setData(tList,'reDraw'); 
		}
		// db object search.
		,getObjectList : function(){
			var _self = this; 
			
			var diffItem = _self.diffItem;
			if(diffItem.source ==''){
				VARSQLUI.toast.open('대상을 선택하세요.');
				return ;
			}
			if(diffItem.target ==''){
				VARSQLUI.toast.open('타켓을 선택하세요.');
				return ;
			}
			
			var objectType = diffItem.objectType;
			if(objectType ==''){
				VARSQLUI.toast.open('objectType을 선택하세요.');
				return ;
			}
			
			_self.initDetailArea();
			
			
			this.loading =true; 
			_self.targetItems = false;
			_self.sourceItems = false;
			
			_self.resultInfo.sourceCount = 0;
			_self.resultInfo.targetCount = 0;
			
			_self.resultReport = '';
			_self.sameInfo ={cnt: 0 , htm :[]};
			_self.differentInfo ={cnt: 0 , htm :[]};
			_self.emptyInfo ={cnt: 0 , htm :[]};
			
			_self.compareObjectName = '';
			_self.isCompleteCompare = false; 
			_self.compareObjectMetaResult = {};
			_self.currentObjectType = objectType;
			_self.currentObject = VARSQL.util.objectMerge ({},diffItem);
			
			// source data load
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/diff/objectList'}
				,loadSelector : '#sourceObjectMeta'
				,data :  {
					vconnid : diffItem.source 
					,objectType : objectType
					,schema : diffItem.sourceSchema
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
					,schema : diffItem.targetSchema
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
		//상세 비교 
		,initDetailArea : function (){
			
			var sourceMetaGridObj = $.pubGrid('#sourceColumn');
			if(sourceMetaGridObj)sourceMetaGridObj.destroy();
			
			var targetMetaGridObj = $.pubGrid('#targetColumn');
			if(targetMetaGridObj)targetMetaGridObj.destroy();
			
			this.otherMetaView({});
		}
		// 테이블 비교
		,tableCompare : function (){
			var _self = this; 
			var sourceItems =this.sourceItems
				,targetItems = this.targetItems;
			
			var compareColKey = ['name','typeAndLength','constraints','defaultVal','nullable','comment'];
			var compareColKeyLength =compareColKey.length;
			var maxLen = Math.max(sourceItems.length,targetItems.length);
			
			var sourceNameMap = {}, targetNameMap={}, targetCompareNameMap={};
			var sourceItem, targetItem;
			for(var i =0 ;i < maxLen; i++){
				sourceItem = sourceItems[i];
				if(sourceItem){
					sourceItem._lower_name = VARSQL.util.removeUnderscore(sourceItem.name, true);
					sourceNameMap[sourceItem.name] = sourceItem;
				}
				
				targetItem = targetItems[i];
				if(targetItem) {
					targetItem._lower_name = VARSQL.util.removeUnderscore(targetItem.name, true);
					
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
						_self.differentInfo.htm.push('<a class="table-info table-name" data-table-name="'+key+'">'+key+'</a>');
						++_self.differentInfo.cnt;
						compareResult.push(compareLog.join(''))
					}else {
						_self.sameInfo.htm.push(key);
						++_self.sameInfo.cnt;
					}
				}else {
					_self.emptyInfo.htm.push(key);
					++_self.emptyInfo.cnt;
					compareResult.push('<span style="color:#f60b0b;">타켓에 <a href="javascript:;" class="table-name" data-table-name="'+key+'">'+key+'</a> 테이블이 존재 하지 않습니다 </span>');
				}
				
				delete targetCompareNameMap[key];
			}
			
			this.compareItem.objectColNameMap =objectColNameMap;
			
			for(var key in targetCompareNameMap){
				compareResult.push('<span style="color:#b55e34;">대상에 [ <a href="javascript:;" class="table-name" data-table-name="'+key+'">'+key+ '</a>] 테이블이 존재 하지 않습니다.</span>');
			}
			
			return compareResult.join('\n');
			
		}
		,isScrollSync : function (){
			return this.scrollSync =='Y';
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
					,selectionMode :'row'
					,headerOptions:{
						resize:{
							update :  function (item){
								$.pubGrid('#targetObjectMeta').setColumnWidth(item.index , item.width);
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
						click : function (rowInfo){
							_self.tableObjectMetaView(rowInfo.item);
						}
					}
					,scroll :{
						vertical : {
							onUpdate : function (item){
								if(_self.isScrollSync()){
									$.pubGrid('#targetObjectMeta').moveVScrollPosition(item.scrollTop,'',false);
								}
							}
						}
						,horizontal :{
							onUpdate : function (item){ 
								if(_self.isScrollSync()){
									$.pubGrid('#targetObjectMeta').moveHScrollPosition(item.scrollLeft,'',false);
								}
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
					,selectionMode :'row'
					,headerOptions:{
						resize:{
							update :  function (item){
								$.pubGrid('#sourceObjectMeta').setColumnWidth(item.index , item.width);
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
						click : function (rowInfo){
							_self.tableObjectMetaView(rowInfo.item);
						}
					}
					,scroll :{
						vertical : {
							onUpdate : function (item){
								if(_self.isScrollSync()){
									$.pubGrid('#sourceObjectMeta').moveVScrollPosition(item.scrollTop, '',false);
								}
								
							}
						}
						,horizontal :{
							onUpdate : function (item){ 
								if(_self.isScrollSync()){
									$.pubGrid('#sourceObjectMeta').moveHScrollPosition(item.scrollLeft,'',false);
								}
							}
						}
					}
				});
			}
		}
		// 테이블  column 비교. 
		, tableObjectMetaView:  function (viewItem,selectItemCompareFlag){
			
			var tblName = viewItem;
			if(VARSQL.isObject(viewItem)){
				tblName = viewItem.name;
			}
			
			this.compareObjectName = tblName;
			
			var compareSourceColList;
			var compareTargetColList;
			
			if(!VARSQL.isUndefined(this.compareObjectMetaResult[tblName])){
				compareSourceColList =this.compareObjectMetaResult[tblName].source;
				compareTargetColList =this.compareObjectMetaResult[tblName].target;
				
				this.compareSourceItem = this.compareItem.sourceNameMap[tblName];
				this.compareTargetItem =this.compareItem.targetNameMap[tblName];
				
			}else{
				var sourceItem; 
				var targetItem;
				
				if(selectItemCompareFlag === true){
					var sourceSelArr = $.pubGrid('#sourceObjectMeta').getSelectionItem('name');
					sourceItem = VARSQL.util.objectMerge({},this.compareItem.sourceNameMap[(sourceSelArr.length > 0 ?sourceSelArr[0].name :'')]||{});
					var targetSelArr = $.pubGrid('#targetObjectMeta').getSelectionItem('name');
					targetItem = VARSQL.util.objectMerge({},this.compareItem.targetNameMap[(targetSelArr.length > 0 ?targetSelArr[0].name :'')]||{});
				}else{
					sourceItem = this.compareItem.sourceNameMap[tblName]||{}; 
					targetItem = this.compareItem.targetNameMap[tblName]||{};
				}
				
				this.compareSourceItem = sourceItem;
				this.compareTargetItem =targetItem;
				
				var sourceColList = sourceItem.colList||[];
				var targetColList = targetItem.colList||[];
				
				compareSourceColList =sourceColList;
				compareTargetColList =targetColList;
			
			
				if( sourceColList.length > 0  && targetColList.length > 0){
					
					compareSourceColList =[];
					compareTargetColList =[];
					
					var maxLen = Math.max(sourceColList.length,targetColList.length);
					
					var sourceColMap={};
					var targetColMap={};
					
					for(var i =0 ; i < maxLen; i++){
						var sourceColItem = sourceColList[i];
						if(sourceColItem){
							sourceColMap[sourceColItem.name] = sourceColItem;
						}
					
						var targetColItem= targetColList[i];
						if(targetColItem){
							targetColMap[targetColItem.name] = targetColItem;
						}
					}
					
					for(var key in sourceColMap){
						var sourceCol = sourceColMap[key];
						var targetCol = targetColMap[key];
						
						if(selectItemCompareFlag ===true ){
							delete sourceCol['__ne'];
							if(targetCol) delete targetCol['__ne'];
						}
						
											
						if(VARSQL.isUndefined(targetCol)){
							targetCol = {'__ne' :true};
						}else{
							delete targetColMap[key];
						}
						
						var equlasFlag = true; 
						for(var colKey in sourceCol){
							if(colKey.lastIndexOf('_ne') == (colKey.length-3)){
								delete sourceCol[colKey];
								continue; 
							}
							
							sourceCol[colKey+'_ne'] =false;
							if(targetCol[colKey+'_ne']) targetCol[colKey+'_ne'] =false;
							
							var targetColVal = targetCol[colKey]; 
							if(!VARSQL.isUndefined(targetColVal)){
								if($.trim(sourceCol[colKey]) != $.trim(targetColVal)){
									sourceCol[colKey+'_ne'] = true;
									targetCol[colKey+'_ne'] = true;
									equlasFlag = false; 
								}
							}
						}
						
						if(!equlasFlag){
							sourceCol['__ne'] = true;
							targetCol['__ne'] = true
						}
						
						compareSourceColList.push(sourceCol);
						compareTargetColList.push(targetCol);
					}
					
					for(var key in targetColMap){
						compareTargetColList.push(targetColMap[key]);
					}
					
					if(selectItemCompareFlag !==true ){
						this.compareObjectMetaResult[tblName] ={
							source : compareSourceColList
							,target : compareTargetColList
						};
					}
				}
			}
			
			var errorFormatter =  function (colItemInfo){
				if(colItemInfo.item[colItemInfo.colInfo.key +'_ne']===true){
					return '<span style="color:red;">'+(colItemInfo.item[colItemInfo.colInfo.key]||'')+'</span>';
				}else{
					if(VARSQL.isUndefined(colItemInfo.item[colItemInfo.colInfo.key])){
						return '<span style="color:red;">-----</span>';;
					}else{
						return colItemInfo.item[colItemInfo.colInfo.key];
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
								$.pubGrid('#targetColumn').setColumnWidth(item.index , item.width);
							}
						}
					}
					,scroll :{
						isPreventDefault : false
						,vertical : {
							onUpdate : function (item){
								$.pubGrid('#targetColumn').moveVScrollPosition(item.scrollTop,'',false);
							}
							,speed : 5
						}
						,horizontal :{
							onUpdate : function (item){ 
								$.pubGrid('#targetColumn').moveHScrollPosition(item.scrollLeft,'',false);
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
								$.pubGrid('#sourceColumn').setColumnWidth(item.index , item.width);
							}
						}
					}
					,scroll :{
						isPreventDefault :false
						,vertical : {
							onUpdate : function (item){
								$.pubGrid('#sourceColumn').moveVScrollPosition(item.scrollTop,'',false);
							}
							,speed : 5
						}
						,horizontal :{
							onUpdate : function (item){ 
								$.pubGrid('#sourceColumn').moveHScrollPosition(item.scrollLeft,'',false);
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
					,selectionMode :'row'
					,headerOptions:{
						resize:{
							update :  function (item){
								$.pubGrid('#targetObjectMeta').setColumnWidth(item.index , item.width);
							}
						}
					}
					,asideOptions :{
						lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
					}
					,tColItem : [
						{key :'name', label:'Object Name', width:200, sort:true}
						,{key :'remarks', label:'Desc', sort:true}
					]
					,tbodyItem :itemArr
					,rowOptions :{
						click : function (rowInfo){
							_self.otherMetaView(rowInfo.item);
						}
					}
					,scroll :{
						vertical : {
							onUpdate : function (item){
								if(_self.isScrollSync()){
									$.pubGrid('#targetObjectMeta').moveVScrollPosition(item.scrollTop,'',false);
								}
							}
						}
						,horizontal :{
							onUpdate : function (item){
								if(_self.isScrollSync()){
									$.pubGrid('#targetObjectMeta').moveHScrollPosition(item.scrollLeft,'',false);
								}
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
					,selectionMode :'row'
					,headerOptions:{
						resize:{
							update :  function (item){
								$.pubGrid('#sourceObjectMeta').setColumnWidth(item.index , item.width);
							}
						}
					}
					,asideOptions :{
						lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
					}
					,tColItem : [
						{key :'name', label:'Object Name', width:200, sort:true}
						,{key :'remarks', label:'Desc', sort:true}
					]
					,tbodyItem :itemArr
					,rowOptions :{
						click : function (rowInfo){
							_self.otherMetaView(rowInfo.item);
						}
					}
					,scroll :{
						vertical : {
							onUpdate : function (item){
								if(_self.isScrollSync()){
									$.pubGrid('#sourceObjectMeta').moveVScrollPosition(item.scrollTop, '',false);
								}
							}
						}
						,horizontal :{
							onUpdate : function (item){
								if(_self.isScrollSync()){
									$.pubGrid('#sourceObjectMeta').moveHScrollPosition(item.scrollLeft,'',false);
								}
							}
						}
					}
				});
			}
		}
		,otherCompare : function (){
			var _self = this; 
			
			var sourceItems =this.sourceItems
				,targetItems = this.targetItems;
		
			var maxLen = Math.max(sourceItems.length,targetItems.length);
			
			var sourceNameMap = {}, targetNameMap={}, targetCompareNameMap={};
			var sourceItem, targetItem;
			for(var i =0 ;i < maxLen; i++){
				sourceItem = sourceItems[i];
				if(sourceItem){
					sourceItem._lower_name = VARSQL.util.removeUnderscore(sourceItem.name, true);
					sourceNameMap[sourceItem.name] = sourceItem;
				}
				
				targetItem = targetItems[i];
				if(targetItem) {
					targetItem._lower_name = VARSQL.util.removeUnderscore(targetItem.name, true);
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
					
					for(var itemKey in sourceItem){
						
						if(sourceItem[itemKey] != targetItem[itemKey]){
							compareFlag = true;
							
							if(itemKey =='createScript'){
								compareLog.push('['+itemKey+'] 같지 않습니다.\n');
							}else{
								compareLog.push('['+itemKey+'] 같지 않습니다. 대상 : '+sourceItem[itemKey] + ' 타켓 : '+targetItem[itemKey] +'\n');
							}
						}
					}
					
					compareLog.push('</div>');
					
					if(compareFlag){
						_self.differentInfo.htm.push('<a class="table-info object-name" data-object-name="'+key+'">'+key+'</a>');
						++_self.differentInfo.cnt;
						compareResult.push(compareLog.join(''))
					}else {
						_self.sameInfo.htm.push(key);
						++_self.sameInfo.cnt;
					}
				}else {
					_self.emptyInfo.htm.push(key);
					++_self.emptyInfo.cnt;
					compareResult.push('<span style="color:#f60b0b;">타켓에 <a href="javascript:;" class="object-name" data-object-name="'+key+'">'+key+'</a> 존재 하지 않습니다 </span>');
				}
				
				delete targetCompareNameMap[key];
			}
			
			this.compareItem.objectColNameMap =objectColNameMap;
			
			for(var key in targetCompareNameMap){
				compareResult.push('<span style="color:#b55e34;">대상에 [ <a href="javascript:;" class="object-name" data-object-name="'+key+'">'+key+ '</a>] 존재 하지 않습니다.</span>');
			}
			
			return compareResult.join('\n');
		}
		,otherMetaView :  function (viewItem ,selectItemCompareFlag ){
			var objName = viewItem;
			if(typeof viewItem==='object'){
				objName = viewItem.name;
			}
			
			this.compareObjectName = objName;
			
			var sourceItem; 
			var targetItem;
			
			if(selectItemCompareFlag === true){
				var sourceSelArr = $.pubGrid('#sourceObjectMeta').getSelectionItem('name');
				sourceItem = VARSQL.util.objectMerge({},this.compareItem.sourceNameMap[(sourceSelArr.length > 0 ?sourceSelArr[0].name :'')]||{});
				var targetSelArr = $.pubGrid('#targetObjectMeta').getSelectionItem('name');
				targetItem = VARSQL.util.objectMerge({},this.compareItem.targetNameMap[(targetSelArr.length > 0 ?targetSelArr[0].name :'')]||{});
			}else{
				sourceItem = this.compareItem.sourceNameMap[objName]||{}; 
				targetItem = this.compareItem.targetNameMap[objName]||{};
			}
			
			
			this.compareSourceItem = sourceItem;
			this.compareTargetItem =targetItem;
			
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
		// object list select item compare
		,selectItemCompare : function (){
			if(this.isCompleteCompare ==true){
				if(this.diffItem.objectType =='table'){
					this.tableObjectMetaView('',true);
				}else{
					this.otherMetaView('',true);
				}
			}
		}
		,sourceChange : function (val){
			var _self = this; 
			
			_self.diffItem.sourceSchema = _self.dbListMap[val].VDBSCHEMA;
			
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/diff/objectType'}
				,data : {
					vconnid : val
				}
				,loadSelector : '#varsqlVueArea'
				,success: function(resData) {
					_self.objectList = resData.items;
					_self.sourceSchemaList = resData.customs.schemaInfo;
				}
			})
		}
		,targetChange : function (val){
			var _self = this; 
			
			_self.diffItem.targetSchema = _self.dbListMap[val].VDBSCHEMA;
			
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/diff/objectType'}
				,loadSelector : '#varsqlVueArea'
				,data : {
					vconnid : val
				}
				,success: function(resData) {
					var schemaList = resData.customs.schemaInfo; 
					
					if(_self.diffItem.sourceSchema != ''){
						for(var i =0, len =schemaList.length; i< len; i++ ){
							if(_self.diffItem.sourceSchema==schemaList[i]){
								_self.diffItem.targetSchema = _self.diffItem.sourceSchema;
								break; 
							}
						}
					}
					
					_self.targetSchemaList = schemaList;
				}
			})
		}
		// source shcema change
		,sourceSchemaChange : function(sourceSchema){
			if(sourceSchema != ''){
				for(var i =0, len =this.targetSchemaList.length; i< len; i++ ){
					if(sourceSchema==this.targetSchemaList[i]){
						this.diffItem.targetSchema = sourceSchema;
						break; 
					}
				}
			}
		}
		,resultDownload : function (){
			
			if(this.isCompleteCompare !== true){
				return ; 
			}
			var headerHtml = $('#downloadTemplate').wrapAll('<div></div>').html();
			
			
			var params ={
				exportType :'text' 
				,fileName : 'dbCompareResult.html'
				,content : headerHtml+'<pre>' + $('#compareResultArea').html() +'</pre>'
			};
			
			VARSQL.req.download({
				type: 'post'
				,url: '/download.varsql'
				,params: params
			});
		}
	}
});

</script>