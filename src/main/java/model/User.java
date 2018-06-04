package model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a User within Gitlab. Typically a User is synonymous with assignee.
 */

public class User {
    @SerializedName("id")
    private Integer id;

    @SerializedName("username")
    private String username;

    @SerializedName("name")
    private String fullName;

    @SerializedName("state")
    private String state;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("web_url")
    private String profileUrl;

    /**
     * Retrieves the unique identifier for the User.
     * @return the unique identifier.
     */

    public Integer getId() {
        return id;
    }

    /**
     * Retrieves the username associated with the User.
     * @return the username.
     */

    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the full name of the User.
     * @return the user's full name.
     */

    public String getFullName() {
        return fullName;
    }

    /**
     * Retrieves the state of the User.
     * @return the user's state.
     */

    public String getState() {
        return state;
    }

    /**
     * Retrieves the URL of the User's avatar image.
     * @return the image url.
     */

    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Retrieves the URL of the User's GitLab profile.
     * @return the profile URL.
     */

    public String getProfileUrl() {
        return profileUrl;
    }

    /**
     * Determines whether or not the USer is active, based off of {@link #state}
     * @return true if {@link #state} is active.
     */

    public Boolean isActive() {
        Boolean isActive = false;
        if (this.state != null) {
            if (this.state.toLowerCase().trim().equals("active".toLowerCase().trim())) {
                isActive = true;
            }
        }
        return isActive;
    }
}
