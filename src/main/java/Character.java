public class Character 
{
    private String name;
    private String realName;
    private String birthDate;
    private String description;

    public Character(String name, String realName, String birthDate, String description)
    {
        this.name = name;
        this.realName = realName;
        this.birthDate = birthDate;
        this.description = description;
    }

    @Override
    public String toString() {
        return name + " (" + realName + ")";
    }
}

