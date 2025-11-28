package com.lul.user.infrastructure.persistence.mapper;

import com.lul.user.domain.userprofile.aggregate.UserProfile;
import com.lul.user.domain.userprofile.valueobject.Email;
import com.lul.user.domain.userprofile.valueobject.FullName;
import com.lul.user.domain.userprofile.valueobject.PhoneNumber;
import com.lul.user.infrastructure.persistence.entity.UserProfileEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(target = "email", expression = "java(domain.getEmail().getValue())")
    @Mapping(target = "firstName", expression = "java(domain.getFullName().getFirstName())")
    @Mapping(target = "lastName", expression = "java(domain.getFullName().getLastName())")
    @Mapping(target = "phoneNumber", expression = "java(domain.getPhoneNumber() != null ? domain.getPhoneNumber().getValue() : null)")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    UserProfileEntity toEntity(UserProfile domain);

    default UserProfile toDomain(UserProfileEntity entity) {
        if (entity == null) {
            return null;
        }

        Email email = new Email(entity.getEmail());
        FullName fullName = new FullName(entity.getFirstName(), entity.getLastName());
        PhoneNumber phoneNumber = entity.getPhoneNumber() != null
            ? new PhoneNumber(entity.getPhoneNumber())
            : null;

        UserProfile profile = UserProfile.create(email, fullName, phoneNumber, entity.getDateOfBirth());

        // Use reflection to set BaseEntity fields
        setBaseEntityFields(profile, entity);

        return profile;
    }

    default void setBaseEntityFields(UserProfile domain, UserProfileEntity entity) {
        try {
            // Set id
            java.lang.reflect.Field idField = com.lul.common.core.domain.BaseEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(domain, entity.getId());

            // Set version
            java.lang.reflect.Field versionField = com.lul.common.core.domain.BaseEntity.class.getDeclaredField("version");
            versionField.setAccessible(true);
            versionField.set(domain, entity.getVersion());

            // Set createdAt
            java.lang.reflect.Field createdAtField = com.lul.common.core.domain.BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(domain, entity.getCreatedAt());

            // Set updatedAt
            java.lang.reflect.Field updatedAtField = com.lul.common.core.domain.BaseEntity.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(domain, entity.getUpdatedAt());
        } catch (Exception e) {
            throw new RuntimeException("Failed to set BaseEntity fields", e);
        }
    }
}
