package triangle.syntacticAnalyser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import triangle.ErrorReporter;
import triangle.syntacticAnalyzer.Parser;
import triangle.syntacticAnalyzer.Scanner;
import triangle.syntacticAnalyzer.SourceFile;

public class TestScanner {
	
	/* some individual unit tests for helper methods in Scanner */

	@Test
	public void testIsDigit() {
		assertTrue(Scanner.isDigit('0'));
		assertTrue(Scanner.isDigit('1'));
		assertTrue(Scanner.isDigit('5'));
		assertTrue(Scanner.isDigit('8'));
		assertTrue(Scanner.isDigit('9'));
		assertFalse(Scanner.isDigit('a'));
		assertFalse(Scanner.isDigit('Z'));
		assertFalse(Scanner.isDigit('&'));
		assertFalse(Scanner.isDigit(';'));
		assertFalse(Scanner.isDigit('\n'));
	}
	
	@Test
	public void testIsOperator() {
		assertTrue(Scanner.isOperator('*'));
		assertTrue(Scanner.isOperator('/'));
		assertTrue(Scanner.isOperator('?'));
		assertTrue(Scanner.isOperator('+'));
		assertTrue(Scanner.isOperator('-'));
		assertFalse(Scanner.isOperator('a'));
		assertFalse(Scanner.isOperator('Z'));
		assertFalse(Scanner.isOperator('1'));
		assertFalse(Scanner.isOperator(';'));
		assertFalse(Scanner.isOperator('\n'));
	}


	/* these tests all try to compile example programs... */

	@Test
	public void testHiNewComment() {
		compileExpectSuccess("../programs/hi-newcomment.tri");
	}

	@Test
	public void testHiNewComment2() {
		compileExpectSuccess("../programs/hi-newcomment2.tri");
	}

	@Test
	public void testBarDemo() {
		compileExpectSuccess("../programs/bardemo.tri");
	}

	@Test
	public void testRepeatUntil() {
		compileExpectSuccess("../programs/repeatuntil.tri");
	}
	private void compileExpectSuccess(String filename) {
		// Resolve directly from the disk path relative to project root
		SourceFile source = SourceFile.ofPath(filename);
		
		Scanner scanner = new Scanner(source);
		ErrorReporter reporter = new ErrorReporter(true);
		Parser parser = new Parser(scanner, reporter);
		
		parser.parseProgram();
		
		assertEquals("Problem compiling " + filename, 0, reporter.getNumErrors());
	}
	
	private void compileExpectFailure(String filename) {
		SourceFile source = SourceFile.ofPath(filename);
		Scanner scanner = new Scanner(source);
		ErrorReporter reporter = new ErrorReporter(true);
		Parser parser = new Parser(scanner, reporter);
	
		assertThrows(RuntimeException.class, new ThrowingRunnable() {
			public void run(){
				parser.parseProgram();
			}
		});
		
		assertNotEquals("Problem compiling " + filename, 0, reporter.getNumErrors());
	}
}
