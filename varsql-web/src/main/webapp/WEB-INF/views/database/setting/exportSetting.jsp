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
				<a href="javascript:;" class="cancel-item"><span class="fa fa-backward"></span></a>
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
	
})
</script>
