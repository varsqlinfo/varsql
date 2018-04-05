<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div data-curr-step="1">
	<div class="process-step-wrapper" style="height:280px;overflow:auto;">
		<div class="process-step" data-step="1">
			<div>
				<div class="col-xs-12">
					<label class="control-label"><spring:message code="msg.table.export.info" /></label>
				</div>
				<div class="col-xs-12">
					<form id="firstConfigForm" name="firstConfigForm" class="form-horizontal bv-form eportalForm">
						<div class="form-group">
							<label class="col-xs-3 control-label">파일명</label>
							<div class="col-xs-9">
								<input class="form-control text required input-sm" id="export_name" name="export_name" value="table_spec" >
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label">다중시트여부</label>
							<div class="col-xs-9">
								<label class="radio-inline"> 
									<input type="radio" name="sheet_flag" value="false" checked>단일 
								</label>
								<label class="radio-inline"> 
									<input type="radio" name="sheet_flag" value="true"> 다중
								</label>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		
		<div class="process-step" data-step="2">
			<div>
				<div class="col-xs-12">
					<label class="control-label"><spring:message code="msg.table.dbclick.move" /></label>
				</div>
				<div class="col-xs-5">
					<div class="top-select mbottom-10 fb tl mRight-20"><spring:message code="label.table" /></div>
					<div>
						<ul id="source" class="pub-select-source pub-multiselect-area" style="height: 200px;width: 100%;">
						</ul>
					</div>
				</div>
				<div class="col-xs-2 text-center">
					<div style="position:relative;top:100px;">
						<a href="javascript:;" class="add-item"><span class="glyphicon glyphicon-forward"></span></a>
						<br/>
						<a href="javascript:;" class="cancle-item"><span class="glyphicon glyphicon-backward"></span></a>
					</div>
				</div>
				<div class="col-xs-5">
					<div class="top-select mbottom-10 fb tl mRight-20"><spring:message code="msg.select.value" /></div>
					<div>
						<ul id="target" class="pub-select-target pub-multiselect-area" style="height: 200px;width: 100%;">
						
						</ul>
					</div>
					<div class="pull-right">
						<a href="javascript:;" class="item-move" mode="up">위</a>
						<span style="padding-left:10px;"></span>
						<a href="javascript:;" class="item-move" mode="down">아래</a>
						<span style="padding-right:10px;"></span>
					</div>
				</div>
			</div>
		</div>
			
		<div class="process-step" data-step="3">
			<div>
				<div class="col-xs-12">
					<label class="control-label"><spring:message code="msg.column.dbclick.move" /></label>
				</div>
				<div style="height:245px;;border:1px solid #ddd;margin:3px;overflow:hidden;">
					<div class="col-xs-3">
						<div class="top-select mbottom-10 fb tl mRight-20"><spring:message code="label.table" /></div>
						<div>
							<ul id="columnSource" class="pub-select-source pub-multiselect-area" style="height: 200px;width: 100%;">
							</ul>
						</div>
					</div>
					<div class="col-xs-1 text-center">
						<div style="position:relative;top:100px;">
							<a href="javascript:;" class="add-item2"><span class="glyphicon glyphicon-forward"></span></a>
							<br/>
							<a href="javascript:;" class="cancle-item2"><span class="glyphicon glyphicon-backward"></span></a>
						</div>
					</div>
					<div class="col-xs-3">
						<div class="top-select mbottom-10 fb tl mRight-20"><spring:message code="msg.select.value" /></div>
						<div>
							<ul id="columnTarget" class="pub-select-target pub-multiselect-area" style="height: 200px;width: 100%;">
							
							</ul>
						</div>
						<div class="pull-right">
							<a href="javascript:;" class="item-move2" mode="up">위</a>
							<span style="padding-left:10px;"></span>
							<a href="javascript:;" class="item-move2" mode="down">아래</a>
							<span style="padding-right:10px;"></span>
						</div>
					</div>
					<div class="col-xs-5">
						<form id="mappingForm" name="mappingForm" class="form-horizontal bv-form eportalForm">
							<div class="col-xs-12" style="padding:0px 0px 5px 0px;">
								컬럼 상세정보
							</div>
							<div class="form-group">
								<label class="col-xs-3 control-label padding0">컬럼</label>
								<div class="col-xs-9">
									<input class="form-control text required input-sm" v-model="detailItem.code" disabled="disabled">
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-3 control-label padding0">컬럼명</label>
								<div class="col-xs-9">
									<input class="form-control text required input-sm" v-model="detailItem.title" >
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-3 control-label padding0">사이즈</label>
								<div class="col-xs-9">
									<input class="form-control text required input-sm" v-model="detailItem.width">
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="col-xs-12">
		<div class="pull-right">
			<button class="btn btn-default btn-sm disabled" data-btn-mode="prev" type="button"><spring:message code="label.prev" /></button>
			<button class="btn btn-default btn-sm" data-btn-mode="next" type="button"><spring:message code="label.next" /></button>
			<button class="btn btn-default btn-sm disabled" data-btn-mode="complete" type="button"><spring:message code="label.complete" /></button>
			<button class="btn btn-default btn-sm" data-btn-mode="cancle"  type="button"><spring:message code="label.cancle" /></button>
		</div>
	</div>
</div>
	
<script>

var mappingFormVue =  new Vue({
	el: '#mappingForm'
	,created:function(){
		
	}
	,data: {
		detailItem :{}
	}
})

