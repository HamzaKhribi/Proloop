package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses;

/**
 * Created by mal21 on 04/05/2016.
 */
public class User {

    public String id;
    public String name;
    public String lastName;
    public String country;
    public String age;
    public String gender;
    public String about_user;
    public String profilePic;
    public String bio;

    public User(String id, String name, String lastName, String country, String age, String gender, String about_user, String profilePic, String bio) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.country = country;
        this.age = age;
        this.gender = gender;
        this.about_user = about_user;
        this.profilePic = profilePic;
        this.bio = bio;
    }

    public User(String name, String lastName, String country, String age, String profilePic, String bio) {
        this.name = name;
        this.lastName = lastName;
        this.country = country;
        this.age = age;
        this.profilePic = profilePic;
        this.bio = bio;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAbout_user() {
        return about_user;
    }

    public void setAbout_user(String about_user) {
        this.about_user = about_user;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", country='" + country + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", about_user='" + about_user + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}




