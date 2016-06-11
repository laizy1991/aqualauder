$(document).ready(function () {
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
		WeixinJSBridge.call('hideOptionMenu');
	});
//	推荐商品
//	$('.menu .ccbg dd').each(function(){
//		if( $(this).attr("menu") == '1' ){
//			$(this).show();
//		}else{
//			$(this).hide();
//		}
//	});
	

	$('#cart').on('click' , function (){
		$('#menu-container').hide();
		$('#cart-container').show();
		$('#user-container').hide();
		
		$(".footermenu ul li a").each(function(){
			$(this).attr("class","");
		});
		$(this).children("a").attr("class","active");
		
		$(".footermenu").hide();
	});
	$('#home').on('click' , function (){
		$('#menu-container').show();
		$('#cart-container').hide();
		$('#user-container').hide();
		
		$(".footermenu ul li a").each(function(){
			$(this).attr("class","");
		});
		$(this).children("a").attr("class","active");
	});
	
	
	$('#ticket').on('click' , function (){
		$('#tx-container').hide();
		$('#member-container').hide();
		$('#user-container').hide();
		$('#ticket-container').show();
		$(".footermenu ul li a").each(function(){
			$(this).attr("class","");
		});
		$(this).children("a").attr("class","active");
	})
	
	$('#member').on('click' , function (){
		$('#tx-container').hide();
		$('#ticket-container').hide();
		$('#user-container').hide();
		$('#member-container').show();
		
		$(".footermenu ul li a").each(function(){
			$(this).attr("class","");
		});
		$(this).children("a").attr("class","active");
	})
	
	$('#tx').on('click' , function (){
		$('#ticket-container').hide();
		$('#member-container').hide();
		$('#user-container').hide();
		$('#tx-container').show();
		
		$(".footermenu ul li a").each(function(){
			$(this).attr("class","");
		});
		$(this).children("a").attr("class","active");
	})
	
	$('#user').on('click' , function (){
		$('#menu-container').hide();
		$('#cart-container').hide();
		$('#ticket-container').hide();
		$('#member-container').hide();
		$('#tx-container').hide();
		$('#user-container').show();

		$(".footermenu ul li a").each(function(){
			$(this).attr("class","");
		});
		$(this).children("a").attr("class","active");
        
		$.ajax({
			type : 'POST',
			url : appurl+'/App/Index/getorders',
			data : {
				uid : $_GET['uid']
			},
			success : function (response , status , xhr){
				if(response){
					var json = eval(response); 
					var html = '';
					var order_status = '';
					var host='http://'+window.location.host+appurl+'?g=App&m=Index&a=member&page_type=order';
					host=host.replace(/\&/g, "%26");
					$.each(json, function (index, value) {
						var pay = '';
						var order = '';
						var url = '?g=App&m=Index&a=del_order&id='+value.id;
						var url2 ='?g=App&m=Index&a=refund_good&id='+value.id;
						var regoodurl = '/App/Index/refund_good';//退换货
						if (value.order_status == '0'){
							order_status = 'no';
							order = '未发货';
						}else if ( value.order_status == '1'){
							order_status = 'no';
							var confirm_url = appurl+'?g=App&m=Index&a=confirm_order&id='+value.orderid+'&uid='+$_GET['uid'];
							order = '<a href="'+confirm_url+'" style="color:red">确认收货</a>';
						}else if ( value.order_status == '4'){
							order_status = 'no';
							order = '已退货';
						}else{
							order_status = 'ok';
							order = '已完成';
						}
						
						if (value.pay_status == '0'){
							pay_status = 'no';
							pay = '<a href="'+value.pay_url+'">去支付</a>';
							$('.regood').hide();
						}else if ( value.pay_status == '1'){
							pay_status = 'ok';
							pay = '已支付';
						}
						//html += '<tr><td>'+value.orderid+'</td><td class="cc">'+value.totalprice+'元</td><td class="cc"><em class="'+pay_status+'">'+pay+'</em></td><td class="cc"><em class="'+order_status+'">'+order+'</em></td></tr>';
						
						html += '<li style="border: 1px solid #d0d0d0;border-radius: 10px;margin-bottom:10px;background-color:#FFF;"><table style="width:100%;"><tr><td style="border-bottom:0px">订单编号:'+value.orderid+'</td></tr>';
						html += '<td style="border-bottom:0px">订单金额:'+value.totalprice+'元</td></tr>';
						html += '<td style="border-bottom:0px">订单时间:'+value.time+'</td></tr>';
						html += '<td style="border-bottom:0px">支付状态:<em class="'+pay_status+'">'+pay+'</em>';
						if (value.pay_status == '0')
						{
							html += '<a href="'+value.pay_url+'">(已经支付?)</a>';
						}
						html += '</td></tr>';
						if(value.order_status == '1')
						{
							html += '<td style="border-bottom:0px">订单状态:<em class="'+order_status+'" style="background-color:#FFFF00;">'+order+'</em></td></tr>';
						}
						else
						{
							html += '<td style="border-bottom:0px">订单状态:<em class="'+order_status+'">'+order+'</em></td></tr>';
						}
						
						html += '<td style="border-bottom:0px">商品名称:'+value.cart_name+'</td></tr>';
						html += '<td style="border-bottom:0px">订单详情:'+value.note+'</td></tr>';
						
						html += '<td style="border-bottom:0px">快递公司:'+value.order_info_name+'</em></td></tr>';
						html += '<td style="border-bottom:0px">快递单号:'+value.order_info_num+'</em></td></tr>';
						html+='<tr><td>';
						if(window.location.host=='bcs.winbz.com'){
							html += '取消订单，请联系DDY客服：4000755867</td></tr><tr><td>';
						}else if(value.pay_status==0){
							html += '<a href='+appurl+url+' onclick = "return func_('+value.order_status+','+value.pay_status+')"><div class="del_order">取消订单</div></a>';
						}
						if(value.order_status == '1'){
							html+='<a href="http://m.kuaidi100.com/index_all.html?type='+value.order_info_name+'&postid='+value.order_info_num+'&callbackurl='+host+'"><div class="see_order">查看物流</div></a>';
						}
						if(value.pay_status=='1' && value.order_status<4 && value.show==1){
							
							html+='<a href="'+appurl+url2+'"><div class="qc_order">退/换货</div></a>';						
						}
						html+='</td></tr>';
						html += '</table></li>';
					});
					
					$('#orderlistinsert').empty();
					$('#orderlistinsert').append( html );					
				}

			},
			beforeSend : function(){
    			$('#page_tag_load').show();
	    	},
	    	complete : function(){
	    		$('#page_tag_load').hide();
	    	}
		});
	});
});

