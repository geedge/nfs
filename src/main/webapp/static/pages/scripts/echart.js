

    //协议统计分析
    function echart_1(rs) {
    	var data=new Array();
    	var drillData=new Array();
    	var unit="bytes";
    	$(rs).each(function(i, d) {
    		var inoctetsNum=d.count;
        	unit=changeUnit(inoctetsNum);
        	inoctetsNum=changeNum(inoctetsNum);
    		var pktNum=new Array();
    		var byteLen=new Array();
			//协议图-操作系统
    		data.push({
				name: d.protoType,
				y: parseFloat(inoctetsNum),
//				drilldown: d.protoType,
    		});
    		
    		pktNum[0]="pktNum";
    		pktNum[1]=parseInt(d.pktNum);
    		byteLen[0]="byteLen";
    		byteLen[1]=parseInt(d.byteLen);
    		
    		drillData.push({
    			name: d.protoType,
    			id: d.protoType,
    			type:'pie',
				innerSize: '70%',
				data: [pktNum,byteLen],
    		});
    		
	   });
    	var chart = Highcharts.chart('chart_1', {
    		chart: {
    				type: 'pie',
    				spacing : [40, 40 , 40, 40],
    				backgroundColor: 'rgba(255, 255, 255, 0)',
        			plotBackgroundColor:null,
        			plotBorderWidth:null,
        			plotShadow:false,
//        			margin:10,
    				
    		},
    		navigation: {
    			buttonOptions: {
    				x:25,
    				y: -18,
    			}
    		},
    		exporting: {
    			allowHTML:true,
    			filename:'Protocol-Type',
    			chartOptions: {
    				plotOptions: {
    					series: {
    						dataLabels: {
    							enabled: true,
								allowOverlap: true, // 允许数据标签重叠
    						},
    					},
    				}
    			},
    			
    		},
    		noData:{
    			style: {//设置字体颜色
					color: '#fff',
				},
    		},
    		colors:['#f36f8a', '#44A9A8', '#ffff43','#25f3e6' ,'#964CEC','#32B0ED','#2b6ed7','#7278DD','#2DA9D8','#C66FE6'],
    		title: {
    				text: null
    		},
    		/*tool: {
				enabled: true,
//				headerFormat: '<span style="font-size:10px">{point.key}- <b>{point.percentage:.1f}%</span>'+unit,
				pointFormat: '{series.name} {point.y}<b>'+unit,
			},*/
			tooltip: {
				headerFormat: '{series.name}<br>',
				pointFormat: '{point.name} <br>{point.y}</b> '+unit
			},
    		credits:{//是否有highcharts水印
    	        enabled:false
    	    },
    	    legend:{
    	    	width:480,
    	    	x:50,
    	    	itemWidth:100,
    	    	itemDistance:2,
    	    	itemHoverStyle:{
    	    		color:'#61D2F7',
    	    	},
    	    	itemStyle:{
    	    		color: '#fff',
    	    		fontFamily:'Microsoft YaHei',
    	    	},
//    	    	navigation: {//图例分页
//	                activeColor: 'red',
//				    animation: true,
//				    arrowSize: 15,
//				    inactiveColor: '#CCC',
//					    style: {
//					    fontWeight: 'bold',
//					    color: '#333',
//					    fontSize: '12px'
//					    }
//			    }
    	    },
    	    
    		plotOptions: {
    				pie: {
    						allowPointSelect: true,
    						cursor: 'pointer',
    						showInLegend: true,
    						dataLabels: {
    								enabled: false,
    								format: '{point.name}:</b>{point.percentage:.1f}%',
    								style: {
    										color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
    								}
    						},
    						size: 200, //饼图的大小
    						states: {
    							hover: {
    									enabled: false
    							}  
    						},
    						point: {
    								events: {
    										mouseOver: function(e) {  // 鼠标滑过时动态更新标题
    												// 标题更新函数，API 地址：https://api.hcharts.cn/highcharts#Chart.setTitle
    												chart.setTitle({
    														text:e.target.name.length>10? this.percentage.toFixed(1)+"%<br><span>"+e.target.name.substring(0,10)+"...</span>":this.percentage.toFixed(1)+"%<br><span>"+e.target.name.substring(0,10)+"</span>",
    														floating:true,
    														y:120,
    														style: {//设置字体颜色
    															color: '#fff',
    								    						fontFamily:'Microsoft YaHei'
    								    					},
    												});
    												this.slice();
    										},
    										// 鼠标移出时，收回突出显示
    										mouseOut: function() {
    												this.slice();
    										},
    										// 默认是点击突出，这里屏蔽掉
    										click: function() {
    												return false;
    										}
    								},
    						},
    				}
    		},
    		series: [{
    				type: 'pie',
    				innerSize: '70%',//圆环的大小
    				name: ' ',
    				data: data
    		}],
//    		drilldown:{
//    			series:drillData,
//    			drillUpButton: {
//					relativeTo: 'spacingBox',
//    			}
//    		}
    		
    	}, function(c) { // 图表初始化完毕后的会掉函数
    		// 环形图圆心
    		var centerY = c.series[0].center[1],
    				titleHeight = parseInt(c.title.styles.fontSize);
    		// 动态设置标题位置
    		c.setTitle({
    				y:centerY + titleHeight/2
    		});
    	});
    	
    }

    
    // echart_main中心图 活跃IP统计
    function echart_main(rs) {
//    	if(rs==null||rs.length<=0){
//    		rs=[{"ipAddr":"103.6.1.12","linkNum":532,"pktNum": 5,"byteLen": 6},
//    		    {"ipAddr":"163.5.6.43","linkNum":532,"pktNum": 5,"byteLen": 7},
//    		    {"ipAddr":"170.1.0.1","linkNum":532,"pktNum": 5,"byteLen": 8},
//    		    {"ipAddr":"10.32.33.61","linkNum":532,"pktNum": 5,"byteLen": 9},
//    		    {"ipAddr":"145.16.1.2","linkNum":532,"pktNum": 5,"byteLen": 10},
//    		    {"ipAddr":"153.5.0.36","linkNum":532,"pktNum": 5,"byteLen": 11}]	
//    	}
    	var data=new Array();
    	var xData=new Array();
    	var drillData=new Array();
    	var unit="bytes";
    	$(rs).each(function(i, d) {
    		var inoctetsNum=d.linkNum;
        	unit=changeUnit(inoctetsNum);
        	inoctetsNum=changeNum(inoctetsNum);
    		/*var pktNum=new Array();
    		var byteLen=new Array();*/
    		xData.push(d.ipAddr);
			//活跃IP图
    		data.push({
				name: d.ipAddr,
				y: parseFloat(inoctetsNum),
//				drilldown: d.ipAddr,
    		});
    		/*pktNum[0]="pktNum";
    		pktNum[1]=parseInt(d.pktNum);
    		byteLen[0]="byteLen";
    		byteLen[1]=parseInt(d.byteLen);*/
    		/*drillData.push({
    			name: d.ipAddr,
    			id: d.ipAddr,
    			type:'pie',
    			tooltip: {
    				enabled: true,
    				pointFormat: '{series.name}: {point.y}<b> ({point.percentage:.1f}%)'
    			},
    			data: [pktNum,byteLen],
    		});*/
	   });
    	var chart = Highcharts.chart('chart_main', {
    		chart: {
                backgroundColor: 'rgba(255, 255, 255, 0)',
    			plotBackgroundColor:null,
    			plotBorderWidth:null,
    			plotShadow:false,
    			type: 'bar',
//    			marginLeft:80,
    			marginTop:50,
    			inverted: true,
    		},
    		navigation: {
    			buttonOptions: {
    				y:13,
    				x:-7
    			}
    		},
    		exporting: {
    			allowHTML:true,
    			filename:'Active-IP',
    			chartOptions: {
    				plotOptions: {
    					series: {
    						dataLabels: {
    							enabled: true,
								allowOverlap: true, // 允许数据标签重叠
    						},
    					},
    				}
    			}
    		},
    		noData:{
    			style: {//设置字体颜色
					color: '#fff',
				},
    		},
    		colors:['#f36f8a', '#44A9A8', '#ffff43','#25f3e6','#964CEC','#32B0ED','#2b6ed7','#7278DD','#2DA9D8','#C66FE6'],
    		title: {
    				text: null
    		},
    		labels:{
    			style: {//设置字体颜色
					color: '#fff',
					fontSize:'10px',
					fontFamily:'Microsoft YaHei'
				},
    		},
    		xAxis: {
    				type:'category',
    				categories: xData,
    				title: {
    						text: null
    				},
    			labels:{
					formatter:function(){
						if(this.value.length>15){
							return '<span>'+this.value.substring(0,15)+"..."+'</span>'
						}else{
							return this.value
						}
					},
					style: {//设置字体颜色
						color: '#fff',
						fontSize:'10px',
						fontFamily:'Microsoft YaHei'
					},
				}
    				
    		},
    		yAxis: {
    				min: 0,
    				title: {
    						text: unit,
    						align:'high',
    						style: {//设置字体颜色
    							color: '#fff',
    							fontSize:'10px',
    							fontFamily:'Microsoft YaHei'
    						},
    				},
    				labels: {
    					style: {//设置字体颜色
    						color: '#fff',
    						fontSize:'10px',
    						fontFamily:'Microsoft YaHei'
    					},
    				},
    				lineWidth: 1,
    				
    		},
    		tooltip: {
				headerFormat: '{series.name}<br>',
				pointFormat: '{point.name} <br>{point.y}</b> '+unit
			},
    		plotOptions: {
    				bar: {
						dataLabels: {
							enabled: true,
							allowOverlap: true, // 允许数据标签重叠
							style: {//设置字体颜色
								color: '#fff',
								fontSize:'10px',
								fontFamily:'Microsoft YaHei'
							},
						},
						
						showInLegend:false
    				},
    				
    		},
    		credits:{//是否有highcharts水印
    	        enabled:false
    	    },
    		series: [{
    			name: ' ',
    			colorByPoint: true,
    			data: data
    		}],
//    			drilldown:{
//    				activeAxisLabelStyle:{
//    					textDecoration:'none',
//    					color: '#fff',
//    					fontStyle:'Microsoft YaHei',
//    					
//    				},
//    				activeDataLabelStyle:{
//    					textDecoration:'none',
//    					color: '#fff',
//    					fontStyle:'Microsoft YaHei'
//    				},
//    				drillUpButton:{
//    					relativeTo: 'spacingBox',
//    				},
//    				series:drillData
//    			}
    	});
    }
    function changeNum(inoctetsNum){
    	// 变化单位
		var inoctetsNumK=inoctetsNum/1024;
		var inoctetsNumM=inoctetsNumK/1024;
		var inoctetsNumG=inoctetsNumM/1024;
		if(inoctetsNumK>1){
			inoctetsNum=inoctetsNumK;
			};
		if(inoctetsNumM>1){
			inoctetsNum=inoctetsNumM;
			};
		if(inoctetsNumG>1){
			inoctetsNum=inoctetsNumG;
			};
			inoctetsNum=inoctetsNum.toFixed(1)
    	return inoctetsNum;
    }
    function changeUnit(inoctetsNum){
    	// 变化单位
    	var unit="bytes";
		var inoctetsNumK=inoctetsNum/1024;
		var inoctetsNumM=inoctetsNumK/1024;
		var inoctetsNumG=inoctetsNumM/1024;
		if(inoctetsNumK>1){
			unit="KB";
			};
		if(inoctetsNumM>1){
			unit="MB";
			};
		if(inoctetsNumG>1){
			unit="GB";
			};
		return unit
    }
    //app应用类型统计  echart_3 
    function echart_3(rs) {
    	var data=new Array();
    	var xData=new Array();
    	var drillData=new Array();
    	var unit="bytes";
    	$(rs).each(function(i, d) {
        	var inoctetsNum=d.count;
        	unit=changeUnit(inoctetsNum);
        	inoctetsNum=changeNum(inoctetsNum);
    		var pktNum=new Array();
    		var byteLen=new Array();
    		xData.push(d.appType);
    		data.push({
				name: d.appType,
				y: parseFloat(inoctetsNum),
//				drilldown: d.appType,
    		});
/*    		pktNum[0]="pktNum";
    		pktNum[1]=parseInt(d.pktNum);
    		byteLen[0]="byteLen";
    		byteLen[1]=parseInt(d.byteLen);
    		drillData.push({
    			name: d.appType,
    			id: d.appType,
    			data: [pktNum,byteLen],
    		});*/
	   });
    	var chart = Highcharts.chart('chart_3',{
    		chart: {
	    			backgroundColor: 'rgba(255, 255, 255, 0)',
	    			plotBackgroundColor:null,
	    			plotBorderWidth:null,
	    			plotShadow:false,
    				type: 'column',
    				marginTop:50,
    				marginBottom:60,
    				
    		},
    		navigation: {
    			buttonOptions: {
    				y: 13,
    			}
    		},
    		exporting: {
    			allowHTML:true,
    			filename:'App',
    		},
    		noData:{
    			style: {//设置字体颜色
					color: '#fff',
				},
    		},
    		labels:{
    			style: {//设置字体颜色
					color: '#fff',
					fontSize:'10px',
					fontFamily:'Microsoft YaHei'
				},
    		},
    		colors:['#f36f8a', '#44A9A8', '#ffff43','#25f3e6','#964CEC','#32B0ED','#2b6ed7','#7278DD','#2DA9D8','#C66FE6'],
    		title: {
    				text: null
    		},
    		credits:{//是否有highcharts水印
    	        enabled:false
    	    },
    		xAxis: {
    				categories: xData,
    				labels:{
    					style: {//设置字体颜色
    						color: '#fff',
    						fontSize:'10px',
    						fontFamily:'Microsoft YaHei'
    					},
    				},
    				title: {
						text: 'App',
						align:'high',
						style: {//设置字体颜色
							color: '#fff',
							fontSize:'10px',
							fontFamily:'Microsoft YaHei'
						},
				},
    		},
    		yAxis: {
    				min: 0,
    				title: {
						text: unit,
						align:'high',
						style: {//设置字体颜色
							color: '#fff',
							fontFamily:'Microsoft YaHei'
						},
    				},
    				labels:{
    					enabled:true,
    					style: {//设置字体颜色
    						color: '#fff',
    						fontSize:'10px',
    						fontFamily:'Microsoft YaHei'
    					},
    				},
    				
    		},
    		tooltip: {
    			enabled: true,
    			headerFormat: '<span style="font-size:10px"><b>{point.key}</span><br>',
    			pointFormat: '{point.y:.1f} <b> '+unit,
    			shared: true,
    			useHTML: true
    		},
    		plotOptions: {
    				column: {
    				dataLabels: {// 柱形图上的数字显示
    					enabled: true,
    					allowOverlap: true, // 允许数据标签重叠
    					style: {//设置字体颜色
    						color: '#fff',
							fontSize:'10px',
							fontFamily:'Microsoft YaHei'
						},
						 
//						format:"{point.y:.2f}",
					},
					showInLegend:false
    				},
    		},
    		
    		series: [{
    			name: '',
    			colorByPoint: true,
    			data: data
    		}],
    			/*drilldown:{
    				activeAxisLabelStyle:{
    					textDecoration:'none',
    					color: '#fff',
    					fontStyle:'Microsoft YaHei',
    					
    				},
    				activeDataLabelStyle:{
    					textDecoration:'none',
    					color: '#fff',
    					fontStyle:'Microsoft YaHei'
    				},
    				drillUpButton: {
    					relativeTo: 'spacingBox',
        			},
    				series:drillData
    			}*/
    	});
    }
   //终端用户 分操作系统
