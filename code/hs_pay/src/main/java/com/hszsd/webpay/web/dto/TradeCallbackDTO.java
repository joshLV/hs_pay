package com.hszsd.webpay.web.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 回调记录业务类
 * Created by gzhengDu on 2016/7/16.
 */
public class TradeCallbackDTO implements Serializable {

    private static final long serialVersionUID = 5173388003400866166L;

    //交易记录流水号
    private String transId;

    //回调次数
    private int count;

    //创建时间
    private Date createDate;

    //创建人
    private String createBy;

    //更新时间
    private Date updateDate;

    //更新人
    private String updateBy;

    //备注
    private String remark;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "TradeCallbackDTO{" +
                "transId='" + transId + '\'' +
                ", count=" + count +
                ", createDate=" + createDate +
                ", createBy='" + createBy + '\'' +
                ", updateDate=" + updateDate +
                ", updateBy='" + updateBy + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
