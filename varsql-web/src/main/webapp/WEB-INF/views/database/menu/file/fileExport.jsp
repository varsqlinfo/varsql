<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div id="<varsql:namespace/>" class="menu-popup-layer wh100">
	
	<div class="process-body col-xs-12 scroll-y" style="position:relative;">
		<div class="process-step" :class="selectMenu==1?'active':''">
			<div class="col-xs-12">
				<div class="field-group">
					<h2 class="process-header">내보내기 선택</h2>
					<div class="process-header-desc">작업의 유형을 아래에서 선택해주세요.</div>
					<ul class="process-type-select">
						<li>
							<label class="checkbox-container">INSERT
							  <input type="radio" v-model="<varsql:namespace/>importType" value="sql" checked="checked">
							  <span class="radiomark"></span>
							</label>
							<div class="checkbox-container-desc">SQL 파일로 내보내기</div>
						</li>
						<li>
							<label class="checkbox-container">XML
							  <input type="radio" v-model="<varsql:namespace/>importType" value="xml">
							  <span class="radiomark"></span>
							</label>
							<div class="checkbox-container-desc">XML 파일로 내보내기</div>
						</li>
						<li>
							<label class="checkbox-container">JSON
							  <input type="radio" v-model="<varsql:namespace/>importType" value="json">
							  <span class="radiomark"></span>
							</label>
							<div class="checkbox-container-desc">JSON 파일로 내보내기</div>
						</li>
						<li>
							<label class="checkbox-container">CSV
							  <input type="radio" v-model="<varsql:namespace/>importType" value="csv">
							  <span class="radiomark"></span>
							</label>
							<div class="checkbox-container-desc">CSV 파일로 내보내기</div>
						</li>
					</ul>
				</div>
			</div>
		</div>
		
		<div class="process-step" :class="step==2?'active':''">
			<div class="col-xs-12">
				<div class="process-title"><spring:message code="msg.export.spec.step2" /></div>

				<c:if test="${schemaInfo ne ''}">
					<div style="padding: 5px 0px 0px;">
						<label class="control-label" style="font-weight: bold;margin-right:5px;"><spring:message code="label.schema" /> : </label>
						<select v-model="selectSchema" @change="getTableList()" style="width: calc(100% - 55px);">
							<c:forEach var="item" items="${schemaList}" begin="0" varStatus="status">
								<option value="${item}">${item}</option>
							</c:forEach>
						</select>
					</div>
				</c:if>
				<div class="process-desc" style="padding: 5px 0px 5px;"><spring:message code="msg.table.dbclick.move" /></div>
			</div>
			<div class="wh100-relative table-select-area" style="float: left;">
				<div class="col-xs-5">
					<div class="top-select mbottom-10 fb tl mRight-20"><spring:message code="label.table" /></div>
					<div>
						<ul id="source" class="pub-select-source pub-multiselect-area" style="height: 200px;width: 100%;">
						</ul>
					</div>
				</div>
				<div class="col-xs-2 text-center">
					<div style="position:relative;top:100px;">
						<a href="javascript:;" @click="selectTableObj.sourceMove()"><span class="fa fa-forward"></span></a>
						<br/>
						<a href="javascript:;" @click="selectTableObj.targetMove()"><span class="fa fa-backward"></span></a>
					</div>
				</div>
				<div class="col-xs-5">
					<div class="top-select mbottom-10 fb tl mRight-20"><spring:message code="msg.select.value" /></div>
					<div>
						<ul id="target" class="pub-select-target pub-multiselect-area" style="height: 200px;width: 100%;">

						</ul>
					</div>
					<div class="pull-right">
						<a href="javascript:;" class="fa fa-arrow-up"  @click="selectTableObj.move('up')"><spring:message code="up" /></a>
						<span style="padding-left:10px;"></span>
						<a href="javascript:;" class="fa fa-arrow-down"  @click="selectTableObj.move('down')"><spring:message code="down" /></a>
						<span style="padding-right:10px;"></span>
					</div>
				</div>
			</div>
		</div>
		
		<step-button :step.sync="step" :end-step="endStep" ref="stepButton"></step-button>
	</div>
</div>

<script>