function echart_2(rs){
	var data=new Array();
	var drillData=new Array();
	var unit="bytes";
	$(rs).each(function(i, d) {
		var inoctetsNum=d.count;
    	unit=changeUnit(inoctetsNum);
    	inoctetsNum=changeNum(inoctetsNum);
//		var pktNum=new Array();
//		var byteLen=new Array();
		data.push({
			name: d.osType,
			y: parseFloat(inoctetsNum),
//			drilldown: d.osType,
		});
		
/*		pktNum[0]="pktNum";
		pktNum[1]=parseInt(d.pktNum);
		byteLen[0]="byteLen";
		byteLen[1]=parseInt(d.byteLen);
		
		drillData.push({
			name: d.osType,
			id: d.osType,
			type:'pie',
			data: [pktNum,byteLen],
		});*/
		
   });
	// 创建图例
	var chart = Highcharts.chart('chart_2',{
			chart: {
					backgroundColor: 'rgba(255, 255, 255, 0)',
	    			plotBackgroundColor:null,
	    			plotBorderWidth:null,
	    			plotShadow:false,
					type: 'pie'
			},
			navigation: {
    			buttonOptions: {
    				y: -8,
    			}
    		},
			exporting: {
    			allowHTML:true,
    			filename:'BS',
    			chartOptions: {
    				plotOptions: {
    					series: {
    						dataLabels: {
    							enabled: true,
								allowOverlap: true, // 允许数据标签重叠
    						},
    					},
    				}
    			}
    		},
			noData:{
    			style: {//设置字体颜色
					color: '#fff',
				},
    		},
			legend:{
				width:480,
    	    	x:40,
    	    	itemWidth:100,
    	    	itemDistance:2,
    	    	itemHoverStyle:{
    	    		color:'#61D2F7',
    	    	},
    	    	itemStyle:{
    	    		color: '#fff',
    	    		fontFamily:'Microsoft YaHei',
    	    	}
    	    },
			colors:['#f36f8a', '#44A9A8', '#ffff43','#25f3e6','#964CEC','#32B0ED','#2b6ed7','#7278DD','#2DA9D8','#C66FE6'],
			title: {
					text: null,
					style: {//设置字体颜色
						color: '#fff',
						fontSize:'18px',
						fontFamily:'Microsoft YaHei'
					},
					
			},
			plotOptions: {
					series: {
						dataLabels: {
							enabled: false,
							format: '{point.name}'
						},
					},
					pie:{
						showInLegend: true,	
						point: {
							events: {
								mouseOver: function(e) { 
										this.slice();
								},
								// 鼠标移出时，收回突出显示
								mouseOut: function() {
										this.slice();
								},
								// 默认是点击突出，这里屏蔽掉
								click: function() {
										return false;
								}
							},
						},
					}
			},
			credits:{//是否有highcharts水印
		        enabled:false
		    },
			tooltip: {
				enabled: true,
				headerFormat: '<span style="font-size:10px">{point.key}: <b>{point.percentage:.1f}%</span><br>',
				pointFormat: '{point.y} <b> '+unit,
				shared: true,
				useHTML: true
			},
			series: [{
					name: ' ',
					colorByPoint: true,
					data: data
			}],
//			drilldown:{
//				activeAxisLabelStyle:{
//					textDecoration:'none',
//					color: '#fff',
//					fontStyle:'Microsoft YaHei',
//					
//				},
//				activeDataLabelStyle:{
//					textDecoration:'none',
//					color: '#fff',
//					fontStyle:'Microsoft YaHei'
//				},
//    			series:drillData,
//    			drillUpButton: {
//					relativeTo: 'spacingBox',
//    			}
//    		}
	});
}
//当点击操作系统列表时-显示浏览器
function echart_5(rs){
	var data=new Array();
	var drillData=new Array();
	var unit="bytes";
	$(rs).each(function(i, d) {
		var inoctetsNum=d.count;
    	unit=changeUnit(inoctetsNum);
    	inoctetsNum=changeNum(inoctetsNum);
//		var pktNum=new Array();
//		var byteLen=new Array();
		//协议图-操作系统
		data.push({
			name: d.bsType,
			y: parseFloat(inoctetsNum),
//			drilldown: d.bsType,
		});
		
/*		pktNum[0]="pktNum";
		pktNum[1]=parseInt(d.pktNum);
		byteLen[0]="byteLen";
		byteLen[1]=parseInt(d.byteLen);
		
		drillData.push({
			name: d.bsType,
			id: d.bsType,
			type:'pie',
			data: [pktNum,byteLen],
		});*/
		
	});
	// 创建图例
	var chart = Highcharts.chart('chart_2',{
		chart: {
			backgroundColor: 'rgba(255, 255, 255, 0)',
			plotBackgroundColor:null,
			plotBorderWidth:null,
			plotShadow:false,
			type: 'pie'
		},
		navigation: {
			buttonOptions: {
				y: -8,
			}
		},
		exporting: {
			allowHTML:true,
			filename:'BS',
			chartOptions: {
				plotOptions: {
					series: {
						dataLabels: {
							enabled: true,
							allowOverlap: true, // 允许数据标签重叠
						},
					},
				}
			}
		},
		legend:{
			width:480,
	    	x:40,
	    	itemWidth:100,
	    	itemDistance:2,
	    	itemHoverStyle:{
	    		color:'#61D2F7',
	    	},
	    	itemStyle:{
	    		color: '#fff',
	    		fontFamily:'Microsoft YaHei',
	    	}
	    },
	    noData:{
			style: {//设置字体颜色
				color: '#fff',
			},
		},
		colors:['#f36f8a', '#44A9A8', '#ffff43','#25f3e6','#964CEC','#32B0ED','#2b6ed7','#7278DD','#2DA9D8','#C66FE6'],
		title: {
			text: null,
			style: {//设置字体颜色
				color: '#fff',
				fontSize:'18px',
				fontFamily:'Microsoft YaHei'
			},
			
		},
		plotOptions: {
			series: {
				dataLabels: {
					enabled: false,
					format: '{point.name}'
				},
				
			},
			pie:{
				point: {
					events: {
						mouseOver: function(e) {  // 鼠标滑过时动态更新标题
							this.slice();
						},
						// 鼠标移出时，收回突出显示
						mouseOut: function() {
							this.slice();
						},
						// 默认是点击突出，这里屏蔽掉
						click: function() {
							return false;
						}
					},
				},
				showInLegend: true,	
			}
		},
		credits:{//是否有highcharts水印
			enabled:false
		},
		tooltip: {
			enabled: true,
			headerFormat: '<span style="font-size:10px">{point.key}: <b>{point.percentage:.1f}%</span><br>',
			pointFormat: '{point.y} <b> '+unit,
			shared: true,
			useHTML: true
		},
		series: [{
			name: ' ',
			colorByPoint: true,
			data: data
		}],
//		drilldown:{
//			activeAxisLabelStyle:{
//				textDecoration:'none',
//				color: '#fff',
//				fontStyle:'Microsoft YaHei',
//				
//			},
//			activeDataLabelStyle:{
//				textDecoration:'none',
//				color: '#fff',
//				fontStyle:'Microsoft YaHei'
//			},
//			series:drillData,
//			drillUpButton: {
//				relativeTo: 'spacingBox',
//			}
//		}
	});
}

