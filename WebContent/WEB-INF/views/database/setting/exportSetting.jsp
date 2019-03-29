<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<div class="col-xs-12">
	<div>
		* 목록을  더블클릭 해서 이동하세요. 
	</div>
	<div class="row">
		<div class="col-xs-5">
			<div class="top-select mbottom-10 fb tl mRight-20">컬럼</div>
			<div>
				<ul id="source" class="pub-select-source pub-multiselect-area" style="height: 200px;width: 100%;">
				</ul>
			</div>
		</div>
		<div class="col-xs-2 text-center">
			<div style="position:relative;top:100px;">
				<a href="javascript:;" class="add-item"><span class="fa fa-forward"></span></a>
				<br/>
				<a href="javascript:;" class="cancle-item"><span class="fa fa-backward"></span></a>
			</div>
		</div>
		<div class="col-xs-5">
			<div class="top-select mbottom-10 fb tl mRight-20">설정값</div>
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

<script>
var selectObj;
var tmpSourceItem = [] , paramSourceItem=[]; 
$(document).ready(function (){
	var paramTargetItem = [];
	for(var i = 0 ;i <20; i++){
		paramSourceItem.push({LINK_ID :'0000'+i ,LINK_NAME:'name'+i});
	}

	for(var i = 0 ;i <20; i++){
		tmpSourceItem.push({LINK_ID :'1000'+i ,LINK_NAME:'name'+i});
	}
	

	for(var i = 0 ;i <10; i++){
		var pageNum = 3; 
		if(i > 5){
			pageNum =4;
		}
		paramTargetItem.push({LINK_ID :'0000'+i ,LINK_NAME:'name'+i,pageNo:pageNum});
	}

	selectObj= $.pubMultiselect('#source', {
		targetSelector : '#target'
		,addItemClass:'text_selected'
		,useMultiSelect : true
		,pageInfo :{
			max :9 
		}
		,maxSize :20
		,maxSizeMsg :'20개 까지 등록 가능합니다.'
		,duplicateCheck : true
		,sourceItem : {
			optVal : 'LINK_ID'
			,optTxt : 'LINK_NAME'
			,items : paramSourceItem
		}
		,targetItem : {
			optVal : 'LINK_ID'
			,optTxt : 'LINK_NAME'
			,items : []
			,click: function (e, sEle){
				//console.log(e,sEle);
			}
			,dblclick : function (e, sEle){
				console.log(e,sEle);
			}
			,render: function (sItem){
				//console.log('render', sItem);
				return '<span  style="color:'+sItem.item.STYLE+'">'+sItem.text+'</span>';
			}
		}
	}); 


	selectObj.setItem('target',paramTargetItem);


	//추가
	$('.add-item').on('click',function (){
		selectObj.sourceMove();
	});
	//취소
	$('.cancle-item').on('click',function (){
		selectObj.targetMove();
	});

	//아래위 이동
	$('.item-move').on('click',function (){
		selectObj.move($(this).attr('mode'));
	});
})
</script>
