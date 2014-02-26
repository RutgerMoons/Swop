
public class TestShizzle {
	public static void main(String[] args) {
		String input = "PUT server/resource port protocol";
		String[] parse = input.split("/");
		for (String str : parse) {
			System.out.println(str);
		}
		
	}
}
