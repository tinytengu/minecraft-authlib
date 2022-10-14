package com.mojang.authlib;

public interface GameProfileRepository {

    void findProfilesByNames(String[] paramArrayOfString, Agent paramAgent, ProfileLookupCallback paramProfileLookupCallback);
}
