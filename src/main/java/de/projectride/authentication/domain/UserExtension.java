package de.projectride.authentication.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

import de.projectride.authentication.domain.enumeration.Sex;

/**
 * A UserExtension.
 */
@Entity
@Table(name = "user_extension")
public class UserExtension implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private Sex sex;

    @NotNull
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "member_since")
    private ZonedDateTime memberSince;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "user_id")
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sex getSex() {
        return sex;
    }

    public UserExtension sex(Sex sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public UserExtension birthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public ZonedDateTime getMemberSince() {
        return memberSince;
    }

    public UserExtension memberSince(ZonedDateTime memberSince) {
        this.memberSince = memberSince;
        return this;
    }

    public void setMemberSince(ZonedDateTime memberSince) {
        this.memberSince = memberSince;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserExtension firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public UserExtension lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserExtension phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public byte[] getImage() {
        return image;
    }

    public UserExtension image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public UserExtension imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public UserExtension aboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
        return this;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public Long getUserId() {
        return userId;
    }

    public UserExtension userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserExtension userExtension = (UserExtension) o;
        if(userExtension.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userExtension.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserExtension{" +
            "id=" + id +
            ", sex='" + sex + "'" +
            ", birthdate='" + birthdate + "'" +
            ", memberSince='" + memberSince + "'" +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", phoneNumber='" + phoneNumber + "'" +
            ", image='" + image + "'" +
            ", imageContentType='" + imageContentType + "'" +
            ", aboutMe='" + aboutMe + "'" +
            ", userId='" + userId + "'" +
            '}';
    }
}
