package com.example.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class EmpLog {
    private Integer id; //ID
    private LocalDateTime operateTime; //操作时间
    private String info; //详细信息

    public EmpLog() {
    }
    public EmpLog(Integer id, LocalDateTime operateTime, String info) {
        this.id = id;
        this.operateTime = operateTime;
        this.info = info;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public LocalDateTime getOperateTime() {
        return operateTime;
    }
    public void setOperateTime(LocalDateTime operateTime) {
        this.operateTime = operateTime;
    }
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    @Override
    public String toString() {
        return "EmpLog{" +
                "id=" + id +
                ", operateTime=" + operateTime +
                ", info='" + info + '\'' +
                '}';
    }
}
