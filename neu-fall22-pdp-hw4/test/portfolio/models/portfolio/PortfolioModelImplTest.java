package portfolio.models.portfolio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import portfolio.helper.StockApiMock;
import portfolio.models.entities.PortfolioEntryWithValue;
import portfolio.models.entities.PortfolioFormat;
import portfolio.models.entities.PortfolioWithValue;
import portfolio.models.entities.Transaction;
import portfolio.models.entities.TransactionType;
import portfolio.models.portfolio.impl.PortfolioModelImpl;
import portfolio.models.portfolio.impl.PortfolioTextParser;
import portfolio.models.stockprice.StockQueryService;
import portfolio.models.stockprice.StockQueryServiceImpl;

/**
 * This is a test class to test PortfolioServiceImpl class.
 */
public class PortfolioModelImplTest {

  private PortfolioModel portfolioModel;
  private final Map<String, Integer> map = new HashMap<>();
  private final List<Transaction> transactions = new ArrayList<>();
  private final List<Transaction> transactions2 = new ArrayList<>();
  private final double EPSILON = 0.000000001;

  @Before
  public void setUp() throws Exception {
    StockQueryService stockQueryService = new StockQueryServiceImpl(new StockApiMock(false));
    portfolioModel = new PortfolioModelImpl(stockQueryService, new PortfolioTextParser());

    transactions.add(
        new Transaction(TransactionType.BUY, "AAA", 110, LocalDate.parse("2022-10-10"), 10));
    transactions.add(
        new Transaction(TransactionType.SELL, "AAA", 10, LocalDate.parse("2022-10-10"), 20));
    transactions.add(
        new Transaction(TransactionType.BUY, "AAPL", 1000, LocalDate.parse("2022-10-11"), 30));
    transactions2.add(
        new Transaction(TransactionType.SELL, "AAPL", 123, LocalDate.parse("2022-10-12"), 40));
    transactions2.add(
        new Transaction(TransactionType.BUY, "AA", 45, LocalDate.parse("2022-10-11"), 50));
  }

  @Test
  public void getPortfolio() throws Exception {
    portfolioModel.create("name", PortfolioFormat.INFLEXIBLE, transactions);
    Portfolio actual = portfolioModel.getPortfolio();
    assertNull(actual);

    portfolioModel.create("name", PortfolioFormat.INFLEXIBLE, transactions);
    actual = portfolioModel.getPortfolio();
    assertNotNull(actual);
  }

  @Test
  public void create_flexible() throws Exception {
    Portfolio actualPortfolio = portfolioModel.create("name", PortfolioFormat.FLEXIBLE, transactions);

    List<Transaction> actual = actualPortfolio.getTransactions();

    assertEquals(PortfolioFormat.FLEXIBLE, actualPortfolio.getFormat());
    assertEquals(transactions.size(), actual.size());
    for (int i = 0; i < transactions.size(); i++) {
      assertEquals(actual.get(i).getType(), transactions.get(i).getType());
      assertEquals(actual.get(i).getSymbol(), transactions.get(i).getSymbol());
      assertEquals(actual.get(i).getAmount(), transactions.get(i).getAmount());
      assertEquals(actual.get(i).getDate(), transactions.get(i).getDate());
    }
    Map<String, Integer> actualMap = actualPortfolio.getComposition();
    assertEquals(2, actualMap.size());
  }

  @Test
  public void create_inflexible() throws Exception {
    Portfolio actualPortfolio = portfolioModel.create("name", PortfolioFormat.INFLEXIBLE, transactions);

    List<Transaction> actual = actualPortfolio.getTransactions();

    assertEquals(PortfolioFormat.INFLEXIBLE, actualPortfolio.getFormat());
    assertEquals(transactions.size(), actual.size());
    for (int i = 0; i < transactions.size(); i++) {
      assertEquals(actual.get(i).getType(), transactions.get(i).getType());
      assertEquals(actual.get(i).getSymbol(), transactions.get(i).getSymbol());
      assertEquals(actual.get(i).getAmount(), transactions.get(i).getAmount());
      assertEquals(actual.get(i).getDate(), transactions.get(i).getDate());
    }
    Map<String, Integer> actualMap = actualPortfolio.getComposition();
    assertEquals(2, actualMap.size());
  }

  @Test
  public void create_transactionConflict() throws Exception {}

  @Test
  public void load() {

  }

  @Test
  public void checkTransaction_noPriceOnThatDate() throws Exception {
    Portfolio actualPortfolio = portfolioModel.create("name", PortfolioFormat.INFLEXIBLE, transactions);
  }

