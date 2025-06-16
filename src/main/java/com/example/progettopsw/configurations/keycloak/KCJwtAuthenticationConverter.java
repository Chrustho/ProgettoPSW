package com.example.progettopsw.configurations.keycloak;


import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;


import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Component
public class KCJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    /**
     * Converts a {@link Jwt} to an {@link AbstractAuthenticationToken}.
     * <p>
     * This implementation extracts the authorities from the JWT and combines them with
     * any additional resource roles defined in the JWT.
     *
     * @param source the JWT to convert
     * @return an {@link AbstractAuthenticationToken} containing the JWT and its authorities
     */
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        return new JwtAuthenticationToken(
                source,
                Stream.concat(
                                new JwtGrantedAuthoritiesConverter().convert(source).stream(),
                                extractResourceRoles(source).stream())
                        .collect(toSet()));
    }

    @SuppressWarnings("unchecked")
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        //EXTRACTING THE ROLES FROM A TOKEN
        Map<String, Object> resourceAccess; //1
        Map<String, Object> resource; //2
        Collection<String> resourceRoles; //3
        resourceAccess = jwt.getClaim("resource_access");

        if (resourceAccess == null || resourceAccess.get("album-app") == null) {
            return Set.of();
        }
        resource = (Map<String, Object>) resourceAccess.get("album-app"); //1.1

        resourceRoles = (Collection<String>) resource.get("roles");

        return resourceRoles
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.replace("-","_")))  //MAPPING IT TO MATCH WITH SPECIFIC ROLE_
                .collect(toSet());
    }

}