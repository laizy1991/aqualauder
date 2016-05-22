/**
 * @description:  工具类
 */
define(function(require, exports, module) {
    require('./underscore.js');  //


    /**
     * 提示框
     * @param {Object} msg
     * @param {Object} title
     * @param {Object} title
     * @param {Object} callback 确定后的回调操作
     */
    exports.tip = function (msg, title,btnTxt,callback) {
        if(!msg || msg.length < 1)
        {
            msg = '是否确认！';
        }

        if(!title || title.length < 1)
        {
            title = '提示';
        }

        if(!btnTxt || btnTxt.length < 1)
        {
            btnTxt = '确认';
        }
        if ($("#dialog-m-tip").length > 0) {
            $("#dialog-m-tip").remove();
        }
        var bodyHeight = $("body").height();
        var bodyWidth =  $("body").width();
        var noticeMsg = '<div id="dialog-m-tip" style="width:'+(bodyWidth-32)+'px;z-index: 1032;margin-left: 16px;margin-right: 16px;background-color: white;border-radius: 4px;position:absolute;">' +
            '<div style="text-align: left;margin-left: 16px;margin-right: 16px;color: #e05012;height: 48px;border-bottom: 2px solid  #e05020;margin-top: 15px;">' + title + '</div>' +
            '<div style="padding-left: 16px;padding-right: 16px;text-align: left;font-size: 16px;color: #353535;height:48px;margin-left: 10px;margin-top: 15px;">' + msg + '</div>' +
            '<div style="font-size: 16px;color: #353535;height:48px;border-top: 1px solid #ebebeb;">'
            +'<input id="dialog-cancel-btn" style="height: 100%;width: 50%;border-right: 1px solid #ebebeb;background: transparent;" type="button" value="取消">'+
            '<input id="dialog-confirm-btn" style="height: 100%;width: 50%;background: transparent;" type="button">'+ '</div>' +
            '</div>';
        $("body").append(noticeMsg);
        var tipEle = $("#dialog-m-tip");
        //tipEle.width((bodyWidth-32));
        tipEle.css("top",bodyHeight*0.3+"px");
        //背景显示
        var noticeBg = '<div id="dialog-m-tip-bg"  style="z-index: 1031; position: fixed; left: 0px; top: 0px; width: '+window.screen.width+'px; height: 100%; overflow: hidden; -webkit-user-select: none; opacity: 0.7;filter: progid:DXImageTransform.Microsoft.Alpha(opacity=70); background: rgb(0, 0, 0);"></div>';
        $("body").append(noticeBg);
        //添加点击事件
        var cancelBtn = $("#dialog-cancel-btn");
        cancelBtn.click(function(){
            $("#dialog-m-tip").remove();
            $("#dialog-m-tip-bg").remove();
        });
        var confirmBtn = $("#dialog-confirm-btn");
        confirmBtn.val(btnTxt);
        confirmBtn.click(callback);
    };

    /**
     * 提示框
     * @param {Object} msg
     * @param {Object} callback 确定后的回调操作
     */
    exports.alert = function(msg, callback) {
        if($(".dialog-m-alert").length>0){
            $(".dialog-m-alert").remove();
        }
        var noticeMsg = '<div class="dialog-m-alert"><div class="dialog-m-alert-msg">'+ msg +'</div></div>',
            width = 20*msg.length,
            height = $(".dialog-m-alert").height();
        $("body").append(noticeMsg);
        $(".dialog-m-alert").width(width);
        $(".dialog-m-alert").css("marginLeft","-"+width/2+"px");
        var wh = $("body").height();
        $(".dialog-m-alert").css("marginTop",wh*0.25+"px");
        $(".dialog-m-alert").fadeOut(3000);

    };


    /**
     * 加载中...
     */
    exports.loading = function() {
        var loadingDiv = '<div class="dialog-m-loading"><img class="dialog-m-loading-img" src="../../public/images/loading-m.gif"></div>',
            shade='<div id="m-dialog" class="dialog-m-loading-shade"></div>',
            width = 48;
        $("body").append(loadingDiv);
        $("body").before(shade);
        $(".dialog-m-loading").width(width);
        $(".dialog-m-loading").height(width);
        $(".dialog-m-loading").css("marginLeft","-"+width/2+"px");
        $(".dialog-m-loading").css("marginTop","-"+width/2+"px");

        this.close = function () {
            $(".dialog-m-loading").remove();
            $(".dialog-m-loading-shade").remove();
        }
        return this;
    };

});