package com.sii.lifeagency.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sii.lifeagency.configuration.GlobalConfiguration;
import com.sii.lifeagency.rest.RestError;
import java.io.IOException;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthTokenFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);	

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("AuthTokenFilter init");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		logger.info(""+ httpRequest);

		try {
			String authJwtToken = httpRequest.getHeader("Authorization");					
			this.validateJwtToken(authJwtToken);
			this.addTokenToSession(authJwtToken, httpRequest);

			chain.doFilter(request, response);
		} catch(JWTDecodeException e) {
			this.invalidTokenResponse(httpResponse, e);
		}
	}

	@Override
	public void destroy() {
		logger.info("AuthTokenFilter destroyed");		
	}

	private void validateJwtToken(String token) throws JWTDecodeException {
		if(token == null) {
			throw new JWTDecodeException("Token is not present.");
		}

		DecodedJWT jwtDecoded = JWT.decode(token);

		if(jwtDecoded.getExpiresAt().before(new Date())){
			throw new JWTDecodeException("Token is expired.");
		}

		if(!jwtDecoded.getClaim("appid").asString().equals(GlobalConfiguration.getInstance().getAzureAppId())){
			throw new JWTDecodeException("Token was not issued for this application.");			
		}
	}

	private void addTokenToSession(String token, HttpServletRequest request) {
		HttpSession session = request.getSession();

		session.setAttribute("azureToken", token);
	}

	private void invalidTokenResponse(HttpServletResponse httpResponse, Exception e) throws IOException {
		logger.info("AuthTokenFilter invalid token");
		
		RestError error = new RestError()
			.setCode(RestError.Code.UNAUTHORIZED)
			.setMessage(e.getMessage());

		httpResponse.setContentType("application/json;charset=utf-8");
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpResponse.getWriter().write(new ObjectMapper().writer().writeValueAsString(error));
	}
}