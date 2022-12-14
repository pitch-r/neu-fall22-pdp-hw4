package portfolio.views;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import portfolio.views.impl.InflexibleCreatePageView;

/**
 * This is a test class to test CreatePageView class.
 */
public class InflexibleCreatePageViewTest {

  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
  private PrintStream printStream;

  private Map<String, Integer> map;


  @Before
  public void setUp() {
    printStream = new PrintStream(outputStreamCaptor);
    map = new HashMap<>();
  }

  @Test
  public void testRender_First() {
    setUp();
    View view =
        new InflexibleCreatePageView(printStream, false,
            false, map, null);
    view.render();
    assertEquals("-------------------------Tips-----------------------------\r\n"
        + "!!! If you enter back, you will back to the main menu.\r\n"
        + "!!! If you want to exit, please input exit\r\n"
        + "----------------------------------------------------------\r\n"
        + "Enter symbol and number of shares for one stock. The format is: AAPL,100.\r\n"
        + "--The symbol must be capital letters and the shares need to be numbers.\r\n"
        + "--The shares cannot be 0 and negative number.\r\n"
        + "--Between the symbol and shares must have a comma with no spaces.\r\n"
        + "--Enter end to finish input this portfolio.\r\n"
        + "input > ", outputStreamCaptor.toString());

  }

  @Test
  public void testRender_ErrorFormat() {
    setUp();
    View view = new InflexibleCreatePageView(printStream, false, false, map,
        "Error Format!");
    view.render();
    assertEquals("---------------------ERROR--------------------------"
        + "------\r\n"
        + "! Error message: Error Format!\r\n"
        + "----------------------------------------------------------\r\n"
        + "-------------------------Tips-----------------------------\r\n"
        + "!!! If you enter back, you will back to the main menu.\r\n"
        + "!!! If you want to exit, please input exit\r\n"
        + "----------------------------------------------------------\r\n"
        + "Enter symbol and number of shares for one stock. The format is: AAPL,100.\r\n"
        + "--The symbol must be capital letters and the shares need to be numbers.\r\n"
        + "--The shares cannot be 0 and negative number.\r\n"
        + "--Between the symbol and shares must have a comma with no spaces.\r\n"
        + "--Enter end to finish input this portfolio.\r\n"
        + "input > ", outputStreamCaptor.toString());

  }

  @Test
  public void testRender_ErrorFormat2() {
    setUp();
    map.put("AAA", 100);
    View view = new InflexibleCreatePageView(printStream, false, false, map,
        "Error Format!");
    view.render();
    assertEquals("---------------------ERROR--------------------------------\r\n"
        + "! Error message: Error Format!\r\n"
        + "----------------------------------------------------------\r\n"
        + "-------------------------Tips-----------------------------\r\n"
        + "!!! If you enter back, you will back to the main menu.\r\n"
        + "!!! If you want to exit, please input exit\r\n"
        + "----------------------------------------------------------\r\n"
        + "          +---------+---------------+\r\n"
        + "Selected: |    Stock|  No. of shares|\r\n"
        + "          +---------+---------------+\r\n"
        + "          |      AAA|            100|\r\n"
        + "          +---------+---------------+\r\n"
        + "Enter symbol and number of shares for one stock. The format is: AAPL,100.\r\n"
        + "--The symbol must be capital letters and the shares need to be numbers.\r\n"
        + "--The shares cannot be 0 and negative number.\r\n"
        + "--Between the symbol and shares must have a comma with no spaces.\r\n"
        + "--Enter end to finish input this portfolio.\r\n"
        + "input > ", outputStreamCaptor.toString());

  }

  @Test
  public void testRender_ErrorShares() {
    setUp();
    View view = new InflexibleCreatePageView(printStream, false, false, map,
        "The shares cannot be negative and 0.");
    view.render();
    assertEquals("---------------------ERROR--------------------------"
        + "------\r\n"
        + "! Error message: The shares cannot be negative and 0.\r\n"
        + "----------------------------------------------------------\r\n"
        + "-------------------------Tips-----------------------------\r\n"
        + "!!! If you enter back, you will back to the main menu.\r\n"
        + "!!! If you want to exit, please input exit\r\n"
        + "----------------------------------------------------------\r\n"
        + "Enter symbol and number of shares for one stock. The format is: AAPL,100.\r\n"
        + "--The symbol must be capital letters and the shares need to be numbers.\r\n"
        + "--The shares cannot be 0 and negative number.\r\n"
        + "--Between the symbol and shares must have a comma with no spaces.\r\n"
        + "--Enter end to finish input this portfolio.\r\n"
        + "input > ", outputStreamCaptor.toString());
  }

