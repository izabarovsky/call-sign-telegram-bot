package com.izabarovsky.callsign.telegram.bot.dmrid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "radioid", url = "https://radioid.net/")
public interface RadioIdClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/dmr/user/")
    ResultModel getDmrId(@SpringQueryMap QueryParams queryParams);

}