function clickTx() {
	$('#ticket-container').hide();
	$('#member-container').hide();
	$('#user-container').hide();
	$('#tx-container').show();
	
	$(".footermenu ul li a").each(function(){
		$(this).attr("class","");
	});
	$(this).children("a").attr("class","active");
}

function clickUser() {

	$('#menu-container').hide();
	$('#cart-container').hide();
	$('#ticket-container').hide();
	$('#member-container').hide();
	$('#tx-container').hide();
	$('#user-container').show();

	$(".footermenu ul li a").each(function(){
		$(this).attr("class","");
	});
	$(this).children("a").attr("class","active");
    
	$.ajax({
		type : 'POST',
		url : appurl+'/App/Index/getorders',
		data : {
			uid : $_GET['uid']
		},
		success : function (response , status , xhr){
			if(response){
				var json = eval(response); 
				var html = '';
				var order_status = '';
				var host='http://'+window.location.host+appurl+'?g=App&m=Index&a=member&page_type=order';
				host=host.replace(/\&/g, "%26");
				$.each(json, function (index, value) {
					var pay = '';
					var order = '';
					var url = '?g=App&m=Index&a=del_order&id='+value.id;
					var url2 ='?g=App&m=Index&a=refund_good&id='+value.id;
					var regoodurl = '/App/Index/refund_good';//退换货
					if (value.order_status == '0'){
						order_status = 'no';
						order = '未发货';
					}else if ( value.order_status == '1'){
						order_status = 'no';
						var confirm_url = appurl+'?g=App&m=Index&a=confirm_order&id='+value.orderid+'&uid='+$_GET['uid'];
						order = '<a href="'+confirm_url+'" style="color:red">确认收货</a>';
					}else if ( value.order_status == '4'){
						order_status = 'no';
						order = '已退货';
					}else{
						order_status = 'ok';
						order = '已完成';
					}
					
					if (value.pay_status == '0'){
						pay_status = 'no';
						pay = '<a href="'+value.pay_url+'">去支付</a>';
						$('.regood').hide();
					}else if ( value.pay_status == '1'){
						pay_status = 'ok';
						pay = '已支付';
					}
					//html += '<tr><td>'+value.orderid+'</td><td class="cc">'+value.totalprice+'元</td><td class="cc"><em class="'+pay_status+'">'+pay+'</em></td><td class="cc"><em class="'+order_status+'">'+order+'</em></td></tr>';
					
					html += '<li style="border: 1px solid #d0d0d0;border-radius: 10px;margin-bottom:10px;background-color:#FFF;"><table style="width:100%;"><tr><td style="border-bottom:0px">订单编号:'+value.orderid+'</td></tr>';
					html += '<td style="border-bottom:0px">订单金额:'+value.totalprice+'元</td></tr>';
					html += '<td style="border-bottom:0px">订单时间:'+value.time+'</td></tr>';
					html += '<td style="border-bottom:0px">支付状态:<em class="'+pay_status+'">'+pay+'</em>';
					if (value.pay_status == '0')
					{
						html += '<a href="'+value.pay_url+'">(已经支付?)</a>';
					}
					html += '</td></tr>';
					if(value.order_status == '1')
					{
						html += '<td style="border-bottom:0px">订单状态:<em class="'+order_status+'" style="background-color:#FFFF00;">'+order+'</em></td></tr>';
					}
					else
					{
						html += '<td style="border-bottom:0px">订单状态:<em class="'+order_status+'">'+order+'</em></td></tr>';
					}
					
					html += '<td style="border-bottom:0px">商品名称:'+value.cart_name+'</td></tr>';
					html += '<td style="border-bottom:0px">订单详情:'+value.note+'</td></tr>';
					
					html += '<td style="border-bottom:0px">快递公司:'+value.order_info_name+'</em></td></tr>';
					html += '<td style="border-bottom:0px">快递单号:'+value.order_info_num+'</em></td></tr>';
					html+='<tr><td>';
					if(window.location.host=='bcs.winbz.com'){
						html += '取消订单，请联系DDY客服：4000755867</td></tr><tr><td>';
					}else if(value.pay_status==0){
						html += '<a href='+appurl+url+' onclick = "return func_('+value.order_status+','+value.pay_status+')"><div class="del_order">取消订单</div></a>';
					}
					if(value.order_status == '1'){
						html+='<a href="http://m.kuaidi100.com/index_all.html?type='+value.order_info_name+'&postid='+value.order_info_num+'&callbackurl='+host+'"><div class="see_order">查看物流</div></a>';
					}
					if(value.pay_status=='1' && value.order_status<4 && value.show==1){
						
						html+='<a href="'+appurl+url2+'"><div class="qc_order">退/换货</div></a>';						
					}
					html+='</td></tr>';
					html += '</table></li>';
				});
				
				$('#orderlistinsert').empty();
				$('#orderlistinsert').append( html );					
			}

		},
		beforeSend : function(){
			$('#page_tag_load').show();
    	},
    	complete : function(){
    		$('#page_tag_load').hide();
    	}
	});
}

