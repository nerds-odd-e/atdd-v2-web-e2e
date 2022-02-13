package com.odde.atddv2;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "selenium-video-api", url = "${selenium-video-api.endpoint.url}")
public interface SeleniumVideoApi {

    @PostMapping("/start")
    void startRecording(@RequestParam(value = "name") String name);

    @PostMapping("/end")
    void endRecording();

}
