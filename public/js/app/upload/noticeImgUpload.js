/**
 */

define(function(require) {
    require('../../thirdParty/swfupload/swfupload');
    var dd = require('../util/dialog');

    window.onload = function () {
        window.swfu = new SWFUpload({
            upload_url: "/ajax.noticeCtrl/upload",
            use_query_string:true,
            file_size_limit : "1 MB",	// 文件大小控制
            file_types : "*.jpg;*.jpeg;*.png;*.gif;*.bmp",
            file_types_description : "All Files",
            file_upload_limit : "0",
            file_post_name: 'fileData',
            file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
            upload_success_handler : uploadSuccess,
            upload_complete_handler : uploadComplete,
            file_queue_error_handler : fileQueueError,
            button_placeholder_id : "placeholder",
            button_width: 120,
            button_height: 42,
            button_text_top_padding: 0,
            button_text_left_padding: 18,
            button_action: SWFUpload.BUTTON_ACTION.SELECT_FILE,
            button_cursor: SWFUpload.CURSOR.HAND,

            // Flash Settings
            flash_url : "/public/js/thirdParty/swfupload/swfupload.swf",

            custom_settings : {
                upload_target : "divFileProgressContainer"
            },
            debug: false
        });
    };

    function fileQueueError(file, errorCode, message) {
        try {
            switch (errorCode) {
                case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
                    dd.alert("文件大小为0");
                    break;
                case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
                    dd.alert("文件大小为超过" + swfu.settings.file_size_limit);
                    break;
                case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
                    dd.alert("文件格式错误")
                    break;
                case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
                    dd.alert("一次上传太多文件")
                    break;
                default:
                    dd.alert(message);
                    break;
            }

        } catch (ex) {
            this.debug(ex);
        }
    }

    function fileDialogComplete(numFilesSelected, numFilesQueued) {
        try {
            if (numFilesQueued > 0) {
                this.startUpload();
            }
        } catch (ex) {
            this.debug(ex);
        }
    }

    function uploadSuccess(file, serverData) {
        serverData = JSON.parse(serverData);
        if(serverData.success) {
            ImgEditor.addImg(serverData.message);
        }
    }

    function uploadComplete() {
        try {
            if (this.getStats().files_queued > 0) {
                this.startUpload();
            }
        } catch (ex) {
            this.debug(ex);
        }
    }
});
