package es.KioskTV.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import es.KioskTV.service.JwtServicio;
import es.KioskTV.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * This filter intercepts incoming requests and authenticates users using JWT.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtServicio jwtService;
	@Autowired
	private UserService userService;

	/**
	 * Constructs a JwtAuthenticationFilter with JwtServicio and UserService.
	 *
	 * @param jwtService     the service for JWT operations
	 * @param usuarioService the service for user-related operations
	 */
	public JwtAuthenticationFilter(JwtServicio jwtService, UserService usuarioService) {
		this.jwtService = jwtService;
		this.userService = usuarioService;
	}

	/**
	 * Performs the actual authentication logic.
	 *
	 * @param request     the HTTP servlet request
	 * @param response    the HTTP servlet response
	 * @param filterChain the filter chain
	 * @throws ServletException if an error occurs during servlet processing
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userDas;
		if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		jwt = authHeader.substring(7);
		userDas = jwtService.extractUserName(jwt);
		if (!StringUtils.isEmpty(userDas) && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userDas);
			if (jwtService.isTokenValid(jwt, userDetails)) {
				SecurityContext context = SecurityContextHolder.createEmptyContext();
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				context.setAuthentication(authToken);
				SecurityContextHolder.setContext(context);
			}
		}
		filterChain.doFilter(request, response);
	}

}
