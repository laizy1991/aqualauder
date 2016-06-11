/**
 */

define(function(require) {
    require('../common/common');  // 公共模块
    require('../../thirdParty/dialog');  // 弹窗插件
    require('../../thirdParty/underscore');
    
    var ajax = require('../util/ajax');
    var dd = require('../util/dialog');

    var Initiator = {
        init: function() {
            this.create($('[role="create"]'));
            this.delete($('[role="delete"]'));
            this.update($('[role="update"]'));
            this.view($('[role="view"]'));
        },

        /**
         * 添加
         */
        create: function(obj) {
            var self = this;

            obj.click(function() {
                var $this = $(this);

                var createDialog = dialog({
                    id: 'createDialog',
                    title: '添加',
                    content: document.getElementById('createDialogTmpl').innerHTML,
                    button: [
                        {
                            value: '保存',
                            callback: function () {
                                var dia = this,
                                    $form = this.__popup.find('form');

                                ajax.post($form.attr('action'), $form.serialize(), function(result){
                                    if(result.success){
                                        dia.close();
                                        dd.alert('添加成功！', function(){
                                            window.location.reload(false);
                                       });
                                    }else{
                                        dd.alert(result.error);
                                    }
                                });
                                return false;
                            },
                            autofocus: true
                        }
                    ],
                    cancelValue: '取消',
                    cancel: function() {

                    },
                    onshow: function() {

                    }
                }).showModal();
            });
        },



        /**
         * 删除
         */
        delete: function(obj) {
            obj.click(function() {
                var $this = $(this),
                    $wrap = $this.closest('tr'),
                	id = $wrap.find('.id').val(),
                    outTradeNo = $wrap.find('.outTradeNo').val(),
                    deleteDialog = dialog({
                    id: 'deleteDialog',
                    title: '删除',
                    content: document.getElementById('deleteDialogTmpl').innerHTML,
                    button: [
                        {
                        	value: '确定',
                            callback: function () {
                                var dia = this,
                                $form = this.__popup.find('form');

                                ajax.post($form.attr('action'), $form.serialize(), function(result){
                                    if(result.success){
                                        dia.close();
                                        dd.alert('删除成功！', function(){
                                            window.location.reload(false);
                                       });
                                    }else{
                                        dd.alert(result.error);
                                    }
                                });
                                return false;
                            },
                            autofocus: true
                        }
                    ],
                    cancelValue: '取消',
                    cancel: function() {

                    },
                    onshow:function() {
                    	$("#idToDelete").val(id);
                        $("#outTradeNoToDelete").text(outTradeNo);
                    }
                }).showModal();
            });
        },

        /**
         * 修改
         */
        update: function(obj) {
            obj.click(function() {
                var $this = $(this),
                    $wrap = $this.closest('tr'),
                    id = $wrap.find('.id').val(),
                    outTradeNo = $wrap.find('.outTradeNo').val(),
                    //expressId = $wrap.find('.expressId').val(),
                    expressNum = $wrap.find('.expressNum').val(),
                    forbidRefund = $wrap.find('.forbidRefund').val(),
                    shippingAddress = $wrap.find('.shippingAddress').val(),
                    state = $wrap.find('.state').val(),
                    updateDialog = dialog({
                    id: 'updateDialog',
                    title: '修改',
                    content: document.getElementById('updateDialogTmpl').innerHTML,
                    button: [
                        {
                        	value: '确定',
                            callback: function () {
                                var dia = this,
                                $form = this.__popup.find('form');
                                
                                ajax.post($form.attr('action'), $form.serialize(), function(result){
                                    if(result.success){
                                        dia.close();
                                        dd.alert('修改信息成功！', function(){
                                            window.location.reload(false);
                                       });
                                    }else{
                                        dd.alert(result.error);
                                    }
                                });
                                return false;
                            },
                            autofocus: true
                        }
                    ],
                    cancelValue: '取消',
                    cancel: function() {

                    },
                    onshow:function() {
                    	$("#idToUpdate").val(id);
                    	$("#outTradeNoToUpdate").text(outTradeNo);
                        //$("#expressIdToUpdate").val(expressId);
                        $("#expressNumToUpdate").val(expressNum);
                        $("#forbidRefundToUpdate").val(forbidRefund);
                        $("#stateToUpdate").val(state);
                        $("#shippingAddressToUpdate").val(shippingAddress);
                    }
                }).showModal();
            });
        },

        /**
         * 查看
         */
        view: function(obj) {
            obj.click(function() {
                var $this = $(this),
                    $wrap = $this.closest('tr'),
                    id = $wrap.find('.id').val(),
                    userId = $wrap.find('.userId').val(),
                    outTradeNo = $wrap.find('.outTradeNo').val(),
                    payType = $wrap.find('.payType').val(),
                    expressId = $wrap.find('.expressId').val(),
                    expressNum = $wrap.find('.expressNum').val(),
                    totalFee = $wrap.find('.totalFee').val(),
                    state = $wrap.find('.state').val(),
                    forbidRefund = $wrap.find('.forbidRefund').val(),
                    orderMemo = $wrap.find('.orderMemo').val(),
                    receiver = $wrap.find('.receiver').val(),
                    mobilePhone = $wrap.find('.mobilePhone').val(),
                    shippingAddress = $wrap.find('.shippingAddress').val(),
                    stateHistory = $wrap.find('.stateHistory').val(),
                    payTime = $wrap.find('.payTime').val(),
                    deliverTime = $wrap.find('.deliverTime').val(),
                    recevTime = $wrap.find('.recevTime').val(),
                    finishTime = $wrap.find('.finishTime').val(),
                    createTime = $wrap.find('.createTime').val(),
                    updateTime = $wrap.find('.updateTime').val(),
                    viewDialog = dialog({
                    id: 'viewDialog',
                    title: '查看',
                    content: document.getElementById('viewDialogTmpl').innerHTML,
                    button: [],
                    cancelValue: '关闭',
                    cancel: function() {

                    },
                    onshow:function() {
                        var stateList = {"0":"创建订单(待支付)","1":"完成支付","2":"待发货","3":"完成发货","4":"确认收货","5":"交易成功(不可退货)","6":"交易关闭"};
                        var forbidRefundList = {"0":"允许退款", "1":"禁止退款"}
                        $("#idToView").text(id);
                        $("#userIdToView").text(userId);
                        $("#outTradeNoToView").text(outTradeNo);
                        $("#payTypeToView").text(payType=="0"?"微信支付":payType=="1"?"余额":"未知");
                        //$("#expressIdToView_" + expressId).show();
                        $("#expressNumToView").text(expressNum);
                        $("#totalFeeToView").text(totalFee);
                        $("#stateToView").text(stateList[state]);
                        $("#forbidRefundToView").text(forbidRefundList[forbidRefund]);
                        $("#orderMemoToView").text(orderMemo);
                        $("#receiverToView").text(receiver);
                        $("#mobilePhoneToView").text(mobilePhone);
                        $("#shippingAddressToView").text(shippingAddress);
                        $("#stateHistoryToView").text(stateHistory);
                        $("#payTimeToView").text(payTime);
                        $("#deliverTimeToView").text(deliverTime);
                        $("#recevTimeToView").text(recevTime);
                        $("#finishTimeToView").text(finishTime);
                        $("#createTimeToView").text(createTime);
                        $("#updateTimeToView").text(updateTime);
                    }
                }).showModal();
            });
        }
        
    }

    Initiator.init();
});
