<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manage.menu.sqllogmgmt" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="varsqlVueArea">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="row">
					<div class="form-horizontal">
						<div class="control-group">
							 <div class="controls">
							 	<div class="col-sm-3">
									<select id="dbinfolist" v-model="vconnid" @change="dbStatsInfo()" class="form-control input-sm">
										<option value=""><spring:message code="select" /></option>
										<c:forEach items="${dbList}" var="tmpInfo" varStatus="status">
											<option value="${tmpInfo.vconnid}">${tmpInfo.vname}</option>
										</c:forEach>
									</select>
								</div>
								<div class="col-xs-9">
								 	<div class="form-group col-xs-8">
									 	<div class="input-group input-daterange">
								            <span class="input-group-addon" style="padding:0px 12px;background:auto;border:0px;">
								            	조회구분
								            	<input type="hidden" name="hidCurrentDate" id="hidCurrentDate" value="${varsqlfn:currentDate('yyyy-MM-dd')}">
								            	<select id="searchGubun" name="searchGubun" class="input-sm">
													<option value="">선택</option>
													<option value="7">주</option>
													<option value="30">월</option>
													<option value="90">분기</option>
													<option value="365">년</option>
												</select>
								            </span>
								            <input name="sdt" id="sdt" type="text" value="${startDate}"  class="input-sm form-control">
								            <span class="input-group-addon">~</span>
											<input name="edt" id="edt" type="text" value="${currentDate}"  class="input-sm form-control">
						          		</div>
						            </div>
						            <div class="col-xs-4">
						            	<button type="button" @click="dbStatsInfo()" class="btn btn-sm btn-outline btn-primary btnSearch">
											조회
										</button>
									</div>
								</div>
							 </div>
					    </div>
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


<script>
VARSQL.loadResource(['juiChart','datepicker']);


VarsqlAPP.vueServiceBean( {
	el: '#varsqlVueArea'
	,validateCheck : true
	,data: {
		vconnid :''
		,detailDate : ''
	}
	,methods:{
		init : function(){
			this.initChart();
			this.initEvt();
		}
		,initEvt : function (){
			$('#sdt').datepicker({
				orientation: "top auto"
				,format: "yyyy-mm-dd"
				,autoclose: true
			}).on('changeDate', function(e){
				var searchGbn = $('#searchGubun').val();
				if(searchGbn != ''){
					VARSQL.util.setRangeDate('#sdt','#edt',$('#hidCurrentDate').val(),new Number(searchGbn));
				}

		    });

			$('#edt').datepicker({
				orientation: "top auto"
				,format: "yyyy-mm-dd"
				,autoclose: true
			}).on('changeDate', function(e){
				var searchGbn = $('#searchGubun').val();
				if(searchGbn != ''){
					VARSQL.util.setRangeDate('#sdt','#edt',$('#hidCurrentDate').val(),new Number('-'+searchGbn));
				}
		    });

			// 주별 등 change
			$('#searchGubun').on('change',function (){
				var searchGbn = $('#searchGubun').val();
				if(searchGbn != ''){
					VARSQL.util.setRangeDate('#sdt','#edt',$('#hidCurrentDate').val(),new Number('-'+searchGbn));
				}
			});
		}
		,initChart : function (){
			var _self = this;

			// main chart;
			VARSQL.pluginUI.chart.bar("#sqlDateChart", {

				axis : [{
			        x : {
			            type : "block",
			            domain : "xCol",
						line : "solid"
			        },
			        y : {
			            type : "range",
			            domain : "yCol",
			            step : 10,
						line : true
			        },
			        data : []
			    }],
			    brush : [{
			        type : "column",
			        outerPadding : 20,
			        target : "yCol"
			    }],
				 event : {
			        click : function(obj, e) {
			            if (obj) {
							_self.dateDetailStats(obj.data.viewDt);
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
			            domain : "xCol",
						line : true
			        },
			        y : {
			            type : "range",
			            domain : "yCol",
			            step : 5,
						line : true
			        },
			        data : []
			    }],
			    brush : [{
			        type : "column",
			        outerPadding : 20,
			        target : "yCol",
			        animate : true
			    }],
				 event : {
			        click : function(obj, e) {
			            if (obj) {
							_self.sqlUserRank(obj.data.xCol);
						}
			        }
			    }
				,widget : [
					{ type : "title", text : "Query 구문별" },
			        { type : "tooltip", orient: "center" },

				]
			});
		}

		,dbStatsInfo:function (sObj){
			var _self = this;
			sObj=$(sObj);

			this.$ajax({
				data:{
					vconnid: _self.vconnid
					,s_date: $('#sdt').val()+' 00:00:00'
					,e_date: $('#edt').val()+' 23:59:59'
				}
				,url : {type:VARSQL.uri.manager, url:'/stats/dbSqlDateStats'}
				,success:function (response){
					var items = response.items ||[];

					VARSQL.pluginUI.chart.bar("#sqlDateChart", {
						axis : [{
					        data :items
					    }]
					});

					if(items.length > 0){
						_self.dateDetailStats(items[items.length-1].viewDt);
					}else{
						_self.dateDetailStats('');
					}
				}
			});
		}
		,dateDetailStats:function (sObj){
			var _self = this;
			if(sObj==''){
				VARSQL.pluginUI.chart.bar("#dateDetailStatsChart", {
					axis : [{
				        data :[]
				    }]
				});
				_self.sqlUserRank('');
				return ;
			}
			_self.detailDate = sObj;
			this.$ajax({
				data:{
					vconnid: _self.vconnid
					,s_date:  sObj+' 00:00:00'
					,e_date:  sObj+' 23:59:59'
				}
				,url : {type:VARSQL.uri.manager, url:'/stats/dbSqlDayStats'}
				,success:function (response){
					var items = response.items ||[];

					if(items.length > 0){
						VARSQL.pluginUI.chart.bar("#dateDetailStatsChart", {
							axis : [{
						        data :items
						    }]
						});
						_self.sqlUserRank('all');
					}else{
						_self.sqlUserRank('');
					}

					return ;
				}
			});
		}
		,sqlUserRank : function (type){

			var _self =this;
			if(type==''){
				VARSQL.pluginUI.chart.bar("#sqlUserRankChart", {
					axis : [{
				        data :[]
				    }]
				});
				return ;
			}
			var date = _self.detailDate;
			this.$ajax({
				data:{
					vconnid: _self.vconnid
					,s_date:date+' 00:00:00'
					,e_date:date+' 23:59:59'
					,command_type :type
				}
				,url : {type:VARSQL.uri.manager, url:'/stats/dbSqlDayUserRank'}
				,success:function (response){
					var names = {};

					var chartData = [];

					var items = response.items;
					var countData = {};
					$.each(items , function (idx , item){
						var label = item.xCol;
						names[label] = label;
						countData[label] =  item.yCol;
					});
					chartData.push(countData);

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
					            text :"[ "+type+" ] Query user Top 5"
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

	}
});

</script>

