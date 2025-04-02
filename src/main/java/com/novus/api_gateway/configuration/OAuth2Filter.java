package com.novus.api_gateway.configuration;

import com.novus.api_gateway.dao.UserDaoUtils;
import com.novus.shared_models.common.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuth2Filter implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();

    private final UserDaoUtils userDaoUtils;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");

        Optional<User> user = userDaoUtils.findByEmail(email);
        if (user.isEmpty()) {
            createNewUser(oAuth2User, email);
        } else if (!user.get().isValidEmail()) {
            validateUserEmail(user.get());
        }

        return oAuth2User;
    }

    private void createNewUser(OAuth2User oAuth2User, String email) {
        String username = generateUsername(oAuth2User);

        User newUser = User.builder()
                .username(username)
                .email(email)
                .isValidEmail(true)
                .build();

        userDaoUtils.save(newUser);
    }

    private String generateUsername(OAuth2User oAuth2User) {
        String username = oAuth2User.getAttribute("name");
        if (username != null) {
            username = username.replaceAll("\\s+", "");
            username = username + "_GOOGLE-" + UUID.randomUUID();
        } else {
            username = "generatedUsername_GOOGLE-" + UUID.randomUUID();
        }
        return username;
    }

    private void validateUserEmail(User user) {
        user.setValidEmail(true);
        userDaoUtils.save(user);
    }

}
