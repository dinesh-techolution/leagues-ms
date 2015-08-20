package com.makeurpicks.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.makeurpicks.domain.GameResponse;

@FeignClient("game")
public interface GameClient {
 
	@RequestMapping(value = "/game/{id}", method=RequestMethod.GET ,produces = "application/json")
    public @ResponseBody GameResponse getGameById(@PathVariable("id") String id);
}
