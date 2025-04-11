package com.engfred.bookstore.config;

/* 5.
the authentication middleware
For every request, we want to retrieve the JWT token in the header “Authorization”, and validate it:

If the token is invalid, reject the request if the token is invalid or continues otherwise.
If the token is valid, extract the username, find the related user in the database,
and set it in the authentication context so you can access it in any application layer.
 */

import com.engfred.bookstore.service.JwtService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component //Tells Spring to auto-detect this class and manage it as a bean.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //Helps catch and send back custom error responses when exceptions occur inside the filter
    private final HandlerExceptionResolver handlerExceptionResolver;
    //Used to fetch user details from DB by username (which is email in this case).
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;


    //This method is the heart of the filter. It intercepts every incoming HTTP request.
    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try{

            final String jwt = authHeader.substring(7); //Cuts off the Bearer prefix and extracts the raw JWT token
            final String email = jwtService.extractEmail(jwt); // Decodes email embedded in the JWT payload

            //SecurityContextHolder holds the security context (who is logged in).
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(email != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
                if(jwtService.validateToken(jwt, userDetails)) {
                    //Create a new authentication token that Spring Security understands
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));

                    //Then it saves the new token in the Spring security context,
                    // so other parts of your app can see that the user is authenticated.
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }catch (Exception exception) {
            //If any error occurs (like token invalid or expired), it is caught and handled by handlerExceptionResolver,
            // which can return a proper error response (like 401 Unauthorized).
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
/*
1. Request comes in
2. Filter checks for JWT in Authorization header
3. If valid:
     - Extracts email from JWT
     - Loads user from DB
     - Validates the token
     - Authenticates user into the app (Spring context)
4. If invalid or missing, continue or throw an error
 */
