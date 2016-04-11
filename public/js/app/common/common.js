define(function(require) {
    require('../../thirdParty/dialog');  // 弹窗插件
    var dd = require('../util/dialog');
    var ajax = require('../util/ajax');
    var Common = {
        init: function() {
            this.dropdown();
            this.placeholder();
            this.sidebar();
            this.updateSessionInfo($('[role="updateSessionInfo"]'));
            this.IntResizer();
        },

        // 下拉菜单
        dropdown: function() {
            var self = this,
                duration = 300;

            $('.dropdown').hover(function() {
                var $this = $(this);

                $this.addClass('open');

            }, function() {
                var $this = $(this);

                $this.removeClass('open');
            });
        },

        // placeholder for lt IE10
        placeholder: function() {
            require('./module/placeholder');
            $('input, textarea').placeholder();
        },
        
        sidebar: function () {
            //$("dd:not(:first)").hide();
            $(".nav-bd a").each(function () {
                if($(this).is(".current")){
                    $("dd:visible").slideUp(100);
                    $(this).parent().slideDown(100);
                    $(this).parent().parent().find("dt").css("color","black");
                }
            });
            $("dt").click(function () {
                if($(this).next().is(":visible")){
                    $(this).next().slideUp(100);
                    return;
                }
                $("dd:visible").slideUp(100);
                $(this).parent().children().next().slideDown(100);
                return false;
            });
        },

        //更新用户权限
        updateSessionInfo: function (obj) {
            obj.click(function () {
                ajax.get("/ajax/User/updateSessionInfo",function (result) {
                    if(result.success){
                        window.location.reload(false);
                    }else{
                        dd.alert("操作失败！");
                    }
                });
            });
        },

        IntResizer: function() {
            var headerHeight = $(".header").height();
            var minHeight = 600;
            var resize = function() {
                var windowHeight = $(window).height();
                if(windowHeight >= minHeight) {
                    $(".content").height(windowHeight - headerHeight);

                }
            }

            window.onresize = resize;
            var contentHeight = $(window).height()-headerHeight;
            $(".content").height( contentHeight> minHeight?contentHeight: minHeight);
        }
    };

    Common.init();
});