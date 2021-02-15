package com.avenue.associateassembly.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.avenue.associateassembly.dto.CpfIntegrationResponseDto;
import com.avenue.associateassembly.exception.IntegrationAcessErrorException;
import com.avenue.associateassembly.exception.InvalidCPFException;

@Service
public class CpfService {

	private static final String CPF_SERVICE_URL = "cpf.service.url";
	private static final String ABLE_TO_VOTE = "ABLE_TO_VOTE";

	private final RestTemplate restTemplate;
	private final Environment environment;

	@Autowired
	public CpfService(RestTemplate restTemplate, Environment environment) {
		this.restTemplate = restTemplate;
		this.environment = environment;
	}

	public boolean isAbleToVote(String cpf) {
		try {
			String url = environment.getProperty(CPF_SERVICE_URL);
			ResponseEntity<CpfIntegrationResponseDto> response = restTemplate.exchange(url, HttpMethod.GET, null, CpfIntegrationResponseDto.class, cpf);
			
			CpfIntegrationResponseDto dto = response.getBody();
			return ABLE_TO_VOTE.equals(dto.getStatus());
		} catch (HttpStatusCodeException ex) {
			if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new InvalidCPFException();
			}

			throw new IntegrationAcessErrorException();
		}
	}
}
