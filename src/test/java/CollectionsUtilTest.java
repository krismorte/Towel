import java.util.List;

import model.Person;
import model.PreData;

public class CollectionsUtilTest {
	@SuppressWarnings("unused")
	private List<Person> list;

	// @Before
	public void setup() {
		PreData data = new PreData();
		list = data.getSampleList();
	}

}
