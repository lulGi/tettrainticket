package com.lul.user.application.dto.mapper;

import com.lul.user.application.dto.response.UserProfileResponse;
import com.lul.user.domain.userprofile.aggregate.UserProfile;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting domain objects to DTOs.
 */
@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    /**
     * Convert UserProfile domain object to UserProfileResponse DTO
     *
     * @param profile  UserProfile aggregate
     * @param username Username from Credential
     * @return UserProfileResponse DTO
     */
    default UserProfileResponse toUserProfileResponse(UserProfile profile, String username) {
        if (profile == null) {
            return null;
        }

        return UserProfileResponse.builder()
                .id(profile.getId())
                .username(username)
                .email(profile.getEmail().getValue())
                .fullName(profile.getFullName().getFirstName() + " " +
                        profile.getFullName().getLastName())
                .phoneNumber(profile.getPhoneNumber() != null ?
                        profile.getPhoneNumber().getValue() : null)
                .dateOfBirth(profile.getDateOfBirth())
                .createdAt(profile.getCreatedAt())
                .build();
    }
}
