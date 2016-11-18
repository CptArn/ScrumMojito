package be.studyfindr;

import be.studyfindr.entities.School;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SchoolTest {
    static School s1;

    @BeforeClass
    public static void setUp() {
        s1 = new School("Odisee", "Gent");
    }

    @Test
    public void test1GettersAndSetters() {
        s1.setAddress("Kortrijk");
        s1.setName("Howest");
        s1.setId(42);
        assert(s1.getAddress() == "Kortrijk" && s1.getName() == "Howest" && s1.getId() == 42);
    }
}
