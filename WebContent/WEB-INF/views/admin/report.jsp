<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>
<script>
$(document).ready(function (){
	fnInit();
});

function fnInit(){
	
};


</script>
<!-- Page Heading -->
<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header"><spring:message code="admin.menu.report" /></h1>
	</div>
	<!-- /.col-lg-12 -->
</div>
<div class="row">
	<div class="col-lg-8">
		<div class="panel panel-default">
			<div class="panel-heading">
				<i class="fa fa-bar-chart-o fa-fw"></i> Area Chart Example
				<div class="pull-right">
					<div class="btn-group">
						<button type="button"
							class="btn btn-default btn-xs dropdown-toggle"
							data-toggle="dropdown">
							Actions <span class="caret"></span>
						</button>
						<ul class="dropdown-menu pull-right" role="menu">
							<li><a href="#">Action</a></li>
							<li><a href="#">Another action</a></li>
							<li><a href="#">Something else here</a></li>
							<li class="divider"></li>
							<li><a href="#">Separated link</a></li>
						</ul>
					</div>
				</div>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div id="morris-area-chart"
					style="position: relative; -webkit-tap-highlight-color: rgba(0, 0, 0, 0);">
					<svg height="352" version="1.1" width="605"
						xmlns="http://www.w3.org/2000/svg"
						style="overflow: hidden; position: relative;">
						<desc style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">Created with RaphaÃ«l 2.1.2</desc>
						<defs style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></defs>
						<text x="46.5" y="320" text-anchor="end"
							font="10px &quot;Arial&quot;" stroke="none" fill="#888888"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: end; font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: normal; font-family: sans-serif;"
							font-size="12px" font-family="sans-serif" font-weight="normal">
						<tspan dy="4.5"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">0</tspan></text>
						<path fill="none" stroke="#aaaaaa" d="M59,320H580"
							stroke-width="0.5"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></path>
						<text x="46.5" y="246.25" text-anchor="end"
							font="10px &quot;Arial&quot;" stroke="none" fill="#888888"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: end; font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: normal; font-family: sans-serif;"
							font-size="12px" font-family="sans-serif" font-weight="normal">
						<tspan dy="4.5"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">7,500</tspan></text>
						<path fill="none" stroke="#aaaaaa" d="M59,246.25H580"
							stroke-width="0.5"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></path>
						<text x="46.5" y="172.5" text-anchor="end"
							font="10px &quot;Arial&quot;" stroke="none" fill="#888888"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: end; font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: normal; font-family: sans-serif;"
							font-size="12px" font-family="sans-serif" font-weight="normal">
						<tspan dy="4.5"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">15,000</tspan></text>
						<path fill="none" stroke="#aaaaaa" d="M59,172.5H580"
							stroke-width="0.5"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></path>
						<text x="46.5" y="98.75" text-anchor="end"
							font="10px &quot;Arial&quot;" stroke="none" fill="#888888"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: end; font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: normal; font-family: sans-serif;"
							font-size="12px" font-family="sans-serif" font-weight="normal">
						<tspan dy="4.5"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">22,500</tspan></text>
						<path fill="none" stroke="#aaaaaa" d="M59,98.75H580"
							stroke-width="0.5"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></path>
						<text x="46.5" y="25" text-anchor="end"
							font="10px &quot;Arial&quot;" stroke="none" fill="#888888"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: end; font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: normal; font-family: sans-serif;"
							font-size="12px" font-family="sans-serif" font-weight="normal">
						<tspan dy="4.5"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">30,000</tspan></text>
						<path fill="none" stroke="#aaaaaa" d="M59,25H580"
							stroke-width="0.5"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></path>
						<text x="483.7764277035237" y="332.5" text-anchor="middle"
							font="10px &quot;Arial&quot;" stroke="none" fill="#888888"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: middle; font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: normal; font-family: sans-serif;"
							font-size="12px" font-family="sans-serif" font-weight="normal"
							transform="matrix(1,0,0,1,0,6)">
						<tspan dy="4.5"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">2012</tspan></text>
						<text x="252.71324422843256" y="332.5" text-anchor="middle"
							font="10px &quot;Arial&quot;" stroke="none" fill="#888888"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: middle; font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: normal; font-family: sans-serif;"
							font-size="12px" font-family="sans-serif" font-weight="normal"
							transform="matrix(1,0,0,1,0,6)">
						<tspan dy="4.5"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">2011</tspan></text>
						<path fill="#7cb47c" stroke="none"
							d="M59,267.7555C73.56014580801944,262.3471666666667,102.68043742405833,251.1236458333333,117.24058323207777,246.12216666666666C131.80072904009722,241.1206875,160.92102065613608,234.5904608378871,175.48116646415554,227.74366666666668C189.88304981774,220.97129417122042,218.68681652490886,193.66629074585634,233.08869987849332,191.6455C247.33232077764276,189.64691574585635,275.8195625759417,210.19643452380953,290.06318347509114,211.66616666666667C304.6233292831106,213.16855952380953,333.74362089914945,202.53345833333333,348.3037667071689,203.534C362.86391251518836,204.53454166666666,391.9842041312272,237.43138069216758,406.5443499392467,219.6705C420.94623329283115,202.10267235883424,449.75,70.78891666666671,464.1518833535845,62.219166666666695C478.5537667071689,53.6494166666667,507.35753341433775,138.54693738615669,521.7594167679222,151.1125C536.3195625759416,163.81614571949,565.4398541919805,160.25012500000003,580,163.29600000000002L580,320L59,320Z"
							fill-opacity="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); fill-opacity: 1;"></path>
						<path fill="none" stroke="#4da74d"
							d="M59,267.7555C73.56014580801944,262.3471666666667,102.68043742405833,251.1236458333333,117.24058323207777,246.12216666666666C131.80072904009722,241.1206875,160.92102065613608,234.5904608378871,175.48116646415554,227.74366666666668C189.88304981774,220.97129417122042,218.68681652490886,193.66629074585634,233.08869987849332,191.6455C247.33232077764276,189.64691574585635,275.8195625759417,210.19643452380953,290.06318347509114,211.66616666666667C304.6233292831106,213.16855952380953,333.74362089914945,202.53345833333333,348.3037667071689,203.534C362.86391251518836,204.53454166666666,391.9842041312272,237.43138069216758,406.5443499392467,219.6705C420.94623329283115,202.10267235883424,449.75,70.78891666666671,464.1518833535845,62.219166666666695C478.5537667071689,53.6494166666667,507.35753341433775,138.54693738615669,521.7594167679222,151.1125C536.3195625759416,163.81614571949,565.4398541919805,160.25012500000003,580,163.29600000000002"
							stroke-width="3"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></path>
						<circle cx="59" cy="267.7555" r="2" fill="#4da74d"
							stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="117.24058323207777" cy="246.12216666666666" r="2"
							fill="#4da74d" stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="175.48116646415554" cy="227.74366666666668" r="2"
							fill="#4da74d" stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="233.08869987849332" cy="191.6455" r="2" fill="#4da74d"
							stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="290.06318347509114" cy="211.66616666666667" r="2"
							fill="#4da74d" stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="348.3037667071689" cy="203.534" r="2" fill="#4da74d"
							stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="406.5443499392467" cy="219.6705" r="2" fill="#4da74d"
							stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="464.1518833535845" cy="62.219166666666695" r="2"
							fill="#4da74d" stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="521.7594167679222" cy="151.1125" r="2" fill="#4da74d"
							stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="580" cy="163.29600000000002" r="2" fill="#4da74d"
							stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<path fill="#a7b3bc" stroke="none"
							d="M59,293.78433333333334C73.56014580801944,287.86958333333337,102.68043742405833,275.3062708333333,117.24058323207777,270.12533333333334C131.80072904009722,264.9443958333334,160.92102065613608,255.1694781420765,175.48116646415554,252.33683333333335C189.88304981774,249.5349781420765,218.68681652490886,249.8652032688766,233.08869987849332,247.58733333333333C247.33232077764276,245.3344949355433,275.8195625759417,237.37349862637362,290.06318347509114,234.214C304.6233292831106,230.9842902930403,333.74362089914945,221.89652083333334,348.3037667071689,222.03050000000002C362.86391251518836,222.1644791666667,391.9842041312272,248.97571402550093,406.5443499392467,235.28583333333336C420.94623329283115,221.7447556921676,449.75,121.13558333333336,464.1518833535845,113.10666666666668C478.5537667071689,105.07775000000002,507.35753341433775,162.62570787795997,521.7594167679222,171.05450000000002C536.3195625759416,179.57591621129328,565.4398541919805,178.44425,580,180.9075L580,320L59,320Z"
							fill-opacity="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); fill-opacity: 1;"></path>
						<path fill="none" stroke="#7a92a3"
							d="M59,293.78433333333334C73.56014580801944,287.86958333333337,102.68043742405833,275.3062708333333,117.24058323207777,270.12533333333334C131.80072904009722,264.9443958333334,160.92102065613608,255.1694781420765,175.48116646415554,252.33683333333335C189.88304981774,249.5349781420765,218.68681652490886,249.8652032688766,233.08869987849332,247.58733333333333C247.33232077764276,245.3344949355433,275.8195625759417,237.37349862637362,290.06318347509114,234.214C304.6233292831106,230.9842902930403,333.74362089914945,221.89652083333334,348.3037667071689,222.03050000000002C362.86391251518836,222.1644791666667,391.9842041312272,248.97571402550093,406.5443499392467,235.28583333333336C420.94623329283115,221.7447556921676,449.75,121.13558333333336,464.1518833535845,113.10666666666668C478.5537667071689,105.07775000000002,507.35753341433775,162.62570787795997,521.7594167679222,171.05450000000002C536.3195625759416,179.57591621129328,565.4398541919805,178.44425,580,180.9075"
							stroke-width="3"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></path>
						<circle cx="59" cy="293.78433333333334" r="2" fill="#7a92a3"
							stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="117.24058323207777" cy="270.12533333333334" r="2"
							fill="#7a92a3" stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="175.48116646415554" cy="252.33683333333335" r="2"
							fill="#7a92a3" stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="233.08869987849332" cy="247.58733333333333" r="2"
							fill="#7a92a3" stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="290.06318347509114" cy="234.214" r="2" fill="#7a92a3"
							stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="348.3037667071689" cy="222.03050000000002" r="2"
							fill="#7a92a3" stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="406.5443499392467" cy="235.28583333333336" r="2"
							fill="#7a92a3" stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="464.1518833535845" cy="113.10666666666668" r="2"
							fill="#7a92a3" stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="521.7594167679222" cy="171.05450000000002" r="2"
							fill="#7a92a3" stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="580" cy="180.9075" r="2" fill="#7a92a3"
							stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<path fill="#2577b5" stroke="none"
							d="M59,293.78433333333334C73.56014580801944,293.509,102.68043742405833,295.44370833333335,117.24058323207777,292.683C131.80072904009722,289.92229166666664,160.92102065613608,272.9209553734062,175.48116646415554,271.69866666666667C189.88304981774,270.4896637067395,218.68681652490886,285.3036809392265,233.08869987849332,282.9578333333333C247.33232077764276,280.6377642725598,275.8195625759417,255.34839972527473,290.06318347509114,253.035C304.6233292831106,250.6701913919414,333.74362089914945,261.79895833333336,348.3037667071689,264.245C362.86391251518836,266.69104166666665,391.9842041312272,284.2243451730419,406.5443499392467,272.60333333333335C420.94623329283115,261.1086368397086,449.75,178.99368750000002,464.1518833535845,171.78216666666668C478.5537667071689,164.57064583333334,507.35753341433775,206.79287682149365,521.7594167679222,214.91116666666667C536.3195625759416,223.1186684881603,565.4398541919805,231.54179166666665,580,237.08533333333332L580,320L59,320Z"
							fill-opacity="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); fill-opacity: 1;"></path>
						<path fill="none" stroke="#0b62a4"
							d="M59,293.78433333333334C73.56014580801944,293.509,102.68043742405833,295.44370833333335,117.24058323207777,292.683C131.80072904009722,289.92229166666664,160.92102065613608,272.9209553734062,175.48116646415554,271.69866666666667C189.88304981774,270.4896637067395,218.68681652490886,285.3036809392265,233.08869987849332,282.9578333333333C247.33232077764276,280.6377642725598,275.8195625759417,255.34839972527473,290.06318347509114,253.035C304.6233292831106,250.6701913919414,333.74362089914945,261.79895833333336,348.3037667071689,264.245C362.86391251518836,266.69104166666665,391.9842041312272,284.2243451730419,406.5443499392467,272.60333333333335C420.94623329283115,261.1086368397086,449.75,178.99368750000002,464.1518833535845,171.78216666666668C478.5537667071689,164.57064583333334,507.35753341433775,206.79287682149365,521.7594167679222,214.91116666666667C536.3195625759416,223.1186684881603,565.4398541919805,231.54179166666665,580,237.08533333333332"
							stroke-width="3"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></path>
						<circle cx="59" cy="293.78433333333334" r="2" fill="#0b62a4"
							stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="117.24058323207777" cy="292.683" r="2" fill="#0b62a4"
							stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="175.48116646415554" cy="271.69866666666667" r="2"
							fill="#0b62a4" stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="233.08869987849332" cy="282.9578333333333" r="2"
							fill="#0b62a4" stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="290.06318347509114" cy="253.035" r="2" fill="#0b62a4"
							stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="348.3037667071689" cy="264.245" r="2" fill="#0b62a4"
							stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="406.5443499392467" cy="272.60333333333335" r="2"
							fill="#0b62a4" stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="464.1518833535845" cy="171.78216666666668" r="2"
							fill="#0b62a4" stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="521.7594167679222" cy="214.91116666666667" r="2"
							fill="#0b62a4" stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle>
						<circle cx="580" cy="237.08533333333332" r="2" fill="#0b62a4"
							stroke="#ffffff" stroke-width="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></circle></svg>
					<div class="morris-hover morris-default-style"
						style="display: none; left: 488px; top: 129px;">
						<div class="morris-hover-row-label">2012 Q2</div>
						<div class="morris-hover-point" style="color: #0b62a4">
							iPhone: 8,432</div>
						<div class="morris-hover-point" style="color: #7A92A3">
							iPad: 5,713</div>
						<div class="morris-hover-point" style="color: #4da74d">iPod
							Touch: 1,791</div>
					</div>
				</div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
		<div class="panel panel-default">
			<div class="panel-heading">
				<i class="fa fa-bar-chart-o fa-fw"></i> Bar Chart Example
				<div class="pull-right">
					<div class="btn-group">
						<button type="button"
							class="btn btn-default btn-xs dropdown-toggle"
							data-toggle="dropdown">
							Actions <span class="caret"></span>
						</button>
						<ul class="dropdown-menu pull-right" role="menu">
							<li><a href="#">Action</a></li>
							<li><a href="#">Another action</a></li>
							<li><a href="#">Something else here</a></li>
							<li class="divider"></li>
							<li><a href="#">Separated link</a></li>
						</ul>
					</div>
				</div>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="row">
					<div class="col-lg-4">
						<div class="table-responsive">
							<table class="table table-bordered table-hover table-striped">
								<thead>
									<tr>
										<th>#</th>
										<th>Date</th>
										<th>Time</th>
										<th>Amount</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>3326</td>
										<td>10/21/2013</td>
										<td>3:29 PM</td>
										<td>$321.33</td>
									</tr>
									<tr>
										<td>3325</td>
										<td>10/21/2013</td>
										<td>3:20 PM</td>
										<td>$234.34</td>
									</tr>
									<tr>
										<td>3324</td>
										<td>10/21/2013</td>
										<td>3:03 PM</td>
										<td>$724.17</td>
									</tr>
									<tr>
										<td>3323</td>
										<td>10/21/2013</td>
										<td>3:00 PM</td>
										<td>$23.71</td>
									</tr>
									<tr>
										<td>3322</td>
										<td>10/21/2013</td>
										<td>2:49 PM</td>
										<td>$8345.23</td>
									</tr>
									<tr>
										<td>3321</td>
										<td>10/21/2013</td>
										<td>2:23 PM</td>
										<td>$245.12</td>
									</tr>
									<tr>
										<td>3320</td>
										<td>10/21/2013</td>
										<td>2:15 PM</td>
										<td>$5663.54</td>
									</tr>
									<tr>
										<td>3319</td>
										<td>10/21/2013</td>
										<td>2:13 PM</td>
										<td>$943.45</td>
									</tr>
								</tbody>
							</table>
						</div>
						<!-- /.table-responsive -->
					</div>
					<!-- /.col-lg-4 (nested) -->
					<div class="col-lg-8">
						<div id="morris-bar-chart"
							style="position: relative; -webkit-tap-highlight-color: rgba(0, 0, 0, 0);">
							<svg height="352" version="1.1" width="394"
								xmlns="http://www.w3.org/2000/svg"
								style="overflow: hidden; position: relative; left: -0.765625px;">
								<desc style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">Created with RaphaÃ«l 2.1.2</desc>
								<defs style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></defs>
								<text x="30.5" y="320" text-anchor="end"
									font="10px &quot;Arial&quot;" stroke="none" fill="#888888"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: end; font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: normal; font-family: sans-serif;"
									font-size="12px" font-family="sans-serif" font-weight="normal">
								<tspan dy="4.5"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">0</tspan></text>
								<path fill="none" stroke="#aaaaaa" d="M43,320H369"
									stroke-width="0.5"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></path>
								<text x="30.5" y="246.25" text-anchor="end"
									font="10px &quot;Arial&quot;" stroke="none" fill="#888888"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: end; font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: normal; font-family: sans-serif;"
									font-size="12px" font-family="sans-serif" font-weight="normal">
								<tspan dy="4.5"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">25</tspan></text>
								<path fill="none" stroke="#aaaaaa" d="M43,246.25H369"
									stroke-width="0.5"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></path>
								<text x="30.5" y="172.5" text-anchor="end"
									font="10px &quot;Arial&quot;" stroke="none" fill="#888888"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: end; font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: normal; font-family: sans-serif;"
									font-size="12px" font-family="sans-serif" font-weight="normal">
								<tspan dy="4.5"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">50</tspan></text>
								<path fill="none" stroke="#aaaaaa" d="M43,172.5H369"
									stroke-width="0.5"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></path>
								<text x="30.5" y="98.75" text-anchor="end"
									font="10px &quot;Arial&quot;" stroke="none" fill="#888888"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: end; font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: normal; font-family: sans-serif;"
									font-size="12px" font-family="sans-serif" font-weight="normal">
								<tspan dy="4.5"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">75</tspan></text>
								<path fill="none" stroke="#aaaaaa" d="M43,98.75H369"
									stroke-width="0.5"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></path>
								<text x="30.5" y="25" text-anchor="end"
									font="10px &quot;Arial&quot;" stroke="none" fill="#888888"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: end; font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: normal; font-family: sans-serif;"
									font-size="12px" font-family="sans-serif" font-weight="normal">
								<tspan dy="4.5"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">100</tspan></text>
								<path fill="none" stroke="#aaaaaa" d="M43,25H369"
									stroke-width="0.5"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></path>
								<text x="345.7142857142857" y="332.5" text-anchor="middle"
									font="10px &quot;Arial&quot;" stroke="none" fill="#888888"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: middle; font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: normal; font-family: sans-serif;"
									font-size="12px" font-family="sans-serif" font-weight="normal"
									transform="matrix(1,0,0,1,0,6)">
								<tspan dy="4.5"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">2012</tspan></text>
								<text x="252.57142857142858" y="332.5" text-anchor="middle"
									font="10px &quot;Arial&quot;" stroke="none" fill="#888888"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: middle; font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: normal; font-family: sans-serif;"
									font-size="12px" font-family="sans-serif" font-weight="normal"
									transform="matrix(1,0,0,1,0,6)">
								<tspan dy="4.5"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">2010</tspan></text>
								<text x="159.42857142857144" y="332.5" text-anchor="middle"
									font="10px &quot;Arial&quot;" stroke="none" fill="#888888"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: middle; font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: normal; font-family: sans-serif;"
									font-size="12px" font-family="sans-serif" font-weight="normal"
									transform="matrix(1,0,0,1,0,6)">
								<tspan dy="4.5"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">2008</tspan></text>
								<text x="66.28571428571428" y="332.5" text-anchor="middle"
									font="10px &quot;Arial&quot;" stroke="none" fill="#888888"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: middle; font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: normal; font-family: sans-serif;"
									font-size="12px" font-family="sans-serif" font-weight="normal"
									transform="matrix(1,0,0,1,0,6)">
								<tspan dy="4.5"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">2006</tspan></text>
								<rect x="48.82142857142857" y="25" width="15.964285714285715"
									height="295" r="0" rx="0" ry="0" fill="#0b62a4" stroke="none"
									fill-opacity="1"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); fill-opacity: 1;"></rect>
								<rect x="67.78571428571428" y="54.5" width="15.964285714285715"
									height="265.5" r="0" rx="0" ry="0" fill="#7a92a3" stroke="none"
									fill-opacity="1"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); fill-opacity: 1;"></rect>
								<rect x="95.39285714285714" y="98.75" width="15.964285714285715"
									height="221.25" r="0" rx="0" ry="0" fill="#0b62a4"
									stroke="none" fill-opacity="1"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); fill-opacity: 1;"></rect>
								<rect x="114.35714285714286" y="128.25"
									width="15.964285714285715" height="191.75" r="0" rx="0" ry="0"
									fill="#7a92a3" stroke="none" fill-opacity="1"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); fill-opacity: 1;"></rect>
								<rect x="141.96428571428572" y="172.5"
									width="15.964285714285715" height="147.5" r="0" rx="0" ry="0"
									fill="#0b62a4" stroke="none" fill-opacity="1"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); fill-opacity: 1;"></rect>
								<rect x="160.92857142857144" y="202" width="15.964285714285715"
									height="118" r="0" rx="0" ry="0" fill="#7a92a3" stroke="none"
									fill-opacity="1"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); fill-opacity: 1;"></rect>
								<rect x="188.53571428571428" y="98.75"
									width="15.964285714285715" height="221.25" r="0" rx="0" ry="0"
									fill="#0b62a4" stroke="none" fill-opacity="1"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); fill-opacity: 1;"></rect>
								<rect x="207.5" y="128.25" width="15.964285714285715"
									height="191.75" r="0" rx="0" ry="0" fill="#7a92a3"
									stroke="none" fill-opacity="1"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); fill-opacity: 1;"></rect>
								<rect x="235.10714285714283" y="172.5"
									width="15.964285714285715" height="147.5" r="0" rx="0" ry="0"
									fill="#0b62a4" stroke="none" fill-opacity="1"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); fill-opacity: 1;"></rect>
								<rect x="254.07142857142856" y="202" width="15.964285714285715"
									height="118" r="0" rx="0" ry="0" fill="#7a92a3" stroke="none"
									fill-opacity="1"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); fill-opacity: 1;"></rect>
								<rect x="281.6785714285714" y="98.75" width="15.964285714285715"
									height="221.25" r="0" rx="0" ry="0" fill="#0b62a4"
									stroke="none" fill-opacity="1"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); fill-opacity: 1;"></rect>
								<rect x="300.6428571428571" y="128.25"
									width="15.964285714285715" height="191.75" r="0" rx="0" ry="0"
									fill="#7a92a3" stroke="none" fill-opacity="1"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); fill-opacity: 1;"></rect>
								<rect x="328.25" y="25" width="15.964285714285715" height="295"
									r="0" rx="0" ry="0" fill="#0b62a4" stroke="none"
									fill-opacity="1"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); fill-opacity: 1;"></rect>
								<rect x="347.2142857142857" y="54.5" width="15.964285714285715"
									height="265.5" r="0" rx="0" ry="0" fill="#7a92a3" stroke="none"
									fill-opacity="1"
									style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); fill-opacity: 1;"></rect></svg>
							<div class="morris-hover morris-default-style"
								style="display: none; left: 21.285714285714278px; top: 138px;">
								<div class="morris-hover-row-label">2006</div>
								<div class="morris-hover-point" style="color: #0b62a4">
									Series A: 100</div>
								<div class="morris-hover-point" style="color: #7a92a3">
									Series B: 90</div>
							</div>
						</div>
					</div>
					<!-- /.col-lg-8 (nested) -->
				</div>
				<!-- /.row -->
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
		<div class="panel panel-default">
			<div class="panel-heading">
				<i class="fa fa-clock-o fa-fw"></i> Responsive Timeline
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<ul class="timeline">
					<li>
						<div class="timeline-badge">
							<i class="fa fa-check"></i>
						</div>
						<div class="timeline-panel">
							<div class="timeline-heading">
								<h4 class="timeline-title">Lorem ipsum dolor</h4>
								<p>
									<small class="text-muted"><i class="fa fa-clock-o"></i>
										11 hours ago via Twitter</small>
								</p>
							</div>
							<div class="timeline-body">
								<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
									Libero laboriosam dolor perspiciatis omnis exercitationem.
									Beatae, officia pariatur? Est cum veniam excepturi. Maiores
									praesentium, porro voluptas suscipit facere rem dicta, debitis.</p>
							</div>
						</div>
					</li>
					<li class="timeline-inverted">
						<div class="timeline-badge warning">
							<i class="fa fa-credit-card"></i>
						</div>
						<div class="timeline-panel">
							<div class="timeline-heading">
								<h4 class="timeline-title">Lorem ipsum dolor</h4>
							</div>
							<div class="timeline-body">
								<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
									Autem dolorem quibusdam, tenetur commodi provident cumque magni
									voluptatem libero, quis rerum. Fugiat esse debitis optio,
									tempore. Animi officiis alias, officia repellendus.</p>
								<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
									Laudantium maiores odit qui est tempora eos, nostrum provident
									explicabo dignissimos debitis vel! Adipisci eius voluptates, ad
									aut recusandae minus eaque facere.</p>
							</div>
						</div>
					</li>
					<li>
						<div class="timeline-badge danger">
							<i class="fa fa-bomb"></i>
						</div>
						<div class="timeline-panel">
							<div class="timeline-heading">
								<h4 class="timeline-title">Lorem ipsum dolor</h4>
							</div>
							<div class="timeline-body">
								<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
									Repellendus numquam facilis enim eaque, tenetur nam id qui vel
									velit similique nihil iure molestias aliquam, voluptatem totam
									quaerat, magni commodi quisquam.</p>
							</div>
						</div>
					</li>
					<li class="timeline-inverted">
						<div class="timeline-panel">
							<div class="timeline-heading">
								<h4 class="timeline-title">Lorem ipsum dolor</h4>
							</div>
							<div class="timeline-body">
								<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
									Voluptates est quaerat asperiores sapiente, eligendi, nihil.
									Itaque quos, alias sapiente rerum quas odit! Aperiam officiis
									quidem delectus libero, omnis ut debitis!</p>
							</div>
						</div>
					</li>
					<li>
						<div class="timeline-badge info">
							<i class="fa fa-save"></i>
						</div>
						<div class="timeline-panel">
							<div class="timeline-heading">
								<h4 class="timeline-title">Lorem ipsum dolor</h4>
							</div>
							<div class="timeline-body">
								<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
									Nobis minus modi quam ipsum alias at est molestiae excepturi
									delectus nesciunt, quibusdam debitis amet, beatae consequuntur
									impedit nulla qui! Laborum, atque.</p>
								<hr>
								<div class="btn-group">
									<button type="button"
										class="btn btn-primary btn-sm dropdown-toggle"
										data-toggle="dropdown">
										<i class="fa fa-gear"></i> <span class="caret"></span>
									</button>
									<ul class="dropdown-menu" role="menu">
										<li><a href="#">Action</a></li>
										<li><a href="#">Another action</a></li>
										<li><a href="#">Something else here</a></li>
										<li class="divider"></li>
										<li><a href="#">Separated link</a></li>
									</ul>
								</div>
							</div>
						</div>
					</li>
					<li>
						<div class="timeline-panel">
							<div class="timeline-heading">
								<h4 class="timeline-title">Lorem ipsum dolor</h4>
							</div>
							<div class="timeline-body">
								<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
									Sequi fuga odio quibusdam. Iure expedita, incidunt unde quis
									nam! Quod, quisquam. Officia quam qui adipisci quas
									consequuntur nostrum sequi. Consequuntur, commodi.</p>
							</div>
						</div>
					</li>
					<li class="timeline-inverted">
						<div class="timeline-badge success">
							<i class="fa fa-graduation-cap"></i>
						</div>
						<div class="timeline-panel">
							<div class="timeline-heading">
								<h4 class="timeline-title">Lorem ipsum dolor</h4>
							</div>
							<div class="timeline-body">
								<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
									Deserunt obcaecati, quaerat tempore officia voluptas debitis
									consectetur culpa amet, accusamus dolorum fugiat, animi dicta
									aperiam, enim incidunt quisquam maxime neque eaque.</p>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-8 -->
	<div class="col-lg-4">
		<div class="panel panel-default">
			<div class="panel-heading">
				<i class="fa fa-bell fa-fw"></i> Notifications Panel
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="list-group">
					<a href="#" class="list-group-item"> <i
						class="fa fa-comment fa-fw"></i> New Comment <span
						class="pull-right text-muted small"><em>4 minutes ago</em>
					</span>
					</a> <a href="#" class="list-group-item"> <i
						class="fa fa-twitter fa-fw"></i> 3 New Followers <span
						class="pull-right text-muted small"><em>12 minutes ago</em>
					</span>
					</a> <a href="#" class="list-group-item"> <i
						class="fa fa-envelope fa-fw"></i> Message Sent <span
						class="pull-right text-muted small"><em>27 minutes ago</em>
					</span>
					</a> <a href="#" class="list-group-item"> <i
						class="fa fa-tasks fa-fw"></i> New Task <span
						class="pull-right text-muted small"><em>43 minutes ago</em>
					</span>
					</a> <a href="#" class="list-group-item"> <i
						class="fa fa-upload fa-fw"></i> Server Rebooted <span
						class="pull-right text-muted small"><em>11:32 AM</em> </span>
					</a> <a href="#" class="list-group-item"> <i
						class="fa fa-bolt fa-fw"></i> Server Crashed! <span
						class="pull-right text-muted small"><em>11:13 AM</em> </span>
					</a> <a href="#" class="list-group-item"> <i
						class="fa fa-warning fa-fw"></i> Server Not Responding <span
						class="pull-right text-muted small"><em>10:57 AM</em> </span>
					</a> <a href="#" class="list-group-item"> <i
						class="fa fa-shopping-cart fa-fw"></i> New Order Placed <span
						class="pull-right text-muted small"><em>9:49 AM</em> </span>
					</a> <a href="#" class="list-group-item"> <i
						class="fa fa-money fa-fw"></i> Payment Received <span
						class="pull-right text-muted small"><em>Yesterday</em> </span>
					</a>
				</div>
				<!-- /.list-group -->
				<a href="#" class="btn btn-default btn-block">View All Alerts</a>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
		<div class="panel panel-default">
			<div class="panel-heading">
				<i class="fa fa-bar-chart-o fa-fw"></i> Donut Chart Example
			</div>
			<div class="panel-body">
				<div id="morris-donut-chart">
					<svg height="352" version="1.1" width="272"
						xmlns="http://www.w3.org/2000/svg"
						style="overflow: hidden; position: relative; left: -0.328125px;">
						<desc style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">Created with RaphaÃ«l 2.1.2</desc>
						<defs style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></defs>
						<path fill="none" stroke="#0b62a4"
							d="M136,262.5A84,84,0,0,0,215.4731310661292,205.70333506292647"
							stroke-width="2" opacity="0"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); opacity: 0;"></path>
						<path fill="#0b62a4" stroke="#ffffff"
							d="M136,265.5A87,87,0,0,0,218.31145717563385,206.6748827437453L250.4791530833528,217.6857564596917A121,121,0,0,1,136,299.5Z"
							stroke-width="3"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></path>
						<path fill="none" stroke="#3980b5"
							d="M215.4731310661292,205.70333506292647A84,84,0,0,0,60.66349426346437,141.3461185956442"
							stroke-width="2" opacity="1"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); opacity: 1;"></path>
						<path fill="#3980b5" stroke="#ffffff"
							d="M218.31145717563385,206.6748827437453A87,87,0,0,0,57.97290477287382,140.01919425977434L22.995241395196558,122.7691778934663A126,126,0,0,1,255.20969659919382,219.3050025943897Z"
							stroke-width="3"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></path>
						<path fill="none" stroke="#679dc6"
							d="M60.66349426346437,141.3461185956442A84,84,0,0,0,135.97361062214395,262.4999958547661"
							stroke-width="2" opacity="0"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); opacity: 0;"></path>
						<path fill="#679dc6" stroke="#ffffff"
							d="M57.97290477287382,140.01919425977434A87,87,0,0,0,135.97266814436335,265.499995706722L135.96198672951687,299.4999940288892A121,121,0,0,1,27.479557212847496,124.98071845324938Z"
							stroke-width="3"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></path>
						<text x="136" y="168.5" text-anchor="middle"
							font="10px &quot;Arial&quot;" stroke="none" fill="#000000"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: middle; font-style: normal; font-variant: normal; font-weight: 800; font-size: 15px; line-height: normal; font-family: Arial;"
							font-size="15px" font-weight="800"
							transform="matrix(1.512,0,0,1.512,-69.632,-90.88)"
							stroke-width="0.6613756613756613">
						<tspan dy="5"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">In-Store Sales</tspan></text>
						<text x="136" y="188.5" text-anchor="middle"
							font="10px &quot;Arial&quot;" stroke="none" fill="#000000"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); text-anchor: middle; font-style: normal; font-variant: normal; font-weight: normal; font-size: 14px; line-height: normal; font-family: Arial;"
							font-size="14px" transform="matrix(1.75,0,0,1.75,-102,-135.375)"
							stroke-width="0.5714285714285714">
						<tspan dy="5"
							style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);">30</tspan></text></svg>
				</div>
				<a href="#" class="btn btn-default btn-block">View Details</a>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
		<div class="chat-panel panel panel-default">
			<div class="panel-heading">
				<i class="fa fa-comments fa-fw"></i> Chat
				<div class="btn-group pull-right">
					<button type="button"
						class="btn btn-default btn-xs dropdown-toggle"
						data-toggle="dropdown">
						<i class="fa fa-chevron-down"></i>
					</button>
					<ul class="dropdown-menu slidedown">
						<li><a href="#"> <i class="fa fa-refresh fa-fw"></i>
								Refresh
						</a></li>
						<li><a href="#"> <i class="fa fa-check-circle fa-fw"></i>
								Available
						</a></li>
						<li><a href="#"> <i class="fa fa-times fa-fw"></i> Busy
						</a></li>
						<li><a href="#"> <i class="fa fa-clock-o fa-fw"></i> Away
						</a></li>
						<li class="divider"></li>
						<li><a href="#"> <i class="fa fa-sign-out fa-fw"></i>
								Sign Out
						</a></li>
					</ul>
				</div>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<ul class="chat">
					<li class="left clearfix"><span class="chat-img pull-left">
							<img src="http://placehold.it/50/55C1E7/fff" alt="User Avatar"
							class="img-circle">
					</span>
						<div class="chat-body clearfix">
							<div class="header">
								<strong class="primary-font">Jack Sparrow</strong> <small
									class="pull-right text-muted"> <i
									class="fa fa-clock-o fa-fw"></i> 12 mins ago
								</small>
							</div>
							<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
								Curabitur bibendum ornare dolor, quis ullamcorper ligula
								sodales.</p>
						</div></li>
					<li class="right clearfix"><span class="chat-img pull-right">
							<img src="http://placehold.it/50/FA6F57/fff" alt="User Avatar"
							class="img-circle">
					</span>
						<div class="chat-body clearfix">
							<div class="header">
								<small class=" text-muted"> <i
									class="fa fa-clock-o fa-fw"></i> 13 mins ago
								</small> <strong class="pull-right primary-font">Bhaumik Patel</strong>
							</div>
							<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
								Curabitur bibendum ornare dolor, quis ullamcorper ligula
								sodales.</p>
						</div></li>
					<li class="left clearfix"><span class="chat-img pull-left">
							<img src="http://placehold.it/50/55C1E7/fff" alt="User Avatar"
							class="img-circle">
					</span>
						<div class="chat-body clearfix">
							<div class="header">
								<strong class="primary-font">Jack Sparrow</strong> <small
									class="pull-right text-muted"> <i
									class="fa fa-clock-o fa-fw"></i> 14 mins ago
								</small>
							</div>
							<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
								Curabitur bibendum ornare dolor, quis ullamcorper ligula
								sodales.</p>
						</div></li>
					<li class="right clearfix"><span class="chat-img pull-right">
							<img src="http://placehold.it/50/FA6F57/fff" alt="User Avatar"
							class="img-circle">
					</span>
						<div class="chat-body clearfix">
							<div class="header">
								<small class=" text-muted"> <i
									class="fa fa-clock-o fa-fw"></i> 15 mins ago
								</small> <strong class="pull-right primary-font">Bhaumik Patel</strong>
							</div>
							<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
								Curabitur bibendum ornare dolor, quis ullamcorper ligula
								sodales.</p>
						</div></li>
				</ul>
			</div>
			<!-- /.panel-body -->
			<div class="panel-footer">
				<div class="input-group">
					<input id="btn-input" type="text" class="form-control input-sm"
						placeholder="Type your message here..."> <span
						class="input-group-btn">
						<button class="btn btn-warning btn-sm" id="btn-chat">
							Send</button>
					</span>
				</div>
			</div>
			<!-- /.panel-footer -->
		</div>
		<!-- /.panel .chat-panel -->
	</div>
	<!-- /.col-lg-4 -->
</div>