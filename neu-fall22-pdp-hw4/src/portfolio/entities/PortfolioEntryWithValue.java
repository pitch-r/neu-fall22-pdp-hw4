package portfolio.entities;

/**
 *
 */
public class PortfolioEntryWithValue {
  private final String symbol;
  private final int amount;
  private final double value;

  /**
   *
   * @param portfolioEntry
   * @param value
   */
  public PortfolioEntryWithValue(PortfolioEntry portfolioEntry, double value){
    this.value = value;
    this.symbol = portfolioEntry.getSymbol();
    this.amount = portfolioEntry.getAmount();
  }

  /**
   *
   * @return
   */
  public double getValue() {
    return value;
  }

  /**
   *
   * @return
   */
  public String getSymbol() {
    return symbol;
  }

  /**
   *
   * @return
   */
  public int getAmount() {
    return amount;
  }
}