/* 取消订单 */
function func_(order_status,pay_status){

	if(order_status == 1){
		
		alert('商品已发货！');
		
		return false;
		
	}else if(pay_status == 1){
		
		alert('商品已付款!');
		
		return false;
		
	}else if(!confirm("确认要取消订单？")){
			
        event.returnValue = false;

	}
				
}
/* END 取消订单 */

//退货
function regood(order_status,pay_status){
	if (pay_status == 0 ) {
		alert('商品未付款');
		return false;
	}else if(order_status==4){
		alert('商品已退货');
		return false;
	}
}

function user() {
	$('#user').click();
}
function home() {
	$('#home').click();
}
function clearCache(){
	$('#ullist').find('li').remove();

	$('#home').click();

	$('.reduce').each(function () {
		$(this).children().css('background','');
	});
	$('#totalNum').html(0);
	$('#cartN2').html(0);
	$('#totalPrice').html(0);
}
function addProductN (){
	var price = $("#goods_price").val();
	var cartMenuN = parseFloat($('#buy_count').html())+1;
	var allCount = parseFloat($("#stockCount").html());
	if(cartMenuN > allCount) {
		return;
	}
	$('#buy_count').html( cartMenuN );
	$('#goods_num').val(cartMenuN);
	doProduct(price)
}
function reduceProductN (){
	var price = $("#goods_price").val();
	var cartMenuN = parseFloat($('#buy_count').html())-1;
	if(cartMenuN < 0) {
		cartMenuN = 0;
	}
	$('#buy_count').html( cartMenuN );
	$('#goods_num').val(cartMenuN);
	doProduct(price)
}
function doProduct (price) {
	price = price / 100;
	var cartMenuN = $('#buy_count').html();
	$('#totalNum').html( cartMenuN );
	var totalPrice = parseFloat(price * cartMenuN);
	$('#totalPrice').html( totalPrice.toFixed(2) );
}

