package com.iaproject.agent.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Map<String, String> home() {
    return Map.of(
        "status", "ok",
        "message", "ia-msa-dm-agent is running");
  }
}
