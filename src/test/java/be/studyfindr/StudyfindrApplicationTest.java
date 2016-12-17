package be.studyfindr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudyfindrApplicationTest {

	@Test
	public void test1HasMain() {
		try{
			assert(StudyfindrApplication.class.getMethod("main", String[].class) != null);
		}catch(Exception ex){
			fail(ex.getMessage());
		}
	}
}
