package portfolio.views.gui;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import portfolio.controllers.InputHandler;
import portfolio.models.entities.PortfolioWithValue;
import portfolio.models.entities.Transaction;
import portfolio.models.portfolio.BuySchedule;
import portfolio.models.portfolio.Portfolio;
import portfolio.views.View;
import portfolio.views.ViewFactory;

/**
 * This is a class that can generate different Swing view, which implement the view factory. It will
 * contain the inputHandler and frame.
 */
public class SwingViewFactory implements ViewFactory {

  private final InputHandler inputHandler;
  private final JFrame frame;

  public SwingViewFactory(JFrame frame, InputHandler inputHandler) {
    this.frame = frame;
    this.inputHandler = inputHandler;
  }

  @Override
  public View newInfoPageView(PortfolioWithValue portfolioWithPrice, Double costOfBasis,
      String errorMessage) {
    return new InfoPageSwingView(frame, inputHandler, portfolioWithPrice, costOfBasis,
        errorMessage);
  }

  @Override
  public View newInflexibleCreatePageView(Boolean isEnd, Boolean isNamed, Map<String, Integer> map,
      String errorMessage) {
    return null;
  }

  @Override
  public View newFlexibleCreatePageView(Boolean isEnd, Boolean isNamed, int stage,
      List<String> inputBuffer,
      List<Transaction> transactions, String errorMessage) {
    return new FlexibleCreatePageSwingView(frame, inputHandler, isEnd, isNamed, stage,
        inputBuffer, transactions,
        errorMessage);
  }

  @Override
  public View newLoadPageView(Portfolio portfolio, boolean showModifyMenu, String errorMessage) {
    return new LoadPageSwingView(frame, inputHandler, portfolio, showModifyMenu, errorMessage);
  }

  @Override
  public View newMainPageView(String errorMessage, boolean isInitFailed) {
    return new MainPageSwingView(frame, inputHandler, errorMessage, isInitFailed);
  }

  @Override
  public View newPerformacePageView(String portfolioName,
      LocalDate startDate,
      LocalDate endDate,
      Map<String, Integer> performance,
      String scale, boolean isFinish, String errorMessage) {
    return new PerformancePageSwingView(frame, inputHandler, portfolioName, startDate, endDate,
        performance, scale, isFinish,
        errorMessage);
  }

  @Override
  public View newScheduleCreatePageView(Map<String, Double> stockList, boolean isEnd,
      List<String> inputBuffer, List<Transaction> transactions, boolean addToPortfolio,
                                        String errorMessage) {
    return new ScheduleCreatePageSwingView(frame, inputHandler, stockList, isEnd, inputBuffer,
        transactions, addToPortfolio, errorMessage);
  }

  @Override
  public View newOneTimeStrategyPageView(Map<String, Double> stockList, boolean isEnd,
      List<String> inputBuffer, String errorMessage) {
    return new OneTimeStrategyPageSwingView(frame, inputHandler, stockList, isEnd, inputBuffer,
        errorMessage);
  }

  @Override
  public View newScheduleInfoPageView(BuySchedule schedule, String errorMessage) {
    return new ScheduleInfoPageSwingView(frame, inputHandler, schedule, errorMessage);
  }

}