//网站流量分析
function echart_4(rs){
	var data=new Array();
//	var drillData=new Array();
	var unit="bytes";
	$(rs).each(function(i, d) {
		var inoctetsNum=d.count;
    	unit=changeUnit(inoctetsNum);
    	inoctetsNum=changeNum(inoctetsNum);
//		var pktNum=new Array();
//		var byteLen=new Array();
		data.push({
			name: d.websiteService,
			y: parseFloat(inoctetsNum),
//			drilldown: d.websiteService,
		});
		
/*		pktNum[0]="pktNum";
		pktNum[1]=parseInt(d.pktNum);
		byteLen[0]="byteLen";
		byteLen[1]=parseInt(d.byteLen);
		
		drillData.push({
			name: d.websiteService,
			id: d.websiteService,
			type:'pie',
			data: [pktNum,byteLen],
		});*/
		
	});
	// 创建图例
	var chart = Highcharts.chart('chart_4',{
		chart: {
			backgroundColor: 'rgba(255, 255, 255, 0)',
			plotBackgroundColor:null,
			plotBorderWidth:null,
			plotShadow:false,
			type: 'pie'
		},
		navigation: {
			buttonOptions: {
				x:-5,
				y: -10,
			}
		},
		exporting: {
			allowHTML:true,
			filename:'Website',
			chartOptions: {
				plotOptions: {
					series: {
						dataLabels: {
							enabled: true,
							allowOverlap: true, // 允许数据标签重叠
						},
					},
				}
			}
		},
		colors:[ '#44A9A8', '#f36f8a','#25f3e6','#ffff43','#964CEC','#32B0ED','#2b6ed7','#7278DD','#2DA9D8','#C66FE6'],
		title: {
			text: null,
		},
		noData:{
			style: {//设置字体颜色
				color: '#fff',
			},
		},
		plotOptions: {
			series: {
				dataLabels: {
					enabled: false,
					format: '{point.name}'
				},
			},
			pie:{
				showInLegend: true,	
				point: {
					events: {
						mouseOver: function(e) {
							this.slice();
						},
						// 鼠标移出时，收回突出显示
						mouseOut: function() {
							this.slice();
						},
						// 默认是点击突出，这里屏蔽掉
						click: function() {
							return false;
						}
					},
				},
			}
		},
		credits:{//是否有highcharts水印
			enabled:false
		},
		legend:{
			width:480,
	    	x:50,
	    	itemWidth:100,
	    	itemDistance:2,
	    	itemHoverStyle:{
	    		color:'#61D2F7',
	    	},
	    	itemStyle:{
	    		color: '#fff',
	    		fontFamily:'Microsoft YaHei',
	    	}
	    },
	    tooltip: {
			enabled: true,
			headerFormat: '<span style="font-size:10px">{point.key}: <b>{point.percentage:.1f}%</span><br>',
			pointFormat: '{point.y} <b> '+unit,
			shared: true,
			useHTML: true
		},
		series: [{
			name: ' ',
			colorByPoint: true,
			data: data,
		}],
//		drilldown:{
//			activeAxisLabelStyle:{
//				textDecoration:'none',
//				color: '#fff',
//				fontStyle:'Microsoft YaHei',
//				
//			},
//			activeDataLabelStyle:{
//				textDecoration:'none',
//				color: '#fff',
//				fontStyle:'Microsoft YaHei'
//			},
//			series:drillData,
//			drillUpButton: {
//				relativeTo: 'spacingBox',
//			}
//		}
	});
}
//网站流量分析-点击列表显示网站分类
function echart_6(rs){
	var data=new Array();
//	var drillData=new Array();
	var unit="bytes";
	$(rs).each(function(i, d) {
		var inoctetsNum=d.count;
    	unit=changeUnit(inoctetsNum);
    	inoctetsNum=changeNum(inoctetsNum);
//		var pktNum=new Array();
//		var byteLen=new Array();
		//协议图-操作系统
		data.push({
			name: d.domain,
			y: parseFloat(inoctetsNum),
//			drilldown: d.domain,
		});
		
/*		pktNum[0]="pktNum";
		pktNum[1]=parseInt(d.pktNum);
		byteLen[0]="byteLen";
		byteLen[1]=parseInt(d.byteLen);
		
		drillData.push({
			name: d.domain,
			id: d.domain,
			type:'pie',
			data: [pktNum,byteLen],
		});*/
		
	});
	// 创建图例
	var chart = Highcharts.chart('chart_4',{
		chart: {
			backgroundColor: 'rgba(255, 255, 255, 0)',
			plotBackgroundColor:null,
			plotBorderWidth:null,
			plotShadow:false,
			type: 'pie'
		},
		navigation: {
			buttonOptions: {
				x:-5,
				y: -10,
			}
		},
		exporting: {
			allowHTML:true,
			filename:'Website',
			chartOptions: {
				plotOptions: {
					series: {
						dataLabels: {
							enabled: true,
							allowOverlap: true, // 允许数据标签重叠
						},
					},
				}
			}
		},
		noData:{
			style: {//设置字体颜色
				color: '#fff',
			},
		},
		colors:[ '#44A9A8', '#f36f8a','#25f3e6','#ffff43','#964CEC','#32B0ED','#2b6ed7','#7278DD','#2DA9D8','#C66FE6'],
		title: {
			text: null,
		},
		plotOptions: {
			series: {
				dataLabels: {
					enabled: false,
					format: '{point.name}'
				},
			},
			pie:{
				showInLegend: true,	
				point: {
					events: {
						mouseOver: function(e) {  // 鼠标滑过时动态更新标题
							this.slice();
						},
						// 鼠标移出时，收回突出显示
						mouseOut: function() {
							this.slice();
						},
						// 默认是点击突出，这里屏蔽掉
						click: function() {
							return false;
						}
					},
				},
			}
		},
		credits:{//是否有highcharts水印
			enabled:false
		},
		legend:{
			width:480,
	    	x:50,
	    	itemWidth:100,
	    	itemDistance:2,
	    	itemHoverStyle:{
	    		color:'#61D2F7',
	    	},
	    	itemStyle:{
	    		color: '#fff',
	    		fontFamily:'Microsoft YaHei',
	    	}
	    },
	    tooltip: {
			enabled: true,
			headerFormat: '<span style="font-size:10px">{point.key}: <b>{point.percentage:.1f}%</span><br>',
			pointFormat: '{point.y} <b> '+unit,
			shared: true,
			useHTML: true
		},
		series: [{
			name: ' ',
			colorByPoint: true,
			data: data,
		}],
//		drilldown:{
//			activeAxisLabelStyle:{
//				textDecoration:'none',
//				color: '#fff',
//				fontStyle:'Microsoft YaHei',
//				
//			},
//			activeDataLabelStyle:{
//				textDecoration:'none',
//				color: '#fff',
//				fontStyle:'Microsoft YaHei'
//			},
//			series:drillData,
//			drillUpButton: {
//				relativeTo: 'spacingBox',
//			}
//		}
	});
}
function echart_topic_domain(rs){
	var data=[{
	    'id': '0.0',
	    'parent': '',
	    'name': 'TOPIC'
	}];
	var unit="bytes";
	$(rs).each(function(i,d){
		var inoctetsNum=d.count;
    	unit=changeUnit(inoctetsNum);
    	inoctetsNum=changeNum(inoctetsNum);
		data.push({
			'id': '1.'+d.topicId,
			'parent': '0.0',
			'name': d.topic,
			'value':parseFloat(inoctetsNum)
		});
		/****主题中的子域名*******/
		$(d.domainData).each(function(j,t){
			var domainNum=d.count;
	    	unit=changeUnit(domainNum);
	    	domainNum=changeNum(domainNum);
			data.push({
				'id': '2.'+t.webId,
				'parent': '1.'+d.topicId,
				'name': t.domain,
				'value':parseFloat(domainNum)
			});
		})
		
	});
	

	// Splice in transparent for the center circle
	Highcharts.getOptions().colors.splice(0, 0, 'transparent');


	var chart = Highcharts.chart('chart_topic', {

	    chart: {
	        backgroundColor: 'rgba(255, 255, 255, 0)',
			plotBackgroundColor:null,
			plotBorderWidth:null,
			plotShadow:false,
			marginTop:50,
			marginBottom:10,
	    },
	    navigation: {
			buttonOptions: {
				x:-5,
				y:15,
			}
		},
	    exporting: {
			allowHTML:true,
			filename:'Topic',
			chartOptions: {
				plotOptions: {
					series: {
						dataLabels: {
							enabled: true,
							allowOverlap: true, // 允许数据标签重叠
						},
					},
				}
			}
		},
	    colors:[ '#44A9A8', '#f36f8a','#25f3e6','#ffff43','#964CEC','#32B0ED','#2b6ed7','#7278DD','#2DA9D8','#C66FE6','#0099cc','#cc0033','#ff6633','#99cccc','#d9f9d0'],
	    noData:{
			style: {//设置字体颜色
				color: '#fff',
			},
		},
	    title: {
	        text: null
	    },
	    credits:{//是否有highcharts水印
			enabled:false
		},
	    series: [{
	        type: "sunburst",
	        data: data,
	        allowDrillToNode: true,
	        cursor: 'pointer',
	        colorByPoint: true,
	        dataLabels: {
	            format: '{point.name}',
	            filter: {
	                property: 'innerArcLength',
	                operator: '>',
	                value: 16
	            }
	        },
	        levels: [{
	            level: 1,
	            levelIsConstant: false,
	            dataLabels: {
	                rotationMode: 'parallel',
	                filter: {
	                    property: 'outerArcLength',
	                    operator: '>',
	                    value: 64
	                }
	            }
	        }, {
	            level: 2,
	            colorByPoint: true,
	            dataLabels: {
	                rotationMode: 'parallel'
	            }
	        },
	        {
	            level: 3,
	            colorVariation: {
	                key: 'brightness',
	                to: -0.5
	            }
	        }, {
	            level: 4,
	            colorVariation: {
	                key: 'brightness',
	                to: 0.5
	            }
	        }]

	    }],
	    tooltip: {
	        headerFormat: "",
	        pointFormat: '<b>{point.name}</b> : <b>{point.value}<b> ' +unit
	    }
	});
}

