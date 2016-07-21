/**
 */

define(function(require) {
    require('../common/common');  // 公共模块
    require('../../thirdParty/dialog');  // 弹窗插件

    var ajax = require('../util/ajax');
    var dd = require('../util/dialog');


    var Initiator = {
        init: function() {
            this.onDelete();
            this.onSubmit();


            window.ImgEditor = {};
            ImgEditor.addImg = function (picUrl, activityUrl, sortNum) {
                picUrl = picUrl?picUrl:"";
                activityUrl = activityUrl?activityUrl:"";
                sortNum = sortNum?sortNum:0;

                var tmpl = $('#pictureTmpl').html();
                tmpl = tmpl.replace("{picUrl}", picUrl);
                tmpl = tmpl.replace("{activityUrl}", activityUrl);
                tmpl = tmpl.replace("{sortNum}", sortNum);
                var dom = $(tmpl);
                $("#pictureList").append(dom);
            }

            ImgEditor.getImgs = function () {
                var imgs = [];
                $("#pictureList .picture-container").each(function (index, obj) {
                    var img = {};
                    img.picUrl = $(obj).find(".picUrl").attr("src");
                    img.activityUrl = $(obj).find(".activityUrl").val();
                    img.sortNum = $(obj).find(".sortNum").val();
                    img.sortNum = img.sortNum?img.sortNum:0;
                    imgs.push(img);

                });
                return imgs;
            }

            var imgs = $("input[name='imgs']").val();
            try {
                imgs = JSON.parse(imgs)
            } catch(e) {
                imgs = [];
            }
            $(imgs).each(function (index, value) {
                ImgEditor.addImg(value.picUrl, value.activityUrl, value.sortNum )
            })
        },


        /**
         * 删除图片
         */
        onDelete: function() {
            $("#pictureList").delegate('[role="delete"]',"click",function() {
                $(this).closest('.picture-container').remove();
            });
        },

        onSubmit: function() {
            $("[role='update']").click(function(){
                var form = $("#update");
                updateDialog = dialog({
                    id: 'updateDialog',
                    title: '修改',
                    content: "确定修改公告信息?",
                    button: [
                        {
                            value: '确定',
                            callback: function () {
                                var dia = this;
                                var data={};
                                data.desc = form.find("[name='desc']").val();
                                data.imgs = ImgEditor.getImgs();
                                ajax.post(form.attr('action'), data, function(result){
                                    if(result.success){
                                        dia.close();
                                        dd.alert('修改信息成功！', function(){
                                            window.location.reload(false);
                                        });
                                    }else{
                                        dia.close();
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

                    }
                }).showModal();
            });
        }

    }

    Initiator.init();

});
