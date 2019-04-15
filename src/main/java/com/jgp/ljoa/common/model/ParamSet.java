package com.jgp.ljoa.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jgp.common.annotation.UI;
import com.jgp.common.persistence.UUIDModel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/9
 */
@UI
@Entity
@Table(name = "LJ_PARAM_SET")//通用参数设置
public class ParamSet extends UUIDModel {
        //参数名称
        @Column(name = "PARAM_NAME",length = 50)
        private String paramName;
        //参数代码
        @Column(name = "PARAM_CODE",length = 50)
        private String paramCode;
        //字符参数值1
        @Column(name = "STR_CONTENT1",length = 200)
        private String strContent1;
        //字符参数值2
        @Column(name = "STR_CONTENT2",length = 200)
        private String strContent2;
        //字符参数值3
        @Column(name = "STR_CONTENT3",length = 200)
        private String strContent3;
        //数值参数值1
        @Column(name = "NUM_CONTENT1",length = 10,precision = 2)
        private Double numContent1;
        //数值参数值2
        @Column(name = "NUM_CONTENT2",length = 10,precision = 2)
        private String numContent2;
        //数值参数值3
        @Column(name = "NUM_CONTENT3",length = 10,precision = 2)
        private String numContent3;
        //日期参数值1
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        @Column(name = "DATE_CONTENT1")
        private LocalDateTime dateContent1;
        //日期参数值2
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        @Column(name = "DATE_CONTENT2")
        private LocalDateTime dateContent2;
        //日期参数值3
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        @Column(name = "DATE_CONTENT3")
        private LocalDateTime dateContent3;

        public String getParamName() {
                return paramName;
        }

        public void setParamName(String paramName) {
                this.paramName = paramName;
        }

        public String getParamCode() {
                return paramCode;
        }

        public void setParamCode(String paramCode) {
                this.paramCode = paramCode;
        }

        public String getStrContent1() {
                return strContent1;
        }

        public void setStrContent1(String strContent1) {
                this.strContent1 = strContent1;
        }

        public String getStrContent2() {
                return strContent2;
        }

        public void setStrContent2(String strContent2) {
                this.strContent2 = strContent2;
        }

        public String getStrContent3() {
                return strContent3;
        }

        public void setStrContent3(String strContent3) {
                this.strContent3 = strContent3;
        }

        public Double getNumContent1() {
                return numContent1;
        }

        public void setNumContent1(Double numContent1) {
                this.numContent1 = numContent1;
        }

        public String getNumContent2() {
                return numContent2;
        }

        public void setNumContent2(String numContent2) {
                this.numContent2 = numContent2;
        }

        public String getNumContent3() {
                return numContent3;
        }

        public void setNumContent3(String numContent3) {
                this.numContent3 = numContent3;
        }

        public LocalDateTime getDateContent1() {
                return dateContent1;
        }

        public void setDateContent1(LocalDateTime dateContent1) {
                this.dateContent1 = dateContent1;
        }

        public LocalDateTime getDateContent2() {
                return dateContent2;
        }

        public void setDateContent2(LocalDateTime dateContent2) {
                this.dateContent2 = dateContent2;
        }

        public LocalDateTime getDateContent3() {
                return dateContent3;
        }

        public void setDateContent3(LocalDateTime dateContent3) {
                this.dateContent3 = dateContent3;
        }
}

