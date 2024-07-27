package com.studcare.service;

import com.studcare.adapter.UserDeletionRequestAdapter;
import com.studcare.adapter.UserDeletionResponseAdapter;
import com.studcare.adapter.UserLogoutRequestAdapter;
import com.studcare.adapter.UserLogoutResponseAdapter;
import com.studcare.adapter.UserProfileRequestAdapter;
import com.studcare.adapter.UserProfileResponseAdapter;
import com.studcare.adapter.UserRegisterRequestAdapter;
import com.studcare.adapter.UserRegisterResponseAdapter;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.exception.StudCareValidationException;
import com.studcare.model.HttpRequestData;
import com.studcare.model.HttpResponseData;
import com.studcare.model.LogoutRequestDTO;
import com.studcare.model.LogoutResponseDTO;
import com.studcare.model.UserDeletionRequestDTO;
import com.studcare.model.UserDeletionResponseDTO;
import com.studcare.model.UserProfileRequestDTO;
import com.studcare.model.UserProfileResponseDTO;
import com.studcare.model.UserRegisterRequestDTO;
import com.studcare.model.UserRegisterResponseDTO;
import com.studcare.validator.UserDeletionValidator;
import com.studcare.validator.UserLogoutValidator;
import com.studcare.validator.UserProfileValidator;
import com.studcare.validator.UserRegisterValidator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.studcare.util.CommonUtils.createResponseEntity;

@Slf4j
@Service
public class AccountService {
	@Autowired
	private UserRegisterRequestAdapter userRegisterRequestAdapter;
	@Autowired
	private UserRegisterResponseAdapter userRegisterResponseAdapter;
	@Autowired
	private UserDeletionRequestAdapter userDeletionRequestAdapter;
	@Autowired
	private UserDeletionResponseAdapter userDeletionResponseAdapter;
	@Autowired
	private UserProfileRequestAdapter userProfileRequestAdapter;
	@Autowired
	private UserProfileResponseAdapter userProfileResponseAdapter;
	@Autowired
	private UserLogoutRequestAdapter userLogoutRequestAdapter;
	@Autowired
	private UserLogoutResponseAdapter userLogoutResponseAdapter;
	@Autowired
	private UserRegisterValidator userRegisterValidator;
	@Autowired
	private UserDeletionValidator userDeletionValidator;
	@Autowired
	private UserProfileValidator userProfileValidator;
	@Autowired
	private UserLogoutValidator userLogoutValidator;
	@Autowired
	private UserService userService;

	@Transactional
	public ResponseEntity<Object> createUser(HttpRequestData httpRequestData) {
		log.info("AccountService.adaptCreateUser() initiated");
		ResponseEntity<Object> responseEntity;
		UserRegisterResponseDTO userRegisterResponseDTO;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			UserRegisterRequestDTO userRegisterRequestDTO = userRegisterRequestAdapter.adapt(httpRequestData);
			userRegisterValidator.validate(userRegisterRequestDTO);
			userRegisterResponseDTO =userService.register(userRegisterRequestDTO.getUserDTO());
			httpResponseData = userRegisterResponseAdapter.adapt(userRegisterResponseDTO);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("AccountService.adaptCreateUser()  finished for {}", userRegisterRequestDTO.getUserDTO().getEmail());
		} catch (StudCareValidationException exception) {
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AccountService.adaptCreateUser()  an validation error occurred while processing the request", exception);
		} catch (StudCareRuntimeException exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AccountService.adaptCreateUser()  an runtime error occurred while processing the request", exception);
		} catch (Exception exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AccountService.adaptCreateUser()  an error occurred while processing the request", exception);
		}
		return responseEntity;
	}

	@Transactional
	public ResponseEntity<Object> deleteUser(HttpRequestData httpRequestData) {
		log.info("AccountService.deleteUser() initiated by user {}", httpRequestData.getReference());
		ResponseEntity<Object> responseEntity;
		UserDeletionResponseDTO userDeletionResponseDTO;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			UserDeletionRequestDTO userDeletionRequestDTO = userDeletionRequestAdapter.adapt(httpRequestData);
			userDeletionValidator.validate(userDeletionRequestDTO);
			userDeletionResponseDTO =userService.delete(userDeletionRequestDTO.getUserEmail());
			httpResponseData = userDeletionResponseAdapter.adapt(userDeletionResponseDTO);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("AccountService.deleteUser() finished for {}", userDeletionRequestDTO.getUserEmail());
		} catch (StudCareValidationException exception) {
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AccountService.deleteUser() an validation error occurred while processing the request", exception);
		} catch (StudCareRuntimeException exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AccountService.deleteUser() an runtime error occurred while processing the request", exception);
		} catch (Exception exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AccountService.deleteUser() an error occurred while processing the request", exception);
		}
		return responseEntity;
	}

	@Transactional
	public ResponseEntity<Object> viewUserProfile(HttpRequestData httpRequestData) {
		log.info("AccountService.viewUserProfile() initiated");
		ResponseEntity<Object> responseEntity;
		UserProfileResponseDTO userProfileResponseDTO;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			UserProfileRequestDTO userProfileRequestDTO = userProfileRequestAdapter.adapt(httpRequestData);
			userProfileValidator.validate(userProfileRequestDTO);
			userProfileResponseDTO = userService.getUserProfile(userProfileRequestDTO.getUserEmail());
			httpResponseData = userProfileResponseAdapter.adapt(userProfileResponseDTO);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("AccountService.viewUserProfile() finished for {}", userProfileRequestDTO.getUserEmail());
		} catch (StudCareValidationException exception) {
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AccountService.viewUserProfile() an validation error occurred while processing the request", exception);
		} catch (StudCareRuntimeException exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AccountService.viewUserProfile() an runtime error occurred while processing the request", exception);
		} catch (Exception exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AccountService.viewUserProfile() an error occurred while processing the request", exception);
		}
		return responseEntity;
	}

	@Transactional
	public ResponseEntity<Object> userLogout(HttpRequestData httpRequestData) {
		log.info("AccountService.userLogout() initiated");
		ResponseEntity<Object> responseEntity;
		LogoutResponseDTO logoutResponseDTO;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			LogoutRequestDTO logoutRequestDTO = userLogoutRequestAdapter.adapt(httpRequestData);
			userLogoutValidator.validate(logoutRequestDTO);
			logoutResponseDTO = userService.logout(logoutRequestDTO);
			httpResponseData = userLogoutResponseAdapter.adapt(logoutResponseDTO);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("AccountService.userLogout() finished for {}", logoutRequestDTO.getEmail());
		} catch (StudCareValidationException exception) {
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AccountService.userLogout() an validation error occurred while processing the request", exception);
		} catch (StudCareRuntimeException exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AccountService.userLogout() an runtime error occurred while processing the request", exception);
		} catch (Exception exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AccountService.userLogout() an error occurred while processing the request", exception);
		}
		return responseEntity;
	}

}
