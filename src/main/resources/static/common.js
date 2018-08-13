/**
 * ajax方法
 * @param [api] :后台地址 例如 nameList/show/data
 * @param [data] :formData form表单提交的数据.
 * @param [callback] :回调函数 调用处直接data即可得到后台JsonResult中data字段的值
 */
function send(api, data, callback) {
    var index;
    $.ajax({
        type: "POST",
        url: getRealPath() + api,
        dataType: "json",
        data: data,
        beforeSend: function () {
            index = loading();
        },
        success: function (res) {
            closeLoading(index);
            if (res.code == '0') {
                if (callback && typeof(callback) === 'function') {
                    callback(res.data);
                }
            } else {
                alertFaild(res.msg);
            }
        },
        error: function (e) {
            closeLoading(index);
            alertError(e.status + " | " + e.statusText);
        }
    });
}

/**
 * 弹框提醒层 - 错误
 * @param [content] 内容
 * @param [yes] 回调
 */
function alertError(content, yes) {
    top.layer.alert(content, {icon: 2, title: '错误'}, yes);
}

/**
 * 弹框提醒层 - 成功
 * @param [content] 内容
 * @param [yes] 回调
 */
function alertSuccess(content, yes) {
    top.layer.alert(content, {icon: 1, title: '成功'}, yes);
}

/**
 * 弹框提醒层 - 失败
 * @param [content] 内容
 * @param [yes] 回调
 */
function alertFaild(content, yes) {
    top.layer.alert(content, {icon: 5, title: '失败'}, yes);
}

/**
 * 消息提醒层 - 错误
 * @param [content] 内容
 * @param [end] 回调
 */
function msgError(content, end) {
    top.layer.msg(content, {icon: 2, title: false}, end);
}

/**
 * 消息提醒层 - 成功
 * @param [content] 内容
 * @param [end] 回调
 */
function msgSuccess(content, end) {
    top.layer.msg(content, {icon: 1, title: false}, end);
}

/**
 * 消息提醒层 - 失败
 * @param [content] 内容
 * @param [end] 回调
 */
function msgFail(content, end) {
    top.layer.msg(content + "", {icon: 5, title: false}, end);
}

/**
 * 消息提醒层 - 2秒后自动消失
 * @returns {string}
 */
function msg(content) {
    top.layer.msg(content, {time: 2000});
}


/**
 * 弹出iframe层标准样式.
 * @param {String} title 标题
 * @param {String} content 内容.此处目前为url
 * @param {Function} end 回调方法,层关掉的时候回调.
 * @param {String} area iframe的宽度,不设置默认1000px.高度自适应.
 */
function openIframe(title, content, end, area) {
    top.layer.open({
        type: 2,
        title: title,
        content: getRealPath() + content,
        offset: '100px',
        area: area ? area : '1000px',
        success: function (layero, index) {
            top.layer.iframeAuto(index);
        },
        end: end
    })
}

function getRealPath() {
    var pathName = window.location.pathname.substring(1);
    var webName = pathName == '' ? '' : pathName.substring(0, pathName.indexOf('/'));
    if (webName == '') {
        return window.location.protocol + '//' + window.location.host + '/';
    } else {
        return window.location.protocol + '//' + window.location.host + '/' + webName + '/';
    }
}


function loading() {
    return window.top.layer.msg('提交中，请稍候', {icon: 16, time: false, shade: 0.3});
}

/**
 * 关闭loading层,如果传index关闭指定的loading,不传index则关闭最后一个打开的loading.
 * @param index
 */
function closeLoading(index) {
    if (arguments.length) {
        window.top.layer.close(index)
    } else {
        window.top.layer.close(window.top.layer.index)
    }
}


/**
 * 所有文本框编辑的时候不允许输入空格.
 */
$('input').on('input', function () {
    // console.log($(this).val());
    $(this).val($.trim($(this).val()))
});