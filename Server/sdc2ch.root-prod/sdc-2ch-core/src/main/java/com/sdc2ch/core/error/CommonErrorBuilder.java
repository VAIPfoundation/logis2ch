package com.sdc2ch.core.error;

import static com.sdc2ch.core.security.auth.error.ApiException.*;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.core.security.auth.error.ApiException;
import com.sdc2ch.core.security.auth.error.ErrorCode;
import com.sdc2ch.core.utils.ObjectMapperBuilderUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

 
@Slf4j
@Getter
@RequiredArgsConstructor(staticName="of")
public class CommonErrorBuilder {

	private ObjectMapper mapper = ObjectMapperBuilderUtils.getInstance();
	
	private ApiException ex;

	public CommonErrorBuilder throwError(String path, HttpStatus status, ErrorCode error, String message) {
		ex = builder()
			.error(status.getReasonPhrase())
			.errorCode(error).message(message)
			.path(path)
			.status(status.value())
			.timestamp(new Date())
			.build();
		return this;
	}
	
	public void sendError(HttpServletResponse response) {
		try {
			log.warn("{}", ex);
			response.setStatus(ex.getStatus());
			mapper.writeValue(response.getWriter(), ex);
		} catch (IOException e) {
			log.error("{}", e);
		}
	}
}
