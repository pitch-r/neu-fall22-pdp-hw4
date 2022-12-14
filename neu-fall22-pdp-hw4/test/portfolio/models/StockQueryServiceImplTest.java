package portfolio.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import portfolio.models.entities.StockListEntry;
import portfolio.models.entities.StockPrice;
import portfolio.helper.StockApiMock;
import portfolio.models.stockprice.StockPriceApi;
import portfolio.models.stockprice.StockQueryService;
import portfolio.models.stockprice.StockQueryServiceImpl;

/**
 * This is a test class to test StockQueryServiceImpl class.
 */
public class StockQueryServiceImplTest {

  private StockQueryService stockQueryService;
  private StockPriceApi stockPriceApi;
  private final Map<String, Integer> map = new HashMap<>();

  private final double EPSILON = 0.000000001;

  @Before
  public void setUp() {
    map.put("AAPL", 100);
    map.put("AAA", 10000);
  }

  @Test
  public void getStockPrice() throws Exception {
    stockPriceApi = new StockApiMock(false);
    stockQueryService = new StockQueryServiceImpl(stockPriceApi);

    List<String> list = new ArrayList<>();
    list.add("AAPL");
    list.add("AAA");

    Map<String, StockPrice> map = new HashMap<>();
    map.put("AAA", new StockPrice(11, 22, 33, 44, 55));
    map.put("AAPL", new StockPrice(1, 2, 3, 4, 5));

    var actual = stockQueryService.getStockPrice(LocalDate.parse("2022-10-10"), list);

    assertEquals(map.size(), actual.size());
    assertEquals(map.get("AAA").getHigh(), actual.get("AAA").getHigh(), EPSILON);
    assertEquals(map.get("AAA").getLow(), actual.get("AAA").getLow(), EPSILON);
    assertEquals(map.get("AAA").getOpen(), actual.get("AAA").getOpen(), EPSILON);
    assertEquals(map.get("AAA").getClose(), actual.get("AAA").getClose(), EPSILON);
    assertEquals(map.get("AAA").getVolume(), actual.get("AAA").getVolume(), EPSILON);
    assertEquals(map.get("AAPL").getHigh(), actual.get("AAPL").getHigh(), EPSILON);
    assertEquals(map.get("AAPL").getLow(), actual.get("AAPL").getLow(), EPSILON);
    assertEquals(map.get("AAPL").getOpen(), actual.get("AAPL").getOpen(), EPSILON);
    assertEquals(map.get("AAPL").getClose(), actual.get("AAPL").getClose(), EPSILON);
    assertEquals(map.get("AAPL").getVolume(), actual.get("AAPL").getVolume(), EPSILON);
  }

  @Test
  public void getStockPrice_StockNotFound() {
    stockPriceApi = new StockApiMock(false);
    stockQueryService = new StockQueryServiceImpl(stockPriceApi);

    List<String> list = new ArrayList<>();
    list.add("AAPL");
    list.add("ABC");

    try {
      stockQueryService.getStockPrice(LocalDate.parse("2022-10-10"), list);
      fail("should fail");
    } catch (Exception e) {
      assertEquals("[ABC] is not a valid stock symbol.", e.getMessage());
    }
  }

  @Test
  public void getStockPrice_DateNotFound() {
    stockPriceApi = new StockApiMock(false);
    stockQueryService = new StockQueryServiceImpl(stockPriceApi);

    List<String> list = new ArrayList<>();
    list.add("AAPL");
    list.add("AAA");

    try {
      stockQueryService.getStockPrice(LocalDate.parse("2022-10-12"), list);
      fail("should fail");
    } catch (Exception e) {
      assertEquals("Stock [AAPL] is not available at date 2022-10-12.", e.getMessage());
    }
  }

  @Test
  public void getStockList() throws Exception {
    stockPriceApi = new StockApiMock(false);
    stockQueryService = new StockQueryServiceImpl(stockPriceApi);
    List<StockListEntry> list = stockQueryService.getStockList();
    assertEquals(2, list.size());
    assertEquals("AAPL", list.get(0).getSymbol());
    assertEquals("AAA", list.get(1).getSymbol());
  }

  @Test
  public void getStockList_Fail() {
    stockPriceApi = new StockApiMock(true);
    stockQueryService = new StockQueryServiceImpl(stockPriceApi);
    try {
      stockQueryService.getStockList();
      fail("should fail");
    } catch (Exception e) {
      assertEquals("Something wrong.", e.getMessage());
    }
  }
}
