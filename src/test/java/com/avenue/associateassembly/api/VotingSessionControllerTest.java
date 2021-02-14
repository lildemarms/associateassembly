package com.avenue.associateassembly.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URISyntaxException;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.avenue.associateassembly.dto.VotingSessionRequestDto;
import com.avenue.associateassembly.dto.VotingSessionResponseDto;
import com.avenue.associateassembly.service.VotingSessionService;

@RunWith(MockitoJUnitRunner.class)
public class VotingSessionControllerTest {

	@Mock
    public VotingSessionService votingSessionService;

    @InjectMocks
    public VotingSessionController votingSessionController;
	
	@Test
    public void shouldCreateVoting() throws URISyntaxException {
        VotingSessionRequestDto request = new VotingSessionRequestDto();
        VotingSessionResponseDto response = new VotingSessionResponseDto();

        response.setId(new ObjectId().toHexString());
        Mockito.when(votingSessionService.create(request)).thenReturn(response);

        ResponseEntity<?> responseEntity = votingSessionController.create(request);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(responseEntity.getBody());
    }
}