  @Test
  public void checkTransaction_symbolInvalid() throws Exception {
    Portfolio actualPortfolio = portfolioModel.create("name", PortfolioFormat.INFLEXIBLE, transactions);
  }

  @Test
  public void checkTransaction_apiUnavailable() throws Exception {
    Portfolio actualPortfolio = portfolioModel.create("name", PortfolioFormat.INFLEXIBLE, transactions);
  }

  @Test
  public void checkTransactions_noPriceOnThatDate() throws Exception {
    Portfolio actualPortfolio = portfolioModel.create("name", PortfolioFormat.INFLEXIBLE, transactions);
  }

  @Test
  public void checkTransactions_symbolInvalid() throws Exception {
    Portfolio actualPortfolio = portfolioModel.create("name", PortfolioFormat.INFLEXIBLE, transactions);
  }

  @Test
  public void checkTransactions_apiUnavailable() throws Exception {
    Portfolio actualPortfolio = portfolioModel.create("name", PortfolioFormat.INFLEXIBLE, transactions);
  }

  @Test
  public void addTransactions_flexible() throws Exception {
    portfolioModel.create("name", PortfolioFormat.FLEXIBLE, transactions);
    portfolioModel.addTransactions(transactions2);

    List<Transaction> actual = portfolioModel.getPortfolio().getTransactions();
    assertEquals(5, actual.size());
  }

  @Test
  public void addTransactions_flexible_error() throws Exception {
    portfolioModel.create("name", PortfolioFormat.FLEXIBLE, transactions);
    transactions2.add(
        new Transaction(TransactionType.SELL, "AAPL", 10000, LocalDate.parse("2022-10-12"),123));
    try {
      portfolioModel.addTransactions(transactions2);
    } catch (Exception e) {
      assertEquals("Transaction invalid.", e.getMessage());
    }
  }

  @Test
  public void addTransactions_inflexible() throws Exception {
    portfolioModel.create("name", PortfolioFormat.INFLEXIBLE, transactions);
    try {
      portfolioModel.addTransactions(transactions2);
      fail("should fail");
    } catch (Exception e) {
      assertEquals("Portfolio is not modifiable.", e.getMessage());
    }
  }

  @Test
  public void getValue() throws Exception {
    portfolioModel.create("name", PortfolioFormat.FLEXIBLE, transactions);
    PortfolioWithValue portfolioWithValue = portfolioModel.getValue(
        LocalDate.parse("2022-10-10"));

    assertEquals(400.0, portfolioWithValue.getTotalValue(), EPSILON);

    List<PortfolioEntryWithValue> list = portfolioWithValue.getValues();
    assertEquals(1, list.size());
    assertEquals(400, list.get(0).getValue(), EPSILON);
  }

  @Test
  public void getValue_noStock() throws Exception {
    portfolioModel.create("name", PortfolioFormat.FLEXIBLE, transactions);
    PortfolioWithValue portfolioWithValue = portfolioModel.getValue(
        LocalDate.parse("2022-10-09"));

    assertEquals(0.0, portfolioWithValue.getTotalValue(), EPSILON);

    List<PortfolioEntryWithValue> list = portfolioWithValue.getValues();
    assertEquals(0, list.size());
  }

  @Test
  public void getValue_apiFail() throws Exception {
    portfolioModel.create("name", PortfolioFormat.FLEXIBLE, transactions);
    PortfolioWithValue portfolioWithValue = portfolioModel.getValue(
        LocalDate.parse("2022-10-09"));

    assertEquals(0.0, portfolioWithValue.getTotalValue(), EPSILON);

    List<PortfolioEntryWithValue> list = portfolioWithValue.getValues();
    assertEquals(0, list.size());
  }

  @Test
  public void getValues() throws Exception {
    portfolioModel.create("name", PortfolioFormat.FLEXIBLE, transactions);
    Map<LocalDate, Double> portfolioWithValue = portfolioModel.getValues(
        LocalDate.parse("2022-10-10"), LocalDate.parse("2022-10-11"));

    assertEquals(400.0, portfolioWithValue.get(LocalDate.parse("2022-10-10")), EPSILON);
    assertEquals(9900.0, portfolioWithValue.get(LocalDate.parse("2022-10-11")), EPSILON);
  }

  @Test
  public void getPerformance_days() {

  }

  @Test
  public void getPerformance_lengthInsufficient() {

  }

  @Test
  public void getPerformance_week() {

  }

  @Test
  public void getPerformance_month() {

  }

  @Test
  public void getPerformance_quarter() {

  }

  @Test
  public void getPerformance_years() {

  }

  @Test
  public void getPerformance_others() {

  }

}