<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<script>
$(document).ready(function (){
	sqlLogStat.init();
});

var sqlLogStat ={
	init:function (){
		VARSQL.loadResource([VARSQL.staticResource.get('juiChart'),VARSQL.staticResource.get('datepicker') ]);
		var _self = this; 
		_self.dblist();
		_self.initEvt();
		_self.initChart();
	}
	,initChart : function (){
		var _self = this; 
		
		// main chart; 
		VARSQL.pluginUI.chart.bar("#sqlDateChart", {
			
			axis : [{
		        x : {
		            type : "block",
		            domain : "X_COL",
					line : "solid"
		        },
		        y : {
		            type : "range",
		            domain : "Y_COL",
		            step : 10,
					line : true
		        },
		        data : []
		    }],
		    brush : [{
		        type : "column",
		        outerPadding : 20,
		        target : "Y_COL"
		    }],
			 event : {
		        click : function(obj, e) {
		            if (obj) {
						_self.dateDetailStats(obj.data.VIEW_DATE);
					}
		        }
		    }
			,widget : [{
		        type : "tooltip"
		    }, 
			{
		        type : "title",
		        text : "COUNT",
		        align : "start",
		        orient : "center",
		        dx : -120,
		        dy : -10
		    }
			
			]
		});
		
		// 상세차트. 
		VARSQL.pluginUI.chart.bar("#dateDetailStatsChart", {
			
			axis : [{
		        x : {
		            type : "block",
		            domain : "X_COL",
					line : "solid"
		        },
		        y : {
		            type : "range",
		            domain : "Y_COL",
		            step : 5,
					line : true
		        },
		        data : []
		    }],
		    brush : [{
		        type : "column",
		        outerPadding : 20,
		        target : "Y_COL"
		    }],
			 event : {
		        click : function(obj, e) {
		            if (obj) {
						_self.sqlUserRank(obj.data.X_COL);
					}
		        }
		    }
			,widget : [{
		        type : "tooltip"
		    }, 
			{
		        type : "title",
		        text : "COUNT",
		        align : "start",
		        orient : "center",
		        dx : -120,
		        dy : -10
		    }
			
			]
		});
		
		
	}
	,initEvt:function (){
		var _self = this; 
		
		$('.btnSearch').on('click',function (){
			_self.dbStatsInfo('#dbinfolist');
		});
		
		$('#sdt').datepicker({
			orientation: "top auto"
			,format: "yyyy-mm-dd"
			,autoclose: true
		}).on('changeDate', function(e){
			VARSQL.util.setRangeDate('#sdt','#edt',$('#hidCurrentDate').val(),new Number($('#searchGubun').val()));
	    });
		
		$('#edt').datepicker({
			orientation: "top auto"
			,format: "yyyy-mm-dd"
			,autoclose: true
		}).on('changeDate', function(e){
			VARSQL.util.setRangeDate('#sdt','#edt',$('#hidCurrentDate').val(),new Number('-'+$('#searchGubun').val()));
	    });
		
		// 주별 등 change
		$('#searchGubun').on('change',function (){
			VARSQL.util.setRangeDate('#sdt','#edt',$('#hidCurrentDate').val(),new Number('-'+$('#searchGubun').val()));
		});
	}
	,dblist:function (no){
		var _self = this; 
		var param = {
			page:no?no:1
			,'searchval':$('#searchval').val()
		};
		
		VARSQL.req.ajax({
			type:'POST'
			,data:param
			,url : {gubun:VARSQL.uri.manager, url:'/dbnuser/dbList'}
			,dataType:'JSON'
			,success:function (response){
					
	    		var resultLen = response.result?response.result.length:0;
	    		
	    		var result = response.result;
	    		
	    		var strHtm = new Array();
	    		var item; 
	    		for(var i = 0 ;i < resultLen; i ++){
	    			item = result[i];
	    			strHtm.push('<option value="'+item.VCONNID+'">'+item.VNAME+ '</option>');
	    		}
	    		
	    		$('#dbinfolist').html(strHtm.join(''));
	    		
	    		$('#dbinfolist').on('change',function (){
	    			_self.dbStatsInfo(this);
	    		})
	    		$('#dbinfolist').trigger('change');
			}
		});
	}
	,dbStatsInfo:function (sObj){
		var _self = this; 
		sObj=$(sObj);
		$('#vconnid').val(sObj.val());
		
		VARSQL.req.ajax({
			type:'POST'
			,data:{
				vconnid:$('#vconnid').val()
				,s_date: $('#sdt').val()+' 00:00:00'
				,e_date: $('#edt').val()+' 23:59:59'
			}
			,url : {gubun:VARSQL.uri.manager, url:'/stats/dbSqlDateStats'}
			,dataType:'JSON'
			,success:function (response){
				var items = response.items ||[]; 
				
				VARSQL.pluginUI.chart.bar("#sqlDateChart", {
					axis : [{
				        data :items
				    }]
				});
				
				_self.dateDetailStats(items[items.length-1].VIEW_DATE);
			}
		});
	}
	,dateDetailStats:function (sObj){
		var _self = this; 
		$('#detailDate').val(sObj);
		VARSQL.req.ajax({
			type:'POST'
			,data:{
				vconnid:$('#vconnid').val()
				,s_date:  sObj+' 00:00:00'
				,e_date:  sObj+' 23:59:59'
			}
			,url : {gubun:VARSQL.uri.manager, url:'/stats/dbSqlDayStats'}
			,dataType:'JSON'
			,success:function (response){
				var items = response.items ||[]; 
				VARSQL.pluginUI.chart.bar("#dateDetailStatsChart", {
					axis : [{
				        data :items
				    }]
				});
				
				_self.sqlUserRank('');
				return ; 
			}
		});
	}
	,sqlUserRank : function (type){
		var date = $('#detailDate').val(); 
		VARSQL.req.ajax({
			type:'POST'
			,data:{
				vconnid:$('#vconnid').val()
				,s_date:date+' 00:00:00'
				,e_date:date+' 23:59:59'
				,command_type :type
			}
			,url : {gubun:VARSQL.uri.manager, url:'/stats/dbSqlDayUserRank'}
			,dataType:'JSON'
			,success:function (response){
				var names = {}; 
				
				var chartData = [];
				
				var items = response.result;
				var aaa = {};
				$.each(items , function (idx , item){
					names[item.LABEL] = item.LABEL;
					aaa[item.LABEL] =  item.DATA;
				});
				chartData.push(aaa);
				
				// 상세차트. 
				VARSQL.pluginUI.chart.pie("#sqlUserRankChart", {
				    axis : {
				        data : chartData
				    },
				    brush : {
				        type : "pie",
		 		        format : function(k, v) {
		 		            return names[k] + ": " + v;
		 		        },
				        showText : true
				    },
				    widget : [
				    	{
				            type : "title",
				            text : "Top 5"
				        }, {
				            type : "tooltip",
				            orient : "left"
				        }, {
				            type : "legend"
				        }
				    ]
				});
				
			}
		});
	}
};

