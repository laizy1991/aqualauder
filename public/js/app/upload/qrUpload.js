/**
 */

define(function(require) {
    require('../../thirdParty/swfupload/swfupload');
    var dd = require('../util/dialog');

    window.initImgUploader = function () {
        window.swfu = new SWFUpload({
            upload_url: "/ajax.qrShareCtrl/upload",
            use_query_string:true,
            // File Upload Settings
            file_size_limit : "1 MB",	// 文件大小控制
            file_types : "*.jpg;*.jpeg;*.png;*.gif;*.bmp",
            file_types_description : "All Files",
            file_upload_limit : "0",
            file_post_name: 'fileData',
            file_queue_error_handler : fileQueueError,
            file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
            file_queued_handler : fileQueueError,
            upload_progress_handler : uploadProgress,
            upload_error_handler : uploadError,
            upload_success_handler : uploadSuccess,
            upload_complete_handler : uploadComplete,
            button_action: SWFUpload.BUTTON_ACTION.SELECT_FILE, //一次只能选一个文件
            button_width: 180,
            button_height: 315,
            button_placeholder_id : "fileSelectButton",
            flash_url : "/public/js/thirdParty/swfupload/swfupload.swf",
            debug: false,
        });
    };

    function fileQueueError(file, errorCode, message) {
        try {

            switch (errorCode) {
                case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
                    imageName = dd.alert("文件大小为0");
                    break;
                case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
                    imageName = dd.alert("文件大小为超过" + swfu.settings.file_size_limit);
                    break;
                case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
                    dd.alert("文件格式错误!")
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

    function fileQueued(file){
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

    function uploadProgress(file, bytesLoaded) {

        try {
            //var percent = Math.ceil((bytesLoaded / file.size) * 100);
        } catch (ex) {
            this.debug(ex);
        }
    }

    function uploadSuccess(file, serverData) {
        try {
            serverData = JSON.parse(serverData)
            if(serverData.success) {
                console.log(serverData)
                $("#imgUrlOnHiddenToUpdate").val(serverData.data.imgUrl);
                $("#imgPathOnHiddenToUpdate").val(serverData.data.imgPath);
                $("#imgUrlToUpdate").attr("src", serverData.data.imgUrl);
            }

        } catch (ex) {
            this.debug(ex);
        }
    }

    function uploadComplete(file) {
        try {

        } catch (ex) {
            this.debug(ex);
        }
    }

    function uploadError(file, errorCode, message) {
        try{

        } catch (ex3) {
            this.debug(ex3);
        }

    }
});
