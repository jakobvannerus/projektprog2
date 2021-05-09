
import java.util.NoSuchElementException;

/**
 * Ett väldigt enkelt testprogram som testar några av
 * metoderna i Graph och kastar undantag om de inte fungerar.
 * 
 * Absolut inte heltäckande tester så: 
 * fel == inte ok.
 * inga fel != godkänt (det kan finnas andra fel).
 * 
 * Filen ska inte lämnas in.
 */
public class I1 {

	public static final String MISSING_NODE = "Z";
	private final Graph<String> stringGraph = new ListGraph<>();

	public static void main(String[] args) throws Exception {

		var app = new I1();

		// testa att grafen är tom från start
		app.test0_new_graph_is_empty();

		// testa att lägga till nod
		app.test1_add_no_duplicate();

		// testa att lägga till samma nod igen
		app.test2_add_with_duplicate();

		// testa connect
		app.test3_connect();

		// testa disconnect
		app.test4_disconnect();

		// testa remove
		app.test5_remove();
	}

	private void test0_new_graph_is_empty() throws Exception {
		if (!stringGraph.getNodes().isEmpty())
			throw new Exception("Error: newly created graph should be empty.");
	}

	private void test1_add_no_duplicate() throws Exception {
		stringGraph.add("A");
		if (stringGraph.getNodes().size() != 1)
			throw new Exception("Error: graph should have 1 node after add 'A'.");
	}

	private void test2_add_with_duplicate() throws Exception {
		stringGraph.add("A");
		if (stringGraph.getNodes().size() != 1)
			throw new Exception("Error: graph should still have 1 node after add 'A' a second time.");
	}

	private void test3_connect() throws Exception {
		test3_connect_ok();
		test3_connect_throws_connection_exists();
		test3_connect_throws_missing_node();
		test3_connect_throws_negative_weight();
	}

	private void test3_connect_ok() throws Exception {
		stringGraph.add("B");
		if (stringGraph.getNodes().size() != 2)
			throw new Exception("Error: graph should have 2 nodes after adding 'A' and 'B'");

		stringGraph.connect("A", "B", "A <=> B", 1);
	}

	private void test3_connect_throws_connection_exists() throws Exception {
		stringGraph.add("B");
		if (stringGraph.getNodes().size() != 2)
			throw new Exception("Error: graph should have 2 node after adding 'A' and 'B'.");

		try {
			stringGraph.connect("A", "B", "A <=> B", 1);
		} catch (IllegalStateException e) {
			// ok
			return;
		}
		throw new Exception("Error: should have thrown IllegalStateException.");
	}

	private void test3_connect_throws_missing_node() throws Exception {
		stringGraph.add("B");
		if (stringGraph.getNodes().size() != 2)
			throw new Exception("Error: graph should have 2 node after adding 'A' and 'B'.");

		try {
			stringGraph.connect("A", MISSING_NODE, "A <=> Z", 1);
		} catch (NoSuchElementException e) {
			// ok
			return;
		}
		throw new Exception("Error: should have thrown NoSuchElementException.");
	}

	private void test3_connect_throws_negative_weight() throws Exception {
		stringGraph.add("B");
		if (stringGraph.getNodes().size() != 2)
			throw new Exception("Error: graph should have 2 node after adding 'A' and 'B'.");

		try {
			stringGraph.connect("A", "B", "A <=> B", -1);
		} catch (IllegalArgumentException e) {
			// ok
			return;
		}
		throw new Exception("Error: should have thrown IllegalArgumentException.");
	}

	private void test4_connect_throws_missing_node() throws Exception {
		try {
			stringGraph.disconnect("A", MISSING_NODE);
		} catch (NoSuchElementException e) {
			// ok
			return;
		}
		throw new Exception("Error: should have thrown NoSuchElementException.");
	}

	private void test4_connect_throws_no_connection_exists() throws Exception {
		stringGraph.add("C");
		try {
			stringGraph.disconnect("A", "C");
		} catch (IllegalStateException e) {
			// ok
			return;
		}
		throw new Exception("Error: should have thrown IllegalStateException.");
	}

	private void test4_disconnect() throws Exception {
		test4_disconnect_ok();
		test4_connect_throws_no_connection_exists();
		test4_connect_throws_missing_node();
	}

	private void test4_disconnect_ok() throws Exception {
		stringGraph.disconnect("A", "B");
		if (stringGraph.getEdgeBetween("A", "B") != null) {
			throw new Exception("Error: edge not disconnected.");
		}
	}

	private void test5_remove() throws Exception {
		test5_remove_missing_node();
		test5_remove_ok();
	}

	private void test5_remove_missing_node() throws Exception {
		try {
			stringGraph.remove(MISSING_NODE);
		} catch (NoSuchElementException e) {
			// ok
			return;
		}
		throw new Exception("Error: should have thrown NoSuchElementException.");
	}

	private void test5_remove_ok() throws Exception {
		stringGraph.remove("C");
		if (stringGraph.getNodes().contains("C")) {
			throw new Exception("Error: node not removed.");
		}
	}
}
