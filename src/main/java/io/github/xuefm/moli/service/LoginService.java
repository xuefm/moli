package io.github.xuefm.moli.service;

import io.github.xuefm.moli.data.login.LoginRequest;
import io.github.xuefm.moli.data.web.Results;

public interface LoginService {
    Results<String> login(LoginRequest loginRequest);

    Results<String> logout();
}
