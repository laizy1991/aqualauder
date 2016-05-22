/**
 * @description: 用户注册登陆
 * @author: lox@warthog.cn
 * @date: 2015-09-17
 */

define(function(require) {
    require('../../web/common/underscore');
    var dd = require('../../web/common/dialog');
    var ajax = require('../../web/common/ajax');

    function checkValidParam(param,minLen,maxLen)
    {
        if(param.length < minLen || param.length > maxLen)
        {
            return false;
        }
        return true;
    }

    var User = {
        init: function() {
            this.resetPwd($('#resetPwdBtn'));
            this.userLogoff($('#logoffBtn'));
        },
        userLogoff:function(obj){
            obj.click(function(event){
                //显示对应的弹框
                dd.tip('是否确认退出！','提示','退出',function(){
                    location.href = '/user/logOff.html';
                });
            });
        },
        resetPwd: function(obj) {
            obj.click(function(event) {
                var newPwdEle = $("#newPwd"),
                    oldPwdEle = $("#oldPwd"),
                    newPwdEle2 = $("#newPwd2");
                if(!checkValidParam(newPwdEle.val(),1,100) || !checkValidParam(oldPwdEle.val(),1,100) || !checkValidParam(newPwdEle2.val(),1,100))
                {
                    dd.alert("请先输入密码");
                    return;
                }else{
                    if(!checkValidParam(newPwdEle.val(),4,20) || !checkValidParam(oldPwdEle.val(),4,20) || !checkValidParam(newPwdEle2.val(),4,20)){
                        dd.alert("密码长度必须大于等于4");
                        return false;
                    }
                }

                if (newPwdEle.val() !== newPwdEle2.val()) {
                    dd.alert("新密码两次输入不相同，请重新确认");
                    return false;
                }

                //获取请求后面的参数
                var url  = '/myspace/resetPwd';
                var params = {
                    'newPwd':$.md5(newPwdEle.val()),
                    'oldPwd':$.md5(oldPwdEle.val())};
                ajax.post(url,params, function (result) {
                    if(result && result.success)
                    {
                        //跳转到下个页面
                        location.href = decodeURIComponent(result.data);
                    }else if(result.errorMsg){
                        dd.alert(result.errorMsg);
                    }else{
                        dd.alert("重置密码失败，请稍后重试");
                    }
                });
            });
        },

    };

    User.init();
});

