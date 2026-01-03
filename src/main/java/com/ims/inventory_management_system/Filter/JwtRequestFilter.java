package com.ims.inventory_management_system.Filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.inventory_management_system.Service.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter{

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try{
            final String autherizationHeader = request.getHeader("Authorization");

            String username = null;
            String jwt = null;

            // Check if the header exists and starts with "Bearer "
            if(autherizationHeader!= null && autherizationHeader.startsWith("Bearer ")){
                jwt = autherizationHeader.substring(7);
                username = jwtUtil.extractUserName(jwt);
            }

             // If we have a username and the user is not already authenticated
            if(username!= null && SecurityContextHolder.getContext().getAuthentication()== null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // If the token is valid, we manually set the authentication for Spring Security
                if(jwtUtil.validateToken(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities()
                        );
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

            //  Continue the filter chain
            filterChain.doFilter(request, response);

        }catch (ExpiredJwtException ex) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "JWT Token has expired");

        } catch (SignatureException ex) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Invalid JWT Signature");

        } catch (MalformedJwtException ex) {
            // ID is ripped/garbage
            sendErrorResponse(response, HttpStatus.BAD_REQUEST, "Malformed JWT Token");

        } catch (Exception ex) {
            // Any other error
            sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "An internal security error occurred");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException{
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);


         // Use Jackson ObjectMapper to convert the Map to a JSON string
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }

    

}
