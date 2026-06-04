package model;

public class Character {
    private String name;
    private String realName;
    private String birthDate;
    private String description;

    public Character(String name, String realName, String birthDate, String description) {
        this.name = name;
        this.realName = realName;
        this.birthDate = birthDate;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getRealName() {
        return realName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", realName='" + realName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}