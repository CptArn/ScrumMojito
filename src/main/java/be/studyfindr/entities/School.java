package be.studyfindr.entities;

import org.bson.Document;

/**
 * Represents a school
 */
public class School {

    /**
     * Creates a new school instance
     * @param name name of school
     * @param address address
     */
    public School(String name, String address) {
        super();
        this.name = name;
        this.address = address;
    }

    /**
     * Parses a new school object from a document
     * @param o document containing school info
     */
    public School(Document o) {
        this.id = (int)o.get("_id");
        this.name = o.getString("name");
        this.address = o.getString("address");
    }

    // private properties
    private int id;
    private String name;
    private String address;

    /**
     * Returns the school id
     * @return school id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the school id
     * @param id new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the school
     * @return name of school
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the school
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the school address
     * @return school address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the school address
     * @param address new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Compares this object with an other object
     * @param obj object to compare
     * @return true if both object are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        School other = (School) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
