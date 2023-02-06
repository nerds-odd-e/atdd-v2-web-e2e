package com.odde.atddv2.jfactory.api;

import com.yaoruozhou.jfactory.Request;

import java.util.ArrayList;
import java.util.List;

@Request(path = "/express/query")
public class LogisticsResponse {
    public int status;
    public String msg;
    public LogisticsResult result;

    public static class LogisticsResult {
        public String number, type, typename, logo;
        public int deliverystatus, issign;
        public List<LogisticsItem> list = new ArrayList<>();
    }

    public static class LogisticsItem {
        public String time, status;
    }
}
