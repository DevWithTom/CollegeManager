import java.io.Serializable;

// implements Serializable to make Student Serializable
public abstract class Person implements Serializable {

    // person id
    protected String id;
    // person name
    protected String name;

    // constructor
    public Person(String id, String name) {
        setId(id);
        setName(name);
    }

    // person id getter
    public String getId() {
        return this.id;
    }

    // person name getter
    public String getName() {
        return this.name;
    }

    // person id setter
    public void setId(String personId) {
        if (personId == null) {
            throw new NullPointerException("id of a person cannot be null!");
        }
        if (personId.isBlank()) {
            throw new IllegalArgumentException("id of a person cannot be empty!");
        }
        this.id = personId.trim();
    }

    // person name setter
    public void setName(String personName) {
        if (personName == null) {
            throw new NullPointerException("name cannot be null!");
        }
        if (personName.isBlank()) {
            throw new IllegalArgumentException("name cannot be empty!");
        }
        this.name = personName.trim();
    }

    // people are uniquely defined by their id, only the id determines equality
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (o instanceof Person) {
            Person other = (Person) o;
            return getId().equals(other.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                '}';
    }
}
