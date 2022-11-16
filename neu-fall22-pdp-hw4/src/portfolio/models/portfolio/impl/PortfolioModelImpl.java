package portfolio.models.portfolio.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import portfolio.models.entities.PortfolioFormat;
import portfolio.models.entities.PortfolioWithValue;
import portfolio.models.entities.StockListEntry;
import portfolio.models.entities.StockPrice;
import portfolio.models.entities.Transaction;
import portfolio.models.entities.TransactionType;
import portfolio.models.portfolio.Portfolio;
import portfolio.models.portfolio.PortfolioParser;
import portfolio.models.portfolio.PortfolioModel;
import portfolio.models.stockprice.StockQueryService;

/**
 * This is a class that represent a portfolio model, which implement the PortfolioModel interface.
 */
public class PortfolioModelImpl implements PortfolioModel {

  private final StockQueryService stockQueryService;
  private final PortfolioParser portfolioParser;
  private Portfolio portfolio = null;

  /**
   * This is a constructor to construct the portfolio model, which will get the stock list and parse
   * the portfolio to given format.
   *
   * @param stockQueryService service for getting stock list
   * @param portfolioParser   parse portfolio
   */
  public PortfolioModelImpl(StockQueryService stockQueryService,
      PortfolioParser portfolioParser) {
    this.stockQueryService = stockQueryService;
    this.portfolioParser = portfolioParser;
  }

  @Override
  public Portfolio getPortfolio() {
    return portfolio;
  }

  @Override
  public Portfolio create(String name, PortfolioFormat format, List<Transaction> transactions)
      throws Exception {

    checkTransactions(format, transactions);
    switch (format) {
      case INFLEXIBLE:
        return new InflexiblePortfolio(name, transactions);
      case FLEXIBLE:
        return new FlexiblePortfolio(name, transactions);
    }
    return portfolio;
  }

  @Override
  public void init() throws Exception {
    portfolio = null;
    stockQueryService.getStockList();
  }

  @Override
  public void load(String name, String text) throws Exception {
    var format = portfolioParser.parseFormat(text);
    portfolio = create(name, format, portfolioParser.parseTransaction(text));
  }

  @Override
  public void checkTransaction(LocalDate date, String symbol) throws Exception {
    stockQueryService.getStockPrice(date, List.of(symbol));
  }

  @Override
  public void checkTransactions(PortfolioFormat format, List<Transaction> transactions)
      throws Exception {
    Map<String, LocalDate> map = new HashMap<>();
    for (var stock : stockQueryService.getStockList()) {
      map.put(stock.getSymbol(), stock.getIpoDate());
    }
    for (var entry : transactions) {
      List<String> symbols = new ArrayList<>();
      symbols.add(entry.getSymbol());
      if (!map.containsKey(entry.getSymbol())) {
        throw new IllegalArgumentException("Symbol [" + entry.getSymbol() + "] not found.");
      }
      if (entry.getDate() != null) {
        stockQueryService.getStockPrice(entry.getDate(), symbols).containsKey(entry.getSymbol());
      }
    }
  }

  @Override
  public void addTransactions(List<Transaction> newTransactions) throws Exception {
    if (portfolio.isReadOnly()) {
      throw new IllegalArgumentException("Portfolio is not modifiable.");
    }

    List<Transaction> transactions = new ArrayList<>(portfolio.getTransactions());
    newTransactions.addAll(transactions);

    // Create same class of portfolio with new set of transactions
    portfolio = portfolio.create(newTransactions);
  }

  @Override
  public PortfolioWithValue getValue(LocalDate date) throws Exception {
    Map<String, StockPrice> prices = stockQueryService.getStockPrice(date,
        portfolio.getSymbols(date));
    return portfolio.getPortfolioWithValue(date, prices);
  }

  @Override
  public double getCostBasis(LocalDate date) throws Exception {
    Map<String, StockPrice> prices = stockQueryService.getStockPrice(date,
        portfolio.getSymbols(date));
    return portfolio.getCostBasis(date, prices);
  }

  @Override
  public Map<LocalDate, Double> getValues(LocalDate from, LocalDate to) {
    Map<LocalDate, Double> map = new HashMap<>();
    for (LocalDate date = from; date.isBefore(to.plusDays(1)); date = date.plusDays(1)) {
      Map<String, StockPrice> prices;
      try {
        prices = stockQueryService.getStockPrice(date, portfolio.getSymbols(date));
        map.put(date, portfolio.getPortfolioWithValue(date, prices).getTotalValue());
      } catch (Exception ignored) {
      }
    }
    return map;
  }

  @Override
  public String getString() throws Exception {
    return portfolioParser.toString(portfolio);
  }
}