  @Test
  public void testRender_ErrorShares2() {
    setUp();
    map.put("AAA", 100);
    View view = new InflexibleCreatePageView(printStream, false, false, map,
        "The shares cannot be negative and 0.");
    view.render();
    assertEquals("---------------------ERROR----------"
        + "----------------------\r\n"
        + "! Error message: The shares cannot be negative and 0.\r\n"
        + "----------------------------------------------------------\r\n"
        + "-------------------------Tips-----------------------------\r\n"
        + "!!! If you enter back, you will back to the main menu.\r\n"
        + "!!! If you want to exit, please input exit\r\n"
        + "----------------------------------------------------------\r\n"
        + "          +---------+---------------+\r\n"
        + "Selected: |    Stock|  No. of shares|\r\n"
        + "          +---------+---------------+\r\n"
        + "          |      AAA|            100|\r\n"
        + "          +---------+---------------+\r\n"
        + "Enter symbol and number of shares for one stock. "
        + "The format is: AAPL,100.\r\n"
        + "--The symbol must be capital letters and the shares need to be numbers.\r\n"
        + "--The shares cannot be 0 and negative number.\r\n"
        + "--Between the symbol and shares must have a comma with no spaces.\r\n"
        + "--Enter end to finish input this portfolio.\r\n"
        + "input > ", outputStreamCaptor.toString());
  }

  @Test
  public void testRender_ErrorSymbol() {
    setUp();
    View view = new InflexibleCreatePageView(printStream, false, false, map,
        "Symbol not found.");
    view.render();
    assertEquals("---------------------ERROR------------------"
        + "--------------\r\n"
        + "! Error message: Symbol not found.\r\n"
        + "----------------------------------------------------------\r\n"
        + "-------------------------Tips-----------------------------\r\n"
        + "!!! If you enter back, you will back to the main menu.\r\n"
        + "!!! If you want to exit, please input exit\r\n"
        + "----------------------------------------------------------\r\n"
        + "Enter symbol and number of shares for one stock. "
        + "The format is: AAPL,100.\r\n"
        + "--The symbol must be capital letters and the shares need to"
        + " be numbers.\r\n"
        + "--The shares cannot be 0 and negative number.\r\n"
        + "--Between the symbol and shares must have a comma with no spaces.\r\n"
        + "--Enter end to finish input this portfolio.\r\n"
        + "input > ", outputStreamCaptor.toString());
  }

  @Test
  public void testRender_ErrorSymbol2() {
    setUp();
    map.put("AAA", 100);
    View view = new InflexibleCreatePageView(printStream, false, false, map,
        "Symbol not found.");
    view.render();
    assertEquals("---------------------ERROR--------"
        + "------------------------\r\n"
        + "! Error message: Symbol not found.\r\n"
        + "----------------------------------------------------------\r\n"
        + "-------------------------Tips-----------------------------\r\n"
        + "!!! If you enter back, you will back to the main menu.\r\n"
        + "!!! If you want to exit, please input exit\r\n"
        + "----------------------------------------------------------\r\n"
        + "          +---------+---------------+\r\n"
        + "Selected: |    Stock|  No. of shares|\r\n"
        + "          +---------+---------------+\r\n"
        + "          |      AAA|            100|\r\n"
        + "          +---------+---------------+\r\n"
        + "Enter symbol and number of shares for one stock. "
        + "The format is: AAPL,100.\r\n"
        + "--The symbol must be capital letters and the "
        + "shares need to be numbers.\r\n"
        + "--The shares cannot be 0 and negative number.\r\n"
        + "--Between the symbol and shares must have a comma with no spaces.\r\n"
        + "--Enter end to finish input this portfolio.\r\n"
        + "input > ", outputStreamCaptor.toString());
  }

  @Test
  public void testRender_End() {
    setUp();
    map.put("AAA", 100);
    map.put("AA", 200);
    View view = new InflexibleCreatePageView(printStream, true, false, map,
        null);
    view.render();
    assertEquals("-------------------------Tips-----------------------------\r\n"
        + "!!! If you enter back, you will back to the main menu.\r\n"
        + "!!! If you want to exit, please input exit\r\n"
        + "----------------------------------------------------------\r\n"
        + "          +---------+---------------+\r\n"
        + "Selected: |    Stock|  No. of shares|\r\n"
        + "          +---------+---------------+\r\n"
        + "          |       AA|            200|\r\n"
        + "          |      AAA|            100|\r\n"
        + "          +---------+---------------+\r\n"
        + "Please enter the file name of this portfolio.The name cannot be end,"
        + " back, no and yes\r\n"
        + "input > ", outputStreamCaptor.toString());
  }