VarsqlAPP.vueServiceBean({
	el: '#<varsql:namespace/>'
	,data: {
		selectMenu : 1
		,<varsql:namespace/>importType : ''
		, step : 1
		, endStep : 3
		, selectSchema : ''
		, selectTableObj : {}
		, selectColumnObj : {}
		, userSetting : VARSQL.util.objectMerge({exportName:'table_spec',sheetFlag:'false', schema:'${schemaInfo}', tables:[]},${userSettingInfo})
		, detailItem :{}
	}
	,mounted : function (){
		
	}
	,methods:{
		init : function (){
			this.selectSchema = this.userSetting.schema;
			this.setUserConfigInfo();
		}
		,selectStep : function (step){
			this.$refs.stepButton.move(step);
		}
		//step 선택
		,moveStep : function (step){
			this.step = step;
		}
		// 완료
		,complete : function (){
			var _self = this;
			if(_self.selectTableObj.getTargetItem().length < 1){
				this.step=2;
    			alert('<spring:message code="msg.table.select" />');
    			return ;
    		}

			if(_self.selectColumnObj.getTargetItem().length < 1){
    			alert('<spring:message code="msg.column.select" />');
    			return ;
    		}

			_self.exportInfo();
		}
		,exportInfo : function (){
			var _self = this;

			var info = $("#firstConfigForm").serializeJSON();

			var prefVal = {
				exportName : _self.userSetting.exportName
				,schema : _self.selectSchema
				,sheetFlag : _self.userSetting.sheetFlag
				,addTableDefinitionFlag : _self.userSetting.addTableDefinitionFlag
				,tables : _self.selectTableObj.getTargetItem()
				,columns: _self.selectColumnObj.getTargetItem()
			};

			var param = {
				prefVal : JSON.stringify(prefVal)
				,schema : _self.selectSchema
				,conuid : '${param.conuid}'
			};

			VARSQL.req.download({
				type: 'post'
				,url: {type:VARSQL.uri.database, url:'/tools/export/spec/tableExport'}
				,params : param
			});
		}
		,setUserConfigInfo : function (){
			var _self = this;

			_self.setTableSelect();
			_self.getTableList();

		}
		,getTableList : function(){
			var _self = this;

			var param = {
				conuid : '${param.conuid}'
				,schema : this.selectSchema
			}

			VARSQL.req.ajax({
				url : {type:VARSQL.uri.database, url:'/tools/export/specMain/tableList'}
				,data: param
				,loadSelector : '.table-select-area'
				,success:function (resData){
					var list = resData.items;

					_self.selectTableObj.setItem('source',list);

					var beforeTableList = [];
					if(_self.selectSchema == _self.userSetting.schema){
						var beforeTables = _self.userSetting.tables;
						var beforeTablelen = beforeTables.length;
						if(beforeTablelen > 0){
							var tableNameObj ={};
							for(var i =0; i <beforeTablelen; i++){
								var item = beforeTables[i];
								tableNameObj[item.name] = i;
							}

							for(var i =0,len = list.length; i <len; i++){
								var item = list[i];
								var beforeTableIdx = tableNameObj[item.name];
								if(!VARSQL.isUndefined(beforeTableIdx)){
									beforeTableList.push(beforeTables[beforeTableIdx]);
								}
							}
						}
						_self.selectTableObj.setItem('target',beforeTableList);
					}else{
						_self.selectTableObj.setItem('target',[]);
					}
				}
			});
		}
		// table select info
		,setTableSelect: function (){
			var _self = this;

			var tmpSourceItem = [] , paramSourceItem=[];

			_self.selectTableObj= $.pubMultiselect('#source', {
				targetSelector : '#target'
				,addItemClass:'text_selected'
				,useMultiSelect : true
				,duplicateCheck : true
				,useDragMove :false
				,sourceItem : {
					optVal : 'name'
					,optTxt : 'name'
					,emptyMessage : '<spring:message code="msg.export.spec.schema.select" />'
					,items : paramSourceItem
				}
				,targetItem : {
					optVal : 'name'
					,optTxt : 'name'
					,items : []
					,click: function (e, sItem){
						_self.detailItem = sItem;
					}
				}
			});
		}
	}
});

</script>