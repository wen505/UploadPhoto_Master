package com.common.vo;


import com.common.util.ConstantUtil;

public class RspData {

    private String code;

    private String error;

    public RspData() {
    }

    public RspData(String code, String error) {
        this.code = code;
        this.error = error;
    }


    /**
     *  返回成功失败！
     * @param success  是否成功
     * @return （1,成功 2 失败）
     */
    public static RspData issuccess(boolean success) {
       if (success){
           return new RspData(ConstantUtil.SUCCESS_CODE, null);
       }else {
           return new RspData(ConstantUtil.ERROR_CODE, null);
       }
    }
    /**
     * 失败返回 ，返回代码为2
     * @param error
     * @return
     */
    public static RspData error(String error) {
        return new RspData(ConstantUtil.ERROR_CODE, error);
    }

    /**
     *  返回失败
     * @param code 错误代码
     * @param error 错误消息
     * @return
     */
    public static RspData error(String code, String error) {
        return new RspData(code, error);
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
