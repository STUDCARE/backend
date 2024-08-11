package com.studcare.security.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.constants.Status;
import com.studcare.data.jpa.repository.UserRepository;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.exception.StudCareValidationException;
import com.studcare.model.HttpRequestData;
import com.studcare.model.HttpResponseData;
import com.studcare.security.JwtService;
import com.studcare.security.auth.dto.AuthenticationRequest;
import com.studcare.security.auth.dto.AuthenticationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import static com.studcare.util.CommonUtils.createResponseEntity;

@Slf4j
@Service
public class AuthenticationService {
	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private ObjectMapper objectMapper;

	public ResponseEntity<Object> authenticate(HttpRequestData httpRequestData) {
		log.info("AuthenticationService.authenticate() initiated");
		AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			AuthenticationRequest authenticationRequest = adaptAuthenticationRequest(httpRequestData);
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
			var user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new StudCareValidationException("User not found"));
			var jwtToken = jwtService.generateToken(user);
			authenticationResponse.setToken(jwtToken);
			authenticationResponse.setUser(user);
			authenticationResponse.setResponseCode(Status.SUCCESS);
			authenticationResponse.setMessage("User Logged in Successfully");
			httpResponseData = adaptAuthenticationResponse(authenticationResponse);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("AuthenticationService.authenticate() finished for {}", authenticationRequest.getEmail());
		} catch (StudCareValidationException exception) {
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			authenticationResponse.setResponseCode(Status.FAILURE);
			authenticationResponse.setMessage(exception.getMessage());
			httpResponseData.setResponseBody(mapResponseData(authenticationResponse));
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AuthenticationService.authenticate() a validation error occurred while processing the request", exception);
		} catch (StudCareRuntimeException exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			authenticationResponse.setResponseCode(Status.FAILURE);
			authenticationResponse.setMessage(exception.getMessage());
			httpResponseData.setResponseBody(mapResponseData(authenticationResponse));
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AuthenticationService.authenticate() a runtime error occurred while processing the request", exception);
		} catch (Exception exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			authenticationResponse.setResponseCode(Status.FAILURE);
			authenticationResponse.setMessage(exception.getMessage());
			httpResponseData.setResponseBody(mapResponseData(authenticationResponse));
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AuthenticationService.authenticate() an error occurred while processing the request", exception);
		}
		return responseEntity;
	}

	private AuthenticationRequest adaptAuthenticationRequest(HttpRequestData httpRequestData) {
		try {
			return objectMapper.readValue(httpRequestData.getRequestBody(), AuthenticationRequest.class);
		} catch (JsonProcessingException exception) {
			log.error("AuthenticationService.adaptAuthenticationRequest(): map user register request to user object failed", exception);
			throw new StudCareRuntimeException("user login request to AuthenticationRequest object failed");
		}
	}

	private HttpResponseData adaptAuthenticationResponse(AuthenticationResponse authenticationResponse) {
		HttpResponseData responseData = new HttpResponseData();
		if (authenticationResponse.getResponseCode().equals(Status.SUCCESS)) {
			responseData.setHttpStatus(HttpStatus.OK);
		} else {
			responseData.setHttpStatus(HttpStatus.BAD_REQUEST);
		}
		responseData.setResponseBody(mapResponseData(authenticationResponse));
		return responseData;
	}

	private String mapResponseData(AuthenticationResponse authenticationResponse) {
		try {
			return objectMapper.writeValueAsString(authenticationResponse);
		} catch (JsonProcessingException exception) {
			log.error("AuthenticationService.adaptAuthenticationRequest(): map user register response to response object failed", exception);
			throw new StudCareRuntimeException("user login response to AuthenticationResponse object failed");
		}
	}
}