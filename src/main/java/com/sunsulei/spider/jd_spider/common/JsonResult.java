package com.sunsulei.spider.jd_spider.common;

import cn.hutool.core.convert.Convert;
import org.springframework.util.StringUtils;

public class JsonResult {

    private String code;
    private String msg;
    private Object data;
    private Boolean success;
    private Number count;

    public static JsonResult resultSuccess(Object o, Number count) {
        if (o == null) {
            return resultError("返回的数据内容为空");
        }
        return resultSuccess("", o, count);
    }

    public static JsonResult resultSuccess(Object o) {
        return resultSuccess("", o, 0);
    }

    public static JsonResult resultSuccess(String msg) {
        if (StringUtils.isEmpty(msg)) {
            return resultError("返回的数据内容为空");
        }
        return resultSuccess(msg, null, 0);
    }

    public static JsonResult resultSuccess(String msg, Object data, Number count) {
        JsonResult json = new JsonResult();
        json.setCode("0");
        json.setMsg(msg);
        json.setData(data);
        json.setCount(count);
        json.setSuccess(true);
        return json;
    }


    public static JsonResult resultError() {
        return resultError("操作失败");
    }

    public static JsonResult resultError(Object errorMsg) {
        JsonResult json = new JsonResult();
        json.setCode("1");
        json.setMsg(Convert.toStr(errorMsg));
        json.setCount(0);
        json.setData(errorMsg);
        json.setSuccess(false);
        return json;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Number getCount() {
        return count;
    }

    public void setCount(Number count) {
        this.count = count;
    }
}
