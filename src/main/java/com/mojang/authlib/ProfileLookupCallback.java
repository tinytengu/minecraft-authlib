package com.mojang.authlib;

public interface ProfileLookupCallback {

    void onProfileLookupSucceeded(GameProfile paramGameProfile);

    void onProfileLookupFailed(GameProfile paramGameProfile, Exception paramException);
}
