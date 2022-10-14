package com.mojang.authlib;

import com.google.common.collect.Multimap;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.util.UUIDTypeAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BaseUserAuthentication implements UserAuthentication {

    protected static final String STORAGE_KEY_PROFILE_NAME = "displayName";

    protected static final String STORAGE_KEY_PROFILE_ID = "uuid";

    protected static final String STORAGE_KEY_PROFILE_PROPERTIES = "profileProperties";

    protected static final String STORAGE_KEY_USER_NAME = "username";

    protected static final String STORAGE_KEY_USER_ID = "userid";

    protected static final String STORAGE_KEY_USER_PROPERTIES = "userProperties";

    private static final Logger LOGGER = LogManager.getLogger();

    private final AuthenticationService authenticationService;

    private final PropertyMap userProperties = new PropertyMap();

    private String userid;

    private String username;

    private String password;

    private GameProfile selectedProfile;

    private UserType userType;

    protected BaseUserAuthentication(AuthenticationService authenticationService) {
        Validate.notNull(authenticationService);
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean canLogIn() {
        return (!canPlayOnline() && StringUtils.isNotBlank(getUsername()) && StringUtils.isNotBlank(getPassword()));
    }

    @Override
    public void logOut() {
        this.password = null;
        this.userid = null;
        setSelectedProfile(null);
        getModifiableUserProperties().clear();
        setUserType(null);
    }

    @Override
    public boolean isLoggedIn() {
        return (getSelectedProfile() != null);
    }

    protected String getUsername() {
        return this.username;
    }

    @Override
    public void setUsername(String username) {
        if (isLoggedIn() && canPlayOnline()) {
            throw new IllegalStateException("Cannot change username whilst logged in & online");
        }
        this.username = username;
    }

    protected String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(String password) {
        if (isLoggedIn() && canPlayOnline() && StringUtils.isNotBlank(password)) {
            throw new IllegalStateException("Cannot set password whilst logged in & online");
        }
        this.password = password;
    }

    @Override
    public void loadFromStorage(Map<String, Object> credentials) {
        logOut();
        setUsername(String.valueOf(credentials.get("username")));
        if (credentials.containsKey("userid")) {
            this.userid = String.valueOf(credentials.get("userid"));
        } else {
            this.userid = this.username;
        }
        if (credentials.containsKey("userProperties"))
      try {
            List<Map<String, String>> list = (List<Map<String, String>>) credentials.get("userProperties");
            for (Map<String, String> propertyMap : list) {
                String name = propertyMap.get("name");
                String value = propertyMap.get("value");
                String signature = propertyMap.get("signature");
                if (signature == null) {
                    getModifiableUserProperties().put(name, new Property(name, value));
                    continue;
                }
                getModifiableUserProperties().put(name, new Property(name, value, signature));
            }
        } catch (Throwable t) {
            LOGGER.warn("Couldn't deserialize user properties", t);
        }
        if (credentials.containsKey("displayName") && credentials.containsKey("uuid")) {
            GameProfile profile = new GameProfile(UUIDTypeAdapter.fromString(String.valueOf(credentials.get("uuid"))), String.valueOf(credentials.get("displayName")));
            if (credentials.containsKey("profileProperties"))
        try {
                List<Map<String, String>> list = (List<Map<String, String>>) credentials.get("profileProperties");
                for (Map<String, String> propertyMap : list) {
                    String name = propertyMap.get("name");
                    String value = propertyMap.get("value");
                    String signature = propertyMap.get("signature");
                    if (signature == null) {
                        profile.getProperties().put(name, new Property(name, value));
                        continue;
                    }
                    profile.getProperties().put(name, new Property(name, value, signature));
                }
            } catch (Throwable t) {
                LOGGER.warn("Couldn't deserialize profile properties", t);
            }
            setSelectedProfile(profile);
        }
    }

    @Override
    public Map<String, Object> saveForStorage() {
        Map<String, Object> result = new HashMap<>();
        if (getUsername() != null) {
            result.put("username", getUsername());
        }
        if (getUserID() != null) {
            result.put("userid", getUserID());
        } else if (getUsername() != null) {
            result.put("username", getUsername());
        }
        if (!getUserProperties().isEmpty()) {
            List<Map<String, String>> properties = new ArrayList<>();
            for (Property userProperty : getUserProperties().values()) {
                Map<String, String> property = new HashMap<>();
                property.put("name", userProperty.getName());
                property.put("value", userProperty.getValue());
                property.put("signature", userProperty.getSignature());
                properties.add(property);
            }
            result.put("userProperties", properties);
        }

        GameProfile _selectedProfile = getSelectedProfile();
        if (_selectedProfile != null) {
            result.put("displayName", _selectedProfile.getName());
            result.put("uuid", _selectedProfile.getId());
            Object properties = new ArrayList();
            for (Property profileProperty : _selectedProfile.getProperties().values()) {
                Map<String, String> property = new HashMap<>();
                property.put("name", profileProperty.getName());
                property.put("value", profileProperty.getValue());
                property.put("signature", profileProperty.getSignature());
                ((List<Map<String, String>>) properties).add(property);
            }
            if (!((List) properties).isEmpty()) {
                result.put("profileProperties", properties);
            }
        }
        return result;
    }

    @Override
    public GameProfile getSelectedProfile() {
        return this.selectedProfile;
    }

    protected void setSelectedProfile(GameProfile selectedProfile) {
        this.selectedProfile = selectedProfile;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append("{");
        if (isLoggedIn()) {
            result.append("Logged in as ");
            result.append(getUsername());
            if (getSelectedProfile() != null) {
                result.append(" / ");
                result.append(getSelectedProfile());
                result.append(" - ");
                if (canPlayOnline()) {
                    result.append("Online");
                } else {
                    result.append("Offline");
                }
            }
        } else {
            result.append("Not logged in");
        }
        result.append("}");
        return result.toString();
    }

    public AuthenticationService getAuthenticationService() {
        return this.authenticationService;
    }

    @Override
    public String getUserID() {
        return this.userid;
    }

    @Override
    public PropertyMap getUserProperties() {
        if (isLoggedIn()) {
            PropertyMap result = new PropertyMap();
            result.putAll((Multimap) getModifiableUserProperties());
            return result;
        }
        return new PropertyMap();
    }

    protected PropertyMap getModifiableUserProperties() {
        return this.userProperties;
    }

    @Override
    public UserType getUserType() {
        if (isLoggedIn()) {
            return (this.userType == null) ? UserType.LEGACY : this.userType;
        }
        return null;
    }

    protected void setUserType(UserType userType) {
        this.userType = userType;
    }

    protected void setUserid(String userid) {
        this.userid = userid;
    }
}