  @Test
  public void testRender_NoStock() {
    setUp();
    View view = new InflexibleCreatePageView(printStream, false, false, map,
        "No stock entered. Please input stock.");
    view.render();
    assertEquals("---------------------ERROR--------------"
        + "------------------\r\n"
        + "! Error message: No stock entered. Please input stock.\r\n"
        + "----------------------------------------------------------\r\n"
        + "-------------------------Tips-----------------------------\r\n"
        + "!!! If you enter back, you will back to the main menu.\r\n"
        + "!!! If you want to exit, please input exit\r\n"
        + "----------------------------------------------------------\r\n"
        + "Enter symbol and number of shares for one stock. The format is: AAPL,100.\r\n"
        + "--The symbol must be capital letters and the shares need to be numbers.\r\n"
        + "--The shares cannot be 0 and negative number.\r\n"
        + "--Between the symbol and shares must have a comma with no spaces.\r\n"
        + "--Enter end to finish input this portfolio.\r\n"
        + "input > ", outputStreamCaptor.toString());
  }


  @Test
  public void testRender_Name() {
    setUp();
    map.put("AAA", 100);
    map.put("AA", 200);
    View view = new InflexibleCreatePageView(printStream, true, true, map,
        null);
    view.render();
    assertEquals("-------------------------Tips-----------------------------\r\n"
            + "!!! If you enter back, you will back to the main menu.\r\n"
            + "!!! If you want to exit, please input exit\r\n"
            + "----------------------------------------------------------\r\n"
            + "Do you want to determine the total value of this portfolio?\r\n"
            + "--Please enter yes if you want to determine. Other input will be "
            + "back to the main menu.\r\n" + "input > "
        , outputStreamCaptor.toString());
  }

  @Test
  public void testRender_NameError() {
    setUp();
    map.put("AAA", 100);
    map.put("AA", 200);
    View view = new InflexibleCreatePageView(printStream, true, false, map,
        "The name cannot be end, back, no and yes.");
    view.render();
    assertEquals("---------------------ERROR------------"
        + "--------------------\r\n"
        + "! Error message: The name cannot be end, back, no and yes.\r\n"
        + "----------------------------------------------------------\r\n"
        + "-------------------------Tips-----------------------------\r\n"
        + "!!! If you enter back, you will back to the main menu.\r\n"
        + "!!! If you want to exit, please input exit\r\n"
        + "----------------------------------------------------------\r\n"
        + "          +---------+---------------+\r\n"
        + "Selected: |    Stock|  No. of shares|\r\n"
        + "          +---------+---------------+\r\n"
        + "          |       AA|            200|\r\n"
        + "          |      AAA|            100|\r\n"
        + "          +---------+---------------+\r\n"
        + "Please enter the file name of this portfolio.The name cannot "
        + "be end, back, no and yes\r\n"
        + "input > ", outputStreamCaptor.toString());
  }

  @Test
  public void testRender_PortfolioExists() {
    setUp();
    map.put("AAA", 100);
    map.put("AA", 200);
    View view = new InflexibleCreatePageView(printStream, true, false, map,
        "There is a file or a directory exists with filename: 222.txt");
    view.render();
    assertEquals("---------------------ERROR--------------------------------\r\n"
            + "! Error message: There is a file or a directory exists "
            + "with filename: 222.txt\r\n"
            + "----------------------------------------------------------\r\n"
            + "-------------------------Tips-----------------------------\r\n"
            + "!!! If you enter back, you will back to the main menu.\r\n"
            + "!!! If you want to exit, please input exit\r\n"
            + "----------------------------------------------------------\r\n"
            + "          +---------+---------------+\r\n"
            + "Selected: |    Stock|  No. of shares|\r\n"
            + "          +---------+---------------+\r\n"
            + "          |       AA|            200|\r\n"
            + "          |      AAA|            100|\r\n"
            + "          +---------+---------------+\r\n"
            + "Please enter the file name of this portfolio.The name "
            + "cannot be end, back, no and yes\r\n"
            + "input > "
        , outputStreamCaptor.toString());
  }

  @Test
  public void testRender_ApiError() {
    setUp();
    View view = new InflexibleCreatePageView(printStream, false, false, map,
        "External API is not ready."
            + " Please try again in the next few minutes.");
    view.render();
    assertEquals("---------------------ERROR--------------------"
            + "------------\r\n"
            + "! Error message: External API is not ready. Please try again "
            + "in the next few minutes.\r\n"
            + "----------------------------------------------------------\r\n"
            + "-------------------------Tips-----------------------------\r\n"
            + "!!! If you enter back, you will back to the main menu.\r\n"
            + "!!! If you want to exit, please input exit\r\n"
            + "----------------------------------------------------------\r\n"
            + "Enter symbol and number of shares for one stock. "
            + "The format is: AAPL,100.\r\n"
            + "--The symbol must be capital letters and the shares "
            + "need to be numbers.\r\n"
            + "--The shares cannot be 0 and negative number.\r\n"
            + "--Between the symbol and shares must have a"
            + " comma with no spaces.\r\n"
            + "--Enter end to finish input this portfolio.\r\n" + "input > "
        , outputStreamCaptor.toString());
  }
}
