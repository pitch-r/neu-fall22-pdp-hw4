package portfolio.models.entities;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * This is a class that represent a portfolio with the value on a certain date. This class contains
 * the date, a list of stocks for the portfolio and the total value of portfolio on a certain date.
 */
public class PortfolioWithValue {

  private final LocalDate date;
  private final List<PortfolioEntryWithValue> stocks;

  private final double totalValue;

  /**
   * This is a constructor to construct a PortfolioWithValue object. It will have the
   * total price of this portfolio on that date.
   *
   * @param date       the date for check
   * @param stocks     the portfolio stock list
   * @param totalValue the total value of the portfolio
   */
  public PortfolioWithValue(LocalDate date, List<PortfolioEntryWithValue> stocks,
                            double totalValue) {
    this.date = date;
    this.stocks = stocks;
    this.totalValue = totalValue;
  }

  /**
   * Return the date that we want to determine.
   *
   * @return the date that we want to determine
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Return the portfolio entry list, which each entry contains the value of its stock.
   *
   * @return PortfolioEntryWithValue object
   */
  public List<PortfolioEntryWithValue> getValues() {
    return Collections.unmodifiableList(stocks);
  }

  /**
   * Return the total value of this portfolio.
   *
   * @return the total value of this portfolio
   */
  public double getTotalValue() {
    return totalValue;
  }
}
