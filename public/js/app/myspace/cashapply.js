/**
 * @description: 提现交互
 * @author: coming@warthog.cn
 * @date: 2015-10-8
 */

define(function(require) {
	require('../../web/common/underscore'); //
	var dd = require('../../web/common/dialog');

	var smsWait = 60;
    function waitGetSms(smsEle)
    {
        if(smsWait == 0)
        {
            smsEle.removeAttr("disabled");
            smsEle.css("color","#6484b1");
            smsEle.val("点击再次获取");
            $("#smsTips").html("");
            smsWait = 60;
        }else
        {
            smsEle.attr("disabled",true);
            smsEle.css("color","#aaaaaa");
            smsEle.val(smsWait+"s");

            smsWait--;
            setTimeout(function(){waitGetSms(smsEle)},1000);
        }
    }
	
	var CashApply = {
		init: function() {
			this.submitForm($('#formBtn'));
			this.getSmsCode($('#smsBtn'))
		},

		 /**
         * 获取短信验证码
         * @param obj
         */
        getSmsCode:function(obj)
        {
            obj.click(function(event){
                //ajax发送请求到服务器
                $.ajax({
					type:'POST',
					async:true,
					data:{},
					url:'/myspace/requestSMS',
					dataType:'json',
					success:function(result){
						if(result && result.success){
			                $("#smsTips").html(result.data);
							$("#smsVerify").val("");
						} else {
							if(_.isMobile()){
	                            dd.alert("发送验证码失败，请稍后重试");
	                        } else{
	                            smsTipWrap.show();
	                            smsTip.html("发送验证码失败，请稍后重试");
	                        }
	                        smsWait = 0;
						}
					}
				});
                
                
                var smsEle = $("#smsBtn");
                //开启定时器
                waitGetSms(smsEle);
            });
        },
		
		/**
		 * 提现
		 * @param obj
		 */
		submitForm: function (obj) {
			obj.click(function () {

				if(_.isEmpty($('#realName').val())) {
					if(_.isMobile()){
						dd.alert("请输入姓名");
					}else{
						$('#errorMemo').html("请输入姓名");
						$('#realName').focus();
					}
					return false;
				}
				if(_.isEmpty($('#payAccount').val())) {
					if(_.isMobile()){
						dd.alert("请输入支付宝账号");
					}else{
						$('#errorMemo').html("请输入支付宝账号");
						$('#payAccount').focus();
					}
					return false;
				}
				if(_.isEmpty($('#cashAmount').val())) {
					if(_.isMobile()){
						dd.alert("请输入提现金额");
					}else{
						$('#errorMemo').html("请输入提现金额");
						$('#cashAmount').focus();
					}
					return false;
				}else{
					if(isNaN($('#cashAmount').val()) || $('#cashAmount').val() <= 0){
						if(_.isMobile()){
							dd.alert("金额必须为大于0");
						}else{
							$('#errorMemo').html("金额必须为大于0");
							$('#cashAmount').select();
						}
						return false;
					}
				}
				
				if(_.isEmpty($('#smsVerify').val())){
					$.ajax({
						type:'POST',
						async:true,
						data:{'payAccount':$('#payAccount').val()},
						url:'/myspace/checkPayAccount',
						dataType:'json',
						success:function(result){
							if(result && result.success){
								$('#cashForm').submit();
							} else {
								if(result.errorCode == 2) {
					                var smsEle = $("#smsBtn");
					                //开启定时器
					                waitGetSms(smsEle);
									$("#updatePayAccountVerify").removeClass("none");
									$("#smsTips").html(result.errorMsg);
								} else if(result.errorCode == 3) {
									window.location.href="/user/inputMobile?redirectUrl=" + result.errorMsg;
								} else {
									$('#errorMemo').html(result.errorMsg);
								}
							}
						}
					});
				} else {
					$('#cashForm').submit();
				}
			});
		}
	};

	CashApply.init();
});