</script>

<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manage.menu.sqllogmgmt" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="row">
					<div class="form-horizontal">
						<div class="control-group">
							 <div class="controls">
							 	<div class="col-sm-3">
								 	<input type="hidden" id="vconnid" name="vconnid" value="">
								 	<input type="hidden" id="detailDate" name="detailDate" value="">
									<select id="dbinfolist" class="form-control input-sm"></select>
								</div>
								<div class="col-xs-6">
									<div class="row">
										<div class="col-xs-4">
											<div class="row">
												<input type="hidden"name="hidCurrentDate" id="hidCurrentDate" value="${currentDate}">
												조회구분
												<select	id="searchGubun" name="searchGubun" class="input-sm">
													<option value="7">주</option>
													<option value="30">월</option>
													<option value="90">분기</option>
													<option value="365">년</option>
												</select>
											</div>
									 	</div>
									 	<div class="form-group col-xs-4">
							                <div class="input-group date">
							                    <input name="sdt" id="sdt" type="text" value="${startDate}" class="input-sm form-control">
							                    <span class="input-group-addon-trans">~</span>
							                </div>
							            </div>
							            <div class="form-group col-xs-4">
							                <div class="input-group date">
							                    <input name="edt" id="edt" type="text" value="${currentDate}" class="input-sm form-control">
							                </div>
							            </div>
									</div>
								</div>
							 </div>
					    </div>
					</div>
			    	<div class="pull-right">
						<button type="button" class="btn btn-outline btn-primary btnSearch">
							조회
						</button>
					</div>
			    </div>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div id="sqlDateChart" style="height:250px;margin:0 auto"></div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
		<div class="row">
			<div class="col-lg-6">
				<div class="panel panel-default">
					<div class="panel-body">
						<div id="dateDetailStatsChart" style="height:200px;margin:0 auto"></div>
					</div>
				</div>
			</div>
			<div class="col-lg-6">
				<div class="panel panel-default">
					<div class="panel-body">
						<div id="sqlUserRankChart" style="height:200px;margin:0 auto"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- /.row -->
