package portfolio.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import portfolio.controllers.impl.CreatePageController;
import portfolio.controllers.impl.InfoPageController;
import portfolio.controllers.impl.MainPageController;
import portfolio.entities.Portfolio;
import portfolio.mock.ArgumentCaptor;
import portfolio.mock.IOServiceMock;
import portfolio.mock.StockApiMock;
import portfolio.mock.ViewFactoryWithArgumentCaptor;
import portfolio.services.portfolio.PortfolioService;
import portfolio.services.portfolio.PortfolioServiceImpl;
import portfolio.services.stockprice.StockQueryService;
import portfolio.services.stockprice.StockQueryServiceImpl;
import portfolio.views.ViewFactory;

/**
 * This is a test class to test CreatePageController class.
 */
public class CreatePageControllerTest {
  private ArgumentCaptor<Object> argumentCaptor;
  private PageController pageController;
  private final Map<String, Integer> map = new HashMap<>();

  private final double EPSILON = 0.000000001;

  @Before
  public void setUp() {
    PortfolioService portfolioService = new PortfolioServiceImpl(new IOServiceMock());
    argumentCaptor = new ArgumentCaptor<>();
    ViewFactory viewFactory = new ViewFactoryWithArgumentCaptor(argumentCaptor);
    StockQueryService stockQueryService = new StockQueryServiceImpl(new StockApiMock(false));
    PageControllerFactory pageControllerFactory = new PageControllerFactory(portfolioService,
        stockQueryService,
        viewFactory);

    map.put("AAPL", 100);
    map.put("AAA", 10000);
    Portfolio portfolio = new Portfolio(map);

    pageController = new CreatePageController(stockQueryService, portfolioService,
        pageControllerFactory,
        viewFactory);
  }

  @Test
  public void getView_init() {
    pageController.getView();
    assertFalse((boolean) argumentCaptor.getArguments().get(0));
    assertFalse((boolean) argumentCaptor.getArguments().get(1));
    HashMap<String, Integer> stockList = (HashMap<String, Integer>) argumentCaptor.getArguments()
        .get(2);
    assertTrue(stockList.isEmpty());
    assertNull(argumentCaptor.getArguments().get(3));
  }

  @Test
  public void handelInput_back() {
    PageController nextPage = pageController.handleInput("back");
    assertEquals(MainPageController.class, nextPage.getClass());
  }

  @Test
  public void handleInput_enterStock() {
    PageController nextPage = pageController.handleInput("AAPL,100");
    assertEquals(pageController, nextPage);
    nextPage = pageController.handleInput("AAA,100");
    assertEquals(pageController, nextPage);

    pageController.getView();
    assertFalse((boolean) argumentCaptor.getArguments().get(0));
    assertFalse((boolean) argumentCaptor.getArguments().get(1));
    HashMap<String, Integer> stockList = (HashMap<String, Integer>) argumentCaptor.getArguments()
        .get(2);
    assertEquals(2, stockList.size());
    assertNull(argumentCaptor.getArguments().get(3));
  }

  @Test
  public void handleInput_enterStock_negativeShares() {
    PageController nextPage = pageController.handleInput("AAPL,-100");
    assertEquals(pageController, nextPage);

    pageController.getView();
    assertFalse((boolean) argumentCaptor.getArguments().get(0));
    assertFalse((boolean) argumentCaptor.getArguments().get(1));
    HashMap<String, Integer> stockList = (HashMap<String, Integer>) argumentCaptor.getArguments()
        .get(2);
    assertTrue(stockList.isEmpty());
    assertEquals("The shares cannot be negative and 0.", argumentCaptor.getArguments().get(3));
  }

  @Test
  public void handleInput_enterStock_stockNotFound() {
    PageController nextPage = pageController.handleInput("BKNG,100");
    assertEquals(pageController, nextPage);

    pageController.getView();
    assertFalse((boolean) argumentCaptor.getArguments().get(0));
    assertFalse((boolean) argumentCaptor.getArguments().get(1));
    HashMap<String, Integer> stockList = (HashMap<String, Integer>) argumentCaptor.getArguments()
        .get(2);
    assertTrue(stockList.isEmpty());
    assertEquals("Symbol not found.", argumentCaptor.getArguments().get(3));
  }

  @Test
  public void handleInput_enterStock_shareNotNumber() {
    PageController nextPage = pageController.handleInput("AAPL,abc");
    assertEquals(pageController, nextPage);

    pageController.getView();
    assertFalse((boolean) argumentCaptor.getArguments().get(0));
    assertFalse((boolean) argumentCaptor.getArguments().get(1));
    HashMap<String, Integer> stockList = (HashMap<String, Integer>) argumentCaptor.getArguments()
        .get(2);
    assertTrue(stockList.isEmpty());
    assertEquals("The share is not a number.", argumentCaptor.getArguments().get(3));
  }

  @Test
  public void handleInput_enterStock_wrongInput1() {
    PageController nextPage = pageController.handleInput("abc");
    assertEquals(pageController, nextPage);

    pageController.getView();
    assertFalse((boolean) argumentCaptor.getArguments().get(0));
    assertFalse((boolean) argumentCaptor.getArguments().get(1));
    HashMap<String, Integer> stockList = (HashMap<String, Integer>) argumentCaptor.getArguments()
        .get(2);
    assertTrue(stockList.isEmpty());
    assertEquals("Error Format!", argumentCaptor.getArguments().get(3));
  }

