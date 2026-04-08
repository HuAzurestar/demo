package com.myhexin.thsmember.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Gemini
 */
public class ResultDTO<T> {
    @JsonProperty("status_code")
    private Integer statusCode;
    @JsonProperty("status_msg")
    private String statusMsg;
    private T data;

    public ResultDTO() {}

    public ResultDTO(Integer statusCode, String statusMsg, T data) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
        this.data = data;
    }

    public static <T> ResultDTO<T> success(T data) {
        return new ResultDTO<>(0, "请求成功!", data);
    }

    // Getters and Setters
    public Integer getStatusCode() { return statusCode; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }
    public String getStatusMsg() { return statusMsg; }
    public void setStatusMsg(String statusMsg) { this.statusMsg = statusMsg; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
