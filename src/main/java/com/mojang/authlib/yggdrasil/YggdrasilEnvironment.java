package com.mojang.authlib.yggdrasil;

import com.mojang.authlib.Environment;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public enum YggdrasilEnvironment implements Environment {
    PROD("https://authserver.mojang.com", "https://api.mojang.com", "https://sessionserver.mojang.com"),
    STAGING("https://yggdrasil-auth-staging.mojang.com", "https://api-staging.mojang.com", "https://yggdrasil-auth-session-staging.mojang.zone");

    private final String authHost;

    private final String accountsHost;

    private final String sessionHost;

    YggdrasilEnvironment(String authHost, String accountsHost, String sessionHost) {
        this.authHost = authHost;
        this.accountsHost = accountsHost;
        this.sessionHost = sessionHost;
    }

    public static Optional<YggdrasilEnvironment> fromString(@Nullable String value) {
        return Stream.<YggdrasilEnvironment>of(values())
                .filter(env -> (value != null && value.equalsIgnoreCase(env.name())))
                .findFirst();
    }

    public String getAuthHost() {
        return this.authHost;
    }

    public String getAccountsHost() {
        return this.accountsHost;
    }

    public String getSessionHost() {
        return this.sessionHost;
    }

    public String getName() {
        return name();
    }

    public String asString() {
        return (new StringJoiner(", ", "", ""))
                .add("authHost='" + this.authHost + "'")
                .add("accountsHost='" + this.accountsHost + "'")
                .add("sessionHost='" + this.sessionHost + "'")
                .add("name='" + getName() + "'")
                .toString();
    }
}
