package com.avenue.associateassembly.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Voting Session")
@RequestMapping(value = "api/v1/votingsessions", produces = "application/json")
public class VotingSessionController {

}
