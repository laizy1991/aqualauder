/**
 * @description: 推广
 */

define(function(require) {
	Date.prototype.format = function (format) {   
        var o = {   
            "M+": this.getMonth() + 1,   
            "d+": this.getDate(),   
            "h+": this.getHours(),   
            "m+": this.getMinutes(),   
            "s+": this.getSeconds(),   
            "q+": Math.floor((this.getMonth() + 3) / 3),   
            "S": this.getMilliseconds()   
        }   
        if (/(y+)/.test(format)) {   
            format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));   
        }   
        for (var k in o) {   
            if (new RegExp("(" + k + ")").test(format)) {   
                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));   
            }   
        }   
        return format;   
    }   
	
	var smsWait = 60;
	
    var Commission = {
        init: function() {
            var tmp = this.swipe();
            this.bindEvent(tmp);
        },
        
        bindEvent: function(mySwiper) {
        	$("#myCommissionBtn").on("click", function() {
        		mySwiper.slideTo(0, 200, true);
        	});
        	$("#helpCommissionBtn").on("click", function() {
        		mySwiper.slideTo(1, 200, true);
        	});
        },

		helpCommission: function() {
			$("#helpCommissionBtn").addClass("avtive_header");
			$("#helpCommissionBtn").removeClass("desc");
			$("#myCommissionBtn").removeClass("avtive_header");
			$("#myCommissionBtn").addClass("desc");
			var month = $('#month').val();
			$.ajax({
				type:'POST',
				async:true,
				data:{'type':1,'month':month,'page':page},
				url:'/myspace/commission',
				dataType:'json',
				success:function(result){
					if(result && result.success){

						var $helpCommission=$("#helpCommission");
						txt = "";
						if(result.data == null || result.data.length == 0) {
							txt += "<div class='pure-g empty'>";
							txt += "<div class='pure-u-1'>无管理提成数据</div>";
							txt += "</div>";
						} else {
							for(var v in result.data){
								var info=result.data[v];
								txt += "<div class='pure-g record'>";
								txt += "<div class='pure-u-1-2 center'>" + info.underlinName + "</div>";
								var blotter = info.blotter/100;
								var len = blotter.toString().split(".").length
								if(len <= 1) {
									blotter = blotter + ".00";
								} else {
									len = blotter.toString().split(".")[1].length
									if(len == 1) {
										blotter = blotter + "0";
									}
								}
								txt += "<div class='pure-u-1-4'><span class='desc'>推广金额</span><br/>￥" + blotter + "</div>";
								if(info.commissionAmount == null || info.commissionAmount < 0) {
									txt += "<div class='pure-u-1-4'><span class='desc'>管理提成</span><br/>次月结算</div>";
								} else {
									var commissionAmount = info.commissionAmount/100;
									len = commissionAmount.toString().split(".").length
									if(len <= 1) {
										commissionAmount = commissionAmount + ".00";
									} else {
										len = commissionAmount.toString().split(".")[1].length
										if(len == 1) {
											commissionAmount = commissionAmount + "0";
										}
									}
									txt += "<div class='pure-u-1-4'><span class='desc'>管理提成<br/></span><span class='money'>+" + commissionAmount + "</span></div>";
								}
								txt += "</div>";
							}
							txt += "<div class='pure-g' id='bottom'>";
							txt += "<div class='pure-u-1-5'>上一页</div>";
							txt += "<div class='pure-u-1-5 split'>|</div>";
							txt += "<div class='pure-u-1-5'><font color='#e05012'>1</font></div>";
							txt += "<div class='pure-u-1-5 split'>|</div>";
							txt += "<div class='pure-u-1-5'><a href='/myspace/commission?month=" + month + "&type=" + 1 + "&page=" + 2 + "'><font color='#576b95'>下一页</font></a></div></div>";
						}
						$helpCommission.html(txt);
					}
				}
			});
		},

		myCommission: function() {
			$("#myCommissionBtn").addClass("avtive_header");
			$("#myCommissionBtn").removeClass("desc");
			$("#helpCommissionBtn").removeClass("avtive_header");
			$("#helpCommissionBtn").addClass("desc");
			var month = $('#month').val();
			$.ajax({
				type:'POST',
				async:true,
				data:{'type':0,'month':month},
				url:'/myspace/commission',
				dataType:'json',
				success:function(result){
					if(result && result.success){
						var $commission=$("#commission");
						txt = "";

						if(result.data == null || result.data.length == 0) {
							txt += "<div class='pure-g empty'>";
							txt += "<div class='pure-u-1'>无提成数据</div>";
							txt += "</div>";
						} else {
							for(var v in result.data){
								var info=result.data[v];
								txt += "<div class='pure-g record'>";
								txt += "<div class='pure-u-1-2'><span class='desc'>" + new Date(info.spreadTime).format("yyyy-MM-dd hh:mm") + "</span><br/><div style='margin-top: 3px;'>" + info.underlinName + "</div></div>";
								var blotter = info.blotter/100;
								var len = blotter.toString().split(".").length
								if(len <= 1) {
									blotter = blotter + ".00";
								} else {
									len = blotter.toString().split(".")[1].length
									if(len == 1) {
										blotter = blotter + "0";
									}
								}
								txt += "<div class='pure-u-1-4'><span class='desc'>消费金额</span><br/>";
								if(blotter > 0) {
									txt += "￥" + blotter; 
								}
								txt += "</div>";
								var commissionAmount = info.commissionAmount/100;
								len = commissionAmount.toString().split(".").length
								if(len <= 1) {
									commissionAmount = commissionAmount + ".00";
								} else {
									len = commissionAmount.toString().split(".")[1].length
									if(len == 1) {
										commissionAmount = commissionAmount + "0";
									}
								}
								txt += "<div class='pure-u-1-4'><span class='desc'>提成金额</span><br/><span class='money'>+" + commissionAmount + "</span></div>";
								txt += "</div>";
							}

							txt += "<div class='pure-g' id='bottom'>";
							txt += "<div class='pure-u-1-5'>上一页</div>";
							txt += "<div class='pure-u-1-5 split'>|</div>";
							txt += "<div class='pure-u-1-5'><font color='#e05012'>1</font></div>";
							txt += "<div class='pure-u-1-5 split'>|</div>";
							txt += "<div class='pure-u-1-5'><a href='/myspace/commission?month=" + month + "&type=" + 0 + "&page=" + 2 + "'><font color='#576b95'>下一页</font></a></div></div>";
						}
						$commission.html(txt);
					}
				}
			});
		},
        
        swipe: function() {
        	var type;
        	var reg = new RegExp("(^|&)type=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) { 
            	type = unescape(r[2]);
            } else {
            	type = 0;
            }

        	reg = new RegExp("(^|&)page=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) { 
            	page = unescape(r[2]);
            } else {
            	page = 1;
            }
        	
        	var mySwiper = new Swiper('.swiper-container', {
        		initialSlide :type,
        		onSlideChangeEnd : function(swiper) {
        			var index;
        			try {
        				index = mySwiper.activeIndex;
        			} catch(err) {
        				index = type;
        			}
        			if (index == 1) {
						Commission.helpCommission();
        			} else {
						Commission.myCommission();
        			}
        		}
        	});
        	
        	return mySwiper;
        	
        }
    };
    
    Commission.init();
});