var tableExport = {
	selectTableObj : []
	,selectColumnObj : []
	,userSettring : ${userSettingInfo}
	,endStep : 3
	,init : function (){
		var _self = this; 
		_self.initEvt();
		
		_self.setUserConfigInfo();
	}
	,initEvt : function (){
		var _self = this; 
		
		//추가
		$('.add-item').on('click',function (){
			_self.selectTableObj.sourceMove();
		});
		//취소
		$('.cancle-item').on('click',function (){
			_self.selectTableObj.targetMove();
		});

		//아래위 이동
		$('.item-move').on('click',function (){
			_self.selectTableObj.move($(this).attr('mode'));
		});
		
		/* column setting event */
		//추가
		$('.add-item2').on('click',function (){
			_self.selectColumnObj.sourceMove();
		});
		//취소
		$('.cancle-item2').on('click',function (){
			_self.selectColumnObj.targetMove();
		});

		//아래위 이동
		$('.item-move2').on('click',function (){
			_self.selectColumnObj.move($(this).attr('mode'));
		});
		
		var currStep =1
			,endStep =_self.endStep; 
		$('.btn[data-btn-mode]').on('click',function (){
			var mode = $(this).data('btn-mode');
			
			 switch (mode) {
	             case 'prev':
					if(currStep > 1){
						currStep = currStep-1;
						if(currStep==1){
							$('[data-btn-mode="prev"]').addClass('disabled');
						}
						
						$('[data-curr-step]').attr('data-curr-step',currStep);
						
						$('[data-btn-mode="next"]').removeClass('disabled');
					}
					return;
	             case 'next':
	            	var nextStep = currStep +1;
	            	
	            	if(nextStep <= endStep){
	            		
	            		if(currStep==2 && _self.selectTableObj.getTargetItem().length < 1){
	            			alert('테이블을 선택화세요.');
	            			return ; 
	            		}
	            		
	            		$('[data-btn-mode="prev"]').removeClass('disabled');
	            		currStep = nextStep;
	            		
	            		$('[data-curr-step]').attr('data-curr-step',currStep);
	            		
	            		if(currStep==endStep){
							$('[data-btn-mode="next"]').addClass('disabled');
							$('[data-btn-mode="complete"]').removeClass('disabled');
						}
	            	}
	            	 return;
	            	 
	             case 'complete':
	            	 
					if(_self.selectTableObj.getTargetItem().length < 1){
            			alert('테이블을 선택화세요.');
            			return ; 
            		}
					
					if(_self.selectColumnObj.getTargetItem().length < 1){
            			alert('컬럼을 선택화세요.');
            			return ; 
            		}
					
					_self.exportInfo();
					return;
	             case 'cancle':
	            	 currStep=1;
	            	 $('[data-btn-mode="prev"], [data-btn-mode="complete"]').addClass('disabled');
	            	 $('[data-btn-mode="next"]').removeClass('disabled');
	            	 $('[data-curr-step]').attr('data-curr-step',currStep);
	                 return;
	             default: // 0-9, a-z
	                
	                 break;
	         }
		});
	}
	,setUserConfigInfo : function (){
		var _self = this; 
		
		$('#export_name').val((_self.userSettring.exportName ||'table-spec'));
		$('[name="sheet_flag"][value="'+_self.userSettring.sheetFlag+'"]').prop('checked',true);

		_self.setTableSelect();
		_self.setColumnSelect();
		
	}
	//내보내기
	,exportInfo : function (){
		var _self = this; 
		
		var info = $("#firstConfigForm").serializeJSON();
		
		var prefVal = {
			exportName : info.export_name
			,sheetFlag : info.sheet_flag
			,tables : _self.selectTableObj.getTargetItem()
			,columns: _self.selectColumnObj.getTargetItem()
		};
		
		var param = {
			prefVal : JSON.stringify(prefVal)
			,conuid : '${param.conuid}'
		};
		
		VARSQL.req.download({
			type: 'post'
			,url: {gubun:VARSQL.uri.database, url:'/tools/export/tableExport.vsql'}
			,params : param
		});
	}
	// table select info
	,setTableSelect: function (){
		var _self = this; 
		
		var tmpSourceItem = [] , paramSourceItem=${varsqlfn:objectToJson(tableInfo)}; 

		_self.selectTableObj= $.pubMultiselect('#source', {
			targetSelector : '#target'
			,addItemClass:'text_selected'
			,useMultiSelect : true
			,duplicateCheck : true
			,useDragMove :false
			,sourceItem : {
				optVal : 'name'
				,optTxt : 'name'
				,items : paramSourceItem
			}
			,targetItem : {
				optVal : 'name'
				,optTxt : 'name'
				,items : (_self.userSettring.tables ||[])
				,click: function (e, sItem){
					//console.log(e,sEle);
				}
			}
		}); 

		//_self.selectObj.setItem('target',tmpSourceItem);
	}
	//컬럼 select
	,setColumnSelect: function (){
		var _self = this; 
		
		var tmpSourceItem = [] , paramSourceItem=${varsqlfn:objectToJson(columnInfo)}; 
		
		var userSettingColumn = _self.userSettring.columns;
		userSettingColumn = $.isArray(userSettingColumn) &&  userSettingColumn.length > 0 ? userSettingColumn : paramSourceItem;

		_self.selectColumnObj= $.pubMultiselect('#columnSource', {
			targetSelector : '#columnTarget'
			,addItemClass:'text_selected'
			,useMultiSelect : true
			,duplicateCheck : true
			,useDragMove :false
			,sourceItem : {
				optVal : 'code'
				,optTxt : 'code'
				,items : paramSourceItem
			}
			,targetItem : {
				optVal : 'code'
				,optTxt : 'code'
				,items : userSettingColumn
				,click: function (e, sItem){
					sItem.width = (sItem.width ||sItem.custom.width);
					mappingFormVue.detailItem = sItem;
				}
			}
		}); 

		//_self.selectObj.setItem('target',tmpSourceItem);
	}
}

$(document).ready(function (){
	tableExport.init();
})



</script>