  @Test
  public void handleInput_enterStock_wrongInput2() {
    PageController nextPage = pageController.handleInput("");
    assertEquals(pageController, nextPage);

    pageController.getView();
    assertFalse((boolean) argumentCaptor.getArguments().get(0));
    assertFalse((boolean) argumentCaptor.getArguments().get(1));
    HashMap<String, Integer> stockList = (HashMap<String, Integer>) argumentCaptor.getArguments()
        .get(2);
    assertTrue(stockList.isEmpty());
    assertEquals("Error Format!", argumentCaptor.getArguments().get(3));
  }

  @Test
  public void handleInput_enterStock_end() {
    pageController.handleInput("AAPL,100");
    pageController.handleInput("AAA,100");
    PageController nextPage = pageController.handleInput("end");
    assertEquals(pageController, nextPage);

    pageController.getView();
    assertTrue((boolean) argumentCaptor.getArguments().get(0));
    assertFalse((boolean) argumentCaptor.getArguments().get(1));
    HashMap<String, Integer> stockList = (HashMap<String, Integer>) argumentCaptor.getArguments()
        .get(2);
    assertEquals(2, stockList.size());
    assertNull(argumentCaptor.getArguments().get(3));
  }

  @Test
  public void handleInput_enterStock_end_name() {
    pageController.handleInput("AAPL,100");
    pageController.handleInput("AAA,100");
    pageController.handleInput("end");
    PageController nextPage = pageController.handleInput("a");
    assertEquals(pageController, nextPage);

    pageController.getView();
    assertTrue((boolean) argumentCaptor.getArguments().get(0));
    assertTrue((boolean) argumentCaptor.getArguments().get(1));
    HashMap<String, Integer> stockList = (HashMap<String, Integer>) argumentCaptor.getArguments()
        .get(2);
    assertEquals(2, stockList.size());
    assertNull(argumentCaptor.getArguments().get(3));
  }

  @Test
  public void handleInput_enterStock_end_fileExists() {
    pageController.handleInput("AAPL,100");
    pageController.handleInput("AAA,100");
    pageController.handleInput("end");
    PageController nextPage = pageController.handleInput("abc");
    assertEquals(pageController, nextPage);

    pageController.getView();
    assertTrue((boolean) argumentCaptor.getArguments().get(0));
    assertFalse((boolean) argumentCaptor.getArguments().get(1));
    HashMap<String, Integer> stockList = (HashMap<String, Integer>) argumentCaptor.getArguments()
        .get(2);
    assertEquals(2, stockList.size());
    assertEquals("File already exists.", argumentCaptor.getArguments().get(3));
  }

  @Test
  public void handleInput_enterStock_end_errorSaveFile() {
    pageController.handleInput("AAPL,100");
    pageController.handleInput("AAA,100");
    pageController.handleInput("end");
    PageController nextPage = pageController.handleInput("otherioerror");
    assertEquals(pageController, nextPage);

    pageController.getView();
    assertTrue((boolean) argumentCaptor.getArguments().get(0));
    assertFalse((boolean) argumentCaptor.getArguments().get(1));
    HashMap<String, Integer> stockList = (HashMap<String, Integer>) argumentCaptor.getArguments()
        .get(2);
    assertEquals(2, stockList.size());
    assertNull(argumentCaptor.getArguments().get(3));
  }

  @Test
  public void handleInput_enterStock_end_listEmpty() {
    PageController nextPage = pageController.handleInput("end");
    assertEquals(pageController, nextPage);

    pageController.getView();
    assertFalse((boolean) argumentCaptor.getArguments().get(0));
    assertFalse((boolean) argumentCaptor.getArguments().get(1));
    assertEquals("No stock entered. Please input stock.", argumentCaptor.getArguments().get(3));
  }

  @Test
  public void handleInput_enterStock_NoStock() {
    PageController nextPage = pageController.handleInput("AAPL,100");
    assertEquals(pageController, nextPage);
    nextPage = pageController.handleInput("AAA,100");
    assertEquals(pageController, nextPage);

    pageController.getView();
    assertFalse((boolean) argumentCaptor.getArguments().get(0));
    assertFalse((boolean) argumentCaptor.getArguments().get(1));
    HashMap<String, Integer> stockList = (HashMap<String, Integer>) argumentCaptor.getArguments()
        .get(2);
    assertEquals(2, stockList.size());
    assertNull(argumentCaptor.getArguments().get(3));
  }

  @Test
  public void handleInput_enterStock_useKeyword() {
    pageController.handleInput("AAPL,100");
    pageController.handleInput("end");
    PageController nextPage = pageController.handleInput("yes");
    assertEquals(pageController, nextPage);

    pageController.getView();
    assertTrue((boolean) argumentCaptor.getArguments().get(0));
    assertFalse((boolean) argumentCaptor.getArguments().get(1));
    assertEquals("The name cannot be end, back, no and yes.", argumentCaptor.getArguments().get(3));
  }

  @Test
  public void handleInput_enterStock_success() {
    pageController.handleInput("AAPL,100");
    pageController.handleInput("end");
    pageController.handleInput("a");
    PageController nextPage = pageController.handleInput("yes");
    assertEquals(InfoPageController.class, nextPage.getClass());
  }

  @Test
  public void handleInput_enterStock_end_no() {
    pageController.handleInput("AAPL,100");
    pageController.handleInput("end");
    pageController.handleInput("a");
    PageController nextPage = pageController.handleInput("no");
    assertEquals(MainPageController.class, nextPage.getClass());
  }
}
