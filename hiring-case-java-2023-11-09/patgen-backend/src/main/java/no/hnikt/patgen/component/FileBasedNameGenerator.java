
package no.hnikt.patgen.component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * @author Jackie Chan
 *
 */
@Component
public class FileBasedNameGenerator implements NameGenerator {
	
	@Value("${dirname}")
	private String dirname;
	
	private final Random random = new Random(System.currentTimeMillis());
	
	public String maleFirstName() {
		try {
			return selectName("male-first.txt");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String femaleFirstName() {
		try {
			return selectName("female-first.txt");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String lastName() {
		try {
			return selectName("lastnames.txt");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private String selectName(String filename) throws IOException {
		List<String> nameList = new ArrayList<>();

		InputStream fileStream ;
		if (dirname == null || "".equals(dirname)) {
			fileStream = new ClassPathResource(filename).getInputStream();
		} else {
			fileStream = new FileInputStream(Paths.get(dirname, filename).toString());
		}
		BufferedReader reader =  new BufferedReader(new InputStreamReader(fileStream, StandardCharsets.UTF_8));
		
		String currentName;
		while ((currentName = reader.readLine()) != null) {
			nameList.add(currentName);
		}
		
		return nameList.get(random.nextInt(nameList.size()));
	}
}