function submitTxOrder () {

	if(!confirm("您确认需要提现吗？"))
	{
		return false;
	}

	$.ajax({
		type : 'POST',
		url : '/cash.html',
		data : {
			amount : $('#amount').val(),
			bank : $('#bank').val()
		},
		success : function (response , status , xhr) {
			if(response==true)
			{
				alert("提现成功");
				location.reload();
			} else {
				alert("提现失败");
			}
		},
		beforeSend : function(){
			$('#tx-menu-shadow').show();
			$('#txshowcard').hide();
		},
		complete : function(){
			$('#tx-menu-shadow').hide();
			$('#txshowcard').show();
		}
	});
}

function submitOrder () {
	//获取订单信息
	var json = '';
	$('#ullist li').each(function () {
		var name = $(this).find('span[name=title]').html();		//用户名称
		var num = $(this).find('span[class=count]').html();		//商品数量
		var price = $(this).find('span[class=price]').html();	//商品单价
		var id = $(this).find('span[class=idss]').html();		//产品id
		json += '{"name":"'+name+'","num":"'+num+'","price":"'+price+'","id":"'+id+'"},';	
		
	});
	json = json.substring(0 , json.length-1);
	json = '['+json+']';
	if($('#totalPrice').html()<=0)
	{
		alert('请选择商品');
		return false;
	}
	var name = $('#name').val();
	var phone = $('#phone').val();
	var weixin = $('#weixin').val();
	var address = $('#address').val();
	var s_province = $('#s_province').val();
	var s_city = $('#s_city').val();
	var s_county = $('#s_county').val();
	var coin =$('#coin').val();	//折扣金额
	var maxcoin=$('#maxcoin').val();
	var reg=/^\d+(\.)?(\d{1,2})?$/;
	if(typeof(coin)!='undefined' && coin!='')
	{
		coin=parseFloat(coin);
		maxcoin=parseFloat(maxcoin);
		if(coin<0){
			alert('抱歉，余额使用不能为负数');
			return false;
		}else if(reg.test(coin)==''){
			alert('抱歉，余额使用最多为小数点后两位');
			return false;
		}else if(coin>maxcoin){
			alert('抱歉，您最多只能使用'+maxcoin+'元');
			return false;
		}
	}

	if(province_check)
	{
		if(s_province=='')
		{
			alert('请选择省份');
			return false;
		}
		
		if(s_city=='城市')
		{
			//alert('请选择城市');
			//return false;
		}
		
		var user_address = s_province+','+s_city+','+s_county+','+address;
	}
	else
	{
		var user_address = address;
	}
	
	
	if(s_county=='区域')
	{
		s_county = '';
	}
	
	if(name.length<=0)
	{
		alert('请输入名称');
		return false;
	}
	
	if(phone.length<=0)
	{
		alert('请填写手机号！');
		return false;
	}
	/*2016-1-11 下单信息校验优化*/
	var pattern = /^1[34578]\d{9}$/; 
	if(!pattern.test(phone)){
		alert('请填写正确格式的手机号码！');
		return false;
	}
	/*/下单信息校验优化*/
	
	if(address.length<=0)
	{
		alert('请输入地址');
		return false;
	}
	
	$.ajax({
		type : 'POST',
		url : appurl+'/App/Index/addorder',
		data : {
			uid : $_GET['uid'],
			cartData : json,
			userData : $('form').serializeArray(),			
			totalPrice : $('#totalPrice').html(),
			coin:coin,
			user_address:user_address,
			good_num_cnt:good_num_cnt,
			good_num_id:good_num_id
		},
		success : function (response , status , xhr) {
			
			if (response.msg) {
				alert(response.msg);return false;
			}
			
			$('#user').click();
			$('#ullist').find('li').remove();
			$('.reduce').each(function () {
				$(this).children().css('background','');
			});
			$('#totalNum').html(0);
			$('#cartN2').html( 0 );
			$('#totalPrice').html(0);
			
			if (response) {
				window.location.href=response;return false;
			}
			
			$.ajax({
				type : 'POST',
				url : appurl+'/App/Index/getorders',
				data : {
					uid : $_GET['uid']
				},
				success : function (response , status , xhr){
					if(response){
						var json = eval(response); 
						var html = '';
						var order_status = '';
						
						$.each(json, function (index, value) {
							var pay = '';
							var order = '';
							if (value.order_status == '0'){
								order_status = 'no';
								order = '未发货';
							}else if ( value.order_status == '1'){
								order_status = 'no';
								var confirm_url = appurl+'/App/Index/confirm_order?id='+value.orderid+'&uid='+$_GET['uid'];
								order = '<a href="'+confirm_url+'" style="color:red">确认收货</a>';
							}else if ( value.order_status == '4'){
								order_status = 'no';
								order = '已退货';
							}else{
								order_status = 'ok';
								order = '已完成';
							}
							
							if (value.pay_status == '0'){
								pay_status = 'no';
								pay = '<a href="'+value.pay_url+'">去支付</a>';
							}else if ( value.pay_status == '1'){
								pay_status = 'ok';
								pay = '已支付';
							}
							//html += '<tr><td>'+value.orderid+'</td><td class="cc">'+value.totalprice+'元</td><td class="cc"><em class="'+pay_status+'">'+pay+'</em></td><td class="cc"><em class="'+order_status+'">'+order+'</em></td></tr>';
						
							html += '<li style="border: 1px solid #d0d0d0;border-radius: 10px;margin-bottom:10px;background-color:#FFF;"><table><tr><td style="border-bottom:0px">订单编号:'+value.orderid+'</td></tr>';
							html += '<td style="border-bottom:0px">订单金额:'+value.totalprice+'元</td></tr>';
							html += '<td style="border-bottom:0px">订单时间:'+value.time+'</td></tr>';
							html += '<td style="border-bottom:0px">支付状态:<em class="'+pay_status+'">'+pay+'</em>';
							if (value.pay_status == '0')
							{
								html += '<a href="'+value.pay_url+'">(已经支付?)</a>';
							}
							html += '</td></tr>';
							if(value.order_status == '1')
							{
								html += '<td style="border-bottom:0px">订单状态:<em class="'+order_status+'" style="background-color:#FFFF00;">'+order+'</em></td></tr>';
							}
							else
							{
								html += '<td style="border-bottom:0px">订单状态:<em class="'+order_status+'">'+order+'</em></td></tr>';
							}
						
							
							html += '<td style="border-bottom:0px">商品名称:'+value.cart_name+'</td></tr>';
							html += '<td style="border-bottom:0px">订单详情:'+value.note+'</td></tr>';
							
							html += '<td style="border-bottom:0px">快递公司:'+value.order_info_name+'</em></td></tr>';
							html += '<td style="border-bottom:0px">快递单号:'+value.order_info_num+'</em></td></tr>';
							
							html += '</table></li>';
						
						});
						$('#orderlistinsert').empty();
						$('#orderlistinsert').append( html );
					}
				},
				beforeSend : function(){
	    			$('#page_tag_load').show();
		    	},
		    	complete : function(){
		    		$('#page_tag_load').hide();
		    	}
			});
		},
		beforeSend : function(){
			$('#menu-shadow').show();
			$('#showcard').hide();
		},
		complete : function(){
			$('#menu-shadow').hide();
			$('#showcard').show();
		}
	});
	

}
var $_GET = (function(){
	var url = window.document.location.href.toString();
	var u = url.split("?");
	if(typeof(u[1]) == "string"){
		u = u[1].split("&");
		var get = {};
		for(var i in u){
			var j = u[i].split("=");
			get[j[0]] = j[1];
		}
		return get;
	} else {
		return {};
	}
})();
String.prototype.colorHex = function(){
	var that = this;
	if(/^(rgb|RGB)/.test(that)){
		var aColor = that.replace(/(?:\(|\)|rgb|RGB)*/g,"").split(",");
		var strHex = "#";
		for(var i=0; i<aColor.length; i++){
			var hex = Number(aColor[i]).toString(16);
			if(hex === "0"){
				hex += hex;	
			}
			strHex += hex;
		}
		if(strHex.length !== 7){
			strHex = that;	
		}
		return strHex;
	}else if(reg.test(that)){
		var aNum = that.replace(/#/,"").split("");
		if(aNum.length === 6){
			return that;	
		}else if(aNum.length === 3){
			var numHex = "#";
			for(var i=0; i<aNum.length; i+=1){
				numHex += (aNum[i]+aNum[i]);
			}
			return numHex;
		}
	}else{
		return that;	
	}
};

var good_num_cnt = 'null';
var good_num_id = 0;
var good_price_key = '';
var good_price = 0;
var good_old_price = 0;
function showDetail(price){
	console.log("showDetail");
	$('body').show();
	$('.disgood').each(function(){
		$(this).hide();
	});
	$('#mcover').show();
	
	check_shengyu();
	
	$('#add_cart').click(function(){
		doProduct(price);
	});		
}
function check_shengyu()
{
	
}

var order_list = new Array();


function in_array(search,array){
    for(var i in array){
        if(array[i]==search){
            return true;
        }
    }
    return false;
}

function now_buy(){
	$('#add_cart').click();
	$('#cart').click();
}

function showMenu(){
	$("#menu").find("ul").toggle();
}
function toggleBar(){
	$(".footermenu ul li a").each(function(){
		$(this).attr("class","");
	});
	$(this).children("a").attr("class","active");
}
function showProducts(id) {
	$('#menu_id li').each(function(){
		if( $(this).attr("menu") == id ){
			$(this).show();
		}else{
			$(this).hide();
		}
	});
	$('#menu ul').hide();
}
function showAll() {
	$('#menu_id li').each(function(){
		$(this).show();
	});
	$('#menu ul').hide();
}
