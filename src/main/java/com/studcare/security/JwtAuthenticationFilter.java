package com.studcare.security;

import com.studcare.constants.Security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private  UserDetailsService userDetailsService;
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
			throws ServletException, IOException {
		final String authHeader = request.getHeader(Security.AUTHORIZATION);
		final String token;
		final String userEmail;
		if (ObjectUtils.isEmpty(authHeader) || !authHeader.startsWith(Security.BEARER)) {
			filterChain.doFilter(request, response);
			return;
		}
		token = authHeader.substring(7);
		userEmail = jwtService.extractUsername(token);
		if (!ObjectUtils.isEmpty(userEmail) && ObjectUtils.isEmpty(SecurityContextHolder.getContext().getAuthentication())) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
			if (jwtService.isTokenValid(token, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			} else {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
				return;
			}
		}
		filterChain.doFilter(request, response);
	}
}
